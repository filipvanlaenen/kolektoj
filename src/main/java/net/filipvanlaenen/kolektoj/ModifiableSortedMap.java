package net.filipvanlaenen.kolektoj;

import net.filipvanlaenen.kolektoj.Map.Entry;

/**
 * Interface defining the signature for all modifiable sorted maps.
 *
 * @param <K> The key type.
 * @param <V> The value type.
 */
public interface ModifiableSortedMap<K, V>
        extends Collection<Entry<K, V>>, ModifiableMap<K, V>, UpdatableSortedMap<K, V> {
}
