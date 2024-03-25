package net.filipvanlaenen.kolektoj.sortedtree;

import static net.filipvanlaenen.kolektoj.Collection.ElementCardinality.DISTINCT_ELEMENTS;

import java.lang.reflect.Array;
import java.util.Comparator;

import net.filipvanlaenen.kolektoj.ModifiableCollection;
import net.filipvanlaenen.kolektoj.Collection.ElementCardinality;
import net.filipvanlaenen.kolektoj.Map.Entry;
import net.filipvanlaenen.kolektoj.Map.KeyAndValueCardinality;

/**
 * A class implementing an AVL tree.
 *
 * @param <K> The sorting key type.
 * @param <C> The content type.
 */
class SortedTree<K, C> {
    private final Comparator<K> comparator;
    private final ElementCardinality elementCardinality;
    private Node<K, C> root;
    /**
     * The size of the tree.
     */
    private int size;

    SortedTree(Comparator<K> comparator, final ElementCardinality elementCardinality) {
        this.comparator = comparator;
        this.elementCardinality = elementCardinality;
    }

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

    private int calculateNodeBalanceFactor(Node<K, C> node) {
        return getNodeHeight(node.getRightChild()) - getNodeHeight(node.getLeftChild());
    }

    void clear() {
        root = null;
        size = 0;
    }

    boolean containsKey(final K key) {
        return getNode(key) != null;
    }

    private void createNodes(Entry<K, C>[] sortedArray) {
        size = sortedArray.length;
        if (size > 0) {
            root = createNodes(sortedArray, 0, size - 1);
        }
    }

    private void createNodes(final K[] sortedArray) {
        size = sortedArray.length;
        if (size > 0) {
            root = createNodes(sortedArray, 0, size - 1);
        }
    }

    private Node<K, C> createNodes(final Entry<K, C>[] sortedArray, final int firstIndex, final int lastIndex) {
        int middleIndex = firstIndex + (lastIndex - firstIndex) / 2;
        Entry<K, C> entry = sortedArray[middleIndex];
        Node<K, C> node = new Node<K, C>(entry.key(), entry.value());
        if (middleIndex > firstIndex) {
            node.setLeftChild(createNodes(sortedArray, firstIndex, middleIndex - 1));
        }
        if (middleIndex < lastIndex) {
            node.setRightChild(createNodes(sortedArray, middleIndex + 1, lastIndex));
        }
        return node;
    }

    private Node<K, C> createNodes(final K[] sortedArray, final int firstIndex, final int lastIndex) {
        int middleIndex = firstIndex + (lastIndex - firstIndex) / 2;
        Node<K, C> node = new Node<K, C>(sortedArray[middleIndex]);
        if (middleIndex > firstIndex) {
            node.setLeftChild(createNodes(sortedArray, firstIndex, middleIndex - 1));
        }
        if (middleIndex < lastIndex) {
            node.setRightChild(createNodes(sortedArray, middleIndex + 1, lastIndex));
        }
        return node;
    }

    static <K, C> SortedTree<K, C> fromSortedArray(final Comparator<K> comparator,
            final KeyAndValueCardinality keyAndValueCardinality, final Entry<K, C>[] sortedArray) {
        SortedTree<K, C> sortedTree = new SortedTree<K, C>(comparator, DISTINCT_ELEMENTS);
        sortedTree.createNodes(sortedArray);
        return sortedTree;
    }

    static <K> SortedTree<K, K> fromSortedArray(final Comparator<K> comparator,
            final ElementCardinality elementCardinality, final K[] sortedArray) {
        SortedTree<K, K> sortedTree = new SortedTree<K, K>(comparator, elementCardinality);
        sortedTree.createNodes(sortedArray);
        return sortedTree;
    }

    Node<K, C> getRootNode() {
        return root;
    }

    Node<K, C> getNode(final K key) {
        return getNode(root, key);
    }

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

    private int getNodeHeight(final Node<K, C> node) {
        return node == null ? 0 : node.getHeight();
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

    private Node<K, C>[] createNodeArray(final int length, Node<K, C>... foo) {
        Class<Node<K, C>> elementType = (Class<Node<K, C>>) foo.getClass().getComponentType();
        return (Node<K, C>[]) Array.newInstance(elementType, length);
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
     * Updates the height of this node.
     */
    private void updateNodeHeight(final Node<K, C> node) {
        int leftHeight = getNodeHeight(node.getLeftChild());
        int rightHeight = getNodeHeight(node.getRightChild());
        node.setHeight(Math.max(leftHeight, rightHeight) + 1);
    }

    int getSize() {
        return size;
    }
}
