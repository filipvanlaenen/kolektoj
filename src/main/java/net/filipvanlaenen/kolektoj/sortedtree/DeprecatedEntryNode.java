package net.filipvanlaenen.kolektoj.sortedtree;

import net.filipvanlaenen.kolektoj.Map.Entry;

/**
 * A class implementing a node in an AVL tree.
 *
 * @param <E> The element type.
 */
final class DeprecatedEntryNode<K, V> extends DeprecatedNode<Entry<K, V>> {
    /**
     * Constructor taking an element as its parameter.
     *
     * @param element The element for this node.
     */
    DeprecatedEntryNode(final Entry<K, V> element) {
        super(element);
    }
}
