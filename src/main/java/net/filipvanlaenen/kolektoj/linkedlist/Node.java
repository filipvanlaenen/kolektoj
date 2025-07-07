package net.filipvanlaenen.kolektoj.linkedlist;

/**
 * A class implementing a node in a linked list.
 */
final class Node<E> {
    /**
     * The element of the node.
     */
    private final E element;
    /**
     * The next node in the linked list.
     */
    private Node<E> next;

    /**
     * Constructor taking the element and the next nodes as its parameters.
     *
     * @param element The element of this node.
     * @param next    The next node in the linked list.
     */
    Node(final E element, final Node<E> next) {
        this.element = element;
        this.next = next;
    }

    /**
     * Returns the element of this node.
     *
     * @return The element of this node.
     */
    E getElement() {
        return element;
    }

    /**
     * Returns the next node in the linked list.
     *
     * @return The next node in the linked list.
     */
    Node<E> getNext() {
        return next;
    }

    /**
     * Sets the next node in the linked list.
     *
     * @param next The next node in the linked list.
     */
    void setNext(final Node<E> next) {
        this.next = next;
    }
}