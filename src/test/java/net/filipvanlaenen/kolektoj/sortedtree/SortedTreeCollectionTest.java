package net.filipvanlaenen.kolektoj.sortedtree;

import net.filipvanlaenen.kolektoj.ModifiableCollectionTestBase;

import java.util.Comparator;
import java.util.Objects;

import net.filipvanlaenen.kolektoj.Collection.ElementCardinality;

/**
 * Unit tests on the {@link net.filipvanlaenen.kolektoj.sortedtree.SortedTreeCollection} class.
 */
public final class SortedTreeCollectionTest extends ModifiableCollectionTestBase<SortedTreeCollection<Integer>> {
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
    protected SortedTreeCollection<Integer> createModifiableCollection(final Integer... integers) {
        return new SortedTreeCollection<Integer>(COMPARATOR, integers);
    }

    @Override
    protected SortedTreeCollection<Integer> createModifiableCollection(final ElementCardinality elementCardinality,
            final Integer... integers) {
        return new SortedTreeCollection<Integer>(elementCardinality, COMPARATOR, integers);
    }
}
