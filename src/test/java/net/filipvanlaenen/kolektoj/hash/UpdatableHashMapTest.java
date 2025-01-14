package net.filipvanlaenen.kolektoj.hash;

import net.filipvanlaenen.kolektoj.Map.Entry;
import net.filipvanlaenen.kolektoj.Map.KeyAndValueCardinality;
import net.filipvanlaenen.kolektoj.MapTestBase.KeyWithCollidingHash;
import net.filipvanlaenen.kolektoj.UpdatableMapTestBase;

/**
 * Unit tests on the {@link net.filipvanlaenen.kolektoj.hash.UpdatableHashMap} class.
 */
public final class UpdatableHashMapTest extends
        UpdatableMapTestBase<UpdatableHashMap<Integer, String>, UpdatableHashMap<KeyWithCollidingHash, Integer>> {
    @Override
    protected UpdatableHashMap<Integer, String> createMap(final Entry<Integer, String>... entries) {
        return new UpdatableHashMap<Integer, String>(entries);
    }

    @Override
    protected UpdatableHashMap<Integer, String> createMap(final KeyAndValueCardinality keyAndValueCardinality,
            final Entry<Integer, String>... entries) {
        return new UpdatableHashMap<Integer, String>(keyAndValueCardinality, entries);
    }

    @Override
    protected UpdatableHashMap<KeyWithCollidingHash, Integer> createCollidingKeyHashMap(
            final Entry<KeyWithCollidingHash, Integer>... entries) {
        return new UpdatableHashMap<KeyWithCollidingHash, Integer>(entries);
    }
}
