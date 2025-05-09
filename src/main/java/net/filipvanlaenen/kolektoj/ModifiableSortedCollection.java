package net.filipvanlaenen.kolektoj;

import java.util.Comparator;

import net.filipvanlaenen.kolektoj.sortedtree.ModifiableSortedTreeCollection;

/**
 * Interface defining the signature for all modifiable sorted collections.
 *
 * @param <E> The element type.
 */
public interface ModifiableSortedCollection<E> extends ModifiableCollection<E>, SortedCollection<E> {
    /**
     * Returns a new empty modifiable sorted collection.
     *
     * @param <E>        The element type.
     * @param comparator The comparator by which to sort the elements.
     * @return A new empty modifiable sorted ordered collection.
     */
    static <E> ModifiableSortedCollection<E> empty(final Comparator<E> comparator) {
        return new ModifiableSortedTreeCollection<E>(comparator);
    }

    /**
     * Returns a new modifiable sorted collection with the specified elements.
     *
     * @param <E>        The element type.
     * @param comparator The comparator by which to sort the elements.
     * @param elements   The elements for the new modifiable sorted collection.
     * @return A new modifiable sorted collection with the specified elements.
     */
    static <E> ModifiableSortedCollection<E> of(final Comparator<E> comparator, final E... elements) {
        return new ModifiableSortedTreeCollection<E>(comparator, elements);
    }

    /**
     * Returns a new modifiable sorted collection with the specified element cardinality and the elements.
     *
     * @param <E>                The element type.
     * @param elementCardinality The element cardinality.
     * @param comparator         The comparator by which to sort the elements.
     * @param elements           The elements for the new modifiable sorted collection.
     * @return A new modifiable sorted collection with the specified element cardinality and the elements.
     */
    static <E> ModifiableSortedCollection<E> of(final ElementCardinality elementCardinality,
            final Comparator<E> comparator, final E... elements) {
        return new ModifiableSortedTreeCollection<E>(elementCardinality, comparator, elements);
    }

    /**
     * Removes an element from the sorted collection at a given position.
     *
     * @param index The position of the element that should be removed.
     * @return The element that was removed.
     * @throws IndexOutOfBoundsException Thrown if the index is out of bounds.
     */
    E removeAt(int index) throws IndexOutOfBoundsException;

    /**
     * Removes the greatest element of this collection.
     *
     * @return The element that was removed.
     * @throws IndexOutOfBoundsException Thrown if the index is out of bounds.
     */
    default E removeGreatest() throws IndexOutOfBoundsException {
        return removeAt(size() - 1);
    }

    /**
     * Removes the least element of this collection.
     *
     * @return The element that was removed.
     * @throws IndexOutOfBoundsException Thrown if the index is out of bounds.
     */
    default E removeLeast() throws IndexOutOfBoundsException {
        return removeAt(0);
    }
}
