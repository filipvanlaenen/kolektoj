package net.filipvanlaenen.kolektoj;

import net.filipvanlaenen.kolektoj.array.ModifiableArrayCollection;

/**
 * Interface defining the signature for all modifiable collections.
 *
 * @param <E> The element type.
 */
public interface ModifiableCollection<E> extends Collection<E> {
    /**
     * Returns a new empty modifiable collection.
     *
     * @param <E> The element type.
     * @return A new empty modifiable collection.
     */
    static <E> ModifiableCollection<E> empty() {
        return new ModifiableArrayCollection<E>();
    }

    /**
     * Returns a new modifiable collection with the specified elements.
     *
     * @param <E>      The element type.
     * @param elements The elements for the new modifiable collection.
     * @return A new modifiable collection with the specified elements.
     */
    static <E> ModifiableCollection<E> of(final E... elements) {
        return new ModifiableArrayCollection<E>(elements);
    }

    /**
     * Adds an element to this collection and returns whether it increased the size of the collection.
     *
     * @param element The element to be added to the collection.
     * @return True if the size of the collection increased after adding the element.
     */
    boolean add(E element);

    /**
     * Removes an element from this collection if it is present.
     *
     * @param element The element to be removed from the collection.
     * @return True if the element was present in the collection.
     */
    boolean remove(E element);
}
