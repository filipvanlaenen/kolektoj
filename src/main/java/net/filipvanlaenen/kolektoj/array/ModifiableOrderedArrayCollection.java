package net.filipvanlaenen.kolektoj.array;

import static net.filipvanlaenen.kolektoj.Collection.ElementCardinality.DISTINCT_ELEMENTS;
import static net.filipvanlaenen.kolektoj.Collection.ElementCardinality.DUPLICATE_ELEMENTS;

import java.lang.reflect.Array;
import java.util.Comparator;
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
     * The element cardinality.
     */
    private final ElementCardinality elementCardinality;
    /**
     * An array with the elements.
     */
    private E[] elements;
    /**
     * The size of the collection.
     */
    private int size;

    /**
     * Constructs a modifiable ordered array collection from another collection, with the elements sorted using the
     * given comparator.
     *
     * @param source     The collection to create a new ordered collection from.
     * @param comparator The comparator by which to sort the elements.
     */
    public ModifiableOrderedArrayCollection(final Collection<E> source, final Comparator<E> comparator) {
        elementCardinality = source.getElementCardinality();
        elements = ArrayUtilities.quicksort(source.toArray(), comparator);
    }

    /**
     * Constructs a modifiable ordered array collection with the given elements.
     *
     * @param elements The elements of the modifiable array collection.
     */
    public ModifiableOrderedArrayCollection(final E... elements) {
        this(DUPLICATE_ELEMENTS, elements);
    }

    /**
     * Constructs a modifiable ordered array collection with the given elements and element cardinality.
     *
     * @param elementCardinality The element cardinality.
     * @param elements           The elements of the modifiable ordered array collection.
     */
    public ModifiableOrderedArrayCollection(final ElementCardinality elementCardinality, final E... elements) {
        this.elementCardinality = elementCardinality;
        if (elementCardinality == DISTINCT_ELEMENTS) {
            this.elements = ArrayUtilities.cloneDistinctElements(elements);
        } else {
            this.elements = elements.clone();
        }
        size = this.elements.length;
    }

    @Override
    public boolean add(final E element) {
        if (elementCardinality == DISTINCT_ELEMENTS && contains(element)) {
            return false;
        }
        if (size == elements.length) {
            resizeTo(elements.length + STRIDE);
        }
        elements[size++] = element;
        return true;
    }

    @Override
    public boolean addAll(final Collection<? extends E> collection) {
        if (collection.isEmpty()) {
            return false;
        }
        // TODO: Element cardinality
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
            if (collection.isEmpty()) {
                return false;
            }
            // TODO: Element cardinality
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
            if (elementCardinality == DISTINCT_ELEMENTS && contains(element)) {
                return false;
            }
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
        return ArrayUtilities.contains(elements, size, element);
    }

    @Override
    public boolean containsAll(final Collection<?> collection) {
        return ArrayUtilities.containsAll(elements, size, collection);
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
    public ElementCardinality getElementCardinality() {
        return elementCardinality;
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
    public boolean removeAll(final Collection<? extends E> collection) {
        boolean result = false;
        for (E element : collection) {
            for (int i = 0; i < size; i++) {
                if (elements[i] == null && element == null || elements[i] != null && elements[i].equals(element)) {
                    System.arraycopy(elements, i + 1, elements, i, size - i - 1);
                    size--;
                    result = true;
                    break;
                }
            }
        }
        // EQMU: Changing the conditional boundary below produces an equivalent mutant.
        // EQMU: Replacing integer subtraction with addition below produces an equivalent mutant.
        // EQMU: Negating the conditional below produces an equivalent mutant.
        if (size < elements.length - STRIDE) {
            // EQMU: Removing the call to resizeTo below produces an equivalent mutant.
            resizeTo(size);
        }
        return result;
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
    public boolean retainAll(final Collection<? extends E> collection) {
        boolean[] retain = new boolean[size];
        for (E element : collection) {
            for (int i = 0; i < size; i++) {
                if (!retain[i] && (elements[i] == null && element == null || elements[i].equals(element))) {
                    retain[i] = true;
                    break;
                }
            }
        }
        int i = 0;
        boolean result = false;
        while (i < size) {
            if (retain[i]) {
                i++;
            } else {
                System.arraycopy(elements, i + 1, elements, i, size - i - 1);
                System.arraycopy(retain, i + 1, retain, i, size - i - 1);
                size--;
                result = true;
            }
        }
        // EQMU: Changing the conditional boundary below produces an equivalent mutant.
        // EQMU: Replacing integer subtraction with addition below produces an equivalent mutant.
        // EQMU: Negating the conditional below produces an equivalent mutant.
        if (size < elements.length - STRIDE) {
            // EQMU: Removing the call to resizeTo below produces an equivalent mutant.
            resizeTo(size);
        }
        return result;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public Spliterator<E> spliterator() {
        int characteristics =
                Spliterator.ORDERED | (elementCardinality == DISTINCT_ELEMENTS ? Spliterator.DISTINCT : 0);
        return new ArraySpliterator<E>(toArray(), characteristics);
    }

    @Override
    public E[] toArray() {
        E[] result = createNewArray(size);
        System.arraycopy(elements, 0, result, 0, size);
        return result;
    }
}
