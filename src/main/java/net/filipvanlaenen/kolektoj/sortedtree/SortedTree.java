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

    SortedTree(final Comparator<E> comparator, final ElementCardinality elementCardinality) {
        this.comparator = comparator;
        this.elementCardinality = elementCardinality;
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
}
