package net.filipvanlaenen.kolektoj.array;

import static net.filipvanlaenen.kolektoj.Collection.ElementCardinality.DISTINCT_ELEMENTS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import net.filipvanlaenen.kolektoj.Collection;

/**
 * Unit tests on the {@link net.filipvanlaenen.kolektoj.array.ArrayUtilities} class.
 */
public class ArrayUtilitiesTest {
    /**
     * The magic number three.
     */
    private static final int THREE = 3;

    /**
     * Verifies that cloneDistinctElements removes duplicate elements. The method is test through the constructor for
     * the ArrayCollection class.
     */
    @Test
    public void cloneDistinctElementsShouldRemoveDuplicateElementsInArrayCollectionConstructor() {
        Collection<Integer> collection = new ArrayCollection<Integer>(DISTINCT_ELEMENTS, 1, 2, 2, THREE, 2, THREE);
        assertEquals(THREE, collection.size());
        assertTrue(collection.contains(1));
        assertTrue(collection.contains(2));
        assertTrue(collection.contains(THREE));
    }

    /**
     * Verifies that cloneDistinctElements removes duplicate null elements. The method is test through the constructor
     * for the ArrayCollection class.
     */
    @Test
    public void cloneDistinctElementsShouldRemoveDuplicateNullElementsInArrayCollectionConstructor() {
        Collection<Integer> collection = new ArrayCollection<Integer>(DISTINCT_ELEMENTS, 1, null, null, 2, null);
        assertEquals(THREE, collection.size());
        assertTrue(collection.contains(null));
        assertTrue(collection.contains(1));
        assertTrue(collection.contains(2));
    }
}
