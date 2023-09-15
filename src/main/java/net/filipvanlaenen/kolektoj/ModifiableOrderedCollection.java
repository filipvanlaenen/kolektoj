package net.filipvanlaenen.kolektoj;

/**
 * Interface defining the signature for all modifiable ordered collections.
 *
 * @param <E> The element type.
 */
public interface ModifiableOrderedCollection<E> extends ModifiableCollection<E>, OrderedCollection<E> {
    /**
     * Adds an element to this collection at a given position and returns whether it increased the size of the
     * collection.
     *
     * @param index   The position where the element should be added.
     * @param element The element to be added to the collection.
     * @return True if the size of the collection increased after adding the element.
     * @throws IllegalArgumentException  Thrown if the element is rejected by the collection's constraints.
     * @throws IndexOutOfBoundsException Thrown if the index is out of bounds.
     */
    boolean add(int index, E element) throws IllegalArgumentException, IndexOutOfBoundsException;

    /**
     * Removes an element from this collection at a given position.
     *
     * @param index The position of the element that should be removed.
     * @return The element that was removed.
     * @throws IndexOutOfBoundsException Thrown if the index is out of bounds.
     */
    E remove(int index) throws IndexOutOfBoundsException;
}
