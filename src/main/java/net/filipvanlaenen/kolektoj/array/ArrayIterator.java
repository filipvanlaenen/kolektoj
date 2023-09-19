package net.filipvanlaenen.kolektoj.array;

import java.util.Iterator;
import java.util.NoSuchElementException;

class ArrayIterator<E> implements Iterator<E> {
    private final E[] elements;
    private int index;

    public ArrayIterator(final E[] elements) {
        this.elements = elements.clone();
    }

    public boolean hasNext() {
        return index < elements.length;
    }

    public E next() {
        if (hasNext()) {
            return elements[index++];
        }
        throw new NoSuchElementException();
    }

    public void remove() {
        throw new UnsupportedOperationException();
    }
}
