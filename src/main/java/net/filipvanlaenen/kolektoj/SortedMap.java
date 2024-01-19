package net.filipvanlaenen.kolektoj;

import net.filipvanlaenen.kolektoj.Map.Entry;

/**
 * Interface defining the signature for all sorted maps.
 *
 * @param <K> The key type.
 * @param <V> The value type.
 */
public interface SortedMap<K, V> extends Collection<Entry<K, V>>, Map<K, V> {
}
