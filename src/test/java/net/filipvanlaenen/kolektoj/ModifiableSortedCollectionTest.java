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
     * Verifies that a modifiable sorted collection constructed with the empty factory method is empty.
     */
    @Test
    public void emptyShouldReturnAnEmptyCollection() {
        assertTrue(ModifiableSortedCollection.empty(COMPARATOR).isEmpty());
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
     * Verifies that the of factory method using a collection clones a collection.
     */
    @Test
    public void ofWithCollectionShoudlReturnAClone() {
        Collection<Integer> collection = Collection.<Integer>of(1, 2, THREE);
        ModifiableSortedCollection<Integer> clone = ModifiableSortedCollection.<Integer>of(COMPARATOR, collection);
        assertArrayEquals(collection.toArray(), clone.toArray());
    }

    /**
     * Verifies that the <code>of</code> factory method using a sorted collection clones a sorted collection.
     */
    @Test
    public void ofWithSortedCollectionShoudlReturnAClone() {
        SortedCollection<Integer> collection = SortedCollection.<Integer>of(COMPARATOR, 1, 2, THREE);
        ModifiableSortedCollection<Integer> clone = ModifiableSortedCollection.<Integer>of(collection);
        assertArrayEquals(collection.toArray(), clone.toArray());
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
}
