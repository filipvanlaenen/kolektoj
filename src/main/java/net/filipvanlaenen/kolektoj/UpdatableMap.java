package net.filipvanlaenen.kolektoj;

import net.filipvanlaenen.kolektoj.Map.Entry;

/**
 * Interface defining the signature for all updatable maps. Updatable maps allow the values to be updated, but no new
 * keys to be added.
 * 
 *
 * @param <K> The key type.
 * @param <V> The value type.
 */
public interface UpdatableMap<K, V> extends Collection<Entry<K, V>>, Map<K, V> {
    /**
     * Updates the key with the given value, and returns the vale that was previously mapped to the key. Throws an
     * exception if the map doesn't contain an entry with the key.
     *
     * @param key   The key.
     * @param value The value.
     * @return The value the key was mapped to previously.
     * @throws IllegalArgumentException Thrown if the map doesn't contain an entry with the key.
     */
    V update(K key, V value) throws IllegalArgumentException;
}
