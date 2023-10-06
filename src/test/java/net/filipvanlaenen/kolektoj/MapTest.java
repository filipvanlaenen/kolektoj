package net.filipvanlaenen.kolektoj;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * Unit tests on the {@link net.filipvanlaenen.kolektoj.Map} class.
 */
public class MapTest {
    /**
     * Verifies that an empty map is empty.
     */
    @Test
    public void isEmptyShouldReturnTrueForAnEmptyMap() {
        assertTrue(Map.empty().isEmpty());
    }

    /**
     * Verifies that a map containing an element is not empty.
     */
    @Test
    public void isEmptyShouldReturnFalseForAMapContainingAnElement() {
        assertFalse(Map.of(new Map.Entry<Integer, Integer>(1, 1)).isEmpty());
    }
}
