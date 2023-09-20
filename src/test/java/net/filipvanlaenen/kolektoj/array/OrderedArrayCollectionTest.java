package net.filipvanlaenen.kolektoj.array;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

/**
 * Unit tests on the {@link net.filipvanlaenen.kolektoj.array.OrderedArrayCollection} class.
 */
public class OrderedArrayCollectionTest {
    /**
     * The magic number three.
     */
    private static final int THREE = 3;
    /**
     * Ordered array collection with the integers 1, 2 and 3.
     */
    private static final OrderedArrayCollection<Integer> COLLECTION123 = new OrderedArrayCollection<Integer>(1, 2, 3);

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
     * Verifies that contains returns false for an element not in the collection.
     */
    @Test
    public void containsShouldReturnFalseForAnElementNotInTheCollection() {
        assertFalse(COLLECTION123.contains(0));
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
        assertArrayEquals(new Integer[] {1, 2, THREE}, COLLECTION123.toArray());
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
}
