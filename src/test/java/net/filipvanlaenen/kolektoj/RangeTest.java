package net.filipvanlaenen.kolektoj;

import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.Comparator;

import org.junit.jupiter.api.Test;

/**
 * Unit tests on the {@link net.filipvanlaenen.kolektoj.Range} class.
 */
public class RangeTest {
    /**
     * The magic number three.
     */
    private static final Range<Integer> ALL = Range.all();

    /**
     * Verifies that zero is not above all.
     */
    @Test
    public void zeroShouldNotBeAboveAll() {
        assertFalse(ALL.isAbove(Comparator.naturalOrder(), 0));
    }

    /**
     * Verifies that zero is not below all.
     */
    @Test
    public void zeroShouldNotBeBelowAll() {
        assertFalse(ALL.isBelow(Comparator.naturalOrder(), 0));
    }
}
