package net.filipvanlaenen.kolektoj.array;

import static net.filipvanlaenen.kolektoj.Collection.ElementCardinality.DISTINCT_ELEMENTS;
import static net.filipvanlaenen.kolektoj.Collection.ElementCardinality.DUPLICATE_ELEMENTS;

import java.util.Iterator;
import java.util.Spliterator;

import net.filipvanlaenen.kolektoj.Collection;

/**
 * An array backed implementation of the {@link net.filipvanlaenen.kolektoj.Collection} interface.
 *
 * @param <E> The element type.
 */
public final class ArrayCollection<E> implements Collection<E> {
    /**
     * The element cardinality.
     */
    private final ElementCardinality elementCardinality;
    /**
     * An array with the elements.
     */
    private final Object[] elements;

    /**
     * Constructs an array collection from another collection, with the same elements and the same element cardinality.
     *
     * @param source The collection to create a new collection from.
     */
    public ArrayCollection(final Collection<? extends E> source) throws IllegalArgumentException {
        this.elementCardinality = source.getElementCardinality();
        this.elements = source.toArray();
    }

    /**
     * Constructs an array collection with the given elements. The element cardinality is defaulted to
     * <code>DUPLICATE_ELEMENTS</code>.
     *
     * @param elements The elements of the collection.
     */
    public ArrayCollection(final E... elements) throws IllegalArgumentException {
        this.elementCardinality = DUPLICATE_ELEMENTS;
        this.elements = elements.clone();
    }

    /**
     * Constructs an array collection with the provided element cardinality and the elements of the provided collection.
     *
     * @param elementCardinality The element cardinality.
     * @param source             The collection to create a new collection from.
     */
    public ArrayCollection(final ElementCardinality elementCardinality, final Collection<? extends E> source)
            throws IllegalArgumentException {
        this.elementCardinality = elementCardinality;
        if (elementCardinality == DISTINCT_ELEMENTS) {
            this.elements = ArrayUtilities.cloneDistinctElements(source.toArray());
        } else {
            this.elements = source.toArray();
        }
    }

    /**
     * Constructs an array collection with the given elements and element cardinality.
     *
     * @param elementCardinality The element cardinality.
     * @param elements           The elements of the collection.
     */
    public ArrayCollection(final ElementCardinality elementCardinality, final E... elements)
            throws IllegalArgumentException {
        this.elementCardinality = elementCardinality;
        if (elementCardinality == DISTINCT_ELEMENTS) {
            this.elements = ArrayUtilities.cloneDistinctElements(elements);
        } else {
            this.elements = elements.clone();
        }
    }

    @Override
    public boolean contains(final E element) {
        return ArrayUtilities.contains(elements, elements.length, element);
    }

    @Override
    public boolean containsAll(final Collection<?> collection) {
        return ArrayUtilities.containsAll(this.elements, elements.length, collection);
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
        return new ArraySpliterator<E>(elements, elementCardinality == DISTINCT_ELEMENTS ? Spliterator.DISTINCT : 0);
    }

    @Override
    public Object[] toArray() {
        return elements.clone();
    }
}
