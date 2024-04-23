package net.filipvanlaenen.kolektoj;

import java.lang.reflect.Array;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import net.filipvanlaenen.kolektoj.array.ArrayCollection;

/**
 * Interface defining the signature for all collections.
 *
 * @param <E> The element type.
 */
public interface Collection<E> extends Cloneable, Iterable<E> {
    /**
     * Enumeration listing the options for the element cardinality in collections.
     */
    enum ElementCardinality {
        /**
         * Only distinct elements allowed.
         */
        DISTINCT_ELEMENTS,
        /**
         * Duplicate elements allowed.
         */
        DUPLICATE_ELEMENTS
    }

    /**
     * Returns a new empty collection.
     *
     * @return A new empty collection.
     */
    static Collection<Object> empty() {
        return new ArrayCollection<Object>();
    }

    /**
     * Returns a new collection with the specified elements.
     *
     * @param <E>      The element type.
     * @param elements The elements for the new collection.
     * @return A new collection with the specified elements.
     */
    static <E> Collection<E> of(final E... elements) {
        return new ArrayCollection<E>(elements);
    }

    /**
     * Returns a new collection with the specified element cardinality and the elements.
     *
     * @param <E>                The element type.
     * @param elementCardinality The element cardinality.
     * @param elements           The elements for the new collection.
     * @return A new collection with the specified element cardinality and the elements.
     */
    static <E> Collection<E> of(final ElementCardinality elementCardinality, final E... elements) {
        return new ArrayCollection<E>(elementCardinality, elements);
    }

    /**
     * Returns whether the collection contains the element.
     *
     * @param element The element.
     * @return True if the collection contains the element.
     */
    boolean contains(E element);

    /**
     * Returns whether this collection contains all the elements of the provided collection, with the same number of
     * occurrences.
     *
     * @param collection The collection to compare elements against.
     * @return True if all of the elements in the provided collection are present in this collection with the same
     *         number of occurrences.
     */
    boolean containsAll(Collection<?> collection);

    /**
     * Returns whether this collection contains the same elements as the provided collection with the same number of
     * occurrences.
     *
     * @param collection The collection to compare elements against.
     * @return True if the provided collection contains the same elements as this collection with the same number of
     *         occurrences.
     */
    default boolean containsSame(final Collection<?> collection) {
        return size() == collection.size() && containsAll(collection);
    }

    /**
     * Returns an element from the collection. Throws an exception if the collection is empty.
     *
     * @return An element from the collection.
     * @throws IndexOutOfBoundsException Thrown if the collection is empty.
     */
    E get() throws IndexOutOfBoundsException;

    /**
     * Returns the element cardinality of the collection.
     *
     * @return The element cardinality.
     */
    ElementCardinality getElementCardinality();

    /**
     * Returns whether the collection is empty or not.
     *
     * @return True if the collection is empty.
     */
    default boolean isEmpty() {
        return size() == 0;
    }

    @Override
    Iterator<E> iterator();

    /**
     * Returns the number of elements in the collection.
     *
     * @return The number of elements in the collection.
     */
    int size();

    /**
     * Returns a spliterator over the elements in this collection.
     *
     * @return A spliterator over the elements in this collection.
     */
    Spliterator<E> spliterator();

    /**
     * Returns a stream over the elements in this collection.
     *
     * @return A stream over the elements in this collection.
     */
    default Stream<E> stream() {
        return StreamSupport.stream(spliterator(), false);
    }

    /**
     * Returns the content of this collection as an array. For ordered and sorted collections, the order of the elements
     * is preserved.
     *
     * @return An array containing the elements of this collection.
     */
    Object[] toArray();

    /**
     * Puts the content of this collection into the array, or creates a new array of the same type with the content of
     * this collection. A new array is created if the provided array is too small. If the provided array is larger than
     * the size of the collection, the remaining places will be set to <code>null</code>. For ordered and sorted
     * collections, the order of the elements is preserved.
     *
     * @param array An array to put the content of this collection in, or to use as the prototype for cloning.
     * @return A new array of the same type, or the provided array, containing the content of this collection.
     */
    default E[] toArray(E[] array) {
        int size = size();
        E[] result = array.length >= size ? array
                : (E[]) Array.newInstance((Class<E[]>) array.getClass().getComponentType(), size);
        System.arraycopy(toArray(), 0, result, 0, size);
        for (int i = size; i < array.length; i++) {
            array[i] = null;
        }
        return result;
    }
}
