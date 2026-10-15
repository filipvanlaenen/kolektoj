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
        assertTrue(UpdatableMap.differenceOf().isEmpty());
    }

    /**
     * Verifies that the difference of one map is that map.
     */
    @Test
    public void differenceOfOneMapShouldBeTheSameMap() {
        assertTrue(map123.containsSame(UpdatableMap.differenceOf(map123)));
    }

    /**
     * Verifies that the difference of three maps only contains the entries of the first map that aren't present in any
     * of the other.
     */
    @Test
    public void differenceOfThreeMapsShouldOnlyContainTheEntriesFromTheFirstMapNotInTheOthers() {
        assertTrue(Map.of(ENTRY3).containsSame(UpdatableMap.differenceOf(map123, Map.of(ENTRY1), map12)));
    }

    /**
     * Verifies that the intersection of no maps is an empty map.
     */
    @Test
    public void intersectionOfNoMapsShouldBeEmpty() {
        assertTrue(UpdatableMap.intersectionOf().isEmpty());
    }

    /**
     * Verifies that the intersection of one map is the map itself.
     */
    @Test
    public void intersectionOfOneMapShouldBeItself() {
        assertTrue(map123.containsSame(UpdatableMap.intersectionOf(map123)));
    }

    /**
     * Verifies that the intersection of two maps is a maps with the common entries.
     */
    @Test
    public void intersectionOfTwoMapsShouldContainCommonEntries() {
        assertTrue(map12.containsSame(UpdatableMap.intersectionOf(Map.of(ENTRY0, ENTRY1, ENTRY2), map123)));
    }

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
    public void ofShouldConstructAnUpdatableMapFromAnotherMapCorrectly() {
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

    /**
     * Verifies that an updatable map constructed from another map is constructed correctly.
     */
    @Test
    public void ofShouldConstructAnUpdatableMapFromAnotherMapCorrectlyWithCorrectCardinality() {
        Map<Integer, String> prototype = Map.of(1, "one", 1, "one", 2, "two", THREE, "three");
        UpdatableMap<Number, String> actual = UpdatableMap.of(DISTINCT_KEYS, prototype);
        assertEquals(THREE, actual.size());
        assertEquals(DISTINCT_KEYS, actual.getKeyAndValueCardinality());
        assertTrue(prototype.containsAll(actual));
    }

    /**
     * Verifies that a map constructed with a default value is constructed correctly.
     */
    @Test
    public void ofShouldConstructAnUpdatableMapWithDefaultValuesAndKeyCollectionCorrectly() {
        UpdatableMap<Integer, String> actual = UpdatableMap.<Integer, String>of("", Collection.of(1, 2, THREE));
        assertEquals(DISTINCT_KEYS, actual.getKeyAndValueCardinality());
        assertEquals(THREE, actual.size());
        assertTrue(actual.contains(new Entry<Integer, String>(1, "")));
        assertTrue(actual.contains(new Entry<Integer, String>(2, "")));
        assertTrue(actual.contains(new Entry<Integer, String>(THREE, "")));
    }

    /**
     * Verifies that a map constructed with entries and key and value cardinality and a default value is constructed
     * correctly.
     */
    @Test
    public void ofShouldConstructAnUpdatableMapWithKeyAndValueCardinalityAndDefaultValuesCorrectly() {
        UpdatableMap<Integer, String> actual =
                UpdatableMap.<Integer, String>of(DUPLICATE_KEYS_WITH_DUPLICATE_VALUES, "", 1, 1, 2, THREE);
        assertEquals(DUPLICATE_KEYS_WITH_DUPLICATE_VALUES, actual.getKeyAndValueCardinality());
        assertEquals(FOUR, actual.size());
        assertTrue(actual.contains(new Entry<Integer, String>(1, "")));
        assertTrue(actual.contains(new Entry<Integer, String>(2, "")));
        assertTrue(actual.contains(new Entry<Integer, String>(THREE, "")));
    }

    /**
     * Verifies that a map constructed with entries and key and value cardinality and a default value is constructed
     * correctly.
     */
    @Test
    public void ofShouldConstructAnUpdatableMapWithKeyAndValueCardinalityAndDefaultValuesWithKeyCollectionCorrectly() {
        UpdatableMap<Integer, String> actual = UpdatableMap.<Integer, String>of(DUPLICATE_KEYS_WITH_DUPLICATE_VALUES,
                "", Collection.of(1, 1, 2, THREE));
        assertEquals(DUPLICATE_KEYS_WITH_DUPLICATE_VALUES, actual.getKeyAndValueCardinality());
        assertEquals(FOUR, actual.size());
        assertTrue(actual.contains(new Entry<Integer, String>(1, "")));
        assertTrue(actual.contains(new Entry<Integer, String>(2, "")));
        assertTrue(actual.contains(new Entry<Integer, String>(THREE, "")));
    }

    /**
     * Verifies that the <code>unionOf</code> method returns the union of two maps.
     */
    @Test
    public void unionOfShouldReturnUnionOfTwoMaps() {
        Map<Integer, String> map23 = Map.<Integer, String>of(ENTRY2, ENTRY3);
        UpdatableMap<Integer, String> actual = UpdatableMap.unionOf(map12, map23);
        assertTrue(actual.containsSame(Map.of(ENTRY1, ENTRY2, ENTRY3)));
    }

    /**
     * Verifies that the <code>unionOf</code> method with key value cardinality returns the union of two maps.
     */
    @Test
    public void unionOfWithKeyValueCardinalityShouldReturnUnionOfTwoMaps() {
        Map<Integer, String> map23 = Map.<Integer, String>of(ENTRY2, ENTRY3);
        UpdatableMap<Integer, String> actual = UpdatableMap.unionOf(DUPLICATE_KEYS_WITH_DUPLICATE_VALUES, map12, map23);
        Map<Integer, String> expected = Map.of(DUPLICATE_KEYS_WITH_DUPLICATE_VALUES, ENTRY1, ENTRY2, ENTRY2, ENTRY3);
        assertTrue(actual.containsSame(expected));
    }
}
