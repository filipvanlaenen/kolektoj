package net.filipvanlaenen.kolektoj.array;

import java.util.Comparator;

import net.filipvanlaenen.kolektoj.Collection.ElementCardinality;
import net.filipvanlaenen.kolektoj.CollectionTestBase.ElementWithCollidingHash;
import net.filipvanlaenen.kolektoj.SortedCollectionTestBase;

/**
 * Unit tests on the {@link net.filipvanlaenen.kolektoj.array.SortedArrayCollection} class.
 */
public final class SortedArrayCollectionTest extends
        SortedCollectionTestBase<SortedArrayCollection<Integer>, SortedArrayCollection<ElementWithCollidingHash>> {
    @Override
    protected SortedArrayCollection<Integer> createCollection(final SortedArrayCollection<Integer> collection) {
        return createOrderedCollection(collection);
    }

    @Override
    protected SortedArrayCollection<Integer> createCollection(final ElementCardinality elementCardinality,
            final SortedArrayCollection<Integer> collection) {
        return new SortedArrayCollection<Integer>(elementCardinality, COMPARATOR, collection);
    }

    @Override
    protected SortedArrayCollection<ElementWithCollidingHash> createCollidingHashValuesCollection(
            final ElementWithCollidingHash... elements) {
        return new SortedArrayCollection<ElementWithCollidingHash>(COLLIDING_HASH_COMPARATOR, elements);
    }

    @Override
    protected SortedArrayCollection<Integer> createOrderedCollection(final ElementCardinality elementCardinality,
            final Integer... integers) {
        return new SortedArrayCollection<Integer>(elementCardinality, COMPARATOR, integers);
    }

    @Override
    protected SortedArrayCollection<Integer> createOrderedCollection(final Integer... integers) {
        return createSortedCollection(COMPARATOR, integers);
    }

    @Override
    protected SortedArrayCollection<Integer> createOrderedCollection(final SortedArrayCollection<Integer> collection) {
        return new SortedArrayCollection<Integer>(COMPARATOR, collection);
    }

    @Override
    protected SortedArrayCollection<Integer> createSortedCollection(final Comparator<Integer> comparator,
            final Integer... integers) {
        return new SortedArrayCollection<Integer>(comparator, integers);
    }
}
