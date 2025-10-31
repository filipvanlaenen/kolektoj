package net.filipvanlaenen.kolektoj.array;

import static net.filipvanlaenen.kolektoj.Collection.ElementCardinality.DISTINCT_ELEMENTS;
import static net.filipvanlaenen.kolektoj.Collection.ElementCardinality.DUPLICATE_ELEMENTS;
import static net.filipvanlaenen.kolektoj.Map.KeyAndValueCardinality.*;

import java.util.Comparator;
import java.util.Iterator;
import java.util.Objects;
import java.util.Spliterator;

import net.filipvanlaenen.kolektoj.Collection;
import net.filipvanlaenen.kolektoj.Map;
import net.filipvanlaenen.kolektoj.ModifiableCollection;
import net.filipvanlaenen.kolektoj.SortedCollection;
import net.filipvanlaenen.kolektoj.UpdatableSortedMap;
import net.filipvanlaenen.kolektoj.sortedtree.ModifiableSortedTreeCollection;

/**
 * An array backed implementation of the {@link net.filipvanlaenen.kolektoj.UpdatableSortedMap} interface.
 *
 * @param <K> The key type.
 * @param <V> The value type.
 */
public final class UpdatableSortedArrayMap<K, V> implements UpdatableSortedMap<K, V> {
    /**
     * The comparator for the keys.
     */
    private final Comparator<? super K> comparator;
    /**
     * A sorted array with the entries.
     */
    private final Object[] entries;
    /**
     * The key and value cardinality.
     */
    private final KeyAndValueCardinality keyAndValueCardinality;
    /**
     * The comparator to use for comparing entries using the keys only in this updatable sorted map.
     */
    private final Comparator<Entry<K, V>> entryByKeyComparator;
    /**
     * A sorted collection with the keys.
     */
    private final SortedCollection<K> keys;
    /**
     * A collection with the values.
     */
    private final ModifiableCollection<V> values;

    /**
     * Constructor taking the entries as its parameter.
     *
     * @param comparator The comparator by which to sort the keys.
     * @param entries    The entries for the map.
     * @throws IllegalArgumentException Thrown if one of the entries is null.
     */
    public UpdatableSortedArrayMap(final Comparator<? super K> comparator, final Entry<K, V>... entries)
            throws IllegalArgumentException {
        this(DISTINCT_KEYS, comparator, entries);
    }

    /**
     * Constructor taking the key and value cardinality and the entries as its parameter.
     *
     * @param keyAndValueCardinality The key and value cardinality.
     * @param comparator             The comparator by which to sort the keys.
     * @param entries                The entries for the map.
     * @throws IllegalArgumentException Thrown if one of the entries is null.
     */
    public UpdatableSortedArrayMap(final KeyAndValueCardinality keyAndValueCardinality,
            final Comparator<? super K> comparator, final Entry<K, V>... entries) throws IllegalArgumentException {
        this(keyAndValueCardinality, comparator, (Object[]) entries);
    }

    /**
     * Constructor taking the key and value cardinality, the comparator and the entries as an object array as its
     * parameter.
     *
     * @param keyAndValueCardinality The key and value cardinality.
     * @param comparator             The comparator by which to sort the keys.
     * @param entries                The entries for the map.
     * @throws IllegalArgumentException Thrown if one of the entries is null.
     */
    private UpdatableSortedArrayMap(final KeyAndValueCardinality keyAndValueCardinality,
            final Comparator<? super K> comparator, final Object[] entries) throws IllegalArgumentException {
        if (entries == null) {
            throw new IllegalArgumentException("Map entries can't be null.");
        }
        for (Object entry : entries) {
            if (entry == null) {
                throw new IllegalArgumentException("Map entries can't be null.");
            }
        }
        this.comparator = comparator;
        this.entryByKeyComparator = new Comparator<Entry<K, V>>() {
            @Override
            public int compare(final Entry<K, V> e1, final Entry<K, V> e2) {
                return comparator.compare(e1.key(), e2.key());
            }
        };
        this.keyAndValueCardinality = keyAndValueCardinality;
        if (keyAndValueCardinality == DISTINCT_KEYS) {
            this.entries =
                    ArrayUtilities.quicksort(ArrayUtilities.cloneDistinctElements(entries), entryByKeyComparator);
        } else {
            this.entries = ArrayUtilities.quicksort(entries, entryByKeyComparator);
        }
        ModifiableCollection<K> theKeys = new ModifiableSortedTreeCollection<K>(
                keyAndValueCardinality == DISTINCT_KEYS ? DISTINCT_ELEMENTS : DUPLICATE_ELEMENTS, comparator);
        ModifiableCollection<V> theValues = new ModifiableArrayCollection<V>();
        for (Object object : entries) {
            Entry<K, V> entry = (Entry<K, V>) object;
            theKeys.add(entry.key());
            theValues.add(entry.value());
        }
        this.keys = new SortedArrayCollection<K>(comparator, theKeys);
        this.values = new ModifiableArrayCollection<V>(theValues);
    }

    /**
     * Constructs a map from another map, with the same entries and the same key and value cardinality.
     *
     * @param comparator The comparator by which to sort the keys.
     * @param map        The map to create a new map from.
     */
    public UpdatableSortedArrayMap(final Comparator<? super K> comparator, final Map<K, V> map) {
        this(map.getKeyAndValueCardinality(), comparator, map.toArray());
    }

    @Override
    public boolean contains(final Entry<K, V> entry) {
        return ArrayUtilities.contains(entries, entries.length, entry, entryByKeyComparator);
    }

    @Override
    public boolean containsAll(final Collection<?> collection) {
        return ArrayUtilities.containsAll(entries, entries.length, collection, entryByKeyComparator);
    }

    @Override
    public boolean containsKey(final K key) {
        return keys.contains(key);
    }

    @Override
    public boolean containsValue(final V value) {
        return values.contains(value);
    }

    @Override
    public Entry<K, V> get() throws IndexOutOfBoundsException {
        if (entries.length == 0) {
            throw new IndexOutOfBoundsException("Cannot return an entry from an empty map.");
        } else {
            return (Entry<K, V>) entries[0];
        }
    }

    @Override
    public V get(final K key) throws IllegalArgumentException {
        int index = ArrayUtilities.findIndex(entries, entries.length, new Entry<K, V>(key, null), entryByKeyComparator);
        if (index == -1) {
            throw new IllegalArgumentException("Map doesn't contain an entry with the key " + key + ".");
        }
        return getValueAt(index);
    }

    @Override
    public Collection<V> getAll(final K key) throws IllegalArgumentException {
        int index = ArrayUtilities.findIndex(entries, entries.length, new Entry<K, V>(key, null), entryByKeyComparator);
        if (index == -1) {
            throw new IllegalArgumentException("Map doesn't contain entries with the key " + key + ".");
        }
        ModifiableCollection<V> result = new ModifiableArrayCollection<V>(
                keyAndValueCardinality == DUPLICATE_KEYS_WITH_DUPLICATE_VALUES ? DUPLICATE_ELEMENTS : DISTINCT_ELEMENTS,
                getValueAt(index));
        if (!keyAndValueCardinality.equals(DISTINCT_KEYS)) {
            int i = index - 1;
            while (i >= 0 && Objects.equals(key, getKeyAt(i))) {
                result.add(getValueAt(i));
                i--;
            }
            i = index + 1;
            while (i < entries.length && Objects.equals(key, getKeyAt(i))) {
                result.add(getValueAt(i));
                i++;
            }
        }
        return result;
    }

    @Override
    public Comparator<? super K> getComparator() {
        return comparator;
    }

    @Override
    public Entry<K, V> getGreaterThan(final K key) throws IndexOutOfBoundsException {
        if (entries.length == 0) {
            throw new IndexOutOfBoundsException("Cannot return an entry from an empty map.");
        }
        int index = getIndexGreaterThan(key);
        if (index == entries.length) {
            throw new IndexOutOfBoundsException(
                    "Cannot return an entry from the map with a key that's greater than the provided value.");
        }
        return (Entry<K, V>) entries[index];
    }

    @Override
    public Entry<K, V> getGreaterThanOrEqualTo(final K key) throws IndexOutOfBoundsException {
        if (entries.length == 0) {
            throw new IndexOutOfBoundsException("Cannot return an entry from an empty map.");
        }
        int i = ArrayUtilities.findInsertionIndex(entries, entries.length, new Entry<K, V>(key, null),
                entryByKeyComparator);
        if (i == entries.length) {
            throw new IndexOutOfBoundsException("Cannot return an entry from the map with a key that's greater than or"
                    + " equal to the provided value.");
        }
        return (Entry<K, V>) entries[i];
    }

    @Override
    public Entry<K, V> getGreatest() throws IndexOutOfBoundsException {
        if (entries.length == 0) {
            throw new IndexOutOfBoundsException("Cannot return an entry from an empty map.");
        } else {
            return (Entry<K, V>) entries[entries.length - 1];
        }
    }

    @Override
    public K getGreatestKey() throws IndexOutOfBoundsException {
        if (entries.length == 0) {
            throw new IndexOutOfBoundsException("Cannot return a key from an empty map.");
        } else {
            return getKeyAt(entries.length - 1);
        }
    }

    private int getIndexGreaterThan(final K key) {
        int i = ArrayUtilities.findInsertionIndex(entries, entries.length, new Entry<K, V>(key, null),
                entryByKeyComparator);
        while (i < entries.length && comparator.compare(key, getKeyAt(i)) == 0) {
            i++;
        }
        return i;
    }

    private int getIndexLessThan(final K key) {
        int i = ArrayUtilities.findInsertionIndex(entries, entries.length, new Entry<K, V>(key, null),
                entryByKeyComparator);
        while (i >= 0 && comparator.compare(key, getKeyAt(i)) <= 0) {
            i--;
        }
        return i;
    }

    private int getIndexLessThanOrEqualTo(final K key) {
        int i = ArrayUtilities.findInsertionIndex(entries, entries.length, new Entry<K, V>(key, null),
                entryByKeyComparator);
        while (i >= 0 && comparator.compare(key, getKeyAt(i)) < 0) {
            i--;
        }
        return i;
    }

    @Override
    public KeyAndValueCardinality getKeyAndValueCardinality() {
        return keyAndValueCardinality;
    }

    private K getKeyAt(final int index) {
        return ((Entry<K, V>) entries[index]).key();
    }

    @Override
    public K getKeyGreaterThan(final K key) throws IndexOutOfBoundsException {
        if (entries.length == 0) {
            throw new IndexOutOfBoundsException("Cannot return a key from an empty map.");
        }
        int index = getIndexGreaterThan(key);
        if (index == entries.length) {
            throw new IndexOutOfBoundsException(
                    "Cannot return a key from the map that's greater than the provided value.");
        }
        return getKeyAt(index);
    }

    @Override
    public K getKeyGreaterThanOrEqualTo(final K key) throws IndexOutOfBoundsException {
        if (entries.length == 0) {
            throw new IndexOutOfBoundsException("Cannot return a key from an empty map.");
        }
        int index = ArrayUtilities.findInsertionIndex(entries, entries.length, new Entry<K, V>(key, null),
                entryByKeyComparator);
        if (index == entries.length) {
            throw new IndexOutOfBoundsException(
                    "Cannot return a key from the map that's greater than or equal to the provided value.");
        }
        return getKeyAt(index);
    }

    @Override
    public K getKeyLessThan(final K key) throws IndexOutOfBoundsException {
        if (entries.length == 0) {
            throw new IndexOutOfBoundsException("Cannot return a key from an empty map.");
        }
        int index = getIndexLessThan(key);
        if (index == -1) {
            throw new IndexOutOfBoundsException(
                    "Cannot return a key from the map that's less than the provided value.");
        }
        return getKeyAt(index);
    }

    @Override
    public K getKeyLessThanOrEqualTo(final K key) throws IndexOutOfBoundsException {
        if (entries.length == 0) {
            throw new IndexOutOfBoundsException("Cannot return a key from an empty map.");
        }
        int index = getIndexLessThanOrEqualTo(key);
        if (index == -1) {
            throw new IndexOutOfBoundsException(
                    "Cannot return a key from the map that's less than or equal to the provided value.");
        }
        return getKeyAt(index);
    }

    @Override
    public SortedCollection<K> getKeys() {
        return keys;
    }

    @Override
    public Entry<K, V> getLeast() throws IndexOutOfBoundsException {
        if (entries.length == 0) {
            throw new IndexOutOfBoundsException("Cannot return an entry from an empty map.");
        } else {
            return (Entry<K, V>) entries[0];
        }
    }

    @Override
    public K getLeastKey() throws IndexOutOfBoundsException {
        if (entries.length == 0) {
            throw new IndexOutOfBoundsException("Cannot return a key from an empty map.");
        } else {
            return getKeyAt(0);
        }
    }

    @Override
    public Entry<K, V> getLessThan(final K key) throws IndexOutOfBoundsException {
        if (entries.length == 0) {
            throw new IndexOutOfBoundsException("Cannot return an entry from an empty map.");
        }
        int index = getIndexLessThan(key);
        if (index == -1) {
            throw new IndexOutOfBoundsException(
                    "Cannot return an entry from the map with a key that's less than the provided value.");
        }
        return (Entry<K, V>) entries[index];
    }

    @Override
    public Entry<K, V> getLessThanOrEqualTo(final K key) throws IndexOutOfBoundsException {
        if (entries.length == 0) {
            throw new IndexOutOfBoundsException("Cannot return an entry from an empty map.");
        }
        int index = getIndexLessThanOrEqualTo(key);
        if (index == -1) {
            throw new IndexOutOfBoundsException(
                    "Cannot return an entry from the map with a key that's less than or equal to the provided value.");
        }
        return (Entry<K, V>) entries[index];
    }

    private V getValueAt(final int index) {
        return ((Entry<K, V>) entries[index]).value();
    }

    @Override
    public Collection<V> getValues() {
        return Collection.<V>of(values);
    }

    @Override
    public Iterator<Entry<K, V>> iterator() {
        return new ArrayIterator<Entry<K, V>>(entries);
    }

    @Override
    public int size() {
        return entries.length;
    }

    @Override
    public Spliterator<Entry<K, V>> spliterator() {
        int characteristics = Spliterator.ORDERED | Spliterator.SORTED
                | (keyAndValueCardinality == DUPLICATE_KEYS_WITH_DUPLICATE_VALUES ? 0 : Spliterator.DISTINCT);
        return new ArraySpliterator<Entry<K, V>>(entries, characteristics, entryByKeyComparator);
    }

    @Override
    public Object[] toArray() {
        return entries.clone();
    }

    @Override
    public V update(final K key, final V value) throws IllegalArgumentException {
        Entry<K, V> newEntry = new Entry<K, V>(key, value);
        if (contains(newEntry)) {
            return value;
        }
        int index = ArrayUtilities.findIndex(entries, entries.length, newEntry, entryByKeyComparator);
        if (index == -1) {
            throw new IllegalArgumentException("Map doesn't contain an entry with the key " + key + ".");
        }
        V oldValue = getValueAt(index);
        entries[index] = newEntry;
        values.remove(oldValue);
        values.add(value);
        return oldValue;
    }

    @Override
    public boolean update(final K key, final V oldValue, final V newValue) throws IllegalArgumentException {
        Entry<K, V> oldEntry = new Entry<K, V>(key, oldValue);
        int index = ArrayUtilities.findIndex(entries, entries.length, oldEntry, entryByKeyComparator);
        if (index == -1) {
            throw new IllegalArgumentException(
                    "Map doesn't contain an entry with the key " + key + " and value " + oldValue + ".");
        }
        while (!entries[index].equals(oldEntry) && index > 0
                && entryByKeyComparator.compare(oldEntry, (Entry<K, V>) entries[index - 1]) == 0) {
            index--;
        }
        while (!entries[index].equals(oldEntry) && index < entries.length - 1
                && entryByKeyComparator.compare(oldEntry, (Entry<K, V>) entries[index + 1]) == 0) {
            index++;
        }
        if (!entries[index].equals(oldEntry)) {
            throw new IllegalArgumentException(
                    "Map doesn't contain an entry with the key " + key + " and value " + oldValue + ".");
        }
        if (Objects.equals(oldValue, newValue)) {
            return false;
        }
        Entry<K, V> newEntry = new Entry<K, V>(key, newValue);
        if (keyAndValueCardinality == DUPLICATE_KEYS_WITH_DISTINCT_VALUES && contains(newEntry)) {
            return false;
        }
        entries[index] = newEntry;
        values.remove(oldValue);
        values.add(newValue);
        return true;
    }
}
