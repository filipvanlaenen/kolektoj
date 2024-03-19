package net.filipvanlaenen.kolektoj.sortedtree;

import static net.filipvanlaenen.kolektoj.Collection.ElementCardinality.DISTINCT_ELEMENTS;

import java.util.Comparator;

import net.filipvanlaenen.kolektoj.Collection;
import net.filipvanlaenen.kolektoj.Collection.ElementCardinality;

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

    private int calculateNodeBalanceFactor(Node<K, C> node) {
        return getNodeHeight(node.getRightChild()) - getNodeHeight(node.getLeftChild());
    }

    boolean containsKey(final K key) {
        return containsKey(root, key);
    }

    private boolean containsKey(final Node<K, C> node, final K key) {
        if (node == null) {
            return false;
        }
        int comparison = comparator.compare(key, node.getKey());
        if (comparison < 0) {
            return containsKey(node.getLeftChild(), key);
        } else if (comparison > 0) {
            return containsKey(node.getRightChild(), key);
        } else {
            return true;
        }
    }

    Node<K, C> getNode() {
        return root;
    }

    Node<K, C> getNode(final K key) {
        if (root == null) {
            return null;
        }
        if (comparator.compare(key, root.getKey()) == 0) {
            return root;
        } else {
            return null;
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

    /**
     * Updates the height of this node.
     */
    private void updateNodeHeight(final Node<K, C> node) {
        int leftHeight = getNodeHeight(node.getLeftChild());
        int rightHeight = getNodeHeight(node.getRightChild());
        node.setHeight(Math.max(leftHeight, rightHeight) + 1);
    }
}
