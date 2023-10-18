package net.filipvanlaenen.kolektoj.hash;

import java.lang.reflect.Array;
import java.util.Iterator;
import java.util.Spliterator;

import net.filipvanlaenen.kolektoj.Collection;
import net.filipvanlaenen.kolektoj.Map;
import net.filipvanlaenen.kolektoj.ModifiableCollection;
import net.filipvanlaenen.kolektoj.ModifiableMap;
import net.filipvanlaenen.kolektoj.array.ArrayCollection;
import net.filipvanlaenen.kolektoj.array.ArrayIterator;
import net.filipvanlaenen.kolektoj.array.ArraySpliterator;
import net.filipvanlaenen.kolektoj.array.ModifiableArrayCollection;

/**
 * A hash backed implementation of the {@link net.filipvanlaenen.kolektoj.ModifiableMap} interface.
 *
 * @param <K> The key type.
 * @param <V> The value type.
 */
public final class ModifiableHashMap<K, V> implements ModifiableMap<K, V> {
    /**
     * The ratio by which the number of entries should be multiplied to construct the hashed array.
     */
    private static final int HASHING_RATIO = 3;
    /**
     * The minimal ratio between the size of the map and the size of the hashed array.
     */
    private static final int MINIMAL_HASHING_RATIO = 2;
    /**
     * The maximal ratio between the size of the map and the size of the hashed array.
     */
    private static final int MAXIMAL_HASHING_RATIO = 4;
    /**
     * The stride for resizing the elements array.
     */
    private static final int STRIDE = 5;
    /**
     * An array with the entries.
     */
    private Entry<K, V>[] entries;
    /**
     * A hashed array with the entries.
     */
    private Entry<K, V>[] hashedEntries;
    /**
     * The size of the hashed array with the entries.
     */
    private int hashedEntriesSize;
    /**
     * A collection with the keys.
     */
    private ModifiableCollection<K> keys;
    /**
     * The size of the map.
     */
    private int size;
    /**
     * A collection with the values.
     */
    private ModifiableCollection<V> values;

    /**
     * Constructor taking the entries as its parameter.
     *
     * @param entries The entries for the map.
     * @throws IllegalArgumentException Thrown if one of the entries is null.
     */
    public ModifiableHashMap(final Entry<K, V>... entries) throws IllegalArgumentException {
        this.entries = entries.clone();
        size = entries.length;
        keys = new ModifiableArrayCollection<K>();
        values = new ModifiableArrayCollection<V>();
        hashedEntriesSize = entries.length * HASHING_RATIO;
        Entry<K, V>[] hashedArray = createNewArray(hashedEntriesSize);
        for (Entry<K, V> entry : entries) {
            if (entry == null) {
                throw new IllegalArgumentException("Map entries can't be null.");
            }
            K key = entry.key();
            keys.add(key);
            values.add(entry.value());
            int i = key == null ? 0 : key.hashCode() % hashedEntriesSize;
            while (hashedArray[i] != null) {
                i = (i + 1) % hashedEntriesSize;
            }
            hashedArray[i] = entry;
        }
        this.hashedEntries = hashedArray;
    }

    @Override
    public boolean add(final K key, final V value) {
        Entry<K, V> entry = new Entry<K, V>(key, value);
        if (size == entries.length) {
            resizeEntriesTo(entries.length + STRIDE);
        }
        entries[size++] = entry;
        if (size * MINIMAL_HASHING_RATIO > hashedEntriesSize) {
            resizeHashedEntriesTo(size * HASHING_RATIO);
        }
        int i = key == null ? 0 : key.hashCode() % hashedEntriesSize;
        while (hashedEntries[i] != null) {
            i = (i + 1) % hashedEntriesSize;
        }
        hashedEntries[i] = entry;
        keys.add(key);
        values.add(value);
        return true;
    }

    @Override
    public boolean addAll(final Map<? extends K, ? extends V> map) {
        int numberOfNewEntries = map.size();
        if (size + numberOfNewEntries > entries.length) {
            resizeEntriesTo(entries.length + numberOfNewEntries + STRIDE);
        }
        if ((size + numberOfNewEntries) * MINIMAL_HASHING_RATIO > hashedEntriesSize) {
            resizeHashedEntriesTo((size + numberOfNewEntries) * HASHING_RATIO);
        }
        int i = 0;
        for (Entry<? extends K, ? extends V> entry : map) {
            K key = entry.key();
            V value = entry.value();
            Entry<K, V> newEntry = new Entry<K, V>(key, value);
            entries[size + i] = newEntry;
            int j = key == null ? 0 : key.hashCode() % hashedEntriesSize;
            while (hashedEntries[i] != null) {
                j = (j + 1) % hashedEntriesSize;
            }
            hashedEntries[j] = newEntry;
            keys.add(key);
            values.add(value);
            i++;
        }
        size += numberOfNewEntries;
        return true;
    }

    @Override
    public boolean contains(final Entry<K, V> entry) {
        return findFirstIndexForEntry(entry) != -1;
    }

    @Override
    public boolean containsAll(Collection<?> collection) {
        if (collection.size() > size) {
            return false;
        }
        boolean[] matches = new boolean[size];
        for (Object element : collection) {
            for (int i = 0; i < size; i++) {
                if (!matches[i] && (element == null && entries[i] == null || entries[i].equals(element))) {
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

    /**
     * Finds the index for the first occurrence of the entry.
     *
     * @param entry The entry.
     * @return Returns the index for the first occurrence of the entry, or -1 if no such entry is present.
     */
    private int findFirstIndexForEntry(final Entry<K, V> entry) {
        if (hashedEntriesSize == 0) {
            return -1;
        }
        K key = entry.key();
        int index = key == null ? 0 : key.hashCode() % hashedEntriesSize;
        while (hashedEntries[index] != null) {
            if (hashedEntries[index].equals(entry)) {
                return index;
            }
            index = (index + 1) % hashedEntriesSize;
        }
        return -1;
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
        int index = key == null ? 0 : key.hashCode() % hashedEntriesSize;
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
    public Entry<K, V> get() throws IndexOutOfBoundsException {
        if (size == 0) {
            throw new IndexOutOfBoundsException("Cannot return an entry from an empty map.");
        } else {
            return entries[0];
        }
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
        int index = key == null ? 0 : key.hashCode() % hashedEntriesSize;
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
        return result;
    }

    @Override
    public Collection<K> getKeys() {
        return new ArrayCollection<K>(keys);
    }

    @Override
    public Collection<V> getValues() {
        return new ArrayCollection<V>(values);
    }

    @Override
    public Iterator<Entry<K, V>> iterator() {
        return new ArrayIterator<Entry<K, V>>(entries);
    }

    @Override
    public V remove(final K key) throws IllegalArgumentException {
        int index = findFirstIndexForKey(key);
        if (index == -1) {
            throw new IllegalArgumentException("Map doesn't contain an entry with the key " + key + ".");
        }
        Entry<K, V> entry = hashedEntries[index];
        V value = entry.value();
        for (int i = 0; i < size; i++) {
            if (entries[i].equals(entry)) {
                entries[i] = entries[size - 1];
                size--;
                break;
            }
        }
        // EQMU: Changing the conditional boundary below produces an equivalent mutant.
        // EQMU: Replacing integer subtraction with addition below produces an equivalent mutant.
        // EQMU: Negating the conditional below produces an equivalent mutant.
        if (size < entries.length - STRIDE) {
            // EQMU: Removing the call to resizeTo below produces an equivalent mutant.
            resizeEntriesTo(size);
        }
        hashedEntries[index] = null;
        if (hashedEntries[index + 1 % hashedEntriesSize] != null || size * MAXIMAL_HASHING_RATIO < hashedEntriesSize) {
            resizeHashedEntriesTo(size * HASHING_RATIO);
        }
        keys.remove(key);
        values.remove(value);
        return value;
    }

    /**
     * Resizes the entries array to the new length. It is assumed that the new length is not less than the current size.
     *
     * @param newLength The new length for the entries array.
     */
    private void resizeEntriesTo(final int newLength) {
        Entry<K, V>[] newElements = createNewArray(newLength);
        System.arraycopy(entries, 0, newElements, 0, size);
        entries = newElements;
    }

    /**
     * Resizes the hashed entries array to the new length. It is assumed that the new length is not less than the
     * current size.
     *
     * @param newLength The new length for the hashed entries array.
     */
    private void resizeHashedEntriesTo(final int newLength) {
        hashedEntriesSize = newLength;
        Entry<K, V>[] hashedArray = createNewArray(hashedEntriesSize);
        for (int i = 0; i < size; i++) {
            Entry<K, V> entry = entries[i];
            K key = entry.key();
            int j = key == null ? 0 : key.hashCode() % hashedEntriesSize;
            while (hashedArray[j] != null) {
                j = (j + 1) % hashedEntriesSize;
            }
            hashedArray[j] = entry;
        }
        this.hashedEntries = hashedArray;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public Spliterator<Entry<K, V>> spliterator() {
        return new ArraySpliterator<Entry<K, V>>(entries, 0);
    }

    @Override
    public Entry<K, V>[] toArray() {
        Entry<K, V>[] result = createNewArray(size);
        System.arraycopy(entries, 0, result, 0, size);
        return result;
    }

    @Override
    public V update(K key, V value) throws IllegalArgumentException {
        int index = findFirstIndexForKey(key);
        if (index == -1) {
            throw new IllegalArgumentException("Map doesn't contain an entry with the key " + key + ".");
        }
        Entry<K, V> oldEntry = hashedEntries[index];
        V oldValue = oldEntry.value();
        if (value != oldValue) {
            Entry<K, V> newEntry = new Entry<K, V>(key, value);
            for (int i = 0; i < size; i++) {
                if (entries[i] == oldEntry) {
                    entries[i] = newEntry;
                    break;
                }
            }
            hashedEntries[index] = newEntry;
            values.remove(oldValue);
            values.add(value);
        }
        return oldValue;
    }
}
