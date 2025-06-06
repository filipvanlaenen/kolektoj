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
     * @param <F>        The element type.
     * @param comparator The comparator by which to sort the elements.
     * @return A new empty modifiable sorted ordered collection.
     */
    static <F> ModifiableSortedCollection<F> empty(final Comparator<? super F> comparator) {
        return new ModifiableSortedTreeCollection<F>(comparator);
    }

    /**
     * Returns a new modifiable sorted collection cloned from the provided collection.
     *
     * @param <F>        The element type.
     * @param comparator The comparator by which to sort the elements.
     * @param collection The original collection.
     * @return A new sorted modifiable collection cloned from the provided collection.
     */
    static <F> ModifiableSortedCollection<F> of(final Comparator<? super F> comparator,
            final Collection<? extends F> collection) {
        return new ModifiableSortedTreeCollection<F>(comparator, collection);
    }

    /**
     * Returns a new modifiable sorted collection with the specified elements.
     *
     * @param <F>        The element type.
     * @param comparator The comparator by which to sort the elements.
     * @param elements   The elements for the new modifiable sorted collection.
     * @return A new modifiable sorted collection with the specified elements.
     */
    static <F> ModifiableSortedCollection<F> of(final Comparator<? super F> comparator, final F... elements) {
        return new ModifiableSortedTreeCollection<F>(comparator, elements);
    }

    /**
     * Returns a new modifiable sorted collection with the specified element cardinality and the elements.
     *
     * @param <F>                The element type.
     * @param elementCardinality The element cardinality.
     * @param comparator         The comparator by which to sort the elements.
     * @param elements           The elements for the new modifiable sorted collection.
     * @return A new modifiable sorted collection with the specified element cardinality and the elements.
     */
    static <F> ModifiableSortedCollection<F> of(final ElementCardinality elementCardinality,
            final Comparator<? super F> comparator, final F... elements) {
        return new ModifiableSortedTreeCollection<F>(elementCardinality, comparator, elements);
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
