package net.filipvanlaenen.kolektoj.linkedlist;

import net.filipvanlaenen.kolektoj.Collection.ElementCardinality;
import net.filipvanlaenen.kolektoj.CollectionTestBase.ElementWithCollidingHash;
import net.filipvanlaenen.kolektoj.OrderedCollectionTestBase;

/**
 * Unit tests on the {@link net.filipvanlaenen.kolektoj.linkedlist.OrderedLinkedListCollection} class.
 */
public final class OrderedLinkedListCollectionTest extends OrderedCollectionTestBase<
        OrderedLinkedListCollection<Integer>, OrderedLinkedListCollection<ElementWithCollidingHash>> {
    @Override
    protected OrderedLinkedListCollection<Integer> createCollection(
            final OrderedLinkedListCollection<Integer> collection) {
        return new OrderedLinkedListCollection<Integer>(collection);
    }

    @Override
    protected OrderedLinkedListCollection<Integer> createCollection(final ElementCardinality elementCardinality,
            final OrderedLinkedListCollection<Integer> collection) {
        return new OrderedLinkedListCollection<Integer>(elementCardinality, collection);
    }

    @Override
    protected OrderedLinkedListCollection<ElementWithCollidingHash> createCollidingHashValuesCollection(
            final ElementWithCollidingHash... elements) {
        return new OrderedLinkedListCollection<ElementWithCollidingHash>(elements);
    }

    @Override
    protected OrderedLinkedListCollection<Integer> createOrderedCollection(final Integer... integers) {
        return new OrderedLinkedListCollection<Integer>(integers);
    }

    @Override
    protected OrderedLinkedListCollection<Integer> createOrderedCollection(final ElementCardinality elementCardinality,
            final Integer... integers) {
        return new OrderedLinkedListCollection<Integer>(elementCardinality, integers);
    }

    @Override
    protected OrderedLinkedListCollection<Integer> createOrderedCollection(
            final OrderedLinkedListCollection<Integer> collection) {
        return new OrderedLinkedListCollection<Integer>(collection);
    }
}
