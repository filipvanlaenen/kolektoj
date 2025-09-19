package net.filipvanlaenen.kolektoj.sortedtree;

import static net.filipvanlaenen.kolektoj.Collection.ElementCardinality.DISTINCT_ELEMENTS;
import static net.filipvanlaenen.kolektoj.Collection.ElementCardinality.DUPLICATE_ELEMENTS;
import static net.filipvanlaenen.kolektoj.Map.KeyAndValueCardinality.DUPLICATE_KEYS_WITH_DUPLICATE_VALUES;

import java.lang.reflect.Array;
import java.util.Comparator;
import java.util.Objects;
import java.util.function.Predicate;

import net.filipvanlaenen.kolektoj.Collection;
import net.filipvanlaenen.kolektoj.Collection.ElementCardinality;
import net.filipvanlaenen.kolektoj.Map.Entry;
import net.filipvanlaenen.kolektoj.Map.KeyAndValueCardinality;
import net.filipvanlaenen.kolektoj.ModifiableCollection;
import net.filipvanlaenen.kolektoj.array.ArrayCollection;
import net.filipvanlaenen.kolektoj.array.ModifiableArrayCollection;

/**
 * A class implementing an AVL tree.
 *
 * @param <K> The sorting key type.
 * @param <C> The content type.
 */
class SortedTree<K, C> {
    /**
     * Enumeration describing the direction preference when trying to find an index.
     */
    private enum IndexPreference {
        /**
         * Return any index.
         */
        ANY,
        /**
         * Return the first index.
         */
        FIRST,
        /**
         * Return the last index.
         */
        LAST
    }

    /**
     * The key comparator to sort the nodes of the tree.
     */
    private final Comparator<? super K> comparator;
    /**
     * The cardinality of the elements.
     */
    private final ElementCardinality elementCardinality;
    /**
     * The class of the key.
     */
    private final Class<K> keyClass = getKeyType();
    /**
     * The root node of the tree.
     */
    private TreeNode<K, C> root;
    /**
     * The size of the tree.
     */
    private int size;

    /**
     * Constructor taking the comparator and the element cardinality as its arguments.
     *
     * @param comparator         The comparator to sort the keys.
     * @param elementCardinality The element cardinality.
     */
    SortedTree(final Comparator<? super K> comparator, final ElementCardinality elementCardinality) {
        this.comparator = comparator;
        this.elementCardinality = elementCardinality;
    }

    /**
     * Adds a node to the tree with the given key and content.
     *
     * @param key     The key for the node.
     * @param content The content for the node.
     * @return True if a new node was created and added to the tree.
     */
    boolean add(final K key, final C content) {
        TreeNode<K, C> newNode = new TreeNode<K, C>(key, content);
        int originalSize = size;
        root = insertNodeAndUpdateSize(root, key, newNode);
        updateNodeHeight(root);
        root = rebalanceNode(root);
        return size != originalSize;
    }

    /**
     * Adds the nodes of the subtree defined by the provided node to an array and returns the index where the next node
     * should be added.
     *
     * @param array The array to store the nodes.
     * @param node  The node defining the subtree.
     * @param index The index where the first node can be stored in the array.
     * @return The index where the next node can be stored in the array.
     */
    private int addNodesToArray(final TreeNode<K, C>[] array, final TreeNode<K, C> node, final int index) {
        if (node == null) {
            return index;
        }
        int result = addNodesToArray(array, node.getLeftChild(), index);
        array[result++] = node;
        return addNodesToArray(array, node.getRightChild(), result);
    }

    /**
     * Calculates the balance factor for a node.
     *
     * @param node The node for which to calculate the balance factor.
     * @return The balance factor.
     */
    private int calculateNodeBalanceFactor(final TreeNode<K, C> node) {
        return getNodeHeight(node.getRightChild()) - getNodeHeight(node.getLeftChild());
    }

    /**
     * Resets the tree to be empty.
     */
    void clear() {
        root = null;
        size = 0;
    }

    private int collectUnmatchedForRemoval(final TreeNode<K, C>[] removeArray, final int removeArraySize,
            final TreeNode<K, C> node, final boolean[] matched, final int index) {
        if (node == null) {
            return removeArraySize;
        }
        int result = removeArraySize;
        if (!matched[index]) {
            removeArray[result++] = node;
        }
        result = collectUnmatchedForRemoval(removeArray, result, node.getLeftChild(), matched, index + 1);
        int leftSize = node.getLeftChild() == null ? 0 : node.getLeftChild().getSize();
        result = collectUnmatchedForRemoval(removeArray, result, node.getRightChild(), matched, index + leftSize + 1);
        return result;
    }

    /**
     * Compacts an array with K,V-entries into a new array with K,Collection<V>-entries, collecting entries with the
     * same K together.
     *
     * @param <L>                    The key type.
     * @param <W>                    The value type.
     * @param keyAndValueCardinality The key and value cardinality.
     * @param kvEntries              An array with the K,V-entries.
     * @param modifiable             Whether the collection should be modifiable or not.
     * @return An array with K,Collection<V>-entries.
     */
    static <L, W> Object[] compact(final KeyAndValueCardinality keyAndValueCardinality, final Object[] kvEntries,
            final boolean modifiable) {
        int kvLength = kvEntries.length;
        ElementCardinality cardinality =
                keyAndValueCardinality == DUPLICATE_KEYS_WITH_DUPLICATE_VALUES ? DUPLICATE_ELEMENTS : DISTINCT_ELEMENTS;
        Object[] firstPass = new Object[kvLength];
        int j = -1;
        for (int i = 0; i < kvLength; i++) {
            if (i == 0 || !Objects.equals(((Entry<L, W>) kvEntries[i]).key(),
                    ((Entry<L, ModifiableCollection<W>>) firstPass[j]).key())) {
                j++;
                firstPass[j] = new Entry<L, ModifiableCollection<W>>(((Entry<L, W>) kvEntries[i]).key(),
                        new ModifiableArrayCollection<W>(cardinality));
            }
            ((Entry<L, ModifiableCollection<W>>) firstPass[j]).value().add(((Entry<L, W>) kvEntries[i]).value());
        }
        int resultLength = j + 1;
        Object[] result = new Object[resultLength];
        for (int i = 0; i < resultLength; i++) {
            result[i] = new Entry<L, Collection<W>>(((Entry<L, ModifiableCollection<W>>) firstPass[i]).key(),
                    modifiable
                            ? new ModifiableArrayCollection<W>(
                                    ((Entry<L, ModifiableCollection<W>>) firstPass[i]).value())
                            : new ArrayCollection<W>(((Entry<L, ModifiableCollection<W>>) firstPass[i]).value()));
        }
        return result;
    }

    /**
     * Returns whether all the elements in the provided collection are used as keys.
     *
     * @param collection A collection of keys.
     * @return True if all the elements in the collection are keys and are used as keys in the sorted tree, and false
     *         otherwise.
     */
    boolean containsAllKeys(final Collection<?> collection) {
        if (collection.size() > size) {
            return false;
        }
        boolean[] matched = new boolean[size];
        for (Object key : collection) {
            if (!(keyClass.isInstance(key) && findAndMarkMatch(root, matched, 0, (K) key))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns whether the tree contains a node with the given key.
     *
     * @param key The key.
     * @return True if the tree contains a node with the given key.
     */
    boolean containsKey(final K key) {
        return getNode(key) != null;
    }

    private void createElementNodes(final Object[] sortedArray) {
        size = sortedArray.length;
        if (size > 0) {
            root = createElementNodes(sortedArray, 0, size - 1);
        }
    }

    private TreeNode<K, C> createElementNodes(final Object[] sortedArray, final int firstIndex, final int lastIndex) {
        int middleIndex = firstIndex + (lastIndex - firstIndex) / 2;
        TreeNode<K, C> node = new TreeNode<K, C>((K) sortedArray[middleIndex]);
        if (middleIndex > firstIndex) {
            node.setLeftChild(createElementNodes(sortedArray, firstIndex, middleIndex - 1));
        }
        if (middleIndex < lastIndex) {
            node.setRightChild(createElementNodes(sortedArray, middleIndex + 1, lastIndex));
        }
        return node;
    }

    private void createEntryNodes(final Object[] sortedArray) {
        size = sortedArray.length;
        if (size > 0) {
            root = createEntryNodes(sortedArray, 0, size - 1);
        }
    }

    private TreeNode<K, C> createEntryNodes(final Object[] sortedArray, final int firstIndex, final int lastIndex) {
        int middleIndex = firstIndex + (lastIndex - firstIndex) / 2;
        Entry<K, C> entry = (Entry<K, C>) sortedArray[middleIndex];
        TreeNode<K, C> node = new TreeNode<K, C>(entry.key(), entry.value());
        if (middleIndex > firstIndex) {
            node.setLeftChild(createEntryNodes(sortedArray, firstIndex, middleIndex - 1));
        }
        if (middleIndex < lastIndex) {
            node.setRightChild(createEntryNodes(sortedArray, middleIndex + 1, lastIndex));
        }
        return node;
    }

    private TreeNode<K, C>[] createNodeArray(final int length, final TreeNode<K, C>... foo) {
        return (TreeNode<K, C>[]) Array.newInstance(getNodeElementType(foo), length);
    }

    private boolean findAndMarkMatch(final TreeNode<K, C> node, final boolean[] matched, final int index, final K key) {
        if (node == null) {
            return false;
        }
        int comparison = comparator.compare(key, node.getKey());
        if (!matched[index] && comparison == 0) {
            matched[index] = true;
            return true;
        } else if (comparison < 0) {
            return findAndMarkMatch(node.getLeftChild(), matched, index + 1, key);
        } else if (comparison > 0) {
            int leftSize = node.getLeftChild() == null ? 0 : node.getLeftChild().getSize();
            return findAndMarkMatch(node.getRightChild(), matched, index + leftSize + 1, key);
        } else if (elementCardinality == DISTINCT_ELEMENTS) {
            return false;
        } else if (findAndMarkMatch(node.getLeftChild(), matched, index + 1, key)) {
            return true;
        } else {
            int leftSize = node.getLeftChild() == null ? 0 : node.getLeftChild().getSize();
            return findAndMarkMatch(node.getRightChild(), matched, index + leftSize + 1, key);
        }
    }

    static <L> SortedTree<L, L> fromSortedElementArray(final Comparator<? super L> comparator,
            final ElementCardinality elementCardinality, final Object[] sortedArray) {
        SortedTree<L, L> sortedTree = new SortedTree<L, L>(comparator, elementCardinality);
        sortedTree.createElementNodes(sortedArray);
        return sortedTree;
    }

    static <L, D, W> SortedTree<L, D> fromSortedEntryArray(final Comparator<? super L> comparator,
            final KeyAndValueCardinality keyAndValueCardinality, final Object[] sortedArray, final boolean modifiable) {
        SortedTree<L, D> sortedTree = new SortedTree<L, D>(comparator, DISTINCT_ELEMENTS);
        sortedTree.createEntryNodes(compact(keyAndValueCardinality, sortedArray, modifiable));
        return sortedTree;
    }

    /**
     * Returns the index for the first occurrence of the key in the sorted tree, or -1 if the sorted tree doesn't
     * contain the key.
     *
     * @param key The key for which the first index should be found.
     * @return The index for the first occurrence of the key, or -1 if the sorted tree doesn't contain it.
     */
    int firstIndexOf(final K key) {
        return indexOf(key, root, 0, IndexPreference.FIRST);
    }

    /**
     * Returns this tree's node at a given index.
     *
     * @param index The index.
     * @return The node at the index.
     * @throws IndexOutOfBoundsException If the index is larger or equal than the size.
     */
    TreeNode<K, C> getAt(final int index) throws IndexOutOfBoundsException {
        if (index >= size) {
            throw new IndexOutOfBoundsException("Cannot return an element at a position beyond the size of the tree.");
        } else {
            return getAt(root, index);
        }
    }

    /**
     * Returns the node at a given index for the subtree starting at the given node.
     *
     * @param node  The node defining the subtree.
     * @param index The index.
     * @return The node at the index.
     */
    private TreeNode<K, C> getAt(final TreeNode<K, C> node, final int index) {
        TreeNode<K, C> leftChild = node.getLeftChild();
        int leftSize = leftChild == null ? 0 : leftChild.getSize();
        if (leftSize < index) {
            return getAt(node.getRightChild(), index - leftSize - 1);
        } else if (leftSize == index) {
            return node;
        } else {
            return getAt(leftChild, index);
        }
    }

    /**
     * Returns the tree's greatest (rightmost) node.
     *
     * @return The tree's greatest (rightmost) node.
     */
    TreeNode<K, C> getGreatest() {
        return root.getRightmostChild();
    }

    /**
     * Returns the height of the tree.
     *
     * @return The height of the tree.
     */
    int getHeight() {
        return getNodeHeight(root);
    }

    /**
     * Returns a class object with the class for the keys.
     *
     * @param keys A variable argument array with the key type as its component type.
     * @return A class object with the class for the keys.
     */
    private Class<K> getKeyType(final K... keys) {
        return (Class<K>) keys.getClass().getComponentType();
    }

    /**
     * Returns the tree's least (leftmost) node.
     *
     * @return The tree's least (leftmost) node.
     */
    TreeNode<K, C> getLeast() {
        return root.getLeftmostChild();
    }

    /**
     * Returns a node with the given key.
     *
     * @param key The key.
     * @return A node with the given key.
     */
    TreeNode<K, C> getNode(final K key) {
        return getNode(root, key);
    }

    /**
     * Returns a node with the given key (or an equivalent key according to the comparator) starting from the provided
     * node.
     *
     * @param node The start node to search from.
     * @param key  The key.
     * @return A node with the given key, or an equivalent one according to the comparator.
     */
    private TreeNode<K, C> getNode(final TreeNode<K, C> node, final K key) {
        if (node == null) {
            return null;
        }
        int comparison = comparator.compare(key, node.getKey());
        if (comparison < 0) {
            return getNode(node.getLeftChild(), key);
        } else if (comparison > 0) {
            return getNode(node.getRightChild(), key);
        } else {
            return node;
        }
    }

    private Class<TreeNode<K, C>> getNodeElementType(final TreeNode<K, C>... node) {
        return (Class<TreeNode<K, C>>) node.getClass().getComponentType();
    }

    /**
     * Returns the height for the subtree defined by the provided node. If the node is <code>null</code>, 0 is returned.
     *
     * @param node The node for which to calculate the height.
     * @return The height of the subtree defined by the provided node, or 0 if it's <code>null</code>.
     */
    private int getNodeHeight(final TreeNode<K, C> node) {
        return node == null ? 0 : node.getHeight();
    }

    /**
     * Returns the root node of the tree.
     *
     * @return The root node of the tree.
     */
    TreeNode<K, C> getRootNode() {
        return root;
    }

    /**
     * Returns the size of the tree.
     *
     * @return The size of the tree.
     */
    int getSize() {
        return size;
    }

    /**
     * Returns the index for an occurrence of the key (or an equivalent one according to the comparator) in the sorted
     * tree, or -1 if the sorted tree doesn't contain the key.
     *
     * @param key The key for which an index should be found.
     * @return The index for an occurrence of the key (or an equivalent one according to the comparator), or -1 if the
     *         sorted tree doesn't contain it.
     */
    int indexOf(final K key) {
        return indexOf(key, root, 0, IndexPreference.ANY);
    }

    /**
     * Returns the index for an occurrence of the key in the sorted tree, searching in the subtree defined by the node,
     * or -1 if the subtree doesn't contain the key.
     *
     * @param key             The key for which an index should be found.
     * @param node            The node defining the subtree in which the key should be found.
     * @param sizeBefore      The size of the tree before the subtree defined by the node.
     * @param indexPreference The direction preference for the index.
     * @return The index for an occurrence of the key, or -1 if the sorted tree doesn't contain it.
     */
    private int indexOf(final K key, final TreeNode<K, C> node, final int sizeBefore,
            final IndexPreference indexPreference) {
        if (node == null) {
            return -1;
        }
        int comparison = comparator.compare(key, node.getKey());
        TreeNode<K, C> leftChild = node.getLeftChild();
        int leftSize = leftChild == null ? 0 : leftChild.getSize();
        if (comparison < 0) {
            return indexOf(key, leftChild, sizeBefore, indexPreference);
        } else if (comparison > 0) {
            return indexOf(key, node.getRightChild(), sizeBefore + leftSize + 1, indexPreference);
        } else {
            if (indexPreference == IndexPreference.ANY) {
                if (Objects.equals(key, node.getKey())) {
                    return sizeBefore + leftSize;
                } else {
                    int lowerIndex = indexOf(key, leftChild, sizeBefore, indexPreference);
                    if (lowerIndex != -1) {
                        return lowerIndex;
                    } else {
                        return indexOf(key, node.getRightChild(), sizeBefore + leftSize + 1, indexPreference);
                    }
                }
            } else if (indexPreference == IndexPreference.LAST) {
                int upperIndex = indexOf(key, node.getRightChild(), sizeBefore + leftSize + 1, indexPreference);
                if (upperIndex != -1) {
                    return upperIndex;
                } else if (Objects.equals(key, node.getKey())) {
                    return sizeBefore + leftSize;
                } else {
                    return indexOf(key, leftChild, sizeBefore, indexPreference);
                }
            } else {
                int lowerIndex = indexOf(key, leftChild, sizeBefore, indexPreference);
                if (lowerIndex != -1) {
                    return lowerIndex;
                } else if (Objects.equals(key, node.getKey())) {
                    return sizeBefore + leftSize;
                } else {
                    return indexOf(key, node.getRightChild(), sizeBefore + leftSize + 1, indexPreference);
                }
            }
        }
    }

    /**
     * Inserts a node into a subtree, updates the size of the entire tree, and rebalances the subtree.
     *
     * @param node    The node defining the subtree.
     * @param key     The key of the node to be inserted.
     * @param newNode The node to be inserted.
     * @return The new root node for the subtree.
     */
    private TreeNode<K, C> insertNodeAndUpdateSize(final TreeNode<K, C> node, final K key,
            final TreeNode<K, C> newNode) {
        if (node == null) {
            size++;
            return newNode;
        } else if (comparator.compare(key, node.getKey()) < 0) {
            node.setLeftChild(insertNodeAndUpdateSize(node.getLeftChild(), key, newNode));
            updateNodeHeight(node);
            return rebalanceNode(node);
        } else if (comparator.compare(key, node.getKey()) > 0) {
            node.setRightChild(insertNodeAndUpdateSize(node.getRightChild(), key, newNode));
            updateNodeHeight(node);
            return rebalanceNode(node);
        } else if (elementCardinality == DISTINCT_ELEMENTS) {
            return node;
        } else {
            node.setRightChild(insertNodeAndUpdateSize(node.getRightChild(), key, newNode));
            updateNodeHeight(node);
            return rebalanceNode(node);
        }
    }

    /**
     * Returns the index for the last occurrence of the key in the sorted tree, or -1 if the sorted tree doesn't contain
     * the key.
     *
     * @param key The key for which the last index should be found.
     * @return The index for the last occurrence of the key, or -1 if the sorted tree doesn't contain it.
     */
    int lastIndexOf(final K key) {
        return indexOf(key, root, 0, IndexPreference.LAST);
    }

    private int markForRemoval(final TreeNode<K, C>[] deleteArray, final int index, final TreeNode<K, C> node,
            final Predicate<? super K> predicate) {
        if (node == null) {
            return index;
        }
        int newIndex = index;
        if (predicate.test(node.getKey())) {
            deleteArray[newIndex++] = node;
        }
        newIndex = markForRemoval(deleteArray, newIndex, node.getLeftChild(), predicate);
        newIndex = markForRemoval(deleteArray, newIndex, node.getRightChild(), predicate);
        return newIndex;
    }

    /**
     * Rebalances a subtree starting from a note, and returns the new root element for that subtree.
     *
     * @param node The node defining the subtree to rebalance.
     * @return The new root element for the subtree.
     */
    private TreeNode<K, C> rebalanceNode(final TreeNode<K, C> node) {
        int balanceFactor = calculateNodeBalanceFactor(node);
        if (balanceFactor < -1) {
            if (calculateNodeBalanceFactor(node.getLeftChild()) <= 0) {
                return rotateRight(node);
            } else {
                node.setLeftChild(rotateLeft(node.getLeftChild()));
                return rotateRight(node);
            }
        } else if (balanceFactor > 1) {
            if (calculateNodeBalanceFactor(node.getRightChild()) >= 0) {
                return rotateLeft(node);
            } else {
                node.setRightChild(rotateRight(node.getRightChild()));
                return rotateLeft(node);
            }
        }
        return node;
    }

    /**
     * Removes a node with the given key from the tree.
     *
     * @param key The key.
     * @return True if a node was removed.
     */
    boolean remove(final K key) {
        if (root == null) {
            return false;
        }
        int originalSize = size;
        root = removeKey(root, key);
        if (root != null) {
            updateNodeHeight(root);
            root = rebalanceNode(root);
        }
        return size != originalSize;
    }

    /**
     * Removes all nodes from this tree with keys that satisfy the given predicate, and returns whether it decreased the
     * size of the tree.
     *
     * @param predicate The predicate to be applied to each key in the tree.
     * @return True if the size of the tree decreased after removing the nodes with keys that satisfied the given
     *         predicate.
     */
    boolean removeIf(final Predicate<? super K> predicate) {
        TreeNode<K, C>[] removeArray = createNodeArray(size);
        int removeArraySize = markForRemoval(removeArray, 0, root, predicate);
        for (int i = 0; i < removeArraySize; i++) {
            remove(removeArray[i].getKey());
        }
        return removeArraySize > 0;
    }

    /**
     * Deletes a node with the given key within the subtree defined by the given node, rebalances the subtree and
     * updates its size, and returns the new root node for the subtree.
     *
     * @param node The node defining the subtree.
     * @param key  The key for which the node should be deleted.
     *
     * @return The new root node for the subtree.
     */
    private TreeNode<K, C> removeKey(final TreeNode<K, C> node, final K key) {
        if (node == null) {
            return null;
        } else if (comparator.compare(key, node.getKey()) < 0) {
            node.setLeftChild(removeKey(node.getLeftChild(), key));
            updateNodeHeight(node);
            return rebalanceNode(node);
        } else if (comparator.compare(key, node.getKey()) > 0) {
            node.setRightChild(removeKey(node.getRightChild(), key));
            updateNodeHeight(node);
            return rebalanceNode(node);
        } else if (node.getLeftChild() == null && node.getRightChild() == null) {
            size--;
            return null;
        } else if (node.getLeftChild() == null) {
            size--;
            return node.getRightChild();
        } else if (node.getRightChild() == null) {
            size--;
            return node.getLeftChild();
        } else {
            TreeNode<K, C> inOrderSuccessor = node.getRightChild().getLeftmostChild();
            node.setKey(inOrderSuccessor.getKey());
            node.setRightChild(removeKey(node.getRightChild(), inOrderSuccessor.getKey()));
            updateNodeHeight(node);
            return rebalanceNode(node);
        }
    }

    /**
     * Retains all nodes in a tree with keys in this collection and removes all other, and returns whether it decreased
     * the size of the tree.
     *
     * @param keys A collection with keys for which the nodes should be retained in this tree.
     * @return True if the size of the tree decreased after retaining only the nodes with keys present in the provided
     *         collection.
     */
    boolean retainAllKeys(final Collection<? extends K> keys) {
        boolean[] matched = new boolean[size];
        for (Object element : keys) {
            if (keyClass.isInstance(element)) {
                findAndMarkMatch(root, matched, 0, (K) element);
            }
        }
        TreeNode<K, C>[] removeArray = createNodeArray(size);
        int removeArraySize = collectUnmatchedForRemoval(removeArray, 0, root, matched, 0);
        for (int i = 0; i < removeArraySize; i++) {
            remove(removeArray[i].getKey());
        }
        return removeArraySize > 0;
    }

    /**
     * Rotates the tree at this node to the left.
     *
     * This means that the tree as outlined in the diagram below:
     *
     * <PRE>
     *             this
     *   leftChild                  rightChild
     *                  middleChild            farRightChild
     * </PRE>
     *
     * is transformed into the following tree:
     *
     * <PRE>
     *                              rightChild
     *             this                        farRightChild
     *   leftChild      middleChild
     * </PRE>
     *
     * @param node The node to rotate around.
     * @return The right child that should replace the current node.
     */
    private TreeNode<K, C> rotateLeft(final TreeNode<K, C> node) {
        TreeNode<K, C> originalRightChild = node.getRightChild();
        node.setRightChild(originalRightChild.getLeftChild());
        originalRightChild.setLeftChild(node);
        updateNodeHeight(node);
        updateNodeHeight(originalRightChild);
        return originalRightChild;
    }

    /**
     * Rotates the tree at this node to the right.
     *
     * This means that the tree as outlined in the diagram below:
     *
     * <PRE>
     *                                      this
     *                leftChild                  rightChild
     *   farLeftChild           middleChild
     * </PRE>
     *
     * is transformed into the following tree:
     *
     * <PRE>
     *                leftChild
     *   farLeftChild                       this
     *                          middleChild      rightChild
     * </PRE>
     *
     * @param node The node to rotate around.
     * @return The left child that should replace the current node.
     */
    private TreeNode<K, C> rotateRight(final TreeNode<K, C> node) {
        TreeNode<K, C> originalLeftChild = node.getLeftChild();
        node.setLeftChild(originalLeftChild.getRightChild());
        originalLeftChild.setRightChild(node);
        updateNodeHeight(node);
        updateNodeHeight(originalLeftChild);
        return originalLeftChild;
    }

    /**
     * Returns the content of this tree as an array.
     *
     * @return An array containing the elements of this tree.
     */
    TreeNode<K, C>[] toArray() {
        TreeNode<K, C>[] array = createNodeArray(size);
        addNodesToArray(array, root, 0);
        return array.clone();
    }

    /**
     * Uncompacts an array with L,Collection<W>-entries into a new array with L,W-entries.
     *
     * @param <L>            The key type.
     * @param <W>            The value type.
     * @param <D>            The value collection type.
     * @param compactedArray An array with the L,Collection<W>-entries.
     * @param array          An array to put the L,W-entries in, or to use as the prototype for cloning.
     * @return An array with L,W-entries.
     */
    static <L, W, D extends Collection<W>> Object[] uncompact(final TreeNode<L, D>[] compactedArray,
            final Object[] array) {
        int size = 0;
        for (TreeNode<L, D> node : compactedArray) {
            size += node.getContent().size();
        }
        Object[] result = array.length == size ? array : new Object[size];
        int i = 0;
        for (TreeNode<L, D> node : compactedArray) {
            L key = node.getKey();
            for (W value : node.getContent()) {
                result[i] = new Entry<L, W>(key, value);
                i++;
            }
        }
        return result;
    }

    /**
     * Updates the height of the given node.
     *
     * @param node The node for which the height should be updated.
     */
    private void updateNodeHeight(final TreeNode<K, C> node) {
        int leftHeight = getNodeHeight(node.getLeftChild());
        int rightHeight = getNodeHeight(node.getRightChild());
        node.setHeight(Math.max(leftHeight, rightHeight) + 1);
    }
}
