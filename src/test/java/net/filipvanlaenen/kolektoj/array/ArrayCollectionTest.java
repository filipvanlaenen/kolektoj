package net.filipvanlaenen.kolektoj.array;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;

/**
 * Unit tests on the {@link net.filipvanlaenen.kolektoj.array.ArrayCollection} class.
 */
public class ArrayCollectionTest {
    /**
     * The magic number three.
     */
    private static final int THREE = 3;
    /**
     * The magic number six.
     */
    private static final int SIX = 6;
    /**
     * Collection with the integers 1, 2 and 3.
     */
    private static final ArrayCollection<Integer> COLLECTION123 = new ArrayCollection<Integer>(1, 2, 3);

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
     * Verifies that trying to get an element from an empty collection throws IndexOutOfBoundsException.
     */
    @Test
    public void getShouldThrowExceptionWhenCalledOnAnEmptyCollection() {
        try {
            new ArrayCollection<Integer>().get();
            fail();
        } catch (IndexOutOfBoundsException e) {
            assertEquals("Cannot return an element from an empty collection.", e.getMessage());
        }
    }

    /**
     * Verifies that the collection produces an array with the elements.
     */
    @Test
    public void toArrayShouldProduceAnArrayWithTheElementsOfTheCollection() {
        ArrayCollection<Integer> collection = new ArrayCollection<Integer>(1, 2);
        Integer[] actual = collection.toArray();
        assertTrue(actual.length == 2 && (actual[0] == 1 || actual[1] == 1) && (actual[0] == 2 || actual[1] == 2));
    }

    /**
     * Verifies that the collection produces a stream that reduces to the correct sum, thus verifying that the
     * spliterator is created correctly.
     */
    @Test
    public void streamShouldProduceAStreamThatReducesToTheCorrectSum() {
        assertEquals(SIX, COLLECTION123.stream().reduce(0, Integer::sum));
    }

    /**
     * Verifies that the collection produces an iterator that when used in a for loop, produces the correct sum.
     */
    @Test
    public void iteratorShouldProduceCorrectSumInForLoop() {
        int sum = 0;
        for (Integer i : COLLECTION123) {
            sum += i;
        }
        assertEquals(SIX, sum);
    }
}
