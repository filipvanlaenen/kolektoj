package net.filipvanlaenen.kolektoj.sortedtree;

import java.util.Comparator;

import net.filipvanlaenen.kolektoj.Collection.ElementCardinality;
import net.filipvanlaenen.kolektoj.SortedCollectionTestBase;

/**
 * Unit tests on the {@link net.filipvanlaenen.kolektoj.sortedtree.SortedTreeCollection} class.
 */
public final class SortedTreeCollectionTest extends SortedCollectionTestBase<SortedTreeCollection<Integer>> {
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
