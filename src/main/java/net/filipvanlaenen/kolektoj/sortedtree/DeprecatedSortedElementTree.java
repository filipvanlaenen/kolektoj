package net.filipvanlaenen.kolektoj.sortedtree;

import java.util.Comparator;

import net.filipvanlaenen.kolektoj.Collection.ElementCardinality;

/**
 * A class implementing an AVL tree.
 *
 * @param <E> The element type.
 */
final class DeprecatedSortedElementTree<E> extends DeprecatedSortedTree<E, DeprecatedElementNode<E>> {
    /**
     * Creates and empty sorted tree.
     *
     * @param comparator         The comparator.
     * @param elementCardinality The element cardinality.
     * @param elementType        The element type.
     */
    DeprecatedSortedElementTree(final Comparator<E> comparator, final ElementCardinality elementCardinality,
            final Class<E> elementType) {
        this(comparator, elementCardinality, null, 0, elementType);
    }

    private DeprecatedSortedElementTree(final Comparator<E> comparator, final ElementCardinality elementCardinality,
            final DeprecatedNode<E> root, final int size, final Class<E> elementType) {
        super(comparator, elementCardinality, root, size, elementType);
    }

    boolean add(final E element) {
        return add(element, new DeprecatedElementNode<E>(element));
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
    private static <E> DeprecatedNode<E> createSortedTree(final E[] sortedArray, final int firstIndex, final int lastIndex) {
        int middleIndex = firstIndex + (lastIndex - firstIndex) / 2;
        DeprecatedNode<E> node = new DeprecatedElementNode<E>(sortedArray[middleIndex]);
        if (middleIndex > firstIndex) {
            node.setLeftChild(createSortedTree(sortedArray, firstIndex, middleIndex - 1));
        }
        if (middleIndex < lastIndex) {
            node.setRightChild(createSortedTree(sortedArray, middleIndex + 1, lastIndex));
        }
        return node;
    }

    static <E> DeprecatedSortedElementTree<E> fromSortedArray(final Comparator<E> comparator,
            final ElementCardinality elementCardinality, final E[] sortedArray) {
        int size = sortedArray.length;
        return new DeprecatedSortedElementTree<E>(comparator, elementCardinality,
                size == 0 ? null : createSortedTree(sortedArray, 0, size - 1), size,
                (Class<E>) sortedArray.getClass().getComponentType());
    }
}
