package net.filipvanlaenen.kolektoj.sortedtree;

import static net.filipvanlaenen.kolektoj.Collection.ElementCardinality.DISTINCT_ELEMENTS;

import java.util.Comparator;

import net.filipvanlaenen.kolektoj.Collection;
import net.filipvanlaenen.kolektoj.Collection.ElementCardinality;

final class SortedTreeUtilities {
    static <E extends Comparable<E>> boolean contains(final Node<E> node, final Comparator<E> comparator,
            final E element) {
        if (node == null) {
            return false;
        }
        int comparison = comparator.compare(element, node.getElement());
        if (comparison == 0) {
            return true;
        } else if (comparison < 0) {
            return contains(node.getLeftChild(), comparator, element);
        } else {
            return contains(node.getRightChild(), comparator, element);
        }
    }

    static <E extends Comparable<E>> boolean containsAll(final Node<E> node, final Comparator<E> comparator,
            final int size, final Class<E> componentType, final ElementCardinality elementCardinality,
            final Collection<?> collection) {
        if (collection.size() > size) {
            return false;
        }
        boolean[] matched = new boolean[size];
        for (Object element : collection) {
            if (!(componentType.isInstance(element)
                    && findAndMarkMatch(node, comparator, elementCardinality, matched, 0, (E) element))) {
                return false;
            }
        }
        return true;
    }

    private static <E extends Comparable<E>> boolean findAndMarkMatch(final Node<E> node,
            final Comparator<E> comparator, final ElementCardinality elementCardinality, final boolean[] matched,
            final int index, final E element) {
        if (node == null) {
            return false;
        }
        int comparison = comparator.compare(element, node.getElement());
        if (!matched[index] && comparison == 0) {
            matched[index] = true;
            return true;
        } else if (comparison < 0) {
            return findAndMarkMatch(node.getLeftChild(), comparator, elementCardinality, matched, index + 1, element);
        } else if (comparison > 0) {
            int leftSize = node.getLeftChild() == null ? 0 : node.getLeftChild().getSize();
            return findAndMarkMatch(node.getRightChild(), comparator, elementCardinality, matched, index + leftSize + 1,
                    element);
        } else if (elementCardinality == DISTINCT_ELEMENTS) {
            return false;
        } else if (findAndMarkMatch(node.getLeftChild(), comparator, elementCardinality, matched, index + 1, element)) {
            return true;
        } else {
            int leftSize = node.getLeftChild() == null ? 0 : node.getLeftChild().getSize();
            return findAndMarkMatch(node.getRightChild(), comparator, elementCardinality, matched, index + leftSize + 1,
                    element);
        }
    }
}
