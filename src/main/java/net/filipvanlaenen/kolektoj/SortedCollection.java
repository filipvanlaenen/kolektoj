package net.filipvanlaenen.kolektoj;

import java.util.Comparator;

import net.filipvanlaenen.kolektoj.array.SortedArrayCollection;

/**
 * Interface defining the signature for all sorted collections.
 *
 * @param <E> The element type.
 */
public interface SortedCollection<E> extends OrderedCollection<E> {
    /**
     * Returns a new empty sorted collection.
     *
     * @param <F>        The element type.
     * @param comparator The comparator by which to sort the elements.
     * @return A new empty sorted collection.
     */
    static <F> SortedCollection<F> empty(final Comparator<? super F> comparator) {
        return new SortedArrayCollection<F>(comparator);
    }

    /**
     * Returns a new sorted collection cloned from the provided collection.
     *
     * @param <F>        The element type.
     * @param comparator The comparator by which to sort the elements.
     * @param collection The original collection.
     * @return A new sorted collection cloned from the provided collection.
     */
    static <F> SortedCollection<F> of(final Comparator<? super F> comparator,
            final Collection<? extends F> collection) {
        return new SortedArrayCollection<F>(comparator, collection);
    }

    /**
     * Returns a new sorted collection with the specified elements.
     *
     * @param <F>        The element type.
     * @param comparator The comparator by which to sort the elements.
     * @param elements   The elements for the new sorted collection.
     * @return A new sorted collection with the specified elements.
     */
    static <F> SortedCollection<F> of(final Comparator<? super F> comparator, final F... elements) {
        return new SortedArrayCollection<F>(comparator, elements);
    }

    /**
     * Returns a new sorted collection with the specified element cardinality and the elements.
     *
     * @param <F>                The element type.
     * @param elementCardinality The element cardinality.
     * @param comparator         The comparator by which to sort the elements.
     * @param elements           The elements for the new sorted collection.
     * @return A new sorted collection with the specified element cardinality and the elements.
     */
    static <F> SortedCollection<F> of(final ElementCardinality elementCardinality,
            final Comparator<? super F> comparator, final F... elements) {
        return new SortedArrayCollection<F>(elementCardinality, comparator, elements);
    }

    /**
     * Returns the comparator sorting this collection.
     *
     * @return The comparator sorting this collection
     */
    Comparator<? super E> getComparator();

    /**
     * Returns the greatest element in the collection.
     *
     * @return The greatest element in the collection.
     * @throws IndexOutOfBoundsException Thrown if the collection is empty.
     */
    default E getGreatest() throws IndexOutOfBoundsException {
        return getFirst();
    }

    /**
     * Returns the least element in the collection.
     *
     * @return The least element in the collection.
     * @throws IndexOutOfBoundsException Thrown if the collection is empty.
     */
    default E getLeast() throws IndexOutOfBoundsException {
        return getLast();
    }
}
