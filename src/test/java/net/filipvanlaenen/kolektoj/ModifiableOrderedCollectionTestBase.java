package net.filipvanlaenen.kolektoj;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

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
     * The magic number six.
     */
    private static final int SIX = 6;
    /**
     * Ordered collection with the integers 1, 2 and 3.
     */
    private final T collection123 = createModifiableOrderedCollection(1, 2, 3);

    /**
     * Creates a modifiable ordered collection containing the provided integers.
     *
     * @param integers The integers to be included in the modifiable ordered collection.
     * @return A modifiable ordered collection containing the provided integers.
     */
    protected abstract T createModifiableOrderedCollection(Integer... integers);

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
        ModifiableOrderedCollection<Integer> collection = createModifiableOrderedCollection();
        collection.addAt(0, 1);
        assertEquals(1, collection.getAt(0));
    }

    /**
     * Verifies that after adding an element at a position to a collection, the elements after the position have been
     * moved up.
     */
    @Test
    public void addAtOneOnACollectionShouldMoveElementsOnePositionHigher() {
        ModifiableOrderedCollection<Integer> collection = createModifiableOrderedCollection(1, 2, THREE);
        collection.addAt(1, 0);
        assertEquals(2, collection.getAt(2));
    }

    /**
     * Verifies that after adding an element at the last position to a collection, the new element is in the last
     * position.
     */
    @Test
    public void addAtLastPositionOnACollectionShouldPlaceElementAtLastPosition() {
        ModifiableOrderedCollection<Integer> collection = createModifiableOrderedCollection(1, 2, THREE);
        collection.addAt(THREE, 0);
        assertEquals(0, collection.getAt(THREE));
    }

    /**
     * Verifies that adding beyond the stride doesn't lead to an exception.
     */
    @Test
    public void addAtManyTimesShouldNotProduceAnException() {
        ModifiableOrderedCollection<Integer> collection = createModifiableOrderedCollection();
        for (int i = 0; i < SIX; i++) {
            collection.addAt(0, i);
        }
        assertEquals(0, collection.getAt(SIX - 1));
    }

    /**
     * Verifies that when you get an element from a collection by its index, the correct element is returned.
     */
    @Test
    public void getAtShouldReturnCorrectElement() {
        assertEquals(2, collection123.getAt(1));
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
}
