package net.filipvanlaenen.kolektoj.sortedtree;

import java.util.Comparator;
import java.util.Objects;

import net.filipvanlaenen.kolektoj.Collection;
import net.filipvanlaenen.kolektoj.Collection.ElementCardinality;
import net.filipvanlaenen.kolektoj.OrderedCollectionTestBase;

public class SortedTreeCollectionTest extends OrderedCollectionTestBase<SortedTreeCollection<Integer>> {
    private static final Comparator<Integer> COMPARATOR = new Comparator<Integer>() {
        @Override
        public int compare(final Integer i1, final Integer i2) {
            if (Objects.equals(i1, i2)) {
                return 0;
            } else if (i1 == null) {
                return -1;
            } else if (i2 == null) {
                return 1;
            } else if (i1 < i2) {
                return -1;
            } else {
                return 1;
            }
        }
    };

    @Override
    protected SortedTreeCollection<Integer> createOrderedCollection(final Collection<Integer> collection,
            final Comparator<Integer> comparator) {
        return new SortedTreeCollection<Integer>(comparator, collection);
    }

    @Override
    protected SortedTreeCollection<Integer> createOrderedCollection(ElementCardinality elementCardinality,
            Integer... integers) {
        return new SortedTreeCollection<Integer>(elementCardinality, COMPARATOR, integers);
    }

    @Override
    protected SortedTreeCollection<Integer> createOrderedCollection(Integer... integers) {
        return new SortedTreeCollection<Integer>(COMPARATOR, integers);
    }

    @Override
    protected SortedTreeCollection<Integer> createOrderedCollection(SortedTreeCollection<Integer> collection) {
        return new SortedTreeCollection<Integer>(COMPARATOR, collection);
    }
}
