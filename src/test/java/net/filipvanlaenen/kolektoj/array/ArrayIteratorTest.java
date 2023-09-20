package net.filipvanlaenen.kolektoj.array;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Iterator;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.Test;

/**
 * Unit tests on the {@link net.filipvanlaenen.kolektoj.array.ArrayIterator} class.
 */
public class ArrayIteratorTest {
    /**
     * An array to create an iterator on for the unit tests.
     */
    private static final Integer[] ARRAY123 = new Integer[] {1, 2, 3};

    /**
     * Creates a new iterator.
     *
     * @return A new iterator.
     */
    private ArrayIterator<Integer> createNewArrayIterator() {
        return new ArrayIterator<Integer>(ARRAY123);
    }

    /**
     * Verifies that when a new iterator is created with three elements, the hasNext method returns true at its first
     * call.
     */
    @Test
    public void hasNextShouldReturnTrueOnFirstCallForAnIteratorWithThreeElements() {
        assertTrue(createNewArrayIterator().hasNext());
    }

    /**
     * Verifies that if the next method has been called three times on an iterator with three elements, the hasNext
     * method returns false.
     */
    @Test
    public void hasNextShouldReturnFalseAfterThreeNextCallsOnAnIteratorWithThreeElements() {
        Iterator<Integer> iterator = createNewArrayIterator();
        iterator.next();
        iterator.next();
        iterator.next();
        assertFalse(iterator.hasNext());
    }

    /**
     * Verifies that the method next returns the first element when it's called the first time on a new iterator.
     */
    @Test
    public void nextShouldReturnFirstElementOnFirstCall() {
        assertEquals(1, createNewArrayIterator().next());
    }

    /**
     * Verifies that the method next throws a NoSuchElementException when it's called a fourth time on an iterator with
     * three elements.
     */
    @Test
    public void nextShouldThrowNoSuchElementWhenCalledFourthTimeOnAnIteratorWithThreeElements() {
        Iterator<Integer> iterator = createNewArrayIterator();
        iterator.next();
        iterator.next();
        iterator.next();
        assertThrows(NoSuchElementException.class, () -> iterator.next());
    }

    /**
     * Verifies that the method remove throws an UnsupportedOperationException when it's called.
     */
    @Test
    public void removeShouldThrowUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> createNewArrayIterator().remove());
    }
}
