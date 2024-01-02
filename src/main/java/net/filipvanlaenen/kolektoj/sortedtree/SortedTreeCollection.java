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
     * A class implementing a node in a sorted tree.
     */
    private final class Node {
        /**
         * The element of the node.
         */
        private E element;
        /**
         * The left child, with elements that compare less than the element of this node.
         */
        private Node leftChild;
        /**
         * The right child, with elements that compare greater than the element of this node.
         */
        private Node rightChild;

        /**
         * Constructor taking an element as its parameter.
         *
         * @param element The element for this node.
         */
        private Node(final E element) {
            this.element = element;
        }

        /**
         * Returns the element of this node.
         *
         * @return The element of this node.
         */
        private E getElement() {
            return element;
        }

        /**
         * Returns the left child of this node.
         *
         * @return The left child of this node.
         */
        private Node getLeftChild() {
            return leftChild;
        }

        /**
         * Returns the right child of this node.
         *
         * @return The right child of this node.
         */
        private Node getRightChild() {
            return rightChild;
        }

        /**
         * Returns the size of this node, which is the size of the left and the right child plus one.
         *
         * @return The size of this node.
         */
        private int getSize() {
            return 1 + getSize(leftChild) + getSize(rightChild);
        }

        /**
         * Helper method returning 0 if the provided parameter is <code>null</code>, and the size of the node otherwise.
         *
         * @param node The node.
         * @return The size of the node, or 0 if the provided parameter is <code>null</code>.
         */
        private int getSize(final Node node) {
            return node == null ? 0 : node.getSize();
        }
    }

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
    private E[] elements;
    /**
     * The root node of the collection.
     */
    private Node root;
    /**
     * The size of the collection.
     */
    private int size;

    public SortedTreeCollection(final Comparator<E> comparator, final E... elements) {
        this(DUPLICATE_ELEMENTS, comparator, elements);
    }

    public SortedTreeCollection(final ElementCardinality elementCardinality, final Comparator<E> comparator,
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
        return contains(root, element);
    }

    private boolean contains(final Node node, final E element) {
        if (node == null) {
            return false;
        }
        int comparison = comparator.compare(element, node.getElement());
        if (comparison == 0) {
            return true;
        } else if (comparison < 0) {
            return contains(node.getLeftChild(), element);
        } else {
            return contains(node.getRightChild(), element);
        }
    }

    @Override
    public boolean containsAll(final Collection<?> collection) {
        if (collection.size() > size) {
            return false;
        }
        boolean[] matched = new boolean[size];
        Class<E> componentType = (Class<E>) elements.getClass().getComponentType();
        for (Object element : collection) {
            if (!(componentType.isInstance(element) && findAndMarkMatch(root, matched, 0, (E) element))) {
                return false;
            }
        }
        return true;
    }

    private boolean findAndMarkMatch(final Node node, final boolean[] matched, final int index, final E element) {
        if (node == null) {
            return false;
        }
        int comparison = comparator.compare(element, node.getElement());
        if (!matched[index] && comparison == 0) {
            matched[index] = true;
            return true;
        } else if (comparison < 0) {
            return findAndMarkMatch(node.getLeftChild(), matched, index + 1, element);
        } else if (comparison > 0) {
            int leftSize = node.getLeftChild() == null ? 0 : node.getLeftChild().getSize();
            return findAndMarkMatch(node.getRightChild(), matched, index + leftSize + 1, element);
        } else if (elementCardinality == DISTINCT_ELEMENTS) {
            return false;
        } else if (findAndMarkMatch(node.getLeftChild(), matched, index + 1, element)) {
            return true;
        } else {
            int leftSize = node.getLeftChild() == null ? 0 : node.getLeftChild().getSize();
            return findAndMarkMatch(node.getRightChild(), matched, index + leftSize + 1, element);
        }
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
