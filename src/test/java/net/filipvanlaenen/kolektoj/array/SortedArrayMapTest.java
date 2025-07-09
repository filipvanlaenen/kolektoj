package net.filipvanlaenen.kolektoj.array;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Comparator;
import java.util.Objects;

import org.junit.jupiter.api.Test;

import net.filipvanlaenen.kolektoj.Map.Entry;
import net.filipvanlaenen.kolektoj.Map.KeyAndValueCardinality;
import net.filipvanlaenen.kolektoj.MapTestBase;
import net.filipvanlaenen.kolektoj.MapTestBase.KeyWithCollidingHash;
import net.filipvanlaenen.kolektoj.SortedMap;

/**
 * Unit tests on the {@link net.filipvanlaenen.kolektoj.array.SortedArrayMap} class.
 */
public final class SortedArrayMapTest
        extends MapTestBase<SortedArrayMap<Integer, String>, SortedArrayMap<KeyWithCollidingHash, Integer>> {
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
    protected SortedArrayMap<Integer, String> createMap(final Entry<Integer, String>... entries) {
        return new SortedArrayMap<Integer, String>(COMPARATOR, entries);
    }

    @Override
    protected SortedArrayMap<Integer, String> createMap(final KeyAndValueCardinality keyAndValueCardinality,
            final Entry<Integer, String>... entries) {
        return new SortedArrayMap<Integer, String>(keyAndValueCardinality, COMPARATOR, entries);
    }

    @Override
    protected SortedArrayMap<KeyWithCollidingHash, Integer> createCollidingKeyHashMap(
            final Entry<KeyWithCollidingHash, Integer>... entries) {
        return new SortedArrayMap<KeyWithCollidingHash, Integer>(KEY_WITH_COLLIDING_HASH_COMPARATOR, entries);
    }

    /**
     * Verifies that <code>getComparator</code> returns the comparator used to create the sorted map.
     */
    @Test
    public void getComparatorShouldReturnTheProvidedComparator() {
        SortedMap<Integer, String> map =
                createMap(new Entry<Integer, String>(1, "one"), new Entry<Integer, String>(2, "two"));
        assertEquals(COMPARATOR, map.getComparator());
    }
}
