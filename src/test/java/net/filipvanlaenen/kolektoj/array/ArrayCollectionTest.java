package net.filipvanlaenen.kolektoj.array;

import net.filipvanlaenen.kolektoj.Collection.ElementCardinality;
import net.filipvanlaenen.kolektoj.CollectionTestBase;

/**
 * Unit tests on the {@link net.filipvanlaenen.kolektoj.array.ArrayCollection} class.
 */
public class ArrayCollectionTest extends CollectionTestBase<ArrayCollection<Integer>> {
    @Override
    protected ArrayCollection<Integer> createCollection(ArrayCollection<Integer> collection) {
        return new ArrayCollection<Integer>(collection);
    }

    @Override
    protected ArrayCollection<Integer> createCollection(ElementCardinality elementCardinality,
            ArrayCollection<Integer> collection) {
        return new ArrayCollection<Integer>(elementCardinality, collection);
    }

    @Override
    protected ArrayCollection<Integer> createCollection(ElementCardinality elementCardinality, Integer... integers) {
        return new ArrayCollection<Integer>(elementCardinality, integers);
    }

    @Override
    protected ArrayCollection<Integer> createCollection(Integer... integers) {
        return new ArrayCollection<Integer>(integers);
    }
}
