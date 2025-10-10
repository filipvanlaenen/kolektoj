package net.filipvanlaenen.kolektoj;

import java.util.Comparator;

import net.filipvanlaenen.kolektoj.Map.Entry;
import net.filipvanlaenen.kolektoj.sortedtree.SortedTreeMap;

/**
 * Interface defining the signature for all sorted maps.
 *
 * @param <K> The key type.
 * @param <V> The value type.
 */
public interface SortedMap<K, V> extends Collection<Entry<K, V>>, Map<K, V> {
    /**
     * Returns a new empty sorted map.
     *
     * @param <L>        The key type.
     * @param <W>        The value type.
     * @param comparator The comparator by which to sort the keys.
     * @return A new empty sorted map.
     */
    static <L, W> SortedMap<L, W> empty(final Comparator<? super L> comparator) {
        return new SortedTreeMap<L, W>(comparator);
    }

    /**
     * Returns a new sorted map with the specified entries.
     *
     * @param <L>        The key type.
     * @param <W>        The value type.
     * @param comparator The comparator by which to sort the keys.
     * @param entries    The entries for the new sorted map.
     * @return A new sorted map with the specified entries.
     */
    static <L, W> SortedMap<L, W> of(final Comparator<? super L> comparator, final Entry<L, W>... entries) {
        return new SortedTreeMap<L, W>(comparator, entries);
    }

    /**
     * Returns a new sorted map containing an entry with the key and the value.
     *
     * @param <L>        The key type.
     * @param <W>        The value type.
     * @param comparator The comparator by which to sort the keys.
     * @param key        The key for the entry.
     * @param value      The value for the entry.
     * @return A new sorted map containing an entry with the key and the value.
     */
    static <L, W> SortedMap<L, W> of(final Comparator<? super L> comparator, final L key, final W value) {
        return new SortedTreeMap<L, W>(comparator, new Entry<L, W>(key, value));
    }

    /**
     * Returns a new sorted map containing two entries using the provided keys and values.
     *
     * @param <L>        The key type.
     * @param <W>        The value type.
     * @param comparator The comparator by which to sort the keys.
     * @param key1       The first key for the entry.
     * @param value1     The first value for the entry.
     * @param key2       The second key for the entry.
     * @param value2     The second value for the entry.
     * @return A new sorted map containing two entries using the provided keys and values.
     */
    static <L, W> SortedMap<L, W> of(final Comparator<? super L> comparator, final L key1, final W value1, final L key2,
            final W value2) {
        return new SortedTreeMap<L, W>(comparator, new Entry<L, W>(key1, value1), new Entry<L, W>(key2, value2));
    }

    /**
     * Returns a new sorted map containing three entries using the provided keys and values.
     *
     * @param <L>        The key type.
     * @param <W>        The value type.
     * @param comparator The comparator by which to sort the keys.
     * @param key1       The first key for the entry.
     * @param value1     The first value for the entry.
     * @param key2       The second key for the entry.
     * @param value2     The second value for the entry.
     * @param key3       The third key for the entry.
     * @param value3     The third value for the entry.
     * @return A new sorted map containing three entries using the provided keys and values.
     */
    static <L, W> SortedMap<L, W> of(final Comparator<? super L> comparator, final L key1, final W value1, final L key2,
            final W value2, final L key3, final W value3) {
        return new SortedTreeMap<L, W>(comparator, new Entry<L, W>(key1, value1), new Entry<L, W>(key2, value2),
                new Entry<L, W>(key3, value3));
    }

    /**
     * Returns a new sorted map containing four entries using the provided keys and values.
     *
     * @param <L>        The key type.
     * @param <W>        The value type.
     * @param comparator The comparator by which to sort the keys.
     * @param key1       The first key for the entry.
     * @param value1     The first value for the entry.
     * @param key2       The second key for the entry.
     * @param value2     The second value for the entry.
     * @param key3       The third key for the entry.
     * @param value3     The third value for the entry.
     * @param key4       The fourth key for the entry.
     * @param value4     The fourth value for the entry.
     * @return A new sorted map containing four entries using the provided keys and values.
     */
    static <L, W> SortedMap<L, W> of(final Comparator<? super L> comparator, final L key1, final W value1, final L key2,
            final W value2, final L key3, final W value3, final L key4, final W value4) {
        return new SortedTreeMap<L, W>(comparator, new Entry<L, W>(key1, value1), new Entry<L, W>(key2, value2),
                new Entry<L, W>(key3, value3), new Entry<L, W>(key4, value4));
    }

    /**
     * Returns a new sorted map containing five entries using the provided keys and values.
     *
     * @param <L>        The key type.
     * @param <W>        The value type.
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
     * @return A new sorted map containing five entries using the provided keys and values.
     */
    static <L, W> SortedMap<L, W> of(final Comparator<? super L> comparator, final L key1, final W value1, final L key2,
            final W value2, final L key3, final W value3, final L key4, final W value4, final L key5, final W value5) {
        return new SortedTreeMap<L, W>(comparator, new Entry<L, W>(key1, value1), new Entry<L, W>(key2, value2),
                new Entry<L, W>(key3, value3), new Entry<L, W>(key4, value4), new Entry<L, W>(key5, value5));
    }

    /**
     * Returns a new sorted map cloned from the provided map but sorted according to the comparator.
     *
     * @param <L>        The key type.
     * @param <W>        The value type.
     * @param comparator The comparator by which to sort the keys.
     * @param map        The original map.
     * @return A new sorted map cloned from the provided map but sorted according to the comparator.
     */
    static <L, W> SortedMap<L, W> of(final Comparator<? super L> comparator, final Map<? extends L, ? extends W> map) {
        return new SortedTreeMap<L, W>(comparator, map);
    }

    /**
     * Returns a new sorted map with the specified entries and key and value cardinality.
     *
     * @param <L>                    The key type.
     * @param <W>                    The value type.
     * @param keyAndValueCardinality The key and value cardinality.
     * @param comparator             The comparator by which to sort the keys.
     * @param entries                The entries for the new sorted map.
     * @return A new sorted map with the specified entries.
     */
    static <L, W> SortedMap<L, W> of(final KeyAndValueCardinality keyAndValueCardinality,
            final Comparator<? super L> comparator, final Entry<L, W>... entries) {
        return new SortedTreeMap<L, W>(keyAndValueCardinality, comparator, entries);
    }

    /**
     * Returns a new sorted map cloned from the provided sorted map.
     *
     * @param <L> The key type.
     * @param <W> The value type.
     * @param map The original sorted map.
     * @return A new sorted map cloned from the provided sorted map.
     */
    static <L, W> SortedMap<L, W> of(final SortedMap<L, ? extends W> map) {
        return new SortedTreeMap<L, W>(map.getComparator(), map);
    }

    /**
     * Returns the comparator sorting the keys of this map.
     *
     * @return The comparator sorting the keys of this map
     */
    Comparator<? super K> getComparator();

    /**
     * Returns the entry with the least key in this map greater than the provided key.
     *
     * @param key The key to match.
     * @return The entry with the least key in this map greater than the provided key.
     * @throws IndexOutOfBoundsException Thrown if the map doesn't contain a key that's greater than the provided key,
     *                                   or the map is empty.
     */
    Entry<K, V> getGreaterThan(K key) throws IndexOutOfBoundsException;

    /**
     * Returns the entry with the least key in this map greater than or equal to the provided key.
     *
     * @param key The key to match.
     * @return The entry with the least key in this map greater than or equal to the provided key.
     * @throws IndexOutOfBoundsException Thrown if the map doesn't contain a key that's greater than or equal to the
     *                                   provided key, or the map is empty.
     */
    Entry<K, V> getGreaterThanOrEqualTo(K key) throws IndexOutOfBoundsException;

    /**
     * Returns the entry with the greatest key in the map.
     *
     * @return The entry with the greatest key in the map.
     * @throws IndexOutOfBoundsException Thrown if the map is empty.
     */
    Entry<K, V> getGreatest() throws IndexOutOfBoundsException;

    /**
     * Returns the greatest key in the map.
     *
     * @return The greatest key in the map.
     * @throws IndexOutOfBoundsException Thrown if the map is empty.
     */
    K getGreatestKey() throws IndexOutOfBoundsException;

    /**
     * Returns a sorted collection with all the keys present in the map.
     *
     * @return A sorted collection with all the keys present in the map.
     */
    SortedCollection<K> getKeys();

    /**
     * Returns the entry with the least key in the map.
     *
     * @return The entry with the least key in the map.
     * @throws IndexOutOfBoundsException Thrown if the map is empty.
     */
    Entry<K, V> getLeast() throws IndexOutOfBoundsException;

    /**
     * Returns the least key in the map.
     *
     * @return The least key in the map.
     * @throws IndexOutOfBoundsException Thrown if the map is empty.
     */
    K getLeastKey() throws IndexOutOfBoundsException;

    /**
     * Returns the entry with the greatest key in this map less than the provided key.
     *
     * @param key The key to match.
     * @return The entry with the greatest key in this map less than the provided key.
     * @throws IndexOutOfBoundsException Thrown if the map doesn't contain a key that's less than the provided key, or
     *                                   the map is empty.
     */
    Entry<K, V> getLessThan(K key) throws IndexOutOfBoundsException;

    /**
     * Returns the entry with the greatest key in this map less than or equal to the provided key.
     *
     * @param key The key to match.
     * @return The entry with the greatest key in this map less than or equal to the provided key.
     * @throws IndexOutOfBoundsException Thrown if the map doesn't contain a key that's less than or equal to the
     *                                   provided key, or the map is empty.
     */
    Entry<K, V> getLessThanOrEqualTo(K key) throws IndexOutOfBoundsException;
}
