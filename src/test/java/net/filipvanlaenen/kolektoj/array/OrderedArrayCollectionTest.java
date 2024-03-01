package net.filipvanlaenen.kolektoj.array;

import net.filipvanlaenen.kolektoj.Collection.ElementCardinality;
import net.filipvanlaenen.kolektoj.OrderedCollectionTestBase;

/**
 * Unit tests on the {@link net.filipvanlaenen.kolektoj.array.OrderedArrayCollection} class.
 */
public final class OrderedArrayCollectionTest extends OrderedCollectionTestBase<OrderedArrayCollection<Integer>> {
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
