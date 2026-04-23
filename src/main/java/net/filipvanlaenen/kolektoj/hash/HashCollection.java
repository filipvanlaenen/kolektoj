package net.filipvanlaenen.kolektoj.hash;

import static net.filipvanlaenen.kolektoj.Collection.ElementCardinality.DISTINCT_ELEMENTS;
import static net.filipvanlaenen.kolektoj.Collection.ElementCardinality.DUPLICATE_ELEMENTS;

import java.util.Iterator;
import java.util.Spliterator;

import net.filipvanlaenen.kolektoj.Collection;
import net.filipvanlaenen.kolektoj.Map.Entry;
import net.filipvanlaenen.kolektoj.array.ArrayIterator;
import net.filipvanlaenen.kolektoj.array.ArraySpliterator;
import net.filipvanlaenen.kolektoj.array.ArrayUtilities;

/**
 * A hash backed implementation of the {@link net.filipvanlaenen.kolektoj.Collection} interface.
 *
 * @param <E> The element type.
 */
public final class HashCollection<E> implements Collection<E> {
    /**
     * The ratio by which the number of entries should be multiplied to construct the hashed array.
     */
    private static final int HASHING_RATIO = 3;
    /**
     * The element cardinality.
     */
    private final ElementCardinality elementCardinality;
    /**
     * An array with the elements.
     */
    private final Object[] elements;
    /**
     * A hashed array with the elements.
     */
    private final Object[] hashedElements;
    /**
     * The size of the hashed array with the elements.
     */
    private final int hashedElementsSize;

    /**
     * Constructs a hash collection from another collection, with the same elements and the same element cardinality.
     *
     * @param source The collection to create a new collection from.
     */
    public HashCollection(final Collection<? extends E> source) throws IllegalArgumentException {
        this.elementCardinality = source.getElementCardinality();
        this.elements = source.toArray();
        this.hashedElementsSize = elements.length * HASHING_RATIO;
        this.hashedElements = HashUtilities.createHashedMapFromElements(this.elements, this.hashedElementsSize);
    }

    /**
     * Constructs a hash collection with the given elements. The element cardinality is defaulted to
     * <code>DUPLICATE_ELEMENTS</code>.
     *
     * @param elements The elements of the collection.
     */
    public HashCollection(final E... elements) throws IllegalArgumentException {
        this.elementCardinality = DUPLICATE_ELEMENTS;
        this.elements = elements.clone();
        hashedElementsSize = elements.length * HASHING_RATIO;
        this.hashedElements = HashUtilities.createHashedMapFromElements(this.elements, this.hashedElementsSize);
    }

    /**
     * Constructs a hash collection with the provided element cardinality and the elements of the provided collection.
     *
     * @param elementCardinality The element cardinality.
     * @param source             The collection to create a new collection from.
     */
    public HashCollection(final ElementCardinality elementCardinality, final Collection<? extends E> source)
            throws IllegalArgumentException {
        this.elementCardinality = elementCardinality;
        if (elementCardinality == DISTINCT_ELEMENTS) {
            this.elements = ArrayUtilities.cloneDistinctElements(source.toArray());
        } else {
            this.elements = source.toArray();
        }
        hashedElementsSize = elements.length * HASHING_RATIO;
        this.hashedElements = HashUtilities.createHashedMapFromElements(this.elements, this.hashedElementsSize);
    }

    /**
     * Constructs a hash collection with the given elements and element cardinality.
     *
     * @param elementCardinality The element cardinality.
     * @param elements           The elements of the collection.
     */
    public HashCollection(final ElementCardinality elementCardinality, final E... elements)
            throws IllegalArgumentException {
        this.elementCardinality = elementCardinality;
        if (elementCardinality == DISTINCT_ELEMENTS) {
            this.elements = ArrayUtilities.cloneDistinctElements(elements);
        } else {
            this.elements = elements.clone();
        }
        hashedElementsSize = elements.length * HASHING_RATIO;
        this.hashedElements = HashUtilities.createHashedMapFromElements(this.elements, this.hashedElementsSize);
    }

    @Override
    public boolean contains(final E element) {
        if (hashedElementsSize == 0) {
            return false;
        }
        Entry entry = new Entry(element, element);
        int index = HashUtilities.hash(element, hashedElementsSize);
        while (hashedElements[index] != null) {
            if (hashedElements[index].equals(entry)) {
                return true;
            }
            index = Math.floorMod(index + 1, hashedElementsSize);
        }
        return false;
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
