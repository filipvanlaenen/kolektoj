package net.filipvanlaenen.kolektoj;

import static net.filipvanlaenen.kolektoj.Collection.ElementCardinality.DISTINCT_ELEMENTS;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Comparator;
import java.util.Objects;

import org.junit.jupiter.api.Test;

/**
 * Unit tests on the {@link net.filipvanlaenen.kolektoj.array.SortedCollection} class.
 */
public class SortedCollectionTest {
    /**
     * The magic number three.
     */
    private static final int THREE = 3;

    /**
     * A comparator ordering integers in the natural order, but in addition handling <code>null</code> as the lowest
     * value.
     */
    private static final Comparator<Integer> COMPARATOR = new Comparator<Integer>() {
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
     * Verifies that an empty ordered collection is empty.
     */
    @Test
    public void isEmptyShouldReturnTrueForAnEmptyCollection() {
        assertTrue(SortedCollection.empty(new Comparator<Object>() {
            @Override
            public int compare(final Object o1, final Object o2) {
                return 0;
            }
        }).isEmpty());
    }

    /**
     * Verifies that a collection containing an element is not empty.
     */
    @Test
    public void isEmptyShouldReturnFalseForACollectionContainingAnElement() {
        assertFalse(SortedCollection.of(COMPARATOR, 1).isEmpty());
    }

    /**
     * Verifies that an ordered collection with a specific element cardinality receives that element cardinality.
     */
    @Test
    public void ofWithElementCardinalityShouldReturnACollectionWithTheElementCardinality() {
        assertEquals(DISTINCT_ELEMENTS, SortedCollection.of(DISTINCT_ELEMENTS, COMPARATOR, 1).getElementCardinality());
    }

    /**
     * Verifies that the of factory method using a collection clones a collection.
     */
    @Test
    public void ofWithCollectionShoudlReturnAClone() {
        Collection<Integer> collection = Collection.<Integer>of(1, 2, THREE);
        SortedCollection<Integer> clone = SortedCollection.<Integer>of(COMPARATOR, collection);
        assertArrayEquals(collection.toArray(), clone.toArray());
    }
}
