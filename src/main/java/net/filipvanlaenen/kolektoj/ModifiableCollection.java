package net.filipvanlaenen.kolektoj;

import java.util.function.Predicate;

import net.filipvanlaenen.kolektoj.array.ModifiableArrayCollection;

/**
 * Interface defining the signature for all modifiable collections.
 *
 * @param <E> The element type.
 */
public interface ModifiableCollection<E> extends Collection<E> {
    /**
     * Returns a new empty modifiable collection.
     *
     * @param <E> The element type.
     * @return A new empty modifiable collection.
     */
    static <E> ModifiableCollection<E> empty() {
        return new ModifiableArrayCollection<E>();
    }

    /**
     * Returns a new modifiable collection with the specified elements.
     *
     * @param <E>      The element type.
     * @param elements The elements for the new modifiable collection.
     * @return A new modifiable collection with the specified elements.
     */
    static <E> ModifiableCollection<E> of(final E... elements) {
        return new ModifiableArrayCollection<E>(elements);
    }

    /**
     * Returns a new modifiable collection with the specified element cardinality and the elements.
     *
     * @param <E>                The element type.
     * @param elementCardinality The element cardinality.
     * @param elements           The elements for the new collection.
     * @return A new modifiable collection with the specified element cardinality and the elements.
     */
    static <E> ModifiableCollection<E> of(final ElementCardinality elementCardinality, final E... elements) {
        return new ModifiableArrayCollection<E>(elementCardinality, elements);
    }

    /**
     * Adds an element to this collection and returns whether it increased the size of the collection.
     *
     * @param element The element to be added to the collection.
     * @return True if the size of the collection increased after adding the element.
     */
    boolean add(E element);

    /**
     * Adds elements from a collection to this collection and returns whether it increased the size of the collection.
     *
     * @param collection A collection from which to add elements.
     * @return True if the size of the collection increased after adding the elements of the collection.
     */
    boolean addAll(Collection<? extends E> collection);

    /**
     * Removes all elements from the collection.
     */
    void clear();

    /**
     * Removes an element from this collection if it is present.
     *
     * @param element The element to be removed from the collection.
     * @return True if the element was present in the collection.
     */
    boolean remove(E element);

    /**
     * Removes all elements from a collection from this collection and returns whether it decreased the size of the
     * collection.
     *
     * @param collection A collection with elements to be removed from this collection.
     * @return True if the size of the collection decreased after removing the elements of the collection.
     */
    boolean removeAll(Collection<? extends E> collection);

    /**
     * Removes all elements from this collection that satisfy the given predicate, and returns whether it decreased the
     * size of the collection.
     * 
     * @param predicate The predicate to be applied to each element of the collection.
     * @return True if the size of the collection decreased after removing the elements that satisfied the given
     *         predicate.
     */
    boolean removeIf(Predicate<? super E> predicate);

    /**
     * Retains elements from a collection in this collection and removes all other, and returns whether it decreased the
     * size of the collection.
     *
     * @param collection A collection with elements to retain in this collection.
     * @return True if the size of the collection decreased after retaining only the elements from the provided
     *         collection.
     */
    boolean retainAll(Collection<? extends E> collection);
}
