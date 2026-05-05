package net.filipvanlaenen.kolektoj.hash;

import net.filipvanlaenen.kolektoj.Collection.ElementCardinality;
import net.filipvanlaenen.kolektoj.CollectionTestBase;
import net.filipvanlaenen.kolektoj.CollectionTestBase.ElementWithCollidingHash;

/**
 * Unit tests on the {@link net.filipvanlaenen.kolektoj.hash.HashCollection} class.
 */
public final class HashCollectionTest
        extends CollectionTestBase<HashCollection<Integer>, HashCollection<ElementWithCollidingHash>> {
    @Override
    protected HashCollection<Integer> createCollection(final ElementCardinality elementCardinality,
            final HashCollection<Integer> collection) {
        return new HashCollection<Integer>(elementCardinality, collection);
    }

    @Override
    protected HashCollection<Integer> createCollection(final ElementCardinality elementCardinality,
            final Integer... integers) {
        return new HashCollection<Integer>(elementCardinality, integers);
    }

    @Override
    protected HashCollection<Integer> createCollection(final HashCollection<Integer> collection) {
        return new HashCollection<Integer>(collection);
    }

    @Override
    protected HashCollection<Integer> createCollection(final Integer... integers) {
        return new HashCollection<Integer>(integers);
    }

    @Override
    protected HashCollection<ElementWithCollidingHash> createCollidingHashValuesCollection(
            final ElementWithCollidingHash... elements) {
        return new HashCollection<ElementWithCollidingHash>(elements);
    }
}
