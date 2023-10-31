package net.filipvanlaenen.kolektoj;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static net.filipvanlaenen.kolektoj.Collection.ElementCardinality.DISTINCT_ELEMENTS;

import org.junit.jupiter.api.Test;

/**
 * Unit tests on the {@link net.filipvanlaenen.kolektoj.array.Collection} class.
 */
public class CollectionTest {
    /**
     * The magic number three.
     */
    private static final int THREE = 3;
    /**
     * The magic number six.
     */
    private static final int SIX = 6;

    /**
     * Verifies that an empty collection is empty.
     */
    @Test
    public void isEmptyShouldReturnTrueForAnEmptyCollection() {
        assertTrue(Collection.empty().isEmpty());
    }

    /**
     * Verifies that a collection containing an element is not empty.
     */
    @Test
    public void isEmptyShouldReturnFalseForACollectionContainingAnElement() {
        assertFalse(Collection.of(1).isEmpty());
    }

    /**
     * Verifies that a collection with a specific element cardinality receives that element cardinality.
     */
    @Test
    public void ofWithElementCardinalityShouldReturnACollectionWithTheElementCardinality() {
        assertEquals(DISTINCT_ELEMENTS, Collection.of(DISTINCT_ELEMENTS, 1).getElementCardinality());
    }

    /**
     * Verifies that the collection produces a stream that reduces to the correct sum.
     */
    @Test
    public void streamShouldProduceAStreamThatReducesToTheCorrectSum() {
        assertEquals(SIX, Collection.of(1, 2, THREE).stream().reduce(0, Integer::sum));
    }

    /**
     * Verifies that containsSame returns false when two collections have different sizes.
     */
    @Test
    public void containsSameReturnsFalseWhenTwoCollectionsHaveDifferentSizes() {
        Collection<Integer> collection1 = Collection.of(1);
        Collection<Integer> collection2 = Collection.of(1, 2);
        assertFalse(collection1.containsSame(collection2));
        assertFalse(collection2.containsSame(collection1));
    }

    /**
     * Verifies that containsSame returns false when two collections have the same size but different elements.
     */
    @Test
    public void containsSameReturnsFalseWhenTwoCollectionsHaveSameSizeButDifferentElements() {
        Collection<Integer> collection1 = Collection.of(1, 2);
        Collection<Integer> collection2 = Collection.of(2, THREE);
        assertFalse(collection1.containsSame(collection2));
        assertFalse(collection2.containsSame(collection1));
    }

    /**
     * Verifies that containsSame returns two when two collections have the same size and elements.
     */
    @Test
    public void containsSameReturnsTrueWhenTwoCollectionsHaveSameSizeAndElements() {
        Collection<Integer> collection1 = Collection.of(1, 2);
        Collection<Integer> collection2 = Collection.of(1, 2);
        assertTrue(collection1.containsSame(collection2));
    }
}
