package net.filipvanlaenen.kolektoj.hash;

import static net.filipvanlaenen.kolektoj.Collection.ElementCardinality.DISTINCT_ELEMENTS;
import static net.filipvanlaenen.kolektoj.Collection.ElementCardinality.DUPLICATE_ELEMENTS;
import static net.filipvanlaenen.kolektoj.Map.KeyAndValueCardinality.DISTINCT_KEYS;
import static net.filipvanlaenen.kolektoj.Map.KeyAndValueCardinality.DUPLICATE_KEYS_WITH_DUPLICATE_VALUES;

import java.lang.reflect.Array;
import java.util.Iterator;
import java.util.Objects;
import java.util.Spliterator;

import net.filipvanlaenen.kolektoj.Collection;
import net.filipvanlaenen.kolektoj.ModifiableCollection;
import net.filipvanlaenen.kolektoj.UpdatableMap;
import net.filipvanlaenen.kolektoj.array.ArrayCollection;
import net.filipvanlaenen.kolektoj.array.ModifiableArrayCollection;

/**
 * A hash backed implementation of the {@link net.filipvanlaenen.kolektoj.UpdatableMap} interface.
 *
 * @param <K> The key type.
 * @param <V> The value type.
 */
public final class UpdatableHashMap<K, V> implements UpdatableMap<K, V> {
    /**
     * The ratio by which the number of entries should be multiplied to construct the hashed array.
     */
    private static final int HASHING_RATIO = 3;
    /**
     * A modifiable collection with the entries.
     */
    private ModifiableCollection<Entry<K, V>> entries;
    /**
     * A hashed array with the entries.
     */
    private final Entry<K, V>[] hashedEntries;
    /**
     * The size of the hashed array with the entries.
     */
    private final int hashedEntriesSize;
    /**
     * The key and value cardinality.
     */
    private final KeyAndValueCardinality keyAndValueCardinality;
    /**
     * A collection with the keys, initialized lazily.
     */
    private final Collection<K> keys;
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
    public UpdatableHashMap(final Entry<K, V>... entries) throws IllegalArgumentException {
        this(DISTINCT_KEYS, entries);
    }

    /**
     * Constructor taking the key and value cardinality and the entries as its parameter.
     *
     * @param keyAndValueCardinality The key and value cardinality.
     * @param entries                The entries for the map.
     * @throws IllegalArgumentException Thrown if one of the entries is null.
     */
    public UpdatableHashMap(final KeyAndValueCardinality keyAndValueCardinality, final Entry<K, V>... entries)
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
        this.keys = new ArrayCollection<K>(theKeys);
        this.values = theValues;
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
        return entries.containsAll(collection);
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
        return hashedEntries[index].value();
    }

    @Override
    public Collection<V> getAll(final K key) throws IllegalArgumentException {
        ModifiableCollection<V> result = new ModifiableArrayCollection<V>(
                keyAndValueCardinality == DUPLICATE_KEYS_WITH_DUPLICATE_VALUES ? DUPLICATE_ELEMENTS
                        : DISTINCT_ELEMENTS);
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
    public KeyAndValueCardinality getKeyAndValueCardinality() {
        return keyAndValueCardinality;
    }

    @Override
    public Collection<K> getKeys() {
        return keys;
    }

    @Override
    public Collection<V> getValues() {
        return new ArrayCollection<V>(values);
    }

    @Override
    public Iterator<Entry<K, V>> iterator() {
        return entries.iterator();
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
    public Entry<K, V>[] toArray() {
        return entries.toArray();
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
            entries.remove(oldEntry);
            entries.add(newEntry);
            hashedEntries[index] = newEntry;
            values.remove(oldValue);
            values.add(value);
        }
        return oldValue;
    }
}
