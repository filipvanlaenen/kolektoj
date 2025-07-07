package net.filipvanlaenen.kolektoj.array;

import static net.filipvanlaenen.kolektoj.Collection.ElementCardinality.DISTINCT_ELEMENTS;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Spliterator;

import org.junit.jupiter.api.Test;

import net.filipvanlaenen.kolektoj.Collection.ElementCardinality;
import net.filipvanlaenen.kolektoj.ModifiableOrderedCollection;
import net.filipvanlaenen.kolektoj.ModifiableOrderedCollectionTestBase;
import net.filipvanlaenen.kolektoj.OrderedCollection;

/**
 * Unit tests on the {@link net.filipvanlaenen.kolektoj.array.ModifiableOrderedArrayCollection} class.
 */
public final class ModifiableOrderedArrayCollectionTest
        extends ModifiableOrderedCollectionTestBase<ModifiableOrderedArrayCollection<Integer>> {
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
    protected ModifiableOrderedArrayCollection<Integer> createModifiableCollection(
            final ElementCardinality elementCardinality, final Integer... integers) {
        return createModifiableOrderedCollection(elementCardinality, integers);
    }

    @Override
    protected ModifiableOrderedArrayCollection<Integer> createModifiableCollection(final Integer... integers) {
        return createModifiableOrderedCollection(integers);
    }

    @Override
    protected ModifiableOrderedArrayCollection<Integer> createModifiableOrderedCollection(
            final ElementCardinality elementCardinality, final Integer... integers) {
        return new ModifiableOrderedArrayCollection<Integer>(elementCardinality, integers);
    }

    @Override
    protected ModifiableOrderedArrayCollection<Integer> createModifiableOrderedCollection(final Integer... integers) {
        return new ModifiableOrderedArrayCollection<Integer>(integers);
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
     * Verifies that adding the elements of a collection to an empty collection at the first position returns true.
     */
    @Test
    public void addAllAtOnAnEmptyCollectionShouldReturnTrue() {
        assertTrue(new ModifiableOrderedArrayCollection<Integer>().addAllAt(0, OrderedCollection.of(1, 2)));
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
                () -> new ModifiableOrderedArrayCollection<Integer>().addAllAt(1, OrderedCollection.of(1, 2)));
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
