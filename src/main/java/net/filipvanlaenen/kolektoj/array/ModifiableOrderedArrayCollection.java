package net.filipvanlaenen.kolektoj.array;

import java.lang.reflect.Array;
import java.util.Iterator;
import java.util.Spliterator;

import net.filipvanlaenen.kolektoj.Collection;
import net.filipvanlaenen.kolektoj.ModifiableOrderedCollection;

/**
 * An array backed implementation of the {@link net.filipvanlaenen.kolektoj.ModifiableOrderedCollection} interface.
 *
 * @param <E> The element type.
 */
public final class ModifiableOrderedArrayCollection<E> implements ModifiableOrderedCollection<E> {
    /**
     * The stride for resizing the elements array.
     */
    private static final int STRIDE = 5;
    /**
     * An array with the elements.
     */
    private E[] elements;
    /**
     * The size of the collection.
     */
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
        if (size == elements.length) {
            resizeTo(elements.length + STRIDE);
        }
        elements[size++] = element;
        return true;
    }

    @Override
    public boolean addAll(final Collection<? extends E> collection) {
        int numberOfNewElements = collection.size();
        // EQMU: Changing the conditional boundary below produces an equivalent mutant.
        if (size + numberOfNewElements > elements.length) {
            resizeTo(size + numberOfNewElements + STRIDE);
        }
        System.arraycopy(collection.toArray(), 0, elements, size, numberOfNewElements);
        size += numberOfNewElements;
        return true;
    }

    @Override
    public boolean addAllAt(final int index, final Collection<? extends E> collection) {
        if (index > elements.length) {
            throw new IndexOutOfBoundsException(
                    "Cannot add the elements of another collection at a position beyond the size of the collection.");
        } else {
            int numberOfNewElements = collection.size();
            // EQMU: Changing the conditional boundary below produces an equivalent mutant.
            if (size + numberOfNewElements > elements.length) {
                resizeTo(size + numberOfNewElements + STRIDE);
            }
            System.arraycopy(elements, index, elements, index + numberOfNewElements, size - index);
            System.arraycopy(collection.toArray(), 0, elements, index, numberOfNewElements);
            size += numberOfNewElements;
            return true;
        }
    }

    @Override
    public boolean addAt(final int index, final E element) throws IndexOutOfBoundsException {
        if (index > elements.length) {
            throw new IndexOutOfBoundsException(
                    "Cannot add an element at a position beyond the size of the collection.");
        } else {
            if (size == elements.length) {
                resizeTo(elements.length + STRIDE);
            }
            System.arraycopy(elements, index, elements, index + 1, size - index);
            elements[index] = element;
            size++;
            return true;
        }
    }

    @Override
    public void clear() {
        size = 0;
        // EQMU: Changing the conditional boundary below produces an equivalent mutant.
        // EQMU: Negating the conditional below produces an equivalent mutant.
        if (elements.length > STRIDE) {
            // EQMU: Removing the call to resizeTo below produces an equivalent mutant.
            resizeTo(STRIDE);
        }
    }

    @Override
    public boolean contains(final E element) {
        for (int i = 0; i < size; i++) {
            E e = elements[i];
            if (e == null && element == null || e != null && e.equals(element)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Creates a new element type array with a given length.
     *
     * @param length The length of the array.
     * @return An array of the given length with the element type.
     */
    private E[] createNewArray(final int length) {
        Class<E[]> clazz = (Class<E[]>) elements.getClass();
        return (E[]) Array.newInstance(clazz.getComponentType(), length);
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
    public boolean remove(final E element) {
        for (int i = 0; i < size; i++) {
            if (elements[i].equals(element)) {
                removeAt(i);
                return true;
            }
        }
        return false;
    }

    @Override
    public E removeAt(final int index) throws IndexOutOfBoundsException {
        if (index >= elements.length) {
            throw new IndexOutOfBoundsException(
                    "Cannot remove an element at a position beyond the size of the collection.");
        } else {
            E result = elements[index];
            System.arraycopy(elements, index + 1, elements, index, size - index - 1);
            size--;
            // EQMU: Changing the conditional boundary below produces an equivalent mutant.
            // EQMU: Replacing integer subtraction with addition below produces an equivalent mutant.
            // EQMU: Negating the conditional below produces an equivalent mutant.
            if (size < elements.length - STRIDE) {
                // EQMU: Removing the call to resizeTo below produces an equivalent mutant.
                resizeTo(size);
            }
            return result;
        }
    }

    /**
     * Resizes the array to the new length. It is assumed that the new length is not less than the current size.
     *
     * @param newLength The new length for the array.
     */
    private void resizeTo(final int newLength) {
        E[] newElements = createNewArray(newLength);
        System.arraycopy(elements, 0, newElements, 0, size);
        elements = newElements;
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
        E[] result = createNewArray(size);
        System.arraycopy(elements, 0, result, 0, size);
        return result;
    }
}
