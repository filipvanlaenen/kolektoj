package net.filipvanlaenen.kolektoj;

import static net.filipvanlaenen.kolektoj.Collection.ElementCardinality.DISTINCT_ELEMENTS;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

/**
 * Unit tests on the {@link net.filipvanlaenen.kolektoj.array.ModifiableOrderedCollection} class.
 */
public class ModifiableOrderedCollectionTest {
    /**
     * The magic number three.
     */
    private static final int THREE = 3;
    /**
     * The magic number four.
     */
    private static final int FOUR = 4;

    /**
     * Verifies that an empty modifiable ordered collection is empty.
     */
    @Test
    public void isEmptyShouldReturnTrueForAnEmptyCollection() {
        assertTrue(ModifiableOrderedCollection.empty().isEmpty());
    }

    /**
     * Verifies that a collection containing an element is not empty.
     */
    @Test
    public void isEmptyShouldReturnFalseForACollectionContainingAnElement() {
        assertFalse(ModifiableOrderedCollection.of(1).isEmpty());
    }

    /**
     * Verifies that a modifiable ordered collection with a specific element cardinality receives that element
     * cardinality.
     */
    @Test
    public void ofWithElementCardinalityShouldReturnACollectionWithTheElementCardinality() {
        assertEquals(DISTINCT_ELEMENTS, ModifiableOrderedCollection.of(DISTINCT_ELEMENTS, 1).getElementCardinality());
    }

    /**
     * Verifies that the of factory method using a collection clones a collection.
     */
    @Test
    public void ofWithCollectionShoudlReturnAClone() {
        OrderedCollection<Integer> collection = OrderedCollection.<Integer>of(1, 2, THREE);
        ModifiableOrderedCollection<Number> clone = ModifiableOrderedCollection.<Number>of(collection);
        assertArrayEquals(collection.toArray(), clone.toArray());
    }

    /**
     * Verifies that addFirst adds an element to the start of an ordered collection.
     */
    @Test
    public void addFirstShouldAddElementToStart() {
        OrderedCollection<Integer> expected = OrderedCollection.of(FOUR, 1, 2, THREE);
        ModifiableOrderedCollection<Integer> actual = ModifiableOrderedCollection.of(1, 2, THREE);
        assertTrue(actual.addFirst(FOUR));
        assertTrue(actual.containsSame(expected));
    }

    /**
     * Verifies that addFirst doesn't add an element to the start of an ordered collection with distinct elements if the
     * element is already present.
     */
    @Test
    public void addFirstShouldNotAddDuplicateElementToStart() {
        OrderedCollection<Integer> expected = OrderedCollection.of(1, 2, THREE);
        ModifiableOrderedCollection<Integer> actual = ModifiableOrderedCollection.of(DISTINCT_ELEMENTS, 1, 2, THREE);
        assertFalse(actual.addFirst(THREE));
        assertTrue(actual.containsSame(expected));
    }

    /**
     * Verifies that addLast adds an element to the end of an ordered collection.
     */
    @Test
    public void addLastShouldAddElementToStart() {
        OrderedCollection<Integer> expected = OrderedCollection.of(1, 2, THREE, FOUR);
        ModifiableOrderedCollection<Integer> actual = ModifiableOrderedCollection.of(1, 2, THREE);
        assertTrue(actual.addLast(FOUR));
        assertTrue(actual.containsSame(expected));
    }

    /**
     * Verifies that addLast doesn't add an element to the end of an ordered collection with distinct elements if the
     * element is already present.
     */
    @Test
    public void addLastShouldNotAddDuplicateElementToStart() {
        OrderedCollection<Integer> expected = OrderedCollection.of(1, 2, THREE);
        ModifiableOrderedCollection<Integer> actual = ModifiableOrderedCollection.of(DISTINCT_ELEMENTS, 1, 2, THREE);
        assertFalse(actual.addLast(1));
        assertTrue(actual.containsSame(expected));
    }

    /**
     * Verifies that removeFirst removes the first element of an ordered collection.
     */
    @Test
    public void removeFirstShouldRemoveElementAtStart() {
        OrderedCollection<Integer> expected = OrderedCollection.of(2, THREE);
        ModifiableOrderedCollection<Integer> actual = ModifiableOrderedCollection.of(1, 2, THREE);
        assertEquals(1, actual.removeFirst());
        assertTrue(actual.containsSame(expected));
    }

    /**
     * Verifies that removeLast removes the last element of an ordered collection.
     */
    @Test
    public void removeLastShouldRemoveElementAtEnd() {
        OrderedCollection<Integer> expected = OrderedCollection.of(1, 2);
        ModifiableOrderedCollection<Integer> actual = ModifiableOrderedCollection.of(1, 2, THREE);
        assertEquals(THREE, actual.removeLast());
        assertTrue(actual.containsSame(expected));
    }
}
