package net.filipvanlaenen.kolektoj.array;

import net.filipvanlaenen.kolektoj.Collection.ElementCardinality;
import net.filipvanlaenen.kolektoj.OrderedCollectionTestBase;

/**
 * Unit tests on the {@link net.filipvanlaenen.kolektoj.array.OrderedArrayCollection} class.
 */
public class OrderedArrayCollectionTest extends OrderedCollectionTestBase<OrderedArrayCollection<Integer>> {
    @Override
    protected OrderedArrayCollection<Integer> createOrderedCollection(Integer... integers) {
        return new OrderedArrayCollection<Integer>(integers);
    }

    @Override
    protected OrderedArrayCollection<Integer> createOrderedCollection(ElementCardinality elementCardinality,
            Integer... integers) {
        return new OrderedArrayCollection<Integer>(elementCardinality, integers);
    }

    @Override
    protected OrderedArrayCollection<Integer> createOrderedCollection(OrderedArrayCollection<Integer> collection) {
        return new OrderedArrayCollection<Integer>(collection);
    }
}
