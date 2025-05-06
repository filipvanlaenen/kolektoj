package net.filipvanlaenen.kolektoj;

import java.util.Comparator;

import net.filipvanlaenen.kolektoj.Map.Entry;
import net.filipvanlaenen.kolektoj.sortedtree.UpdatableSortedTreeMap;

/**
 * Interface defining the signature for all updatable sorted maps.
 *
 * @param <K> The key type.
 * @param <V> The value type.
 */
public interface UpdatableSortedMap<K, V> extends Collection<Entry<K, V>>, UpdatableMap<K, V>, SortedMap<K, V> {
    /**
     * Returns a new empty updatable sorted map.
     *
     * @param <K>        The key type.
     * @param <V>        The value type.
     * @param comparator The comparator by which to sort the keys.
     * @return A new empty updatable sorted map.
     */
    static <K, V> UpdatableSortedMap<K, V> empty(final Comparator<K> comparator) {
        return new UpdatableSortedTreeMap<K, V>(comparator);
    }

    /**
     * Returns a new updatable sorted map with the specified entries.
     *
     * @param <K>        The key type.
     * @param <V>        The value type.
     * @param comparator The comparator by which to sort the keys.
     * @param entries    The entries for the new updatable sorted map.
     * @return A new updatable sorted map with the specified entries.
     */
    static <K, V> UpdatableSortedMap<K, V> of(final Comparator<K> comparator, final Entry<K, V>... entries) {
        return new UpdatableSortedTreeMap<K, V>(comparator, entries);
    }

    /**
     * Returns a new updatable sorted map with the specified keys with a default value.
     *
     * @param <K>          The key type.
     * @param <V>          The value type.
     * @param comparator   The comparator by which to sort the keys.
     * @param defaultValue The default value for the entries.
     * @param keys         The keys for the new map.
     * @return A new updatable sorted map with the specified entries.
     */
    static <K, V> UpdatableSortedMap<K, V> of(final Comparator<K> comparator, final V defaultValue, final K... keys) {
        ModifiableMap<K, V> map = ModifiableMap.<K, V>empty();
        for (K key : keys) {
            map.add(key, defaultValue);
        }
        return new UpdatableSortedTreeMap<K, V>(comparator, map);
    }

    /**
     * Returns a new updatable sorted map with the specified entries and key and value cardinality.
     *
     * @param <K>                    The key type.
     * @param <V>                    The value type.
     * @param keyAndValueCardinality The key and value cardinality.
     * @param comparator             The comparator by which to sort the keys.
     * @param entries                The entries for the new updatable sorted map.
     * @return A new updatable sorted map with the specified entries.
     */
    static <K, V> UpdatableSortedMap<K, V> of(final KeyAndValueCardinality keyAndValueCardinality,
            final Comparator<K> comparator, final Entry<K, V>... entries) {
        return new UpdatableSortedTreeMap<K, V>(keyAndValueCardinality, comparator, entries);
    }

    /**
     * Returns a new updatable sorted map with the specified keys with a default value and key and value cardinality.
     *
     * @param <K>                    The key type.
     * @param <V>                    The value type.
     * @param keyAndValueCardinality The key and value cardinality.
     * @param comparator             The comparator by which to sort the keys.
     * @param defaultValue           The default value for the entries.
     * @param keys                   The keys for the new map.
     * @return A new updatable sorted map with the specified entries.
     */
    static <K, V> UpdatableSortedMap<K, V> of(final KeyAndValueCardinality keyAndValueCardinality,
            final Comparator<K> comparator, final V defaultValue, final K... keys) {
        ModifiableMap<K, V> map = ModifiableMap.<K, V>of(keyAndValueCardinality);
        for (K key : keys) {
            map.add(key, defaultValue);
        }
        return new UpdatableSortedTreeMap<K, V>(comparator, map);
    }

    /**
     * Returns a new updatable sorted map containing an entry with the key and the value.
     *
     * @param <K>        The key type.
     * @param <V>        The value type.
     * @param comparator The comparator by which to sort the keys.
     * @param key        The key for the entry.
     * @param value      The value for the entry.
     * @return A new updatable sorted map containing an entry with the key and the value.
     */
    static <K, V> UpdatableSortedMap<K, V> of(final Comparator<K> comparator, final K key, final V value) {
        return new UpdatableSortedTreeMap<K, V>(comparator, new Entry<K, V>(key, value));
    }

    /**
     * Returns a new updatable sorted map containing two entries using the provided keys and values.
     *
     * @param <K>        The key type.
     * @param <V>        The value type.
     * @param comparator The comparator by which to sort the keys.
     * @param key1       The first key for the entry.
     * @param value1     The first value for the entry.
     * @param key2       The second key for the entry.
     * @param value2     The second value for the entry.
     * @return A new updatable sorted map containing two entries using the provided keys and values.
     */
    static <K, V> UpdatableSortedMap<K, V> of(final Comparator<K> comparator, final K key1, final V value1,
            final K key2, final V value2) {
        return new UpdatableSortedTreeMap<K, V>(comparator, new Entry<K, V>(key1, value1),
                new Entry<K, V>(key2, value2));
    }

    /**
     * Returns a new updatable sorted map containing three entries using the provided keys and values.
     *
     * @param <K>        The key type.
     * @param <V>        The value type.
     * @param comparator The comparator by which to sort the keys.
     * @param key1       The first key for the entry.
     * @param value1     The first value for the entry.
     * @param key2       The second key for the entry.
     * @param value2     The second value for the entry.
     * @param key3       The third key for the entry.
     * @param value3     The third value for the entry.
     * @return A new updatable sorted map containing three entries using the provided keys and values.
     */
    static <K, V> UpdatableSortedMap<K, V> of(final Comparator<K> comparator, final K key1, final V value1,
            final K key2, final V value2, final K key3, final V value3) {
        return new UpdatableSortedTreeMap<K, V>(comparator, new Entry<K, V>(key1, value1),
                new Entry<K, V>(key2, value2), new Entry<K, V>(key3, value3));
    }

    /**
     * Returns a new updatable sorted map containing four entries using the provided keys and values.
     *
     * @param <K>        The key type.
     * @param <V>        The value type.
     * @param comparator The comparator by which to sort the keys.
     * @param key1       The first key for the entry.
     * @param value1     The first value for the entry.
     * @param key2       The second key for the entry.
     * @param value2     The second value for the entry.
     * @param key3       The third key for the entry.
     * @param value3     The third value for the entry.
     * @param key4       The fourth key for the entry.
     * @param value4     The fourth value for the entry.
     * @return A new updatable sorted map containing four entries using the provided keys and values.
     */
    static <K, V> UpdatableSortedMap<K, V> of(final Comparator<K> comparator, final K key1, final V value1,
            final K key2, final V value2, final K key3, final V value3, final K key4, final V value4) {
        return new UpdatableSortedTreeMap<K, V>(comparator, new Entry<K, V>(key1, value1),
                new Entry<K, V>(key2, value2), new Entry<K, V>(key3, value3), new Entry<K, V>(key4, value4));
    }

    /**
     * Returns a new updatable sorted map containing five entries using the provided keys and values.
     *
     * @param <K>        The key type.
     * @param <V>        The value type.
     * @param comparator The comparator by which to sort the keys.
     * @param key1       The first key for the entry.
     * @param value1     The first value for the entry.
     * @param key2       The second key for the entry.
     * @param value2     The second value for the entry.
     * @param key3       The third key for the entry.
     * @param value3     The third value for the entry.
     * @param key4       The fourth key for the entry.
     * @param value4     The fourth value for the entry.
     * @param key5       The fifth key for the entry.
     * @param value5     The fifth value for the entry.
     * @return A new updatable sorted map containing five entries using the provided keys and values.
     */
    static <K, V> UpdatableSortedMap<K, V> of(final Comparator<K> comparator, final K key1, final V value1,
            final K key2, final V value2, final K key3, final V value3, final K key4, final V value4, final K key5,
            final V value5) {
        return new UpdatableSortedTreeMap<K, V>(comparator, new Entry<K, V>(key1, value1),
                new Entry<K, V>(key2, value2), new Entry<K, V>(key3, value3), new Entry<K, V>(key4, value4),
                new Entry<K, V>(key5, value5));
    }
}
