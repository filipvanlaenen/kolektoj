package net.filipvanlaenen.kolektoj.array;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import net.filipvanlaenen.kolektoj.Map.Entry;
import net.filipvanlaenen.kolektoj.Map;
import net.filipvanlaenen.kolektoj.ModifiableMap;

/**
 * Unit tests on the {@link net.filipvanlaenen.kolektoj.array.ModifiableArrayMap} class.
 */
public class ModifiableArrayMapTest {
    /**
     * The magic number three.
     */
    private static final int THREE = 3;
    private static final Entry<Integer, String> ENTRY_NULL = new Entry<Integer, String>(null, null);
    private static final Entry<Integer, String> ENTRY1 = new Entry<Integer, String>(1, "one");
    private static final Entry<Integer, String> ENTRY2 = new Entry<Integer, String>(2, "two");
    private static final Entry<Integer, String> ENTRY3 = new Entry<Integer, String>(3, "three");
    /**
     * Map with the integers 1, 2 and 3 mapped to their words.
     */
    private static final ModifiableMap<Integer, String> MAP123 =
            new ModifiableArrayMap<Integer, String>(ENTRY1, ENTRY2, ENTRY3);
    private static final Map<Integer, String> MAP123NULL =
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
}
