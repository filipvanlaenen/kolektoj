package net.filipvanlaenen.kolektoj;

import static net.filipvanlaenen.kolektoj.Collection.ElementCardinality.DISTINCT_ELEMENTS;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

/**
 * Unit tests on the {@link net.filipvanlaenen.kolektoj.array.OrderedCollection} class.
 */
public class OrderedCollectionTest {
    /**
     * The magic number three.
     */
    private static final int THREE = 3;
    /**
     * The magic number four.
     */
    private static final int FOUR = 4;
    /**
     * The magic number five.
     */
    private static final int FIVE = 5;
    /**
     * Collection with the integer 1.
     */
    private final OrderedCollection<Integer> collection1 = OrderedCollection.of(1);
    /**
     * Collection with the integer 1 and 2.
     */
    private final OrderedCollection<Integer> collection12 = OrderedCollection.of(1, 2);
    /**
     * Collection with the integers 1, 2 and 3.
     */
    private final OrderedCollection<Integer> collection123 = OrderedCollection.of(1, 2, THREE);

    /**
     * Verifies that a sequence with zero integers is empty.
     */
    @Test
    public void createSequenceFromIndexOfLengthZeroShouldReturnEmptyCollection() {
        assertTrue(OrderedCollection.createSequence(i -> i, 0).isEmpty());
    }

    /**
     * Verifies that a sequence with one integer can be generated using the index to generate the elements.
     */
    @Test
    public void createSequenceShouldCreateSequenceWithOneIntegerFromIndex() {
        OrderedCollection<Integer> expected = OrderedCollection.of(0);
        OrderedCollection<Integer> actual = OrderedCollection.createSequence(i -> i, 1);
        assertTrue(actual.containsSame(expected));
    }

    /**
     * Verifies that a sequence with three integers can be generated using the index to generate the elements.
     */
    @Test
    public void createSequenceShouldCreateSequenceWithThreeIntegersFromIndex() {
        OrderedCollection<Integer> expected = OrderedCollection.of(0, 1, 2);
        OrderedCollection<Integer> actual = OrderedCollection.createSequence(i -> i, THREE);
        assertTrue(actual.containsSame(expected));
    }

    /**
     * Verifies that an empty sequence is created if the requested number of times is zero.
     */
    @Test
    public void createSequenceShouldCreateEmptySequenceFromFirstElementIfTimesIsZero() {
        OrderedCollection<Integer> actual = OrderedCollection.createSequence(1, i -> i + 1, 0);
        assertTrue(actual.isEmpty());
    }

    /**
     * Verifies that a sequence with one integer can be generated using the first element to generate the following
     * elements.
     */
    @Test
    public void createSequenceShouldCreateSequenceWithOneIntegerFromFirstElement() {
        OrderedCollection<Integer> actual = OrderedCollection.createSequence(1, i -> i + 1, 1);
        assertTrue(actual.containsSame(collection1));
    }

    /**
     * Verifies that a sequence with three integers can be generated using the first element to generate the following
     * elements.
     */
    @Test
    public void createSequenceShouldCreateSequenceWithThreeIntegersFromFirstElement() {
        OrderedCollection<Integer> actual = OrderedCollection.createSequence(1, i -> i + 1, THREE);
        assertTrue(actual.containsSame(collection123));
    }

    /**
     * Verifies that an empty collection is returned if a condition evaluates to false for the first index.
     */
    @Test
    public void createSequenceShouldReturnEmptyCollectionIfPredicateEvaluatesFalseForFirstIndex() {
        assertTrue(OrderedCollection.createSequence(i -> i, i -> i < 0).isEmpty());
    }

    /**
     * Verifies that a sequence with integers can be generated using the index to generate the elements until a
     * condition evaluates false.
     */
    @Test
    public void createSequenceShouldCreateSequenceWithIntegersFromIndexUntilPredicateEvaluatesFalse() {
        OrderedCollection<Integer> expected = OrderedCollection.of(0, 1, 2);
        OrderedCollection<Integer> actual = OrderedCollection.createSequence(i -> i, i -> i < THREE);
        assertTrue(actual.containsSame(expected));
    }

    /**
     * Verifies that an empty collection is generated if the predicate evaluates to false for the first element.
     */
    @Test
    public void createSequenceShouldReturnEmptyCollectionIfPredicateEvaluatesToFalseForFirstElement() {
        assertTrue(OrderedCollection.createSequence(1, i -> i + 1, i -> i <= 0).isEmpty());
    }

    /**
     * Verifies that a sequence with three integers can be generated using the first element to generate the following
     * elements.
     */
    @Test
    public void createSequenceShouldCreateSequenceWithIntegersFromFirstElementUntilPredicateEvaluatesFalse() {
        OrderedCollection<Integer> actual = OrderedCollection.createSequence(1, i -> i + 1, i -> i <= THREE);
        assertTrue(actual.containsSame(collection123));
    }

    /**
     * Verifies that the difference of one collection is that collection.
     */
    @Test
    public void differenceOfOneCollectionShouldBeTheSameCollection() {
        assertTrue(collection1.containsSame(OrderedCollection.differenceOf(collection1)));
    }

    /**
     * Verifies that the difference of three collections only contains the elements of the first collection that aren't
     * present in any of the other.
     */
    @Test
    public void differenceOfThreeCollectionsShouldOnlyContainTheElementsFromTheFirstCollectionNotInTheOthers() {
        assertTrue(Collection.of(3)
                .containsSame(OrderedCollection.differenceOf(collection123, collection1, collection12)));
    }

    /**
     * Verifies that getFirst returns the first element.
     */
    @Test
    public void getFirstShouldReturnTheFirstElement() {
        assertEquals(1, collection123.getFirst());
    }

    /**
     * Verifies that getLast returns the last element.
     */
    @Test
    public void getLastShouldReturnTheLastElement() {
        assertEquals(THREE, collection123.getLast());
    }

    /**
     * Verifies that the intersection of one collection is that collection.
     */
    @Test
    public void intersectionOfOneCollectionShouldBeTheSameCollection() {
        assertTrue(collection1.containsSame(OrderedCollection.intersectionOf(collection1)));
    }

    /**
     * Verifies that the intersection of three collections only contains the common elements.
     */
    @Test
    public void intersectionOfThreeCollectionsShouldOnlyContainTheCommonElements() {
        assertTrue(
                collection1.containsSame(OrderedCollection.intersectionOf(collection123, collection1, collection12)));
    }

    /**
     * Verifies that an empty ordered collection is empty.
     */
    @Test
    public void isEmptyShouldReturnTrueForAnEmptyCollection() {
        assertTrue(OrderedCollection.empty().isEmpty());
    }

    /**
     * Verifies that a collection containing an element is not empty.
     */
    @Test
    public void isEmptyShouldReturnFalseForACollectionContainingAnElement() {
        assertFalse(collection1.isEmpty());
    }

    /**
     * Verifies that an ordered collection with a specific element cardinality receives that element cardinality.
     */
    @Test
    public void ofWithElementCardinalityShouldReturnACollectionWithTheElementCardinality() {
        assertEquals(DISTINCT_ELEMENTS, OrderedCollection.of(DISTINCT_ELEMENTS, 1).getElementCardinality());
    }

    /**
     * Verifies that a modifiable ordered collection with a specific element cardinality receives that element
     * cardinality.
     */
    @Test
    public void ofWithElementCardinalityAndCollectionShouldReturnACollectionWithTheElementCardinality() {
        OrderedCollection<Number> clone = OrderedCollection.of(DISTINCT_ELEMENTS, OrderedCollection.of(1, 1));
        assertEquals(DISTINCT_ELEMENTS, clone.getElementCardinality());
        assertEquals(1, clone.size());
    }

    /**
     * Verifies that the of factory method using a collection clones a collection.
     */
    @Test
    public void ofWithCollectionShouldReturnAClone() {
        OrderedCollection<Number> clone = OrderedCollection.<Number>of(collection123);
        assertArrayEquals(collection123.toArray(), clone.toArray());
    }

    /**
     * Verifies that the of factory method using a collection and from and to indices clones a collection.
     */
    @Test
    public void ofWithCollectionAndIndicesShoudlReturnAClone() {
        OrderedCollection<Integer> collection = OrderedCollection.<Integer>of(1, 2, THREE, FOUR, FIVE);
        OrderedCollection<Number> slice = OrderedCollection.<Number>of(collection, 1, THREE);
        assertTrue(slice.containsSame(Collection.of(2, THREE)));
    }

    /**
     * Verifies that the union of no collections is empty.
     */
    @Test
    public void unionOfNoCollectionsShouldBeEmpty() {
        assertTrue(OrderedCollection.unionOf().isEmpty());
    }

    /**
     * Verifies that the union of one collection is that collection.
     */
    @Test
    public void unionOfOneCollectionShouldBeTheSameCollection() {
        assertTrue(collection1.containsSame(OrderedCollection.unionOf(collection1)));
    }

    /**
     * Verifies that the union of three collections only all elements.
     */
    @Test
    public void unionOfThreeCollectionsShouldContainAllElements() {
        assertArrayEquals(Collection.of(1, 2, THREE, 1, 1, 2).toArray(),
                OrderedCollection.unionOf(collection123, collection1, collection12).toArray());
    }

    /**
     * Verifies that the union of no collections is empty.
     */
    @Test
    public void unionOfNoCollectionsWithDistinctElementsShouldBeEmpty() {
        assertTrue(OrderedCollection.unionOf(DISTINCT_ELEMENTS).isEmpty());
    }

    /**
     * Verifies that the union of one collection is that collection.
     */
    @Test
    public void unionOfOneCollectionWithDistinctElementsShouldBeTheSameCollection() {
        assertTrue(collection1.containsSame(OrderedCollection.unionOf(DISTINCT_ELEMENTS, collection1)));
    }

    /**
     * Verifies that the union of three collections only all elements.
     */
    @Test
    public void unionOfThreeCollectionsWithDistinctElementsShouldContainAllDistinctElements() {
        assertArrayEquals(collection123.toArray(),
                OrderedCollection.unionOf(DISTINCT_ELEMENTS, collection123, collection1, collection12).toArray());
    }
}
