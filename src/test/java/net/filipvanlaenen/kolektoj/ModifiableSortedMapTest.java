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
    private final SortedMap<Integer, String> map12 = SortedMap.<Integer, String>of(COMPARATOR, ENTRY1, ENTRY2);
    /**
     * Map with the doubles 1, 2 and 3.
     */
    private final SortedMap<Integer, String> map123 = SortedMap.of(COMPARATOR, ENTRY1, ENTRY2, ENTRY3);
    /**
     * /** A comparator ordering integers in the natural order, but in addition handles <code>null</code> as the lowest
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
     * Verifies that the difference of no maps is empty.
     */
    @Test
    public void differenceOfNoMapsShouldBeEmpty() {
        assertTrue(ModifiableSortedMap.differenceOf(COMPARATOR).isEmpty());
    }

    /**
     * Verifies that the difference of one map is that map.
     */
    @Test
    public void differenceOfOneMapShouldBeTheSameMap() {
        assertTrue(map123.containsSame(ModifiableSortedMap.differenceOf(COMPARATOR, map123)));
    }

    /**
     * Verifies that the difference of three maps only contains the entries of the first map that aren't present in any
     * of the other.
     */
    @Test
    public void differenceOfThreeMapsShouldOnlyContainTheEntriesFromTheFirstMapNotInTheOthers() {
        assertTrue(Map.of(ENTRY3)
                .containsSame(ModifiableSortedMap.differenceOf(COMPARATOR, map123, Map.of(ENTRY1), map12)));
    }

    /**
     * Verifies that the difference of one map is that map.
     */
    @Test
    public void differenceOfOneSortedMapShouldBeTheSameMap() {
        assertTrue(map123.containsSame(ModifiableSortedMap.differenceOf(map123)));
    }

    /**
     * Verifies that the difference of three maps only contains the entries of the first map that aren't present in any
     * of the other.
     */
    @Test
    public void differenceOfThreeMapsShouldOnlyContainTheEntriesFromTheSortedMapNotInTheOthers() {
        assertTrue(Map.of(ENTRY3).containsSame(ModifiableSortedMap.differenceOf(map123, Map.of(ENTRY1), map12)));
    }

    /**
     * Verifies that a modifiable sorted map constructed with the empty factory method is empty.
     */
    @Test
    public void emptyShouldConstructAnEmptyMap() {
        assertTrue(ModifiableSortedMap.empty(COMPARATOR).isEmpty());
    }

    /**
     * Verifies that the intersection of no maps is an empty map.
     */
    @Test
    public void intersectionOfNoMapsShouldBeEmpty() {
        assertTrue(ModifiableSortedMap.intersectionOf(COMPARATOR).isEmpty());
    }

    /**
     * Verifies that the intersection of one map is the map itself.
     */
    @Test
    public void intersectionOfOneMapShouldBeItself() {
        assertTrue(map123.containsSame(ModifiableSortedMap.intersectionOf(COMPARATOR, map123)));
    }

    /**
     * Verifies that the intersection of two maps is a maps with the common entries.
     */
    @Test
    public void intersectionOfTwoMapsShouldContainCommonEntries() {
        assertTrue(map12
                .containsSame(ModifiableSortedMap.intersectionOf(COMPARATOR, Map.of(ENTRY0, ENTRY1, ENTRY2), map123)));
    }

    /**
     * Verifies that the intersection of one map is the map itself.
     */
    @Test
    public void intersectionOfOneSortedMapShouldBeItself() {
        assertTrue(map123.containsSame(ModifiableSortedMap.intersectionOf(map123)));
    }

    /**
     * Verifies that the intersection of two maps is a maps with the common entries.
     */
    @Test
    public void intersectionOfSortedAndOtherMapShouldContainCommonEntries() {
        assertTrue(map12.containsSame(ModifiableSortedMap.intersectionOf(map123, Map.of(ENTRY0, ENTRY1, ENTRY2))));
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

    /**
     * Verifies that a sorted map constructed from another sorted map is constructed correctly.
     */
    @Test
    public void ofShouldConstructASortedMapFromAnotherSortedMapCorrectly() {
        SortedMap<Integer, String> prototype = SortedMap.of(COMPARATOR, 1, "one", 2, "two", THREE, "three");
        ModifiableSortedMap<Integer, String> actual = ModifiableSortedMap.of(prototype);
        assertTrue(actual.containsSame(prototype));
        assertEquals(COMPARATOR, actual.getComparator());
    }

    /**
     * Verifies that the of factory method using a map and a range clones a map.
     */
    @Test
    public void ofShouldConstructASortedMapFromAnotherSortedMapAndRangeCorrectly() {
        SortedMap<Integer, String> prototype =
                SortedMap.of(COMPARATOR, 1, "one", 2, "two", THREE, "three", FOUR, "four", FIVE, "five");
        ModifiableSortedMap<Integer, String> actual =
                ModifiableSortedMap.of(prototype, Range.greaterThan(1).lessThan(FIVE));
        assertTrue(actual.containsSame(Map.of(2, "two", THREE, "three", FOUR, "four")));
        assertEquals(COMPARATOR, actual.getComparator());
    }

    /**
     * Verifies that a sorted map constructed from another map and a constructor is constructed correctly.
     */
    @Test
    public void ofShouldConstructASortedMapFromAMapAndAComparatorCorrectly() {
        Map<Integer, String> prototype = Map.of(1, "one", 2, "two", THREE, "three");
        ModifiableSortedMap<Integer, String> actual = ModifiableSortedMap.of(COMPARATOR, prototype);
        assertTrue(actual.containsSame(prototype));
        assertEquals(COMPARATOR, actual.getComparator());
    }
}
