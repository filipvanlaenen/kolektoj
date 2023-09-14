package net.filipvanlaenen.kolektoj;

import java.util.HashSet;
import java.util.Set;

/**
 * Interface defining the signature for all collections.
 *
 * @param <E> The element type.
 */
public interface Collection<E> {
    /**
     * Default implementation of the collection interface.
     *
     * @param <E> The element type.
     */
    final class DefaultCollection<E> implements Collection<E> {
        /**
         * A set with the elements.
         */
        private final Set<E> elements = new HashSet<E>();

        @Override
        public boolean contains(final E element) {
            return elements.contains(element);
        }

        @Override
        public E get() throws IndexOutOfBoundsException {
            if (isEmpty()) {
                throw new IndexOutOfBoundsException("Cannot return an element from an empty collection.");
            } else {
                return elements.iterator().next();
            }
        }

        @Override
        public int size() {
            return elements.size();
        }
    }

    /**
     * Returns a new empty collection.
     *
     * @return A new empty collection.
     */
    static Collection empty() {
        return new DefaultCollection();
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

    /**
     * Returns the number of elements in the collection.
     *
     * @return The number of elements in the collection.
     */
    int size();
}
