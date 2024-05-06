package net.filipvanlaenen.kolektoj;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertSame;
import static org.junit.jupiter.api.Assertions.*;
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

    /**
     * Verifies that <code>toArray</code> with a prototype returns an array containing the elements of the collection.
     */
    @Test
    public void toArrayWithPrototypeShouldReturnANewArrayWithTheContentOfTheCollection() {
        Collection<Integer> collection = Collection.of(1);
        Integer[] actual = collection.toArray(EmptyArrays.INTEGERS);
        Integer[] expected = new Integer[] {1};
        assertArrayEquals(expected, actual);
    }

    /**
     * Verifies that <code>toArray</code> fills the provided array with the elements of the collection if it's large
     * enough.
     */
    @Test
    public void toArrayWithPrototypeShouldFillTheArrayWithTheContentOfTheCollection() {
        Collection<Integer> collection = Collection.of(1);
        Integer[] prototype = new Integer[] {0};
        Integer[] actual = collection.toArray(prototype);
        assertSame(prototype, actual);
        Integer[] expected = new Integer[] {1};
        assertArrayEquals(expected, actual);
    }

    /**
     * Verifies that <code>toArray</code> fills the provided array with the elements of the collection if it's large
     * enough and sets the rest of the array to <code>null</code>.
     */
    @Test
    public void toArrayWithPrototypeShouldFillTheArrayWithTheContentOfTheCollectionAndNullTheRest() {
        Collection<Integer> collection = Collection.of(1);
        Integer[] prototype = new Integer[] {0, 0};
        Integer[] actual = collection.toArray(prototype);
        assertSame(prototype, actual);
        Integer[] expected = new Integer[] {1, null};
        assertArrayEquals(expected, actual);
    }
}
