package net.filipvanlaenen.kolektoj;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
     * Verifies that trying to remove the entry with the least key from an empty map throws IndexOutOfBoundsException.
     */
    @Test
    public void removeLeastShouldThrowExceptionWhenCalledOnAnEmptyMap() {
        IndexOutOfBoundsException exception =
                assertThrows(IndexOutOfBoundsException.class, () -> createMap().removeLeast());
        assertEquals("Cannot remove an entry from an empty map.", exception.getMessage());
    }
}
