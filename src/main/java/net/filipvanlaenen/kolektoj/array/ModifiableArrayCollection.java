package net.filipvanlaenen.kolektoj.array;

import java.lang.reflect.Array;
import java.util.Iterator;
import java.util.Spliterator;

import net.filipvanlaenen.kolektoj.ModifiableCollection;

public final class ModifiableArrayCollection<E> implements ModifiableCollection<E> {
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
    public ModifiableArrayCollection(final E... elements) {
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
    public Iterator<E> iterator() {
        return new ArrayIterator<E>(toArray());
    }

    @Override
    public boolean remove(final E element) {
        // TODO Auto-generated method stub
        return false;
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
