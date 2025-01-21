package net.filipvanlaenen.kolektoj.sortedtree;

import static net.filipvanlaenen.kolektoj.Collection.ElementCardinality.DISTINCT_ELEMENTS;
import static net.filipvanlaenen.kolektoj.Collection.ElementCardinality.DUPLICATE_ELEMENTS;
import static net.filipvanlaenen.kolektoj.Map.KeyAndValueCardinality.DUPLICATE_KEYS_WITH_DUPLICATE_VALUES;

import java.lang.reflect.Array;
import java.util.Comparator;
import java.util.Objects;
import java.util.function.Predicate;

import net.filipvanlaenen.kolektoj.Collection;
import net.filipvanlaenen.kolektoj.ModifiableCollection;
import net.filipvanlaenen.kolektoj.Collection.ElementCardinality;
import net.filipvanlaenen.kolektoj.Map.Entry;
import net.filipvanlaenen.kolektoj.Map.KeyAndValueCardinality;
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
     * The key comparator to sort the nodes of the tree.
     */
    private final Comparator<K> comparator;
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
    private Node<K, C> root;
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
    SortedTree(final Comparator<K> comparator, final ElementCardinality elementCardinality) {
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
        Node<K, C> newNode = new Node<K, C>(key, content);
        int originalSize = size;
        root = insertNodeAndUpdateSize(root, key, newNode);
        updateNodeHeight(root);
        root = rebalanceNode(root);
        return size != originalSize;
    }

    private int addNodesToArray(final Node<K, C>[] array, final Node<K, C> node, final int index) {
        if (node == null) {
            return index;
        }
        int result = addNodesToArray(array, node.getLeftChild(), index);
        array[result++] = node;
        return addNodesToArray(array, node.getRightChild(), result);
    }

    private int calculateNodeBalanceFactor(final Node<K, C> node) {
        return getNodeHeight(node.getRightChild()) - getNodeHeight(node.getLeftChild());
    }

    /**
     * Resets the tree to be empty.
     */
    void clear() {
        root = null;
        size = 0;
    }

    private int collectUnmatchedForRemoval(final Node<K, C>[] removeArray, final int removeArraySize,
            final Node<K, C> node, final boolean[] matched, final int index) {
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
     * Compacts and array with K,V-entries into a new array with K,Collection<V>-entries, collecting entries with the
     * same K together.
     *
     * @param kvEntries An array with the K,V-entries.
     * @return An array with K,Collection<V>-entries.
     */
    static <K, V> Object[] compact(final KeyAndValueCardinality keyAndValueCardinality, final Entry<K, V>[] kvEntries,
            final boolean modifiable) {
        int kvLength = kvEntries.length;
        ElementCardinality cardinality =
                keyAndValueCardinality == DUPLICATE_KEYS_WITH_DUPLICATE_VALUES ? DUPLICATE_ELEMENTS : DISTINCT_ELEMENTS;
        Object[] firstPass = new Object[kvLength];
        int j = -1;
        for (int i = 0; i < kvLength; i++) {
            if (i == 0 || !Objects.equals(((Entry<K, V>) kvEntries[i]).key(),
                    ((Entry<K, ModifiableCollection<V>>) firstPass[j]).key())) {
                j++;
                firstPass[j] = new Entry<K, ModifiableCollection<V>>(((Entry<K, V>) kvEntries[i]).key(),
                        new ModifiableArrayCollection<V>(cardinality));
            }
            ((Entry<K, ModifiableCollection<V>>) firstPass[j]).value().add(((Entry<K, V>) kvEntries[i]).value());
        }
        int resultLength = j + 1;
        Object[] result = new Object[resultLength];
        for (int i = 0; i < resultLength; i++) {
            result[i] = new Entry<K, Collection<V>>(((Entry<K, ModifiableCollection<V>>) firstPass[i]).key(),
                    modifiable
                            ? new ModifiableArrayCollection<V>(
                                    ((Entry<K, ModifiableCollection<V>>) firstPass[i]).value())
                            : new ArrayCollection<V>(((Entry<K, ModifiableCollection<V>>) firstPass[i]).value()));
        }
        return result;
    }

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

    private Node<K, C>[] createNodeArray(final int length, final Node<K, C>... foo) {
        return (Node<K, C>[]) Array.newInstance(getNodeElementType(foo), length);
    }

    private void createEntryNodes(final Object[] sortedArray) {
        size = sortedArray.length;
        if (size > 0) {
            root = createEntryNodes(sortedArray, 0, size - 1);
        }
    }

    private Node<K, C> createEntryNodes(final Object[] sortedArray, final int firstIndex, final int lastIndex) {
        int middleIndex = firstIndex + (lastIndex - firstIndex) / 2;
        Entry<K, C> entry = (Entry<K, C>) sortedArray[middleIndex];
        Node<K, C> node = new Node<K, C>(entry.key(), entry.value());
        if (middleIndex > firstIndex) {
            node.setLeftChild(createEntryNodes(sortedArray, firstIndex, middleIndex - 1));
        }
        if (middleIndex < lastIndex) {
            node.setRightChild(createEntryNodes(sortedArray, middleIndex + 1, lastIndex));
        }
        return node;
    }

    private void createElementNodes(final Object[] sortedArray) {
        size = sortedArray.length;
        if (size > 0) {
            root = createElementNodes(sortedArray, 0, size - 1);
        }
    }

    private Node<K, C> createElementNodes(final Object[] sortedArray, final int firstIndex, final int lastIndex) {
        int middleIndex = firstIndex + (lastIndex - firstIndex) / 2;
        Node<K, C> node = new Node<K, C>((K) sortedArray[middleIndex]);
        if (middleIndex > firstIndex) {
            node.setLeftChild(createElementNodes(sortedArray, firstIndex, middleIndex - 1));
        }
        if (middleIndex < lastIndex) {
            node.setRightChild(createElementNodes(sortedArray, middleIndex + 1, lastIndex));
        }
        return node;
    }

    private Node<K, C> deleteNodeAndUpdateSize(final K element, final Node<K, C> node) {
        if (node == null) {
            return null;
        } else if (comparator.compare(element, node.getKey()) < 0) {
            node.setLeftChild(deleteNodeAndUpdateSize(element, node.getLeftChild()));
            updateNodeHeight(node);
            return node;
        } else if (comparator.compare(element, node.getKey()) > 0) {
            node.setRightChild(deleteNodeAndUpdateSize(element, node.getRightChild()));
            updateNodeHeight(node);
            return node;
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
            Node<K, C> inOrderSuccessor = node.getRightChild().getLeftmostChild();
            node.setKey(inOrderSuccessor.getKey());
            node.setRightChild(deleteNodeAndUpdateSize(inOrderSuccessor.getKey(), node.getRightChild()));
            updateNodeHeight(node);
            return node;
        }
    }

    private boolean findAndMarkMatch(final Node<K, C> node, final boolean[] matched, final int index, final K key) {
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

    static <L> SortedTree<L, L> fromSortedElementArray(final Comparator<L> comparator,
            final ElementCardinality elementCardinality, final Object[] sortedArray) {
        SortedTree<L, L> sortedTree = new SortedTree<L, L>(comparator, elementCardinality);
        sortedTree.createElementNodes(sortedArray);
        return sortedTree;
    }

    static <L, D, W> SortedTree<L, D> fromSortedEntryArray(final Comparator<L> comparator,
            final KeyAndValueCardinality keyAndValueCardinality, final Entry<L, W>[] sortedArray,
            final boolean modifiable) {
        SortedTree<L, D> sortedTree = new SortedTree<L, D>(comparator, DISTINCT_ELEMENTS);
        sortedTree.createEntryNodes(compact(keyAndValueCardinality, sortedArray, modifiable));
        return sortedTree;
    }

    Node<K, C> getAt(final int index) throws IndexOutOfBoundsException {
        if (index >= size) {
            throw new IndexOutOfBoundsException(
                    "Cannot return an element at a position beyond the size of the collection.");
        } else {
            return getAt(root, index);
        }
    }

    private Node<K, C> getAt(final Node<K, C> node, final int index) {
        int leftSize = node.getLeftChild().getSize();
        if (leftSize < index) {
            return getAt(node.getRightChild(), index - leftSize - 1);
        } else if (leftSize == index) {
            return node;
        } else {
            return getAt(node.getLeftChild(), index);
        }
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
     * Returns a node with the given key.
     *
     * @param key The key.
     * @return A node with the given key.
     */
    Node<K, C> getNode(final K key) {
        return getNode(root, key);
    }

    /**
     * Returns a node with the given key starting from the provided node.
     *
     * @param node The start node to search from.
     * @param key  The key.
     * @return A node with the given key.
     */
    private Node<K, C> getNode(final Node<K, C> node, final K key) {
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

    private Class<Node<K, C>> getNodeElementType(final Node<K, C>... foo) {
        return (Class<Node<K, C>>) foo.getClass().getComponentType();
    }

    private int getNodeHeight(final Node<K, C> node) {
        return node == null ? 0 : node.getHeight();
    }

    /**
     * Returns the root node of the tree.
     *
     * @return The root node of the tree.
     */
    Node<K, C> getRootNode() {
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

    private Node<K, C> insertNodeAndUpdateSize(final Node<K, C> node, final K key, final Node<K, C> newNode) {
        if (node == null) {
            size++;
            return newNode;
        } else if (comparator.compare(key, node.getKey()) < 0) {
            node.setLeftChild(insertNodeAndUpdateSize(node.getLeftChild(), key, newNode));
            updateNodeHeight(node);
            return node;
        } else if (comparator.compare(key, node.getKey()) > 0) {
            node.setRightChild(insertNodeAndUpdateSize(node.getRightChild(), key, newNode));
            updateNodeHeight(node);
            return node;
        } else if (elementCardinality == DISTINCT_ELEMENTS) {
            return node;
        } else {
            node.setRightChild(insertNodeAndUpdateSize(node.getRightChild(), key, newNode));
            updateNodeHeight(node);
            return node;
        }
    }

    private int markForRemoval(final Node<K, C>[] deleteArray, final int index, final Node<K, C> node,
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

    private Node<K, C> rebalanceNode(final Node<K, C> node) {
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
        root = deleteNodeAndUpdateSize(key, root);
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
        Node<K, C>[] removeArray = createNodeArray(size);
        int removeArraySize = markForRemoval(removeArray, 0, root, predicate);
        for (int i = 0; i < removeArraySize; i++) {
            remove(removeArray[i].getKey());
        }
        return removeArraySize > 0;
    }

    /**
     * Retains all nodes in a tree with keys in this collection and removes all other, and returns whether it decreased
     * the size of the tree.
     *
     * @param collection A collection with keys for which the nodes should be retained in this tree.
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
        Node<K, C>[] removeArray = createNodeArray(size);
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
    private Node<K, C> rotateLeft(final Node<K, C> node) {
        Node<K, C> originalRightChild = node.getRightChild();
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
    private Node<K, C> rotateRight(final Node<K, C> node) {
        Node<K, C> originalLeftChild = node.getLeftChild();
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
    Node<K, C>[] toArray() {
        Node<K, C>[] array = createNodeArray(size);
        addNodesToArray(array, root, 0);
        return array.clone();
    }

    /**
     * Updates the height of the given node.
     *
     * @param node The node for which the height should be updated.
     */
    private void updateNodeHeight(final Node<K, C> node) {
        int leftHeight = getNodeHeight(node.getLeftChild());
        int rightHeight = getNodeHeight(node.getRightChild());
        node.setHeight(Math.max(leftHeight, rightHeight) + 1);
    }
}
