package net.filipvanlaenen.kolektoj.hash;

import java.lang.reflect.Array;
import java.util.Iterator;
import java.util.Objects;
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
        if (entries == null) {
            throw new IllegalArgumentException("Map entries can't be null.");
        }
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
            int i = HashUtilities.hash(key, hashedEntriesSize);
            while (hashedArray[i] != null) {
                i = Math.floorMod(i + 1, hashedEntriesSize);
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
        // EQMU: Changing the conditional boundary below produces an equivalent mutant.
        if (size * MINIMAL_HASHING_RATIO > hashedEntriesSize) {
            resizeHashedEntriesTo(size * HASHING_RATIO);
        }
        int i = HashUtilities.hash(key, hashedEntriesSize);
        while (hashedEntries[i] != null) {
            i = Math.floorMod(i + 1, hashedEntriesSize);
        }
        hashedEntries[i] = entry;
        keys.add(key);
        values.add(value);
        return true;
    }

    @Override
    public boolean addAll(final Map<? extends K, ? extends V> map) {
        if (map.isEmpty()) {
            return false;
        }
        int numberOfNewEntries = map.size();
        // EQMU: Changing the conditional boundary below produces an equivalent mutant.
        if (size + numberOfNewEntries > entries.length) {
            resizeEntriesTo(entries.length + numberOfNewEntries + STRIDE);
        }
        // EQMU: Changing the conditional boundary below produces an equivalent mutant.
        if ((size + numberOfNewEntries) * MINIMAL_HASHING_RATIO > hashedEntriesSize) {
            resizeHashedEntriesTo((size + numberOfNewEntries) * HASHING_RATIO);
        }
        int i = 0;
        for (Entry<? extends K, ? extends V> entry : map) {
            K key = entry.key();
            V value = entry.value();
            Entry<K, V> newEntry = new Entry<K, V>(key, value);
            entries[size + i] = newEntry;
            int j = HashUtilities.hash(key, hashedEntriesSize);
            while (hashedEntries[j] != null) {
                j = Math.floorMod(j + 1, hashedEntriesSize);
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
    public void clear() {
        size = 0;
        // EQMU: Changing the conditional boundary below produces an equivalent mutant.
        // EQMU: Negating the conditional below produces an equivalent mutant.
        if (STRIDE < entries.length) {
            // EQMU: Removing the call to resizeTo below produces an equivalent mutant.
            resizeEntriesTo(STRIDE);
        }
        // EQMU: Replacing integer multiplication with division below produces an equivalent mutant.
        resizeHashedEntriesTo(entries.length * HASHING_RATIO);
        keys.clear();
        values.clear();
    }

    @Override
    public boolean contains(final Entry<K, V> entry) {
        return findFirstIndexForEntry(entry) != -1;
    }

    @Override
    public boolean containsAll(final Collection<?> collection) {
        if (collection.size() > size) {
            return false;
        }
        boolean[] matches = new boolean[size];
        for (Object element : collection) {
            for (int i = 0; i < size; i++) {
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
        return values.contains(value);
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
     * Finds the index for the first occurrence of an entry.
     *
     * @param entry The entry.
     * @return Returns the index for the first occurrence of an entry, or -1 if no such entry is present.
     */
    private int findFirstIndexForEntry(final Entry<? extends K, ? extends V> entry) {
        if (hashedEntriesSize == 0) {
            return -1;
        }
        int index = HashUtilities.hash(entry.key(), hashedEntriesSize);
        while (hashedEntries[index] != null) {
            if (hashedEntries[index].equals(entry)) {
                return index;
            }
            index = Math.floorMod(index + 1, hashedEntriesSize);
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
        ModifiableCollection<V> result = ModifiableCollection.empty();
        int index = HashUtilities.hash(key, hashedEntriesSize);
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
        return new ArrayCollection<K>(keys);
    }

    @Override
    public Collection<V> getValues() {
        return new ArrayCollection<V>(values);
    }

    @Override
    public Iterator<Entry<K, V>> iterator() {
        return new ArrayIterator<Entry<K, V>>(toArray());
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
            resizeEntriesTo(size + STRIDE);
        }
        hashedEntries[index] = null;
        if (hashedEntries[Math.floorMod(index + 1, hashedEntriesSize)] != null
                || size * MAXIMAL_HASHING_RATIO < hashedEntriesSize) {
            resizeHashedEntriesTo(size * HASHING_RATIO);
        }
        keys.remove(key);
        values.remove(value);
        return value;
    }

    @Override
    public boolean removeAll(final Map<? extends K, ? extends V> map) {
        boolean result = false;
        for (Entry<? extends K, ? extends V> e : map) {
            int index = findFirstIndexForEntry(e);
            if (index == -1) {
                break;
            }
            Entry<K, V> entry = hashedEntries[index];
            for (int i = 0; i < size; i++) {
                if (entries[i].equals(entry)) {
                    entries[i] = entries[size - 1];
                    size--;
                    break;
                }
            }
            hashedEntries[index] = null;
            if (hashedEntries[Math.floorMod(index + 1, hashedEntriesSize)] != null) {
                resizeHashedEntriesTo(size * HASHING_RATIO);
            }
            keys.remove(entry.key());
            values.remove(entry.value());
            result = true;
        }
        // EQMU: Changing the conditional boundary below produces an equivalent mutant.
        // EQMU: Replacing integer subtraction with addition below produces an equivalent mutant.
        // EQMU: Negating the conditional below produces an equivalent mutant.
        if (size < entries.length - STRIDE) {
            // EQMU: Removing the call to resizeTo below produces an equivalent mutant.
            resizeEntriesTo(size + STRIDE);
        }
        if (size * MAXIMAL_HASHING_RATIO < hashedEntriesSize) {
            resizeHashedEntriesTo(size * HASHING_RATIO);
        }
        return result;
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
            int j = HashUtilities.hash(entry.key(), hashedEntriesSize);
            while (hashedArray[j] != null) {
                j = Math.floorMod(j + 1, hashedEntriesSize);
            }
            hashedArray[j] = entry;
        }
        this.hashedEntries = hashedArray;
    }

    @Override
    public boolean retainAll(final Map<? extends K, ? extends V> map) {
        boolean[] retain = new boolean[size];
        for (Entry<? extends K, ? extends V> entry : map) {
            for (int i = 0; i < size; i++) {
                if (!retain[i] && Objects.equals(entry, entries[i])) {
                    retain[i] = true;
                    break;
                }
            }
        }
        int i = 0;
        boolean result = false;
        while (i < size) {
            if (retain[i]) {
                i++;
            } else {
                Entry<K, V> entry = entries[i];
                int index = findFirstIndexForEntry(entry);
                for (int j = 0; j < size; j++) {
                    if (entries[j].equals(entry)) {
                        entries[j] = entries[size - 1];
                        size--;
                        break;
                    }
                }
                hashedEntries[index] = null;
                if (hashedEntries[Math.floorMod(index + 1, hashedEntriesSize)] != null) {
                    resizeHashedEntriesTo(size * HASHING_RATIO);
                }
                keys.remove(entry.key());
                values.remove(entry.value());
                retain[i] = retain[size - 1];
                result = true;
            }
        }
        // EQMU: Changing the conditional boundary below produces an equivalent mutant.
        // EQMU: Replacing integer subtraction with addition below produces an equivalent mutant.
        // EQMU: Negating the conditional below produces an equivalent mutant.
        if (size < entries.length - STRIDE) {
            // EQMU: Removing the call to resizeTo below produces an equivalent mutant.
            resizeEntriesTo(size + STRIDE);
        }
        if (size * MAXIMAL_HASHING_RATIO < hashedEntriesSize) {
            resizeHashedEntriesTo(size * HASHING_RATIO);
        }
        return result;
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
    public V update(final K key, final V value) throws IllegalArgumentException {
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
