package net.filipvanlaenen.kolektoj.array;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import net.filipvanlaenen.kolektoj.Collection;
import net.filipvanlaenen.kolektoj.Map.Entry;

/**
 * Unit tests on the {@link net.filipvanlaenen.kolektoj.array.ArrayMap} class.
 */
public class ArrayMapTest {
    /**
     * The magic number three.
     */
    private static final int THREE = 3;
    /**
     * The magic number six.
     */
    private static final int SIX = 6;
    private static final Entry<Integer, String> ENTRY_NULL = new Entry<Integer, String>(null, null);
    private static final Entry<Integer, String> ENTRY1 = new Entry<Integer, String>(1, "one");
    private static final Entry<Integer, String> ENTRY2 = new Entry<Integer, String>(2, "two");
    private static final Entry<Integer, String> ENTRY3 = new Entry<Integer, String>(3, "three");
    /**
     * Map with the integers 1, 2 and 3 mapped to their words.
     */
    private static final ArrayMap<Integer, String> MAP123 = new ArrayMap<Integer, String>(ENTRY1, ENTRY2, ENTRY3);
    private static final ArrayMap<Integer, String> MAP123NULL =
            new ArrayMap<Integer, String>(ENTRY1, ENTRY2, ENTRY3, ENTRY_NULL);

    /**
     * Verifies that the correct length is returned for a map with three entries.
     */
    @Test
    public void sizeShouldReturnThreeForAMapOfThreeEntries() {
        assertEquals(THREE, MAP123.size());
    }

    /**
     * Verifies that contains returns true for an entry in the map.
     */
    @Test
    public void containsShouldReturnTrueForAnEntryInTheMap() {
        assertTrue(MAP123.contains(ENTRY1));
    }

    /**
     * Verifies that contains returns false for an entry not in the map.
     */
    @Test
    public void containsShouldReturnFalseForAnEntryNotInTheMap() {
        assertFalse(MAP123.contains(new Entry<Integer, String>(0, "zero")));
    }

    /**
     * Verifies that containsKey returns true for a key in the map.
     */
    @Test
    public void containsKeyShouldReturnTrueForAKeyInTheMap() {
        assertTrue(MAP123.containsKey(1));
    }

    /**
     * Verifies that containsKey returns false for a key not in the map.
     */
    @Test
    public void containsKeyShouldReturnFalseForAKeyNotInTheMap() {
        assertFalse(MAP123.containsKey(0));
    }

    /**
     * Verifies that containsKey returns false for null if not in the map.
     */
    @Test
    public void containsKeyShouldReturnFalseForNullIfNotInTheMap() {
        assertFalse(MAP123.containsKey(null));
    }

    /**
     * Verifies that containsValue returns true for a value in the map.
     */
    @Test
    public void containsValueShouldReturnTrueForAValueInTheMap() {
        assertTrue(MAP123.containsValue("one"));
    }

    /**
     * Verifies that containsValue returns true for null if in the map.
     */
    @Test
    public void containsValueShouldReturnTrueForNullIfInTheMap() {
        assertTrue(MAP123NULL.containsValue(null));
    }

    /**
     * Verifies that containsValue returns false for a value not in the map.
     */
    @Test
    public void containsValueShouldReturnFalseForAValueNotInTheMap() {
        assertFalse(MAP123.containsValue("zero"));
    }

    /**
     * Verifies that trying to get an element from an empty map throws IndexOutOfBoundsException.
     */
    @Test
    public void getShouldThrowExceptionWhenCalledOnAnEmptyMap() {
        IndexOutOfBoundsException exception =
                assertThrows(IndexOutOfBoundsException.class, () -> new ArrayMap<Integer, String>().get());
        assertEquals("Cannot return an entry from an empty map.", exception.getMessage());
    }

    /**
     * Verifies that when you get an entry from a map, the map contains it.
     */
    @Test
    public void getShouldReturnAnEntryPresentInTheMap() {
        Entry<Integer, String> entry = MAP123.get();
        assertTrue(MAP123.contains(entry));
    }

    /**
     * Verifies that when you try to use get with a key not in the map, an IllegalArgumentException is thrown.
     */
    @Test
    public void getShouldThrowExceptionWhenCalledWithAbsentKey() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> MAP123.get(0));
        assertEquals("Map doesn't contain an entry with the key 0.", exception.getMessage());
    }

    /**
     * Verifies that when you try to use get with a key in the map, the value is returned.
     */
    @Test
    public void getShouldReturnValueForKey() {
        assertEquals("one", MAP123.get(1));
    }

    /**
     * Verifies that when you try to use getAll with a key not in the map, an IllegalArgumentException is thrown.
     */
    @Test
    public void getAllShouldThrowExceptionWhenCalledWithAbsentKey() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> MAP123.getAll(0));
        assertEquals("Map doesn't contain entries with the key 0.", exception.getMessage());
    }

    /**
     * Verifies that when you try to use getAll with a key in the map, a collection with the value is returned.
     */
    @Test
    public void getAllShouldReturnSingleValueForKey() {
        Collection<String> actual = MAP123.getAll(1);
        assertEquals(1, actual.size());
        assertTrue(actual.contains("one"));
    }

    /**
     * Verifies that when you try to use getAll with a key in the map that has multiple values, a collection with the
     * values is returned.
     */
    @Test
    public void getAllShouldReturnManyValuesForKey() {
        ArrayMap<Integer, String> map =
                new ArrayMap<Integer, String>(ENTRY1, new Entry<Integer, String>(1, "two"), ENTRY3);
        Collection<String> actual = map.getAll(1);
        assertEquals(2, actual.size());
        assertTrue(actual.contains("one"));
        assertTrue(actual.contains("two"));
    }

    /**
     * Verifies that getKeys returns all the keys.
     */
    @Test
    public void getKeysShouldReturnAllKeys() {
        Collection<Integer> actual = MAP123.getKeys();
        assertEquals(3, actual.size());
        assertTrue(actual.contains(1));
        assertTrue(actual.contains(2));
        assertTrue(actual.contains(3));
    }

    /**
     * Verifies that getValues returns all the values.
     */
    @Test
    public void getValuesShouldReturnAllKeys() {
        Collection<String> actual = new ArrayMap<Integer, String>(ENTRY1, ENTRY2, ENTRY3).getValues();
        assertEquals(3, actual.size());
        assertTrue(actual.contains("one"));
        assertTrue(actual.contains("two"));
        assertTrue(actual.contains("three"));
    }

    /**
     * Verifies that the map produces an array with the entries.
     */
    @Test
    public void toArrayShouldProduceAnArrayWithTheEntriesOfTheMap() {

        ArrayMap<Integer, String> map = new ArrayMap<Integer, String>(ENTRY1, ENTRY2);
        Entry<Integer, String>[] actual = map.toArray();
        assertTrue(actual.length == 2 && (actual[0] == ENTRY1 || actual[1] == ENTRY1)
                && (actual[0] == ENTRY2 || actual[1] == ENTRY2));
    }

    /**
     * Verifies that the map produces a stream that reduces to the correct key sum, thus verifying that the spliterator
     * is created correctly.
     */
    @Test
    public void streamShouldProduceAStreamThatReducesToTheCorrectSum() {
        assertEquals(SIX, MAP123.stream().map(e -> e.key()).reduce(0, Integer::sum));
    }

    /**
     * Verifies that the map produces an iterator that when used in a for loop, produces the correct key sum.
     */
    @Test
    public void iteratorShouldProduceCorrectSumInForLoop() {
        int sum = 0;
        for (Entry<Integer, String> entry : MAP123) {
            sum += entry.key();
        }
        assertEquals(SIX, sum);
    }
}
