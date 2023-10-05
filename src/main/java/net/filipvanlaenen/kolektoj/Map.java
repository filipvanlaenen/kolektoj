package net.filipvanlaenen.kolektoj;

import net.filipvanlaenen.kolektoj.Map.Entry;
import net.filipvanlaenen.kolektoj.array.ArrayMap;

/**
 * Interface defining the signature for all maps.
 *
 * @param <K> The key type.
 * @param <V> The value type.
 */
public interface Map<K, V> extends Collection<Entry<K, V>> {
    /**
     * An entry in a map.
     *
     * @param <K>   The key type.
     * @param <V>   The value type.
     * @param key   The key.
     * @param value The value.
     */
    public record Entry<K, V>(K key, V value) {
    }

    /**
     * Returns a new empty map.
     *
     * @param <K> The key type.
     * @param <V> The value type.
     * @return A new empty map.
     */
    static <K, V> Map<K, V> empty() {
        return new ArrayMap<K, V>();
    }

    /**
     * Returns a new map with the specified entries.
     *
     * @param <K>     The key type.
     * @param <V>     The value type.
     * @param entries The entries for the new map.
     * @return A new map with the specified entries.
     */
    static <K, V> Map<K, V> of(final Entry<K, V>... entries) {
        return new ArrayMap<K, V>(entries);
    }

    /**
     * Returns whether the map contains an entry with the given key.
     *
     * @param key The key.
     * @return True if the map contains an entry with the given key.
     */
    boolean containsKey(K key);

    /**
     * Returns whether the map contains an entry with the given value.
     *
     * @param value The value.
     * @return True if the map contains an entry with the given value.
     */
    boolean containsValue(V value);

    /**
     * Returns a value mapped to the key. Throws an exception if the map doesn't contain an entry with the key.
     *
     * @param key The key.
     * @return A value mapped to the key.
     * @throws IllegalArgumentException Thrown if the map doesn't contain an entry with the key.
     */
    V get(K key) throws IllegalArgumentException;

    /**
     * Returns a collection with all values mapped to the key. Throws an exception if the map doesn't contain entries
     * with the key.
     *
     * @param key The key.
     * @return A collection with all the values mapped to the key.
     * @throws IllegalArgumentException Thrown if the map doesn't contain entries with the key.
     */
    Collection<V> getAll(K key) throws IllegalArgumentException;

    /**
     * Returns a collection with all the keys present in the map.
     *
     * @return A collection with all the keys present in the map.
     */
    Collection<K> getKeys();

    /**
     * Returns a collection with all the values present in the map.
     *
     * @return A collection with all the values present in the map.
     */
    Collection<V> getValues();
}
