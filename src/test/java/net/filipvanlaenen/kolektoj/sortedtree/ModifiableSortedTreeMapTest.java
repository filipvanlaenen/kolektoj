package net.filipvanlaenen.kolektoj.sortedtree;

import net.filipvanlaenen.kolektoj.Map.Entry;
import net.filipvanlaenen.kolektoj.Map.KeyAndValueCardinality;
import net.filipvanlaenen.kolektoj.MapTestBase.KeyWithCollidingHash;

import java.util.Comparator;
import java.util.Objects;

import net.filipvanlaenen.kolektoj.ModifiableSortedMapTestBase;

/**
 * Unit tests on the {@link net.filipvanlaenen.kolektoj.sortedtree.ModifiableSortedTreeMap} class.
 */
public class ModifiableSortedTreeMapTest extends
        ModifiableSortedMapTestBase<ModifiableSortedTreeMap<Integer, String>, ModifiableSortedTreeMap<KeyWithCollidingHash, Integer>> {

    /**
     * A comparator ordering integers in the natural order, but in addition handling <code>null</code> as the lowest
     * value.
     */
    private static final Comparator<Integer> COMPARATOR = new Comparator<Integer>() {
        @Override
        public int compare(final Integer i1, final Integer i2) {
            if (Objects.equals(i1, i2)) {
                return 0;
            } else if (i1 == null) {
                return -1;
            } else if (i2 == null) {
                return 1;
            } else if (i1 < i2) {
                return -1;
            } else {
                return 1;
            }
        }
    };

    @Override
    protected ModifiableSortedTreeMap<Integer, String> createMap(Entry<Integer, String>... entries) {
        return new ModifiableSortedTreeMap<Integer, String>(COMPARATOR, entries);
    }

    @Override
    protected ModifiableSortedTreeMap<Integer, String> createMap(KeyAndValueCardinality keyAndValueCardinality,
            Entry<Integer, String>... entries) {
        return new ModifiableSortedTreeMap<Integer, String>(keyAndValueCardinality, COMPARATOR, entries);
    }

    @Override
    protected ModifiableSortedTreeMap<KeyWithCollidingHash, Integer> createCollidingKeyHashMap(
            Entry<KeyWithCollidingHash, Integer>... entries) {
        return new ModifiableSortedTreeMap<KeyWithCollidingHash, Integer>(new Comparator<KeyWithCollidingHash>() {
            @Override
            public int compare(KeyWithCollidingHash o1, KeyWithCollidingHash o2) {
                if (Objects.equals(o1, o2)) {
                    return 0;
                } else if (o1 == null) {
                    return -1;
                } else if (o2 == null) {
                    return 1;
                } else {
                    return o1.toString().compareTo(o2.toString());
                }
            }
        }, entries);
    }
}
