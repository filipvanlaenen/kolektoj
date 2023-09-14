package net.filipvanlaenen.kolektoj;

/**
 * Interface defining the signature for all collections.
 *
 * @param <E> The element type.
 */
public interface Collection<E> {
    /**
     * Returns whether the collection contains the element.
     *
     * @param element The element.
     * @return True if the collection contains the element.
     */
    boolean contains(E element);

    /**
     * Returns an element from the collection.
     *
     * @return An element from the collection.
     */
    E get();

    /**
     * Returns whether the collection is empty or not.
     *
     * @return True if the collection is empty.
     */
    default boolean isEmpty() {
        return size() == 0;
    }

    /**
     * Returns the number of elements in the collection.
     *
     * @return The number of elements in the collection.
     */
    int size();
}
