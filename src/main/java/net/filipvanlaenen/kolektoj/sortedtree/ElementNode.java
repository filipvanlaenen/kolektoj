package net.filipvanlaenen.kolektoj.sortedtree;

/**
 * A class implementing an element node in an AVL tree. The element is used as the key to sort the tree.
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
