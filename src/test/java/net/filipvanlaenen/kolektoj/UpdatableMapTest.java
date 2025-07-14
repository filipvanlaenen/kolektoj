package net.filipvanlaenen.kolektoj;

import static net.filipvanlaenen.kolektoj.Map.KeyAndValueCardinality.DISTINCT_KEYS;
import static net.filipvanlaenen.kolektoj.Map.KeyAndValueCardinality.DUPLICATE_KEYS_WITH_DUPLICATE_VALUES;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import net.filipvanlaenen.kolektoj.Map.Entry;

/**
 * Unit tests on the {@link net.filipvanlaenen.kolektoj.UpdatableMap} class.
 */
public class UpdatableMapTest {
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
     * Verifies that an empty updatable map is empty.
     */
    @Test
    public void isEmptyShouldReturnTrueForAnEmptyUpdatableMap() {
        assertTrue(UpdatableMap.empty().isEmpty());
    }

    /**
     * Verifies that an updatable map constructed with an entry contains that entry with the key and value.
     */
    @Test
    public void ofShouldConstructAnUpdatableMapContainingAnEntry() {
        UpdatableMap<Integer, String> actual = UpdatableMap.of(new Entry<Integer, String>(1, "one"));
        assertEquals(1, actual.size());
        assertTrue(actual.contains(new Entry<Integer, String>(1, "one")));
    }

    /**
     * Verifies that an updatable map constructed with one key and value contains an entry with the key and value.
     */
    @Test
    public void ofShouldConstructAnUpdatableMapContainingAnEntryWithTheKeyAndValue() {
        UpdatableMap<Integer, String> actual = UpdatableMap.of(1, "one");
        assertEquals(1, actual.size());
        assertTrue(actual.contains(new Entry<Integer, String>(1, "one")));
    }

    /**
     * Verifies that an updatable map constructed with two keys and values contains two entries with the keys and
     * values.
     */
    @Test
    public void ofShouldConstructAnUpdatableMapContainingTwoEntriesWithTheKeysAndValues() {
        UpdatableMap<Integer, String> actual = UpdatableMap.of(1, "one", 2, "two");
        assertEquals(2, actual.size());
        assertTrue(actual.contains(new Entry<Integer, String>(1, "one")));
        assertTrue(actual.contains(new Entry<Integer, String>(2, "two")));
    }

    /**
     * Verifies that an updatable map constructed with three keys and values contains three entries with the keys and
     * values.
     */
    @Test
    public void ofShouldConstructAnUpdatableMapContainingThreeEntriesWithTheKeysAndValues() {
        UpdatableMap<Integer, String> actual = UpdatableMap.of(1, "one", 2, "two", THREE, "three");
        assertEquals(THREE, actual.size());
        assertTrue(actual.contains(new Entry<Integer, String>(1, "one")));
        assertTrue(actual.contains(new Entry<Integer, String>(2, "two")));
        assertTrue(actual.contains(new Entry<Integer, String>(THREE, "three")));
    }

    /**
     * Verifies that an updatable map constructed with four keys and values contains four entries with the keys and
     * values.
     */
    @Test
    public void ofShouldConstructAnUpdatableMapContainingFourEntriesWithTheKeysAndValues() {
        UpdatableMap<Integer, String> actual = UpdatableMap.of(1, "one", 2, "two", THREE, "three", FOUR, "four");
        assertEquals(FOUR, actual.size());
        assertTrue(actual.contains(new Entry<Integer, String>(1, "one")));
        assertTrue(actual.contains(new Entry<Integer, String>(2, "two")));
        assertTrue(actual.contains(new Entry<Integer, String>(THREE, "three")));
        assertTrue(actual.contains(new Entry<Integer, String>(FOUR, "four")));
    }

    /**
     * Verifies that an updatable map constructed with five keys and values contains five entries with the keys and
     * values.
     */
    @Test
    public void ofShouldConstructAnUpdatableMapContainingFiveEntriesWithTheKeysAndValues() {
        UpdatableMap<Integer, String> actual =
                UpdatableMap.of(1, "one", 2, "two", THREE, "three", FOUR, "four", FIVE, "five");
        assertEquals(FIVE, actual.size());
        assertTrue(actual.contains(new Entry<Integer, String>(1, "one")));
        assertTrue(actual.contains(new Entry<Integer, String>(2, "two")));
        assertTrue(actual.contains(new Entry<Integer, String>(THREE, "three")));
        assertTrue(actual.contains(new Entry<Integer, String>(FOUR, "four")));
        assertTrue(actual.contains(new Entry<Integer, String>(FIVE, "five")));
    }

    /**
     * Verifies that an updatable map constructed with entries and key and value cardinality is constructed correctly.
     */
    @Test
    public void ofShouldConstructAnUpdatableMapWithKeyAndValueCardinalityAndEntriesCorrectly() {
        UpdatableMap<Integer, String> map = UpdatableMap.of(DISTINCT_KEYS, new Entry<Integer, String>(1, "one"),
                new Entry<Integer, String>(2, "two"));
        assertEquals(DISTINCT_KEYS, map.getKeyAndValueCardinality());
        assertEquals(2, map.size());
    }

    /**
     * Verifies that an updatable map constructed from another map is constructed correctly.
     */
    @Test
    public void ofShouldConstructAMapFromAnotherMapCorrectly() {
        Map<Integer, String> prototype = Map.of(1, "one", 2, "two", THREE, "three");
        UpdatableMap<Number, String> actual = UpdatableMap.of(prototype);
        assertTrue(actual.containsSame(prototype));
    }

    /**
     * Verifies that an updatable map with three keys and a default value is constructed correctly.
     */
    @Test
    public void ofShouldConstructAnUpdatableMapWithADefaultValue() {
        UpdatableMap<Integer, String> actual = UpdatableMap.<Integer, String>of("", 1, 2, THREE);
        assertEquals(THREE, actual.size());
        assertTrue(actual.contains(new Entry<Integer, String>(1, "")));
        assertTrue(actual.contains(new Entry<Integer, String>(2, "")));
        assertTrue(actual.contains(new Entry<Integer, String>(THREE, "")));
    }

    /**
     * Verifies that an updatable map with key and value cardinality and a default value is constructed correctly.
     */
    @Test
    public void ofShouldConstructAnUpdatableMapWithKeyAndValueCardinalityAndADefaultValue() {
        UpdatableMap<Integer, String> actual =
                UpdatableMap.<Integer, String>of(DUPLICATE_KEYS_WITH_DUPLICATE_VALUES, "", 1, 1, 2, THREE);
        assertEquals(DUPLICATE_KEYS_WITH_DUPLICATE_VALUES, actual.getKeyAndValueCardinality());
        assertEquals(FOUR, actual.size());
        assertTrue(actual.contains(new Entry<Integer, String>(1, "")));
        assertTrue(actual.contains(new Entry<Integer, String>(2, "")));
        assertTrue(actual.contains(new Entry<Integer, String>(THREE, "")));
    }
}
