package net.filipvanlaenen.kolektoj.sortedtree;

/**
 * A class implementing a node in an AVL tree.
 *
 * @param <K> The sorting key type.
 */
abstract class Node<K> {
    /**
     * The height of the node.
     */
    private int height;
    /**
     * The left child containing the nodes having sorting keys that compare less than (or equal to) the sorting key of
     * this node.
     */
    private Node<K> leftChild;
    /**
     * The right child containing the nodes having sorting keys that compare greater than (or equal to) the sorting key
     * of this node.
     */
    private Node<K> rightChild;
    /**
     * The sorting key for the node.
     */
    private K sortingKey;

    /**
     * Constructor taking the node's sorting key as its parameter.
     *
     * @param sortingKey The sorting key for this node.
     */
    Node(final K sortingKey) {
        this.sortingKey = sortingKey;
    }

    /**
     * Calculates the balance factor for this node.
     *
     * @return The balance factor for this node.
     */
    int calculateBalanceFactor() {
        return getHeight(rightChild) - getHeight(leftChild);
    }

    /**
     * Returns the height of this node.
     *
     * @return The height of this node.
     */
    private int getHeight() {
        return height;
    }

    /**
     * Helper method returning -1 if the provided parameter is <code>null</code>, and the height of the node otherwise.
     *
     * @param node The node.
     * @param <L>  The sorting key type.
     * @return The height of the node, or -1 if the provided parameter is <code>null</code>.
     */
    private static <L> int getHeight(final Node<L> node) {
        return node == null ? -1 : node.getHeight();
    }

    /**
     * Returns the left child of this node.
     *
     * @return The left child of this node.
     */
    Node<K> getLeftChild() {
        return leftChild;
    }

    /**
     * Returns the leftmost child by descending as far as possible to the left starting from this node.
     *
     * @return The leftmost child of this node.
     */
    Node<K> getLeftmostChild() {
        return leftChild == null ? this : leftChild.getLeftmostChild();
    }

    /**
     * Returns the right child of this node.
     *
     * @return The right child of this node.
     */
    Node<K> getRightChild() {
        return rightChild;
    }

    /**
     * Returns the size of this node, which is the size of the left and the right child plus one.
     *
     * @return The size of this node.
     */
    int getSize() {
        return 1 + getSize(leftChild) + getSize(rightChild);
    }

    /**
     * Helper method returning 0 if the provided parameter is <code>null</code>, and the size of the node otherwise.
     *
     * @param node The node.
     * @param <F>  The sorting key type.
     * @return The size of the node, or 0 if the provided parameter is <code>null</code>.
     */
    private static <F> int getSize(final Node<F> node) {
        return node == null ? 0 : node.getSize();
    }

    /**
     * Returns the sorting key of this node.
     *
     * @return The sorting key of this node.
     */
    K getSortingKey() {
        return sortingKey;
    }

    /**
     * Rebalances the current node.
     *
     * @return The node that should replace the current node.
     */
    Node<K> rebalance() {
        int balanceFactor = calculateBalanceFactor();
        if (balanceFactor < -1) {
            if (getLeftChild().calculateBalanceFactor() <= 0) {
                return rotateRight();
            } else {
                setLeftChild(getLeftChild().rotateLeft());
                return rotateRight();
            }
        } else if (balanceFactor > 1) {
            if (getRightChild().calculateBalanceFactor() >= 0) {
                return rotateLeft();
            } else {
                setRightChild(getRightChild().rotateRight());
                return rotateLeft();
            }
        }
        return this;
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
    private Node<K> rotateLeft() {
        Node<K> originalRightChild = getRightChild();
        setRightChild(originalRightChild.getLeftChild());
        originalRightChild.setLeftChild(this);
        updateHeight();
        originalRightChild.updateHeight();
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
    private Node<K> rotateRight() {
        Node<K> originalLeftChild = getLeftChild();
        setLeftChild(originalLeftChild.getRightChild());
        originalLeftChild.setRightChild(this);
        updateHeight();
        originalLeftChild.updateHeight();
        return originalLeftChild;
    }

    /**
     * Sets the left child of this node.
     *
     * @param leftChild The left child for this node.
     */
    void setLeftChild(final Node<K> leftChild) {
        this.leftChild = leftChild;
    }

    /**
     * Sets the right child of this node.
     *
     * @param rightChild The right child for this node.
     */
    void setRightChild(final Node<K> rightChild) {
        this.rightChild = rightChild;
    }

    /**
     * Sets the sorting key of this node.
     *
     * @param sortingKey The new sorting key for this node.
     */
    void setSortingKey(final K sortingKey) {
        this.sortingKey = sortingKey;
    }

    /**
     * Updates the height of this node.
     */
    void updateHeight() {
        height = Math.max(getHeight(leftChild), getHeight(rightChild)) + 1;
    }
}
