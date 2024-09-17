package net.filipvanlaenen.kolektoj;

import static net.filipvanlaenen.kolektoj.Collection.ElementCardinality.DISTINCT_ELEMENTS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * Unit tests on the {@link net.filipvanlaenen.kolektoj.array.OrderedValueCollection} class.
 */
public class OrderedValueCollectionTest {
    /**
     * An ordered value collection to run tests on.
     */
    private static final OrderedValueCollection<Integer> ORDERED_VALUE_COLLECTION = OrderedValueCollection.of(1);
    /**
     * A large ordered value collection to run tests on.
     */
    private static final OrderedValueCollection<Integer> LARGE_ORDERED_VALUE_COLLECTION =
            OrderedValueCollection.of(1, 2);
    /**
     * An ordered value collection with the same elements as the other large ordered value collection but in a different
     * order to run tests on.
     */
    private static final OrderedValueCollection<Integer> OTHER_LARGE_ORDERED_VALUE_COLLECTION =
            OrderedValueCollection.of(2, 1);

    /**
     * Verifies that an ordered value collection is not equal to a String.
     */
    @Test
    public void anOrderedValueCollectionShouldNotBeEqualToAString() {
        assertFalse(ORDERED_VALUE_COLLECTION.equals(""));
    }

    /**
     * Verifies that an ordered value collection is equal to itself.
     */
    @Test
    public void anOrderedValueCollectionShouldBeEqualToItself() {
        assertEquals(ORDERED_VALUE_COLLECTION, ORDERED_VALUE_COLLECTION);
    }

    /**
     * Verifies that an ordered value collection is equal to another ordered value collection with the same elements in
     * the same order.
     */
    @Test
    public void orderedValueCollectionsShouldBeEqualWhenTheSameElementsAreInTheSameOrder() {
        assertEquals(LARGE_ORDERED_VALUE_COLLECTION, OrderedValueCollection.of(1, 2));
    }

    /**
     * Verifies that an ordered value collection is not equal to another ordered value collection with the same elements
     * but in a different order.
     */
    @Test
    public void orderedValueCollectionsShouldNotBeEqualWhenTheElementsAreInADifferentOrder() {
        assertFalse(LARGE_ORDERED_VALUE_COLLECTION.equals(OTHER_LARGE_ORDERED_VALUE_COLLECTION));
    }

    /**
     * Verifies that an ordered value collection is not equal to another ordered value collection with different
     * elements.
     */
    @Test
    public void orderedValueCollectionsShouldNotBeEqualWhenTheElementsAreDifferent() {
        assertFalse(ORDERED_VALUE_COLLECTION.equals(LARGE_ORDERED_VALUE_COLLECTION));
    }

    /**
     * Verifies that an ordered value collection has the same hash code as itself.
     */
    @Test
    public void anOrderedValueCollectionShouldHaveTheSameHashcodeAsItself() {
        assertEquals(ORDERED_VALUE_COLLECTION.hashCode(), ORDERED_VALUE_COLLECTION.hashCode());
    }

    /**
     * Verifies that an ordered value collection has the same hash code as another value collection with the same
     * elements in the same order.
     */
    @Test
    public void orderedValueCollectionsShouldHaveTheSameHashCodeWhenTheSameElementsAreInTheSameOrder() {
        assertEquals(LARGE_ORDERED_VALUE_COLLECTION.hashCode(), OrderedValueCollection.of(1, 2).hashCode());
    }

    /**
     * Verifies that an ordered value collection (in general) does not have the same hash code as another ordered value
     * collection with the same elements in a different order.
     */
    @Test
    public void orderedValueCollectionsShouldNotHaveTheSameHashCodeWhenTheElementsAreInADifferentOrder() {
        assertFalse(LARGE_ORDERED_VALUE_COLLECTION.hashCode() == OTHER_LARGE_ORDERED_VALUE_COLLECTION.hashCode());
    }

    /**
     * Verifies that an empty ordered value collection is empty.
     */
    @Test
    public void isEmptyShouldReturnTrueForAnEmptyOrderedValueCollection() {
        assertTrue(OrderedValueCollection.empty().isEmpty());
    }

    /**
     * Verifies that an ordered value collection with a specific element cardinality receives that element cardinality.
     */
    @Test
    public void ofWithElementCardinalityShouldReturnAnOrderedValueCollectionWithTheElementCardinality() {
        assertEquals(DISTINCT_ELEMENTS, OrderedValueCollection.of(DISTINCT_ELEMENTS, 1).getElementCardinality());
    }

    /**
     * Verifies that the <code>contains</code> method is wired correctly to the internal collection.
     */
    @Test
    public void containsShouldBeWiredCorrectlyToTheInternalCollection() {
        assertTrue(ORDERED_VALUE_COLLECTION.contains(1));
        assertFalse(ORDERED_VALUE_COLLECTION.contains(0));
    }

    /**
     * Verifies that the <code>containsAll</code> method is wired correctly to the internal collection.
     */
    @Test
    public void containsAllShouldBeWiredCorrectlyToTheInternalCollection() {
        assertTrue(LARGE_ORDERED_VALUE_COLLECTION.containsAll(ORDERED_VALUE_COLLECTION));
        assertFalse(LARGE_ORDERED_VALUE_COLLECTION.containsAll(ValueCollection.of(0)));
    }

    /**
     * Verifies that the <code>get</code> method is wired correctly to the internal collection.
     */
    @Test
    public void getShouldBeWiredCorrectlyToTheInternalCollection() {
        assertTrue(LARGE_ORDERED_VALUE_COLLECTION.contains(LARGE_ORDERED_VALUE_COLLECTION.get()));
    }

    /**
     * Verifies that the <code>getAt</code> method is wired correctly to the internal collection.
     */
    @Test
    public void getAtShouldBeWiredCorrectlyToTheInternalCollection() {
        assertEquals(1, LARGE_ORDERED_VALUE_COLLECTION.getAt(0));
    }

    /**
     * Verifies that the <code>size</code> method is wired correctly to the internal collection.
     */
    @Test
    public void sizeShouldBeWiredCorrectlyToTheInternalCollection() {
        assertEquals(1, ORDERED_VALUE_COLLECTION.size());
    }

    /**
     * Verifies that the <code>spliterator</code> method is wired correctly to the internal collection.
     */
    @Test
    public void spliteratorShouldBeWiredCorrectlyToTheInternalCollection() {
        assertEquals(2, LARGE_ORDERED_VALUE_COLLECTION.spliterator().estimateSize());
    }

    /**
     * Verifies that the <code>toArray</code> method is wired correctly to the internal collection.
     */
    @Test
    public void toArrayShouldBeWiredCorrectlyToTheInternalCollection() {
        assertEquals(2, LARGE_ORDERED_VALUE_COLLECTION.toArray().length);
    }
}
