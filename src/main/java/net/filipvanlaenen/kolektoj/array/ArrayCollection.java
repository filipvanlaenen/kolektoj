package net.filipvanlaenen.kolektoj.array;

import static net.filipvanlaenen.kolektoj.Collection.ElementCardinality.DUPLICATE_ELEMENTS;
import static net.filipvanlaenen.kolektoj.Collection.ElementCardinality.DISTINCT_ELEMENTS;

import java.lang.reflect.Array;
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
    private final E[] elements;

    /**
     * Constructs a collection from another collection, with the same elements and the same element cardinality.
     *
     * @param source The collection to create a new collection from.
     */
    public ArrayCollection(final Collection<E> source) {
        elementCardinality = source.getElementCardinality();
        elements = source.toArray();
    }

    /**
     * Constructs a collection with the given elements. The element cardinality is defaulted to
     * <code>DUPLICATE_ELEMENTS</code>.
     *
     * @param elements The elements of the collection.
     */
    public ArrayCollection(final E... elements) {
        this(DUPLICATE_ELEMENTS, elements);
    }

    /**
     * Constructs a collection with the given elements and element cardinality.
     *
     * @param elementCardinality The element cardinality.
     * @param elements           The elements of the collection.
     */
    public ArrayCollection(final ElementCardinality elementCardinality, final E... elements) {
        this.elementCardinality = elementCardinality;
        if (elementCardinality == DISTINCT_ELEMENTS) {
            this.elements = cloneDistinctElements(elements);
        } else {
            this.elements = elements.clone();
        }
    }

    /**
     * Returns a clone of an array, but only with distinct elements.
     *
     * @param source The array to clone.
     * @return A new array containing only distinct elements from the source array.
     */
    private E[] cloneDistinctElements(final E[] source) {
        int originalLength = source.length;
        int resultLength = originalLength;
        boolean[] duplicate = new boolean[originalLength];
        for (int i = 0; i < originalLength; i++) {
            if (!duplicate[i]) {
                for (int j = i + 1; j < originalLength; j++) {
                    if (source[i] == null && source[j] == null || source[i] != null && source[i].equals(source[j])) {
                        duplicate[j] = true;
                        resultLength--;
                    }
                }
            }
        }
        Class<E[]> clazz = (Class<E[]>) source.getClass();
        E[] result = (E[]) Array.newInstance(clazz.getComponentType(), resultLength);
        for (int i = 0, j = 0; j < resultLength; i++, j++) {
            while (duplicate[i]) {
                i++;
            }
            result[j] = source[i];
        }
        return result;
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
    public E[] toArray() {
        return elements.clone();
    }
}
