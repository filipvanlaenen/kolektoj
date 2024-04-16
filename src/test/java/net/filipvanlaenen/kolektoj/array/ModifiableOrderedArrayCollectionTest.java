package net.filipvanlaenen.kolektoj.array;

import static net.filipvanlaenen.kolektoj.Collection.ElementCardinality.DISTINCT_ELEMENTS;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Spliterator;

import org.junit.jupiter.api.Test;

import net.filipvanlaenen.kolektoj.Collection;
import net.filipvanlaenen.kolektoj.Collection.ElementCardinality;
import net.filipvanlaenen.kolektoj.ModifiableCollectionTestBase;
import net.filipvanlaenen.kolektoj.ModifiableOrderedCollection;
import net.filipvanlaenen.kolektoj.OrderedCollection;

/**
 * Unit tests on the {@link net.filipvanlaenen.kolektoj.array.ModifiableOrderedArrayCollection} class.
 */
public final class ModifiableOrderedArrayCollectionTest
        extends ModifiableCollectionTestBase<ModifiableOrderedArrayCollection<Integer>> {
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
     * The magic number six.
     */
    private static final int SIX = 6;
    /**
     * Collection with the integers 1, 2 and 3.
     */
    private static final ModifiableOrderedCollection<Integer> COLLECTION123 = createNewCollection();
    /**
     * Collection with the integers 1, 2, 3 and null.
     */
    private static final ModifiableOrderedCollection<Integer> COLLECTION123NULL =
            new ModifiableOrderedArrayCollection<Integer>(1, 2, 3, null);

    @Override
    protected ModifiableOrderedArrayCollection<Integer> createModifiableCollection(final Integer... integers) {
        return new ModifiableOrderedArrayCollection<Integer>(integers);
    }

    @Override
    protected ModifiableOrderedArrayCollection<Integer> createModifiableCollection(
            final ElementCardinality elementCardinality, final Integer... integers) {
        return new ModifiableOrderedArrayCollection<Integer>(elementCardinality, integers);
    }

    /**
     * Creates a new collection to run the unit tests on.
     *
     * @return A new collection to run the unit tests on.
     */
    private static ModifiableOrderedArrayCollection<Integer> createNewCollection() {
        return new ModifiableOrderedArrayCollection<Integer>(1, 2, THREE);
    }

    /**
     * Verifies that when you get an element from a collection at an index, the correct element is returned.
     */
    @Test
    public void getAtShouldReturnTheElementAtTheIndexInTheCollection() {
        assertEquals(2, COLLECTION123.getAt(1));
    }

    /**
     * Verifies that trying to get an element at an index beyond the size of the collection throws
     * IndexOutOfBoundsException.
     */
    @Test
    public void getAtShouldThrowExceptionWhenCalledBeyondCollectionSize() {
        IndexOutOfBoundsException exception =
                assertThrows(IndexOutOfBoundsException.class, () -> COLLECTION123.getAt(THREE));
        assertEquals("Cannot return an element at a position beyond the size of the collection.",
                exception.getMessage());
    }

    /**
     * Verifies that the collection produces an array with the elements in the correct order.
     */
    @Test
    public void toArrayShouldProduceAnArrayWithTheElementsOfTheCollectionInTheCorrectOrder() {
        ModifiableOrderedCollection<Integer> collection = new ModifiableOrderedArrayCollection<Integer>(1, 2);
        Object[] actual = collection.toArray();
        Object[] expected = new Object[] {1, 2};
        assertArrayEquals(expected, actual);
    }

    /**
     * Verifies that trying to add an element at an index beyond the size of the collection throws
     * IndexOutOfBoundsException.
     */
    @Test
    public void addAtShouldThrowExceptionWhenCalledBeyondCollectionSize() {
        IndexOutOfBoundsException exception =
                assertThrows(IndexOutOfBoundsException.class, () -> COLLECTION123.addAt(SIX, 0));
        assertEquals("Cannot add an element at a position beyond the size of the collection.", exception.getMessage());
    }

    /**
     * Verifies that adding an element at position 0 to an empty collection returns true.
     */
    @Test
    public void addAtZeroOnAnEmptyCollectionShouldReturnTrue() {
        assertTrue(new ModifiableOrderedArrayCollection<Integer>().addAt(0, 1));
    }

    /**
     * Verifies that after adding an element at position 0 to an empty collection, the element is at position 0.
     */
    @Test
    public void addAtZeroOnAnEmptyCollectionShouldPutAnElementAtPositionZero() {
        ModifiableOrderedCollection<Integer> collection = new ModifiableOrderedArrayCollection<Integer>();
        collection.addAt(0, 1);
        assertEquals(1, collection.getAt(0));
    }

    /**
     * Verifies that after adding an element at a position to a collection, the elements after the position have been
     * moved up.
     */
    @Test
    public void addAtOneOnACollectionShouldMoveElementsOnePositionHigher() {
        ModifiableOrderedCollection<Integer> collection = createNewCollection();
        collection.addAt(1, 0);
        assertEquals(2, collection.getAt(2));
    }

    /**
     * Verifies that after adding an element at the last position to a collection, the new element is in the last
     * position.
     */
    @Test
    public void addAtLastPositionOnACollectionShouldPlaceElementAtLastPosition() {
        ModifiableOrderedCollection<Integer> collection = createNewCollection();
        collection.addAt(THREE, 0);
        assertEquals(0, collection.getAt(THREE));
    }

    /**
     * Verifies that adding beyond the stride doesn't lead to an exception.
     */
    @Test
    public void addAtManyTimesShouldNotProduceAnException() {
        ModifiableOrderedCollection<Integer> collection = new ModifiableOrderedArrayCollection<Integer>();
        for (int i = 0; i < SIX; i++) {
            collection.addAt(0, i);
        }
        assertEquals(0, collection.getAt(SIX - 1));
    }

    /**
     * Verifies that after adding an element at position 0 to an empty collection, the collection has size 1.
     */
    @Test
    public void addAtZeroOnAnEmptyCollectionShouldIncreaseCollectionSizeToOne() {
        ModifiableOrderedCollection<Integer> collection = new ModifiableOrderedArrayCollection<Integer>();
        collection.addAt(0, 1);
        assertEquals(1, collection.size());
    }

    /**
     * Verifies that adding a duplicate element at zero to a collection with distinct elements returns false.
     */
    @Test
    public void addAtZeroDuplicateElementOnCollectionWithDistinctElementsShouldReturnFalse() {
        ModifiableOrderedCollection<Integer> collection =
                new ModifiableOrderedArrayCollection<Integer>(DISTINCT_ELEMENTS, 1);
        assertFalse(collection.addAt(0, 1));
    }

    /**
     * Verifies that adding a new element at zero to a collection with distinct elements returns true.
     */
    @Test
    public void addAtZeroNewElementOnCollectionWithDistinctElementsShouldReturnTrue() {
        ModifiableOrderedCollection<Integer> collection =
                new ModifiableOrderedArrayCollection<Integer>(DISTINCT_ELEMENTS, 1);
        assertTrue(collection.addAt(1, 2));
    }

    /**
     * Verifies that adding the elements of a collection to an empty collection at the first position returns true.
     */
    @Test
    public void addAllAtOnAnEmptyCollectionShouldReturnTrue() {
        assertTrue(new ModifiableOrderedArrayCollection<Integer>().addAllAt(0, Collection.of(1, 2)));
    }

    /**
     * Verifies that adding an empty collection at zero returns false.
     */
    @Test
    public void addAllAtWithEmptyCollectionShouldReturnFalse() {
        assertFalse(new ModifiableOrderedArrayCollection<Integer>().addAllAt(0,
                new ModifiableOrderedArrayCollection<Integer>()));
    }

    /**
     * Verifies that adding duplicate elements at zero to a collection with distinct elements returns false.
     */
    @Test
    public void addAllAtOfDuplicateElementsToCollectionWithDistinctElementsShouldReturnFalse() {
        ModifiableOrderedCollection<Integer> collection =
                new ModifiableOrderedArrayCollection<Integer>(DISTINCT_ELEMENTS, 1, 2, THREE);
        assertFalse(collection.addAllAt(0, COLLECTION123));
    }

    /**
     * Verifies that adding at zero a collection with duplicate and new elements to a collection with distinct elements
     * returns true.
     */
    @Test
    public void addAllAtOfNewAndDuplicateElementsToCollectionWithDistinctElementsShouldReturnTrue() {
        ModifiableOrderedCollection<Integer> collection =
                new ModifiableOrderedArrayCollection<Integer>(DISTINCT_ELEMENTS, 1, 2, THREE);
        assertTrue(collection.addAllAt(0, COLLECTION123NULL));
    }

    /**
     * Verifies that adding a collection at zero with duplicate and new elements to a collection with distinct elements
     * increases the size correctly.
     */
    @Test
    public void addAllAtOfNewAndDuplicateElementsToCollectionWithDistinctElementsShouldIncreaseSizeCorrectly() {
        ModifiableOrderedCollection<Integer> collection =
                new ModifiableOrderedArrayCollection<Integer>(DISTINCT_ELEMENTS, 1, 2, THREE);
        collection.addAllAt(0, new OrderedArrayCollection<Integer>(FOUR, FOUR, 1, 2, THREE, FIVE));
        assertArrayEquals(new Integer[] {FOUR, FIVE, 1, 2, THREE}, collection.toArray());
    }

    /**
     * Verifies that trying to add a collectiont at an index beyond the size of the collection throws
     * IndexOutOfBoundsException.
     */
    @Test
    public void addAllAtShouldThrowExceptionWhenCalledBeyondCollectionSize() {
        IndexOutOfBoundsException exception = assertThrows(IndexOutOfBoundsException.class,
                () -> new ModifiableOrderedArrayCollection<Integer>().addAllAt(1, Collection.of(1, 2)));
        assertEquals("Cannot add the elements of another collection at a position beyond the size of the collection.",
                exception.getMessage());
    }

    /**
     * Verifies that after adding the elements of a collection to an empty collection at the first position, the size is
     * increased to by the size of the added collection.
     */
    @Test
    public void sizeShouldBeThreeAfterAddingCollectionWithThreeElementsToAnEmptyCollectionAtFirstPosition() {
        ModifiableOrderedCollection<Integer> collection = createNewCollection();
        collection.addAllAt(THREE, createNewCollection());
        assertEquals(SIX, collection.size());
    }

    /**
     * Verifies that after adding the elements of a collection to a collection at a position, the collection contains
     * the elements added.
     */
    @Test
    public void collectionShouldContainElementsAfterHavingItAddedAllAtAPosition() {
        ModifiableOrderedCollection<Integer> collection = createNewCollection();
        collection.addAllAt(1, Collection.of(0, SIX));
        assertTrue(collection.contains(0));
        assertTrue(collection.contains(SIX));
    }

    /**
     * Verifies that after adding the elements of an ordered collection to a collection, the collection contains the
     * elements added in the same order.
     */
    @Test
    public void collectionShouldContainElementsInSameOrderedAfterHavingItAddedAll() {
        ModifiableOrderedCollection<Integer> collection = createNewCollection();
        collection.addAll(OrderedCollection.of(0, SIX));
        assertEquals(1, collection.getAt(0));
        assertEquals(0, collection.getAt(THREE));
        assertEquals(SIX, collection.getAt(FOUR));
    }

    /**
     * Verifies that after adding the elements of an ordered collection to a collection at a position, the collection
     * contains the elements added in the same order.
     */
    @Test
    public void collectionShouldContainElementsInSameOrderedAfterHavingItAddedAllAtAPosition() {
        ModifiableOrderedCollection<Integer> collection = createNewCollection();
        collection.addAllAt(1, OrderedCollection.of(0, SIX));
        assertEquals(1, collection.getAt(0));
        assertEquals(0, collection.getAt(1));
        assertEquals(SIX, collection.getAt(2));
        assertEquals(2, collection.getAt(THREE));
    }

    /**
     * Verifies that removeAll can remove the first element.
     */
    @Test
    public void removeAllShouldRemoveFirstElement() {
        ModifiableOrderedCollection<Integer> collection = new ModifiableOrderedArrayCollection<Integer>(0, 1, 2);
        collection.removeAll(new ArrayCollection<Integer>(0));
        assertArrayEquals(new Integer[] {1, 2}, collection.toArray());
    }

    /**
     * Verifies that removeAll can remove the last element.
     */
    @Test
    public void removeAllShouldRemoveLastElement() {
        ModifiableOrderedCollection<Integer> collection = new ModifiableOrderedArrayCollection<Integer>(0, 1, 2);
        collection.removeAll(new ArrayCollection<Integer>(2));
        assertArrayEquals(new Integer[] {0, 1}, collection.toArray());
    }

    /**
     * Verifies that trying to remove an element at an index beyond the size of the collection throws
     * IndexOutOfBoundsException.
     */
    @Test
    public void removeAtShouldThrowExceptionWhenCalledBeyondCollectionSize() {
        IndexOutOfBoundsException exception =
                assertThrows(IndexOutOfBoundsException.class, () -> COLLECTION123.removeAt(THREE));
        assertEquals("Cannot remove an element at a position beyond the size of the collection.",
                exception.getMessage());
    }

    /**
     * Verifies that removeAt returns the element being removed.
     */
    @Test
    public void removeAtShouldReturnTheElementThatIsRemoved() {
        assertEquals(2, createNewCollection().removeAt(1));
    }

    /**
     * Verifies that the spliterator has the ordered flag set.
     */
    @Test
    public void spliteratorShouldSetOrderedFlag() {
        assertTrue(COLLECTION123.spliterator().hasCharacteristics(Spliterator.ORDERED));
    }
}
