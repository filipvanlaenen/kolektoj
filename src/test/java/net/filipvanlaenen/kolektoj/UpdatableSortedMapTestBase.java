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
     * The magic number three.
     */
    private static final int THREE = 3;
    /**
     * The magic number four.
     */
    private static final int FOUR = 4;
    /**
     * Sorted map with three entries.
     */
    private T map123 = createMap(ENTRY1, ENTRY2, ENTRY3);
    /**
     * Sorted map with two entries.
     */
    private T map13 = createMap(ENTRY1, ENTRY3);
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
     * Verifies that <code>getGreaterThan</code> on an empty map throws IndexOutOfBoundsException.
     */
    @Test
    public void getGreaterThanShouldThrowExceptionWhenCalledOnAnEmptyMap() {
        IndexOutOfBoundsException exception =
                assertThrows(IndexOutOfBoundsException.class, () -> createMap().getGreaterThan(2));
        assertEquals("Cannot return an entry from an empty map.", exception.getMessage());
    }

    /**
     * Verifies that <code>getGreaterThan</code> returns an entry that's greater than the provided key.
     */
    @Test
    public void getGreaterThanShouldReturnTheFirstEntryThatIsGreater() {
        assertEquals(ENTRY3, map123.getGreaterThan(2));
    }

    /**
     * Verifies that <code>getGreaterThan</code> throws IndexOutOfBoundsException when there's no greater element.
     */
    @Test
    public void getGreaterThanShouldThrowExceptionWhenCalledWithGreatestKey() {
        IndexOutOfBoundsException exception =
                assertThrows(IndexOutOfBoundsException.class, () -> map123.getGreaterThan(THREE));
        assertEquals("Cannot return an entry from the map with a key that's greater than the provided value.",
                exception.getMessage());
    }

    /**
     * Verifies that <code>getGreaterThanOrEqualTo</code> on an empty map throws IndexOutOfBoundsException.
     */
    @Test
    public void getGreaterThanOrEqualToShouldThrowExceptionWhenCalledOnAnEmptyMap() {
        IndexOutOfBoundsException exception =
                assertThrows(IndexOutOfBoundsException.class, () -> createMap().getGreaterThanOrEqualTo(2));
        assertEquals("Cannot return an entry from an empty map.", exception.getMessage());
    }

    /**
     * Verifies that <code>getGreaterThanOrEqualTo</code> returns an entry with a key that's equal to the provided value
     * if it's present.
     */
    @Test
    public void getGreaterThanOrEqualToShouldReturnTheEntryThatIsEqualIfPresent() {
        assertEquals(ENTRY2, map123.getGreaterThanOrEqualTo(2));
    }

    /**
     * Verifies that <code>getGreaterThanOrEqualTo</code> returns an entry with a key that's greater than if the
     * provided value is absent.
     */
    @Test
    public void getGreaterThanOrEqualToShouldReturnGreaterEntryIfProvidedKeyIsAbsent() {
        assertEquals(ENTRY3, map13.getGreaterThanOrEqualTo(2));
    }

    /**
     * Verifies that <code>getGreaterThanOrEqualTo</code> throws IndexOutOfBoundsException when there's no greater or
     * equal key.
     */
    @Test
    public void getGreaterThanOrEqualToShouldThrowExceptionWhenCalledWithGreaterThanGreatestKey() {
        IndexOutOfBoundsException exception =
                assertThrows(IndexOutOfBoundsException.class, () -> map123.getGreaterThanOrEqualTo(FOUR));
        assertEquals(
                "Cannot return an entry from the map with a key that's greater than or equal to the provided value.",
                exception.getMessage());
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
     * Verifies that trying to get the greatest key from an empty map throws IndexOutOfBoundsException.
     */
    @Test
    public void getGreatestKeyShouldThrowExceptionWhenCalledOnAnEmptyMap() {
        IndexOutOfBoundsException exception =
                assertThrows(IndexOutOfBoundsException.class, () -> createMap().getGreatestKey());
        assertEquals("Cannot return a key from an empty map.", exception.getMessage());
    }

    /**
     * Verifies that <code>getGreatestKey</code> returns the greatest key of a sorted map.
     */
    @Test
    public void getGreatestKeyShouldReturnTheGreatestKeyInASortedMap() {
        assertEquals(THREE, map123.getGreatestKey());
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

    /**
     * Verifies that <code>getLeastKey</code> returns the least key of a sorted map.
     */
    @Test
    public void getLeastKeyShouldReturnTheLeastKeyInASortedMap() {
        assertEquals(1, map123.getLeastKey());
    }

    /**
     * Verifies that trying to get the least key from an empty map throws IndexOutOfBoundsException.
     */
    @Test
    public void getLeastKeyShouldThrowExceptionWhenCalledOnAnEmptyMap() {
        IndexOutOfBoundsException exception =
                assertThrows(IndexOutOfBoundsException.class, () -> createMap().getLeastKey());
        assertEquals("Cannot return a key from an empty map.", exception.getMessage());
    }

    /**
     * Verifies that <code>getLessThan</code> on an empty map throws IndexOutOfBoundsException.
     */
    @Test
    public void getLessThanShouldThrowExceptionWhenCalledOnAnEmptyMap() {
        IndexOutOfBoundsException exception =
                assertThrows(IndexOutOfBoundsException.class, () -> createMap().getLessThan(2));
        assertEquals("Cannot return an entry from an empty map.", exception.getMessage());
    }

    /**
     * Verifies that <code>getLessThan</code> returns an entry that's less than the provided key.
     */
    @Test
    public void getLessThanShouldReturnTheLastEntryThatIsLess() {
        assertEquals(ENTRY1, map123.getLessThan(2));
    }

    /**
     * Verifies that <code>getLessThan</code> throws IndexOutOfBoundsException when there's no less key.
     */
    @Test
    public void getLessThanShouldThrowExceptionWhenCalledWithLeastKey() {
        IndexOutOfBoundsException exception =
                assertThrows(IndexOutOfBoundsException.class, () -> map123.getLessThan(1));
        assertEquals("Cannot return an entry from the map with a key that's less than the provided value.",
                exception.getMessage());
    }

    /**
     * Verifies that <code>getLessThanOrEqualTo</code> on an empty map throws IndexOutOfBoundsException.
     */
    @Test
    public void getLessThanOrEqualToShouldThrowExceptionWhenCalledOnAnEmptyMap() {
        IndexOutOfBoundsException exception =
                assertThrows(IndexOutOfBoundsException.class, () -> createMap().getLessThanOrEqualTo(2));
        assertEquals("Cannot return an entry from an empty map.", exception.getMessage());
    }

    /**
     * Verifies that <code>getLessThanOrEqualTo</code> returns an entry with a key that's equal to the provided value if
     * it's present.
     */
    @Test
    public void getLessThanOrEqualToShouldReturnTheEntryThatIsEqualIfPresent() {
        assertEquals(ENTRY2, map123.getLessThanOrEqualTo(2));
    }

    /**
     * Verifies that <code>getLessThanOrEqualTo</code> returns an entry that's less if the provided key is absent.
     */
    @Test
    public void getLessThanOrEqualToShouldReturnLessEntryIfProvidedKeyIsAbsent() {
        assertEquals(ENTRY1, map13.getLessThanOrEqualTo(2));
    }

    /**
     * Verifies that <code>getLessThanOrEqualTo</code> throws IndexOutOfBoundsException when there's no less or equal
     * key.
     */
    @Test
    public void getLessThanOrEqualToShouldThrowExceptionWhenCalledWithGreaterThanGreatestKey() {
        IndexOutOfBoundsException exception =
                assertThrows(IndexOutOfBoundsException.class, () -> map123.getLessThanOrEqualTo(0));
        assertEquals("Cannot return an entry from the map with a key that's less than or equal to the provided value.",
                exception.getMessage());
    }
}
