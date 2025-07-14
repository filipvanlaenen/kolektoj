package net.filipvanlaenen.kolektoj;

import static net.filipvanlaenen.kolektoj.Map.KeyAndValueCardinality.DISTINCT_KEYS;
import static net.filipvanlaenen.kolektoj.Map.KeyAndValueCardinality.DUPLICATE_KEYS_WITH_DISTINCT_VALUES;
import static net.filipvanlaenen.kolektoj.Map.KeyAndValueCardinality.DUPLICATE_KEYS_WITH_DUPLICATE_VALUES;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Comparator;
import java.util.Objects;

import org.junit.jupiter.api.Test;

import net.filipvanlaenen.kolektoj.Map.Entry;

/**
 * Unit tests on the {@link net.filipvanlaenen.kolektoj.ModifiableSortedMap} class.
 */
public class ModifiableSortedMapTest {
    /**
     * The magic number three.
     */
    private static final int THREE = 3;
    /**
     * The magic number four.
     */
    private static final int FOUR = 4;
    /**
     * The magic number five.
     */
    private static final int FIVE = 5;
    /**
     * A comparator ordering integers in the natural order, but in addition handles <code>null</code> as the lowest
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

    /**
     * Verifies that a modifiable sorted map constructed with the empty factory method is empty.
     */
    @Test
    public void emptyShouldConstructAnEmptyMap() {
        assertTrue(ModifiableSortedMap.empty(COMPARATOR).isEmpty());
    }

    /**
     * Verifies that a modifiable sorted map constructed with one key and value contains an entry with the key and
     * value.
     */
    @Test
    public void ofShouldConstructAMapContainingAnEntryWithTheKeyAndValue() {
        ModifiableSortedMap<Integer, String> actual = ModifiableSortedMap.of(COMPARATOR, 1, "one");
        assertEquals(1, actual.size());
        assertTrue(actual.contains(new Entry<Integer, String>(1, "one")));
    }

    /**
     * Verifies that a modifiable sorted map constructed with two keys and values contains two entries with the keys and
     * values.
     */
    @Test
    public void ofShouldConstructAMapContainingTwoEntriesWithTheKeysAndValues() {
        ModifiableSortedMap<Integer, String> actual = ModifiableSortedMap.of(COMPARATOR, 1, "one", 2, "two");
        assertEquals(2, actual.size());
        assertTrue(actual.contains(new Entry<Integer, String>(1, "one")));
        assertTrue(actual.contains(new Entry<Integer, String>(2, "two")));
    }

    /**
     * Verifies that a modifiable sorted map constructed with three keys and values contains two entries with the keys
     * and values.
     */
    @Test
    public void ofShouldConstructAMapContainingThreeEntriesWithTheKeysAndValues() {
        ModifiableSortedMap<Integer, String> actual =
                ModifiableSortedMap.of(COMPARATOR, 1, "one", 2, "two", THREE, "three");
        assertEquals(THREE, actual.size());
        assertTrue(actual.contains(new Entry<Integer, String>(1, "one")));
        assertTrue(actual.contains(new Entry<Integer, String>(2, "two")));
        assertTrue(actual.contains(new Entry<Integer, String>(THREE, "three")));
    }

    /**
     * Verifies that a modifiable sorted map constructed with four keys and values contains four entries with the keys
     * and values.
     */
    @Test
    public void ofShouldConstructAMapContainingFourEntriesWithTheKeysAndValues() {
        ModifiableSortedMap<Integer, String> actual =
                ModifiableSortedMap.of(COMPARATOR, 1, "one", 2, "two", THREE, "three", FOUR, "four");
        assertEquals(FOUR, actual.size());
        assertTrue(actual.contains(new Entry<Integer, String>(1, "one")));
        assertTrue(actual.contains(new Entry<Integer, String>(2, "two")));
        assertTrue(actual.contains(new Entry<Integer, String>(THREE, "three")));
        assertTrue(actual.contains(new Entry<Integer, String>(FOUR, "four")));
    }

    /**
     * Verifies that a modifiable sorted map constructed with five keys and values contains five entries with the keys
     * and values.
     */
    @Test
    public void ofShouldConstructAMapContainingFiveEntriesWithTheKeysAndValues() {
        ModifiableSortedMap<Integer, String> actual =
                ModifiableSortedMap.of(COMPARATOR, 1, "one", 2, "two", THREE, "three", FOUR, "four", FIVE, "five");
        assertEquals(FIVE, actual.size());
        assertTrue(actual.contains(new Entry<Integer, String>(1, "one")));
        assertTrue(actual.contains(new Entry<Integer, String>(2, "two")));
        assertTrue(actual.contains(new Entry<Integer, String>(THREE, "three")));
        assertTrue(actual.contains(new Entry<Integer, String>(FOUR, "four")));
        assertTrue(actual.contains(new Entry<Integer, String>(FIVE, "five")));
    }

    /**
     * Verifies that a modifiable sorted map constructed with entries is constructed correctly.
     */
    @Test
    public void ofShouldConstructAMapWithEntriesCorrectly() {
        ModifiableSortedMap<Integer, String> actual = ModifiableSortedMap.of(COMPARATOR,
                new Entry<Integer, String>(1, "one"), new Entry<Integer, String>(2, "two"));
        assertEquals(DISTINCT_KEYS, actual.getKeyAndValueCardinality());
        assertEquals(2, actual.size());
        assertTrue(actual.contains(new Entry<Integer, String>(1, "one")));
        assertTrue(actual.contains(new Entry<Integer, String>(2, "two")));
    }

    /**
     * Verifies that a modifiable sorted map constructed with entries and key and value cardinality is constructed
     * correctly.
     */
    @Test
    public void ofShouldConstructAMapWithKeyAndValueCardinalityAndEntriesCorrectly() {
        ModifiableSortedMap<Integer, String> actual = ModifiableSortedMap.of(DUPLICATE_KEYS_WITH_DISTINCT_VALUES,
                COMPARATOR, new Entry<Integer, String>(1, "one"), new Entry<Integer, String>(2, "two"));
        assertEquals(DUPLICATE_KEYS_WITH_DISTINCT_VALUES, actual.getKeyAndValueCardinality());
        assertEquals(2, actual.size());
        assertTrue(actual.contains(new Entry<Integer, String>(1, "one")));
        assertTrue(actual.contains(new Entry<Integer, String>(2, "two")));
    }

    /**
     * Verifies that a modifiable sorted map with three keys and a default values is constructed correctly.
     */
    @Test
    public void ofShouldConstructAMapWithADefaultValueCorrectly() {
        ModifiableSortedMap<Integer, String> actual = ModifiableSortedMap.of(COMPARATOR, "", 1, 2, THREE);
        assertEquals(THREE, actual.size());
        assertTrue(actual.contains(new Entry<Integer, String>(1, "")));
        assertTrue(actual.contains(new Entry<Integer, String>(2, "")));
        assertTrue(actual.contains(new Entry<Integer, String>(THREE, "")));
    }

    /**
     * Verifies that a modifiable sorted map with entries and key and value cardinality and a default values is
     * constructed correctly.
     */
    @Test
    public void ofShouldConstructAMapWithKeyAndValueCardinalityAndADefaultValueCorrectly() {
        ModifiableSortedMap<Integer, String> actual =
                ModifiableSortedMap.of(DUPLICATE_KEYS_WITH_DUPLICATE_VALUES, COMPARATOR, "", 1, 1, 2, THREE);
        assertEquals(DUPLICATE_KEYS_WITH_DUPLICATE_VALUES, actual.getKeyAndValueCardinality());
        assertEquals(FOUR, actual.size());
        assertTrue(actual.contains(new Entry<Integer, String>(1, "")));
        assertTrue(actual.contains(new Entry<Integer, String>(2, "")));
        assertTrue(actual.contains(new Entry<Integer, String>(THREE, "")));
    }
}
