package net.filipvanlaenen.kolektoj.array;

import net.filipvanlaenen.kolektoj.Collection.ElementCardinality;
import net.filipvanlaenen.kolektoj.CollectionTestBase.ElementWithCollidingHash;
import net.filipvanlaenen.kolektoj.ModifiableOrderedCollectionTestBase;

/**
 * Unit tests on the {@link net.filipvanlaenen.kolektoj.array.ModifiableOrderedArrayCollection} class.
 */
public final class ModifiableOrderedArrayCollectionTest extends ModifiableOrderedCollectionTestBase<
        ModifiableOrderedArrayCollection<Integer>, ModifiableOrderedArrayCollection<ElementWithCollidingHash>> {
    @Override
    protected ModifiableOrderedArrayCollection<Integer> createCollection(
            final ModifiableOrderedArrayCollection<Integer> collection) {
        return new ModifiableOrderedArrayCollection<Integer>(collection);
    }

    @Override
    protected ModifiableOrderedArrayCollection<Integer> createCollection(final ElementCardinality elementCardinality,
            final ModifiableOrderedArrayCollection<Integer> collection) {
        return new ModifiableOrderedArrayCollection<Integer>(elementCardinality, collection);
    }

    @Override
    protected ModifiableOrderedArrayCollection<ElementWithCollidingHash> createCollidingHashValuesCollection(
            final ElementWithCollidingHash... elements) {
        return new ModifiableOrderedArrayCollection<ElementWithCollidingHash>(elements);
    }

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

    @Override
    protected ModifiableOrderedArrayCollection<Integer> createModifiableCollection(
            final ElementCardinality elementCardinality, final ModifiableOrderedArrayCollection<Integer> integers) {
        return new ModifiableOrderedArrayCollection<Integer>(elementCardinality, integers);
    }
}
