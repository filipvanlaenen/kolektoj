package net.filipvanlaenen.kolektoj.hash;

import net.filipvanlaenen.kolektoj.Map.Entry;
import net.filipvanlaenen.kolektoj.Map.KeyAndValueCardinality;
import net.filipvanlaenen.kolektoj.MapTestBase;
import net.filipvanlaenen.kolektoj.MapTestBase.KeyWithCollidingHash;

/**
 * Unit tests on the {@link net.filipvanlaenen.kolektoj.hash.HashMap} class.
 */
public final class HashMapTest
        extends MapTestBase<HashMap<Integer, String>, HashMap<KeyWithCollidingHash, Integer>> {
    @Override
    protected HashMap<Integer, String> createMap(final Entry<Integer, String>... entries) {
        return new HashMap<Integer, String>(entries);
    }

    @Override
    protected HashMap<Integer, String> createMap(final KeyAndValueCardinality keyAndValueCardinality,
            final Entry<Integer, String>... entries) {
        return new HashMap<Integer, String>(keyAndValueCardinality, entries);
    }

    @Override
    protected HashMap<KeyWithCollidingHash, Integer> createCollidingKeyHashMap(
            final Entry<KeyWithCollidingHash, Integer>... entries) {
        return new HashMap<KeyWithCollidingHash, Integer>(entries);
    }
}
