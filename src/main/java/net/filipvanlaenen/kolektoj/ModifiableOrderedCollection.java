package net.filipvanlaenen.kolektoj;

public interface ModifiableOrderedCollection<E> extends ModifiableCollection<E>, OrderedCollection<E> {
    /**
     * Adds an element to this collection at a given position and returns whether it increased the size of the
     * collection.
     *
     * @param index   The position where the element should be added.
     * @param element The element to be added to the collection.
     * @return True if the size of the collection increased after adding the element.
     */
    boolean add(int index, E element) throws IndexOutOfBoundsException;

    /**
     * Removes an element from this collection at a given position.
     *
     * @param index The position of the element that should be removed.
     * @return The element that was removed.
     */
    E remove(int index) throws IndexOutOfBoundsException;
}
