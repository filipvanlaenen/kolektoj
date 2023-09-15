package net.filipvanlaenen.kolektoj.array;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * Unit tests on the {@link net.filipvanlaenen.kolektoj.array.ArrayCollection} class.
 */
public class ArrayCollectionTest {
    /**
     * The magic number three.
     */
    private static final int THREE = 3;
    /**
     * Collection with the integers 1, 2 and 3.
     */
    private static final ArrayCollection<Integer> COLLECTION123 = new ArrayCollection<Integer>(1, 2, 3);

    /**
     * Verifies that the correct length is returned for a collection with three elements.
     */
    @Test
    public void sizeShouldReturnThreeForACollectionOfThreeElements() {
        assertEquals(THREE, COLLECTION123.size());
    }
}
