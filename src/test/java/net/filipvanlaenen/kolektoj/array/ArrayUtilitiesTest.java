package net.filipvanlaenen.kolektoj.array;

import static net.filipvanlaenen.kolektoj.Collection.ElementCardinality.DISTINCT_ELEMENTS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Comparator;
import java.util.Objects;

import org.junit.jupiter.api.Test;

import net.filipvanlaenen.kolektoj.Collection;
import net.filipvanlaenen.kolektoj.Map.Entry;
import net.filipvanlaenen.kolektoj.SortedCollection;

/**
 * Unit tests on the {@link net.filipvanlaenen.kolektoj.array.ArrayUtilities} class.
 */
public class ArrayUtilitiesTest {
    /**
     * Entry 1.
     */
    private static final Entry<Integer, String> ENTRY1 = new Entry<Integer, String>(1, "one");
    /**
     * Entry 1 bis.
     */
    private static final Entry<Integer, String> ENTRY1BIS = new Entry<Integer, String>(1, "bis");
    /**
     * Entry 1 ter.
     */
    private static final Entry<Integer, String> ENTRY1TER = new Entry<Integer, String>(1, "ter");
    /**
     * Entry 1 quater.
     */
    private static final Entry<Integer, String> ENTRY1QUATER = new Entry<Integer, String>(1, "quater");
    /**
     * Entry 2.
     */
    private static final Entry<Integer, String> ENTRY2 = new Entry<Integer, String>(2, "two");
    /**
     * The magic number three.
     */
    private static final int THREE = 3;
    /**
     * The magic number four.
     */
    private static final int FOUR = 4;
    /**
     * The magic number one million.
     */
    private static final int ONE_MILLION = 1000000;
    /**
     * Collection with the integers 1, 2 and 3.
     */
    private static final Collection<Integer> COLLECTION123 = new ArrayCollection<Integer>(1, 2, 3);
    /**
     * Collection with the integers 1, 2, 3 and null.
     */
    private static final Collection<Integer> COLLECTION123NULL = new ArrayCollection<Integer>(1, 2, 3, null);
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
     * Sorted collection with the integers 1, 2 and 3.
     */
    private static final SortedCollection<Integer> SORTED_COLLECTION123 =
            new SortedArrayCollection<Integer>(COMPARATOR, 1, 2, 3);
    /**
     * Sorted collection with the integers 1, 2, 3 and null.
     */
    private static final SortedCollection<Integer> SORTED_COLLECTION123NULL =
            new SortedArrayCollection<Integer>(COMPARATOR, 1, 2, 3, null);

    /**
     * Verifies that cloneDistinctElements removes duplicate elements. The method is tested through the constructor for
     * the ArrayCollection class.
     */
    @Test
    public void cloneDistinctElementsShouldRemoveDuplicateElementsInArrayCollectionConstructor() {
        Collection<Integer> collection = new ArrayCollection<Integer>(DISTINCT_ELEMENTS, 1, 2, 2, THREE, 2, THREE);
        assertEquals(THREE, collection.size());
        assertTrue(collection.contains(1));
        assertTrue(collection.contains(2));
        assertTrue(collection.contains(THREE));
    }

    /**
     * Verifies that cloneDistinctElements removes duplicate null elements. The method is tested through the constructor
     * for the ArrayCollection class.
     */
    @Test
    public void cloneDistinctElementsShouldRemoveDuplicateNullElementsInArrayCollectionConstructor() {
        Collection<Integer> collection = new ArrayCollection<Integer>(DISTINCT_ELEMENTS, 1, null, null, 2, null);
        assertEquals(THREE, collection.size());
        assertTrue(collection.contains(null));
        assertTrue(collection.contains(1));
        assertTrue(collection.contains(2));
    }

    /**
     * Verifies that contains returns true for an element in the collection. The method is tested through the contains
     * method in the ArrayCollection class.
     */
    @Test
    public void containsShouldReturnTrueForAnElementInTheCollection() {
        assertTrue(COLLECTION123.contains(1));
    }

    /**
     * Verifies that contains returns true for null if it's in the collection. The method is tested through the contains
     * method in the ArrayCollection class.
     */
    @Test
    public void containsShouldReturnTrueForNullIfInTheCollection() {
        assertTrue(COLLECTION123NULL.contains(null));
    }

    /**
     * Verifies that contains returns false for an element not in the collection. The method is tested through the
     * contains method in the ArrayCollection class.
     */
    @Test
    public void containsShouldReturnFalseForAnElementNotInTheCollection() {
        assertFalse(COLLECTION123.contains(0));
    }

    /**
     * Verifies that contains returns false for null if it isn't in the collection. The method is tested through the
     * contains method in the ArrayCollection class.
     */
    @Test
    public void containsShouldReturnFalseForNullIfNotInTheTheCollection() {
        assertFalse(COLLECTION123.contains(null));
    }

    /**
     * Verifies that contains returns true for an element in the sorted collection. The method is tested through the
     * contains method in the SortedArrayCollection class.
     */
    @Test
    public void containsShouldReturnTrueForAnElementInTheSortedCollection() {
        assertTrue(SORTED_COLLECTION123.contains(1));
        assertTrue(SORTED_COLLECTION123.contains(2));
        assertTrue(SORTED_COLLECTION123.contains(THREE));
    }

    /**
     * Verifies that contains returns true for null if it's in the sorted collection. The method is tested through the
     * contains method in the SortedArrayCollection class.
     */
    @Test
    public void containsShouldReturnTrueForNullIfInTheSortedCollection() {
        assertTrue(SORTED_COLLECTION123NULL.contains(null));
    }

    /**
     * Verifies that contains returns false for an element not in the sorted collection. The method is tested through
     * the contains method in the SortedArrayCollection class.
     */
    @Test
    public void containsShouldReturnFalseForAnElementNotInTheSortedCollection() {
        assertFalse(SORTED_COLLECTION123.contains(0));
        assertFalse(SORTED_COLLECTION123.contains(FOUR));
    }

    /**
     * Verifies that contains returns false for null if it isn't in the sorted collection. The method is tested through
     * the contains method in the SortedArrayCollection class.
     */
    @Test
    public void containsShouldReturnFalseForNullIfNotInTheTheSortedCollection() {
        assertFalse(SORTED_COLLECTION123.contains(null));
    }

    /**
     * Verifies that containsAll returns false if the other collection is larger. The method is tested through the
     * containsAll method in the ArrayCollection class.
     */
    @Test
    public void containsAllShouldReturnFalseIfTheOtherCollectionIsLarger() {
        assertFalse(COLLECTION123.containsAll(COLLECTION123NULL));
    }

    /**
     * Verifies that containsAll returns true if a collection is compared to itself. The method is tested through the
     * containsAll method in the ArrayCollection class.
     */
    @Test
    public void containsAllShouldReturnTrueWhenComparedToItself() {
        assertTrue(COLLECTION123.containsAll(COLLECTION123));
    }

    /**
     * Verifies that containsAll returns false if a collection contains another element. The method is tested through
     * the containsAll method in the ArrayCollection class.
     */
    @Test
    public void containsAllShouldReturnFalseWhenComparedToCollectionWithAnotherElement() {
        assertFalse(COLLECTION123.containsAll(new ArrayCollection<Integer>(0, 1, 2)));
    }

    /**
     * Verifies that containsAll returns false if the other sorted collection is larger. The method is tested through
     * the containsAll method in the SortedArrayCollection class.
     */
    @Test
    public void containsAllShouldReturnFalseIfTheOtherSortedCollectionIsLarger() {
        assertFalse(SORTED_COLLECTION123.containsAll(SORTED_COLLECTION123NULL));
    }

    /**
     * Verifies that containsAll returns true if a sorted collection is compared to itself. The method is tested through
     * the containsAll method in the SortedArrayCollection class.
     */
    @Test
    public void containsAllShouldReturnTrueForSortedCollectionWhenComparedToItself() {
        assertTrue(SORTED_COLLECTION123.containsAll(SORTED_COLLECTION123));
    }

    /**
     * Verifies that containsAll returns true if a sorted collection with duplicate elements is compared to itself. The
     * method is tested through the containsAll method in the SortedArrayCollection class.
     */
    @Test
    public void containsAllShouldReturnTrueForSortedCollectionWithDuplicateElementsWhenComparedToItself() {
        SortedCollection<Integer> sortedCollection112233 =
                new SortedArrayCollection<Integer>(COMPARATOR, 1, 2, THREE, 1, 2, THREE);
        SortedCollection<Integer> sortedCollection111222333 =
                new SortedArrayCollection<Integer>(COMPARATOR, 1, 2, THREE, 1, 2, THREE, 1, 2, THREE);
        assertTrue(sortedCollection112233.containsAll(sortedCollection112233));
        assertTrue(sortedCollection111222333.containsAll(sortedCollection111222333));
        assertFalse(sortedCollection112233.containsAll(sortedCollection111222333));
        assertTrue(sortedCollection111222333.containsAll(sortedCollection112233));
    }

    /**
     * Verifies that containsAll returns false if a sorted collection contains another element. The method is tested
     * through the containsAll method in the SortedArrayCollection class.
     */
    @Test
    public void containsAllShouldReturnFalseWhenComparedToSortedCollectionWithAnotherElement() {
        assertFalse(SORTED_COLLECTION123.containsAll(new SortedArrayCollection<Integer>(COMPARATOR, 0, 1, 2)));
    }

    /**
     * Verifies that containsAll returns false if the other collection contains an element of a different type. The
     * method is tested through the containsAll method in the SortedArrayCollection class.
     */
    @Test
    public void containsAllShouldReturnFalseWhenComparedToCollectionWithElementOfDifferentType() {
        assertFalse(SORTED_COLLECTION123.containsAll(new ArrayCollection<Object>("Foo")));
    }

    /**
     * Verifies that findIndex returns the correct index for the given element. The method is tested through the get
     * method in the SortedArrayMap class.
     */
    @Test
    public void findIndexShouldReturnCorrectIndexForGivenElement() {
        SortedArrayMap<Integer, String> map = new SortedArrayMap<Integer, String>(COMPARATOR, ENTRY1, ENTRY2);
        assertEquals("one", map.get(1));
        assertEquals("two", map.get(2));
    }

    /**
     * Verifies that findIndex returns the correct index for the given element. The method is tested through the
     * contains method in the SortedArrayMap class.
     */
    @Test
    public void findIndexShouldReturnCorrectIndexForGivenElementWithDuplicateKeys() {
        SortedArrayMap<Integer, String> map =
                new SortedArrayMap<Integer, String>(COMPARATOR, ENTRY1, ENTRY1BIS, ENTRY1TER);
        assertTrue(map.contains(ENTRY1));
        assertTrue(map.contains(ENTRY1BIS));
        assertTrue(map.contains(ENTRY1TER));
        assertFalse(map.contains(ENTRY1QUATER));
    }

    /**
     * Verifies that quicksort can handle a large array of one million elements that is already sorted. The method is
     * tested through the constructor of the SortedArrayCollection class.
     */
    @Test
    public void quicksortShouldHandleALargeSortedArray() {
        Integer[] array = new Integer[ONE_MILLION];
        for (int i = 0; i < ONE_MILLION; i++) {
            array[i] = i;
        }
        SortedArrayCollection<Integer> collection = new SortedArrayCollection<Integer>(COMPARATOR, array);
        assertEquals(ONE_MILLION, collection.size());
    }

    /**
     * Verifies that quicksort can handle a large array of one million elements that is reverse-sorted. The method is
     * tested through the constructor of the SortedArrayCollection class.
     */
    @Test
    public void quicksortShouldHandleALargeReverseSortedArray() {
        Integer[] array = new Integer[ONE_MILLION];
        for (int i = 0; i < ONE_MILLION; i++) {
            array[i] = ONE_MILLION - i;
        }
        SortedArrayCollection<Integer> collection = new SortedArrayCollection<Integer>(COMPARATOR, array);
        assertEquals(ONE_MILLION, collection.size());
    }
}
