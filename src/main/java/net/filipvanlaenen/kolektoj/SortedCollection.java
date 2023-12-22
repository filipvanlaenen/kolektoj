package net.filipvanlaenen.kolektoj;

/**
 * Interface defining the signature for all sorted collections. Sorted collections are by definition also modifiable: if
 * a sorted collection is not modifiable, it's simply an ordered collection.
 *
 * @param <E> The element type.
 */
public interface SortedCollection<E> extends ModifiableCollection<E>, OrderedCollection<E> {
    /**
     * Removes an element from the sorted collection at a given position.
     *
     * @param index The position of the element that should be removed.
     * @return The element that was removed.
     * @throws IndexOutOfBoundsException Thrown if the index is out of bounds.
     */
    E removeAt(int index) throws IndexOutOfBoundsException;
}
