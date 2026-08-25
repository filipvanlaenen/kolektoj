package net.filipvanlaenen.kolektoj.linkedlist;

import net.filipvanlaenen.kolektoj.Collection.ElementCardinality;
import net.filipvanlaenen.kolektoj.CollectionTestBase;
import net.filipvanlaenen.kolektoj.CollectionTestBase.ElementWithCollidingHash;

/**
 * Unit tests on the {@link net.filipvanlaenen.kolektoj.linkedlist.LinkedListCollection} class.
 */
public final class LinkedListCollectionTest
        extends CollectionTestBase<LinkedListCollection<Integer>, LinkedListCollection<ElementWithCollidingHash>> {
    @Override
    protected LinkedListCollection<Integer> createCollection(final LinkedListCollection<Integer> collection) {
        return new LinkedListCollection<Integer>(collection);
    }

    @Override
    protected LinkedListCollection<Integer> createCollection(final ElementCardinality elementCardinality,
            final LinkedListCollection<Integer> collection) {
        return new LinkedListCollection<Integer>(elementCardinality, collection);
    }

    @Override
    protected LinkedListCollection<Integer> createCollection(final ElementCardinality elementCardinality,
            final Integer... integers) {
        return new LinkedListCollection<Integer>(elementCardinality, integers);
    }

    @Override
    protected LinkedListCollection<Integer> createCollection(final Integer... integers) {
        return new LinkedListCollection<Integer>(integers);
    }

    @Override
    protected LinkedListCollection<ElementWithCollidingHash> createCollidingHashValuesCollection(
            final ElementWithCollidingHash... elements) {
        return new LinkedListCollection<ElementWithCollidingHash>(elements);
    }
}
