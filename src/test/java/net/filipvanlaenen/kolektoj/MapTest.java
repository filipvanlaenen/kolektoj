package net.filipvanlaenen.kolektoj;

import static net.filipvanlaenen.kolektoj.Map.KeyAndValueCardinality.DISTINCT_KEYS;
import static net.filipvanlaenen.kolektoj.Map.KeyAndValueCardinality.DUPLICATE_KEYS_WITH_DISTINCT_VALUES;
import static net.filipvanlaenen.kolektoj.Map.KeyAndValueCardinality.DUPLICATE_KEYS_WITH_DUPLICATE_VALUES;
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
     * An entry with key 0 and value zero.
     */
    protected static final Entry<Integer, String> ENTRY0 = new Entry<Integer, String>(0, "zero");
    /**
     * An entry with key 1 and value one.
     */
    protected static final Entry<Integer, String> ENTRY1 = new Entry<Integer, String>(1, "one");
    /**
     * An entry with key 2 and value two.
     */
    protected static final Entry<Integer, String> ENTRY2 = new Entry<Integer, String>(2, "two");
    /**
     * An entry with key 3 and value three.
     */
    protected static final Entry<Integer, String> ENTRY3 = new Entry<Integer, String>(3, "three");
    /**
     * Map with the doubles 1 and 2.
     */
    private final Map<Integer, String> map12 = Map.<Integer, String>of(ENTRY1, ENTRY2);
    /**
     * Map with the doubles 1, 2 and 3.
     */
    private final Map<Integer, String> map123 = Map.of(ENTRY1, ENTRY2, ENTRY3);

    /**
     * Verifies that the difference of no maps is empty.
     */
    @Test
    public void differenceOfNoMapsShouldBeEmpty() {
        assertTrue(Map.differenceOf().isEmpty());
    }

    /**
     * Verifies that the difference of one map is that map.
     */
    @Test
    public void differenceOfOneMapShouldBeTheSameMap() {
        assertTrue(map123.containsSame(Map.differenceOf(map123)));
    }

    /**
     * Verifies that the difference of three maps only contains the entries of the first map that aren't present in any
     * of the other.
     */
    @Test
    public void differenceOfThreeMapsShouldOnlyContainTheEntriesFromTheFirstMapNotInTheOthers() {
        assertTrue(Map.of(ENTRY3).containsSame(Map.differenceOf(map123, Map.of(ENTRY1), map12)));
    }

    /**
     * Verifies that the intersection of no maps is an empty map.
     */
    @Test
    public void intersectionOfNoMapsShouldBeEmpty() {
        assertTrue(Map.intersectionOf().isEmpty());
    }

    /**
     * Verifies that the intersection of one map is the map itself.
     */
    @Test
    public void intersectionOfOneMapShouldBeItself() {
        assertTrue(map123.containsSame(Map.intersectionOf(map123)));
    }

    /**
     * Verifies that the intersection of two maps is a maps with the common entries.
     */
    @Test
    public void intersectionOfTwoMapsShouldContainCommonEntries() {
        assertTrue(map12.containsSame(Map.intersectionOf(Map.of(ENTRY0, ENTRY1, ENTRY2), map123)));
    }

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
     * Verifies that a map constructed from another map but different key and value cardinality is constructed
     * correctly.
     */
    @Test
    public void ofShouldConstructAMapFromAnotherMapWithOtherKeyAndValueCardinalityCorrectly() {
        Map<Integer, String> prototype = Map.of(1, "one", 1, "one", 2, "two", THREE, "three");
        Map<Number, String> actual = Map.of(DISTINCT_KEYS, prototype);
        assertEquals(DISTINCT_KEYS, actual.getKeyAndValueCardinality());
        assertTrue(actual.containsSame(Map.of(1, "one", 2, "two", THREE, "three")));
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

    /**
     * Verifies that the <code>unionOf</code> method returns the union of two maps.
     */
    @Test
    public void unionOfShouldReturnUnionOfTwoMaps() {
        Map<Integer, String> map23 = Map.<Integer, String>of(ENTRY2, ENTRY3);
        Map<Integer, String> actual = Map.unionOf(map12, map23);
        assertTrue(actual.containsSame(Map.of(ENTRY1, ENTRY2, ENTRY3)));
    }

    /**
     * Verifies that the <code>unionOf</code> method with key value cardinality returns the union of two maps.
     */
    @Test
    public void unionOfWithKeyValueCardinalityShouldReturnUnionOfTwoMaps() {
        Map<Integer, String> map23 = Map.<Integer, String>of(ENTRY2, ENTRY3);
        Map<Integer, String> actual = Map.unionOf(DUPLICATE_KEYS_WITH_DUPLICATE_VALUES, map12, map23);
        Map<Integer, String> expected = Map.of(DUPLICATE_KEYS_WITH_DUPLICATE_VALUES, ENTRY1, ENTRY2, ENTRY2, ENTRY3);
        assertTrue(actual.containsSame(expected));
    }
}
