package net.filipvanlaenen.kolektoj;

import net.filipvanlaenen.kolektoj.array.ModifiableOrderedArrayCollection;

/**
 * Interface defining the signature for all modifiable ordered collections.
 *
 * @param <E> The element type.
 */
public interface ModifiableOrderedCollection<E> extends ModifiableCollection<E>, OrderedCollection<E> {
    /**
     * Returns a new empty modifiable ordered collection.
     *
     * @param <F> The element type.
     * @return A new empty modifiable ordered collection.
     */
    static <F> ModifiableOrderedCollection<F> empty() {
        return new ModifiableOrderedArrayCollection<F>();
    }

    /**
     * Returns a new modifiable ordered collection with the specified element cardinality and the elements.
     *
     * @param <F>                The element type.
     * @param elementCardinality The element cardinality.
     * @param elements           The elements for the new modifiable ordered collection.
     * @return A new modifiable ordered collection with the specified element cardinality and the elements.
     */
    static <F> ModifiableOrderedCollection<F> of(final ElementCardinality elementCardinality, final F... elements) {
        return new ModifiableOrderedArrayCollection<F>(elementCardinality, elements);
    }

    /**
     * Returns a new modifiable ordered collection with the specified elements.
     *
     * @param <F>      The element type.
     * @param elements The elements for the new modifiable ordered collection.
     * @return A new modifiable ordered collection with the specified elements.
     */
    static <F> ModifiableOrderedCollection<F> of(final F... elements) {
        return new ModifiableOrderedArrayCollection<F>(elements);
    }

    /**
     * Returns a new modifiable ordered collection cloned from the provided ordered collection.
     *
     * @param <F>        The element type.
     * @param collection The original ordered collection.
     * @return A new modifiable ordered collection cloned from the provided ordered collection.
     */
    static <F> ModifiableOrderedCollection<F> of(final OrderedCollection<? extends F> collection) {
        return new ModifiableOrderedArrayCollection<F>(collection);
    }

    /**
     * Adds an element to this collection at a given position and returns whether it increased the size of the
     * collection.
     *
     * @param index   The position where the element should be added.
     * @param element The element to be added to the collection.
     * @return True if the size of the collection increased after adding the element.
     * @throws IndexOutOfBoundsException Thrown if the index is out of bounds.
     */
    boolean addAt(int index, E element) throws IndexOutOfBoundsException;

    /**
     * Adds elements from an ordered collection to this collection at a given position and returns whether it increased
     * the size of the collection.
     *
     * @param index      The position where the elements from the collection should be added.
     * @param collection A collection from which to add elements.
     * @return True if the size of the collection increased after adding the elements of the collection.
     */
    boolean addAllAt(int index, OrderedCollection<? extends E> collection) throws IndexOutOfBoundsException;

    /**
     * Adds elements from an ordered collection to the start of this collection and returns whether it increased the
     * size of the collection.
     *
     * @param collection A collection from which to add elements to the start of this collection.
     * @return True if the size of the collection increased after adding the elements of the collection.
     */
    default boolean addAllFirst(OrderedCollection<? extends E> collection) throws IndexOutOfBoundsException {
        return addAllAt(0, collection);
    }

    /**
     * Adds elements from an ordered collection to the end of this collection and returns whether it increased the size
     * of the collection.
     *
     * @param collection A collection from which to add elements to the end of this collection.
     * @return True if the size of the collection increased after adding the elements of the collection.
     */
    default boolean addAllLast(OrderedCollection<? extends E> collection) throws IndexOutOfBoundsException {
        return addAllAt(size(), collection);
    }

    /**
     * Adds an element to the start of this collection and returns whether it increased the size of the collection.
     *
     * @param element The element to be added to the start of this collection.
     * @return True if the size of the collection increased after adding the element.
     * @throws IndexOutOfBoundsException Thrown if the index is out of bounds.
     */
    default boolean addFirst(final E element) throws IndexOutOfBoundsException {
        return addAt(0, element);
    }

    /**
     * Adds an element to the end of this collection and returns whether it increased the size of the collection.
     *
     * @param element The element to be added to the end of this collection.
     * @return True if the size of the collection increased after adding the element.
     * @throws IndexOutOfBoundsException Thrown if the index is out of bounds.
     */
    default boolean addLast(final E element) throws IndexOutOfBoundsException {
        return addAt(size(), element);
    }

    /**
     * Puts an element in this collection at a given position and returns the original value.
     *
     * @param index   The position that should be overwritten.
     * @param element The element to be put at the given position in the collection.
     * @return The original value that has been overwritten.
     * @throws IllegalArgumentException  Thrown if putting the element at the given position would result into
     *                                   duplicates when they're not allowed.
     * @throws IndexOutOfBoundsException Thrown if the index is out of bounds.
     */
    E putAt(int index, E element) throws IllegalArgumentException, IndexOutOfBoundsException;

    /**
     * Removes an element from this collection at a given position.
     *
     * @param index The position of the element that should be removed.
     * @return The element that was removed.
     * @throws IndexOutOfBoundsException Thrown if the index is out of bounds.
     */
    E removeAt(int index) throws IndexOutOfBoundsException;

    /**
     * Removes the element at the start of this collection.
     *
     * @return The element that was removed.
     * @throws IndexOutOfBoundsException Thrown if the index is out of bounds.
     */
    default E removeFirst() throws IndexOutOfBoundsException {
        return removeAt(0);
    }

    /**
     * Removes the element at the end of this collection.
     *
     * @return The element that was removed.
     * @throws IndexOutOfBoundsException Thrown if the index is out of bounds.
     */
    default E removeLast() throws IndexOutOfBoundsException {
        return removeAt(size() - 1);
    }
}
