package net.filipvanlaenen.kolektoj;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Comparator;
import java.util.Objects;

import org.junit.jupiter.api.Test;

/**
 * Unit tests on implementations of the {@link net.filipvanlaenen.kolektoj.SortedCollection} interface.
 *
 * @param <T> The subclass type to be tested.
 */
public abstract class SortedCollectionTestBase<T extends SortedCollection<Integer>>
        extends OrderedCollectionTestBase<T> {
    /**
     * The magic number four.
     */
    private static final int FOUR = 4;
    /**
     * The magic number five.
     */
    private static final int FIVE = 5;
    /**
     * The magic number sixteen.
     */
    private static final int SIXTEEN = 16;
    /**
     * The magic number seventeen.
     */
    private static final int SEVENTEEN = 17;
    /**
     * The magic number eighteen.
     */
    private static final int EIGHTEEN = 18;
    /**
     * Sorted collection using the modulo five comparator.
     */
    private final T mod5collection = createSortedCollection(MOD5COMPARATOR, 1, 2, 3, 6, 7, 8, 11, 12, 13);
    /**
     * Large sorted collection using the modulo five comparator.
     */
    private final T largeMod5collection =
            createSortedCollection(MOD5COMPARATOR, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14);

    /**
     * A comparator ordering integers in the natural order, but in addition handling <code>null</code> as the lowest
     * value.
     */
    protected static final Comparator<Integer> COMPARATOR = new Comparator<Integer>() {
        @Override
        public int compare(final Integer i1, final Integer i2) {
            if (Objects.equals(i1, i2)) {
                return 0;
            } else if (i1 == null) {
                return -1;
            } else if (i2 == null) {
                return 1;
            } else if (i1 < i2) {
                return -1;
            } else {
                return 1;
            }
        }
    };
    /**
     * A comparator ordering integers in the natural order, but modulo five, and handling <code>null</code> as the
     * lowest value.
     */
    protected static final Comparator<Integer> MOD5COMPARATOR = new Comparator<Integer>() {
        @Override
        public int compare(final Integer i1, final Integer i2) {
            if (Objects.equals(i1, i2)) {
                return 0;
            } else if (i1 == null) {
                return -1;
            } else if (i2 == null) {
                return 1;
            } else {
                int m1 = i1 % FIVE;
                int m2 = i2 % FIVE;
                if (m1 == m2) {
                    return 0;
                } else if (m1 < m2) {
                    return -1;
                } else {
                    return 1;
                }
            }
        }
    };

    /**
     * Creates a sorted collection with the specified comparator containing the provided integers.
     *
     * @param comparator The comparator for the sorted collection.
     * @param integers   The integers to be included in the sorted collection.
     * @return A sorted collection with the specified comparator containing the provided integers.
     */
    protected abstract T createSortedCollection(Comparator<Integer> comparator, Integer... integers);

    /**
     * Verifies that <code>getComparator</code> returns the comparator used to create the sorted collection.
     */
    @Test
    public void getComparatorShouldReturnTheProvidedComparator() {
        T collection = createSortedCollection(COMPARATOR, 1, 2);
        assertEquals(COMPARATOR, collection.getComparator());
    }

    /**
     * Verifies that firstIndexOf returns the correct index for elements in the collection.
     */
    @Test
    public void firstIndexOfShouldReturnIndexForAnElementInTheCollection() {
        for (int i : mod5collection) {
            assertEquals(i, mod5collection.getAt(mod5collection.firstIndexOf(i)));
        }
    }

    /**
     * Verifies that firstIndexOf returns -1 for elements not in the collection.
     */
    @Test
    public void firstIndexOfShouldReturnMinusOneForAnElementNotInTheCollection() {
        assertEquals(-1, mod5collection.firstIndexOf(SIXTEEN));
        assertEquals(-1, mod5collection.firstIndexOf(SEVENTEEN));
        assertEquals(-1, mod5collection.firstIndexOf(EIGHTEEN));
        assertEquals(-1, mod5collection.firstIndexOf(FOUR));
    }

    /**
     * Verifies that indexOf returns the correct index for elements in the collection.
     */
    @Test
    public void indexOfShouldReturnIndexForAnElementInTheCollection() {
        for (int i : mod5collection) {
            assertEquals(i, mod5collection.getAt(mod5collection.indexOf(i)));
        }
        for (int i : largeMod5collection) {
            assertEquals(i, largeMod5collection.getAt(largeMod5collection.indexOf(i)));
        }
    }

    /**
     * Verifies that indexOf returns -1 for elements not in the collection.
     */
    @Test
    public void indexOfShouldReturnMinusOneForAnElementNotInTheCollection() {
        assertEquals(-1, mod5collection.indexOf(SIXTEEN));
        assertEquals(-1, mod5collection.indexOf(SEVENTEEN));
        assertEquals(-1, mod5collection.indexOf(EIGHTEEN));
        assertEquals(-1, mod5collection.indexOf(FOUR));
    }

    /**
     * Verifies that lastIndexOf returns the correct index for elements in the collection.
     */
    @Test
    public void lastIndexOfShouldReturnIndexForAnElementInTheCollection() {
        for (int i : mod5collection) {
            assertEquals(i, mod5collection.getAt(mod5collection.lastIndexOf(i)));
        }
    }

    /**
     * Verifies that lastIndexOf returns -1 for elements not in the collection.
     */
    @Test
    public void lastIndexOfShouldReturnMinusOneForAnElementNotInTheCollection() {
        assertEquals(-1, mod5collection.lastIndexOf(SIXTEEN));
        assertEquals(-1, mod5collection.lastIndexOf(SEVENTEEN));
        assertEquals(-1, mod5collection.lastIndexOf(EIGHTEEN));
        assertEquals(-1, mod5collection.lastIndexOf(FOUR));
    }
}
