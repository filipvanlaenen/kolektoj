package net.filipvanlaenen.kolektoj.array;

import java.lang.reflect.Array;
import java.util.Iterator;
import java.util.Spliterator;

import net.filipvanlaenen.kolektoj.Collection;
import net.filipvanlaenen.kolektoj.Map;
import net.filipvanlaenen.kolektoj.ModifiableCollection;

/**
 * An array backed implementation of the {@link net.filipvanlaenen.kolektoj.Map} interface.
 *
 * @param <K> The key type.
 * @param <V> The value type.
 */
public class ArrayMap<K, V> implements Map<K, V> {
    /**
     * An array with the entries.
     */
    private final Entry<K, V>[] entries;
    /**
     * A hashed array with the entries.
     */
    private final Entry<K, V>[] hashedEntries;
    private Collection<K> keys;
    private Collection<V> values;

    public ArrayMap(final Entry<K, V>... entries) {
        this.entries = entries.clone();
        int hashedArraySize = entries.length * 3;
        Entry<K, V>[] hashedArray = createNewArray(hashedArraySize);
        for (Entry<K, V> entry : entries) {
            int i = entry.key().hashCode() % hashedArraySize;
            while (hashedArray[i] != null) {
                i = (i + 1) % hashedArraySize;
            }
            hashedArray[i] = entry;
        }
        this.hashedEntries = hashedArray;
    }

    @Override
    public boolean contains(final Entry<K, V> entry) {
        return findFirstEntryIndex(entry) != -1;
    }

    @Override
    public boolean containsKey(final K key) {
        return findFirstKeyIndex(key) != -1;
    }

    @Override
    public boolean containsValue(V value) {
        for (Entry<K, V> entry : entries) {
            V v = entry.value();
            if (v == null && value == null || v != null && v.equals(value)) {
                return true;
            }
        }
        return false;
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

    private int findFirstEntryIndex(final Entry<K, V> entry) {
        int hashedEntriesSize = hashedEntries.length;
        int index = entry.key().hashCode() % hashedEntriesSize;
        while (hashedEntries[index] != null) {
            if (hashedEntries[index].equals(entry)) {
                return index;
            }
            index = (index + 1) % hashedEntriesSize;
        }
        return -1;
    }

    private int findFirstKeyIndex(final K key) {
        int hashedEntriesSize = hashedEntries.length;
        int index = key.hashCode() % hashedEntriesSize;
        while (hashedEntries[index] != null) {
            K k = hashedEntries[index].key();
            if (k == null && key == null || k != null && k.equals(key)) {
                return index;
            }
            index = (index + 1) % hashedEntriesSize;
        }
        return -1;
    }

    @Override
    public V get(final K key) throws IllegalArgumentException {
        int index = findFirstKeyIndex(key);
        if (index == -1) {
            throw new IllegalArgumentException("Map doesn't contain an entry with the key " + key + ".");
        }
        return hashedEntries[index].value();
    }

    @Override
    public Collection<V> getAll(final K key) throws IllegalArgumentException {
        int hashedEntriesSize = hashedEntries.length;
        int index = key.hashCode() % hashedEntriesSize;
        ModifiableCollection<V> result = ModifiableCollection.empty();
        while (hashedEntries[index] != null) {
            K k = hashedEntries[index].key();
            if (k == null && key == null || k != null && k.equals(key)) {
                result.add(hashedEntries[index].value());
            }
            index = (index + 1) % hashedEntriesSize;
        }
        if (result.isEmpty()) {
            throw new IllegalArgumentException("Map doesn't contain entries with the key " + key + ".");
        }
        return Collection.of(result.toArray());
    }

    @Override
    public Collection<K> getKeys() {
        if (keys == null) {
            ModifiableCollection<K> result = ModifiableCollection.empty();
            for (Entry<K, V> entry : entries) {
                result.add(entry.key());
            }
            keys = Collection.of(result.toArray());
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
            values = Collection.of(result.toArray());
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
