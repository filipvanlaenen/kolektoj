package net.filipvanlaenen.kolektoj;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import net.filipvanlaenen.kolektoj.MapTestBase.KeyWithCollidingHash;

/**
 * Unit tests on implementations of the {@link net.filipvanlaenen.kolektoj.UpdatableMap} interface.
 *
 * @param <T> The subclass type to be tested.
 */
public abstract class UpdatableMapTestBase<T extends UpdatableMap<Integer, String>, TC extends UpdatableMap<KeyWithCollidingHash, Integer>>
        extends MapTestBase<T, TC> {
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
     * Verifies that updating a key with a new value returns the old value for the key.
     */
    @Test
    public void updateShouldReturnTheOldValueForTheKey() {
        assertEquals("one", createMap(ENTRY1, ENTRY2, ENTRY3).update(1, "bis"));
    }
}
