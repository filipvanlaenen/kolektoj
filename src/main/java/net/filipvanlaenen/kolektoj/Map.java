package net.filipvanlaenen.kolektoj;

import static net.filipvanlaenen.kolektoj.Collection.ElementCardinality.DISTINCT_ELEMENTS;
import static net.filipvanlaenen.kolektoj.Collection.ElementCardinality.DUPLICATE_ELEMENTS;

import net.filipvanlaenen.kolektoj.Map.Entry;
import net.filipvanlaenen.kolektoj.hash.HashMap;

/**
 * Interface defining the signature for all maps.
 *
 * @param <K> The key type.
 * @param <V> The value type.
 */
public interface Map<K, V> extends Collection<Entry<K, V>> {
    /**
     * Enumeration listing the options for the key and value cardinality in maps.
     */
    enum KeyAndValueCardinality {
        /**
         * Only distinct keys allowed.
         */
        DISTINCT_KEYS,
        /**
         * Duplicate keys allowed, but only with distinct values.
         */
        DUPLICATE_KEYS_WITH_DISTINCT_VALUES,
        /**
         * Duplicate keys with duplicate values allowed.
         */
        DUPLICATE_KEYS_WITH_DUPLICATE_VALUES
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
     * @param <L> The key type.
     * @param <W> The value type.
     * @return A new empty map.
     */
    static <L, W> Map<L, W> empty() {
        return new HashMap<L, W>();
    }

    /**
     * Returns a new map with the specified entries.
     *
     * @param <L>     The key type.
     * @param <W>     The value type.
     * @param entries The entries for the new map.
     * @return A new map with the specified entries.
     */
    static <L, W> Map<L, W> of(final Entry<L, W>... entries) {
        return new HashMap<L, W>(entries);
    }

    /**
     * Returns a new map with the specified entries and key and value cardinality.
     *
     * @param <L>                    The key type.
     * @param <W>                    The value type.
     * @param keyAndValueCardinality The key and value cardinality.
     * @param entries                The entries for the new map.
     * @return A new map with the specified entries.
     */
    static <L, W> Map<L, W> of(final KeyAndValueCardinality keyAndValueCardinality, final Entry<L, W>... entries) {
        return new HashMap<L, W>(keyAndValueCardinality, entries);
    }

    /**
     * Returns a new map cloned from the provided map with the provided key and value cardinality.
     *
     * @param <L>                    The key type.
     * @param <W>                    The value type.
     * @param keyAndValueCardinality The key and value cardinality.
     * @param map                    The original map.
     * @return A new map cloned from the provided map.
     */
    static <L, W> Map<L, W> of(final KeyAndValueCardinality keyAndValueCardinality,
            final Map<? extends L, ? extends W> map) {
        return new HashMap<L, W>(keyAndValueCardinality, map);
    }

    /**
     * Returns a new map containing an entry with the key and the value.
     *
     * @param <L>   The key type.
     * @param <W>   The value type.
     * @param key   The key for the entry.
     * @param value The value for the entry.
     * @return A new map containing an entry with the key and the value.
     */
    static <L, W> Map<L, W> of(final L key, final W value) {
        return new HashMap<L, W>(new Entry<L, W>(key, value));
    }

    /**
     * Returns a new map containing two entries using the provided keys and values.
     *
     * @param <L>    The key type.
     * @param <W>    The value type.
     * @param key1   The first key for the entry.
     * @param value1 The first value for the entry.
     * @param key2   The second key for the entry.
     * @param value2 The second value for the entry.
     * @return A new map containing two entries using the provided keys and values.
     */
    static <L, W> Map<L, W> of(final L key1, final W value1, final L key2, final W value2) {
        return new HashMap<L, W>(new Entry<L, W>(key1, value1), new Entry<L, W>(key2, value2));
    }

    /**
     * Returns a new map containing three entries using the provided keys and values.
     *
     * @param <L>    The key type.
     * @param <W>    The value type.
     * @param key1   The first key for the entry.
     * @param value1 The first value for the entry.
     * @param key2   The second key for the entry.
     * @param value2 The second value for the entry.
     * @param key3   The third key for the entry.
     * @param value3 The third value for the entry.
     * @return A new map containing three entries using the provided keys and values.
     */
    static <L, W> Map<L, W> of(final L key1, final W value1, final L key2, final W value2, final L key3,
            final W value3) {
        return new HashMap<L, W>(new Entry<L, W>(key1, value1), new Entry<L, W>(key2, value2),
                new Entry<L, W>(key3, value3));
    }

    /**
     * Returns a new map containing four entries using the provided keys and values.
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
     * @return A new map containing four entries using the provided keys and values.
     */
    static <L, W> Map<L, W> of(final L key1, final W value1, final L key2, final W value2, final L key3, final W value3,
            final L key4, final W value4) {
        return new HashMap<L, W>(new Entry<L, W>(key1, value1), new Entry<L, W>(key2, value2),
                new Entry<L, W>(key3, value3), new Entry<L, W>(key4, value4));
    }

    /**
     * Returns a new map containing five entries using the provided keys and values.
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
     * @return A new map containing five entries using the provided keys and values.
     */
    static <L, W> Map<L, W> of(final L key1, final W value1, final L key2, final W value2, final L key3, final W value3,
            final L key4, final W value4, final L key5, final W value5) {
        return new HashMap<L, W>(new Entry<L, W>(key1, value1), new Entry<L, W>(key2, value2),
                new Entry<L, W>(key3, value3), new Entry<L, W>(key4, value4), new Entry<L, W>(key5, value5));
    }

    /**
     * Returns a new map cloned from the provided map.
     *
     * @param <L> The key type.
     * @param <W> The value type.
     * @param map The original map.
     * @return A new map cloned from the provided map.
     */
    static <L, W> Map<L, W> of(final Map<? extends L, ? extends W> map) {
        return new HashMap<L, W>(map);
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
     * Returns a value mapped to the key, or the provided value if the key is absent.
     *
     * @param key          The key.
     * @param defaultValue The value to be returned if the key is absent.
     * @return A value mapped to the key, of the provided value if the key is absent.
     */
    default V get(final K key, final V defaultValue) {
        return containsKey(key) ? get(key) : defaultValue;
    }

    /**
     * Returns a collection with all values mapped to the key. Throws an exception if the map doesn't contain entries
     * with the key.
     *
     * @param key The key.
     * @return A collection with all the values mapped to the key.
     * @throws IllegalArgumentException Thrown if the map doesn't contain entries with the key.
     */
    Collection<V> getAll(K key) throws IllegalArgumentException;

    @Override
    default ElementCardinality getElementCardinality() {
        return getKeyAndValueCardinality() == KeyAndValueCardinality.DUPLICATE_KEYS_WITH_DUPLICATE_VALUES
                ? DUPLICATE_ELEMENTS
                : DISTINCT_ELEMENTS;
    }

    /**
     * Returns the key and value cardinality of the collection.
     *
     * @return The key and value cardinality.
     */
    KeyAndValueCardinality getKeyAndValueCardinality();

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
