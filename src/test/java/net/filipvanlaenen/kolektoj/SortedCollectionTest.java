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
     * Collection with the integer 1.
     */
    private final SortedCollection<Integer> collection1 = SortedCollection.of(COMPARATOR, 1);
    /**
     * Collection with the integer 1 and 2.
     */
    private final SortedCollection<Integer> collection12 = SortedCollection.of(COMPARATOR, 1, 2);
    /**
     * Collection with the integers 1, 2 and 3.
     */
    private final SortedCollection<Integer> collection123 = SortedCollection.of(COMPARATOR, 1, 2, THREE);

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
     * Verifies that the difference of no collections is empty.
     */
    @Test
    public void differenceOfNoCollectionsShouldBeEmpty() {
        assertTrue(SortedCollection.differenceOf(COMPARATOR).isEmpty());
    }

    /**
     * Verifies that the difference of one collection is that collection.
     */
    @Test
    public void differenceOfOneCollectionShouldBeTheSameCollection() {
        assertTrue(collection1.containsSame(SortedCollection.differenceOf(COMPARATOR, collection1)));
    }

    /**
     * Verifies that the difference of three collections only contains the elements of the first collection that aren't
     * present in any of the other.
     */
    @Test
    public void differenceOfThreeCollectionsShouldOnlyContainTheElementsFromTheFirstCollectionNotInTheOthers() {
        assertTrue(Collection.of(3)
                .containsSame(SortedCollection.differenceOf(COMPARATOR, collection123, collection1, collection12)));
    }

    /**
     * Verifies that the difference of one collection is that collection.
     */
    @Test
    public void differenceOfOneSortedCollectionShouldBeTheSameCollection() {
        assertTrue(collection1.containsSame(SortedCollection.differenceOf(collection1)));
    }

    /**
     * Verifies that the difference of three collections only contains the elements of the first collection that aren't
     * present in any of the other.
     */
    @Test
    public void differenceOfThreeCollectionsShouldOnlyContainTheElementsFromTheSortedCollectionNotInTheOthers() {
        assertTrue(
                Collection.of(3).containsSame(SortedCollection.differenceOf(collection123, collection1, collection12)));
    }

    /**
     * Verifies that the intersection of no collections is empty.
     */
    @Test
    public void intersectionOfNoCollectionsShouldBeEmpty() {
        assertTrue(SortedCollection.intersectionOf(COMPARATOR).isEmpty());
    }

    /**
     * Verifies that the intersection of one collection is that collection.
     */
    @Test
    public void intersectionOfOneCollectionShouldBeTheSameCollection() {
        assertTrue(collection1.containsSame(SortedCollection.intersectionOf(COMPARATOR, collection1)));
    }

    /**
     * Verifies that the intersection of three collections only contains the common elements.
     */
    @Test
    public void intersectionOfThreeCollectionsShouldOnlyContainTheCommonElements() {
        assertTrue(collection1
                .containsSame(SortedCollection.intersectionOf(COMPARATOR, collection123, collection1, collection12)));
    }

    /**
     * Verifies that the intersection of one sorted collection is that collection.
     */
    @Test
    public void intersectionOfOneSortedCollectionShouldBeTheSameCollection() {
        assertTrue(collection1.containsSame(SortedCollection.intersectionOf(collection1)));
    }

    /**
     * Verifies that the intersection of three collections only contains the common elements.
     */
    @Test
    public void intersectionOfSortedCollectionAndTwoCollectionsShouldOnlyContainTheCommonElements() {
        assertTrue(collection1
                .containsSame(SortedCollection.intersectionOf(collection123, collection1, Collection.of(1, 2))));
    }

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
        assertFalse(collection1.isEmpty());
    }

    /**
     * Verifies that an ordered collection with a specific element cardinality receives that element cardinality.
     */
    @Test
    public void ofWithElementCardinalityShouldReturnACollectionWithTheElementCardinality() {
        assertEquals(DISTINCT_ELEMENTS, SortedCollection.of(DISTINCT_ELEMENTS, COMPARATOR, 1).getElementCardinality());
    }

    /**
     * Verifies that a modifiable ordered collection with a specific element cardinality receives that element
     * cardinality.
     */
    @Test
    public void ofWithElementCardinalityAndCollectionShouldReturnACollectionWithTheElementCardinality() {
        SortedCollection<Integer> clone = SortedCollection.of(DISTINCT_ELEMENTS, COMPARATOR, Collection.of(1, 1));
        assertEquals(DISTINCT_ELEMENTS, clone.getElementCardinality());
        assertEquals(1, clone.size());
    }

    /**
     * Verifies that the <code>of</code> factory method using a collection clones a collection.
     */
    @Test
    public void ofWithCollectionShoudlReturnAClone() {
        SortedCollection<Integer> clone = SortedCollection.<Integer>of(COMPARATOR, collection123);
        assertArrayEquals(collection123.toArray(), clone.toArray());
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
     * Verifies that the of factory method using a collection and a range clones a collection.
     */
    @Test
    public void ofWithCollectionAndRangeShouldReturnAClone() {
        SortedCollection<Integer> collection = SortedCollection.<Integer>of(COMPARATOR, 1, 2, THREE, FOUR, FIVE);
        SortedCollection<Integer> slice = SortedCollection.<Integer>of(collection, Range.greaterThan(1).lessThan(FIVE));
        assertTrue(slice.containsSame(Collection.of(2, THREE, FOUR)));
    }

    /**
     * Verifies that the <code>of</code> factory method using a sorted collection clones a sorted collection.
     */
    @Test
    public void ofWithSortedCollectionShoudlReturnAClone() {
        SortedCollection<Integer> clone = SortedCollection.<Integer>of(collection123);
        assertArrayEquals(collection123.toArray(), clone.toArray());
        assertEquals(COMPARATOR, clone.getComparator());
    }

    /**
     * Verifies that the <code>getGreatest</code> returns the greatest element.
     */
    @Test
    public void getGreatestReturnsGreatestElement() {
        assertEquals(THREE, collection123.getGreatest());
    }

    /**
     * Verifies that the <code>getLeast</code> returns the greatest element.
     */
    @Test
    public void getLeastReturnsLeastElement() {
        assertEquals(1, collection123.getLeast());
    }

    /**
     * Verifies that the union of no collections is empty.
     */
    @Test
    public void unionOfNoCollectionsShouldBeEmpty() {
        assertTrue(SortedCollection.unionOf(COMPARATOR).isEmpty());
    }

    /**
     * Verifies that the union of one collection is that collection.
     */
    @Test
    public void unionOfOneCollectionShouldBeTheSameCollection() {
        assertTrue(collection1.containsSame(SortedCollection.unionOf(COMPARATOR, collection1)));
    }

    /**
     * Verifies that the union of three collections only all elements.
     */
    @Test
    public void unionOfThreeCollectionsShouldContainAllElements() {
        assertTrue(Collection.of(1, 2, THREE, 1, 1, 2)
                .containsSame(SortedCollection.unionOf(COMPARATOR, collection123, collection1, collection12)));
    }

    /**
     * Verifies that the union of one collection is that collection.
     */
    @Test
    public void unionOfOneSortedCollectionShouldBeTheSameCollection() {
        assertTrue(collection1.containsSame(SortedCollection.unionOf(collection1)));
    }

    /**
     * Verifies that the union of three collections only all elements.
     */
    @Test
    public void unionOfThreeSortedCollectionsShouldContainAllElements() {
        assertTrue(Collection.of(1, 2, THREE, 1, 1, 2)
                .containsSame(SortedCollection.unionOf(collection123, collection1, collection12)));
    }
}
