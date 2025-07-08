package net.filipvanlaenen.kolektoj.array;

import java.util.Comparator;

import net.filipvanlaenen.kolektoj.Collection.ElementCardinality;
import net.filipvanlaenen.kolektoj.SortedCollectionTestBase;

/**
 * Unit tests on the {@link net.filipvanlaenen.kolektoj.array.SortedArrayCollection} class.
 */
public final class SortedArrayCollectionTest extends SortedCollectionTestBase<SortedArrayCollection<Integer>> {
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
