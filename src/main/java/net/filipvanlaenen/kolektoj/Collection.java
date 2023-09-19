package net.filipvanlaenen.kolektoj;

import java.util.Iterator;
import java.util.Spliterator;
import java.util.function.IntFunction;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import net.filipvanlaenen.kolektoj.array.ArrayCollection;

/**
 * Interface defining the signature for all collections.
 *
 * @param <E> The element type.
 */
public interface Collection<E> extends Iterable<E> {
    /**
     * Returns a new empty collection.
     *
     * @param <E> The element type.
     * @return A new empty collection.
     */
    static <E> Collection<E> empty() {
        return new ArrayCollection<E>();
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
     * Returns whether the collection contains the element.
     *
     * @param element The element.
     * @return True if the collection contains the element.
     */
    boolean contains(E element);

    /**
     * Returns an element from the collection. Throws an exception if the collection is empty.
     *
     * @return An element from the collection.
     * @throws IndexOutOfBoundsException Thrown if the collection is empty.
     */
    E get() throws IndexOutOfBoundsException;

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
     * Returns the content of this collection as an array.
     *
     * @param generator A function that can produce an array for the element type of a given length.
     * @return An array containing the elements of this collection.
     */
    E[] toArray(IntFunction<E[]> generator);
}
