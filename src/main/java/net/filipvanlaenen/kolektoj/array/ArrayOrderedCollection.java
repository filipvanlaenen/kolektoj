package net.filipvanlaenen.kolektoj.array;

import java.util.Arrays;

import net.filipvanlaenen.kolektoj.OrderedCollection;

/**
 * An array backed implementation of the {@link net.filipvanlaenen.kolektoj.OrderedCollection} interface.
 *
 * @param <E> The element type.
 */
public final class ArrayOrderedCollection<E> implements OrderedCollection<E> {
    /**
     * An array with the elements.
     */
    private final E[] elements;

    /**
     * Constructs a collection with the given elements.
     *
     * @param elements The elements of the collection.
     */
    public ArrayOrderedCollection(final E... elements) {
        this.elements = (E[]) Arrays.copyOf(elements, elements.length, Object[].class);
    }

    @Override
    public boolean contains(final E element) {
        for (E e : elements) {
            if (e.equals(element)) {
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
    public E get(final int index) throws IndexOutOfBoundsException {
        if (index >= elements.length) {
            throw new IndexOutOfBoundsException(
                    "Cannot return an element at a position beyond the size of the collection.");
        } else {
            return elements[index];
        }
    }

    @Override
    public int size() {
        return elements.length;
    }
}
