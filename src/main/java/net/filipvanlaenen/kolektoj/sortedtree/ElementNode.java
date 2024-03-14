package net.filipvanlaenen.kolektoj.sortedtree;

/**
 * A class implementing a node in an AVL tree.
 *
 * @param <E> The element type.
 */
final class ElementNode<E> extends Node<E> {
    /**
     * Constructor taking an element as its parameter.
     *
     * @param element The element for this node.
     */
    ElementNode(final E element) {
        super(element);
    }
}
