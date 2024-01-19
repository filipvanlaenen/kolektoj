package net.filipvanlaenen.kolektoj;

/**
 * Interface defining the signature for all modifiable sorted collections.
 *
 * @param <E> The element type.
 */
public interface ModifiableSortedCollection<E> extends ModifiableCollection<E>, SortedCollection<E> {
    /**
     * Removes an element from the sorted collection at a given position.
     *
     * @param index The position of the element that should be removed.
     * @return The element that was removed.
     * @throws IndexOutOfBoundsException Thrown if the index is out of bounds.
     */
    E removeAt(int index) throws IndexOutOfBoundsException;
}
