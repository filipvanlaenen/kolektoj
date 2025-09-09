package net.filipvanlaenen.kolektoj;

import java.util.Iterator;
import java.util.Spliterator;

/**
 * Class wrapping around the default ordered collection implementation, but with the modification that the
 * <code>equals</code> method returns <code>true</code> if the collection values are equal and in the same order.
 * Ordered value collections can be used as fields in record classes.
 *
 * @param <E> The element type.
 */
public final class OrderedValueCollection<E> implements OrderedCollection<E> {
    /**
     * The magic number 31.
     */
    private static final int THIRTY_ONE = 31;
    /**
     * The ordered collection to wrap around.
     */
    private final OrderedCollection<E> collection;

    /**
     * Private constructor to avoid instantiation outside the factory methods.
     *
     * @param collection The ordered collection to wrap around.
     */
    private OrderedValueCollection(final OrderedCollection<E> collection) {
        this.collection = collection;
    }

    /**
     * Returns a new empty ordered value collection.
     *
     * @param <F> The element type.
     * @return A new empty ordered value collection.
     */
    public static <F> OrderedValueCollection<F> empty() {
        return new OrderedValueCollection<F>(OrderedCollection.empty());
    }

    /**
     * Returns a new ordered value collection with the specified elements.
     *
     * @param <F>      The element type.
     * @param elements The elements for the new ordered value collection.
     * @return A new ordered value collection with the specified elements.
     */
    public static <F> OrderedValueCollection<F> of(final F... elements) {
        return new OrderedValueCollection<F>(OrderedCollection.of(elements));
    }

    /**
     * Returns a new ordered value collection with the specified element cardinality and the elements.
     *
     * @param <F>                The element type.
     * @param elementCardinality The element cardinality.
     * @param elements           The elements for the new ordered value collection.
     * @return A new ordered value collection with the specified element cardinality and the elements.
     */
    public static <F> OrderedValueCollection<F> of(final ElementCardinality elementCardinality, final F... elements) {
        return new OrderedValueCollection<F>(OrderedCollection.of(elementCardinality, elements));
    }

    /**
     * Returns a new ordered value collection cloned from the provided ordered collection.
     *
     * @param <F>        The element type.
     * @param collection The original ordered collection.
     * @return A new ordered value collection cloned from the provided ordered collection.
     */
    static <F> OrderedValueCollection<F> of(final OrderedCollection<? extends F> collection) {
        return new OrderedValueCollection<F>(OrderedCollection.of(collection));
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
        if (object instanceof OrderedValueCollection) {
            Object[] thisArray = toArray();
            Object[] otherArray = ((OrderedValueCollection<?>) object).toArray();
            int length = thisArray.length;
            if (length != otherArray.length) {
                return false;
            }
            for (int i = 0; i < length; i++) {
                if (!thisArray[i].equals(otherArray[i])) {
                    return false;
                }
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    public E get() throws IndexOutOfBoundsException {
        return collection.get();
    }

    @Override
    public E getAt(final int index) throws IndexOutOfBoundsException {
        return collection.getAt(index);
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
                // EQMU: Replacing the integer addition with subtraction below produces an equivalent mutant
                // EQMU: Replacing the integer multiplication with division below produces an equivalent mutant
                result = result * THIRTY_ONE + element.hashCode();
            }
        }
        return result;
    }

    @Override
    public int indexOf(final E element) {
        return collection.indexOf(element);
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
