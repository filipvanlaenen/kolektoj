package net.filipvanlaenen.kolektoj.hash;

import java.lang.reflect.Array;
import java.util.Iterator;
import java.util.Objects;
import java.util.Spliterator;

import net.filipvanlaenen.kolektoj.Collection;
import net.filipvanlaenen.kolektoj.Map;
import net.filipvanlaenen.kolektoj.ModifiableCollection;
import net.filipvanlaenen.kolektoj.array.ArrayCollection;
import net.filipvanlaenen.kolektoj.array.ArrayIterator;
import net.filipvanlaenen.kolektoj.array.ArraySpliterator;

/**
 * A hash backed implementation of the {@link net.filipvanlaenen.kolektoj.Map} interface.
 *
 * @param <K> The key type.
 * @param <V> The value type.
 */
public final class HashMap<K, V> implements Map<K, V> {
    /**
     * The ratio by which the number of entries should be multiplied to construct the hashed array.
     */
    private static final int HASHING_RATIO = 3;
    /**
     * An array with the entries.
     */
    private final Entry<K, V>[] entries;
    /**
     * A hashed array with the entries.
     */
    private final Entry<K, V>[] hashedEntries;
    /**
     * The size of the hashed array with the entries.
     */
    private final int hashedEntriesSize;
    /**
     * A collection with the keys, initialized lazily.
     */
    private Collection<K> keys;
    /**
     * A collection with the values, initialized lazily.
     */
    private Collection<V> values;

    /**
     * Constructor taking the entries as its parameter.
     *
     * @param entries The entries for the map.
     * @throws IllegalArgumentException Thrown if one of the entries is null.
     */
    public HashMap(final Entry<K, V>... entries) throws IllegalArgumentException {
        this.entries = entries.clone();
        hashedEntriesSize = entries.length * HASHING_RATIO;
        Entry<K, V>[] hashedArray = createNewArray(hashedEntriesSize);
        for (Entry<K, V> entry : entries) {
            if (entry == null) {
                throw new IllegalArgumentException("Map entries can't be null.");
            }
            int i = HashUtilities.hash(entry.key(), hashedEntriesSize);
            while (hashedArray[i] != null) {
                i = Math.floorMod(i + 1, hashedEntriesSize);
            }
            hashedArray[i] = entry;
        }
        this.hashedEntries = hashedArray;
    }

    @Override
    public boolean contains(final Entry<K, V> entry) {
        if (hashedEntriesSize == 0) {
            return false;
        }
        int index = HashUtilities.hash(entry.key(), hashedEntriesSize);
        while (hashedEntries[index] != null) {
            if (hashedEntries[index].equals(entry)) {
                return true;
            }
            index = Math.floorMod(index + 1, hashedEntriesSize);
        }
        return false;
    }

    @Override
    public boolean containsAll(final Collection<?> collection) {
        if (collection.size() > size()) {
            return false;
        }
        boolean[] matches = new boolean[entries.length];
        for (Object element : collection) {
            for (int i = 0; i < entries.length; i++) {
                if (!matches[i] && Objects.equals(element, entries[i])) {
                    matches[i] = true;
                    break;
                }
            }
        }
        for (boolean match : matches) {
            if (!match) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean containsKey(final K key) {
        return findFirstIndexForKey(key) != -1;
    }

    @Override
    public boolean containsValue(final V value) {
        return getValues().contains(value);
    }

    /**
     * Creates a new entry array with a given length.
     *
     * @param length The length of the array.
     * @return An array of the given length with the entry type.
     */
    private Entry<K, V>[] createNewArray(final int length) {
        Class<Entry<K, V>[]> clazz = (Class<Entry<K, V>[]>) entries.getClass();
        return (Entry<K, V>[]) Array.newInstance(clazz.getComponentType(), length);
    }

    @Override
    public Entry<K, V> get() throws IndexOutOfBoundsException {
        if (entries.length == 0) {
            throw new IndexOutOfBoundsException("Cannot return an entry from an empty map.");
        } else {
            return entries[0];
        }
    }

    @Override
    public Iterator<Entry<K, V>> iterator() {
        return new ArrayIterator<Entry<K, V>>(entries);
    }

    /**
     * Finds the index for the first occurrence of an entry with the key.
     *
     * @param key The key.
     * @return Returns the index for the first occurrence of an entry with the key, or -1 if no such entry is present.
     */
    private int findFirstIndexForKey(final K key) {
        if (hashedEntriesSize == 0) {
            return -1;
        }
        int index = HashUtilities.hash(key, hashedEntriesSize);
        while (hashedEntries[index] != null) {
            K k = hashedEntries[index].key();
            if (Objects.equals(k, key)) {
                return index;
            }
            index = Math.floorMod(index + 1, hashedEntriesSize);
        }
        return -1;
    }

    @Override
    public V get(final K key) throws IllegalArgumentException {
        int index = findFirstIndexForKey(key);
        if (index == -1) {
            throw new IllegalArgumentException("Map doesn't contain an entry with the key " + key + ".");
        }
        return hashedEntries[index].value();
    }

    @Override
    public Collection<V> getAll(final K key) throws IllegalArgumentException {
        int index = HashUtilities.hash(key, hashedEntriesSize);
        ModifiableCollection<V> result = ModifiableCollection.empty();
        while (hashedEntries[index] != null) {
            K k = hashedEntries[index].key();
            if (Objects.equals(k, key)) {
                result.add(hashedEntries[index].value());
            }
            index = Math.floorMod(index + 1, hashedEntriesSize);
        }
        if (result.isEmpty()) {
            throw new IllegalArgumentException("Map doesn't contain entries with the key " + key + ".");
        }
        return result;
    }

    @Override
    public ElementCardinality getElementCardinality() {
        // TODO: Auto-generated method stub
        return null;
    }

    @Override
    public Collection<K> getKeys() {
        if (keys == null) {
            ModifiableCollection<K> result = ModifiableCollection.empty();
            for (Entry<K, V> entry : entries) {
                result.add(entry.key());
            }
            keys = new ArrayCollection<K>(result);
        }
        return keys;
    }

    @Override
    public Collection<V> getValues() {
        if (values == null) {
            ModifiableCollection<V> result = ModifiableCollection.empty();
            for (Entry<K, V> entry : entries) {
                result.add(entry.value());
            }
            values = new ArrayCollection<V>(result);
        }
        return values;
    }

    @Override
    public int size() {
        return entries.length;
    }

    @Override
    public Spliterator<Entry<K, V>> spliterator() {
        return new ArraySpliterator<Entry<K, V>>(entries, 0);
    }

    @Override
    public Entry<K, V>[] toArray() {
        return entries.clone();
    }
}
