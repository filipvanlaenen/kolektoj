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
     * @param <L> The key type.
     * @param <W> The value type.
     * @return A new empty updatable map.
     */
    static <L, W> UpdatableMap<L, W> empty() {
        return new UpdatableHashMap<L, W>();
    }

    /**
     * Returns a new updatable map with the specified entries.
     *
     * @param <L>     The key type.
     * @param <W>     The value type.
     * @param entries The entries for the new updatable map.
     * @return A new updatable map with the specified entries.
     */
    static <L, W> UpdatableMap<L, W> of(final Entry<L, W>... entries) {
        return new UpdatableHashMap<L, W>(entries);
    }

    /**
     * Returns a new updatable map containing an entry with the key and the value.
     *
     * @param <L>   The key type.
     * @param <W>   The value type.
     * @param key   The key for the entry.
     * @param value The value for the entry.
     * @return A new updatable map containing an entry with the key and the value.
     */
    static <L, W> UpdatableMap<L, W> of(final L key, final W value) {
        return new UpdatableHashMap<L, W>(new Entry<L, W>(key, value));
    }

    /**
     * Returns a new updatable map containing two entries using the provided keys and values.
     *
     * @param <L>    The key type.
     * @param <W>    The value type.
     * @param key1   The first key for the entry.
     * @param value1 The first value for the entry.
     * @param key2   The second key for the entry.
     * @param value2 The second value for the entry.
     * @return A new updatable map containing two entries using the provided keys and values.
     */
    static <L, W> UpdatableMap<L, W> of(final L key1, final W value1, final L key2, final W value2) {
        return new UpdatableHashMap<L, W>(new Entry<L, W>(key1, value1), new Entry<L, W>(key2, value2));
    }

    /**
     * Returns a new updatable map containing three entries using the provided keys and values.
     *
     * @param <L>    The key type.
     * @param <W>    The value type.
     * @param key1   The first key for the entry.
     * @param value1 The first value for the entry.
     * @param key2   The second key for the entry.
     * @param value2 The second value for the entry.
     * @param key3   The third key for the entry.
     * @param value3 The third value for the entry.
     * @return A new updatable map containing three entries using the provided keys and values.
     */
    static <L, W> UpdatableMap<L, W> of(final L key1, final W value1, final L key2, final W value2, final L key3,
            final W value3) {
        return new UpdatableHashMap<L, W>(new Entry<L, W>(key1, value1), new Entry<L, W>(key2, value2),
                new Entry<L, W>(key3, value3));
    }

    /**
     * Returns a new updatable map containing four entries using the provided keys and values.
     *
     * @param <L>    The key type.
     * @param <W>    The value type.
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
    static <L, W> UpdatableMap<L, W> of(final L key1, final W value1, final L key2, final W value2, final L key3,
            final W value3, final L key4, final W value4) {
        return new UpdatableHashMap<L, W>(new Entry<L, W>(key1, value1), new Entry<L, W>(key2, value2),
                new Entry<L, W>(key3, value3), new Entry<L, W>(key4, value4));
    }

    /**
     * Returns a new updatable map containing five entries using the provided keys and values.
     *
     * @param <L>    The key type.
     * @param <W>    The value type.
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
    static <L, W> UpdatableMap<L, W> of(final L key1, final W value1, final L key2, final W value2, final L key3,
            final W value3, final L key4, final W value4, final L key5, final W value5) {
        return new UpdatableHashMap<L, W>(new Entry<L, W>(key1, value1), new Entry<L, W>(key2, value2),
                new Entry<L, W>(key3, value3), new Entry<L, W>(key4, value4), new Entry<L, W>(key5, value5));
    }

    /**
     * Returns a new updatable map with the specified entries and key and value cardinality.
     *
     * @param <L>                    The key type.
     * @param <W>                    The value type.
     * @param keyAndValueCardinality The key and value cardinality.
     * @param entries                The entries for the new map.
     * @return A new updatable map with the specified entries.
     */
    static <L, W> UpdatableMap<L, W> of(final KeyAndValueCardinality keyAndValueCardinality,
            final Entry<L, W>... entries) {
        return new UpdatableHashMap<L, W>(keyAndValueCardinality, entries);
    }

    /**
     * Returns a new updatable map with the specified keys with a default value and key and value cardinality.
     *
     * @param <L>                    The key type.
     * @param <W>                    The value type.
     * @param keyAndValueCardinality The key and value cardinality.
     * @param defaultValue           The default value for the entries.
     * @param keys                   The keys for the new map.
     * @return A new updatable map with the specified entries.
     */
    static <L, W> UpdatableMap<L, W> of(final KeyAndValueCardinality keyAndValueCardinality, final W defaultValue,
            final L... keys) {
        ModifiableMap<L, W> map = ModifiableMap.<L, W>of(keyAndValueCardinality);
        for (L key : keys) {
            map.add(key, defaultValue);
        }
        return new UpdatableHashMap<L, W>(map);
    }

    /**
     * Returns a new updatable map with the specified keys with a default value.
     *
     * @param <L>          The key type.
     * @param <W>          The value type.
     * @param defaultValue The default value for the entries.
     * @param keys         The keys for the new map.
     * @return A new updatable map with the specified entries.
     */
    static <L, W> UpdatableMap<L, W> of(final W defaultValue, final L... keys) {
        ModifiableMap<L, W> map = ModifiableMap.<L, W>empty();
        for (L key : keys) {
            map.add(key, defaultValue);
        }
        return new UpdatableHashMap<L, W>(map);
    }

    /**
     * Returns a new updatable map cloned from the provided map.
     *
     * @param <L> The key type.
     * @param <W> The value type.
     * @param map The original map.
     * @return A new updatable map cloned from the provided map.
     */
    static <L, W> UpdatableMap<L, W> of(final Map<? extends L, ? extends W> map) {
        return new UpdatableHashMap<L, W>(map);
    }

    /**
     * Updates the key with the given value, and returns the value that was previously mapped to the key. Throws an
     * exception if the map doesn't contain an entry with the key.
     *
     * @param key   The key.
     * @param value The value.
     * @return The value the key was mapped to previously.
     * @throws IllegalArgumentException Thrown if the map doesn't contain an entry with the key.
     */
    V update(K key, V value) throws IllegalArgumentException;
}
