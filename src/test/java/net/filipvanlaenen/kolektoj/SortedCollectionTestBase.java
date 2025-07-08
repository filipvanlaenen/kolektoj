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
}
