package net.filipvanlaenen.kolektoj.hash;

import static net.filipvanlaenen.kolektoj.Map.KeyAndValueCardinality.DISTINCT_KEYS;
import static net.filipvanlaenen.kolektoj.Map.KeyAndValueCardinality.DUPLICATE_KEYS_WITH_DISTINCT_VALUES;
import static net.filipvanlaenen.kolektoj.Map.KeyAndValueCardinality.DUPLICATE_KEYS_WITH_DUPLICATE_VALUES;

import net.filipvanlaenen.kolektoj.ModifiableCollection;
import net.filipvanlaenen.kolektoj.Map.Entry;
import net.filipvanlaenen.kolektoj.Map.KeyAndValueCardinality;

/**
 * A class with utility methods for hash-backed collections.
 */
final class HashUtilities {
    /**
     * Private constructor to avoid instantiation of this utility class.
     */
    private HashUtilities() {
    }

    /**
     * Returns the hash value for an object in a hash of size <code>size</code>.
     *
     * @param object The object to hash.
     * @param size   The size of the hash.
     * @return The hash value for the object for the hash size.
     */
    static int hash(final Object object, final int size) {
        return object == null ? 0 : Math.floorMod(object.hashCode(), size);
    }

    /**
     * Populates the state for a map from an array of entries.
     *
     * @param <K>                    The key type.
     * @param <V>                    The value type.
     * @param theEntries             The collection to which the entries should be added.
     * @param theHashedEntries       The hashed array to which the entries should be added.
     * @param theKeys                The collection to which the keys should be added.
     * @param theValues              The collection to which the values should be added.
     * @param keyAndValueCardinality The key and value cardinality of the map.
     * @param entries                The array with the entries.
     * @throws IllegalArgumentException Thrown if one of the entries is null.
     */
    static <K, V> void populateMapFromEntries(final ModifiableCollection<Entry<K, V>> theEntries,
            final Entry<K, V>[] theHashedEntries, final ModifiableCollection<K> theKeys,
            final ModifiableCollection<V> theValues, final KeyAndValueCardinality keyAndValueCardinality,
            final Entry<K, V>... entries) throws IllegalArgumentException {
        int hashedEntriesSize = theHashedEntries.length;
        for (Entry<K, V> entry : entries) {
            if (entry == null) {
                throw new IllegalArgumentException("Map entries can't be null.");
            }
            K key = entry.key();
            if (keyAndValueCardinality == DUPLICATE_KEYS_WITH_DUPLICATE_VALUES
                    || keyAndValueCardinality == DUPLICATE_KEYS_WITH_DISTINCT_VALUES && !theEntries.contains(entry)
                    || keyAndValueCardinality == DISTINCT_KEYS && !theKeys.contains(key)) {
                theEntries.add(entry);
                theKeys.add(key);
                theValues.add(entry.value());
                int i = HashUtilities.hash(key, hashedEntriesSize);
                while (theHashedEntries[i] != null) {
                    i = Math.floorMod(i + 1, hashedEntriesSize);
                }
                theHashedEntries[i] = entry;
            }
        }
    }
}
