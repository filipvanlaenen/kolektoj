package net.filipvanlaenen.kolektoj;

import java.util.Iterator;
import java.util.Spliterator;

/**
 * Class wrapping around the default collection implementation, but with the modification that the <code>equals</code>
 * method returns <code>true</code> if the collection values are equal. Value collections can be used as fields in
 * record classes.
 *
 * @param <E> The element type.
 */
public final class ValueCollection<E> implements Collection<E> {
    /**
     * The collection to wrap around.
     */
    private final Collection<E> collection;

    /**
     * Private constructor to avoid instantiation outside the factory methods.
     *
     * @param collection The collection to wrap around.
     */
    private ValueCollection(final Collection<E> collection) {
        this.collection = collection;
    }

    /**
     * Returns a new empty value collection.
     *
     * @param <F> The element type.
     * @return A new empty value collection.
     */
    public static <F> ValueCollection<F> empty() {
        return new ValueCollection<F>(Collection.empty());
    }

    /**
     * Returns a new value collection cloned from the provided collection.
     *
     * @param <F>        The element type.
     * @param collection The original collection.
     * @return A new value collection cloned from the provided collection.
     */
    static <F> ValueCollection<F> of(final Collection<? extends F> collection) {
        return new ValueCollection<F>(Collection.of(collection));
    }

    /**
     * Returns a new value collection with the specified elements.
     *
     * @param <F>      The element type.
     * @param elements The elements for the new value collection.
     * @return A new value collection with the specified elements.
     */
    public static <F> ValueCollection<F> of(final F... elements) {
        return new ValueCollection<F>(Collection.of(elements));
    }

    /**
     * Returns a new value collection with the specified element cardinality and the elements.
     *
     * @param <F>                The element type.
     * @param elementCardinality The element cardinality.
     * @param elements           The elements for the new value collection.
     * @return A new value collection with the specified element cardinality and the elements.
     */
    public static <F> ValueCollection<F> of(final ElementCardinality elementCardinality, final F... elements) {
        return new ValueCollection<F>(Collection.of(elementCardinality, elements));
    }

    @Override
    public boolean contains(final E element) {
        return collection.contains(element);
    }

    @Override
    public boolean containsAll(final Collection<?> otherCollection) {
        return collection.containsAll(otherCollection);
    }

    @Override
    public boolean equals(final Object object) {
        if (object instanceof ValueCollection) {
            return collection.containsSame((ValueCollection<?>) object);
        } else {
            return false;
        }
    }

    @Override
    public E get() throws IndexOutOfBoundsException {
        return collection.get();
    }

    @Override
    public ElementCardinality getElementCardinality() {
        return collection.getElementCardinality();
    }

    @Override
    public int hashCode() {
        int result = 1;
        for (E element : collection) {
            if (element != null) {
                // EQMU: Replacing the integer multiplication with division below produces an equivalent mutant (because
                // the result is initialized to 1).
                result *= element.hashCode();
            }
        }
        return result;
    }

    @Override
    public Iterator<E> iterator() {
        return collection.iterator();
    }

    @Override
    public int size() {
        return collection.size();
    }

    @Override
    public Spliterator<E> spliterator() {
        return collection.spliterator();
    }

    @Override
    public Object[] toArray() {
        return collection.toArray();
    }
}
