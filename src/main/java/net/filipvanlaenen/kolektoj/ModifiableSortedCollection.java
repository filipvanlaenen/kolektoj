package net.filipvanlaenen.kolektoj;

import java.util.Comparator;

import net.filipvanlaenen.kolektoj.sortedtree.ModifiableSortedTreeCollection;

/**
 * Interface defining the signature for all modifiable sorted collections.
 *
 * @param <E> The element type.
 */
public interface ModifiableSortedCollection<E> extends ModifiableCollection<E>, SortedCollection<E> {
    /**
     * Returns a new modifiable sorted collection containing all the elements present in the first collection, but not
     * in any of the other provided collections.
     *
     * This method corresponds to the difference (or relative complement) operation in set theory, denoted by the symbol
     * ∖, with {1, 2, 3} ∖ {2, 3, 4} = {1}.
     *
     * @param <F>         The element type.
     * @param comparator  The comparator by which to sort the elements.
     * @param collections The collections for which to calculate the difference.
     * @return A new modifiable sorted collection containing all the elements present in the first collection, but not
     *         in any of the other provided collections.
     */
    static <F> Collection<F> differenceOf(final Comparator<? super F> comparator,
            final Collection<? extends F>... collections) {
        if (collections.length == 0) {
            return empty(comparator);
        }
        ModifiableSortedCollection<F> result = ModifiableSortedCollection.of(comparator, collections[0]);
        for (int i = 1; i < collections.length; i++) {
            result.removeAll(collections[i]);
        }
        return result;
    }

    /**
     * Returns a new modifiable sorted collection containing all the elements present in the first collection, but not
     * in any of the other provided collections, sorted the same way as the first collection.
     *
     * This method corresponds to the difference (or relative complement) operation in set theory, denoted by the symbol
     * ∖, with {1, 2, 3} ∖ {2, 3, 4} = {1}.
     *
     * @param <F>              The element type.
     * @param sortedCollection The sorted collection from which to calculate the difference.
     * @param collections      The collections for which to calculate the difference.
     * @return A new modifiable sorted collection containing all the elements present in the first collection, but not
     *         in any of the other provided collections, sorted the same way as the first collection.
     */
    static <F> Collection<F> differenceOf(final SortedCollection<F> sortedCollection,
            final Collection<? extends F>... collections) {
        ModifiableSortedCollection<F> result = ModifiableSortedCollection.of(sortedCollection);
        for (int i = 0; i < collections.length; i++) {
            result.removeAll(collections[i]);
        }
        return result;
    }

    /**
     * Returns a new empty modifiable sorted collection.
     *
     * @param <F>        The element type.
     * @param comparator The comparator by which to sort the elements.
     * @return A new empty modifiable sorted ordered collection.
     */
    static <F> ModifiableSortedCollection<F> empty(final Comparator<? super F> comparator) {
        return new ModifiableSortedTreeCollection<F>(comparator);
    }

    /**
     * Returns a new modifiable sorted collection containing all the elements present in each of the provided
     * collections.
     *
     * This method corresponds to the intersection operation in set theory, denoted by the symbol ∩, with {1, 2, 3} ∩
     * {2, 3, 4} = {2, 3}.
     *
     * @param <F>         The element type.
     * @param comparator  The comparator by which to sort the elements.
     * @param collections The collections from which to calculate the intersection.
     * @return A new modifiable sorted collection containing all the elements present in each of the provided
     *         collections.
     */
    static <F> ModifiableSortedCollection<F> intersectionOf(final Comparator<? super F> comparator,
            final Collection<? extends F>... collections) {
        if (collections.length == 0) {
            return empty(comparator);
        }
        ModifiableSortedCollection<F> result = ModifiableSortedCollection.of(comparator, collections[0]);
        for (int i = 1; i < collections.length; i++) {
            result.retainAll(collections[i]);
        }
        return result;
    }

    /**
     * Returns a new modifiable sorted collection containing all the elements present in each of the provided
     * collections, sorted the same way as the first collection.
     *
     * This method corresponds to the intersection operation in set theory, denoted by the symbol ∩, with {1, 2, 3} ∩
     * {2, 3, 4} = {2, 3}.
     *
     * @param <F>              The element type.
     * @param sortedCollection The sorted collection from which to calculate the intersection.
     * @param collections      The other collections from which to calculate the intersection.
     * @return A new modifiable sorted collection containing all the elements present in each of the provided
     *         collections, sorted the same way as the first collection.
     */
    static <F> ModifiableSortedCollection<F> intersectionOf(final SortedCollection<F> sortedCollection,
            final Collection<? extends F>... collections) {
        ModifiableSortedCollection<F> result = of(sortedCollection);
        for (int i = 0; i < collections.length; i++) {
            result.retainAll(collections[i]);
        }
        return result;
    }

    /**
     * Returns a new modifiable sorted collection cloned from the provided collection.
     *
     * @param <F>        The element type.
     * @param comparator The comparator by which to sort the elements.
     * @param collection The original collection.
     * @return A new sorted modifiable collection cloned from the provided collection.
     */
    static <F> ModifiableSortedCollection<F> of(final Comparator<? super F> comparator,
            final Collection<? extends F> collection) {
        return new ModifiableSortedTreeCollection<F>(comparator, collection);
    }

    /**
     * Returns a new modifiable sorted collection cloned from a range in the provided ordered collection.
     *
     * @param <F>        The element type.
     * @param comparator The comparator by which to sort the elements.
     * @param collection The original ordered collection.
     * @param fromIndex  The index of the first element to be included in the new sorted collection.
     * @param toIndex    The index of the first element not to be included in the new sorted collection.
     * @return A new modifiable sorted collection cloned from a range in the provided ordered collection.
     */
    static <F> ModifiableSortedCollection<F> of(final Comparator<? super F> comparator,
            final OrderedCollection<? extends F> collection, final int fromIndex, final int toIndex) {
        ModifiableSortedCollection<F> result =
                new ModifiableSortedTreeCollection<F>(collection.getElementCardinality(), comparator);
        for (int i = fromIndex; i < toIndex; i++) {
            result.add(collection.getAt(i));
        }
        return result;
    }

    /**
     * Returns a new modifiable sorted collection with the specified elements.
     *
     * @param <F>        The element type.
     * @param comparator The comparator by which to sort the elements.
     * @param elements   The elements for the new modifiable sorted collection.
     * @return A new modifiable sorted collection with the specified elements.
     */
    static <F> ModifiableSortedCollection<F> of(final Comparator<? super F> comparator, final F... elements) {
        return new ModifiableSortedTreeCollection<F>(comparator, elements);
    }

    /**
     * Returns a new modifiable sorted collection with the specified element cardinality and the elements.
     *
     * @param <F>                The element type.
     * @param elementCardinality The element cardinality.
     * @param comparator         The comparator by which to sort the elements.
     * @param elements           The elements for the new modifiable sorted collection.
     * @return A new modifiable sorted collection with the specified element cardinality and the elements.
     */
    static <F> ModifiableSortedCollection<F> of(final ElementCardinality elementCardinality,
            final Comparator<? super F> comparator, final F... elements) {
        return new ModifiableSortedTreeCollection<F>(elementCardinality, comparator, elements);
    }

    /**
     * Returns a new modifiable sorted collection cloned from the provided collection with the specified element
     * cardinality.
     *
     * @param <F>                The element type.
     * @param elementCardinality The element cardinality.
     * @param comparator         The comparator by which to sort the elements.
     * @param collection         The original collection.
     * @return A new modifiable sorted collection cloned from a collection and with the specified element cardinality.
     */
    static <F> ModifiableSortedCollection<F> of(final ElementCardinality elementCardinality,
            final Comparator<? super F> comparator, final Collection<? extends F> collection) {
        return new ModifiableSortedTreeCollection<F>(elementCardinality, comparator, collection);
    }

    /**
     * Returns a new modifiable sorted collection cloned from the provided sorted collection.
     *
     * @param <F>        The element type.
     * @param collection The original sorted collection.
     * @return A new modifiable sorted collection cloned from the provided sorted collection.
     */
    static <F> ModifiableSortedCollection<F> of(final SortedCollection<F> collection) {
        return new ModifiableSortedTreeCollection<F>(collection.getComparator(), collection);
    }

    /**
     * Returns a new modifiable sorted collection cloned from the provided sorted collection.
     *
     * @param <F>        The element type.
     * @param collection The original sorted collection.
     * @param range      The range.
     * @return A new modifiable sorted collection cloned from the provided sorted collection.
     */
    static <F> ModifiableSortedCollection<F> of(final SortedCollection<F> collection, final Range<F> range) {
        ModifiableSortedCollection<F> result =
                new ModifiableSortedTreeCollection<F>(collection.getElementCardinality(), collection.getComparator());
        boolean below = true;
        for (F element : collection) {
            if (below && !range.isBelow(collection.getComparator(), element)) {
                below = false;
            }
            if (!below) {
                if (range.isAbove(collection.getComparator(), element)) {
                    break;
                }
                result.add(element);
            }
        }
        return result;
    }

    /**
     * Returns a new modifiable sorted collection with the specified comparator containing all the elements from the
     * provided collections.
     *
     * This method corresponds to the union operation in set theory, denoted by the symbol ∪, with {1, 2, 3} ∪ {2, 3, 4}
     * = {1, 2, 3, 4}. For multisets, allowing duplicate elements, {1, 2, 3} ∪ {2, 3, 4} = {1, 2, 2, 3, 3, 4}.
     *
     * @param <F>         The element type.
     * @param comparator  The comparator by which to sort the elements.
     * @param collections The collections from which to copy all the elements.
     * @return A new modifiable sorted collection with the specified comparator containing all the elements from the
     *         provided collections.
     */
    static <F> ModifiableSortedCollection<F> unionOf(final Comparator<? super F> comparator,
            final Collection<? extends F>... collections) {
        ModifiableSortedCollection<F> result = ModifiableSortedCollection.of(comparator);
        for (int i = 0; i < collections.length; i++) {
            result.addAll(collections[i]);
        }
        return result;
    }

    /**
     * Returns a new modifiable sorted collection containing all the elements from the provided collections, sorted the
     * same way as the first collection.
     *
     * This method corresponds to the union operation in set theory, denoted by the symbol ∪, with {1, 2, 3} ∪ {2, 3, 4}
     * = {1, 2, 3, 4}. For multisets, allowing duplicate elements, {1, 2, 3} ∪ {2, 3, 4} = {1, 2, 2, 3, 3, 4}.
     *
     * @param <F>              The element type.
     * @param sortedCollection The sorted collection from which to copy the comparator, the element cardinality and all
     *                         the elements.
     * @param collections      The collections from which to copy all the elements.
     * @return A new modifiable sorted collection containing all the elements from the provided collections, sorted the
     *         same way as the first collection.
     */
    static <F> ModifiableSortedCollection<F> unionOf(final SortedCollection<F> sortedCollection,
            final Collection<? extends F>... collections) {
        ModifiableSortedCollection<F> result = ModifiableSortedCollection.of(sortedCollection);
        for (int i = 0; i < collections.length; i++) {
            result.addAll(collections[i]);
        }
        return result;
    }

    /**
     * Removes an element from the sorted collection at a given position.
     *
     * @param index The position of the element that should be removed.
     * @return The element that was removed.
     * @throws IndexOutOfBoundsException Thrown if the index is out of bounds.
     */
    E removeAt(int index) throws IndexOutOfBoundsException;

    /**
     * Removes the greatest element of this collection.
     *
     * @return The element that was removed.
     * @throws IndexOutOfBoundsException Thrown if the collection is empty.
     */
    default E removeGreatest() throws IndexOutOfBoundsException {
        if (size() == 0) {
            throw new IndexOutOfBoundsException("Cannot remove an element from an empty collection.");
        } else {
            return removeAt(size() - 1);
        }
    }

    /**
     * Removes the least element of this collection.
     *
     * @return The element that was removed.
     * @throws IndexOutOfBoundsException Thrown if the collection is empty.
     */
    default E removeLeast() throws IndexOutOfBoundsException {
        if (size() == 0) {
            throw new IndexOutOfBoundsException("Cannot remove an element from an empty collection.");
        } else {
            return removeAt(0);
        }
    }
}
