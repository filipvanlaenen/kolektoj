package net.filipvanlaenen.kolektoj.sortedtree;

import java.util.Comparator;

import net.filipvanlaenen.kolektoj.Collection.ElementCardinality;
import net.filipvanlaenen.kolektoj.Map.Entry;

/**
 * A class implementing an AVL tree.
 *
 * @param <E> The element type.
 */
final class DeprecatedSortedEntryTree<K, V> extends DeprecatedSortedTree<Entry<K, V>, DeprecatedEntryNode<K, V>> {
    /**
     * Creates and empty sorted tree.
     *
     * @param comparator         The comparator.
     * @param elementCardinality The element cardinality.
     * @param elementType        The element type.
     */
    DeprecatedSortedEntryTree(final Comparator<Entry<K, V>> comparator, final ElementCardinality elementCardinality,
            final Class<Entry<K, V>> elementType) {
        this(comparator, elementCardinality, null, 0, elementType);
    }

    private DeprecatedSortedEntryTree(final Comparator<Entry<K, V>> comparator, final ElementCardinality elementCardinality,
            final DeprecatedNode<Entry<K, V>> root, final int size, final Class<Entry<K, V>> elementType) {
        super(comparator, elementCardinality, root, size, elementType);
    }

    boolean add(final Entry<K, V> element) {
        return add(element, new DeprecatedEntryNode<K, V>(element));
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
    private static <K, V> DeprecatedNode<Entry<K, V>> createSortedTree(final Entry<K, V>[] sortedArray, final int firstIndex,
            final int lastIndex) {
        int middleIndex = firstIndex + (lastIndex - firstIndex) / 2;
        DeprecatedNode<Entry<K, V>> node = new DeprecatedEntryNode<K, V>(sortedArray[middleIndex]);
        if (middleIndex > firstIndex) {
            node.setLeftChild(createSortedTree(sortedArray, firstIndex, middleIndex - 1));
        }
        if (middleIndex < lastIndex) {
            node.setRightChild(createSortedTree(sortedArray, middleIndex + 1, lastIndex));
        }
        return node;
    }

    static <K, V> DeprecatedSortedEntryTree<K, V> fromSortedArray(final Comparator<Entry<K, V>> comparator,
            final ElementCardinality elementCardinality, final Entry<K, V>[] sortedArray) {
        int size = sortedArray.length;
        return new DeprecatedSortedEntryTree<K, V>(comparator, elementCardinality,
                size == 0 ? null : createSortedTree(sortedArray, 0, size - 1), size,
                (Class<Entry<K, V>>) sortedArray.getClass().getComponentType());
    }
}
