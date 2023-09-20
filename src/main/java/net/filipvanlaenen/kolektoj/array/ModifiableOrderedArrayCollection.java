package net.filipvanlaenen.kolektoj.array;

import java.lang.reflect.Array;
import java.util.Iterator;
import java.util.Spliterator;

import net.filipvanlaenen.kolektoj.ModifiableOrderedCollection;

public final class ModifiableOrderedArrayCollection<E> implements ModifiableOrderedCollection<E> {
    /**
     * An array with the elements.
     */
    private E[] elements;
    private int size;

    /**
     * Constructs a collection with the given elements.
     *
     * @param elements The elements of the collection.
     */
    public ModifiableOrderedArrayCollection(final E... elements) {
        this.elements = elements.clone();
        size = elements.length;
    }

    @Override
    public boolean add(final E element) {
        if (size >= elements.length) {
            extendElements(1);
        }
        size++;
        return true;
    }

    @Override
    public boolean addAt(final int index, final E element) throws IllegalArgumentException, IndexOutOfBoundsException {
        // TODO Auto-generated method stub
        return false;
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

    private void extendElements(final int i) {
        Class<E[]> clazz = (Class<E[]>) elements.getClass();
        E[] newElements = (E[]) Array.newInstance(clazz.getComponentType(), size + i);
        System.arraycopy(elements, 0, newElements, 0, size);
        elements = newElements;
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
    public Iterator<E> iterator() {
        return new ArrayIterator<E>(toArray());
    }

    @Override
    public boolean remove(E element) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public E removeAt(final int index) throws IndexOutOfBoundsException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public Spliterator<E> spliterator() {
        return new ArraySpliterator<E>(toArray(), Spliterator.ORDERED);
    }

    @Override
    public E[] toArray() {
        Class<E[]> clazz = (Class<E[]>) elements.getClass();
        E[] result = (E[]) Array.newInstance(clazz.getComponentType(), size);
        System.arraycopy(elements, 0, result, 0, size);
        return result;
    }
}
