package net.filipvanlaenen.kolektoj.array;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import org.junit.jupiter.api.Test;

import net.filipvanlaenen.kolektoj.Collection.ElementCardinality;
import net.filipvanlaenen.kolektoj.ModifiableOrderedCollection;
import net.filipvanlaenen.kolektoj.ModifiableOrderedCollectionTestBase;

/**
 * Unit tests on the {@link net.filipvanlaenen.kolektoj.array.ModifiableOrderedArrayCollection} class.
 */
public final class ModifiableOrderedArrayCollectionTest
        extends ModifiableOrderedCollectionTestBase<ModifiableOrderedArrayCollection<Integer>> {
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

}
