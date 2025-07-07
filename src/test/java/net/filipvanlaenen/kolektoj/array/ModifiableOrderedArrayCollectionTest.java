package net.filipvanlaenen.kolektoj.array;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import net.filipvanlaenen.kolektoj.Collection.ElementCardinality;
import net.filipvanlaenen.kolektoj.ModifiableOrderedCollection;
import net.filipvanlaenen.kolektoj.ModifiableOrderedCollectionTestBase;

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
     * Collection with the integers 1, 2 and 3.
     */
    private static final ModifiableOrderedCollection<Integer> COLLECTION123 = createNewCollection();

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
}
