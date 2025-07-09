package net.filipvanlaenen.kolektoj.sortedtree;

import net.filipvanlaenen.kolektoj.Map.Entry;
import net.filipvanlaenen.kolektoj.Map.KeyAndValueCardinality;
import net.filipvanlaenen.kolektoj.MapTestBase.KeyWithCollidingHash;
import net.filipvanlaenen.kolektoj.UpdatableSortedMapTestBase;

/**
 * Unit tests on the {@link net.filipvanlaenen.kolektoj.sortedtree.UpdatableSortedTreeMap} class.
 */
public final class UpdatableSortedTreeMapTest extends UpdatableSortedMapTestBase<
        UpdatableSortedTreeMap<Integer, String>, UpdatableSortedTreeMap<KeyWithCollidingHash, Integer>> {
    @Override
    protected UpdatableSortedTreeMap<Integer, String> createMap(final Entry<Integer, String>... entries) {
        return new UpdatableSortedTreeMap<Integer, String>(COMPARATOR, entries);
    }

    @Override
    protected UpdatableSortedTreeMap<Integer, String> createMap(final KeyAndValueCardinality keyAndValueCardinality,
            final Entry<Integer, String>... entries) {
        return new UpdatableSortedTreeMap<Integer, String>(keyAndValueCardinality, COMPARATOR, entries);
    }

    @Override
    protected UpdatableSortedTreeMap<KeyWithCollidingHash, Integer> createCollidingKeyHashMap(
            final Entry<KeyWithCollidingHash, Integer>... entries) {
        return new UpdatableSortedTreeMap<KeyWithCollidingHash, Integer>(KEY_WITH_COLLIDING_HASH_COMPARATOR, entries);
    }
}
