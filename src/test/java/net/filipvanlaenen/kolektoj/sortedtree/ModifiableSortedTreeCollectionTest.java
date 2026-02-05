package net.filipvanlaenen.kolektoj.sortedtree;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Comparator;
import java.util.Objects;

import org.junit.jupiter.api.Test;

import net.filipvanlaenen.kolektoj.Collection.ElementCardinality;
import net.filipvanlaenen.kolektoj.Collection;
import net.filipvanlaenen.kolektoj.ModifiableCollection;
import net.filipvanlaenen.kolektoj.ModifiableCollectionTestBase;
import net.filipvanlaenen.kolektoj.ModifiableSortedCollection;

/**
 * Unit tests on the {@link net.filipvanlaenen.kolektoj.sortedtree.ModifiableSortedTreeCollection} class.
 */
public final class ModifiableSortedTreeCollectionTest
        extends ModifiableCollectionTestBase<ModifiableSortedTreeCollection<Integer>> {
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
     * Sorted collection with the integers 1, 2 and 3.
     */
    private final ModifiableSortedTreeCollection<Integer> collection123 = createModifiableCollection(1, 2, 3);
    /**
     * Sorted collection with the integers 1 and 3.
     */
    private final ModifiableSortedTreeCollection<Integer> collection13 = createModifiableCollection(1, 3);
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

    @Override
    protected ModifiableSortedTreeCollection<Integer> createModifiableCollection(final Integer... integers) {
        return new ModifiableSortedTreeCollection<Integer>(COMPARATOR, integers);
    }

    @Override
    protected ModifiableSortedTreeCollection<Integer> createModifiableCollection(
            final ElementCardinality elementCardinality, final Integer... integers) {
        return new ModifiableSortedTreeCollection<Integer>(elementCardinality, COMPARATOR, integers);
    }

    @Override
    protected ModifiableSortedTreeCollection<Integer> createModifiableCollection(
            final ElementCardinality elementCardinality, final ModifiableSortedTreeCollection<Integer> integers) {
        return new ModifiableSortedTreeCollection<Integer>(elementCardinality, COMPARATOR, integers);
    }

    /**
     * Creates a new modifiable sorted collection to run the unit tests on.
     *
     * @return A new modifiable sorted collection to run the unit tests on.
     */
    private ModifiableSortedCollection<Integer> createCollection513() {
        return createModifiableCollection(FIVE, 1, THREE);
    }

    /**
     * Verifies that adding element happens according to the comparator.
     */
    @Test
    public void addShouldInsertAnNewElementAccordingToSorting() {
        ModifiableCollection<Integer> collection = createCollection513();
        collection.add(2);
        assertArrayEquals(new Integer[] {1, 2, THREE, FIVE}, collection.toArray());
    }

    /**
     * Verifies that adding elements from a collection happens according to the comparator.
     */
    @Test
    public void addAllShouldInsertAnNewElementsAccordingToSorting() {
        ModifiableCollection<Integer> collection = createCollection513();
        collection.addAll(Collection.of(2, FOUR));
        assertArrayEquals(new Integer[] {1, 2, THREE, FOUR, FIVE}, collection.toArray());
    }

    /**
     * Verifies that firstIndexOf returns -1 for an element not in the collection.
     */
    @Test
    public void firstIndexOfShouldReturnMinusOneForAnElementNotInTheCollection() {
        assertEquals(-1, createCollection513().firstIndexOf(0));
    }

    /**
     * Verifies that firstIndexOf returns the correct index for an element in the collection.
     */
    @Test
    public void firstIndexOfShouldReturnIndexForAnElementInTheCollection() {
        assertEquals(1, createCollection513().firstIndexOf(THREE));
    }

    /**
     * Verifies that getting an element at an index occurs according to sorting.
     */
    @Test
    public void getAtShouldReturnElementAtIndexAfterSorting() {
        assertEquals(THREE, createCollection513().getAt(1));
    }

    /**
     * Verifies that <code>getComparator</code> returns the comparator used to create the sorted collection.
     */
    @Test
    public void getComparatorShouldReturnTheProvidedComparator() {
        assertEquals(COMPARATOR, createCollection513().getComparator());
    }

    /**
     * Verifies that <code>getGreaterThan</code> on an empty collection throws IndexOutOfBoundsException.
     */
    @Test
    public void getGreaterThanShouldThrowExceptionWhenCalledOnAnEmptyCollection() {
        IndexOutOfBoundsException exception =
                assertThrows(IndexOutOfBoundsException.class, () -> createModifiableCollection().getGreaterThan(2));
        assertEquals("Cannot return an element from an empty collection.", exception.getMessage());
    }

    /**
     * Verifies that <code>getGreaterThan</code> returns an element that's greater than the provided element.
     */
    @Test
    public void getGreaterThanShouldReturnTheFirstElementThatIsGreater() {
        assertEquals(THREE, collection123.getGreaterThan(2));
    }

    /**
     * Verifies that <code>getGreaterThan</code> throws IndexOutOfBoundsException when there's no greater element.
     */
    @Test
    public void getGreaterThanShouldThrowExceptionWhenCalledWithGreatestElement() {
        IndexOutOfBoundsException exception =
                assertThrows(IndexOutOfBoundsException.class, () -> collection123.getGreaterThan(THREE));
        assertEquals("Cannot return an element from the collection that's greater than the provided value.",
                exception.getMessage());
    }

    /**
     * Verifies that <code>getGreaterThanOrEqualTo</code> on an empty collection throws IndexOutOfBoundsException.
     */
    @Test
    public void getGreaterThanOrEqualToShouldThrowExceptionWhenCalledOnAnEmptyCollection() {
        IndexOutOfBoundsException exception = assertThrows(IndexOutOfBoundsException.class,
                () -> createModifiableCollection().getGreaterThanOrEqualTo(2));
        assertEquals("Cannot return an element from an empty collection.", exception.getMessage());
    }

    /**
     * Verifies that <code>getGreaterThanOrEqualTo</code> returns an element that's equal to the provided element if
     * it's present.
     */
    @Test
    public void getGreaterThanOrEqualToShouldReturnTheElementThatIsEqualIfPresent() {
        assertEquals(2, collection123.getGreaterThanOrEqualTo(2));
    }

    /**
     * Verifies that <code>getGreaterThanOrEqualTo</code> returns an element that's greater than if the provided element
     * is absent.
     */
    @Test
    public void getGreaterThanOrEqualToShouldReturnGreaterElementIfProvidedElementIsAbsent() {
        assertEquals(THREE, collection13.getGreaterThanOrEqualTo(2));
    }

    /**
     * Verifies that <code>getGreaterThanOrEqualTo</code> throws IndexOutOfBoundsException when there's no greater or
     * equal element.
     */
    @Test
    public void getGreaterThanOrEqualToShouldThrowExceptionWhenCalledWithGreaterThanGreatestElement() {
        IndexOutOfBoundsException exception =
                assertThrows(IndexOutOfBoundsException.class, () -> collection123.getGreaterThanOrEqualTo(FOUR));
        assertEquals("Cannot return an element from the collection that's greater than or equal to the provided value.",
                exception.getMessage());
    }

    /**
     * Verifies that <code>getLessThan</code> on an empty collection throws IndexOutOfBoundsException.
     */
    @Test
    public void getLessThanShouldThrowExceptionWhenCalledOnAnEmptyCollection() {
        IndexOutOfBoundsException exception =
                assertThrows(IndexOutOfBoundsException.class, () -> createModifiableCollection().getLessThan(2));
        assertEquals("Cannot return an element from an empty collection.", exception.getMessage());
    }

    /**
     * Verifies that <code>getLessThan</code> returns an element that's less than the provided element.
     */
    @Test
    public void getLessThanShouldReturnTheLastElementThatIsLess() {
        assertEquals(1, collection123.getLessThan(2));
    }

    /**
     * Verifies that <code>getLessThan</code> throws IndexOutOfBoundsException when there's no less element.
     */
    @Test
    public void getLessThanShouldThrowExceptionWhenCalledWithLeastElement() {
        IndexOutOfBoundsException exception =
                assertThrows(IndexOutOfBoundsException.class, () -> collection123.getLessThan(1));
        assertEquals("Cannot return an element from the collection that's less than the provided value.",
                exception.getMessage());
    }

    /**
     * Verifies that <code>getLessThanOrEqualTo</code> on an empty collection throws IndexOutOfBoundsException.
     */
    @Test
    public void getLessThanOrEqualToShouldThrowExceptionWhenCalledOnAnEmptyCollection() {
        IndexOutOfBoundsException exception = assertThrows(IndexOutOfBoundsException.class,
                () -> createModifiableCollection().getLessThanOrEqualTo(2));
        assertEquals("Cannot return an element from an empty collection.", exception.getMessage());
    }

    /**
     * Verifies that <code>getLessThanOrEqualTo</code> returns an element that's equal to the provided element if it's
     * present.
     */
    @Test
    public void getLessThanOrEqualToShouldReturnTheElementThatIsEqualIfPresent() {
        assertEquals(2, collection123.getLessThanOrEqualTo(2));
    }

    /**
     * Verifies that <code>getLessThanOrEqualTo</code> returns an element that's less if the provided element is absent.
     */
    @Test
    public void getLessThanOrEqualToShouldReturnLessElementIfProvidedElementIsAbsent() {
        assertEquals(1, collection13.getLessThanOrEqualTo(2));
    }

    /**
     * Verifies that <code>getLessThanOrEqualTo</code> throws IndexOutOfBoundsException when there's no less or equal
     * element.
     */
    @Test
    public void getLessThanOrEqualToShouldThrowExceptionWhenCalledWithGreaterThanGreatestElement() {
        IndexOutOfBoundsException exception =
                assertThrows(IndexOutOfBoundsException.class, () -> collection123.getLessThanOrEqualTo(0));
        assertEquals("Cannot return an element from the collection that's less than or equal to the provided value.",
                exception.getMessage());
    }

    /**
     * Verifies that indexOf returns -1 for an element not in the collection.
     */
    @Test
    public void indexOfShouldReturnMinusOneForAnElementNotInTheCollection() {
        assertEquals(-1, createCollection513().indexOf(0));
    }

    /**
     * Verifies that indexOf returns the correct index for an element in the collection.
     */
    @Test
    public void indexOfShouldReturnIndexForAnElementInTheCollection() {
        assertEquals(1, createCollection513().indexOf(THREE));
    }

    /**
     * Verifies that lastIndexOf returns -1 for an element not in the collection.
     */
    @Test
    public void lastIndexOfShouldReturnMinusOneForAnElementNotInTheCollection() {
        assertEquals(-1, createCollection513().lastIndexOf(0));
    }

    /**
     * Verifies that lastIndexOf returns the correct index for an element in the collection.
     */
    @Test
    public void lastIndexOfShouldReturnIndexForAnElementInTheCollection() {
        assertEquals(1, createCollection513().lastIndexOf(THREE));
    }

    /**
     * Verifies that removing an element at an index occurs according to sorting.
     */
    @Test
    public void removeAtShouldReturnElementAtIndexAfterSorting() {
        assertEquals(THREE, createCollection513().removeAt(1));
    }

    /**
     * Verifies that removing an element at an index occurs according to sorting.
     */
    @Test
    public void removeAtShouldRemoveAtIndexAfterSorting() {
        ModifiableSortedCollection<Integer> collection = createCollection513();
        collection.removeAt(1);
        assertArrayEquals(new Integer[] {1, FIVE}, collection.toArray());
    }

    /**
     * Verifies that trying to remove an element at an index beyond the size of the collection throws
     * IndexOutOfBoundsException.
     */
    @Test
    public void removeAtShouldThrowExceptionWhenCalledBeyondCollectionSize() {
        IndexOutOfBoundsException exception =
                assertThrows(IndexOutOfBoundsException.class, () -> createCollection513().removeAt(THREE));
        assertEquals("Cannot remove an element at a position beyond the size of the collection.",
                exception.getMessage());
    }
}
