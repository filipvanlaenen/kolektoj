package net.filipvanlaenen.kolektoj.sortedtree;

/**
 * A class implementing a node in an AVL tree.
 *
 * @param <E> The element type.
 */
final class EntryNode<E> extends Node<E> {
    /**
     * Constructor taking an element as its parameter.
     *
     * @param element The element for this node.
     */
    EntryNode(final E element) {
        super(element);
    }
}
