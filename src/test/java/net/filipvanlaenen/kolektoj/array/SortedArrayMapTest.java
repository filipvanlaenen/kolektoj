package net.filipvanlaenen.kolektoj.array;

import net.filipvanlaenen.kolektoj.Map.Entry;
import net.filipvanlaenen.kolektoj.Map.KeyAndValueCardinality;
import net.filipvanlaenen.kolektoj.MapTestBase.KeyWithCollidingHash;
import net.filipvanlaenen.kolektoj.SortedMapTestBase;

/**
 * Unit tests on the {@link net.filipvanlaenen.kolektoj.array.SortedArrayMap} class.
 */
public final class SortedArrayMapTest
        extends SortedMapTestBase<SortedArrayMap<Integer, String>, SortedArrayMap<KeyWithCollidingHash, Integer>> {
    @Override
    protected SortedArrayMap<Integer, String> createMap(final Entry<Integer, String>... entries) {
        return new SortedArrayMap<Integer, String>(COMPARATOR, entries);
    }

    @Override
    protected SortedArrayMap<Integer, String> createMap(final KeyAndValueCardinality keyAndValueCardinality,
            final Entry<Integer, String>... entries) {
        return new SortedArrayMap<Integer, String>(keyAndValueCardinality, COMPARATOR, entries);
    }

    @Override
    protected SortedArrayMap<KeyWithCollidingHash, Integer> createCollidingKeyHashMap(
            final Entry<KeyWithCollidingHash, Integer>... entries) {
        return new SortedArrayMap<KeyWithCollidingHash, Integer>(KEY_WITH_COLLIDING_HASH_COMPARATOR, entries);
    }
}
