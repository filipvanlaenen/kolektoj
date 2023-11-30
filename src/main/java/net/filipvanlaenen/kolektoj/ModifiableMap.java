package net.filipvanlaenen.kolektoj;

import java.util.function.Predicate;

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
     * Returns a new empty modifiable map.
     *
     * @param <K> The key type.
     * @param <V> The value type.
     * @return A new empty map.
     */
    static <K, V> ModifiableMap<K, V> empty() {
        return new ModifiableHashMap<K, V>();
    }

    /**
     * Returns a new modifiable map with the specified entries.
     *
     * @param <K>     The key type.
     * @param <V>     The value type.
     * @param entries The entries for the new modifiable map.
     * @return A new modifiable map with the specified entries.
     */
    static <K, V> ModifiableMap<K, V> of(final Entry<K, V>... entries) {
        return new ModifiableHashMap<K, V>(entries);
    }

    /**
     * Returns a new modifiable map containing an entry with the key and the value.
     *
     * @param <K>   The key type.
     * @param <V>   The value type.
     * @param key   The key for the entry.
     * @param value The value for the entry.
     * @return A new modifiable map containing an entry with the key and the value.
     */
    static <K, V> ModifiableMap<K, V> of(final K key, final V value) {
        return new ModifiableHashMap<K, V>(new Entry<K, V>(key, value));
    }

    /**
     * Returns a new modifiable map containing two entries using the provided keys and values.
     *
     * @param <K>    The key type.
     * @param <V>    The value type.
     * @param key1   The first key for the entry.
     * @param value1 The first value for the entry.
     * @param key2   The second key for the entry.
     * @param value2 The second value for the entry.
     * @return A new modifiable map containing two entries using the provided keys and values.
     */
    static <K, V> ModifiableMap<K, V> of(final K key1, final V value1, final K key2, final V value2) {
        return new ModifiableHashMap<K, V>(new Entry<K, V>(key1, value1), new Entry<K, V>(key2, value2));
    }

    /**
     * Returns a new modifiable map containing three entries using the provided keys and values.
     *
     * @param <K>    The key type.
     * @param <V>    The value type.
     * @param key1   The first key for the entry.
     * @param value1 The first value for the entry.
     * @param key2   The second key for the entry.
     * @param value2 The second value for the entry.
     * @param key3   The third key for the entry.
     * @param value3 The third value for the entry.
     * @return A new modifiable map containing three entries using the provided keys and values.
     */
    static <K, V> ModifiableMap<K, V> of(final K key1, final V value1, final K key2, final V value2, final K key3,
            final V value3) {
        return new ModifiableHashMap<K, V>(new Entry<K, V>(key1, value1), new Entry<K, V>(key2, value2),
                new Entry<K, V>(key3, value3));
    }

    /**
     * Returns a new modifiable map with the specified entries and key and value cardinality.
     *
     * @param <K>                    The key type.
     * @param <V>                    The value type.
     * @param keyAndValueCardinality The key and value cardinality.
     * @param entries                The entries for the new map.
     * @return A new modifiable map with the specified entries.
     */
    static <K, V> ModifiableMap<K, V> of(final KeyAndValueCardinality keyAndValueCardinality,
            final Entry<K, V>... entries) {
        return new ModifiableHashMap<K, V>(keyAndValueCardinality, entries);
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
     * Adds the entries of a map to this map, and returns whether it increased the size of the map.
     *
     * @param map The map from which entries should be added.
     * @return True if the size of the map increased after adding the entries from the map.
     */
    boolean addAll(Map<? extends K, ? extends V> map);

    /**
     * Removes all entries from the map.
     */
    void clear();

    /**
     * Adds an entry to this map with the given key and value if the key isn't already present, otherwise updates the
     * key with the given value. If the key was previously mapped to a value, the value is returned, and null otherwise.
     *
     * @param key   The key.
     * @param value The value.
     * @return The value if the key was previously mapped to a value, or null otherwise.
     */
    default V put(final K key, final V value) {
        if (containsKey(key)) {
            return update(key, value);
        } else {
            add(key, value);
            return null;
        }
    }

    /**
     * Puts the entries of a map into this map .
     *
     * @param map The map from which entries should be put into this map.
     */
    default void putAll(final Map<? extends K, ? extends V> map) {
        for (Entry<? extends K, ? extends V> entry : map) {
            put(entry.key(), entry.value());
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
     * Removes the entries of a map from this map, and returns whether it decreased the size of the map.
     *
     * @param map The map with entries to be removed from this map.
     * @return True if the size of the map decreased after removing the entries from the map.
     */
    boolean removeAll(Map<? extends K, ? extends V> map);

    /**
     * Removes all entries from this map that satisfy the given predicate, and returns whether it decreased the size of
     * the map.
     * 
     * @param predicate The predicate to be applied to each entry of the map.
     * @return True if the size of the map decreased after removing the entries that satisfied the given predicate.
     */
    boolean removeIf(Predicate<Entry<? extends K, ? extends V>> predicate);

    /**
     * Retains the entries of a map in this map, while removing all other, and returns whether it decreased the size of
     * the map.
     *
     * @param map The map with entries te be retained in this map.
     * @return True if the size of the map decreased after removing the entries not present in the provided map.
     */
    boolean retainAll(Map<? extends K, ? extends V> map);

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
