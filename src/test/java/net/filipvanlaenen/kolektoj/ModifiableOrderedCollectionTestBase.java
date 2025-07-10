package net.filipvanlaenen.kolektoj;

import static net.filipvanlaenen.kolektoj.Collection.ElementCardinality.DISTINCT_ELEMENTS;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Spliterator;

import org.junit.jupiter.api.Test;

import net.filipvanlaenen.kolektoj.Collection.ElementCardinality;

/**
 * Unit tests on implementations of the {@link net.filipvanlaenen.kolektoj.ModifiableOrderedCollection} interface.
 *
 * @param <T> The subclass type to be tested.
 */
public abstract class ModifiableOrderedCollectionTestBase<T extends ModifiableOrderedCollection<Integer>>
        extends ModifiableCollectionTestBase<T> {
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
     * Ordered collection with the integers 1, 2 and 3.
     */
    private final T collection123 = createModifiableOrderedCollection(1, 2, 3);
    /**
     * Collection with the integers 1, 2, 3 and null.
     */
    private final T collection123null = createModifiableOrderedCollection(1, 2, 3, null);

    /**
     * Creates a modifiable ordered collection containing the provided integers with a given element cardinality.
     *
     * @param elementCardinality The element cardinality for the modifiable ordered collection.
     * @param integers           The integers to be included in the modifiable ordered collection.
     * @return A modifiable ordered collection containing the provided integers.
     */
    protected abstract T createModifiableOrderedCollection(ElementCardinality elementCardinality, Integer... integers);

    /**
     * Creates a modifiable ordered collection containing the provided integers.
     *
     * @param integers The integers to be included in the modifiable ordered collection.
     * @return A modifiable ordered collection containing the provided integers.
     */
    protected abstract T createModifiableOrderedCollection(Integer... integers);

    /**
     * Verifies that adding the elements of a collection to an empty collection at the first position returns true.
     */
    @Test
    public void addAllAtOnAnEmptyCollectionShouldReturnTrue() {
        assertTrue(createModifiableOrderedCollection().addAllAt(0, OrderedCollection.of(1, 2)));
    }

    /**
     * Verifies that adding an empty collection at zero returns false.
     */
    @Test
    public void addAllAtWithEmptyCollectionShouldReturnFalse() {
        assertFalse(createModifiableOrderedCollection().addAllAt(0, OrderedCollection.empty()));
    }

    /**
     * Verifies that adding duplicate elements at zero to a collection with distinct elements returns false.
     */
    @Test
    public void addAllAtOfDuplicateElementsToCollectionWithDistinctElementsShouldReturnFalse() {
        T collection = createModifiableOrderedCollection(DISTINCT_ELEMENTS, 1, 2, THREE);
        assertFalse(collection.addAllAt(0, collection123));
    }

    /**
     * Verifies that adding at zero a collection with duplicate and new elements to a collection with distinct elements
     * returns true.
     */
    @Test
    public void addAllAtOfNewAndDuplicateElementsToCollectionWithDistinctElementsShouldReturnTrue() {
        T collection = createModifiableOrderedCollection(DISTINCT_ELEMENTS, 1, 2, THREE);
        assertTrue(collection.addAllAt(0, collection123null));
    }

    /**
     * Verifies that adding a collection at zero with duplicate and new elements to a collection with distinct elements
     * is done correctly.
     */
    @Test
    public void addAllAtOfNewAndDuplicateElementsToCollectionWithDistinctElementsShouldBeDoneCorrectly() {
        T collection = createModifiableOrderedCollection(DISTINCT_ELEMENTS, 1, 2, THREE);
        collection.addAllAt(0, OrderedCollection.of(FOUR, FOUR, 1, 2, THREE, FIVE));
        assertArrayEquals(new Integer[] {FOUR, FIVE, 1, 2, THREE}, collection.toArray());
    }

    /**
     * Verifies that adding a collection in the middle is done correctly.
     */
    @Test
    public void addAllAtInTheMiddleShouldBeDoneCorrectly() {
        T collection = createModifiableOrderedCollection(1, 2, THREE, FOUR);
        collection.addAllAt(2, OrderedCollection.of(FIVE, SIX));
        assertArrayEquals(new Integer[] {1, 2, FIVE, SIX, THREE, FOUR}, collection.toArray());
    }

    /**
     * Verifies that trying to add a collectiont at an index beyond the size of the collection throws
     * IndexOutOfBoundsException.
     */
    @Test
    public void addAllAtShouldThrowExceptionWhenCalledBeyondCollectionSize() {
        IndexOutOfBoundsException exception = assertThrows(IndexOutOfBoundsException.class,
                () -> createModifiableOrderedCollection().addAllAt(1, OrderedCollection.of(1, 2)));
        assertEquals("Cannot add the elements of another collection at a position beyond the size of the collection.",
                exception.getMessage());
    }

    /**
     * Verifies that trying to add an element at an index beyond the size of the collection throws
     * IndexOutOfBoundsException.
     */
    @Test
    public void addAtShouldThrowExceptionWhenCalledBeyondCollectionSize() {
        IndexOutOfBoundsException exception =
                assertThrows(IndexOutOfBoundsException.class, () -> collection123.addAt(SIX, 0));
        assertEquals("Cannot add an element at a position beyond the size of the collection.", exception.getMessage());
    }

    /**
     * Verifies that adding an element at position 0 to an empty collection returns true.
     */
    @Test
    public void addAtZeroOnAnEmptyCollectionShouldReturnTrue() {
        assertTrue(createModifiableOrderedCollection().addAt(0, 1));
    }

    /**
     * Verifies that after adding an element at position 0 to an empty collection, the element is at position 0.
     */
    @Test
    public void addAtZeroOnAnEmptyCollectionShouldPutAnElementAtPositionZero() {
        T collection = createModifiableOrderedCollection();
        collection.addAt(0, 1);
        assertEquals(1, collection.getAt(0));
    }

    /**
     * Verifies that after adding an element at a position to a collection, the elements after the position have been
     * moved up.
     */
    @Test
    public void addAtOneOnACollectionShouldMoveElementsOnePositionHigher() {
        T collection = createModifiableOrderedCollection(1, 2, THREE);
        collection.addAt(1, 0);
        assertEquals(2, collection.getAt(2));
    }

    /**
     * Verifies that adding in the middle is done correctly.
     */
    @Test
    public void addAtInTheMiddleShouldBeDoneCorrectly() {
        T collection = createModifiableOrderedCollection(1, 2, THREE, FOUR);
        collection.addAt(2, FIVE);
        assertArrayEquals(new Integer[] {1, 2, FIVE, THREE, FOUR}, collection.toArray());
    }

    /**
     * Verifies that after adding an element at the last position to a collection, the new element is in the last
     * position.
     */
    @Test
    public void addAtLastPositionOnACollectionShouldPlaceElementAtLastPosition() {
        T collection = createModifiableOrderedCollection(1, 2, THREE);
        collection.addAt(THREE, 0);
        assertEquals(0, collection.getAt(THREE));
    }

    /**
     * Verifies that adding beyond the stride doesn't lead to an exception.
     */
    @Test
    public void addAtManyTimesShouldNotProduceAnException() {
        T collection = createModifiableOrderedCollection();
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
        T collection = createModifiableOrderedCollection();
        collection.addAt(0, 1);
        assertEquals(1, collection.size());
    }

    /**
     * Verifies that adding a duplicate element at zero to a collection with distinct elements returns false.
     */
    @Test
    public void addAtZeroDuplicateElementOnCollectionWithDistinctElementsShouldReturnFalse() {
        T collection = createModifiableOrderedCollection(DISTINCT_ELEMENTS, 1);
        assertFalse(collection.addAt(0, 1));
    }

    /**
     * Verifies that adding a new element at zero to a collection with distinct elements returns true.
     */
    @Test
    public void addAtZeroNewElementOnCollectionWithDistinctElementsShouldReturnTrue() {
        T collection = createModifiableOrderedCollection(DISTINCT_ELEMENTS, 1);
        assertTrue(collection.addAt(1, 2));
    }

    /**
     * Verifies that after adding the elements of a collection to a collection at a position, the collection contains
     * the elements added.
     */
    @Test
    public void collectionShouldContainElementsAfterHavingItAddedAllAtAPosition() {
        T collection = createModifiableOrderedCollection(1, 2, THREE);
        collection.addAllAt(1, OrderedCollection.of(0, SIX));
        assertTrue(collection.contains(0));
        assertTrue(collection.contains(SIX));
    }

    /**
     * Verifies that after adding the elements of an ordered collection to a collection, the collection contains the
     * elements added in the same order.
     */
    @Test
    public void collectionShouldContainElementsInSameOrderedAfterHavingItAddedAll() {
        T collection = createModifiableOrderedCollection(1, 2, THREE);
        collection.addAllLast(OrderedCollection.of(0, SIX));
        Object[] actual = collection.toArray();
        Object[] expected = new Object[] {1, 2, THREE, 0, SIX};
        assertArrayEquals(expected, actual);
    }

    /**
     * Verifies that after adding the elements of an ordered collection to a collection at a position, the collection
     * contains the elements added in the same order.
     */
    @Test
    public void collectionShouldContainElementsInSameOrderedAfterHavingItAddedAllAtAPosition() {
        T collection = createModifiableOrderedCollection(1, 2, THREE);
        collection.addAllAt(1, OrderedCollection.of(0, SIX));
        assertEquals(1, collection.getAt(0));
        assertEquals(0, collection.getAt(1));
        assertEquals(SIX, collection.getAt(2));
        assertEquals(2, collection.getAt(THREE));
    }

    /**
     * Verifies that when you get an element from a collection by its index, the correct element is returned.
     */
    @Test
    public void getAtShouldReturnCorrectElement() {
        assertEquals(2, collection123.getAt(1));
    }

    /**
     * Verifies that <code>getAt</code> returns an element after two elements have been added to an empty collection.
     *
     * Note: this unit test was written to verify that the pointer to the tail is properly set by the add method in
     * ModifiableOrderedLinkedListCollection.
     */
    @Test
    public void getAtShouldReturnCorrectElementInTheMiddleAfterAddingAnotherOne() {
        T collection = createModifiableOrderedCollection();
        collection.add(1);
        collection.add(2);
        assertNotNull(collection.getAt(1));
    }

    /**
     * Verifies that when you try to get an element from a collection beyond the length, getAt throws
     * IndexOutOfBoundsException.
     */
    @Test
    public void getAtShouldThrowIndexOutOfBoundsExceptionWhenCalledBeyondSize() {
        IndexOutOfBoundsException exception =
                assertThrows(IndexOutOfBoundsException.class, () -> collection123.getAt(THREE));
        assertEquals("Cannot return an element at a position beyond the size of the collection.",
                exception.getMessage());
    }

    /**
     * Verifies that trying to remove an element in the middle of the collection returns true.
     */
    @Test
    public void removeShouldReturnTrueForAnElementInTheMiddleOfACollection() {
        assertTrue(createModifiableOrderedCollection(1, 2, THREE).remove(2));
    }

    /**
     * Verifies that removeAll can remove the first element.
     */
    @Test
    public void removeAllShouldRemoveFirstElement() {
        T collection = createModifiableOrderedCollection(1, 2, THREE);
        collection.removeAll(Collection.of(1));
        assertArrayEquals(new Integer[] {2, THREE}, collection.toArray());
    }

    /**
     * Verifies that removeAll can remove the last element.
     */
    @Test
    public void removeAllShouldRemoveLastElement() {
        T collection = createModifiableOrderedCollection(1, 2, THREE);
        collection.removeAll(Collection.of(THREE));
        assertArrayEquals(new Integer[] {1, 2}, collection.toArray());
    }

    /**
     * Verifies that trying to remove an element at an index beyond the size of the collection throws
     * IndexOutOfBoundsException.
     */
    @Test
    public void removeAtShouldThrowExceptionWhenCalledBeyondCollectionSize() {
        IndexOutOfBoundsException exception =
                assertThrows(IndexOutOfBoundsException.class, () -> collection123.removeAt(THREE));
        assertEquals("Cannot remove an element at a position beyond the size of the collection.",
                exception.getMessage());
    }

    /**
     * Verifies that removeAt returns the element being removed.
     */
    @Test
    public void removeAtShouldReturnTheElementThatIsRemoved() {
        assertEquals(2, createModifiableOrderedCollection(1, 2, THREE).removeAt(1));
    }

    /**
     * Verifies that after adding the elements of a collection to an empty collection at the first position, the size is
     * increased to by the size of the added collection.
     */
    @Test
    public void sizeShouldBeThreeAfterAddingCollectionWithThreeElementsToAnEmptyCollectionAtFirstPosition() {
        T collection = createModifiableOrderedCollection(1, 2, THREE);
        collection.addAllAt(THREE, createModifiableOrderedCollection(1, 2, THREE));
        assertEquals(SIX, collection.size());
    }

    /**
     * Verifies that the spliterator has the ordered flag set.
     */
    @Test
    public void spliteratorShouldSetOrderedFlag() {
        assertTrue(collection123.spliterator().hasCharacteristics(Spliterator.ORDERED));
    }

    /**
     * Verifies that the collection produces an array with the elements in the correct order.
     */
    @Test
    public void toArrayShouldProduceAnArrayWithTheElementsOfTheCollectionInTheCorrectOrder() {
        T collection = createModifiableOrderedCollection(1, 2);
        Object[] actual = collection.toArray();
        Object[] expected = new Object[] {1, 2};
        assertArrayEquals(expected, actual);
    }
}
