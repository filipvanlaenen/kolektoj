package net.filipvanlaenen.kolektoj.array;

import static net.filipvanlaenen.kolektoj.Collection.ElementCardinality.DISTINCT_ELEMENTS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
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
     * Collection with the integers 1, 2 and 3.
     */
    private static final Collection<Integer> COLLECTION123 = new ArrayCollection<Integer>(1, 2, 3);
    /**
     * Collection with the integers 1, 2, 3 and null.
     */
    private static final Collection<Integer> COLLECTION123NULL = new ArrayCollection<Integer>(1, 2, 3, null);

    /**
     * Verifies that cloneDistinctElements removes duplicate elements. The method is tested through the constructor for
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
     * Verifies that cloneDistinctElements removes duplicate null elements. The method is tested through the constructor
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

    /**
     * Verifies that containsAll returns false is the other collection is larger. The method is tested through the
     * containsAll method in the ArrayCollection class.
     */
    @Test
    public void containsAllShouldReturnFalseIfTheOtherCollectionIsLarger() {
        assertFalse(COLLECTION123.containsAll(COLLECTION123NULL));
    }

    /**
     * Verifies that containsAll returns true if a collection is compared to itself. The method is tested through the
     * containsAll method in the ArrayCollection class.
     */
    @Test
    public void containsAllShouldReturnTrueWhenComparedToItself() {
        assertTrue(COLLECTION123.containsAll(COLLECTION123));
    }

    /**
     * Verifies that containsAll returns false if a collection contains another element. The method is tested through
     * the containsAll method in the ArrayCollection class.
     */
    @Test
    public void containsAllShouldReturnFalseWhenComparedToCollectionWithAnotherElement() {
        assertFalse(COLLECTION123.containsAll(new ArrayCollection<Integer>(0, 1, 2)));
    }
}
