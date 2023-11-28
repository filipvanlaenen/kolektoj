package net.filipvanlaenen.kolektoj;

import static net.filipvanlaenen.kolektoj.Collection.ElementCardinality.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Spliterator;

import org.junit.jupiter.api.Test;

import net.filipvanlaenen.kolektoj.Collection.ElementCardinality;

/**
 * Unit tests on implementations of the {@link net.filipvanlaenen.kolektoj.ModifiableCollection} interface.
 *
 * @param <T> The subclass type to be tested.
 */
public abstract class ModifiableCollectionTestBase<T extends ModifiableCollection<Integer>> {
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
    private final ModifiableCollection<Integer> collection12 = createModifiableCollection(1, 2);
    /**
     * Collection with the integers 1, 2 and 3.
     */
    private final ModifiableCollection<Integer> collection123 = createCollection123();
    /**
     * Collection with the integers 1, 2, 3 and null.
     */
    private final ModifiableCollection<Integer> collection123null = createModifiableCollection(1, 2, 3, null);

    /**
     * Creates a modifiable collection containing the provided integers.
     *
     * @param integers The integers to be included in the modifiable collection.
     * @return A modifiable collection containing the provided integers.
     */
    protected abstract T createModifiableCollection(Integer... integers);

    /**
     * Creates a modifiable collection with the specified element cardinality containing the provided integers.
     *
     * @param elementCardinality The element cardinality for the modifiable collection.
     * @param integers           The integers to be included in the modifiable collection.
     * @return A modifiable collection with the specified element cardinality containing the provided integers.
     */
    protected abstract T createModifiableCollection(ElementCardinality elementCardinality, Integer... integers);

    /**
     * Creates a new modifiable collection to run the unit tests on.
     *
     * @return A new modifiable collection to run the unit tests on.
     */
    private T createCollection123() {
        return createModifiableCollection(1, 2, THREE);
    }

    /**
     * Verifies that the correct length is returned for a collection with three elements.
     */
    @Test
    public void sizeShouldReturnThreeForACollectionOfThreeElements() {
        assertEquals(THREE, collection123.size());
    }

    /**
     * Verifies that contains returns true for an element in the collection.
     */
    @Test
    public void containsShouldReturnTrueForAnElementInTheCollection() {
        assertTrue(collection123.contains(1));
    }

    /**
     * Verifies that contains returns false for an element not in the collection.
     */
    @Test
    public void containsShouldReturnFalseForAnElementNotInTheCollection() {
        assertFalse(collection123.contains(0));
    }

    /**
     * Verifies that when you get an element from a collection, the collection contains it.
     */
    @Test
    public void getShouldReturnAnElementPresentInTheCollection() {
        Integer element = collection123.get();
        assertTrue(collection123.contains(element));
    }

    /**
     * Verifies that trying to get an element from an empty collection throws IndexOutOfBoundsException.
     */
    @Test
    public void getShouldThrowExceptionWhenCalledOnAnEmptyCollection() {
        IndexOutOfBoundsException exception =
                assertThrows(IndexOutOfBoundsException.class, () -> createModifiableCollection().get());
        assertEquals("Cannot return an element from an empty collection.", exception.getMessage());
    }

    /**
     * Verifies that the collection produces an array with the elements.
     */
    @Test
    public void toArrayShouldProduceAnArrayWithTheElementsOfTheCollection() {
        Integer[] actual = collection12.toArray();
        assertTrue(actual.length == 2 && (actual[0] == 1 || actual[1] == 1) && (actual[0] == 2 || actual[1] == 2));
    }

    /**
     * Verifies that the collection produces a stream that reduces to the correct sum, thus verifying that the
     * spliterator is created correctly.
     */
    @Test
    public void streamShouldProduceAStreamThatReducesToTheCorrectSum() {
        assertEquals(SIX, collection123.stream().reduce(0, Integer::sum));
    }

    /**
     * Verifies that the collection produces an iterator that when used in a for loop, produces the correct sum.
     */
    @Test
    public void iteratorShouldProduceCorrectSumInForLoop() {
        int sum = 0;
        for (Integer i : collection123) {
            sum += i;
        }
        assertEquals(SIX, sum);
    }

    /**
     * Verifies that adding an element to an empty collection returns true.
     */
    @Test
    public void addOnAnEmptyCollectionShouldReturnTrue() {
        assertTrue(createModifiableCollection().add(1));
    }

    /**
     * Verifies that adding a duplicate element to a collection with distinct elements returns false.
     */
    @Test
    public void addDuplicateElementOnCollectionWithDistinctElementsShouldReturnFalse() {
        ModifiableCollection<Integer> collection = createModifiableCollection(DISTINCT_ELEMENTS, 1);
        assertFalse(collection.add(1));
    }

    /**
     * Verifies that adding a new element to a collection with distinct elements returns true.
     */
    @Test
    public void addNewElementOnCollectionWithDistinctElementsShouldReturnTrue() {
        ModifiableCollection<Integer> collection = createModifiableCollection(DISTINCT_ELEMENTS, 1);
        assertTrue(collection.add(2));
    }

    /**
     * Verifies that adding beyond the stride doesn't lead to an exception.
     */
    @Test
    public void addManyTimesShouldNotProduceAnException() {
        ModifiableCollection<Integer> collection = createModifiableCollection();
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
        ModifiableCollection<Integer> collection = createModifiableCollection();
        collection.add(1);
        assertEquals(1, collection.size());
    }

    /**
     * Verifies that after adding an element to a collection, the collection contains the element added.
     */
    @Test
    public void emptyCollectionShouldContainAnElementAfterHavingItAdded() {
        ModifiableCollection<Integer> collection = createCollection123();
        collection.add(SIX);
        assertTrue(collection.contains(SIX));
    }

    /**
     * Verifies that adding the elements of a collection to an empty collection returns true.
     */
    @Test
    public void addAllOnAnEmptyCollectionShouldReturnTrue() {
        assertTrue(createModifiableCollection().addAll(collection12));
    }

    /**
     * Verifies that adding an empty collection returns false.
     */
    @Test
    public void addAllWithEmptyCollectionShouldReturnFalse() {
        assertFalse(createModifiableCollection().addAll(createModifiableCollection()));
    }

    /**
     * Verifies that adding duplicate elements to a collection with distinct elements returns false.
     */
    @Test
    public void addAllOfDuplicateElementsToCollectionWithDistinctElementsShouldReturnFalse() {
        ModifiableCollection<Integer> collection = createModifiableCollection(DISTINCT_ELEMENTS, 1, 2, THREE);
        assertFalse(collection.addAll(collection123));
    }

    /**
     * Verifies that adding a collection with duplicate and new elements to a collection with distinct elements returns
     * true.
     */
    @Test
    public void addAllOfNewAndDuplicateElementsToCollectionWithDistinctElementsShouldReturnTrue() {
        ModifiableCollection<Integer> collection = createModifiableCollection(DISTINCT_ELEMENTS, 1, 2, THREE);
        assertTrue(collection.addAll(collection123null));
    }

    /**
     * Verifies that adding a collection with duplicate and new elements to a collection with distinct elements
     * increases the size correctly.
     */
    @Test
    public void addAllOfNewAndDuplicateElementsToCollectionWithDistinctElementsShouldIncreaseSizeCorrectly() {
        ModifiableCollection<Integer> collection = createModifiableCollection(DISTINCT_ELEMENTS, 1, 2, THREE);
        collection.addAll(collection123null);
        assertEquals(FOUR, collection.size());
    }

    /**
     * Verifies that after adding the elements of a collection to a collection, the size is increased to by the size of
     * the added collection.
     */
    @Test
    public void sizeShouldBeThreeAfterAddingCollectionWithThreeElementsToAnEmptyCollection() {
        ModifiableCollection<Integer> collection = createModifiableCollection();
        collection.addAll(collection123);
        assertEquals(THREE, collection.size());
    }

    /**
     * Verifies that after adding the elements of a collection to a collection, the collection contains the elements
     * added.
     */
    @Test
    public void collectionShouldContainElementsAfterHavingItAddedAll() {
        ModifiableCollection<Integer> collection = createCollection123();
        collection.addAll(Collection.of(0, SIX));
        assertTrue(collection.contains(0));
        assertTrue(collection.contains(SIX));
    }

    /**
     * Verifies that trying to remove an element from an empty collection returns false.
     */
    @Test
    public void removeOnEmptyCollectionShouldReturnFalse() {
        assertFalse(createModifiableCollection().remove(1));
    }

    /**
     * Verifies that trying to remove an element from a collection with only that element returns true.
     */
    @Test
    public void removeElementFromCollectionWithOnlyThatElementShouldReturnTrue() {
        assertTrue(createModifiableCollection(1).remove(1));
    }

    /**
     * Verifies that trying to remove an element that isn't in the collection returns false.
     */
    @Test
    public void removeShouldReturnFalseWhenTryingToRemoveAnElementTheCollectionDoesNotContain() {
        assertFalse(createCollection123().remove(SIX));
    }

    /**
     * Verifies that trying to remove an element that is in the collection returns true.
     */
    @Test
    public void removeShouldReturnTrueForAnElementInTheCollection() {
        assertTrue(createCollection123().remove(1));
    }

    /**
     * Verifies that the size of the collection is decreased by one when an element is removed.
     */
    @Test
    public void sizeShouldBeDecreasedByOneWhenAnElementIsRemoved() {
        ModifiableCollection<Integer> collection = createCollection123();
        collection.remove(1);
        assertEquals(2, collection.size());
    }

    /**
     * Verifies that when an element is removed, the collection doesn't contain it anymore.
     */
    @Test
    public void collectionShouldNotContainAnElementAfterItHasBeenRemoved() {
        ModifiableCollection<Integer> collection = createCollection123();
        collection.remove(1);
        assertFalse(collection.contains(1));
    }

    /**
     * Verifies that when a collection is cleared, it becomes empty.
     */
    @Test
    public void clearShouldMakeCollectionEmpty() {
        ModifiableCollection<Integer> collection = createCollection123();
        collection.clear();
        assertTrue(collection.isEmpty());
        assertEquals(0, collection.toArray().length);
    }

    /**
     * Verifies that duplicate elements are removed if a collection with distinct elements is constructed.
     */
    @Test
    public void constructorShouldRemoveDuplicateElementsFromDistinctCollection() {
        ModifiableCollection<Integer> collection =
                createModifiableCollection(DISTINCT_ELEMENTS, 1, 2, 2, THREE, 2, THREE);
        assertEquals(THREE, collection.size());
        assertEquals(THREE, collection.toArray().length);
        assertTrue(collection.contains(1));
        assertTrue(collection.contains(2));
        assertTrue(collection.contains(THREE));
    }

    /**
     * Verifies that duplicate elements are not removed if a collection with duplicate elements is constructed.
     */
    @Test
    public void constructorShouldNotRemoveDuplicateElementsFromDuplicateCollection() {
        ModifiableCollection<Integer> collection =
                createModifiableCollection(DUPLICATE_ELEMENTS, 1, 2, 2, THREE, 2, THREE);
        assertEquals(SIX, collection.size());
        assertEquals(SIX, collection.toArray().length);
    }

    /**
     * Verifies that by default, a collection can contain duplicate elements.
     */
    @Test
    public void constructorShouldSetElementCardinalityToDuplicateByDefault() {
        assertEquals(DUPLICATE_ELEMENTS, createModifiableCollection().getElementCardinality());
    }

    /**
     * Verifies that when distinct elements are requested, the element cardinality is set to distinct elements.
     */
    @Test
    public void constructorShouldSetElementCardinalityToDistinctElementsWhenSpecified() {
        assertEquals(DISTINCT_ELEMENTS, createModifiableCollection(DISTINCT_ELEMENTS, 1).getElementCardinality());
    }

    /**
     * Verifies that containsAll returns false is the other collection is larger.
     */
    @Test
    public void containsAllShouldReturnFalseIfTheOtherCollectionIsLarger() {
        assertFalse(collection123.containsAll(collection123null));
    }

    /**
     * Verifies that containsAll returns true if a collection is compared to itself.
     */
    @Test
    public void containsAllShouldReturnTrueWhenComparedToItself() {
        assertTrue(collection123.containsAll(collection123));
    }

    /**
     * Verifies that containsAll returns false if one element doesn't match.
     */
    @Test
    public void containsAllShouldReturnFalseWhenOneElementDoesNotMatch() {
        assertFalse(collection123.containsAll(createModifiableCollection(1, 2, FOUR)));
    }

    /**
     * Verifies that the spliterator has the distinct flag not set for collections with duplicate elements.
     */
    @Test
    public void spliteratorShouldNotSetDistinctFlagForCollectionWithDuplicateElements() {
        assertFalse(createModifiableCollection(DUPLICATE_ELEMENTS, 1).spliterator()
                .hasCharacteristics(Spliterator.DISTINCT));
    }

    /**
     * Verifies that the spliterator has the distinct flag set for collections with distinct elements.
     */
    @Test
    public void spliteratorShouldSetDistinctFlagForCollectionWithDistinctElements() {
        assertTrue(createModifiableCollection(DISTINCT_ELEMENTS, 1).spliterator()
                .hasCharacteristics(Spliterator.DISTINCT));
    }

    /**
     * Verifies that when all elements are removed, a collection is empty.
     */
    @Test
    public void removeAllWithTheSameElementsShouldMakeCollectionEmpty() {
        ModifiableCollection<Integer> collection = createCollection123();
        collection.removeAll(collection123);
        assertTrue(collection.isEmpty());
    }

    /**
     * Verifies that when some elements are removed, removeAll returns true.
     */
    @Test
    public void removeAllShouldReturnTrueWhenSomeElementsAreRemoved() {
        ModifiableCollection<Integer> collection = createCollection123();
        assertTrue(collection.removeAll(collection12));
    }

    /**
     * Verifies that when no elements are removed, removeAll returns false.
     */
    @Test
    public void removeAllShouldReturnFalseWhenNoElementsAreRemoved() {
        ModifiableCollection<Integer> collection = createCollection123();
        assertFalse(collection.removeAll(Collection.of(FOUR)));
    }

    /**
     * Verifies that not all duplicate elements are removed by removeAll.
     */
    @Test
    public void removeAllShouldNotRemoveDuplicateElements() {
        ModifiableCollection<Integer> collection = createModifiableCollection(DUPLICATE_ELEMENTS, 0, 1, 1, 2, 2);
        collection.removeAll(Collection.of(0, 1, 2));
        assertEquals(2, collection.size());
        assertTrue(collection.contains(1));
        assertTrue(collection.contains(2));
    }

    /**
     * Verifies that retainAll on an empty collection returns false.
     */
    @Test
    public void retainAllShouldReturnFalseOnAnEmptyCollection() {
        assertFalse(createModifiableCollection().retainAll(collection123));
    }

    /**
     * Verifies that the correct removal of all but one elements by retainAll.
     */
    @Test
    public void retainAllShouldRemoveAllButOneElementsCorrectly() {
        for (int i = 1; i <= THREE; i++) {
            ModifiableCollection<Integer> collection = createCollection123();
            assertTrue(collection.retainAll(createModifiableCollection(i)));
            assertEquals(1, collection.size());
            assertEquals(1, collection.toArray().length);
            for (int j = 1; j <= THREE; j++) {
                assertEquals(i == j, collection.contains(j));
            }
        }
    }

    /**
     * Verifies that the correct removal of one element by retainAll.
     */
    @Test
    public void retainAllShouldRemoveOneElementCorrectly() {
        for (int i = 1; i <= THREE; i++) {
            ModifiableCollection<Integer> collection = createCollection123();
            ModifiableCollection<Integer> r = createCollection123();
            r.remove(i);
            assertTrue(collection.retainAll(r));
            assertEquals(2, collection.size());
            assertEquals(2, collection.toArray().length);
            for (int j = 1; j <= THREE; j++) {
                assertEquals(i != j, collection.contains(j));
            }
        }
    }

    /**
     * Verifies that when no elements are removed, retainAll returns false.
     */
    @Test
    public void retainAllShouldReturnFalseWhenNoElementsAreRemoved() {
        ModifiableCollection<Integer> collection = createCollection123();
        assertFalse(collection.retainAll(collection123));
    }

    /**
     * Verifies that when all elements are retained, the collection has remained intact.
     */
    @Test
    public void retainAllWithTheSameElementsShouldNotRemoveElements() {
        ModifiableCollection<Integer> collection = createCollection123();
        collection.retainAll(collection123);
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
        ModifiableCollection<Integer> collection = createCollection123();
        assertTrue(collection.retainAll(Collection.of(FOUR)));
        assertTrue(collection.isEmpty());
    }
}
