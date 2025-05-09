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
     * @param comparator The comparator by which to sort the elements.
     * @return A new empty sorted collection.
     */
    static SortedCollection<Object> empty(final Comparator<Object> comparator) {
        return new SortedArrayCollection<Object>(comparator);
    }

    /**
     * Returns a new sorted collection with the specified elements.
     *
     * @param <E>        The element type.
     * @param comparator The comparator by which to sort the elements.
     * @param elements   The elements for the new sorted collection.
     * @return A new sorted collection with the specified elements.
     */
    static <E> SortedCollection<E> of(final Comparator<E> comparator, final E... elements) {
        return new SortedArrayCollection<E>(comparator, elements);
    }

    /**
     * Returns a new sorted collection with the specified element cardinality and the elements.
     *
     * @param <E>                The element type.
     * @param elementCardinality The element cardinality.
     * @param comparator         The comparator by which to sort the elements.
     * @param elements           The elements for the new sorted collection.
     * @return A new sorted collection with the specified element cardinality and the elements.
     */
    static <E> SortedCollection<E> of(final ElementCardinality elementCardinality, final Comparator<E> comparator,
            final E... elements) {
        return new SortedArrayCollection<E>(elementCardinality, comparator, elements);
    }

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
