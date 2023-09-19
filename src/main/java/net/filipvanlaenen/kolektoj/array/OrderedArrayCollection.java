package net.filipvanlaenen.kolektoj.array;

import java.util.Arrays;
import java.util.Spliterator;
import java.util.function.IntFunction;

import net.filipvanlaenen.kolektoj.OrderedCollection;

/**
 * An array backed implementation of the {@link net.filipvanlaenen.kolektoj.OrderedCollection} interface.
 *
 * @param <E> The element type.
 */
public final class OrderedArrayCollection<E> implements OrderedCollection<E> {
    /**
     * An array with the elements.
     */
    private final E[] elements;

    /**
     * Constructs a collection with the given elements.
     *
     * @param elements The elements of the collection.
     */
    public OrderedArrayCollection(final E... elements) {
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
    public E getAt(final int index) throws IndexOutOfBoundsException {
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

    @Override
    public Spliterator<E> spliterator() {
        return new ArraySpliterator<E>(elements, Spliterator.ORDERED);
    }

    public E[] toArray() {
        return elements.clone();
    }

    @Override
    public E[] toArray(final IntFunction<E[]> generator) {
        return toArray();
    }
}
