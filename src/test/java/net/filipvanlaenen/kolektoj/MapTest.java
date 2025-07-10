package net.filipvanlaenen.kolektoj;

import static net.filipvanlaenen.kolektoj.Map.KeyAndValueCardinality.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import net.filipvanlaenen.kolektoj.Collection.ElementCardinality;
import net.filipvanlaenen.kolektoj.Map.Entry;
import net.filipvanlaenen.kolektoj.hash.HashMap;

/**
 * Unit tests on the {@link net.filipvanlaenen.kolektoj.Map} class.
 */
public class MapTest {
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
        assertTrue(Map.empty().isEmpty());
    }

    /**
     * Verifies that a map containing an element is not empty.
     */
    @Test
    public void isEmptyShouldReturnFalseForAMapContainingAnElement() {
        assertFalse(Map.of(new Entry<Integer, Integer>(1, 1)).isEmpty());
    }

    /**
     * Verifies that a map constructed with one key and value contains an entry with the key and value.
     */
    @Test
    public void ofShouldConstructAMapContainingAnEntryWithTheKeyAndValue() {
        Map<Integer, String> actual = Map.of(1, "one");
        assertEquals(1, actual.size());
        assertTrue(actual.contains(new Entry<Integer, String>(1, "one")));
    }

    /**
     * Verifies that a map constructed with two keys and values contains two entries with the keys and values.
     */
    @Test
    public void ofShouldConstructAMapContainingTwoEntriesWithTheKeysAndValues() {
        Map<Integer, String> actual = Map.of(1, "one", 2, "two");
        assertEquals(2, actual.size());
        assertTrue(actual.contains(new Entry<Integer, String>(1, "one")));
        assertTrue(actual.contains(new Entry<Integer, String>(2, "two")));
    }

    /**
     * Verifies that a map constructed with three keys and values contains three entries with the keys and values.
     */
    @Test
    public void ofShouldConstructAMapContainingThreeEntriesWithTheKeysAndValues() {
        Map<Integer, String> actual = Map.of(1, "one", 2, "two", THREE, "three");
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
        Map<Integer, String> actual = Map.of(1, "one", 2, "two", THREE, "three", FOUR, "four");
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
        Map<Integer, String> actual = Map.of(1, "one", 2, "two", THREE, "three", FOUR, "four", FIVE, "five");
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
        Map<Integer, String> map =
                Map.of(DISTINCT_KEYS, new Entry<Integer, String>(1, "one"), new Entry<Integer, String>(2, "two"));
        assertEquals(DISTINCT_KEYS, map.getKeyAndValueCardinality());
        assertEquals(2, map.size());
    }

    /**
     * Verifies that a map constructed from another map is constructed correctly.
     */
    @Test
    public void ofShouldConstructAMapFromAnotherMapCorrectly() {
        Map<Integer, String> prototype = Map.of(1, "one", 2, "two", THREE, "three");
        Map<Number, String> actual = Map.of(prototype);
        assertTrue(actual.containsSame(prototype));
    }

    /**
     * Verifies that the element cardinality for a map with duplicate keys and values is that duplicate elements are
     * allowed.
     */
    @Test
    public void getElementCardinalityShouldReturnDuplicateElementsForMapWithDuplicateKeysAndValues() {
        assertEquals(ElementCardinality.DUPLICATE_ELEMENTS,
                new HashMap<Integer, String>(DUPLICATE_KEYS_WITH_DUPLICATE_VALUES).getElementCardinality());
    }

    /**
     * Verifies that the element cardinality for a map with duplicate keys and distinct values is that duplicate
     * elements are not allowed.
     */
    @Test
    public void getElementCardinalityShouldReturnDistinctElementsForMapWithDuplicateKeysAndDistinctValues() {
        assertEquals(ElementCardinality.DISTINCT_ELEMENTS,
                new HashMap<Integer, String>(DUPLICATE_KEYS_WITH_DISTINCT_VALUES).getElementCardinality());
    }

    /**
     * Verifies that the element cardinality for a map with distinct keys is that duplicate elements are not allowed.
     */
    @Test
    public void getElementCardinalityShouldReturnDistinctElementsForMapWithDistinctKeys() {
        assertEquals(ElementCardinality.DISTINCT_ELEMENTS,
                new HashMap<Integer, String>(DISTINCT_KEYS).getElementCardinality());
    }

    /**
     * Verifies that the get method with a default value returns the mapped value when the key is present in the map.
     */
    @Test
    public void getWithDefaultValueShouldReturnMappedValueWhenKeyIsPresent() {
        assertEquals("one", Map.of(1, "one").get(1, null));
    }

    /**
     * Verifies that the get method with a default value returns the default value when the key is absent in the map.
     */
    @Test
    public void getWithDefaultValueShouldReturnDefaultValueWhenKeyIsAbsent() {
        assertNull(Map.of(1, "one").get(2, null));
    }
}
