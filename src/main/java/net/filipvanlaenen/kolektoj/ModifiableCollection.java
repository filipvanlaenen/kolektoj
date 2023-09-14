package net.filipvanlaenen.kolektoj;

/**
 * Interface defining the signature for all modifiable collections.
 *
 * @param <E> The element type.
 */
public interface ModifiableCollection<E> extends Collection<E> {
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
