package net.filipvanlaenen.kolektoj.array;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * An iterator backed by an array.
 *
 * @param <E> The element type.
 */
public final class ArrayIterator<E> implements Iterator<E> {
    /**
     * An array with the elements.
     */
    private final E[] elements;
    /**
     * The index of the iterator.
     */
    private int index;

    /**
     * Constructor taking an array with the elements as its parameter.
     *
     * @param elements An array with the elements to iterate over.
     */
    public ArrayIterator(final E[] elements) {
        this.elements = elements.clone();
    }

    @Override
    public boolean hasNext() {
        return index < elements.length;
    }

    @Override
    public E next() {
        if (hasNext()) {
            return elements[index++];
        }
        throw new NoSuchElementException();
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }
}
