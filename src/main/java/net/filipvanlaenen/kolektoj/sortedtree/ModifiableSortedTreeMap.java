package net.filipvanlaenen.kolektoj.sortedtree;

import static net.filipvanlaenen.kolektoj.Collection.ElementCardinality.DISTINCT_ELEMENTS;
import static net.filipvanlaenen.kolektoj.Collection.ElementCardinality.DUPLICATE_ELEMENTS;
import static net.filipvanlaenen.kolektoj.Map.KeyAndValueCardinality.DISTINCT_KEYS;
import static net.filipvanlaenen.kolektoj.Map.KeyAndValueCardinality.DUPLICATE_KEYS_WITH_DUPLICATE_VALUES;

import java.lang.reflect.Array;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Objects;
import java.util.Spliterator;
import java.util.function.Predicate;

import net.filipvanlaenen.kolektoj.Collection;
import net.filipvanlaenen.kolektoj.Map;
import net.filipvanlaenen.kolektoj.ModifiableCollection;
import net.filipvanlaenen.kolektoj.ModifiableSortedCollection;
import net.filipvanlaenen.kolektoj.ModifiableSortedMap;
import net.filipvanlaenen.kolektoj.array.ArrayCollection;
import net.filipvanlaenen.kolektoj.array.ArrayIterator;
import net.filipvanlaenen.kolektoj.array.ArraySpliterator;
import net.filipvanlaenen.kolektoj.array.ArrayUtilities;
import net.filipvanlaenen.kolektoj.array.ModifiableArrayCollection;

/**
 * An implementation of the {@link net.filipvanlaenen.kolektoj.ModifiableSortedMap} interface backed by a sorted tree,
 * in particular an AVL tree.
 *
 * @param <K> The key type.
 * @param <V> The value type.
 */
public final class ModifiableSortedTreeMap<K, V> implements ModifiableSortedMap<K, V> {
    /**
     * The comparator to use for comparing the keys in this sorted map.
     */
    private final Comparator<K> comparator;
    /**
     * A sorted array with the entries.
     */
    private Entry<K, V>[] cachedArray;
    /**
     * A boolean flag indicating whether the cachedArray field is dirty.
     */
    private boolean cachedArrayDirty;
    /**
     * The key and value cardinality.
     */
    private final KeyAndValueCardinality keyAndValueCardinality;
    /**
     * The comparator to use for comparing entries using the keys only in this sorted map.
     */
    private final Comparator<Entry<K, V>> entryByKeyComparator;
    /**
     * The comparator to use for comparing entries using the keys and the values in this sorted map.
     */
    private final Comparator<Entry<K, V>> entryByKeyAndValueComparator;
    /**
     * A sorted collection with the keys.
     */
    private final ModifiableSortedCollection<K> keys;
    /**
     * The size of the collection.
     */
    private int size;
    /**
     * The sorted tree with the entries.
     */
    private final SortedTree<K, ModifiableCollection<V>> sortedTree;
    /**
     * A collection with the values.
     */
    private final ModifiableCollection<V> values;

    /**
     * Constructor taking the entries as its parameter.
     *
     * @param entries The entries for the map.
     * @throws IllegalArgumentException Thrown if one of the entries is null.
     */
    public ModifiableSortedTreeMap(final Comparator<K> comparator, final Entry<K, V>... entries)
            throws IllegalArgumentException {
        this(DISTINCT_KEYS, comparator, entries);
    }

    /**
     * Constructor taking the key and value cardinality and the entries as its parameter.
     *
     * @param keyAndValueCardinality The key and value cardinality.
     * @param entries                The entries for the map.
     * @throws IllegalArgumentException Thrown if one of the entries is null.
     */
    public ModifiableSortedTreeMap(final KeyAndValueCardinality keyAndValueCardinality, final Comparator<K> comparator,
            final Entry<K, V>... entries) throws IllegalArgumentException {
        if (entries == null) {
            throw new IllegalArgumentException("Map entries can't be null.");
        }
        for (Entry<K, V> entry : entries) {
            if (entry == null) {
                throw new IllegalArgumentException("Map entries can't be null.");
            }
        }

        this.comparator = comparator;
        this.entryByKeyComparator = new Comparator<Entry<K, V>>() {
            @Override
            public int compare(Entry<K, V> e1, Entry<K, V> e2) {
                return comparator.compare(e1.key(), e2.key());
            }
        };
        this.entryByKeyAndValueComparator = new Comparator<Entry<K, V>>() {
            @Override
            public int compare(Entry<K, V> e1, Entry<K, V> e2) {
                int keyComparison = comparator.compare(e1.key(), e2.key());
                if (keyComparison == 0) {
                    if (Objects.equals(e1.value(), e2.value())) {
                        return 0;
                    } else {
                        return 1;
                    }
                } else {
                    return keyComparison;
                }
            }
        };
        this.keyAndValueCardinality = keyAndValueCardinality;
        if (keyAndValueCardinality == DISTINCT_KEYS) {
            cachedArray = ArrayUtilities.quicksort(ArrayUtilities.cloneDistinctElements(entries), entryByKeyComparator);
            cachedArrayDirty = cachedArray.length != entries.length;
        } else {
            cachedArray = ArrayUtilities.quicksort(entries, entryByKeyComparator);
            cachedArrayDirty = false;
        }
        size = this.cachedArray.length;
        sortedTree = SortedTree.fromSortedArray(comparator, keyAndValueCardinality, compact(this.cachedArray));
        ModifiableCollection<K> theKeys = new ModifiableSortedTreeCollection<K>(
                keyAndValueCardinality == DISTINCT_KEYS ? DISTINCT_ELEMENTS : DUPLICATE_ELEMENTS, comparator);
        ModifiableCollection<V> theValues = new ModifiableArrayCollection<V>();
        for (Entry<K, V> entry : entries) {
            theKeys.add(entry.key());
            theValues.add(entry.value());
        }
        this.keys = new ModifiableSortedTreeCollection<K>(comparator, theKeys);
        this.values = new ModifiableArrayCollection<V>(theValues);
    }

    @Override
    public boolean add(K key, V value) {
        Node<K, ModifiableCollection<V>> node = sortedTree.getNode(key);
        boolean changed;
        if (node == null) {
            changed = sortedTree.add(key,
                    new ModifiableArrayCollection<V>(
                            keyAndValueCardinality == DUPLICATE_KEYS_WITH_DUPLICATE_VALUES ? DUPLICATE_ELEMENTS
                                    : DISTINCT_ELEMENTS,
                            value));
        } else {
            changed = node.getContent().add(value);
        }
        if (changed) {
            size++;
        }
        values.add(value);
        keys.add(key);
        cachedArrayDirty = cachedArrayDirty || changed;
        return changed;
    }

    @Override
    public boolean addAll(Map<? extends K, ? extends V> map) {
        boolean result = false;
        for (Entry<? extends K, ? extends V> entry : map) {
            result |= add(entry.key(), entry.value());
        }
        return result;
    }

    @Override
    public void clear() {
        sortedTree.clear();
        cachedArrayDirty = cachedArray.length != 0;
    }

    private Entry<K, ModifiableCollection<V>>[] createModifiableCollectionEntryArray(final int length,
            Entry<K, ModifiableCollection<V>>... foo) {
        Class<Entry<K, ModifiableCollection<V>>> elementType =
                (Class<Entry<K, ModifiableCollection<V>>>) foo.getClass().getComponentType();
        return (Entry<K, ModifiableCollection<V>>[]) Array.newInstance(elementType, length);
    }

    private Entry<K, ModifiableCollection<V>>[] compact(Entry<K, V>[] entries) {
        int originalLength = entries.length;
        ElementCardinality cardinality =
                keyAndValueCardinality == DUPLICATE_KEYS_WITH_DUPLICATE_VALUES ? DUPLICATE_ELEMENTS : DISTINCT_ELEMENTS;
        Entry<K, ModifiableCollection<V>>[] firstPass = createModifiableCollectionEntryArray(originalLength);
        int j = -1;
        for (int i = 0; i < originalLength; i++) {
            if (i == 0 || !Objects.equals(entries[i].key(), firstPass[j].key())) {
                j++;
                firstPass[j] = new Entry<K, ModifiableCollection<V>>(entries[i].key(),
                        new ModifiableArrayCollection<V>(cardinality));
            }
            firstPass[j].value().add(entries[i].value());
        }
        int resultLength = j + 1;
        Entry<K, ModifiableCollection<V>>[] result = createModifiableCollectionEntryArray(resultLength);
        for (int i = 0; i < resultLength; i++) {
            result[i] = new Entry<K, ModifiableCollection<V>>(firstPass[i].key(),
                    new ModifiableArrayCollection<V>(firstPass[i].value()));
        }
        return result;
    }

    @Override
    public boolean contains(Entry<K, V> element) {
        Node<K, ModifiableCollection<V>> node = sortedTree.getNode(element.key());
        return node != null && node.getContent().contains(element.value());
    }

    @Override
    public boolean containsAll(Collection<?> collection) {
        if (collection.size() > size()) {
            return false;
        }
        return ArrayUtilities.containsAll(toArray(), size(), collection);
    }

    @Override
    public boolean containsKey(K key) {
        return keys.contains(key);
    }

    @Override
    public boolean containsValue(V value) {
        return values.contains(value);
    }

    @Override
    public Entry<K, V> get() throws IndexOutOfBoundsException {
        if (size == 0) {
            throw new IndexOutOfBoundsException("Cannot return an entry from an empty map.");
        } else {
            Node<K, ModifiableCollection<V>> node = sortedTree.getRootNode();
            return new Entry<K, V>(node.getKey(), node.getContent().get());
        }
    }

    @Override
    public V get(K key) throws IllegalArgumentException {
        Node<K, ModifiableCollection<V>> node = sortedTree.getNode(key);
        if (node == null) {
            throw new IllegalArgumentException("Map doesn't contain an entry with the key " + key + ".");
        }
        return node.getContent().get();
    }

    @Override
    public Collection<V> getAll(K key) throws IllegalArgumentException {
        Node<K, ModifiableCollection<V>> node = sortedTree.getNode(key);
        if (node == null) {
            throw new IllegalArgumentException("Map doesn't contain entries with the key " + key + ".");
        }
        return new ArrayCollection<V>(node.getContent());
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
        return new ArrayIterator<Entry<K, V>>(toArray());
    }

    @Override
    public V remove(K key) throws IllegalArgumentException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean removeAll(Map<? extends K, ? extends V> map) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean removeIf(Predicate<Entry<? extends K, ? extends V>> predicate) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean retainAll(Map<? extends K, ? extends V> map) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public Spliterator<Entry<K, V>> spliterator() {
        int characteristics = Spliterator.ORDERED | Spliterator.SORTED
                | (keyAndValueCardinality == DISTINCT_KEYS ? Spliterator.DISTINCT : 0);
        return new ArraySpliterator<Entry<K, V>>(toArray(), characteristics, entryByKeyComparator);
    }

    @Override
    public Entry<K, V>[] toArray() {
        if (cachedArrayDirty) {
            Class<Entry<K, V>> elementType = (Class<Entry<K, V>>) cachedArray.getClass().getComponentType();
            cachedArray = (Entry<K, V>[]) Array.newInstance(elementType, size);
            Node<K, ModifiableCollection<V>>[] compactedArray = sortedTree.toArray();
            int i = 0;
            for (Node<K, ModifiableCollection<V>> node : compactedArray) {
                K key = node.getKey();
                for (V value : node.getContent()) {
                    cachedArray[i] = new Entry<K, V>(key, value);
                    i++;
                }
            }
            cachedArrayDirty = false;
        }
        return cachedArray.clone();
    }

    @Override
    public V update(K key, V value) throws IllegalArgumentException {
        Node<K, ModifiableCollection<V>> node = sortedTree.getNode(key);
        if (node == null) {
            throw new IllegalArgumentException("Map doesn't contain an entry with the key " + key + ".");
        }
        ModifiableCollection<V> content = node.getContent();
        V oldValue = content.get();
        content.remove(oldValue);
        content.add(value);
        values.remove(oldValue);
        values.add(value);
        return oldValue;
    }
}
