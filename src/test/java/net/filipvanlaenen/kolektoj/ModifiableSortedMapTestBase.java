package net.filipvanlaenen.kolektoj;

import static net.filipvanlaenen.kolektoj.Map.KeyAndValueCardinality.DUPLICATE_KEYS_WITH_DISTINCT_VALUES;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import net.filipvanlaenen.kolektoj.MapTestBase.KeyWithCollidingHash;

/**
 * Unit tests on implementations of the {@link net.filipvanlaenen.kolektoj.ModifiableSortedMap} interface.
 *
 * @param <T>  The subclass type to be tested.
 * @param <TC> The subclass type to be tested, but with a key type with colliding hash values.
 */
public abstract class ModifiableSortedMapTestBase<T extends ModifiableSortedMap<Integer, String>,
        TC extends ModifiableSortedMap<KeyWithCollidingHash, Integer>> extends SortedMapTestBase<T, TC> {
    /**
     * The magic number four.
     */
    private static final int FOUR = 4;
    /**
     * Sorted map with three entries.
     */
    private T map123 = createMap123();

    /**
     * Creates a sorted map with three entries.
     *
     * @return A new sorted map with three entries.
     */
    private T createMap123() {
        return createMap(ENTRY1, ENTRY2, ENTRY3);
    }

    /**
     * Verifies that clearing a map sets the size to zero.
     */
    @Test
    public void clearShouldSetMapToBeEmpty() {
        T map = createMap123();
        map.clear();
        assertTrue(map.isEmpty());
        assertFalse(map.containsKey(1));
    }

    /**
     * Verifies that clearing a map empties caches.
     */
    @Test
    public void clearShouldSetCachesToBeEmpty() {
        T map = createMap123();
        map.clear();
        assertEquals(0, map.toArray().length);
        assertFalse(map.contains(ENTRY1));
    }

    /**
     * Verifies that keys are removed from the map when it's cleared.
     */
    @Test
    public void clearShouldRemoveKeys() {
        T map = createMap123();
        map.clear();
        assertTrue(map.getKeys().isEmpty());
    }

    /**
     * Verifies that values are removed from the map when it's cleared.
     */
    @Test
    public void clearShouldRemoveValues() {
        T map = createMap123();
        map.clear();
        assertTrue(map.getValues().isEmpty());
    }

    /**
     * Verifies that trying to remove the entry with the greatest key from an empty map throws
     * IndexOutOfBoundsException.
     */
    @Test
    public void removeGreatestShouldThrowExceptionWhenCalledOnAnEmptyMap() {
        IndexOutOfBoundsException exception =
                assertThrows(IndexOutOfBoundsException.class, () -> createMap().removeGreatest());
        assertEquals("Cannot remove an entry from an empty map.", exception.getMessage());
    }

    /**
     * Verifies that <code>removeGreatest</code> returns the greatest entry of a sorted map.
     */
    @Test
    public void removeGreatestShouldReturnTheGreatestEntryInASortedMap() {
        assertEquals(ENTRY3, map123.removeGreatest());
    }

    /**
     * Verifies that <code>removeGreatest</code> removes the greatest entry of a sorted map.
     */
    @Test
    public void removeGreatestShouldRemoveTheGreatestEntryInASortedMap() {
        map123.removeGreatest();
        assertEquals(2, map123.size());
        assertFalse(map123.contains(ENTRY3));
    }

    /**
     * Verifies that trying to remove the entry with the least key from an empty map throws IndexOutOfBoundsException.
     */
    @Test
    public void removeLeastShouldThrowExceptionWhenCalledOnAnEmptyMap() {
        IndexOutOfBoundsException exception =
                assertThrows(IndexOutOfBoundsException.class, () -> createMap().removeLeast());
        assertEquals("Cannot remove an entry from an empty map.", exception.getMessage());
    }

    /**
     * Verifies that <code>removeLeast</code> returns the least entry of a sorted map.
     */
    @Test
    public void removeLeastShouldReturnTheLeastEntryInASortedMap() {
        assertEquals(ENTRY1, map123.removeLeast());
    }

    /**
     * Verifies that <code>removeLeast</code> removes the least entry of a sorted map.
     */
    @Test
    public void removeLeastShouldRemoveTheLeastEntryInASortedMap() {
        map123.removeLeast();
        assertEquals(2, map123.size());
        assertFalse(map123.contains(ENTRY1));
    }

    /**
     * Verifies that trying to update an absent key throws IllegalArgumentException.
     */
    @Test
    public void updateShouldThrowExceptionForAbsentKey() {
        IllegalArgumentException exception =
                assertThrows(IllegalArgumentException.class, () -> createMap123().update(FOUR, "four"));
        assertEquals("Map doesn't contain an entry with the key 4.", exception.getMessage());
    }

    /**
     * Verifies that updating a key with a new value stores the new value for the key.
     */
    @Test
    public void updateShouldStoreTheNewValueForTheKey() {
        UpdatableMap<Integer, String> map = createMap123();
        map.update(1, "bis");
        assertEquals("bis", map.get(1));
        assertTrue(map.containsValue("bis"));
        assertTrue(map.contains(ENTRY1BIS));
        assertFalse(map.containsValue("one"));
        assertFalse(map.contains(ENTRY1));
    }

    /**
     * Verifies that for maps allowing duplicate keys but not duplicate values, values are updated such that they are
     * kept distinct.
     */
    @Test
    public void updateShouldAvoidDuplicateValuesForMapsWithDistinctValues() {
        UpdatableMap<Integer, String> map = createMap(DUPLICATE_KEYS_WITH_DISTINCT_VALUES, ENTRY1, ENTRY1BIS);
        assertEquals("one", map.update(1, "one"));
        assertEquals("bis", map.update(1, "bis"));
    }

    /**
     * Verifies that updating a key with a new value returns the old value for the key.
     */
    @Test
    public void updateShouldReturnTheOldValueForTheKey() {
        assertEquals("one", createMap123().update(1, "bis"));
    }
}
