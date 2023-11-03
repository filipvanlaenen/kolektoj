package net.filipvanlaenen.kolektoj.array;

import static net.filipvanlaenen.kolektoj.Collection.ElementCardinality.DISTINCT_ELEMENTS;
import static net.filipvanlaenen.kolektoj.Collection.ElementCardinality.DUPLICATE_ELEMENTS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Spliterator;

import org.junit.jupiter.api.Test;

import net.filipvanlaenen.kolektoj.Collection;
import net.filipvanlaenen.kolektoj.ModifiableCollection;

/**
 * Unit tests on the {@link net.filipvanlaenen.kolektoj.array.ModifiableArrayCollection} class.
 */
public class ModifiableArrayCollectionTest {
    /**
     * The magic number three.
     */
    private static final int THREE = 3;
    /**
     * The magic number four.
     */
    private static final int FOUR = 4;
    /**
     * The magic number six.
     */
    private static final int SIX = 6;
    /**
     * The magic number fifteen.
     */
    private static final int FIFTEEN = 15;
    /**
     * Collection with the integers 1 and 2.
     */
    private static final ModifiableCollection<Integer> COLLECTION12 = new ModifiableArrayCollection<Integer>(1, 2);
    /**
     * Collection with the integers 1, 2 and 3.
     */
    private static final ModifiableCollection<Integer> COLLECTION123 = createNewCollection();
    /**
     * Collection with the integers 1, 2, 3 and null.
     */
    private static final ModifiableCollection<Integer> COLLECTION123NULL =
            new ModifiableArrayCollection<Integer>(1, 2, 3, null);

    /**
     * Creates a new collection to run the unit tests on.
     *
     * @return A new collection to run the unit tests on.
     */
    private static ModifiableArrayCollection<Integer> createNewCollection() {
        return new ModifiableArrayCollection<Integer>(1, 2, THREE);
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
     * Verifies that contains returns false for an element not in the collection.
     */
    @Test
    public void containsShouldReturnFalseForAnElementNotInTheCollection() {
        assertFalse(COLLECTION123.contains(0));
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
        IndexOutOfBoundsException exception =
                assertThrows(IndexOutOfBoundsException.class, () -> new ModifiableArrayCollection<Integer>().get());
        assertEquals("Cannot return an element from an empty collection.", exception.getMessage());
    }

    /**
     * Verifies that the collection produces an array with the elements.
     */
    @Test
    public void toArrayShouldProduceAnArrayWithTheElementsOfTheCollection() {
        Integer[] actual = COLLECTION12.toArray();
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
        assertTrue(new ModifiableArrayCollection<Integer>().add(1));
    }

    /**
     * Verifies that adding a duplicate element to a collection with distinct elements returns false.
     */
    @Test
    public void addDuplicateElementOnCollectionWithDistinctElementsShouldReturnFalse() {
        ModifiableCollection<Integer> collection = new ModifiableArrayCollection<Integer>(DISTINCT_ELEMENTS, 1);
        assertFalse(collection.add(1));
    }

    /**
     * Verifies that adding a new element to a collection with distinct elements returns true.
     */
    @Test
    public void addNewElementOnCollectionWithDistinctElementsShouldReturnTrue() {
        ModifiableCollection<Integer> collection = new ModifiableArrayCollection<Integer>(DISTINCT_ELEMENTS, 1);
        assertTrue(collection.add(2));
    }

    /**
     * Verifies that adding beyond the stride doesn't lead to an exception.
     */
    @Test
    public void addManyTimesShouldNotProduceAnException() {
        ModifiableCollection<Integer> collection = new ModifiableArrayCollection<Integer>();
        for (int i = 0; i < SIX; i++) {
            collection.add(i);
        }
        assertEquals(FIFTEEN, collection.stream().reduce(0, Integer::sum));
    }

    /**
     * Verifies that after adding an element to an empty collection, the size is increased to one.
     */
    @Test
    public void sizeShouldBeOneAfterAddingAnElementToAnEmptyCollection() {
        ModifiableCollection<Integer> collection = new ModifiableArrayCollection<Integer>();
        collection.add(1);
        assertEquals(1, collection.size());
    }

    /**
     * Verifies that after adding an element to a collection, the collection contains the element added.
     */
    @Test
    public void emptyCollectionShouldContainAnElementAfterHavingItAdded() {
        ModifiableCollection<Integer> collection = createNewCollection();
        collection.add(SIX);
        assertTrue(collection.contains(SIX));
    }

    /**
     * Verifies that adding the elements of a collection to an empty collection returns true.
     */
    @Test
    public void addAllOnAnEmptyCollectionShouldReturnTrue() {
        assertTrue(new ModifiableArrayCollection<Integer>().addAll(COLLECTION12));
    }

    /**
     * Verifies that adding an empty collection returns false.
     */
    @Test
    public void addAllWithEmptyCollectionShouldReturnFalse() {
        assertFalse(new ModifiableArrayCollection<Integer>().addAll(new ModifiableArrayCollection<Integer>()));
    }

    /**
     * Verifies that adding duplicate elements to a collection with distinct elements returns false.
     */
    @Test
    public void addAllOfDuplicateElementsToCollectionWithDistinctElementsShouldReturnFalse() {
        ModifiableCollection<Integer> collection =
                new ModifiableArrayCollection<Integer>(DISTINCT_ELEMENTS, 1, 2, THREE);
        assertFalse(collection.addAll(COLLECTION123));
    }

    /**
     * Verifies that adding a collection with duplicate and new elements to a collection with distinct elements returns
     * true.
     */
    @Test
    public void addAllOfNewAndDuplicateElementsToCollectionWithDistinctElementsShouldReturnTrue() {
        ModifiableCollection<Integer> collection =
                new ModifiableArrayCollection<Integer>(DISTINCT_ELEMENTS, 1, 2, THREE);
        assertTrue(collection.addAll(COLLECTION123NULL));
    }

    /**
     * Verifies that adding a collection with duplicate and new elements to a collection with distinct elements
     * increases the size correctly.
     */
    @Test
    public void addAllOfNewAndDuplicateElementsToCollectionWithDistinctElementsShouldIncreaseSizeCorrectly() {
        ModifiableCollection<Integer> collection =
                new ModifiableArrayCollection<Integer>(DISTINCT_ELEMENTS, 1, 2, THREE);
        collection.addAll(COLLECTION123NULL);
        assertEquals(FOUR, collection.size());
    }

    /**
     * Verifies that after adding the elements of a collection to a collection, the size is increased to by the size of
     * the added collection.
     */
    @Test
    public void sizeShouldBeThreeAfterAddingCollectionWithThreeElementsToAnEmptyCollection() {
        ModifiableCollection<Integer> collection = new ModifiableArrayCollection<Integer>();
        collection.addAll(COLLECTION123);
        assertEquals(THREE, collection.size());
    }

    /**
     * Verifies that after adding the elements of a collection to a collection, the collection contains the elements
     * added.
     */
    @Test
    public void collectionShouldContainElementsAfterHavingItAddedAll() {
        ModifiableCollection<Integer> collection = createNewCollection();
        collection.addAll(Collection.of(0, SIX));
        assertTrue(collection.contains(0));
        assertTrue(collection.contains(SIX));
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
     * Verifies that the size of the collection is decreased by one when an element is removed.
     */
    @Test
    public void sizeShouldBeDecreasedByOneWhenAnElementIsRemoved() {
        ModifiableCollection<Integer> collection = createNewCollection();
        collection.remove(1);
        assertEquals(2, collection.size());
    }

    /**
     * Verifies that when an element is removed, the collection doesn't contain it anymore.
     */
    @Test
    public void collectionShouldNotContainAnElementAfterItHasBeenRemoved() {
        ModifiableCollection<Integer> collection = createNewCollection();
        collection.remove(1);
        assertFalse(collection.contains(1));
    }

    /**
     * Verifies that when a collection is cleared, it becomes empty.
     */
    @Test
    public void clearShouldMakeCollectionEmpty() {
        ModifiableCollection<Integer> collection = createNewCollection();
        collection.clear();
        assertTrue(collection.isEmpty());
    }

    /**
     * Verifies that duplicate elements are removed if a collection with distinct elements is constructed.
     */
    @Test
    public void constructorShouldRemoveDuplicateElementsFromDistinctCollection() {
        ModifiableCollection<Integer> collection =
                new ModifiableArrayCollection<Integer>(DISTINCT_ELEMENTS, 1, 2, 2, THREE, 2, THREE);
        assertEquals(THREE, collection.size());
        assertTrue(collection.contains(1));
        assertTrue(collection.contains(2));
        assertTrue(collection.contains(THREE));
    }

    /**
     * Verifies that by default, a collection can contain duplicate elements.
     */
    @Test
    public void constructorShouldSetElementCardinalityToDuplicateByDefault() {
        assertEquals(DUPLICATE_ELEMENTS, new ModifiableArrayCollection<Integer>().getElementCardinality());
    }

    /**
     * Verifies that when distinct elements are requested, the element cardinality is set to distinct elements.
     */
    @Test
    public void constructorShouldSetElementCardinalityToDistinctElementsWhenSpecified() {
        assertEquals(DISTINCT_ELEMENTS,
                new ModifiableArrayCollection<Integer>(DISTINCT_ELEMENTS, 1).getElementCardinality());
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
     * Verifies that the spliterator has the distinct flag not set for collections with duplicate elements.
     */
    @Test
    public void spliteratorShouldNotSetDistinctFlagForCollectionWithDuplicateElements() {
        assertFalse(new ModifiableArrayCollection<Integer>(DUPLICATE_ELEMENTS, 1).spliterator()
                .hasCharacteristics(Spliterator.DISTINCT));
    }

    /**
     * Verifies that the spliterator has the distinct flag set for collections with distinct elements.
     */
    @Test
    public void spliteratorShouldSetDistinctFlagForCollectionWithDistinctElements() {
        assertTrue(new ModifiableArrayCollection<Integer>(DISTINCT_ELEMENTS, 1).spliterator()
                .hasCharacteristics(Spliterator.DISTINCT));
    }

    /**
     * Verifies that when all elements are removed, a collection is empty.
     */
    @Test
    public void removeAllWithTheSameElementsShouldMakeCollectionEmpty() {
        ModifiableCollection<Integer> collection = createNewCollection();
        collection.removeAll(COLLECTION123);
        assertTrue(collection.isEmpty());
    }

    /**
     * Verifies that when some elements are removed, removeAll returns true.
     */
    @Test
    public void removeAllShouldReturnTrueWhenSomeElementsAreRemoved() {
        ModifiableCollection<Integer> collection = createNewCollection();
        assertTrue(collection.removeAll(COLLECTION12));
    }

    /**
     * Verifies that when no elements are removed, removeAll returns false.
     */
    @Test
    public void removeAllShouldReturnFalseWhenNoElementsAreRemoved() {
        ModifiableCollection<Integer> collection = createNewCollection();
        assertFalse(collection.removeAll(new ModifiableArrayCollection<Integer>(FOUR)));
    }

    /**
     * Verifies that not all duplicate elements are removed by removeAll.
     */
    @Test
    public void removeAllShouldNotRemoveDuplicateElements() {
        ModifiableCollection<Integer> collection =
                new ModifiableArrayCollection<Integer>(DUPLICATE_ELEMENTS, 0, 1, 1, 2, 2);
        collection.removeAll(new ModifiableArrayCollection<Integer>(0, 1, 2));
        assertEquals(2, collection.size());
        assertTrue(collection.contains(1));
        assertTrue(collection.contains(2));
    }

    /**
     * Verifies that when some elements are removed, retainAll returns true.
     */
    @Test
    public void retainAllShouldReturnTrueWhenSomeElementsAreRemoved() {
        ModifiableCollection<Integer> collection = createNewCollection();
        assertTrue(collection.retainAll(COLLECTION12));
    }

    /**
     * Verifies that when no elements are removed, retainAll returns false.
     */
    @Test
    public void retainAllShouldReturnFalseWhenNoElementsAreRemoved() {
        ModifiableCollection<Integer> collection = createNewCollection();
        assertFalse(collection.retainAll(COLLECTION123));
    }

    /**
     * Verifies that when all elements are retained, a collection is empty.
     */
    @Test
    public void retainAllWithTheSameElementsShouldNotRemoveElements() {
        ModifiableCollection<Integer> collection = createNewCollection();
        collection.retainAll(COLLECTION123);
        assertEquals(THREE, collection.size());
        assertTrue(collection.contains(1));
        assertTrue(collection.contains(2));
        assertTrue(collection.contains(THREE));
    }

    /**
     * Verifies that when only absent elements should be retained, retainAll empties the collection.
     */
    @Test
    public void retainAllWithAbsentElementsOnlyClearsTheCollection() {
        ModifiableCollection<Integer> collection = createNewCollection();
        collection.retainAll(new ModifiableArrayCollection<Integer>(FOUR));
        assertTrue(collection.isEmpty());
    }
}
