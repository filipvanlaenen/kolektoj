package net.filipvanlaenen.kolektoj;

import net.filipvanlaenen.kolektoj.Map.Entry;
import net.filipvanlaenen.kolektoj.hash.UpdatableHashMap;

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
     * Returns a new empty updatable map.
     *
     * @param <K> The key type.
     * @param <V> The value type.
     * @return A new empty updatable map.
     */
    static <K, V> UpdatableMap<K, V> empty() {
        return new UpdatableHashMap<K, V>();
    }

    /**
     * Returns a new updatable map with the specified entries.
     *
     * @param <K>     The key type.
     * @param <V>     The value type.
     * @param entries The entries for the new updatable map.
     * @return A new updatable map with the specified entries.
     */
    static <K, V> UpdatableMap<K, V> of(final Entry<K, V>... entries) {
        return new UpdatableHashMap<K, V>(entries);
    }

    /**
     * Returns a new updatable map containing an entry with the key and the value.
     *
     * @param <K>   The key type.
     * @param <V>   The value type.
     * @param key   The key for the entry.
     * @param value The value for the entry.
     * @return A new updatable map containing an entry with the key and the value.
     */
    static <K, V> UpdatableMap<K, V> of(final K key, final V value) {
        return new UpdatableHashMap<K, V>(new Entry<K, V>(key, value));
    }

    /**
     * Returns a new updatable map containing two entries using the provided keys and values.
     *
     * @param <K>    The key type.
     * @param <V>    The value type.
     * @param key1   The first key for the entry.
     * @param value1 The first value for the entry.
     * @param key2   The second key for the entry.
     * @param value2 The second value for the entry.
     * @return A new updatable map containing two entries using the provided keys and values.
     */
    static <K, V> UpdatableMap<K, V> of(final K key1, final V value1, final K key2, final V value2) {
        return new UpdatableHashMap<K, V>(new Entry<K, V>(key1, value1), new Entry<K, V>(key2, value2));
    }

    /**
     * Returns a new updatable map containing three entries using the provided keys and values.
     *
     * @param <K>    The key type.
     * @param <V>    The value type.
     * @param key1   The first key for the entry.
     * @param value1 The first value for the entry.
     * @param key2   The second key for the entry.
     * @param value2 The second value for the entry.
     * @param key3   The third key for the entry.
     * @param value3 The third value for the entry.
     * @return A new updatable map containing three entries using the provided keys and values.
     */
    static <K, V> UpdatableMap<K, V> of(final K key1, final V value1, final K key2, final V value2, final K key3,
            final V value3) {
        return new UpdatableHashMap<K, V>(new Entry<K, V>(key1, value1), new Entry<K, V>(key2, value2),
                new Entry<K, V>(key3, value3));
    }

    /**
     * Returns a new updatable map containing four entries using the provided keys and values.
     *
     * @param <K>    The key type.
     * @param <V>    The value type.
     * @param key1   The first key for the entry.
     * @param value1 The first value for the entry.
     * @param key2   The second key for the entry.
     * @param value2 The second value for the entry.
     * @param key3   The third key for the entry.
     * @param value3 The third value for the entry.
     * @param key4   The fourth key for the entry.
     * @param value4 The fourth value for the entry.
     * @return A new updatable map containing four entries using the provided keys and values.
     */
    static <K, V> UpdatableMap<K, V> of(final K key1, final V value1, final K key2, final V value2, final K key3,
            final V value3, final K key4, final V value4) {
        return new UpdatableHashMap<K, V>(new Entry<K, V>(key1, value1), new Entry<K, V>(key2, value2),
                new Entry<K, V>(key3, value3), new Entry<K, V>(key4, value4));
    }

    /**
     * Returns a new updatable map containing five entries using the provided keys and values.
     *
     * @param <K>    The key type.
     * @param <V>    The value type.
     * @param key1   The first key for the entry.
     * @param value1 The first value for the entry.
     * @param key2   The second key for the entry.
     * @param value2 The second value for the entry.
     * @param key3   The third key for the entry.
     * @param value3 The third value for the entry.
     * @param key4   The fourth key for the entry.
     * @param value4 The fourth value for the entry.
     * @param key5   The fifth key for the entry.
     * @param value5 The fifth value for the entry.
     * @return A new updatable map containing five entries using the provided keys and values.
     */
    static <K, V> UpdatableMap<K, V> of(final K key1, final V value1, final K key2, final V value2, final K key3,
            final V value3, final K key4, final V value4, final K key5, final V value5) {
        return new UpdatableHashMap<K, V>(new Entry<K, V>(key1, value1), new Entry<K, V>(key2, value2),
                new Entry<K, V>(key3, value3), new Entry<K, V>(key4, value4), new Entry<K, V>(key5, value5));
    }

    /**
     * Returns a new updatable map with the specified entries and key and value cardinality.
     *
     * @param <K>                    The key type.
     * @param <V>                    The value type.
     * @param keyAndValueCardinality The key and value cardinality.
     * @param entries                The entries for the new map.
     * @return A new updatable map with the specified entries.
     */
    static <K, V> UpdatableMap<K, V> of(final KeyAndValueCardinality keyAndValueCardinality,
            final Entry<K, V>... entries) {
        return new UpdatableHashMap<K, V>(keyAndValueCardinality, entries);
    }

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
