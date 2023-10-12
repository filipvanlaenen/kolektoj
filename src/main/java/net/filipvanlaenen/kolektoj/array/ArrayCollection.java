package net.filipvanlaenen.kolektoj.array;

import java.util.Iterator;
import java.util.Spliterator;

import net.filipvanlaenen.kolektoj.Collection;

/**
 * An array backed implementation of the {@link net.filipvanlaenen.kolektoj.Collection} interface.
 *
 * @param <E> The element type.
 */
public final class ArrayCollection<E> implements Collection<E> {
    /**
     * An array with the elements.
     */
    private final E[] elements;

    /**
     * Constructs a collection with the given elements.
     *
     * @param elements The elements of the collection.
     */
    public ArrayCollection(final E... elements) {
        this.elements = elements.clone();
    }

    /**
     * Constructs a collection from another collection, with the same elements.
     *
     * @param source The collection to create a new collection from.
     */
    public ArrayCollection(final Collection<E> source) {
        elements = source.toArray();
    }

    @Override
    public boolean contains(final E element) {
        for (E e : elements) {
            if (e == null && element == null || e != null && e.equals(element)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public E get() throws IndexOutOfBoundsException {
        if (elements.length == 0) {
            throw new IndexOutOfBoundsException("Cannot return an element from an empty collection.");
        } else {
            return elements[0];
        }
    }

    @Override
    public Iterator<E> iterator() {
        return new ArrayIterator<E>(elements);
    }

    @Override
    public int size() {
        return elements.length;
    }

    @Override
    public Spliterator<E> spliterator() {
        return new ArraySpliterator<E>(elements, 0);
    }

    @Override
    public E[] toArray() {
        return elements.clone();
    }
}
