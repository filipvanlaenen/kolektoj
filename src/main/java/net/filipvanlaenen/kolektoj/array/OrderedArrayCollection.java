package net.filipvanlaenen.kolektoj.array;

import java.util.Comparator;
import java.util.Iterator;
import java.util.Spliterator;

import net.filipvanlaenen.kolektoj.Collection;
import net.filipvanlaenen.kolektoj.OrderedCollection;
import net.filipvanlaenen.kolektoj.Collection.ElementCardinality;

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
     * Constructs an ordered collection with the given elements.
     *
     * @param elements The elements of the collection.
     */
    public OrderedArrayCollection(final E... elements) {
        this.elements = elements.clone();
    }

    /**
     * Constructs an ordered collection from another ordered collection, with the elements in the same order.
     *
     * @param source The ordered collection to create a new ordered collection from.
     */
    public OrderedArrayCollection(final OrderedCollection<E> source) {
        elements = source.toArray();
    }

    /**
     * Constructs an ordered collection from another collection, with the elements sorted using the given comparator.
     *
     * @param source     The collection to create a new ordered collection from.
     * @param comparator The comparator by which to sort the elements.
     */
    public OrderedArrayCollection(final Collection<E> source, final Comparator<E> comparator) {
        E[] array = source.toArray();
        quicksort(array, comparator, 0, array.length - 1);
        elements = array;
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
    public boolean containsAll(final Collection<?> collection) {
        if (collection.size() > size()) {
            return false;
        }
        boolean[] matches = new boolean[elements.length];
        for (Object element : collection) {
            for (int i = 0; i < elements.length; i++) {
                if (!matches[i] && (element == null && elements[i] == null || elements[i].equals(element))) {
                    matches[i] = true;
                    break;
                }
            }
        }
        for (boolean match : matches) {
            if (!match) {
                return false;
            }
        }
        return true;
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
        // TODO: Auto-generated method stub
        return null;
    }

    @Override
    public Iterator<E> iterator() {
        return new ArrayIterator<E>(elements);
    }

    /**
     * Partitions an array for the Quicksort algorithm using the given comparator.
     *
     * @param array      The array to partition.
     * @param comparator The comparator to use.
     * @param first      The index of the first element in the array that should be partitioned.
     * @param last       The index of the last element in the array that should be partitioned.
     * @return The index of the pivot element.
     */
    private int partition(final E[] array, final Comparator<E> comparator, final int first, final int last) {
        E pivot = array[last];
        int index = first - 1;
        for (int j = first; j < last; j++) {
            // EQMU: Changing the conditional boundary below produces an equivalent mutant.
            if (comparator.compare(array[j], pivot) <= 0) {
                swap(array, ++index, j);
            }
        }
        swap(array, index + 1, last);
        return index + 1;
    }

    /**
     * Sorts an array using the given comparator according to the Quicksort algorithm.
     *
     * @param array      The array to sort.
     * @param comparator The comparator to use.
     * @param first      The index of the first element in the array that should be sorted.
     * @param last       The index of the last element in the array that should be sorted.
     */
    private void quicksort(final E[] array, final Comparator<E> comparator, final int first, final int last) {
        // EQMU: Changing the conditional boundary below produces an equivalent mutant.
        if (first < last) {
            int pivotIndex = partition(array, comparator, first, last);
            quicksort(array, comparator, first, pivotIndex - 1);
            quicksort(array, comparator, pivotIndex + 1, last);
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

    /**
     * Swaps two elements in an array.
     *
     * @param array The array.
     * @param i     The index of the first element.
     * @param j     The index of the second element.
     */
    private void swap(final E[] array, final int i, final int j) {
        E value = array[i];
        array[i] = array[j];
        array[j] = value;
    }

    @Override
    public E[] toArray() {
        return elements.clone();
    }
}
