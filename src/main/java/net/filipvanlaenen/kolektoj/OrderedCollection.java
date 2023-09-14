package net.filipvanlaenen.kolektoj;

public interface OrderedCollection<E> extends Collection<E> {
    /**
     * Returns the element from the collection at the given position.
     *
     * @return The element from the collection at the given position.
     */
    E get(int index) throws IndexOutOfBoundsException;
}
