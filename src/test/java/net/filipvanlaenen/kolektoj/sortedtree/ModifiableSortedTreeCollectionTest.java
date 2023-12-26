package net.filipvanlaenen.kolektoj.sortedtree;

import java.util.Comparator;
import java.util.Objects;

import net.filipvanlaenen.kolektoj.Collection.ElementCardinality;
import net.filipvanlaenen.kolektoj.ModifiableCollectionTestBase;

/**
 * Unit tests on the {@link net.filipvanlaenen.kolektoj.sortedtree.ModifiableSortedTreeCollection} class.
 */
public final class ModifiableSortedTreeCollectionTest extends ModifiableCollectionTestBase<ModifiableSortedTreeCollection<Integer>> {
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
    protected ModifiableSortedTreeCollection<Integer> createModifiableCollection(final Integer... integers) {
        return new ModifiableSortedTreeCollection<Integer>(COMPARATOR, integers);
    }

    @Override
    protected ModifiableSortedTreeCollection<Integer> createModifiableCollection(final ElementCardinality elementCardinality,
            final Integer... integers) {
        return new ModifiableSortedTreeCollection<Integer>(elementCardinality, COMPARATOR, integers);
    }
}
