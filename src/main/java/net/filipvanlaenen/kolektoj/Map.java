package net.filipvanlaenen.kolektoj;

import net.filipvanlaenen.kolektoj.Map.Entry;
import net.filipvanlaenen.kolektoj.hash.HashMap;

/**
 * Interface defining the signature for all maps.
 *
 * @param <K> The key type.
 * @param <V> The value type.
 */
public interface Map<K, V> extends Collection<Entry<K, V>> {
    public enum KeyAndValueCardinality {
        DISTINCT_KEYS, DUPLICATE_KEYS_WITH_DISTINCT_VALUES, DUPLICATE_KEYS_WITH_DUPLICATE_VALUES
    }

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
        return new HashMap<K, V>();
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
        return new HashMap<K, V>(entries);
    }

    /**
     * Returns a new map containing an entry with the key and the value.
     *
     * @param <K>   The key type.
     * @param <V>   The value type.
     * @param key   The key for the entry.
     * @param value The value for the entry.
     * @return A new map containing an entry with the key and the value.
     */
    static <K, V> Map<K, V> of(final K key, final V value) {
        return new HashMap<K, V>(new Entry<K, V>(key, value));
    }

    /**
     * Returns a new map containing two entries using the provided keys and values.
     *
     * @param <K>    The key type.
     * @param <V>    The value type.
     * @param key1   The first key for the entry.
     * @param value1 The first value for the entry.
     * @param key2   The second key for the entry.
     * @param value2 The second value for the entry.
     * @return A new map containing two entries using the provided keys and values.
     */
    static <K, V> Map<K, V> of(final K key1, final V value1, final K key2, final V value2) {
        return new HashMap<K, V>(new Entry<K, V>(key1, value1), new Entry<K, V>(key2, value2));
    }

    /**
     * Returns a new map containing three entries using the provided keys and values.
     *
     * @param <K>    The key type.
     * @param <V>    The value type.
     * @param key1   The first key for the entry.
     * @param value1 The first value for the entry.
     * @param key2   The second key for the entry.
     * @param value2 The second value for the entry.
     * @param key3   The third key for the entry.
     * @param value3 The third value for the entry.
     * @return A new map containing three entries using the provided keys and values.
     */
    static <K, V> Map<K, V> of(final K key1, final V value1, final K key2, final V value2, final K key3,
            final V value3) {
        return new HashMap<K, V>(new Entry<K, V>(key1, value1), new Entry<K, V>(key2, value2),
                new Entry<K, V>(key3, value3));
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
