package net.filipvanlaenen.kolektoj.array;

import net.filipvanlaenen.kolektoj.Collection.ElementCardinality;
import net.filipvanlaenen.kolektoj.CollectionTestBase.ElementWithCollidingHash;
import net.filipvanlaenen.kolektoj.OrderedCollectionTestBase;

/**
 * Unit tests on the {@link net.filipvanlaenen.kolektoj.array.OrderedArrayCollection} class.
 */
public final class OrderedArrayCollectionTest extends
        OrderedCollectionTestBase<OrderedArrayCollection<Integer>, OrderedArrayCollection<ElementWithCollidingHash>> {
    @Override
    protected OrderedArrayCollection<Integer> createCollection(final OrderedArrayCollection<Integer> collection) {
        return new OrderedArrayCollection<Integer>(collection);
    }

    @Override
    protected OrderedArrayCollection<Integer> createCollection(final ElementCardinality elementCardinality,
            final OrderedArrayCollection<Integer> collection) {
        return new OrderedArrayCollection<Integer>(elementCardinality, collection);
    }

    @Override
    protected OrderedArrayCollection<ElementWithCollidingHash> createCollidingHashValuesCollection(
            final ElementWithCollidingHash... elements) {
        return new OrderedArrayCollection<ElementWithCollidingHash>(elements);
    }

    @Override
    protected OrderedArrayCollection<Integer> createOrderedCollection(final Integer... integers) {
        return new OrderedArrayCollection<Integer>(integers);
    }

    @Override
    protected OrderedArrayCollection<Integer> createOrderedCollection(final ElementCardinality elementCardinality,
            final Integer... integers) {
        return new OrderedArrayCollection<Integer>(elementCardinality, integers);
    }

    @Override
    protected OrderedArrayCollection<Integer> createOrderedCollection(
            final OrderedArrayCollection<Integer> collection) {
        return new OrderedArrayCollection<Integer>(collection);
    }
}
