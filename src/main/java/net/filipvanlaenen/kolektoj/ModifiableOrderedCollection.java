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
     * @param <E> The element type.
     * @return A new empty modifiable ordered collection.
     */
    static <E> ModifiableOrderedCollection<E> empty() {
        return new ModifiableOrderedArrayCollection<E>();
    }

    /**
     * Returns a new modifiable ordered collection with the specified elements.
     *
     * @param <E>      The element type.
     * @param elements The elements for the new modifiable ordered collection.
     * @return A new modifiable ordered collection with the specified elements.
     */
    static <E> ModifiableOrderedCollection<E> of(final E... elements) {
        return new ModifiableOrderedArrayCollection<E>(elements);
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
    boolean addAllAt(int index, Collection<? extends E> collection) throws IndexOutOfBoundsException;

    /**
     * Removes an element from this collection at a given position.
     *
     * @param index The position of the element that should be removed.
     * @return The element that was removed.
     * @throws IndexOutOfBoundsException Thrown if the index is out of bounds.
     */
    E removeAt(int index) throws IndexOutOfBoundsException;
}
