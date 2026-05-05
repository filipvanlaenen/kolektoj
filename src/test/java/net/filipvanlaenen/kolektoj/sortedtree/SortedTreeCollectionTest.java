package net.filipvanlaenen.kolektoj.sortedtree;

import java.util.Comparator;

import net.filipvanlaenen.kolektoj.Collection.ElementCardinality;
import net.filipvanlaenen.kolektoj.CollectionTestBase.ElementWithCollidingHash;
import net.filipvanlaenen.kolektoj.SortedCollectionTestBase;

/**
 * Unit tests on the {@link net.filipvanlaenen.kolektoj.sortedtree.SortedTreeCollection} class.
 */
public final class SortedTreeCollectionTest extends
        SortedCollectionTestBase<SortedTreeCollection<Integer>, SortedTreeCollection<ElementWithCollidingHash>> {
    @Override
    protected SortedTreeCollection<Integer> createCollection(final SortedTreeCollection<Integer> collection) {
        return createOrderedCollection(collection);
    }

    @Override
    protected SortedTreeCollection<Integer> createCollection(final ElementCardinality elementCardinality,
            final SortedTreeCollection<Integer> collection) {
        return new SortedTreeCollection<Integer>(elementCardinality, COMPARATOR, collection);
    }

    @Override
    protected SortedTreeCollection<ElementWithCollidingHash> createCollidingHashValuesCollection(
            final ElementWithCollidingHash... elements) {
        return new SortedTreeCollection<ElementWithCollidingHash>(COLLIDING_HASH_COMPARATOR, elements);
    }

    @Override
    protected SortedTreeCollection<Integer> createOrderedCollection(final ElementCardinality elementCardinality,
            final Integer... integers) {
        return new SortedTreeCollection<Integer>(elementCardinality, COMPARATOR, integers);
    }

    @Override
    protected SortedTreeCollection<Integer> createOrderedCollection(final Integer... integers) {
        return createSortedCollection(COMPARATOR, integers);
    }

    @Override
    protected SortedTreeCollection<Integer> createOrderedCollection(final SortedTreeCollection<Integer> collection) {
        return new SortedTreeCollection<Integer>(COMPARATOR, collection);
    }

    @Override
    protected SortedTreeCollection<Integer> createSortedCollection(final Comparator<Integer> comparator,
            final Integer... integers) {
        return new SortedTreeCollection<Integer>(comparator, integers);
    }
}
