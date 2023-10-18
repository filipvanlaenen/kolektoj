package net.filipvanlaenen.kolektoj.array;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
     * The magic number six.
     */
    private static final int SIX = 6;
    /**
     * The magic number fifteen.
     */
    private static final int FIFTEEN = 15;
    /**
     * Collection with the integers 1, 2 and 3.
     */
    private static final ModifiableCollection<Integer> COLLECTION123 = createNewCollection();

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
     * Verifies that contains returns true for null if it's in the collection.
     */
    @Test
    public void containsShouldReturnTrueForNullIfInTheCollection() {
        assertTrue(new ModifiableArrayCollection<Integer>(1, 2, THREE, null).contains(null));
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
        ModifiableCollection<Integer> collection = new ModifiableArrayCollection<Integer>(1, 2);
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
        assertTrue(new ModifiableArrayCollection<Integer>().add(1));
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
     * Verifies that after adding an element to an empty collection, the collection contains the element added.
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
        assertTrue(new ModifiableArrayCollection<Integer>().addAll(Collection.of(1, 2)));
    }

    /**
     * Verifies that after adding the elements of a collection to an empty collection, the size is increased to by the
     * size of the added collection.
     */
    @Test
    public void sizeShouldBeThreeAfterAddingCollectionWithThreeElementsToAnEmptyCollection() {
        ModifiableCollection<Integer> collection = new ModifiableArrayCollection<Integer>();
        collection.addAll(Collection.of(1, 2, THREE));
        assertEquals(THREE, collection.size());
    }

    /**
     * Verifies that after adding the elements of a collection to an empty collection, the collection contains the
     * elements added.
     */
    @Test
    public void emptyCollectionShouldContainElementsAfterHavingItAddedAll() {
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
}
