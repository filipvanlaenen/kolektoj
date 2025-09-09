package net.filipvanlaenen.kolektoj.array;

import static net.filipvanlaenen.kolektoj.Collection.ElementCardinality.DISTINCT_ELEMENTS;
import static net.filipvanlaenen.kolektoj.Collection.ElementCardinality.DUPLICATE_ELEMENTS;

import java.util.Comparator;
import java.util.Iterator;
import java.util.Spliterator;

import net.filipvanlaenen.kolektoj.Collection;
import net.filipvanlaenen.kolektoj.SortedCollection;

/**
 * An array backed implementation of the {@link net.filipvanlaenen.kolektoj.SortedCollection} interface.
 *
 * @param <E> The element type.
 */
public final class SortedArrayCollection<E> implements SortedCollection<E> {
    /**
     * The comparator to use for comparing the elements in this collection.
     */
    private final Comparator<? super E> comparator;
    /**
     * The element cardinality.
     */
    private final ElementCardinality elementCardinality;
    /**
     * An array with the elements.
     */
    private final Object[] elements;

    /**
     * Constructs a new sorted array collection from another collection, with the elements sorted using the given
     * comparator.
     *
     * @param source     The collection to create a new ordered collection from.
     * @param comparator The comparator by which to sort the elements.
     */
    public SortedArrayCollection(final Comparator<? super E> comparator, final Collection<? extends E> source) {
        this.comparator = comparator;
        this.elementCardinality = source.getElementCardinality();
        this.elements = ArrayUtilities.quicksort(source.toArray(), comparator);
    }

    /**
     * Constructs a new sorted array collection with the given elements using the comparator for sorting. The element
     * cardinality is defaulted to <code>DUPLICATE_ELEMENTS</code>.
     *
     * @param comparator The comparator by which to sort the elements.
     * @param elements   The elements of the collection.
     */
    public SortedArrayCollection(final Comparator<? super E> comparator, final E... elements) {
        this.comparator = comparator;
        this.elementCardinality = DUPLICATE_ELEMENTS;
        this.elements = ArrayUtilities.quicksort(elements, comparator);
    }

    /**
     * Constructs a new sorted array collection with the given elements and element cardinality and using the comparator
     * for sorting.
     *
     * @param elementCardinality The element cardinality.
     * @param comparator         The comparator by which to sort the elements.
     * @param elements           The elements of the collection.
     */
    public SortedArrayCollection(final ElementCardinality elementCardinality, final Comparator<? super E> comparator,
            final E... elements) {
        this.comparator = comparator;
        this.elementCardinality = elementCardinality;
        if (elementCardinality == DISTINCT_ELEMENTS) {
            this.elements = ArrayUtilities.quicksort(ArrayUtilities.cloneDistinctElements(elements), comparator);
        } else {
            this.elements = ArrayUtilities.quicksort(elements, comparator);
        }
    }

    @Override
    public boolean contains(final E element) {
        return ArrayUtilities.contains(elements, elements.length, element, comparator);
    }

    @Override
    public boolean containsAll(final Collection<?> collection) {
        return ArrayUtilities.containsAll(elements, elements.length, collection, comparator);
    }

    @Override
    public int firstIndexOf(final E element) {
        int i = ArrayUtilities.findIndex(elements, elements.length, element, comparator);
        if (i == -1) {
            return -1;
        } else {
            while (i > 0 && comparator.compare(element, (E) elements[i - 1]) == 0) {
                i--;
            }
            return i;
        }
    }

    @Override
    public E get() throws IndexOutOfBoundsException {
        if (elements.length == 0) {
            throw new IndexOutOfBoundsException("Cannot return an element from an empty collection.");
        } else {
            return (E) elements[0];
        }
    }

    @Override
    public E getAt(final int index) throws IndexOutOfBoundsException {
        if (index >= elements.length) {
            throw new IndexOutOfBoundsException(
                    "Cannot return an element at a position beyond the size of the collection.");
        } else {
            return (E) elements[index];
        }
    }

    @Override
    public Comparator<? super E> getComparator() {
        return comparator;
    }

    @Override
    public ElementCardinality getElementCardinality() {
        return elementCardinality;
    }

    @Override
    public int indexOf(final E element) {
        return ArrayUtilities.findIndex(elements, elements.length, element, comparator);
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
        int characteristics = Spliterator.ORDERED | Spliterator.SORTED
                | (elementCardinality == DISTINCT_ELEMENTS ? Spliterator.DISTINCT : 0);
        return new ArraySpliterator<E>(elements, characteristics, comparator);
    }

    @Override
    public Object[] toArray() {
        return elements.clone();
    }
}
