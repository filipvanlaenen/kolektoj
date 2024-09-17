package net.filipvanlaenen.kolektoj;

import static net.filipvanlaenen.kolektoj.Collection.ElementCardinality.DISTINCT_ELEMENTS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * Unit tests on the {@link net.filipvanlaenen.kolektoj.array.ValueCollection} class.
 */
public class ValueCollectionTest {
    /**
     * A value collection to run tests on.
     */
    private static final ValueCollection<Integer> VALUE_COLLECTION = ValueCollection.of(1);
    /**
     * A large value collection to run tests on.
     */
    private static final ValueCollection<Integer> LARGE_VALUE_COLLECTION = ValueCollection.of(1, 2);
    /**
     * A value collection with the same elements as the other large value collection to run tests on.
     */
    private static final ValueCollection<Integer> EQUAL_LARGE_VALUE_COLLECTION = ValueCollection.of(2, 1);

    /**
     * Verifies that a value collection is not equal to a String.
     */
    @Test
    public void aValueCollectionShouldNotBeEqualToAString() {
        assertFalse(VALUE_COLLECTION.equals(""));
    }

    /**
     * Verifies that a value collection is equal to itself.
     */
    @Test
    public void aValueCollectionShouldBeEqualToItself() {
        assertEquals(VALUE_COLLECTION, VALUE_COLLECTION);
    }

    /**
     * Verifies that a value collection is equal to another value collection with the same elements.
     */
    @Test
    public void aValueCollectionShouldBeEqualToAnotherValueCollectionWithTheSameElements() {
        assertEquals(LARGE_VALUE_COLLECTION, EQUAL_LARGE_VALUE_COLLECTION);
    }

    /**
     * Verifies that a value collection is not equal to another value collection with different elements.
     */
    @Test
    public void aValueCollectionShouldNotBeEqualToAnotherValueCollectionWithDifferentElements() {
        assertFalse(VALUE_COLLECTION.equals(LARGE_VALUE_COLLECTION));
    }

    /**
     * Verifies that a value collection has the same hash code as itself.
     */
    @Test
    public void aValueCollectionShouldHaveTheSameHashcodeAsItself() {
        assertEquals(VALUE_COLLECTION.hashCode(), VALUE_COLLECTION.hashCode());
    }

    /**
     * Verifies that a value collection has the same hash code as another value collection with the same elements.
     */
    @Test
    public void aValueCollectionShouldHaveTheSameHashCodeAsAnotherValueCollectionWithTheSameElements() {
        assertEquals(LARGE_VALUE_COLLECTION.hashCode(), EQUAL_LARGE_VALUE_COLLECTION.hashCode());
    }

    /**
     * Verifies that a value collection (in general) does not have the same hash code as a value collection with
     * different elements.
     */
    @Test
    public void aValueCollectionShouldNotHaveTheSameHashCodeAsAnotherValueCollectionWithDifferentElements() {
        assertFalse(VALUE_COLLECTION.hashCode() == LARGE_VALUE_COLLECTION.hashCode());
    }

    /**
     * Verifies that an empty value collection is empty.
     */
    @Test
    public void isEmptyShouldReturnTrueForAnEmptyValueCollection() {
        assertTrue(ValueCollection.empty().isEmpty());
    }

    /**
     * Verifies that a value collection with a specific element cardinality receives that element cardinality.
     */
    @Test
    public void ofWithElementCardinalityShouldReturnAValueCollectionWithTheElementCardinality() {
        assertEquals(DISTINCT_ELEMENTS, ValueCollection.of(DISTINCT_ELEMENTS, 1).getElementCardinality());
    }

    /**
     * Verifies that the <code>contains</code> method is wired correctly to the internal collection.
     */
    @Test
    public void containsShouldBeWiredCorrectlyToTheInternalCollection() {
        assertTrue(VALUE_COLLECTION.contains(1));
        assertFalse(VALUE_COLLECTION.contains(0));
    }

    /**
     * Verifies that the <code>containsAll</code> method is wired correctly to the internal collection.
     */
    @Test
    public void containsAllShouldBeWiredCorrectlyToTheInternalCollection() {
        assertTrue(LARGE_VALUE_COLLECTION.containsAll(VALUE_COLLECTION));
        assertFalse(LARGE_VALUE_COLLECTION.containsAll(ValueCollection.of(0)));
    }

    /**
     * Verifies that the <code>get</code> method is wired correctly to the internal collection.
     */
    @Test
    public void getShouldBeWiredCorrectlyToTheInternalCollection() {
        assertTrue(LARGE_VALUE_COLLECTION.contains(LARGE_VALUE_COLLECTION.get()));
    }

    /**
     * Verifies that the <code>spliterator</code> method is wired correctly to the internal collection.
     */
    @Test
    public void spliteratorShouldBeWiredCorrectlyToTheInternalCollection() {
        assertEquals(2, LARGE_VALUE_COLLECTION.spliterator().estimateSize());
    }

    /**
     * Verifies that the <code>toArray</code> method is wired correctly to the internal collection.
     */
    @Test
    public void toArrayShouldBeWiredCorrectlyToTheInternalCollection() {
        assertEquals(2, LARGE_VALUE_COLLECTION.toArray().length);
    }
}
