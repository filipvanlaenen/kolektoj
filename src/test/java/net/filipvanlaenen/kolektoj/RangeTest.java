package net.filipvanlaenen.kolektoj;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Comparator;

import org.junit.jupiter.api.Test;

/**
 * Unit tests on the {@link net.filipvanlaenen.kolektoj.Range} class.
 */
public class RangeTest {
    /**
     * The magic number three.
     */
    private static final int THREE = 3;
    /**
     * The magic number four.
     */
    private static final int FOUR = 4;
    /**
     * The range with all values.
     */
    private static final Range<Integer> ALL = Range.all();
    /**
     * The range with no values.
     */
    private static final Range<Integer> NONE = Range.none();
    /**
     * The range with the single value one.
     */
    private static final Range<Integer> ONE = Range.equalTo(1);
    /**
     * The range greater than one.
     */
    private static final Range<Integer> GREATER_THAN_ONE = Range.greaterThan(1);
    /**
     * The range greater than or equal to one.
     */
    private static final Range<Integer> GREATER_THAN_OR_EQUAL_TO_ONE = Range.greaterThanOrEqualTo(1);
    /**
     * The range greater than one and less than three.
     */
    private static final Range<Integer> GREATER_THAN_ONE_AND_LESS_THAN_THREE = Range.greaterThan(1).lessThan(THREE);
    /**
     * The range greater than one and less than or equal to three.
     */
    private static final Range<Integer> GREATER_THAN_ONE_AND_LESS_THAN_OR_EQUAL_TO_THREE =
            Range.greaterThan(1).lessThanOrEqualTo(THREE);
    /**
     * The range less than one.
     */
    private static final Range<Integer> LESS_THAN_ONE = Range.lessThan(1);
    /**
     * The range less than or equal to one.
     */
    private static final Range<Integer> LESS_THAN_ONE_OR_EQUAL_TO = Range.lessThanOrEqualTo(1);

    /**
     * Verifies that the factory method creates a range for all values.
     */
    @Test
    public void allShouldCreateARangeForAllValues() {
        assertEquals(new Range.All<Integer>(), Range.all());
    }

    /**
     * Verifies that the factory method creates a range for no values.
     */
    @Test
    public void noneShouldCreateARangeForNoValues() {
        assertEquals(new Range.None<Integer>(), Range.none());
    }

    /**
     * Verifies that the factory method creates a range for one value.
     */
    @Test
    public void equalToShouldCreateRangeForOneValue() {
        assertEquals(new Range.EqualTo<Integer>(1), Range.equalTo(1));
    }

    /**
     * Verifies that the factory method creates a range for greater than a value.
     */
    @Test
    public void greaterThanShouldCreateRangeForGreaterThanAValue() {
        assertEquals(new Range.GreaterThan<Integer>(1), Range.greaterThan(1));
    }

    /**
     * Verifies that the factory method creates a range for greater than or equal to a value.
     */
    @Test
    public void greaterThanOrEqualToShouldCreateRangeForGreaterThanOrEqualToAValue() {
        assertEquals(new Range.GreaterThanOrEqualTo<Integer>(1), Range.greaterThanOrEqualTo(1));
    }

    /**
     * Verifies that the factory method creates a range for less than a value.
     */
    @Test
    public void lessThanShouldCreateRangeForLessThanAValue() {
        assertEquals(new Range.LessThan<Integer>(1), Range.lessThan(1));
    }

    /**
     * Verifies that the factory method creates a range for less than or equal to a value.
     */
    @Test
    public void lessThanOrEqualToShouldCreateRangeForLessThanOrEqualToAValue() {
        assertEquals(new Range.LessThanOrEqualTo<Integer>(1), Range.lessThanOrEqualTo(1));
    }

    /**
     * Verifies that zero is not above all.
     */
    @Test
    public void zeroShouldNotBeAboveAll() {
        assertFalse(ALL.isAbove(Comparator.naturalOrder(), 0));
    }

    /**
     * Verifies that zero is not below all.
     */
    @Test
    public void zeroShouldNotBeBelowAll() {
        assertFalse(ALL.isBelow(Comparator.naturalOrder(), 0));
    }

    /**
     * Verifies that zero is above none.
     */
    @Test
    public void zeroShouldBeAboveNone() {
        assertTrue(NONE.isAbove(Comparator.naturalOrder(), 0));
    }

    /**
     * Verifies that zero is below none.
     */
    @Test
    public void zeroShouldBeBelowNone() {
        assertTrue(NONE.isBelow(Comparator.naturalOrder(), 0));
    }

    /**
     * Verifies that zero is below one.
     */
    @Test
    public void zeroShouldBeBelowOne() {
        assertTrue(ONE.isBelow(Comparator.naturalOrder(), 0));
    }

    /**
     * Verifies that one isn't below one.
     */
    @Test
    public void oneShouldNotBeBelowOne() {
        assertFalse(ONE.isBelow(Comparator.naturalOrder(), 1));
    }

    /**
     * Verifies that one isn't above one.
     */
    @Test
    public void oneShouldNotBeAboveOne() {
        assertFalse(ONE.isAbove(Comparator.naturalOrder(), 1));
    }

    /**
     * Verifies that two is above one.
     */
    @Test
    public void twoShouldBeAboveOne() {
        assertTrue(ONE.isAbove(Comparator.naturalOrder(), 2));
    }

    /**
     * Verifies that one is below greater than one.
     */
    @Test
    public void oneShouldBeBelowGreaterThanOne() {
        assertTrue(GREATER_THAN_ONE.isBelow(Comparator.naturalOrder(), 1));
    }

    /**
     * Verifies that two isn't below greater than one.
     */
    @Test
    public void twoShouldNotBeBelowGreaterThanOne() {
        assertFalse(GREATER_THAN_ONE.isBelow(Comparator.naturalOrder(), 2));
    }

    /**
     * Verifies that two isn't above greater than one.
     */
    @Test
    public void twoShouldNotBeAboveGreaterThanOne() {
        assertFalse(GREATER_THAN_ONE.isAbove(Comparator.naturalOrder(), 2));
    }

    /**
     * Verifies that zero is below greater than or equal to one.
     */
    @Test
    public void zeroShouldBeBelowGreaterThanOrEqualToOne() {
        assertTrue(GREATER_THAN_OR_EQUAL_TO_ONE.isBelow(Comparator.naturalOrder(), 0));
    }

    /**
     * Verifies that one isn't below greater than or equal to one.
     */
    @Test
    public void oneShouldNotBeBelowGreaterThanOrEqualToOne() {
        assertFalse(GREATER_THAN_OR_EQUAL_TO_ONE.isBelow(Comparator.naturalOrder(), 1));
    }

    /**
     * Verifies that two isn't above greater than or equal to one.
     */
    @Test
    public void twoShouldNotBeAboveGreaterThanOrEqualToOne() {
        assertFalse(GREATER_THAN_OR_EQUAL_TO_ONE.isAbove(Comparator.naturalOrder(), 2));
    }

    /**
     * Verifies that one is below greater than one and less than three.
     */
    @Test
    public void oneShouldBeBelowGreaterThanOneAndLessThanThree() {
        assertTrue(GREATER_THAN_ONE_AND_LESS_THAN_THREE.isBelow(Comparator.naturalOrder(), 1));
    }

    /**
     * Verifies that two isn't below greater than one and less than three.
     */
    @Test
    public void twoShouldNotBeBelowGreaterThanOneAndLessThanThree() {
        assertFalse(GREATER_THAN_ONE_AND_LESS_THAN_THREE.isBelow(Comparator.naturalOrder(), 2));
    }

    /**
     * Verifies that two isn't above greater than one and less than three.
     */
    @Test
    public void twoShouldNotBeAboveGreaterThanOneAndLessThanThree() {
        assertFalse(GREATER_THAN_ONE_AND_LESS_THAN_THREE.isAbove(Comparator.naturalOrder(), 2));
    }

    /**
     * Verifies that three is above greater than one and less than three.
     */
    @Test
    public void threeShouldBeAboveGreaterThanOneAndLessThanThree() {
        assertTrue(GREATER_THAN_ONE_AND_LESS_THAN_THREE.isAbove(Comparator.naturalOrder(), THREE));
    }

    /**
     * Verifies that one is below greater than one and less than or equal to three.
     */
    @Test
    public void oneShouldBeBelowGreaterThanOneAndLessThanOrEqualToThree() {
        assertTrue(GREATER_THAN_ONE_AND_LESS_THAN_OR_EQUAL_TO_THREE.isBelow(Comparator.naturalOrder(), 1));
    }

    /**
     * Verifies that two isn't below greater than one and less than or equal to three.
     */
    @Test
    public void twoShouldNotBeBelowGreaterThanOneAndLessThanOrEqualToThree() {
        assertFalse(GREATER_THAN_ONE_AND_LESS_THAN_OR_EQUAL_TO_THREE.isBelow(Comparator.naturalOrder(), 2));
    }

    /**
     * Verifies that three isn't above greater than one and less than or equal to three.
     */
    @Test
    public void threeShouldNotBeAboveGreaterThanOneAndLessThanOrEqualToThree() {
        assertFalse(GREATER_THAN_ONE_AND_LESS_THAN_OR_EQUAL_TO_THREE.isAbove(Comparator.naturalOrder(), THREE));
    }

    /**
     * Verifies that four is above greater than one and less than or equal to three.
     */
    @Test
    public void fourShouldBeAboveGreaterThanOneAndLessThanOrEqualToThree() {
        assertTrue(GREATER_THAN_ONE_AND_LESS_THAN_OR_EQUAL_TO_THREE.isAbove(Comparator.naturalOrder(), FOUR));
    }

    /**
     * Verifies that zero isn't below less than one.
     */
    @Test
    public void zeroShouldNotBeBelowLessThanOne() {
        assertFalse(LESS_THAN_ONE.isBelow(Comparator.naturalOrder(), 0));
    }

    /**
     * Verifies that zero isn't above less than one.
     */
    @Test
    public void zeroShouldNotBeAboveLessThanOne() {
        assertFalse(LESS_THAN_ONE.isAbove(Comparator.naturalOrder(), 0));
    }

    /**
     * Verifies that one is above less than one.
     */
    @Test
    public void oneShouldBeAboveLessThanOne() {
        assertTrue(LESS_THAN_ONE.isAbove(Comparator.naturalOrder(), 1));
    }

    /**
     * Verifies that zero isn't below less than or equal to one.
     */
    @Test
    public void zeroShouldNotBeBelowLessThanOrEqualToOne() {
        assertFalse(LESS_THAN_ONE_OR_EQUAL_TO.isBelow(Comparator.naturalOrder(), 0));
    }

    /**
     * Verifies that one isn't above less than or equal to one.
     */
    @Test
    public void oneShouldNotBeAboveLessThanOrEqualToOne() {
        assertFalse(LESS_THAN_ONE_OR_EQUAL_TO.isAbove(Comparator.naturalOrder(), 1));
    }

    /**
     * Verifies that two is above less than or equal to one.
     */
    @Test
    public void twoShouldBeAboveLessThanOrEqualToOne() {
        assertTrue(LESS_THAN_ONE_OR_EQUAL_TO.isAbove(Comparator.naturalOrder(), 2));
    }
}
