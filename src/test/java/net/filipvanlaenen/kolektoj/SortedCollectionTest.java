package net.filipvanlaenen.kolektoj;

import static net.filipvanlaenen.kolektoj.Collection.ElementCardinality.DISTINCT_ELEMENTS;
import static org.junit.jupiter.api.Assertions.*;

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
     * The magic number four.
     */
    private static final int FOUR = 4;
    /**
     * The magic number five.
     */
    private static final int FIVE = 5;

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
     * Verifies that the <code>of</code> factory method using a collection clones a collection.
     */
    @Test
    public void ofWithCollectionShoudlReturnAClone() {
        Collection<Integer> collection = Collection.<Integer>of(1, 2, THREE);
        SortedCollection<Integer> clone = SortedCollection.<Integer>of(COMPARATOR, collection);
        assertArrayEquals(collection.toArray(), clone.toArray());
    }

    /**
     * Verifies that the of factory method using a collection and from and to indices clones a collection.
     */
    @Test
    public void ofWithCollectionAndIndicesShouldReturnAClone() {
        OrderedCollection<Integer> collection = OrderedCollection.<Integer>of(1, 2, THREE, FOUR, FIVE);
        SortedCollection<Integer> slice = SortedCollection.<Integer>of(COMPARATOR, collection, 1, THREE);
        assertTrue(slice.containsSame(Collection.of(2, THREE)));
    }

    /**
     * Verifies that the <code>of</code> factory method using a sorted collection clones a sorted collection.
     */
    @Test
    public void ofWithSortedCollectionShoudlReturnAClone() {
        SortedCollection<Integer> collection = SortedCollection.<Integer>of(COMPARATOR, 1, 2, THREE);
        SortedCollection<Integer> clone = SortedCollection.<Integer>of(collection);
        assertArrayEquals(collection.toArray(), clone.toArray());
        assertEquals(COMPARATOR, clone.getComparator());
    }

    /**
     * Verifies that the <code>getGreatest</code> returns the greatest element.
     */
    @Test
    public void getGreatestReturnsGreatestElement() {
        SortedCollection<Integer> collection = SortedCollection.<Integer>of(COMPARATOR, 1, 2, THREE);
        assertEquals(THREE, collection.getGreatest());
    }

    /**
     * Verifies that the <code>getLeast</code> returns the greatest element.
     */
    @Test
    public void getLeastReturnsLeastElement() {
        SortedCollection<Integer> collection = SortedCollection.<Integer>of(COMPARATOR, 1, 2, THREE);
        assertEquals(1, collection.getLeast());
    }
}
