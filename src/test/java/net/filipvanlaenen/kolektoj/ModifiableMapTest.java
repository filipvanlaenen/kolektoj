package net.filipvanlaenen.kolektoj;

import static net.filipvanlaenen.kolektoj.Map.KeyAndValueCardinality.DISTINCT_KEYS;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import net.filipvanlaenen.kolektoj.Map.Entry;

/**
 * Unit tests on the {@link net.filipvanlaenen.kolektoj.ModifiableMap} class.
 */
public class ModifiableMapTest {
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
     * Verifies that an empty map is empty.
     */
    @Test
    public void isEmptyShouldReturnTrueForAnEmptyMap() {
        assertTrue(ModifiableMap.empty().isEmpty());
    }

    /**
     * Verifies that a map containing an element is not empty.
     */
    @Test
    public void isEmptyShouldReturnFalseForAMapContainingAnElement() {
        assertFalse(ModifiableMap.of(new Entry<Integer, Integer>(1, 1)).isEmpty());
    }

    /**
     * Verifies that a map constructed with one key and value contains an entry with the key and value.
     */
    @Test
    public void ofShouldConstructAMapContainingAnEntryWithTheKeyAndValue() {
        ModifiableMap<Integer, String> actual = ModifiableMap.of(1, "one");
        assertEquals(1, actual.size());
        assertTrue(actual.contains(new Entry<Integer, String>(1, "one")));
    }

    /**
     * Verifies that a map constructed with two keys and values contains two entries with the keys and values.
     */
    @Test
    public void ofShouldConstructAMapContainingTwoEntriesWithTheKeysAndValues() {
        ModifiableMap<Integer, String> actual = ModifiableMap.of(1, "one", 2, "two");
        assertEquals(2, actual.size());
        assertTrue(actual.contains(new Entry<Integer, String>(1, "one")));
        assertTrue(actual.contains(new Entry<Integer, String>(2, "two")));
    }

    /**
     * Verifies that a map constructed with three keys and values contains two entries with the keys and values.
     */
    @Test
    public void ofShouldConstructAMapContainingThreeEntriesWithTheKeysAndValues() {
        ModifiableMap<Integer, String> actual = ModifiableMap.of(1, "one", 2, "two", THREE, "three");
        assertEquals(THREE, actual.size());
        assertTrue(actual.contains(new Entry<Integer, String>(1, "one")));
        assertTrue(actual.contains(new Entry<Integer, String>(2, "two")));
        assertTrue(actual.contains(new Entry<Integer, String>(THREE, "three")));
    }

    /**
     * Verifies that a map constructed with four keys and values contains four entries with the keys and values.
     */
    @Test
    public void ofShouldConstructAMapContainingFourEntriesWithTheKeysAndValues() {
        ModifiableMap<Integer, String> actual = ModifiableMap.of(1, "one", 2, "two", THREE, "three", FOUR, "four");
        assertEquals(FOUR, actual.size());
        assertTrue(actual.contains(new Entry<Integer, String>(1, "one")));
        assertTrue(actual.contains(new Entry<Integer, String>(2, "two")));
        assertTrue(actual.contains(new Entry<Integer, String>(THREE, "three")));
        assertTrue(actual.contains(new Entry<Integer, String>(FOUR, "four")));
    }

    /**
     * Verifies that a map constructed with five keys and values contains five entries with the keys and values.
     */
    @Test
    public void ofShouldConstructAMapContainingFiveEntriesWithTheKeysAndValues() {
        ModifiableMap<Integer, String> actual =
                ModifiableMap.of(1, "one", 2, "two", THREE, "three", FOUR, "four", FIVE, "five");
        assertEquals(FIVE, actual.size());
        assertTrue(actual.contains(new Entry<Integer, String>(1, "one")));
        assertTrue(actual.contains(new Entry<Integer, String>(2, "two")));
        assertTrue(actual.contains(new Entry<Integer, String>(THREE, "three")));
        assertTrue(actual.contains(new Entry<Integer, String>(FOUR, "four")));
        assertTrue(actual.contains(new Entry<Integer, String>(FIVE, "five")));
    }

    /**
     * Verifies that a map constructed with entries and key and value cardinality is constructed correctly.
     */
    @Test
    public void ofShouldConstructAMapWithKeyAndValueCardinalityAndEntriesCorrectly() {
        ModifiableMap<Integer, String> map = ModifiableMap.of(DISTINCT_KEYS, new Entry<Integer, String>(1, "one"),
                new Entry<Integer, String>(2, "two"));
        assertEquals(DISTINCT_KEYS, map.getKeyAndValueCardinality());
        assertEquals(2, map.size());
    }

    /**
     * Verifies that put returns null when the key is absent in the map.
     */
    @Test
    public void putShouldReturnNullIfTheKeyIsNew() {
        assertNull(ModifiableMap.empty().put(1, "one"));
    }

    /**
     * Verifies that put returns the previous value if the key is present in the map.
     */
    @Test
    public void putShouldReturnPreviousValueIfTheKeyIsPresent() {
        assertEquals("one", ModifiableMap.of(1, "one").put(1, "another"));
    }

    /**
     * Verifies that put adds an entry if the key is absent in the map.
     */
    @Test
    public void putShouldAddEntryIfTheKeyIsAbsent() {
        ModifiableMap<Integer, String> map = ModifiableMap.empty();
        map.put(1, "one");
        assertTrue(map.contains(new Entry<Integer, String>(1, "one")));
    }

    /**
     * Verifies that put updates an entry if the key is present in the map.
     */
    @Test
    public void putShouldUpdateEntryIfTheKeyIsPresent() {
        ModifiableMap<Integer, String> map = ModifiableMap.of(1, "another");
        map.put(1, "one");
        assertTrue(map.contains(new Entry<Integer, String>(1, "one")));
    }

    /**
     * Verifies that putAll puts all entries in the provided map.
     */
    @Test
    public void putAllShouldPutAllEntriesFromTheMap() {
        ModifiableMap<Integer, String> map = ModifiableMap.of(1, "another");
        map.putAll(Map.of(1, "one", 2, "two"));
        assertTrue(map.contains(new Entry<Integer, String>(1, "one")));
        assertTrue(map.contains(new Entry<Integer, String>(2, "two")));
    }
}
