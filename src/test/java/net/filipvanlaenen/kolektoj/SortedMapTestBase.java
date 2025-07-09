package net.filipvanlaenen.kolektoj;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Comparator;
import java.util.Objects;

import org.junit.jupiter.api.Test;

import net.filipvanlaenen.kolektoj.Map.Entry;
import net.filipvanlaenen.kolektoj.MapTestBase.KeyWithCollidingHash;

/**
 * Unit tests on implementations of the {@link net.filipvanlaenen.kolektoj.SortedMap} interface.
 *
 * @param <T>  The subclass type to be tested.
 * @param <TC> The subclass type to be tested, but with a key type with colliding hash values.
 */
public abstract class SortedMapTestBase<T extends SortedMap<Integer, String>,
        TC extends SortedMap<KeyWithCollidingHash, Integer>> extends MapTestBase<T, TC> {
    /**
     * A comparator ordering integers in the natural order, but in addition handling <code>null</code> as the lowest
     * value.
     */
    protected static final Comparator<Integer> COMPARATOR = new Comparator<Integer>() {
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

    /**
     * Verifies that <code>getComparator</code> returns the comparator used to create the sorted map.
     */
    @Test
    public void getComparatorShouldReturnTheProvidedComparator() {
        T map = createMap(new Entry<Integer, String>(1, "one"), new Entry<Integer, String>(2, "two"));
        assertEquals(COMPARATOR, map.getComparator());
    }
}
