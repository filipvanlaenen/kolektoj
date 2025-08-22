package net.filipvanlaenen.kolektoj;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
     * Sorted map with three entries.
     */
    private T map123 = createMap(ENTRY1, ENTRY2, ENTRY3);

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
}
