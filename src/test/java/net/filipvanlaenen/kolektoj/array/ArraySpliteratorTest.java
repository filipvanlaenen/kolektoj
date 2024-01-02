package net.filipvanlaenen.kolektoj.array;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Comparator;
import java.util.Spliterator;

import org.junit.jupiter.api.Test;

/**
 * Unit tests on the {@link net.filipvanlaenen.kolektoj.array.ArraySpliterator} class.
 */
public class ArraySpliteratorTest {
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
     * An array containing the numbers from 1 to 6.
     */
    private static final Integer[] ARRAY123456 = new Integer[] {1, 2, THREE, FOUR, FIVE, SIX};

    /**
     * Creates a new spliterator.
     *
     * @return A new spliterator.
     */
    private Spliterator<Integer> createNewSpliterator() {
        return new ArraySpliterator<Integer>(ARRAY123456, 0);
    }

    /**
     * Verifies that the size of a new spliterator is correct.
     */
    @Test
    public void estimateSizeShouldReturnCorrectSize() {
        assertEquals(SIX, createNewSpliterator().estimateSize());
    }

    /**
     * Verifies that tryAdvance returns true for a new spliterator.
     */
    @Test
    public void tryAdvanceShouldReturnTrueForANewSpliterator() {
        assertTrue(createNewSpliterator().tryAdvance(Integer::longValue));
    }

    /**
     * Verifies that calling tryAdvance makes the length of the spliterator shorter.
     */
    @Test
    public void tryAdvanceShouldReduceTheLengthByOne() {
        Spliterator<Integer> spliterator = createNewSpliterator();
        spliterator.tryAdvance(Integer::longValue);
        assertEquals(FIVE, spliterator.estimateSize());
    }

    /**
     * Verifies that tryAdvance calls the consumer.
     */
    @Test
    public void tryAdanceShouldCallTheConsumer() {
        StringBuffer sb = new StringBuffer();
        createNewSpliterator().tryAdvance((x) -> sb.append(x));
        assertEquals("1", sb.toString());
    }

    /**
     * Verifies that tryAdvance returns false after having been called as many times as there are elements.
     */
    @Test
    public void tryAdvanceShouldReturnFalseAfterTheLastElement() {
        Spliterator<Integer> spliterator = createNewSpliterator();
        for (int i = 0; i < SIX; i++) {
            spliterator.tryAdvance(Integer::longValue);
        }
        assertFalse(spliterator.tryAdvance(Integer::longValue));
    }

    /**
     * Verifies that trySplit splits the spliterator in two equal-sized spliterators.
     */
    @Test
    public void trySplitShouldSplitTheSpliteratorInTwoEqualSizedSpliterators() {
        Spliterator<Integer> spliterator = createNewSpliterator();
        Spliterator<Integer> spliterator2 = spliterator.trySplit();
        assertEquals(THREE, spliterator.estimateSize());
        assertEquals(THREE, spliterator2.estimateSize());
    }

    /**
     * Verifies that trySplit splits the spliterator in two almost equal-sized spliterators after advancing once.
     */
    @Test
    public void trySplitShouldSplitTheSpliteratorInTwoAlmostEqualSizedSpliteratorsAfterAdvancingOnce() {
        Spliterator<Integer> spliterator = createNewSpliterator();
        spliterator.tryAdvance(Integer::longValue);
        Spliterator<Integer> spliterator2 = spliterator.trySplit();
        assertEquals(THREE, spliterator.estimateSize());
        assertEquals(2, spliterator2.estimateSize());
    }

    /**
     * Verifies that trySplit returns null if there's only one element left.
     */
    @Test
    public void trySplitShouldReturnNullIfOnlyOneElementLeft() {
        Spliterator<Integer> spliterator = createNewSpliterator();
        for (int i = 0; i < FIVE; i++) {
            spliterator.tryAdvance(Integer::longValue);
        }
        assertNull(spliterator.trySplit());
        assertEquals(1, spliterator.estimateSize());
    }

    /**
     * Verifies that trySplit returns a spliterator starting with the right element.
     */
    @Test
    public void trySplitShouldReturnASpliteratorStartingAtTheCorrectElement() {
        Spliterator<Integer> spliterator = createNewSpliterator();
        Spliterator<Integer> spliterator2 = spliterator.trySplit();
        StringBuffer sb = new StringBuffer();
        spliterator2.tryAdvance((x) -> sb.append(x));
        assertEquals("1", sb.toString());
    }

    /**
     * Verifies that trySplit moves the current spliterator to the right element.
     */
    @Test
    public void trySplitShouldMoveSpliteratorToTheCorrectElement() {
        Spliterator<Integer> spliterator = createNewSpliterator();
        spliterator.trySplit();
        StringBuffer sb = new StringBuffer();
        spliterator.tryAdvance((x) -> sb.append(x));
        assertEquals("4", sb.toString());
    }

    /**
     * Verifies that a default spliterator has the correct characteristics.
     */
    @Test
    public void characteristicsShouldReturnTheDefaultCharacteristicsByDefault() {
        assertEquals(Spliterator.SIZED | Spliterator.SUBSIZED, createNewSpliterator().characteristics());
    }

    /**
     * Verifies that additional characteristics are added to the spliterator.
     */
    @Test
    public void additionalCharacteristicsShouldBeAddedToTheSpliterator() {
        assertEquals(Spliterator.SIZED | Spliterator.SORTED | Spliterator.SUBSIZED,
                new ArraySpliterator<Integer>(ARRAY123456, Spliterator.SORTED).characteristics());
    }

    /**
     * Verifies that getComparator returns <code>null</code> when no comparator is provided.
     */
    @Test
    public void getComparatorShouldReturnNullWhenNoComparatorWasProvided() {
        assertNull(createNewSpliterator().getComparator());
    }

    /**
     * Verifies that getComparator returns the provided comparator.
     */
    @Test
    public void getComparatorShouldReturnTheProvidedComparator() {
        Comparator<Integer> comparator = Comparator.naturalOrder();
        ArraySpliterator<Integer> spliterator = new ArraySpliterator<Integer>(ARRAY123456, 0, comparator);
        assertEquals(comparator, spliterator.getComparator());
    }
}
