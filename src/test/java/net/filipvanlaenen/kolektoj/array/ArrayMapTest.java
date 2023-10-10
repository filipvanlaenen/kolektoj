package net.filipvanlaenen.kolektoj.array;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

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
     * Map with the integers 1, 2 and 3 mapped to their words.
     */
    private static final ArrayMap<Integer, String> MAP123 =
            new ArrayMap<Integer, String>(new Entry<Integer, String>(1, "one"), new Entry<Integer, String>(2, "two"),
                    new Entry<Integer, String>(3, "three"));

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
        assertTrue(MAP123.contains(new Entry<Integer, String>(1, "one")));
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
     * Verifies that containsValue returns true for a value in the map.
     */
    @Test
    public void containsValueShouldReturnTrueForAValueInTheMap() {
        assertTrue(MAP123.containsValue("one"));
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
    public void getShouldReturnValyeForKey() {
        assertEquals("one", MAP123.get(1));
    }
}
