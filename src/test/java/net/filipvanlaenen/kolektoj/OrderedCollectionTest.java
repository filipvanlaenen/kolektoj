package net.filipvanlaenen.kolektoj;

import static net.filipvanlaenen.kolektoj.Collection.ElementCardinality.DISTINCT_ELEMENTS;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
        assertFalse(OrderedCollection.of(1).isEmpty());
    }

    /**
     * Verifies that an ordered collection with a specific element cardinality receives that element cardinality.
     */
    @Test
    public void ofWithElementCardinalityShouldReturnACollectionWithTheElementCardinality() {
        assertEquals(DISTINCT_ELEMENTS, OrderedCollection.of(DISTINCT_ELEMENTS, 1).getElementCardinality());
    }

    /**
     * Verifies that the of factory method using a collection clones a collection.
     */
    @Test
    public void ofWithCollectionShoudlReturnAClone() {
        OrderedCollection<Integer> collection = OrderedCollection.<Integer>of(1, 2, THREE);
        OrderedCollection<Number> clone = OrderedCollection.<Number>of(collection);
        assertArrayEquals(collection.toArray(), clone.toArray());
    }

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
        OrderedCollection<Integer> expected = OrderedCollection.of(1);
        OrderedCollection<Integer> actual = OrderedCollection.createSequence(1, i -> i + 1, 1);
        assertTrue(actual.containsSame(expected));
    }

    /**
     * Verifies that a sequence with three integers can be generated using the first element to generate the following
     * elements.
     */
    @Test
    public void createSequenceShouldCreateSequenceWithThreeIntegersFromFirstElement() {
        OrderedCollection<Integer> expected = OrderedCollection.of(1, 2, THREE);
        OrderedCollection<Integer> actual = OrderedCollection.createSequence(1, i -> i + 1, THREE);
        assertTrue(actual.containsSame(expected));
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
        OrderedCollection<Integer> expected = OrderedCollection.of(1, 2, THREE);
        OrderedCollection<Integer> actual = OrderedCollection.createSequence(1, i -> i + 1, i -> i <= THREE);
        assertTrue(actual.containsSame(expected));
    }

    /**
     * Verifies that getFirst returns the first element.
     */
    @Test
    public void getFirstShouldReturnTheFirstElement() {
        assertEquals(1, OrderedCollection.of(1, 2, THREE).getFirst());
    }

    /**
     * Verifies that getLast returns the last element.
     */
    @Test
    public void getLastShouldReturnTheLastElement() {
        assertEquals(THREE, OrderedCollection.of(1, 2, THREE).getLast());
    }
}
