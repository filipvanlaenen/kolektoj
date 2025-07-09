package net.filipvanlaenen.kolektoj.array;

import net.filipvanlaenen.kolektoj.Map.Entry;
import net.filipvanlaenen.kolektoj.Map.KeyAndValueCardinality;
import net.filipvanlaenen.kolektoj.MapTestBase.KeyWithCollidingHash;
import net.filipvanlaenen.kolektoj.UpdatableSortedMapTestBase;

/**
 * Unit tests on the {@link net.filipvanlaenen.kolektoj.array.UpdatableSortedArrayMap} class.
 */
public final class UpdatableSortedArrayMapTest extends UpdatableSortedMapTestBase<
        UpdatableSortedArrayMap<Integer, String>, UpdatableSortedArrayMap<KeyWithCollidingHash, Integer>> {
    @Override
    protected UpdatableSortedArrayMap<Integer, String> createMap(final Entry<Integer, String>... entries) {
        return new UpdatableSortedArrayMap<Integer, String>(COMPARATOR, entries);
    }

    @Override
    protected UpdatableSortedArrayMap<Integer, String> createMap(final KeyAndValueCardinality keyAndValueCardinality,
            final Entry<Integer, String>... entries) {
        return new UpdatableSortedArrayMap<Integer, String>(keyAndValueCardinality, COMPARATOR, entries);
    }

    @Override
    protected UpdatableSortedArrayMap<KeyWithCollidingHash, Integer> createCollidingKeyHashMap(
            final Entry<KeyWithCollidingHash, Integer>... entries) {
        return new UpdatableSortedArrayMap<KeyWithCollidingHash, Integer>(KEY_WITH_COLLIDING_HASH_COMPARATOR, entries);
    }
}
