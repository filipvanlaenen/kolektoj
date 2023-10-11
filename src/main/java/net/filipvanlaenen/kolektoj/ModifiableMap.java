package net.filipvanlaenen.kolektoj;

import net.filipvanlaenen.kolektoj.Map.Entry;
import net.filipvanlaenen.kolektoj.hash.ModifiableHashMap;

/**
 * Interface defining the signature for all modifiable maps.
 *
 * @param <K> The key type.
 * @param <V> The value type.
 */
public interface ModifiableMap<K, V> extends Collection<Entry<K, V>>, Map<K, V> {
    /**
     * Returns a new empty map.
     *
     * @param <E> The element type.
     * @return A new empty map.
     */
    static <K, V> ModifiableMap<K, V> empty() {
        return new ModifiableHashMap<K, V>();
    }

    /**
     * Adds an entry to this map with the given key and value, and returns whether it increased the size of the map.
     *
     * @param key   The key.
     * @param value The value.
     * @return True if the size of the map increased after adding an entry with the key and the value.
     */
    boolean add(K key, V value);

    /**
     * Adds an entry to this map with the given key and value if the key isn't already present, otherwise updates the
     * key with the given value. If the key was previously mapped to a value, the value is returned, and null otherwise.
     * 
     * @param key   The key.
     * @param value The value.
     * @return The value if the key was previously mapped to a value, or null otherwise.
     */
    default V put(K key, V value) {
        if (containsKey(key)) {
            return update(key, value);
        } else {
            add(key, value);
            return null;
        }
    }

    /**
     * Removes an entry from this map with the given key, and returns the value that was mapped to the key. Throws an
     * exception if the map doesn't contain an entry with the key.
     *
     * @param key The key.
     * @return The value that was mapped to the key.
     * @throws IllegalArgumentException Thrown if the map doesn't contain an entry with the key.
     */
    V remove(K key) throws IllegalArgumentException;

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
