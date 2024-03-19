package net.filipvanlaenen.kolektoj.sortedtree;

/**
 * A class implementing a node in an AVL tree.
 *
 * @param <K> The sorting key type.
 * @param <C> The content type.
 */
class Node<K, C> {
    private C content;
    private int height;
    private K key;
    private Node<K, C> leftChild;
    private Node<K, C> rightChild;

    Node(final K key) {
        this(key, null);
    }

    Node(final K key, final C content) {
        this.key = key;
        this.content = content;
    }

    C getContent() {
        return content;
    }

    K getKey() {
        return key;
    }

    void setLeftChild(final Node<K, C> node) {
        leftChild = node;
    }

    void setRightChild(final Node<K, C> node) {
        rightChild = node;
    }
}
