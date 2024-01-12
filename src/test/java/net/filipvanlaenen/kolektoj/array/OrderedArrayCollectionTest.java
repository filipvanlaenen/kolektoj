package net.filipvanlaenen.kolektoj.array;

import static net.filipvanlaenen.kolektoj.Collection.ElementCardinality.DISTINCT_ELEMENTS;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Comparator;

import org.junit.jupiter.api.Test;

import net.filipvanlaenen.kolektoj.Collection;
import net.filipvanlaenen.kolektoj.Collection.ElementCardinality;
import net.filipvanlaenen.kolektoj.OrderedCollection;
import net.filipvanlaenen.kolektoj.OrderedCollectionTestBase;

/**
 * Unit tests on the {@link net.filipvanlaenen.kolektoj.array.OrderedArrayCollection} class.
 */
public class OrderedArrayCollectionTest extends OrderedCollectionTestBase<OrderedArrayCollection<Integer>> {
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
     * The magic number six.
     */
    private static final int SIX = 6;
    /**
     * An array containing the numbers 1, 2 and 3.
     */
    private static final Integer[] ARRAY123 = new Integer[] {1, 2, THREE};
    /**
     * An array containing the numbers from 1 to 6.
     */
    private static final Integer[] ARRAY123456 = new Integer[] {1, 2, THREE, FOUR, FIVE, SIX};
    /**
     * Ordered array collection with the integers 1, 2 and 3.
     */
    private static final OrderedCollection<Integer> COLLECTION123 = new OrderedArrayCollection<Integer>(1, 2, 3);
    /**
     * Ordered array collection with the integers 1, 2, 3 and null.
     */
    private static final OrderedCollection<Integer> COLLECTION123NULL =
            new OrderedArrayCollection<Integer>(1, 2, 3, null);

    @Override
    protected OrderedArrayCollection<Integer> createOrderedCollection(Integer... integers) {
        return new OrderedArrayCollection<Integer>(integers);
    }

    @Override
    protected OrderedArrayCollection<Integer> createOrderedCollection(ElementCardinality elementCardinality,
            Integer... integers) {
        return new OrderedArrayCollection<Integer>(elementCardinality, integers);
    }

    /**
     * Verifies that contains returns true for an element in the collection.
     */
    @Test
    public void containsShouldReturnTrueForAnElementInTheCollection() {
        assertTrue(COLLECTION123.contains(1));
    }

    /**
     * Verifies that contains returns false for an element not in the collection.
     */
    @Test
    public void containsShouldReturnFalseForAnElementNotInTheCollection() {
        assertFalse(COLLECTION123.contains(0));
    }

    /**
     * Verifies that containsAll returns false is the other collection is larger.
     */
    @Test
    public void containsAllShouldReturnFalseIfTheOtherCollectionIsLarger() {
        assertFalse(COLLECTION123.containsAll(COLLECTION123NULL));
    }

    /**
     * Verifies that containsAll returns true if a collection is compared to itself.
     */
    @Test
    public void containsAllShouldReturnTrueWhenComparedToItself() {
        assertTrue(COLLECTION123.containsAll(COLLECTION123));
    }

    /**
     * Verifies that when you get an element from a collection, the collection contains it.
     */
    @Test
    public void getShouldReturnAnElementPresentInTheCollection() {
        Integer element = COLLECTION123.get();
        assertTrue(COLLECTION123.contains(element));
    }

    /**
     * Verifies that the collection constructed using another ordered collection produces the correct array.
     */
    @Test
    public void constructorUsingOrderedCollectionShouldProduceTheCorrectArray() {
        assertArrayEquals(ARRAY123, new OrderedArrayCollection<Integer>(COLLECTION123).toArray());
    }

    /**
     * Verifies that the collection constructed using another collection and the natural comparator produces the correct
     * array.
     */
    @Test
    public void constructorUsingCollectionAndNaturalOrderComparatorShouldProduceTheCorrectArray() {
        assertArrayEquals(ARRAY123456, new OrderedArrayCollection<Integer>(Collection.of(1, FIVE, SIX, 2, FOUR, THREE),
                Comparator.naturalOrder()).toArray());
    }

    /**
     * Verifies that the constructor using a comparator transfers the element cardinality correctly.
     */
    @Test
    public void constructorUsingCollectionAndNaturalOrderComparatorShouldTransferElementCardinality() {
        assertEquals(DISTINCT_ELEMENTS,
                new OrderedArrayCollection<Integer>(Collection.of(DISTINCT_ELEMENTS, 1, FIVE, SIX, 2, FOUR, THREE),
                        Comparator.naturalOrder()).getElementCardinality());
    }

    /**
     * Verifies that duplicate elements are removed if a collection with distinct elements is constructed.
     */
    @Test
    public void constructorShouldRemoveDuplicateElementsFromDistinctCollection() {
        OrderedCollection<Integer> collection = createOrderedCollection(DISTINCT_ELEMENTS, 1, 2, 2, THREE, 2, THREE);
        assertEquals(THREE, collection.size());
        assertTrue(collection.contains(1));
        assertTrue(collection.contains(2));
        assertTrue(collection.contains(THREE));
    }
}
