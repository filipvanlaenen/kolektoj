package net.filipvanlaenen.kolektoj;

import static net.filipvanlaenen.kolektoj.Collection.ElementCardinality.DISTINCT_ELEMENTS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * Unit tests on the {@link net.filipvanlaenen.kolektoj.array.OrderedCollection} class.
 */
public class OrderedCollectionTest {
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
}
