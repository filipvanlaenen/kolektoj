package net.filipvanlaenen.kolektoj.hash;

import static net.filipvanlaenen.kolektoj.Collection.ElementCardinality.DISTINCT_ELEMENTS;
import static net.filipvanlaenen.kolektoj.Collection.ElementCardinality.DUPLICATE_ELEMENTS;
import static net.filipvanlaenen.kolektoj.Map.KeyAndValueCardinality.DISTINCT_KEYS;
import static net.filipvanlaenen.kolektoj.Map.KeyAndValueCardinality.DUPLICATE_KEYS_WITH_DUPLICATE_VALUES;

import java.lang.reflect.Array;
import java.util.Iterator;
import java.util.Objects;
import java.util.Spliterator;
import java.util.function.Predicate;

import net.filipvanlaenen.kolektoj.Collection;
import net.filipvanlaenen.kolektoj.Map;
import net.filipvanlaenen.kolektoj.ModifiableCollection;
import net.filipvanlaenen.kolektoj.ModifiableMap;
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
     * A modifiable collection with the entries.
     */
    private ModifiableCollection<Entry<K, V>> entries;
    /**
     * A hashed array with the entries.
     */
    private Object[] hashedEntries;
    /**
     * The size of the hashed array with the entries.
     */
    private int hashedEntriesSize;
    /**
     * The key and value cardinality.
     */
    private final KeyAndValueCardinality keyAndValueCardinality;
    /**
     * A modifiable collection with the keys.
     */
    private ModifiableCollection<K> keys;
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
        this(DISTINCT_KEYS, entries);
    }

    /**
     * Constructor taking the key and value cardinality and the entries as its parameter.
     *
     * @param keyAndValueCardinality The key and value cardinality.
     * @param entries                The entries for the map.
     * @throws IllegalArgumentException Thrown if one of the entries is null.
     */
    public ModifiableHashMap(final KeyAndValueCardinality keyAndValueCardinality, final Entry<K, V>... entries)
            throws IllegalArgumentException {
        if (entries == null) {
            throw new IllegalArgumentException("Map entries can't be null.");
        }
        this.keyAndValueCardinality = keyAndValueCardinality;
        hashedEntriesSize = entries.length * HASHING_RATIO;
        Class<Entry<K, V>[]> clazz = (Class<Entry<K, V>[]>) entries.getClass();
        Entry<K, V>[] theHashedEntries = (Entry<K, V>[]) Array.newInstance(clazz.getComponentType(), hashedEntriesSize);
        ModifiableCollection<Entry<K, V>> theEntries =
                new ModifiableArrayCollection<Entry<K, V>>(getElementCardinality());
        ModifiableCollection<K> theKeys = new ModifiableArrayCollection<K>(
                keyAndValueCardinality == DISTINCT_KEYS ? DISTINCT_ELEMENTS : DUPLICATE_ELEMENTS);
        ModifiableCollection<V> theValues = new ModifiableArrayCollection<V>();
        HashUtilities.populateMapFromEntries(theEntries, theHashedEntries, theKeys, theValues, keyAndValueCardinality,
                entries);
        this.entries = theEntries;
        this.hashedEntries = theHashedEntries;
        this.keys = theKeys;
        this.values = theValues;
    }

    /**
     * Constructs a modifiable hash map from another map, with the same entries and the same key and value cardinality.
     *
     * @param map The map to create a new modifiable map from.
     */
    public ModifiableHashMap(final Map<? extends K, ? extends V> map) {
        this(map.getKeyAndValueCardinality());
        addAll(map);
    }

    @Override
    public boolean add(final K key, final V value) {
        if (keyAndValueCardinality == DISTINCT_KEYS && containsKey(key)) {
            return false;
        }
        Entry<K, V> entry = new Entry<K, V>(key, value);
        if (keyAndValueCardinality == KeyAndValueCardinality.DUPLICATE_KEYS_WITH_DISTINCT_VALUES && contains(entry)) {
            return false;
        }
        entries.add(entry);
        // EQMU: Changing the conditional boundary below produces an equivalent mutant.
        if (entries.size() * MINIMAL_HASHING_RATIO > hashedEntriesSize) {
            resizeHashedEntriesTo(entries.size());
        } else {
            int i = HashUtilities.hash(key, hashedEntriesSize);
            while (hashedEntries[i] != null) {
                i = Math.floorMod(i + 1, hashedEntriesSize);
            }
            hashedEntries[i] = entry;
        }
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
        if ((entries.size() + numberOfNewEntries) * MINIMAL_HASHING_RATIO > hashedEntriesSize) {
            resizeHashedEntriesTo(entries.size() + numberOfNewEntries);
        }
        boolean result = false;
        for (Entry<? extends K, ? extends V> entry : map) {
            K key = entry.key();
            if (keyAndValueCardinality == DISTINCT_KEYS && containsKey(key)) {
                continue;
            }
            V value = entry.value();
            Entry<K, V> newEntry = new Entry<K, V>(key, value);
            if (keyAndValueCardinality == KeyAndValueCardinality.DUPLICATE_KEYS_WITH_DISTINCT_VALUES
                    && contains(newEntry)) {
                continue;
            }
            entries.add(newEntry);
            int i = HashUtilities.hash(key, hashedEntriesSize);
            while (hashedEntries[i] != null) {
                i = Math.floorMod(i + 1, hashedEntriesSize);
            }
            hashedEntries[i] = newEntry;
            keys.add(key);
            values.add(value);
            result = true;
        }
        return result;
    }

    @Override
    public void clear() {
        entries.clear();
        resizeHashedEntriesTo(0);
        keys.clear();
        values.clear();
    }

    @Override
    public boolean contains(final Entry<K, V> entry) {
        return findFirstIndexForEntry(entry) != -1;
    }

    @Override
    public boolean containsAll(final Collection<?> collection) {
        return entries.containsAll(collection);
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
            K k = ((Entry<K, V>) hashedEntries[index]).key();
            if (Objects.equals(k, key)) {
                return index;
            }
            index = Math.floorMod(index + 1, hashedEntriesSize);
        }
        return -1;
    }

    @Override
    public Entry<K, V> get() throws IndexOutOfBoundsException {
        if (entries.isEmpty()) {
            throw new IndexOutOfBoundsException("Cannot return an entry from an empty map.");
        } else {
            return entries.get();
        }
    }

    @Override
    public V get(final K key) throws IllegalArgumentException {
        int index = findFirstIndexForKey(key);
        if (index == -1) {
            throw new IllegalArgumentException("Map doesn't contain an entry with the key " + key + ".");
        }
        return ((Entry<K, V>) hashedEntries[index]).value();
    }

    @Override
    public Collection<V> getAll(final K key) throws IllegalArgumentException {
        ModifiableCollection<V> result = new ModifiableArrayCollection<V>(
                keyAndValueCardinality == DUPLICATE_KEYS_WITH_DUPLICATE_VALUES ? DUPLICATE_ELEMENTS
                        : DISTINCT_ELEMENTS);
        int index = HashUtilities.hash(key, hashedEntriesSize);
        while (hashedEntries[index] != null) {
            K k = ((Entry<K, V>) hashedEntries[index]).key();
            if (Objects.equals(k, key)) {
                result.add(((Entry<K, V>) hashedEntries[index]).value());
            }
            index = Math.floorMod(index + 1, hashedEntriesSize);
        }
        if (result.isEmpty()) {
            throw new IllegalArgumentException("Map doesn't contain entries with the key " + key + ".");
        }
        return result;
    }

    @Override
    public KeyAndValueCardinality getKeyAndValueCardinality() {
        return keyAndValueCardinality;
    }

    @Override
    public Collection<K> getKeys() {
        return Collection.<K>of(keys);
    }

    @Override
    public Collection<V> getValues() {
        return Collection.<V>of(values);
    }

    @Override
    public Iterator<Entry<K, V>> iterator() {
        return entries.iterator();
    }

    @Override
    public V remove(final K key) throws IllegalArgumentException {
        int index = findFirstIndexForKey(key);
        if (index == -1) {
            throw new IllegalArgumentException("Map doesn't contain an entry with the key " + key + ".");
        }
        Entry<K, V> entry = (Entry<K, V>) hashedEntries[index];
        V value = entry.value();
        entries.remove(entry);
        hashedEntries[index] = null;
        // EQMU: Changing the conditional boundary below produces an equivalent mutant.
        // EQMU: Negating the second conditional below produces an equivalent mutant.
        // EQMU: Replacing integer multiplication with division below produces an equivalent mutant.
        if (hashedEntries[Math.floorMod(index + 1, hashedEntriesSize)] != null
                || entries.size() * MAXIMAL_HASHING_RATIO < hashedEntriesSize) {
            resizeHashedEntriesTo(entries.size());
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
            Entry<K, V> entry = (Entry<K, V>) hashedEntries[index];
            entries.remove(entry);
            hashedEntries[index] = null;
            if (hashedEntries[Math.floorMod(index + 1, hashedEntriesSize)] != null) {
                resizeHashedEntriesTo(entries.size());
            }
            keys.remove(entry.key());
            values.remove(entry.value());
            result = true;
        }
        // EQMU: Changing the conditional boundary below produces an equivalent mutant.
        // EQMU: Negating the conditional below produces an equivalent mutant.
        // EQMU: Replacing integer multiplication with division below produces an equivalent mutant.
        if (entries.size() * MAXIMAL_HASHING_RATIO < hashedEntriesSize) {
            // EQMU: Removing the call to resizeHashedEntriesTo below produces an equivalent mutant.
            resizeHashedEntriesTo(entries.size());
        }
        return result;
    }

    @Override
    public boolean removeIf(final Predicate<Entry<? extends K, ? extends V>> predicate) {
        int size = entries.size();
        boolean[] retain = new boolean[size];
        Object[] entriesArray = entries.toArray();
        for (int i = 0; i < size; i++) {
            retain[i] = !predicate.test((Entry<K, V>) entriesArray[i]);
        }
        return retainAndRehash(entriesArray, retain);
    }

    /**
     * Resizes the hashed entries array to the new base length. The base length will be multiplied by a ratio to
     * calculate the actual new length for the hashed entries array.
     *
     * @param newBaseLength The new base length for the hashed entries array.
     */
    private void resizeHashedEntriesTo(final int newBaseLength) {
        hashedEntriesSize = newBaseLength * HASHING_RATIO;
        Object[] hashedArray = new Object[hashedEntriesSize];
        for (Entry<K, V> entry : entries) {
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
        int size = entries.size();
        boolean[] retain = new boolean[size];
        Object[] entriesArray = entries.toArray();
        for (Entry<? extends K, ? extends V> entry : map) {
            for (int i = 0; i < size; i++) {
                if (!retain[i] && Objects.equals(entry, entriesArray[i])) {
                    retain[i] = true;
                    break;
                }
            }
        }
        return retainAndRehash(entriesArray, retain);
    }

    /**
     * Retains the entries according to a retention array and rehashes if necessary.
     *
     * @param entriesArray A array with the entries.
     * @param retain       The retention array.
     * @return True if at least one entry was removed.
     */
    private boolean retainAndRehash(final Object[] entriesArray, final boolean[] retain) {
        int size = retain.length;
        boolean result = false;
        for (int i = 0; i < size; i++) {
            if (!retain[i]) {
                Entry<K, V> entry = (Entry<K, V>) entriesArray[i];
                int index = findFirstIndexForEntry(entry);
                entries.remove(entry);
                hashedEntries[index] = null;
                if (hashedEntries[Math.floorMod(index + 1, hashedEntriesSize)] != null) {
                    resizeHashedEntriesTo(size);
                }
                keys.remove(entry.key());
                values.remove(entry.value());
                result = true;
            }
        }
        // EQMU: Changing the conditional boundary below produces an equivalent mutant.
        // EQMU: Negating the conditional below produces an equivalent mutant.
        // EQMU: Replacing integer multiplication with division below produces an equivalent mutant.
        if (size * MAXIMAL_HASHING_RATIO < hashedEntriesSize) {
            // EQMU: Removing the call to resizeHashedEntriesTo below produces an equivalent mutant.
            resizeHashedEntriesTo(size);
        }
        return result;
    }

    @Override
    public int size() {
        return entries.size();
    }

    @Override
    public Spliterator<Entry<K, V>> spliterator() {
        return entries.spliterator();
    }

    @Override
    public Object[] toArray() {
        return entries.toArray();
    }

    @Override
    public V update(final K key, final V value) throws IllegalArgumentException {
        int index = findFirstIndexForKey(key);
        if (index == -1) {
            throw new IllegalArgumentException("Map doesn't contain an entry with the key " + key + ".");
        }
        Entry<K, V> oldEntry = (Entry<K, V>) hashedEntries[index];
        V oldValue = oldEntry.value();
        if (value != oldValue) {
            Entry<K, V> newEntry = new Entry<K, V>(key, value);
            entries.remove(oldEntry);
            entries.add(newEntry);
            hashedEntries[index] = newEntry;
            values.remove(oldValue);
            values.add(value);
        }
        return oldValue;
    }
}
