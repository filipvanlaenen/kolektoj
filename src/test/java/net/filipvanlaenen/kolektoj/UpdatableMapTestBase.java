package net.filipvanlaenen.kolektoj;

import static net.filipvanlaenen.kolektoj.Map.KeyAndValueCardinality.DUPLICATE_KEYS_WITH_DISTINCT_VALUES;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import net.filipvanlaenen.kolektoj.MapTestBase.KeyWithCollidingHash;

/**
 * Unit tests on implementations of the {@link net.filipvanlaenen.kolektoj.UpdatableMap} interface.
 *
 * @param <T>  The subclass type to be tested.
 * @param <TC> The subclass type to be tested, but with a key type with colliding hash values.
 */
public abstract class UpdatableMapTestBase<T extends UpdatableMap<Integer, String>,
        TC extends UpdatableMap<KeyWithCollidingHash, Integer>> extends MapTestBase<T, TC> {
    /**
     * The magic number four.
     */
    private static final int FOUR = 4;

    /**
     * Verifies that trying to update an absent key throws IllegalArgumentException.
     */
    @Test
    public void updateShouldThrowExceptionForAbsentKey() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> createMap(ENTRY1, ENTRY2, ENTRY3).update(FOUR, "four"));
        assertEquals("Map doesn't contain an entry with the key 4.", exception.getMessage());
    }

    /**
     * Verifies that updating a key with a new value stores the new value for the key.
     */
    @Test
    public void updateShouldStoreTheNewValueForTheKey() {
        UpdatableMap<Integer, String> map = createMap(ENTRY1, ENTRY2, ENTRY3);
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
        assertEquals("one", createMap(ENTRY1, ENTRY2, ENTRY3).update(1, "bis"));
    }
}
