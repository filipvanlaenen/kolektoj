package net.filipvanlaenen.kolektoj.array;

import static net.filipvanlaenen.kolektoj.Collection.ElementCardinality.DISTINCT_ELEMENTS;
import static net.filipvanlaenen.kolektoj.Collection.ElementCardinality.DUPLICATE_ELEMENTS;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Comparator;
import java.util.Spliterator;

import org.junit.jupiter.api.Test;

import net.filipvanlaenen.kolektoj.Collection;
import net.filipvanlaenen.kolektoj.ModifiableOrderedCollection;
import net.filipvanlaenen.kolektoj.OrderedCollection;

/**
 * Unit tests on the {@link net.filipvanlaenen.kolektoj.array.ModifiableOrderedArrayCollection} class.
 */
public class ModifiableOrderedArrayCollectionTest {
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
     * An array containing the numbers from 1 to 6.
     */
    private static final Integer[] ARRAY123456 = new Integer[] {1, 2, THREE, FOUR, FIVE, SIX};
    /**
     * Collection with the integers 1 and 2.
     */
    private static final ModifiableOrderedCollection<Integer> COLLECTION12 =
            new ModifiableOrderedArrayCollection<Integer>(1, 2);
    /**
     * Collection with the integers 1, 2 and 3.
     */
    private static final ModifiableOrderedCollection<Integer> COLLECTION123 = createNewCollection();
    /**
     * Collection with the integers 1, 2, 3 and null.
     */
    private static final ModifiableOrderedCollection<Integer> COLLECTION123NULL =
            new ModifiableOrderedArrayCollection<Integer>(1, 2, 3, null);

    /**
     * Creates a new collection to run the unit tests on.
     *
     * @return A new collection to run the unit tests on.
     */
    private static ModifiableOrderedArrayCollection<Integer> createNewCollection() {
        return new ModifiableOrderedArrayCollection<Integer>(1, 2, THREE);
    }

    /**
     * Verifies that the correct length is returned for a collection with three elements.
     */
    @Test
    public void sizeShouldReturnThreeForACollectionOfThreeElements() {
        assertEquals(THREE, COLLECTION123.size());
    }

    /**
     * Verifies that contains returns true for an element in the collection.
     */
    @Test
    public void containsShouldReturnTrueForAnElementInTheCollection() {
        assertTrue(COLLECTION123.contains(1));
    }

    /**
     * Verifies that contains returns true for null if it's in the collection.
     */
    @Test
    public void containsShouldReturnTrueForNullIfInTheCollection() {
        assertTrue(new ModifiableOrderedArrayCollection<Integer>(1, 2, THREE, null).contains(null));
    }

    /**
     * Verifies that contains returns false for an element not in the collection.
     */
    @Test
    public void containsShouldReturnFalseForAnElementNotInTheCollection() {
        assertFalse(COLLECTION123.contains(0));
    }

    /**
     * Verifies that contains returns false for null if it isn't in the collection.
     */
    @Test
    public void containsShouldReturnFalseForNullIfNotInTheTheCollection() {
        assertFalse(COLLECTION123.contains(null));
    }

    /**
     * Verifies that containsAll returns false is the other collection is larger.
     */
    @Test
    public void containsAllShouldReturnFalseIfTheOtherCollectionIsLarger() {
        assertFalse(COLLECTION123.containsAll(COLLECTION123NULL));
    }

    /**
     * Verifies that containsAll returns true if a collection is compared to itself.
     */
    @Test
    public void containsAllShouldReturnTrueWhenComparedToItself() {
        assertTrue(COLLECTION123.containsAll(COLLECTION123));
    }

    /**
     * Verifies that when you get an element from a collection, the collection contains it.
     */
    @Test
    public void getShouldReturnAnElementPresentInTheCollection() {
        Integer element = COLLECTION123.get();
        assertTrue(COLLECTION123.contains(element));
    }

    /**
     * Verifies that trying to get an element from an empty collection throws IndexOutOfBoundsException.
     */
    @Test
    public void getShouldThrowExceptionWhenCalledOnAnEmptyCollection() {
        IndexOutOfBoundsException exception = assertThrows(IndexOutOfBoundsException.class,
                () -> new ModifiableOrderedArrayCollection<Integer>().get());
        assertEquals("Cannot return an element from an empty collection.", exception.getMessage());
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
     * Verifies that the collection produces an array with the elements.
     */
    @Test
    public void toArrayShouldProduceAnArrayWithTheElementsOfTheCollection() {
        ModifiableOrderedCollection<Integer> collection = new ModifiableOrderedArrayCollection<Integer>(1, 2);
        Integer[] actual = collection.toArray();
        assertTrue(actual.length == 2 && (actual[0] == 1 || actual[1] == 1) && (actual[0] == 2 || actual[1] == 2));
    }

    /**
     * Verifies that the collection produces a stream that reduces to the correct sum, thus verifying that the
     * spliterator is created correctly.
     */
    @Test
    public void streamShouldProduceAStreamThatReducesToTheCorrectSum() {
        assertEquals(SIX, COLLECTION123.stream().reduce(0, Integer::sum));
    }

    /**
     * Verifies that the collection produces an iterator that when used in a for loop, produces the correct sum.
     */
    @Test
    public void iteratorShouldProduceCorrectSumInForLoop() {
        int sum = 0;
        for (Integer i : COLLECTION123) {
            sum += i;
        }
        assertEquals(SIX, sum);
    }

    /**
     * Verifies that adding an element to an empty collection returns true.
     */
    @Test
    public void addOnAnEmptyCollectionShouldReturnTrue() {
        assertTrue(new ModifiableOrderedArrayCollection<Integer>().add(1));
    }

    /**
     * Verifies that adding a duplicate element to a collection with distinct elements returns false.
     */
    @Test
    public void addDuplicateElementOnCollectionWithDistinctElementsShouldReturnFalse() {
        ModifiableOrderedCollection<Integer> collection =
                new ModifiableOrderedArrayCollection<Integer>(DISTINCT_ELEMENTS, 1);
        assertFalse(collection.add(1));
    }

    /**
     * Verifies that adding a new element to a collection with distinct elements returns true.
     */
    @Test
    public void addNewElementOnCollectionWithDistinctElementsShouldReturnTrue() {
        ModifiableOrderedCollection<Integer> collection =
                new ModifiableOrderedArrayCollection<Integer>(DISTINCT_ELEMENTS, 1);
        assertTrue(collection.add(2));
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
     * Verifies that after adding an element to an empty collection, the size is increased to one.
     */
    @Test
    public void sizeShouldBeOneAfterAddingAnElementToAnEmptyCollection() {
        ModifiableOrderedCollection<Integer> collection = new ModifiableOrderedArrayCollection<Integer>();
        collection.add(1);
        assertEquals(1, collection.size());
    }

    /**
     * Verifies that after adding an element to an empty collection, the collection contains the element added.
     */
    @Test
    public void emptyCollectionShouldContainAnElementAfterHavingItAdded() {
        ModifiableOrderedCollection<Integer> collection = createNewCollection();
        collection.add(SIX);
        assertTrue(collection.contains(SIX));
    }

    /**
     * Verifies that adding the elements of a collection to an empty collection returns true.
     */
    @Test
    public void addAllOnAnEmptyCollectionShouldReturnTrue() {
        assertTrue(new ModifiableOrderedArrayCollection<Integer>().addAll(Collection.of(1, 2)));
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
     * Verifies that adding an empty collection returns false.
     */
    @Test
    public void addAllWithEmptyCollectionShouldReturnFalse() {
        assertFalse(new ModifiableOrderedArrayCollection<Integer>()
                .addAll(new ModifiableOrderedArrayCollection<Integer>()));
    }

    /**
     * Verifies that adding duplicate elements to a collection with distinct elements returns false.
     */
    @Test
    public void addAllOfDuplicateElementsToCollectionWithDistinctElementsShouldReturnFalse() {
        ModifiableOrderedCollection<Integer> collection =
                new ModifiableOrderedArrayCollection<Integer>(DISTINCT_ELEMENTS, 1, 2, THREE);
        assertFalse(collection.addAll(COLLECTION123));
    }

    /**
     * Verifies that adding a collection with duplicate and new elements to a collection with distinct elements returns
     * true.
     */
    @Test
    public void addAllOfNewAndDuplicateElementsToCollectionWithDistinctElementsShouldReturnTrue() {
        ModifiableOrderedCollection<Integer> collection =
                new ModifiableOrderedArrayCollection<Integer>(DISTINCT_ELEMENTS, 1, 2, THREE);
        assertTrue(collection.addAll(COLLECTION123NULL));
    }

    /**
     * Verifies that adding a collection with duplicate and new elements to a collection with distinct elements
     * increases the size correctly.
     */
    @Test
    public void addAllOfNewAndDuplicateElementsToCollectionWithDistinctElementsShouldIncreaseSizeCorrectly() {
        ModifiableOrderedCollection<Integer> collection =
                new ModifiableOrderedArrayCollection<Integer>(DISTINCT_ELEMENTS, 1, 2, THREE);
        collection.addAll(COLLECTION123NULL);
        assertEquals(FOUR, collection.size());
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
     * Verifies that after adding the elements of a collection to an empty collection, the size is increased to by the
     * size of the added collection.
     */
    @Test
    public void sizeShouldBeThreeAfterAddingCollectionWithThreeElementsToAnEmptyCollection() {
        ModifiableOrderedCollection<Integer> collection = new ModifiableOrderedArrayCollection<Integer>();
        collection.addAll(Collection.of(1, 2, THREE));
        assertEquals(THREE, collection.size());
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
     * Verifies that after adding the elements of a collection to a collection, the collection contains the elements
     * added.
     */
    @Test
    public void collectionShouldContainElementsAfterHavingItAddedAll() {
        ModifiableOrderedCollection<Integer> collection = createNewCollection();
        collection.addAll(Collection.of(0, SIX));
        assertTrue(collection.contains(0));
        assertTrue(collection.contains(SIX));
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
     * Verifies that trying to remove an element that isn't in the collection returns false.
     */
    @Test
    public void removeShouldReturnFalseWhenTryingToRemoveAnElementTheCollectionDoesNotContain() {
        assertFalse(createNewCollection().remove(SIX));
    }

    /**
     * Verifies that trying to remove an element that is in the collection returns true.
     */
    @Test
    public void removeShouldReturnTrueForAnElementInTheCollection() {
        assertTrue(createNewCollection().remove(1));
    }

    /**
     * Verifies that when some elements are removed, removeAll returns true.
     */
    @Test
    public void removeAllShouldReturnTrueWhenSomeElementsAreRemoved() {
        ModifiableOrderedCollection<Integer> collection = createNewCollection();
        assertTrue(collection.removeAll(COLLECTION12));
    }

    /**
     * Verifies that when no elements are removed, removeAll returns false.
     */
    @Test
    public void removeAllShouldReturnFalseWhenNoElementsAreRemoved() {
        ModifiableOrderedCollection<Integer> collection = createNewCollection();
        assertFalse(collection.removeAll(new ArrayCollection<Integer>(FOUR)));
    }

    /**
     * Verifies that not all duplicate elements are removed by removeAll.
     */
    @Test
    public void removeAllShouldNotRemoveDuplicateElements() {
        ModifiableOrderedCollection<Integer> collection =
                new ModifiableOrderedArrayCollection<Integer>(DUPLICATE_ELEMENTS, 0, 1, 1, 2, 2, THREE);
        collection.removeAll(new ArrayCollection<Integer>(0, 1, 2));
        assertArrayEquals(new Integer[] {1, 2, THREE}, collection.toArray());
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
     * Verifies that the size of the collection is decreased by one when an element is removed.
     */
    @Test
    public void sizeShouldBeDecreasedByOneWhenAnElementIsRemoved() {
        ModifiableOrderedCollection<Integer> collection = createNewCollection();
        collection.remove(1);
        assertEquals(2, collection.size());
    }

    /**
     * Verifies that when an element is removed, the collection doesn't contain it anymore.
     */
    @Test
    public void collectionShouldNotContainAnElementAfterItHasBeenRemoved() {
        ModifiableOrderedCollection<Integer> collection = createNewCollection();
        collection.remove(1);
        assertFalse(collection.contains(1));
    }

    /**
     * Verifies that when a collection is cleared, it becomes empty.
     */
    @Test
    public void clearShouldMakeCollectionEmpty() {
        ModifiableOrderedCollection<Integer> collection = createNewCollection();
        collection.clear();
        assertTrue(collection.isEmpty());
    }

    /**
     * Verifies that the collection constructed using another collection and the natural comparator produces the correct
     * array.
     */
    @Test
    public void constructorUsingCollectionAndNaturalOrderComparatorShouldProduceTheCorrectArray() {
        assertArrayEquals(ARRAY123456,
                new ModifiableOrderedArrayCollection<Integer>(Collection.of(1, FIVE, SIX, 2, FOUR, THREE),
                        Comparator.naturalOrder()).toArray());
    }

    /**
     * Verifies that the constructor using a comparator transfers the element cardinality correctly.
     */
    @Test
    public void constructorUsingCollectionAndNaturalOrderComparatorShouldTransferElementCardinality() {
        assertEquals(DISTINCT_ELEMENTS,
                new ModifiableOrderedArrayCollection<Integer>(
                        Collection.of(DISTINCT_ELEMENTS, 1, FIVE, SIX, 2, FOUR, THREE), Comparator.naturalOrder())
                                .getElementCardinality());
    }

    /**
     * Verifies that duplicate elements are removed if a collection with distinct elements is constructed.
     */
    @Test
    public void constructorShouldRemoveDuplicateElementsFromDistinctCollection() {
        ModifiableOrderedCollection<Integer> collection =
                new ModifiableOrderedArrayCollection<Integer>(DISTINCT_ELEMENTS, 1, 2, 2, THREE, 2, THREE);
        Integer[] expected = new Integer[] {1, 2, THREE};
        assertArrayEquals(expected, collection.toArray());
    }

    /**
     * Verifies that by default, a collection can contain duplicate elements.
     */
    @Test
    public void constructorShouldSetElementCardinalityToDuplicateByDefault() {
        assertEquals(DUPLICATE_ELEMENTS, new ModifiableOrderedArrayCollection<Integer>().getElementCardinality());
    }

    /**
     * Verifies that when distinct elements are requested, the element cardinality is set to distinct elements.
     */
    @Test
    public void constructorShouldSetElementCardinalityToDistinctElementsWhenSpecified() {
        assertEquals(DISTINCT_ELEMENTS,
                new ModifiableOrderedArrayCollection<Integer>(DISTINCT_ELEMENTS, 1).getElementCardinality());
    }

    /**
     * Verifies that the spliterator has the ordered flag set.
     */
    @Test
    public void spliteratorShouldSetOrderedFlag() {
        assertTrue(COLLECTION123.spliterator().hasCharacteristics(Spliterator.ORDERED));
    }

    /**
     * Verifies that the spliterator has the distinct flag not set for collections with duplicate elements.
     */
    @Test
    public void spliteratorShouldNotSetDistinctFlagForCollectionWithDuplicateElements() {
        assertFalse(new ModifiableOrderedArrayCollection<Integer>(DUPLICATE_ELEMENTS, 1).spliterator()
                .hasCharacteristics(Spliterator.DISTINCT));
    }

    /**
     * Verifies that the spliterator has the distinct flag set for collections with distinct elements.
     */
    @Test
    public void spliteratorShouldSetDistinctFlagForCollectionWithDistinctElements() {
        assertTrue(new ModifiableOrderedArrayCollection<Integer>(DISTINCT_ELEMENTS, 1).spliterator()
                .hasCharacteristics(Spliterator.DISTINCT));
    }

    /**
     * Verifies that when some elements are removed, retainAll returns true.
     */
    @Test
    public void retainAllShouldReturnTrueWhenSomeElementsAreRemoved() {
        ModifiableOrderedCollection<Integer> collection = createNewCollection();
        assertTrue(collection.retainAll(COLLECTION12));
    }

    /**
     * Verifies that when no elements are removed, retainAll returns false.
     */
    @Test
    public void retainAllShouldReturnFalseWhenNoElementsAreRemoved() {
        ModifiableOrderedCollection<Integer> collection = createNewCollection();
        assertFalse(collection.retainAll(COLLECTION123));
    }

    /**
     * Verifies that when all elements are retained, a collection is empty.
     */
    @Test
    public void retainAllWithTheSameElementsShouldNotRemoveElements() {
        ModifiableOrderedCollection<Integer> collection = createNewCollection();
        collection.retainAll(COLLECTION123);
        assertArrayEquals(new Integer[] {1, 2, THREE}, collection.toArray());
    }

    /**
     * Verifies that when only absent elements should be retained, retainAll empties the collection.
     */
    @Test
    public void retainAllWithAbsentElementsOnlyClearsTheCollection() {
        ModifiableOrderedCollection<Integer> collection = createNewCollection();
        collection.retainAll(new ModifiableOrderedArrayCollection<Integer>(FOUR));
        assertTrue(collection.isEmpty());
    }
}
