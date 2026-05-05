package net.filipvanlaenen.kolektoj.array;

import net.filipvanlaenen.kolektoj.Collection.ElementCardinality;
import net.filipvanlaenen.kolektoj.CollectionTestBase;
import net.filipvanlaenen.kolektoj.CollectionTestBase.ElementWithCollidingHash;

/**
 * Unit tests on the {@link net.filipvanlaenen.kolektoj.array.ArrayCollection} class.
 */
public final class ArrayCollectionTest
        extends CollectionTestBase<ArrayCollection<Integer>, ArrayCollection<ElementWithCollidingHash>> {
    @Override
    protected ArrayCollection<Integer> createCollection(final ArrayCollection<Integer> collection) {
        return new ArrayCollection<Integer>(collection);
    }

    @Override
    protected ArrayCollection<Integer> createCollection(final ElementCardinality elementCardinality,
            final ArrayCollection<Integer> collection) {
        return new ArrayCollection<Integer>(elementCardinality, collection);
    }

    @Override
    protected ArrayCollection<Integer> createCollection(final ElementCardinality elementCardinality,
            final Integer... integers) {
        return new ArrayCollection<Integer>(elementCardinality, integers);
    }

    @Override
    protected ArrayCollection<Integer> createCollection(final Integer... integers) {
        return new ArrayCollection<Integer>(integers);
    }

    @Override
    protected ArrayCollection<ElementWithCollidingHash> createCollidingHashValuesCollection(
            final ElementWithCollidingHash... elements) {
        return new ArrayCollection<ElementWithCollidingHash>(elements);
    }
}
