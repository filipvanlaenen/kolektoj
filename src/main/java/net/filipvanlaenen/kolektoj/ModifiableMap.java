package net.filipvanlaenen.kolektoj;

import net.filipvanlaenen.kolektoj.Map.Entry;

/**
 * Interface defining the signature for all modifiable maps.
 *
 * @param <K> The key type.
 * @param <V> The value type.
 */
public interface ModifiableMap<K, V> extends Collection<Entry<K, V>>, Map<K, V> {
    /**
     * Adds an entry to this map with the given key and value, and returns whether it increased the size of the map.
     *
     * @param key   The key.
     * @param value The value.
     * @return True if the size of the map increased after adding an entry with the key and the value.
     */
    boolean add(K key, V value);

    /**
     * Removes an entry from this map with the given key, and returns the value that was mapped to the key. Throws an
     * exception if the map doesn't contain an entry with the key.
     *
     * @param key The key.
     * @return The value that was mapped to the key.
     * @throws IllegalArgumentException Thrown if the map doesn't contain an entry with the key.
     */
    V remove(K key) throws IllegalArgumentException;
}
