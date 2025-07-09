package net.filipvanlaenen.kolektoj.sortedtree;

import net.filipvanlaenen.kolektoj.Map.Entry;
import net.filipvanlaenen.kolektoj.Map.KeyAndValueCardinality;
import net.filipvanlaenen.kolektoj.MapTestBase.KeyWithCollidingHash;
import net.filipvanlaenen.kolektoj.SortedMapTestBase;

/**
 * Unit tests on the {@link net.filipvanlaenen.kolektoj.sortedtree.SortedTreeMap} class.
 */
public final class SortedTreeMapTest
        extends SortedMapTestBase<SortedTreeMap<Integer, String>, SortedTreeMap<KeyWithCollidingHash, Integer>> {
    @Override
    protected SortedTreeMap<Integer, String> createMap(final Entry<Integer, String>... entries) {
        return new SortedTreeMap<Integer, String>(COMPARATOR, entries);
    }

    @Override
    protected SortedTreeMap<Integer, String> createMap(final KeyAndValueCardinality keyAndValueCardinality,
            final Entry<Integer, String>... entries) {
        return new SortedTreeMap<Integer, String>(keyAndValueCardinality, COMPARATOR, entries);
    }

    @Override
    protected SortedTreeMap<KeyWithCollidingHash, Integer> createCollidingKeyHashMap(
            final Entry<KeyWithCollidingHash, Integer>... entries) {
        return new SortedTreeMap<KeyWithCollidingHash, Integer>(KEY_WITH_COLLIDING_HASH_COMPARATOR, entries);
    }
}
