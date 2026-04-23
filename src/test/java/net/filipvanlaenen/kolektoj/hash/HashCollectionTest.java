package net.filipvanlaenen.kolektoj.hash;

import net.filipvanlaenen.kolektoj.Collection.ElementCardinality;
import net.filipvanlaenen.kolektoj.CollectionTestBase;

/**
 * Unit tests on the {@link net.filipvanlaenen.kolektoj.hash.HashCollection} class.
 */
public class HashCollectionTest extends CollectionTestBase<HashCollection<Integer>> {
    @Override
    protected HashCollection<Integer> createCollection(Integer... integers) {
        return new HashCollection<Integer>(integers);
    }

    @Override
    protected HashCollection<Integer> createCollection(ElementCardinality elementCardinality, Integer... integers) {
        return new HashCollection<Integer>(elementCardinality, integers);
    }

    @Override
    protected HashCollection<Integer> createCollection(HashCollection<Integer> collection) {
        return new HashCollection<Integer>(collection);
    }

    @Override
    protected HashCollection<Integer> createCollection(ElementCardinality elementCardinality,
            HashCollection<Integer> collection) {
        return new HashCollection<Integer>(elementCardinality, collection);
    }
}
