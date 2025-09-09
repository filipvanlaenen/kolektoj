package net.filipvanlaenen.kolektoj.sortedtree;

import static net.filipvanlaenen.kolektoj.Collection.ElementCardinality.DISTINCT_ELEMENTS;
import static net.filipvanlaenen.kolektoj.Collection.ElementCardinality.DUPLICATE_ELEMENTS;

import java.util.Comparator;
import java.util.Iterator;
import java.util.Spliterator;

import net.filipvanlaenen.kolektoj.Collection;
import net.filipvanlaenen.kolektoj.SortedCollection;
import net.filipvanlaenen.kolektoj.array.ArrayIterator;
import net.filipvanlaenen.kolektoj.array.ArraySpliterator;
import net.filipvanlaenen.kolektoj.array.ArrayUtilities;

/**
 * An implementation of the {@link net.filipvanlaenen.kolektoj.OrderedCollection} interface backed by a sorted tree, in
 * particular an AVL tree.
 *
 * @param <E> The element type.
 */
public final class SortedTreeCollection<E> implements SortedCollection<E> {
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
     * The size of the collection.
     */
    private final int size;
    /**
     * The sorted tree with the elements.
     */
    private final SortedTree<E, E> sortedTree;

    /**
     * Constructs a new sorted tree collection from another collection, with the elements sorted using the given
     * comparator.
     *
     * @param source     The collection to create a new ordered collection from.
     * @param comparator The comparator by which to sort the elements.
     */
    public SortedTreeCollection(final Comparator<? super E> comparator, final Collection<E> source) {
        this.comparator = comparator;
        this.elementCardinality = source.getElementCardinality();
        this.elements = ArrayUtilities.quicksort(source.toArray(), comparator);
        sortedTree = SortedTree.fromSortedElementArray(comparator, elementCardinality, this.elements);
        this.size = this.elements.length;
    }

    /**
     * Constructs a new sorted tree collection with the given elements using the comparator for sorting. The element
     * cardinality is defaulted to <code>DUPLICATE_ELEMENTS</code>.
     *
     * @param comparator The comparator by which to sort the elements.
     * @param elements   The elements of the collection.
     */
    public SortedTreeCollection(final Comparator<? super E> comparator, final E... elements) {
        this(DUPLICATE_ELEMENTS, comparator, elements);
    }

    /**
     * Constructs a new sorted tree collection with the given elements and element cardinality and using the comparator
     * for sorting.
     *
     * @param elementCardinality The element cardinality.
     * @param comparator         The comparator by which to sort the elements.
     * @param elements           The elements of the collection.
     */
    public SortedTreeCollection(final ElementCardinality elementCardinality, final Comparator<? super E> comparator,
            final E... elements) {
        this.comparator = comparator;
        this.elementCardinality = elementCardinality;
        if (elementCardinality == DISTINCT_ELEMENTS) {
            this.elements = ArrayUtilities.quicksort(ArrayUtilities.cloneDistinctElements(elements), comparator);
        } else {
            this.elements = ArrayUtilities.quicksort(elements, comparator);
        }
        sortedTree = SortedTree.fromSortedElementArray(comparator, elementCardinality, this.elements);
        this.size = this.elements.length;
    }

    @Override
    public boolean contains(final E element) {
        return sortedTree.containsKey(element);
    }

    @Override
    public boolean containsAll(final Collection<?> collection) {
        return sortedTree.containsAllKeys(collection);
    }

    @Override
    public int firstIndexOf(final E element) {
        return sortedTree.firstIndexOf(element);
    }

    @Override
    public E get() throws IndexOutOfBoundsException {
        if (size == 0) {
            throw new IndexOutOfBoundsException("Cannot return an element from an empty collection.");
        } else {
            return sortedTree.getRootNode().getKey();
        }
    }

    @Override
    public E getAt(final int index) throws IndexOutOfBoundsException {
        if (index >= size) {
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
        return sortedTree.indexOf(element);
    }

    @Override
    public Iterator<E> iterator() {
        return new ArrayIterator<E>(toArray());
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public Spliterator<E> spliterator() {
        return new ArraySpliterator<E>(toArray(), Spliterator.ORDERED | Spliterator.SORTED
                | (elementCardinality == DISTINCT_ELEMENTS ? Spliterator.DISTINCT : 0), comparator);
    }

    @Override
    public Object[] toArray() {
        return elements.clone();
    }
}
