package net.filipvanlaenen.kolektoj;

import java.util.Comparator;

import net.filipvanlaenen.kolektoj.Map.Entry;
import net.filipvanlaenen.kolektoj.sortedtree.ModifiableSortedTreeMap;

/**
 * Interface defining the signature for all modifiable sorted maps.
 *
 * @param <K> The key type.
 * @param <V> The value type.
 */
public interface ModifiableSortedMap<K, V>
        extends Collection<Entry<K, V>>, ModifiableMap<K, V>, UpdatableSortedMap<K, V> {
    /**
     * Returns a new empty modifiable sorted map.
     *
     * @param <L>        The key type.
     * @param <W>        The value type.
     * @param comparator The comparator by which to sort the keys.
     * @return A new empty modifiable sorted map.
     */
    static <L, W> ModifiableSortedMap<L, W> empty(final Comparator<L> comparator) {
        return new ModifiableSortedTreeMap<L, W>(comparator);
    }

    /**
     * Returns a new modifiable sorted map with the specified entries.
     *
     * @param <L>        The key type.
     * @param <W>        The value type.
     * @param comparator The comparator by which to sort the keys.
     * @param entries    The entries for the new modifiable sorted map.
     * @return A new modifiable sorted map with the specified entries.
     */
    static <L, W> ModifiableSortedMap<L, W> of(final Comparator<L> comparator, final Entry<L, W>... entries) {
        return new ModifiableSortedTreeMap<L, W>(comparator, entries);
    }

    /**
     * Returns a new modifiable sorted map containing an entry with the key and the value.
     *
     * @param <L>        The key type.
     * @param <W>        The value type.
     * @param comparator The comparator by which to sort the keys.
     * @param key        The key for the entry.
     * @param value      The value for the entry.
     * @return A new modifiable sorted map containing an entry with the key and the value.
     */
    static <L, W> ModifiableSortedMap<L, W> of(final Comparator<L> comparator, final L key, final W value) {
        return new ModifiableSortedTreeMap<L, W>(comparator, new Entry<L, W>(key, value));
    }

    /**
     * Returns a new modifiable sorted map containing two entries using the provided keys and values.
     *
     * @param <L>        The key type.
     * @param <W>        The value type.
     * @param comparator The comparator by which to sort the keys.
     * @param key1       The first key for the entry.
     * @param value1     The first value for the entry.
     * @param key2       The second key for the entry.
     * @param value2     The second value for the entry.
     * @return A new modifiable sorted map containing two entries using the provided keys and values.
     */
    static <L, W> ModifiableSortedMap<L, W> of(final Comparator<L> comparator, final L key1, final W value1,
            final L key2, final W value2) {
        return new ModifiableSortedTreeMap<L, W>(comparator, new Entry<L, W>(key1, value1),
                new Entry<L, W>(key2, value2));
    }

    /**
     * Returns a new modifiable sorted map containing three entries using the provided keys and values.
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
     * @return A new modifiable sorted map containing three entries using the provided keys and values.
     */
    static <L, W> ModifiableSortedMap<L, W> of(final Comparator<L> comparator, final L key1, final W value1,
            final L key2, final W value2, final L key3, final W value3) {
        return new ModifiableSortedTreeMap<L, W>(comparator, new Entry<L, W>(key1, value1),
                new Entry<L, W>(key2, value2), new Entry<L, W>(key3, value3));
    }

    /**
     * Returns a new modifiable sorted map containing four entries using the provided keys and values.
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
     * @return A new modifiable sorted map containing four entries using the provided keys and values.
     */
    static <L, W> ModifiableSortedMap<L, W> of(final Comparator<L> comparator, final L key1, final W value1,
            final L key2, final W value2, final L key3, final W value3, final L key4, final W value4) {
        return new ModifiableSortedTreeMap<L, W>(comparator, new Entry<L, W>(key1, value1),
                new Entry<L, W>(key2, value2), new Entry<L, W>(key3, value3), new Entry<L, W>(key4, value4));
    }

    /**
     * Returns a new modifiable sorted map containing five entries using the provided keys and values.
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
     * @return A new modifiable sorted map containing five entries using the provided keys and values.
     */
    static <L, W> ModifiableSortedMap<L, W> of(final Comparator<L> comparator, final L key1, final W value1,
            final L key2, final W value2, final L key3, final W value3, final L key4, final W value4, final L key5,
            final W value5) {
        return new ModifiableSortedTreeMap<L, W>(comparator, new Entry<L, W>(key1, value1),
                new Entry<L, W>(key2, value2), new Entry<L, W>(key3, value3), new Entry<L, W>(key4, value4),
                new Entry<L, W>(key5, value5));
    }

    /**
     * Returns a new modifiable sorted map cloned from the provided map but sorted according to the comparator.
     *
     * @param <L>        The key type.
     * @param <W>        The value type.
     * @param comparator The comparator by which to sort the keys.
     * @param map        The original map.
     * @return A new modifiable sorted map cloned from the provided map but sorted according to the comparator.
     */
    static <L, W> ModifiableSortedMap<L, W> of(final Comparator<? super L> comparator,
            final Map<? extends L, ? extends W> map) {
        return new ModifiableSortedTreeMap<L, W>(comparator, map);
    }

    /**
     * Returns a new modifiable sorted map with the specified keys with a default value.
     *
     * @param <L>          The key type.
     * @param <W>          The value type.
     * @param comparator   The comparator by which to sort the keys.
     * @param defaultValue The default value for the entries.
     * @param keys         The keys for the new map.
     * @return A new modifiable sorted map with the specified entries.
     */
    static <L, W> ModifiableSortedMap<L, W> of(final Comparator<L> comparator, final W defaultValue, final L... keys) {
        ModifiableSortedMap<L, W> map = ModifiableSortedMap.<L, W>empty(comparator);
        for (L key : keys) {
            map.add(key, defaultValue);
        }
        return map;
    }

    /**
     * Returns a new modifiable sorted map with the specified entries and key and value cardinality.
     *
     * @param <L>                    The key type.
     * @param <W>                    The value type.
     * @param keyAndValueCardinality The key and value cardinality.
     * @param comparator             The comparator by which to sort the keys.
     * @param entries                The entries for the new modifiable sorted map.
     * @return A new modifiable sorted map with the specified entries.
     */
    static <L, W> ModifiableSortedMap<L, W> of(final KeyAndValueCardinality keyAndValueCardinality,
            final Comparator<L> comparator, final Entry<L, W>... entries) {
        return new ModifiableSortedTreeMap<L, W>(keyAndValueCardinality, comparator, entries);
    }

    /**
     * Returns a new modifiable sorted map with the specified keys with a default value and key and value cardinality.
     *
     * @param <L>                    The key type.
     * @param <W>                    The value type.
     * @param keyAndValueCardinality The key and value cardinality.
     * @param comparator             The comparator by which to sort the keys.
     * @param defaultValue           The default value for the entries.
     * @param keys                   The keys for the new map.
     * @return A new modifiable sorted map with the specified entries.
     */
    static <L, W> ModifiableSortedMap<L, W> of(final KeyAndValueCardinality keyAndValueCardinality,
            final Comparator<L> comparator, final W defaultValue, final L... keys) {
        ModifiableSortedMap<L, W> map = ModifiableSortedMap.<L, W>of(keyAndValueCardinality, comparator);
        for (L key : keys) {
            map.add(key, defaultValue);
        }
        return map;
    }

    /**
     * Returns a new modifiable sorted map cloned from the provided sorted map.
     *
     * @param <L> The key type.
     * @param <W> The value type.
     * @param map The original sorted map.
     * @return A new modifiable sorted map cloned from the provided sorted map.
     */
    static <L, W> ModifiableSortedMap<L, W> of(final SortedMap<L, ? extends W> map) {
        return new ModifiableSortedTreeMap<L, W>(map.getComparator(), map);
    }

    /**
     * Returns a new modifiable sorted map cloned from the provided sorted map.
     *
     * @param <L>   The key type.
     * @param <W>   The value type.
     * @param map   The original sorted map.
     * @param range The range.
     * @return A new modifiable sorted map cloned from the provided sorted map.
     */
    static <L, W> ModifiableSortedMap<L, W> of(final SortedMap<L, ? extends W> map, final Range<L> range) {
        ModifiableSortedMap<L, W> result =
                new ModifiableSortedTreeMap<L, W>(map.getKeyAndValueCardinality(), map.getComparator());
        boolean below = true;
        for (Entry<L, ? extends W> entry : map) {
            if (below && !range.isBelow(map.getComparator(), entry.key())) {
                below = false;
            }
            if (!below) {
                if (range.isAbove(map.getComparator(), entry.key())) {
                    break;
                }
                result.add(entry.key(), entry.value());
            }
        }
        return result;
    }

    /**
     * Removes the entry with the greatest key in the map.
     *
     * @return The entry that was removed.
     * @throws IndexOutOfBoundsException Thrown if the map is empty.
     */
    Entry<K, V> removeGreatest();

    /**
     * Removes the entry with the least key in the map.
     *
     * @return The entry that was removed.
     * @throws IndexOutOfBoundsException Thrown if the map is empty.
     */
    Entry<K, V> removeLeast();
}
