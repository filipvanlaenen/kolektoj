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
    private final Comparator<E> comparator;
    /**
     * The element cardinality.
     */
    private final ElementCardinality elementCardinality;
    /**
     * An array with the elements.
     */
    private final E[] elements;

    /**
     * Constructs a new sorted array collection from another collection, with the elements sorted using the given
     * comparator.
     *
     * @param source     The collection to create a new ordered collection from.
     * @param comparator The comparator by which to sort the elements.
     */
    public SortedArrayCollection(final Comparator<E> comparator, final Collection<E> source) {
        this(source.getElementCardinality(), comparator, source.toArray());
    }

    /**
     * Constructs a new sorted array collection with the given elements using the comparator for sorting. The element
     * cardinality is defaulted to <code>DUPLICATE_ELEMENTS</code>.
     *
     * @param comparator The comparator by which to sort the elements.
     * @param elements   The elements of the collection.
     */
    public SortedArrayCollection(final Comparator<E> comparator, final E... elements) {
        this(DUPLICATE_ELEMENTS, comparator, elements);
    }

    /**
     * Constructs a new sorted array collection with the given elements and element cardinality and using the comparator
     * for sorting.
     *
     * @param elementCardinality The element cardinality.
     * @param comparator         The comparator by which to sort the elements.
     * @param elements           The elements of the collection.
     */
    public SortedArrayCollection(final ElementCardinality elementCardinality, final Comparator<E> comparator,
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
        int below = -1;
        int above = elements.length;
        while (above > below + 1) {
            int middle = (below + above) / 2;
            int comparison = comparator.compare(element, elements[middle]);
            if (comparison == 0) {
                return true;
            } else if (comparison < 0) {
                // EQMU: Changing the conditional boundary above produces an equivalent mutant.
                above = middle;
            } else {
                below = middle;
            }
        }
        return false;
    }

    @Override
    public boolean containsAll(final Collection<?> collection) {
        if (collection.size() > elements.length) {
            return false;
        }
        boolean[] matches = new boolean[elements.length];
        for (Object element : collection) {
            boolean found = false;
            int below = -1;
            int above = elements.length;
            while (above > below + 1) {
                int middle = (below + above) / 2;
                // TODO: Return false if cast doesn't work.
                int comparison = comparator.compare((E) element, elements[middle]);
                if (comparison == 0) {
                    if (!matches[middle]) {
                        matches[middle] = true;
                        found = true;
                        break;
                    } else {
                        while (middle > 0 && comparator.compare(elements[middle - 1], elements[middle]) == 0) {
                            middle--;
                            if (!matches[middle]) {
                                matches[middle] = true;
                                found = true;
                                break;
                            }
                        }
                        while (middle + 1 < elements.length
                                && comparator.compare(elements[middle], elements[middle + 1]) == 0) {
                            middle++;
                            if (!matches[middle]) {
                                matches[middle] = true;
                                found = true;
                                break;
                            }
                        }
                    }
                } else if (comparison < 0) {
                    // EQMU: Changing the conditional boundary above produces an equivalent mutant.
                    above = middle;
                } else {
                    below = middle;
                }
            }
            if (!found) {
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
        return elementCardinality;
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
    public E[] toArray() {
        return elements.clone();
    }
}
