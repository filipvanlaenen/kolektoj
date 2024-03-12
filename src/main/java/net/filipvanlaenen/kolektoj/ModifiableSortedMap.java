package net.filipvanlaenen.kolektoj;

import java.util.Comparator;

import net.filipvanlaenen.kolektoj.Map.Entry;
import net.filipvanlaenen.kolektoj.sortedtree.ModifiableSortedTreeMap;

/**
 * Interface defining the signature for all modifiable sorted maps.
 *
 * @param <K> The key type.
 * @param <V> The value type.
 */
public interface ModifiableSortedMap<K, V>
        extends Collection<Entry<K, V>>, ModifiableMap<K, V>, UpdatableSortedMap<K, V> {
    /**
     * Returns a new empty modifiable sorted map.
     *
     * @param <K> The key type.
     * @param <V> The value type.
     * @return A new empty modifiable sorted map.
     */
    static <K, V> ModifiableSortedMap<K, V> empty(final Comparator<K> comparator) {
        return new ModifiableSortedTreeMap<K, V>(comparator);
    }
}
