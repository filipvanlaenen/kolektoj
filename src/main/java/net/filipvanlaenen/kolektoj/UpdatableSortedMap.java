package net.filipvanlaenen.kolektoj;

import net.filipvanlaenen.kolektoj.Map.Entry;

/**
 * Interface defining the signature for all updatable sorted maps.
 *
 * @param <K> The key type.
 * @param <V> The value type.
 */
public interface UpdatableSortedMap<K, V> extends Collection<Entry<K, V>>, UpdatableMap<K, V>, SortedMap<K, V> {
}
