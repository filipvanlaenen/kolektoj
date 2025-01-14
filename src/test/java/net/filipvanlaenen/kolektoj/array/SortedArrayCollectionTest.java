package net.filipvanlaenen.kolektoj.array;

import java.util.Comparator;
import java.util.Objects;

import net.filipvanlaenen.kolektoj.Collection.ElementCardinality;
import net.filipvanlaenen.kolektoj.OrderedCollectionTestBase;

/**
 * Unit tests on the {@link net.filipvanlaenen.kolektoj.array.SortedArrayCollection} class.
 */
public final class SortedArrayCollectionTest extends OrderedCollectionTestBase<SortedArrayCollection<Integer>> {
    /**
     * A comparator ordering integers in the natural order, but in addition handling <code>null</code> as the lowest
     * value.
     */
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
    protected SortedArrayCollection<Integer> createOrderedCollection(final ElementCardinality elementCardinality,
            final Integer... integers) {
        return new SortedArrayCollection<Integer>(elementCardinality, COMPARATOR, integers);
    }

    @Override
    protected SortedArrayCollection<Integer> createOrderedCollection(final Integer... integers) {
        return new SortedArrayCollection<Integer>(COMPARATOR, integers);
    }

    @Override
    protected SortedArrayCollection<Integer> createOrderedCollection(final SortedArrayCollection<Integer> collection) {
        return new SortedArrayCollection<Integer>(COMPARATOR, collection);
    }
}
