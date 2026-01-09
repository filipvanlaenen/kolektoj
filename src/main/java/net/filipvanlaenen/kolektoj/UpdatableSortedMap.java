package net.filipvanlaenen.kolektoj;

import java.util.Comparator;

import net.filipvanlaenen.kolektoj.Map.Entry;
import net.filipvanlaenen.kolektoj.sortedtree.ModifiableSortedTreeMap;
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
     * @param <L>        The key type.
     * @param <W>        The value type.
     * @param comparator The comparator by which to sort the keys.
     * @return A new empty updatable sorted map.
     */
    static <L, W> UpdatableSortedMap<L, W> empty(final Comparator<? super L> comparator) {
        return new UpdatableSortedTreeMap<L, W>(comparator);
    }

    /**
     * Returns a new updatable sorted map with the specified entries.
     *
     * @param <L>        The key type.
     * @param <W>        The value type.
     * @param comparator The comparator by which to sort the keys.
     * @param entries    The entries for the new updatable sorted map.
     * @return A new updatable sorted map with the specified entries.
     */
    static <L, W> UpdatableSortedMap<L, W> of(final Comparator<? super L> comparator, final Entry<L, W>... entries) {
        return new UpdatableSortedTreeMap<L, W>(comparator, entries);
    }

    /**
     * Returns a new updatable sorted map containing an entry with the key and the value.
     *
     * @param <L>        The key type.
     * @param <W>        The value type.
     * @param comparator The comparator by which to sort the keys.
     * @param key        The key for the entry.
     * @param value      The value for the entry.
     * @return A new updatable sorted map containing an entry with the key and the value.
     */
    static <L, W> UpdatableSortedMap<L, W> of(final Comparator<? super L> comparator, final L key, final W value) {
        return new UpdatableSortedTreeMap<L, W>(comparator, new Entry<L, W>(key, value));
    }

    /**
     * Returns a new updatable sorted map containing two entries using the provided keys and values.
     *
     * @param <L>        The key type.
     * @param <W>        The value type.
     * @param comparator The comparator by which to sort the keys.
     * @param key1       The first key for the entry.
     * @param value1     The first value for the entry.
     * @param key2       The second key for the entry.
     * @param value2     The second value for the entry.
     * @return A new updatable sorted map containing two entries using the provided keys and values.
     */
    static <L, W> UpdatableSortedMap<L, W> of(final Comparator<? super L> comparator, final L key1, final W value1,
            final L key2, final W value2) {
        return new UpdatableSortedTreeMap<L, W>(comparator, new Entry<L, W>(key1, value1),
                new Entry<L, W>(key2, value2));
    }

    /**
     * Returns a new updatable sorted map containing three entries using the provided keys and values.
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
     * @return A new updatable sorted map containing three entries using the provided keys and values.
     */
    static <L, W> UpdatableSortedMap<L, W> of(final Comparator<? super L> comparator, final L key1, final W value1,
            final L key2, final W value2, final L key3, final W value3) {
        return new UpdatableSortedTreeMap<L, W>(comparator, new Entry<L, W>(key1, value1),
                new Entry<L, W>(key2, value2), new Entry<L, W>(key3, value3));
    }

    /**
     * Returns a new updatable sorted map containing four entries using the provided keys and values.
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
     * @return A new updatable sorted map containing four entries using the provided keys and values.
     */
    static <L, W> UpdatableSortedMap<L, W> of(final Comparator<? super L> comparator, final L key1, final W value1,
            final L key2, final W value2, final L key3, final W value3, final L key4, final W value4) {
        return new UpdatableSortedTreeMap<L, W>(comparator, new Entry<L, W>(key1, value1),
                new Entry<L, W>(key2, value2), new Entry<L, W>(key3, value3), new Entry<L, W>(key4, value4));
    }

    /**
     * Returns a new updatable sorted map containing five entries using the provided keys and values.
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
     * @return A new updatable sorted map containing five entries using the provided keys and values.
     */
    static <L, W> UpdatableSortedMap<L, W> of(final Comparator<? super L> comparator, final L key1, final W value1,
            final L key2, final W value2, final L key3, final W value3, final L key4, final W value4, final L key5,
            final W value5) {
        return new UpdatableSortedTreeMap<L, W>(comparator, new Entry<L, W>(key1, value1),
                new Entry<L, W>(key2, value2), new Entry<L, W>(key3, value3), new Entry<L, W>(key4, value4),
                new Entry<L, W>(key5, value5));
    }

    /**
     * Returns a new updatable sorted map cloned from the provided map but sorted according to the comparator.
     *
     * @param <L>        The key type.
     * @param <W>        The value type.
     * @param comparator The comparator by which to sort the keys.
     * @param map        The original map.
     * @return A new updatable sorted map cloned from the provided map but sorted according to the comparator.
     */
    static <L, W> UpdatableSortedMap<L, W> of(final Comparator<? super L> comparator,
            final Map<? extends L, ? extends W> map) {
        return new UpdatableSortedTreeMap<L, W>(comparator, map);
    }

    /**
     * Returns a new updatable sorted map with the specified keys with a default value.
     *
     * @param <L>          The key type.
     * @param <W>          The value type.
     * @param comparator   The comparator by which to sort the keys.
     * @param defaultValue The default value for the entries.
     * @param keys         The keys for the new map.
     * @return A new updatable sorted map with the specified entries.
     */
    static <L, W> UpdatableSortedMap<L, W> of(final Comparator<? super L> comparator, final W defaultValue,
            final L... keys) {
        ModifiableMap<L, W> map = ModifiableMap.<L, W>empty();
        for (L key : keys) {
            map.add(key, defaultValue);
        }
        return new UpdatableSortedTreeMap<L, W>(comparator, map);
    }

    /**
     * Returns a new updatable sorted map with the specified entries and key and value cardinality.
     *
     * @param <L>                    The key type.
     * @param <W>                    The value type.
     * @param keyAndValueCardinality The key and value cardinality.
     * @param comparator             The comparator by which to sort the keys.
     * @param entries                The entries for the new updatable sorted map.
     * @return A new updatable sorted map with the specified entries.
     */
    static <L, W> UpdatableSortedMap<L, W> of(final KeyAndValueCardinality keyAndValueCardinality,
            final Comparator<? super L> comparator, final Entry<L, W>... entries) {
        return new UpdatableSortedTreeMap<L, W>(keyAndValueCardinality, comparator, entries);
    }

    /**
     * Returns a new updatable sorted map with the specified keys with a default value and key and value cardinality.
     *
     * @param <L>                    The key type.
     * @param <W>                    The value type.
     * @param keyAndValueCardinality The key and value cardinality.
     * @param comparator             The comparator by which to sort the keys.
     * @param defaultValue           The default value for the entries.
     * @param keys                   The keys for the new map.
     * @return A new updatable sorted map with the specified entries.
     */
    static <L, W> UpdatableSortedMap<L, W> of(final KeyAndValueCardinality keyAndValueCardinality,
            final Comparator<? super L> comparator, final W defaultValue, final L... keys) {
        ModifiableMap<L, W> map = ModifiableMap.<L, W>of(keyAndValueCardinality);
        for (L key : keys) {
            map.add(key, defaultValue);
        }
        return new UpdatableSortedTreeMap<L, W>(comparator, map);
    }

    /**
     * Returns a new updatable sorted map cloned from the provided sorted map.
     *
     * @param <L> The key type.
     * @param <W> The value type.
     * @param map The original sorted map.
     * @return A new updatable sorted map cloned from the provided sorted map.
     */
    static <L, W> UpdatableSortedMap<L, W> of(final SortedMap<L, ? extends W> map) {
        return new UpdatableSortedTreeMap<L, W>(map.getComparator(), map);
    }

    /**
     * Returns a new updatable sorted map cloned from the provided sorted map.
     *
     * @param <L>   The key type.
     * @param <W>   The value type.
     * @param map   The original sorted map.
     * @param range The range.
     * @return A new updatable sorted map cloned from the provided sorted map.
     */
    static <L, W> UpdatableSortedMap<L, W> of(final SortedMap<L, ? extends W> map, final Range<L> range) {
        ModifiableSortedMap<L, W> slice =
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
                slice.add(entry.key(), entry.value());
            }
        }
        return new UpdatableSortedTreeMap<L, W>(map.getComparator(), slice);
    }
}
