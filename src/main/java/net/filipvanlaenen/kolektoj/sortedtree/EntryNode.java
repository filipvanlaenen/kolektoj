package net.filipvanlaenen.kolektoj.sortedtree;

import net.filipvanlaenen.kolektoj.Map.Entry;

/**
 * A class implementing a node in an AVL tree.
 *
 * @param <E> The element type.
 */
final class EntryNode<K, V> extends Node<Entry<K, V>> {
    /**
     * Constructor taking an element as its parameter.
     *
     * @param element The element for this node.
     */
    EntryNode(final Entry<K, V> element) {
        super(element);
    }
}
