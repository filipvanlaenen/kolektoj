package net.filipvanlaenen.kolektoj;

import static net.filipvanlaenen.kolektoj.Collection.ElementCardinality.DISTINCT_ELEMENTS;
import static net.filipvanlaenen.kolektoj.Collection.ElementCardinality.DUPLICATE_ELEMENTS;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Comparator;
import java.util.Objects;

import org.junit.jupiter.api.Test;

/**
 * Unit tests on the {@link net.filipvanlaenen.kolektoj.array.ModifiableSortedCollection} class.
 */
public class ModifiableSortedCollectionTest {
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
    private final ModifiableSortedCollection<Integer> collection1 = ModifiableSortedCollection.of(COMPARATOR, 1);
    /**
     * Collection with the integer 1 and 2.
     */
    private final ModifiableSortedCollection<Integer> collection12 = ModifiableSortedCollection.of(COMPARATOR, 1, 2);
    /**
     * Collection with the integers 1, 2 and 3.
     */
    private final ModifiableSortedCollection<Integer> collection123 =
            ModifiableSortedCollection.of(COMPARATOR, 1, 2, THREE);

    /**
     * A comparator ordering integers in the natural order, but in addition handles <code>null</code> as the lowest
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
        assertTrue(ModifiableSortedCollection.differenceOf(COMPARATOR).isEmpty());
    }

    /**
     * Verifies that the difference of one collection is that collection.
     */
    @Test
    public void differenceOfOneCollectionShouldBeTheSameCollection() {
        assertTrue(collection1.containsSame(ModifiableSortedCollection.differenceOf(COMPARATOR, collection1)));
    }

    /**
     * Verifies that the difference of three collections only contains the elements of the first collection that aren't
     * present in any of the other.
     */
    @Test
    public void differenceOfThreeCollectionsShouldOnlyContainTheElementsFromTheFirstCollectionNotInTheOthers() {
        assertTrue(Collection.of(THREE).containsSame(
                ModifiableSortedCollection.differenceOf(COMPARATOR, collection123, collection1, collection12)));
    }

    /**
     * Verifies that the difference of one collection is that collection.
     */
    @Test
    public void differenceOfOneSortedCollectionShouldBeTheSameCollection() {
        assertTrue(collection1.containsSame(ModifiableSortedCollection.differenceOf(collection1)));
    }

    /**
     * Verifies that the difference of three collections only contains the elements of the first collection that aren't
     * present in any of the other.
     */
    @Test
    public void differenceOfThreeCollectionsShouldOnlyContainTheElementsFromTheSortedCollectionNotInTheOthers() {
        assertTrue(Collection.of(THREE)
                .containsSame(ModifiableSortedCollection.differenceOf(collection123, collection1, collection12)));
    }

    /**
     * Verifies that a modifiable sorted collection constructed with the empty factory method is empty.
     */
    @Test
    public void emptyShouldReturnAnEmptyCollection() {
        assertTrue(ModifiableSortedCollection.empty(COMPARATOR).isEmpty());
    }

    /**
     * Verifies that the intersection of no collections is empty.
     */
    @Test
    public void intersectionOfNoCollectionsShouldBeEmpty() {
        assertTrue(ModifiableSortedCollection.intersectionOf(COMPARATOR).isEmpty());
    }

    /**
     * Verifies that the intersection of one collection is that collection.
     */
    @Test
    public void intersectionOfOneCollectionShouldBeTheSameCollection() {
        assertTrue(collection1.containsSame(ModifiableSortedCollection.intersectionOf(COMPARATOR, collection1)));
    }

    /**
     * Verifies that the intersection of three collections only contains the common elements.
     */
    @Test
    public void intersectionOfThreeCollectionsShouldOnlyContainTheCommonElements() {
        assertTrue(collection1.containsSame(
                ModifiableSortedCollection.intersectionOf(COMPARATOR, collection123, collection1, collection12)));
    }

    /**
     * Verifies that the intersection of one sorted collection is that collection.
     */
    @Test
    public void intersectionOfOneSortedCollectionShouldBeTheSameCollection() {
        assertTrue(collection1.containsSame(ModifiableSortedCollection.intersectionOf(collection1)));
    }

    /**
     * Verifies that the intersection of three collections only contains the common elements.
     */
    @Test
    public void intersectionOfSortedCollectionAndTwoCollectionsShouldOnlyContainTheCommonElements() {
        assertTrue(collection1.containsSame(
                ModifiableSortedCollection.intersectionOf(collection123, collection1, Collection.of(1, 2))));
    }

    /**
     * Verifies that a modifiable sorted collection constructed with the of factory method contains the provided
     * elements.
     */
    @Test
    public void ofShouldConstructACollectionContainingTheProvidedElements() {
        ModifiableSortedCollection<Integer> collection = ModifiableSortedCollection.of(COMPARATOR, 1, 2);
        assertEquals(DUPLICATE_ELEMENTS, collection.getElementCardinality());
        assertEquals(2, collection.size());
        assertTrue(collection.contains(1));
        assertTrue(collection.contains(2));
    }

    /**
     * Verifies that a modifiable sorted collection constructed with the of factory method contains the provided
     * elements.
     */
    @Test
    public void ofWithElementCardinalityShouldConstructACollectionContainingTheProvidedElementCardinalityAndElements() {
        ModifiableSortedCollection<Integer> collection =
                ModifiableSortedCollection.of(DISTINCT_ELEMENTS, COMPARATOR, 1, 2);
        assertEquals(2, collection.size());
        assertTrue(collection.contains(1));
        assertTrue(collection.contains(2));
    }

    /**
     * Verifies that a modifiable ordered collection with a specific element cardinality receives that element
     * cardinality.
     */
    @Test
    public void ofWithElementCardinalityAndCollectionShouldReturnACollectionWithTheElementCardinality() {
        ModifiableSortedCollection<Integer> clone =
                ModifiableSortedCollection.of(DISTINCT_ELEMENTS, COMPARATOR, Collection.of(1, 1));
        assertEquals(DISTINCT_ELEMENTS, clone.getElementCardinality());
        assertEquals(1, clone.size());
    }

    /**
     * Verifies that the of factory method using a collection clones a collection.
     */
    @Test
    public void ofWithCollectionShoudlReturnAClone() {
        ModifiableSortedCollection<Integer> clone = ModifiableSortedCollection.<Integer>of(COMPARATOR, collection123);
        assertArrayEquals(collection123.toArray(), clone.toArray());
    }

    /**
     * Verifies that the of factory method using a collection and from and to indices clones a collection.
     */
    @Test
    public void ofWithCollectionAndIndicesShouldReturnAClone() {
        OrderedCollection<Integer> collection = OrderedCollection.<Integer>of(1, 2, THREE, FOUR, FIVE);
        ModifiableSortedCollection<Integer> slice =
                ModifiableSortedCollection.<Integer>of(COMPARATOR, collection, 1, THREE);
        assertTrue(slice.containsSame(Collection.of(2, THREE)));
    }

    /**
     * Verifies that the of factory method using a collection and a range clones a collection.
     */
    @Test
    public void ofWithCollectionAndRangeShouldReturnAClone() {
        SortedCollection<Integer> collection = SortedCollection.<Integer>of(COMPARATOR, 1, 2, THREE, FOUR, FIVE);
        ModifiableSortedCollection<Integer> slice =
                ModifiableSortedCollection.<Integer>of(collection, Range.greaterThan(1).lessThan(FIVE));
        assertTrue(slice.containsSame(Collection.of(2, THREE, FOUR)));
    }

    /**
     * Verifies that the <code>of</code> factory method using a sorted collection clones a sorted collection.
     */
    @Test
    public void ofWithSortedCollectionShoudlReturnAClone() {
        ModifiableSortedCollection<Integer> clone = ModifiableSortedCollection.<Integer>of(collection123);
        assertArrayEquals(collection123.toArray(), clone.toArray());
        assertEquals(COMPARATOR, clone.getComparator());
    }

    /**
     * Verifies that <code>removeLeast</code> returns and removes the least element.
     */
    @Test
    public void removeLeastShouldRemoveTheLeastElement() {
        ModifiableSortedCollection<Integer> collection =
                ModifiableSortedCollection.<Integer>of(COMPARATOR, 1, 2, THREE);
        assertEquals(1, collection.removeLeast());
        assertFalse(collection.contains(1));
    }

    /**
     * Verifies that <code>removeGreatest</code> returns and removes the least element.
     */
    @Test
    public void removeGreatestShouldRemoveTheGreatestElement() {
        ModifiableSortedCollection<Integer> collection =
                ModifiableSortedCollection.<Integer>of(COMPARATOR, 1, 2, THREE);
        assertEquals(THREE, collection.removeGreatest());
        assertFalse(collection.contains(THREE));
    }

    /**
     * Verifies that the union of no collections is empty.
     */
    @Test
    public void unionOfNoCollectionsShouldBeEmpty() {
        assertTrue(ModifiableSortedCollection.unionOf(COMPARATOR).isEmpty());
    }

    /**
     * Verifies that the union of one collection is that collection.
     */
    @Test
    public void unionOfOneCollectionShouldBeTheSameCollection() {
        assertTrue(collection1.containsSame(ModifiableSortedCollection.unionOf(COMPARATOR, collection1)));
    }

    /**
     * Verifies that the union of three collections only all elements.
     */
    @Test
    public void unionOfThreeCollectionsShouldContainAllElements() {
        assertTrue(Collection.of(1, 2, THREE, 1, 1, 2).containsSame(
                ModifiableSortedCollection.unionOf(COMPARATOR, collection123, collection1, collection12)));
    }

    /**
     * Verifies that the union of one collection is that collection.
     */
    @Test
    public void unionOfOneSortedCollectionShouldBeTheSameCollection() {
        assertTrue(collection1.containsSame(ModifiableSortedCollection.unionOf(collection1)));
    }

    /**
     * Verifies that the union of three collections only all elements.
     */
    @Test
    public void unionOfThreeSortedCollectionsShouldContainAllElements() {
        assertTrue(Collection.of(1, 2, THREE, 1, 1, 2)
                .containsSame(ModifiableSortedCollection.unionOf(collection123, collection1, collection12)));
    }
}
