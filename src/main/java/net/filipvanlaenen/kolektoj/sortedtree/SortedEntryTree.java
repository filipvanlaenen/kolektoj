package net.filipvanlaenen.kolektoj.sortedtree;

import java.util.Comparator;

import net.filipvanlaenen.kolektoj.Collection.ElementCardinality;
import net.filipvanlaenen.kolektoj.Map.Entry;

/**
 * A class implementing an AVL tree.
 *
 * @param <E> The element type.
 */
final class SortedEntryTree<K, V> extends SortedTree<Entry<K, V>, EntryNode<K, V>> {
    /**
     * Creates and empty sorted tree.
     *
     * @param comparator         The comparator.
     * @param elementCardinality The element cardinality.
     * @param elementType        The element type.
     */
    SortedEntryTree(final Comparator<Entry<K, V>> comparator, final ElementCardinality elementCardinality,
            final Class<Entry<K, V>> elementType) {
        this(comparator, elementCardinality, null, 0, elementType);
    }

    private SortedEntryTree(final Comparator<Entry<K, V>> comparator, final ElementCardinality elementCardinality,
            final Node<Entry<K, V>> root, final int size, final Class<Entry<K, V>> elementType) {
        super(comparator, elementCardinality, root, size, elementType);
    }

    boolean add(final Entry<K, V> element) {
        return add(element, new EntryNode<K, V>(element));
    }

    /**
     * Creates a sorted tree from a sorted array from the provided first to last index.
     *
     * @param sortedArray The array with the elements, sorted.
     * @param firstIndex  The first index to be included in the tree.
     * @param lastIndex   The last index to be included in the tree.
     * @return A sorted tree with the elements from the sorted array.
     * @param <E> The element type.
     */
    private static <K, V> Node<Entry<K, V>> createSortedTree(final Entry<K, V>[] sortedArray, final int firstIndex,
            final int lastIndex) {
        int middleIndex = firstIndex + (lastIndex - firstIndex) / 2;
        Node<Entry<K, V>> node = new EntryNode<K, V>(sortedArray[middleIndex]);
        if (middleIndex > firstIndex) {
            node.setLeftChild(createSortedTree(sortedArray, firstIndex, middleIndex - 1));
        }
        if (middleIndex < lastIndex) {
            node.setRightChild(createSortedTree(sortedArray, middleIndex + 1, lastIndex));
        }
        return node;
    }

    static <K, V> SortedEntryTree<K, V> fromSortedArray(final Comparator<Entry<K, V>> comparator,
            final ElementCardinality elementCardinality, final Entry<K, V>[] sortedArray) {
        int size = sortedArray.length;
        return new SortedEntryTree<K, V>(comparator, elementCardinality,
                size == 0 ? null : createSortedTree(sortedArray, 0, size - 1), size,
                (Class<Entry<K, V>>) sortedArray.getClass().getComponentType());
    }
}
