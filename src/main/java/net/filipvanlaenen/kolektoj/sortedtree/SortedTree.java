package net.filipvanlaenen.kolektoj.sortedtree;

import static net.filipvanlaenen.kolektoj.Collection.ElementCardinality.DISTINCT_ELEMENTS;

import java.util.Comparator;

import net.filipvanlaenen.kolektoj.Collection;
import net.filipvanlaenen.kolektoj.Collection.ElementCardinality;

final class SortedTree<E extends Comparable<E>> {
    /**
     * The comparator to use for comparing the elements in this collection.
     */
    private final Comparator<E> comparator;
    /**
     * The element cardinality.
     */
    private final ElementCardinality elementCardinality;
    /**
     * The root node of the collection.
     */
    private Node<E> root;
    /**
     * The size of the collection.
     */
    private int size;

    SortedTree(final Comparator<E> comparator, final ElementCardinality elementCardinality) {
        this(comparator, elementCardinality, null, 0);
    }

    private SortedTree(final Comparator<E> comparator, final ElementCardinality elementCardinality, final Node<E> root,
            final int size) {
        this.comparator = comparator;
        this.elementCardinality = elementCardinality;
        this.root = root;
        this.size = size;
    }

    boolean contains(E element) {
        return contains(root, element);
    }

    boolean contains(final Node<E> node, final E element) {
        if (node == null) {
            return false;
        }
        int comparison = comparator.compare(element, node.getElement());
        if (comparison == 0) {
            return true;
        } else if (comparison < 0) {
            return contains(node.getLeftChild(), element);
        } else {
            return contains(node.getRightChild(), element);
        }
    }

    boolean containsAll(final Class<E> componentType, final Collection<?> collection) {
        return containsAll(root, size, componentType, collection);
    }

    boolean containsAll(final Node<E> node, final int size, final Class<E> componentType,
            final Collection<?> collection) {
        if (collection.size() > size) {
            return false;
        }
        boolean[] matched = new boolean[size];
        for (Object element : collection) {
            if (!(componentType.isInstance(element) && findAndMarkMatch(node, matched, 0, (E) element))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Creates a sorted tree from a sorted array from the provided first to last index.
     *
     * @param sortedArray The array with the elements, sorted.
     * @param firstIndex  The first index to be included in the tree.
     * @param lastIndex   The last index to be included in the tree.
     * @return A sorted tree with the elements from the sorted array.
     */
    private static <E extends Comparable<E>> Node<E> createSortedTree(final E[] sortedArray, final int firstIndex,
            final int lastIndex) {
        int middleIndex = firstIndex + (lastIndex - firstIndex) / 2;
        Node<E> node = new Node<E>(sortedArray[middleIndex]);
        if (middleIndex > firstIndex) {
            node.setLeftChild(createSortedTree(sortedArray, firstIndex, middleIndex - 1));
        }
        if (middleIndex < lastIndex) {
            node.setRightChild(createSortedTree(sortedArray, middleIndex + 1, lastIndex));
        }
        return node;
    }

    private boolean findAndMarkMatch(final Node<E> node, final boolean[] matched, final int index, final E element) {
        if (node == null) {
            return false;
        }
        int comparison = comparator.compare(element, node.getElement());
        if (!matched[index] && comparison == 0) {
            matched[index] = true;
            return true;
        } else if (comparison < 0) {
            return findAndMarkMatch(node.getLeftChild(), matched, index + 1, element);
        } else if (comparison > 0) {
            int leftSize = node.getLeftChild() == null ? 0 : node.getLeftChild().getSize();
            return findAndMarkMatch(node.getRightChild(), matched, index + leftSize + 1, element);
        } else if (elementCardinality == DISTINCT_ELEMENTS) {
            return false;
        } else if (findAndMarkMatch(node.getLeftChild(), matched, index + 1, element)) {
            return true;
        } else {
            int leftSize = node.getLeftChild() == null ? 0 : node.getLeftChild().getSize();
            return findAndMarkMatch(node.getRightChild(), matched, index + leftSize + 1, element);
        }
    }

    static <E extends Comparable<E>> SortedTree<E> fromSortedArray(final Comparator<E> comparator,
            final ElementCardinality elementCardinality, final E[] sortedArray) {
        int size = sortedArray.length;
        return new SortedTree<E>(comparator, elementCardinality,
                size == 0 ? null : createSortedTree(sortedArray, 0, size - 1), size);
    }

    public E getRootElement() {
        return root.getElement();
    }
}
