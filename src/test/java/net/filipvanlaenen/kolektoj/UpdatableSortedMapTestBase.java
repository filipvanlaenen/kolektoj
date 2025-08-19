package net.filipvanlaenen.kolektoj;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Comparator;
import java.util.Objects;

import org.junit.jupiter.api.Test;

import net.filipvanlaenen.kolektoj.Map.Entry;
import net.filipvanlaenen.kolektoj.MapTestBase.KeyWithCollidingHash;

/**
 * Unit tests on implementations of the {@link net.filipvanlaenen.kolektoj.UpdatableSortedMap} interface.
 *
 * @param <T>  The subclass type to be tested.
 * @param <TC> The subclass type to be tested, but with a key type with colliding hash values.
 */
public abstract class UpdatableSortedMapTestBase<T extends UpdatableSortedMap<Integer, String>,
        TC extends UpdatableSortedMap<KeyWithCollidingHash, Integer>> extends UpdatableMapTestBase<T, TC> {
    /**
     * Sorted map with three entries.
     */
    private T map123 = createMap(ENTRY1, ENTRY2, ENTRY3);
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

    /**
     * Verifies that trying to get the greatest entry from an empty map throws IndexOutOfBoundsException.
     */
    @Test
    public void getGreatestShouldThrowExceptionWhenCalledOnAnEmptyMap() {
        IndexOutOfBoundsException exception =
                assertThrows(IndexOutOfBoundsException.class, () -> createMap().getGreatest());
        assertEquals("Cannot return an entry from an empty map.", exception.getMessage());
    }

    /**
     * Verifies that <code>getGreatest</code> returns the greatest entry of a sorted map.
     */
    @Test
    public void getGreatestShouldReturnTheGreatestEntryInASortedMap() {
        assertEquals(ENTRY3, map123.getGreatest());
    }

    /**
     * Verifies that trying to get the least entry from an empty map throws IndexOutOfBoundsException.
     */
    @Test
    public void getLeastShouldThrowExceptionWhenCalledOnAnEmptyMap() {
        IndexOutOfBoundsException exception =
                assertThrows(IndexOutOfBoundsException.class, () -> createMap().getLeast());
        assertEquals("Cannot return an entry from an empty map.", exception.getMessage());
    }

    /**
     * Verifies that <code>getLeast</code> returns the greatest entry of a sorted map.
     */
    @Test
    public void getLeastShouldReturnTheGreatestEntryInASortedMap() {
        assertEquals(ENTRY1, map123.getLeast());
    }
}
