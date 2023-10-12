package net.filipvanlaenen.kolektoj.array;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Comparator;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import net.filipvanlaenen.kolektoj.Collection;

/**
 * Unit tests on the {@link net.filipvanlaenen.kolektoj.array.OrderedArrayCollection} class.
 */
public class OrderedArrayCollectionTest {
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
    private static final OrderedArrayCollection<Integer> COLLECTION123 = new OrderedArrayCollection<Integer>(1, 2, 3);
    /**
     * Ordered array collection with the integers 1, 2, 3 and null.
     */
    private static final OrderedArrayCollection<Integer> COLLECTION123NULL =
            new OrderedArrayCollection<Integer>(1, 2, 3, null);

    /**
     * Verifies that the correct length is returned for a collection with three elements.
     */
    @Test
    public void sizeShouldReturnThreeForACollectionOfThreeElements() {
        assertEquals(THREE, COLLECTION123.size());
    }

    /**
     * Verifies that contains returns true for an element in the collection.
     */
    @Test
    public void containsShouldReturnTrueForAnElementInTheCollection() {
        assertTrue(COLLECTION123.contains(1));
    }

    /**
     * Verifies that contains returns true for null if it's in the collection.
     */
    @Test
    public void containsShouldReturnTrueForNullIfInTheCollection() {
        assertTrue(COLLECTION123NULL.contains(null));
    }

    /**
     * Verifies that contains returns false for an element not in the collection.
     */
    @Test
    public void containsShouldReturnFalseForAnElementNotInTheCollection() {
        assertFalse(COLLECTION123.contains(0));
    }

    /**
     * Verifies that contains returns false for null if it isn't in the collection.
     */
    @Test
    public void containsShouldReturnFalseForNullIfNotInTheTheCollection() {
        assertFalse(COLLECTION123.contains(null));
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
     * Verifies that when you get an element from a collection by its index, the correct element is returned.
     */
    @Test
    public void getAtShouldReturnCorrectElement() {
        assertEquals(2, COLLECTION123.getAt(1));
    }

    /**
     * Verifies that trying to get an element from an empty collection throws IndexOutOfBoundsException.
     */
    @Test
    public void getShouldThrowExceptionWhenCalledOnAnEmptyCollection() {
        IndexOutOfBoundsException exception =
                assertThrows(IndexOutOfBoundsException.class, () -> new OrderedArrayCollection<Integer>().get());
        assertEquals("Cannot return an element from an empty collection.", exception.getMessage());
    }

    /**
     * Verifies that when you try to get an element from a collection beyond the length, getAt throws
     * IndexOutOfBoundsException.
     */
    @Test
    public void getAtShouldThrowIndexOutOfBoundsExceptionWhenCalledBeyondSize() {
        IndexOutOfBoundsException exception =
                assertThrows(IndexOutOfBoundsException.class, () -> COLLECTION123.getAt(THREE));
        assertEquals("Cannot return an element at a position beyond the size of the collection.",
                exception.getMessage());
    }

    /**
     * Verifies that the collection produces an array with the elements in the same order.
     */
    @Test
    public void toArrayShouldProduceAnArrayWithTheElementsOfTheCollection() {
        assertArrayEquals(ARRAY123, COLLECTION123.toArray());
    }

    /**
     * Verifies that the collection produces an iterator that produces the elements in the correct order.
     */
    @Test
    public void iteratorShouldProduceCorrectStringInForLoop() {
        String actual = "";
        for (Integer i : COLLECTION123) {
            actual += i;
        }
        assertEquals("123", actual);
    }

    /**
     * Verifies that the collection produces a stream that collects to the correct string, thus verifying that the
     * spliterator is created correctly.
     */
    @Test
    public void streamShouldProduceAStreamThatCollectsToTheCorrectString() {
        assertEquals("1,2,3", COLLECTION123.stream().map(Object::toString).collect(Collectors.joining(",")));
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
}
