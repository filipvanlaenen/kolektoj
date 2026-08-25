package net.filipvanlaenen.kolektoj;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertSame;
import static org.junit.jupiter.api.Assertions.*;
import static net.filipvanlaenen.kolektoj.Collection.ElementCardinality.*;

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
     * Collection with the integer 1.
     */
    private final Collection<Integer> collection1 = Collection.of(1);
    /**
     * Collection with the integers 1 and 2.
     */
    private final Collection<Integer> collection12 = Collection.of(1, 2);
    /**
     * Collection with the integers 1, 2 and 3.
     */
    private final Collection<Integer> collection123 = Collection.of(1, 2, THREE);

    /**
     * Verifies that the difference of no collections is empty.
     */
    @Test
    public void differenceOfNoCollectionsShouldBeEmpty() {
        assertTrue(Collection.differenceOf().isEmpty());
    }

    /**
     * Verifies that the difference of one collection is that collection.
     */
    @Test
    public void differenceOfOneCollectionShouldBeTheSameCollection() {
        assertTrue(collection1.containsSame(Collection.differenceOf(collection1)));
    }

    /**
     * Verifies that the difference of three collections only contains the elements of the first collection that aren't
     * present in any of the other.
     */
    @Test
    public void differenceOfThreeCollectionsShouldOnlyContainTheElementsFromTheFirstCollectionNotInTheOthers() {
        assertTrue(
                Collection.of(THREE).containsSame(Collection.differenceOf(collection123, collection1, collection12)));
    }

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
        assertFalse(collection1.isEmpty());
    }

    /**
     * Verifies that the intersection of no collections is empty.
     */
    @Test
    public void intersectionOfNoCollectionsShouldBeEmpty() {
        assertTrue(Collection.intersectionOf().isEmpty());
    }

    /**
     * Verifies that the intersection of one collection is that collection.
     */
    @Test
    public void intersectionOfOneCollectionShouldBeTheSameCollection() {
        assertTrue(collection1.containsSame(Collection.intersectionOf(collection1)));
    }

    /**
     * Verifies that the intersection of three collections only contains the common elements.
     */
    @Test
    public void intersectionOfThreeCollectionsShouldOnlyContainTheCommonElements() {
        assertTrue(collection1.containsSame(Collection.intersectionOf(collection123, collection1, collection12)));
    }

    /**
     * Verifies that a collection with a specific element cardinality receives that element cardinality.
     */
    @Test
    public void ofWithElementCardinalityShouldReturnACollectionWithTheElementCardinality() {
        assertEquals(DISTINCT_ELEMENTS, Collection.of(DISTINCT_ELEMENTS, 1).getElementCardinality());
    }

    /**
     * Verifies that the of factory method using a collection clones a collection.
     */
    @Test
    public void ofWithCollectionShoudlReturnAClone() {
        assertTrue(collection123.containsSame(Collection.of(collection123)));
    }

    /**
     * Verifies that the of factory method using element cardinality and a collection clones a collection with the new
     * element cardinality.
     */
    @Test
    public void ofWithElementCardinalityAndCollectionShoudlReturnAClone() {
        Collection<Integer> collection = Collection.<Integer>of(DUPLICATE_ELEMENTS, 1, 1, 2, THREE);
        Collection<Number> clone = Collection.<Number>of(DISTINCT_ELEMENTS, collection);
        assertEquals(DISTINCT_ELEMENTS, clone.getElementCardinality());
        assertTrue(clone.containsSame(collection123));
    }

    /**
     * Verifies that the collection produces a stream that reduces to the correct sum.
     */
    @Test
    public void streamShouldProduceAStreamThatReducesToTheCorrectSum() {
        assertEquals(SIX, collection123.stream().reduce(0, Integer::sum));
    }

    /**
     * Verifies that containsSame returns false when two collections have different sizes.
     */
    @Test
    public void containsSameReturnsFalseWhenTwoCollectionsHaveDifferentSizes() {
        assertFalse(collection1.containsSame(collection12));
        assertFalse(collection12.containsSame(collection1));
    }

    /**
     * Verifies that containsSame returns false when two collections have the same size but different elements.
     */
    @Test
    public void containsSameReturnsFalseWhenTwoCollectionsHaveSameSizeButDifferentElements() {
        Collection<Integer> collection23 = Collection.of(2, THREE);
        assertFalse(collection12.containsSame(collection23));
        assertFalse(collection23.containsSame(collection12));
    }

    /**
     * Verifies that containsSame returns two when two collections have the same size and elements.
     */
    @Test
    public void containsSameReturnsTrueWhenTwoCollectionsHaveSameSizeAndElements() {
        Collection<Integer> firstCollection = Collection.of(1, 2);
        Collection<Integer> secondCollection = Collection.of(1, 2);
        assertTrue(firstCollection.containsSame(secondCollection));
    }

    /**
     * Verifies that <code>toArray</code> with a prototype returns an array containing the elements of the collection.
     */
    @Test
    public void toArrayWithPrototypeShouldReturnANewArrayWithTheContentOfTheCollection() {
        Integer[] actual = collection1.toArray(EmptyArrays.INTEGERS);
        Integer[] expected = new Integer[] {1};
        assertArrayEquals(expected, actual);
    }

    /**
     * Verifies that <code>toArray</code> fills the provided array with the elements of the collection if it's large
     * enough.
     */
    @Test
    public void toArrayWithPrototypeShouldFillTheArrayWithTheContentOfTheCollection() {
        Integer[] prototype = new Integer[] {0};
        Integer[] actual = collection1.toArray(prototype);
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
        Integer[] prototype = new Integer[] {0, 0};
        Integer[] actual = collection1.toArray(prototype);
        assertSame(prototype, actual);
        Integer[] expected = new Integer[] {1, null};
        assertArrayEquals(expected, actual);
    }

    /**
     * Verifies that the union of no collections is empty.
     */
    @Test
    public void unionOfNoCollectionsShouldBeEmpty() {
        assertTrue(Collection.unionOf().isEmpty());
    }

    /**
     * Verifies that the union of one collection is that collection.
     */
    @Test
    public void unionOfOneCollectionShouldBeTheSameCollection() {
        assertTrue(collection1.containsSame(Collection.unionOf(collection1)));
    }

    /**
     * Verifies that the union of three collections only all elements.
     */
    @Test
    public void unionOfThreeCollectionsShouldContainAllElements() {
        assertTrue(Collection.of(1, 2, THREE, 1, 1, 2)
                .containsSame(Collection.unionOf(collection123, collection1, collection12)));
    }

    /**
     * Verifies that the union of no collections is empty.
     */
    @Test
    public void unionOfNoCollectionsWithDistinctElementsShouldBeEmpty() {
        assertTrue(Collection.unionOf(DISTINCT_ELEMENTS).isEmpty());
    }

    /**
     * Verifies that the union of one collection is that collection.
     */
    @Test
    public void unionOfOneCollectionWithDistinctElementsShouldBeTheSameCollection() {
        assertTrue(collection1.containsSame(Collection.unionOf(DISTINCT_ELEMENTS, collection1)));
    }

    /**
     * Verifies that the union of three collections only all elements.
     */
    @Test
    public void unionOfThreeCollectionsWithDistinctElementsShouldContainAllDistinctElements() {
        assertTrue(collection123
                .containsSame(Collection.unionOf(DISTINCT_ELEMENTS, collection123, collection1, collection12)));
    }
}
