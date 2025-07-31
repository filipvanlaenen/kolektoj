package net.filipvanlaenen.kolektoj.sortedtree;

/**
 * A class implementing a node in an AVL tree.
 *
 * @param <K> The sorting key type.
 * @param <C> The content type.
 */
class Node<K, C> {
    /**
     * The content of the node.
     */
    private C content;
    /**
     * The height of the node.
     */
    private int height;
    /**
     * The key of the node.
     */
    private K key;
    /**
     * The left child of the node.
     */
    private Node<K, C> leftChild;
    /**
     * The right child of the node.
     */
    private Node<K, C> rightChild;

    /**
     * Constructor taking the key as its parameter. Sets the content to <code>null</code>.
     *
     * @param key The key for the node.
     */
    Node(final K key) {
        this(key, null);
    }

    /**
     * Constructor taking the key and the content as its parameters.
     *
     * @param key     The key for the node.
     * @param content The content for the node.
     */
    Node(final K key, final C content) {
        this.key = key;
        this.content = content;
    }

    /**
     * Returns the content of the node.
     *
     * @return The content of the node.
     */
    C getContent() {
        return content;
    }

    /**
     * Returns the height of the node.
     *
     * @return The height of the node.
     */
    int getHeight() {
        return height;
    }

    /**
     * Returns the key of the node.
     *
     * @return The key of the node.
     */
    K getKey() {
        return key;
    }

    /**
     * Returns the left child of the node.
     *
     * @return The left child of the node.
     */
    Node<K, C> getLeftChild() {
        return leftChild;
    }

    /**
     * Returns the leftmost child of the node. If the node has no left child, the node itself will be returned.
     *
     * @return The leftmost child of the node, or the node itself if it has no left child.
     */
    Node<K, C> getLeftmostChild() {
        return leftChild == null ? this : leftChild.getLeftmostChild();
    }

    /**
     * Returns the right child of the node.
     *
     * @return The right child of the node.
     */
    Node<K, C> getRightChild() {
        return rightChild;
    }

    /**
     * Returns the rightmost child of the node. If the node has no right child, the node itself will be returned.
     *
     * @return The rightmost child of the node, or the node itself if it has no right child.
     */
    Node<K, C> getRightmostChild() {
        return rightChild == null ? this : rightChild.getRightmostChild();
    }

    /**
     * Returns the size of the node.
     *
     * @return The size of the node.
     */
    int getSize() {
        return 1 + (leftChild == null ? 0 : leftChild.getSize()) + (rightChild == null ? 0 : rightChild.getSize());
    }

    /**
     * Sets the height of the node.
     *
     * @param height The height of the node.
     */
    void setHeight(final int height) {
        this.height = height;
    }

    /**
     * Sets the key of the node.
     *
     * @param key The key of the node.
     */
    void setKey(final K key) {
        this.key = key;
    }

    /**
     * Sets the left child of the node.
     *
     * @param leftChild The left child of the node.
     */
    void setLeftChild(final Node<K, C> leftChild) {
        this.leftChild = leftChild;
    }

    /**
     * Sets the right child of the node.
     *
     * @param rightChild The right child of the node.
     */
    void setRightChild(final Node<K, C> rightChild) {
        this.rightChild = rightChild;
    }
}
