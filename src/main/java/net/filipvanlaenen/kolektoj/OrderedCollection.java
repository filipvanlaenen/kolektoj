package net.filipvanlaenen.kolektoj;

import net.filipvanlaenen.kolektoj.array.OrderedArrayCollection;

/**
 * Interface defining the signature for all ordered collections.
 *
 * @param <E> The element type.
 */
public interface OrderedCollection<E> extends Collection<E> {
    /**
     * Returns a new empty ordered collection.
     *
     * @param <E> The element type.
     * @return A new empty ordered collection.
     */
    static <E> OrderedCollection<E> empty() {
        return new OrderedArrayCollection<E>();
    }

    /**
     * Returns a new ordered collection with the specified elements.
     *
     * @param <E>      The element type.
     * @param elements The elements for the new ordered collection.
     * @return A new ordered collection with the specified elements.
     */
    static <E> OrderedCollection<E> of(final E... elements) {
        return new OrderedArrayCollection<E>(elements);
    }

    /**
     * Returns a new ordered collection with the specified element cardinality and the elements.
     *
     * @param <E>                The element type.
     * @param elementCardinality The element cardinality.
     * @param elements           The elements for the new ordered collection.
     * @return A new ordered collection with the specified element cardinality and the elements.
     */
    static <E> Collection<E> of(final ElementCardinality elementCardinality, final E... elements) {
        return new OrderedArrayCollection<E>(elementCardinality, elements);
    }

    /**
     * Returns the element from the collection at the given position.
     *
     * @param index The position of the element that should be returned.
     * @return The element from the collection at the given position.
     * @throws IndexOutOfBoundsException Thrown if the index is out of bounds.
     */
    E getAt(int index) throws IndexOutOfBoundsException;
}
