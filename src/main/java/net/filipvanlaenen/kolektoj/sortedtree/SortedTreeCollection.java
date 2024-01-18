package net.filipvanlaenen.kolektoj.sortedtree;

import static net.filipvanlaenen.kolektoj.Collection.ElementCardinality.DISTINCT_ELEMENTS;
import static net.filipvanlaenen.kolektoj.Collection.ElementCardinality.DUPLICATE_ELEMENTS;

import java.util.Comparator;
import java.util.Iterator;
import java.util.Spliterator;

import net.filipvanlaenen.kolektoj.Collection;
import net.filipvanlaenen.kolektoj.OrderedCollection;
import net.filipvanlaenen.kolektoj.array.ArrayIterator;
import net.filipvanlaenen.kolektoj.array.ArraySpliterator;
import net.filipvanlaenen.kolektoj.array.ArrayUtilities;

/**
 * An implementation of the {@link net.filipvanlaenen.kolektoj.OrderedCollection} interface backed by a sorted tree, in
 * particular an AVL tree.
 *
 * @param <E> The element type.
 */
public final class SortedTreeCollection<E extends Comparable<E>> implements OrderedCollection<E> {
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
     * The root node of the collection.
     */
    private Node<E> root;
    /**
     * The size of the collection.
     */
    private final int size;
    private final SortedTree<E> sortedTree;

    /**
     * Constructs a new sorted tree collection from another collection, with the elements sorted using the given
     * comparator.
     *
     * @param source     The collection to create a new ordered collection from.
     * @param comparator The comparator by which to sort the elements.
     */
    public SortedTreeCollection(final Comparator<E> comparator, final Collection<E> source) {
        this(source.getElementCardinality(), comparator, source.toArray());
    }

    /**
     * Constructs a new sorted tree collection with the given elements using the comparator for sorting. The element
     * cardinality is defaulted to <code>DUPLICATE_ELEMENTS</code>.
     *
     * @param comparator The comparator by which to sort the elements.
     * @param elements   The elements of the collection.
     */
    public SortedTreeCollection(final Comparator<E> comparator, final E... elements) {
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
    public SortedTreeCollection(final ElementCardinality elementCardinality, final Comparator<E> comparator,
            final E... elements) {
        this.comparator = comparator;
        this.elementCardinality = elementCardinality;
        if (elementCardinality == DISTINCT_ELEMENTS) {
            this.elements = ArrayUtilities.quicksort(ArrayUtilities.cloneDistinctElements(elements), comparator);
        } else {
            this.elements = ArrayUtilities.quicksort(elements, comparator);
        }
        sortedTree = new SortedTree<E>(comparator, elementCardinality);
        this.size = this.elements.length;
        if (size > 0) {
            root = createSortedTree(this.elements, 0, size - 1);
        }
    }

    @Override
    public boolean contains(final E element) {
        return sortedTree.contains(root, element);
    }

    @Override
    public boolean containsAll(final Collection<?> collection) {
        return sortedTree.containsAll(root, size, (Class<E>) elements.getClass().getComponentType(), collection);
    }

    /**
     * Creates a sorted tree from a sorted array from the provided first to last index.
     *
     * @param array      The array with the elements, sorted.
     * @param firstIndex The first index to be included in the tree.
     * @param lastIndex  The last index to be included in the tree.
     * @return A sorted tree with the elements from the sorted array.
     */
    private Node<E> createSortedTree(final E[] array, final int firstIndex, final int lastIndex) {
        int middleIndex = firstIndex + (lastIndex - firstIndex) / 2;
        Node<E> node = new Node<E>(array[middleIndex]);
        if (middleIndex > firstIndex) {
            node.setLeftChild(createSortedTree(array, firstIndex, middleIndex - 1));
        }
        if (middleIndex < lastIndex) {
            node.setRightChild(createSortedTree(array, middleIndex + 1, lastIndex));
        }
        return node;
    }

    @Override
    public E get() throws IndexOutOfBoundsException {
        if (root == null) {
            throw new IndexOutOfBoundsException("Cannot return an element from an empty collection.");
        } else {
            return root.getElement();
        }
    }

    @Override
    public E getAt(final int index) throws IndexOutOfBoundsException {
        if (index >= size) {
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
    public int size() {
        return size;
    }

    @Override
    public Spliterator<E> spliterator() {
        return new ArraySpliterator<E>(toArray(), Spliterator.ORDERED | Spliterator.SORTED
                | (elementCardinality == DISTINCT_ELEMENTS ? Spliterator.DISTINCT : 0), comparator);
    }

    @Override
    public E[] toArray() {
        return elements.clone();
    }
}
