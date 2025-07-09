package net.filipvanlaenen.kolektoj.sortedtree;

import net.filipvanlaenen.kolektoj.Map.Entry;
import net.filipvanlaenen.kolektoj.Map.KeyAndValueCardinality;
import net.filipvanlaenen.kolektoj.MapTestBase.KeyWithCollidingHash;
import net.filipvanlaenen.kolektoj.ModifiableSortedMapTestBase;

/**
 * Unit tests on the {@link net.filipvanlaenen.kolektoj.sortedtree.ModifiableSortedTreeMap} class.
 */
public final class ModifiableSortedTreeMapTest extends ModifiableSortedMapTestBase<
        ModifiableSortedTreeMap<Integer, String>, ModifiableSortedTreeMap<KeyWithCollidingHash, Integer>> {
    @Override
    protected ModifiableSortedTreeMap<Integer, String> createMap(final Entry<Integer, String>... entries) {
        return new ModifiableSortedTreeMap<Integer, String>(COMPARATOR, entries);
    }

    @Override
    protected ModifiableSortedTreeMap<Integer, String> createMap(final KeyAndValueCardinality keyAndValueCardinality,
            final Entry<Integer, String>... entries) {
        return new ModifiableSortedTreeMap<Integer, String>(keyAndValueCardinality, COMPARATOR, entries);
    }

    @Override
    protected ModifiableSortedTreeMap<KeyWithCollidingHash, Integer> createCollidingKeyHashMap(
            final Entry<KeyWithCollidingHash, Integer>... entries) {
        return new ModifiableSortedTreeMap<KeyWithCollidingHash, Integer>(KEY_WITH_COLLIDING_HASH_COMPARATOR, entries);
    }
}
