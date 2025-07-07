package net.filipvanlaenen.kolektoj.array;

import net.filipvanlaenen.kolektoj.Collection.ElementCardinality;
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
}
