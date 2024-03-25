package net.filipvanlaenen.kolektoj.sortedtree;

import static net.filipvanlaenen.kolektoj.Collection.ElementCardinality.DISTINCT_ELEMENTS;
import static net.filipvanlaenen.kolektoj.Collection.ElementCardinality.DUPLICATE_ELEMENTS;
import static net.filipvanlaenen.kolektoj.Map.KeyAndValueCardinality.DISTINCT_KEYS;
import static net.filipvanlaenen.kolektoj.Map.KeyAndValueCardinality.DUPLICATE_KEYS_WITH_DUPLICATE_VALUES;

import java.util.Comparator;
import java.util.Iterator;
import java.util.Objects;
import java.util.Spliterator;

import net.filipvanlaenen.kolektoj.Collection;
import net.filipvanlaenen.kolektoj.ModifiableCollection;
import net.filipvanlaenen.kolektoj.SortedCollection;
import net.filipvanlaenen.kolektoj.SortedMap;
import net.filipvanlaenen.kolektoj.array.ArrayCollection;
import net.filipvanlaenen.kolektoj.array.ArrayIterator;
import net.filipvanlaenen.kolektoj.array.ArraySpliterator;
import net.filipvanlaenen.kolektoj.array.ArrayUtilities;
import net.filipvanlaenen.kolektoj.array.ModifiableArrayCollection;
import net.filipvanlaenen.kolektoj.array.SortedArrayCollection;

/**
 * An implementation of the {@link net.filipvanlaenen.kolektoj.SortedMap} interface backed by a sorted tree, in
 * particular an AVL tree.
 *
 * @param <K> The key type.
 * @param <V> The value type.
 */
public final class SortedTreeMap<K, V> implements SortedMap<K, V> {
    /**
     * The comparator to use for comparing the keys in this sorted map.
     */
    private final Comparator<K> comparator;
    /**
     * A sorted array with the entries.
     */
    private final Entry<K, V>[] entries;
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
    private final SortedCollection<K> keys;
    /**
     * The size of the collection.
     */
    private final int size;
    /**
     * The sorted tree with the entries.
     */
    private final SortedTree<K, Collection<V>> sortedTree;
    /**
     * A collection with the values.
     */
    private final Collection<V> values;

    /**
     * Constructor taking the entries as its parameter.
     *
     * @param entries The entries for the map.
     * @throws IllegalArgumentException Thrown if one of the entries is null.
     */
    public SortedTreeMap(final Comparator<K> comparator, final Entry<K, V>... entries) throws IllegalArgumentException {
        this(DISTINCT_KEYS, comparator, entries);
    }

    /**
     * Constructor taking the key and value cardinality and the entries as its parameter.
     *
     * @param keyAndValueCardinality The key and value cardinality.
     * @param entries                The entries for the map.
     * @throws IllegalArgumentException Thrown if one of the entries is null.
     */
    public SortedTreeMap(final KeyAndValueCardinality keyAndValueCardinality, final Comparator<K> comparator,
            final Entry<K, V>... entries) throws IllegalArgumentException {
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
            this.entries =
                    ArrayUtilities.quicksort(ArrayUtilities.cloneDistinctElements(entries), entryByKeyComparator);
        } else {
            this.entries = ArrayUtilities.quicksort(entries, entryByKeyComparator);
        }
        size = this.entries.length;

        sortedTree = SortedTree.fromSortedArray(comparator, keyAndValueCardinality, compact(this.entries));

        // TODO: Can the keys be extracted using fromSortedArray?
        ModifiableCollection<K> theKeys = new ModifiableSortedTreeCollection<K>(
                keyAndValueCardinality == DISTINCT_KEYS ? DISTINCT_ELEMENTS : DUPLICATE_ELEMENTS, comparator);

        ModifiableCollection<V> theValues = new ModifiableArrayCollection<V>();
        for (Entry<K, V> entry : entries) {
            theKeys.add(entry.key());
            theValues.add(entry.value());
        }
        this.keys = new SortedArrayCollection<K>(comparator, theKeys);
        this.values = new ArrayCollection<V>(theValues);
    }

    private Entry<K, Collection<V>>[] compact(Entry<K, V>[] entries) {
        int originalLength = entries.length;
        ElementCardinality cardinality =
                keyAndValueCardinality == DUPLICATE_KEYS_WITH_DUPLICATE_VALUES ? DUPLICATE_ELEMENTS : DISTINCT_ELEMENTS;
        Entry<K, ModifiableCollection<V>>[] firstPass =
                (Entry<K, ModifiableCollection<V>>[]) new Object[originalLength];
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
        Entry<K, Collection<V>>[] result = (Entry<K, Collection<V>>[]) new Object[resultLength];
        for (int i = 0; i < resultLength; i++) {
            result[i] = new Entry<K, Collection<V>>(firstPass[i].key(), new ArrayCollection<V>(firstPass[i].value()));
        }
        return result;
    }

    @Override
    public boolean contains(Entry<K, V> element) {
        Node<K, Collection<V>> node = sortedTree.getNode(element.key());
        if (node == null) {
            return false;
        } else {
            return node.getContent().contains(element.value());
        }
    }

    @Override
    public boolean containsAll(Collection<?> collection) {
        return false; // TODO
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
            Node<K, Collection<V>> root = sortedTree.getRootNode();
            return new Entry<K, V>(root.getKey(), root.getContent().get());
        }
    }

    @Override
    public V get(K key) throws IllegalArgumentException {
        Node<K, Collection<V>> node = sortedTree.getNode(key);
        if (node == null) {
            throw new IllegalArgumentException("Map doesn't contain an entry with the key " + key + ".");
        }
        return node.getContent().get();
    }

    @Override
    public Collection<V> getAll(K key) throws IllegalArgumentException {
        Node<K, Collection<V>> node = sortedTree.getNode(key);
        if (node == null) {
            throw new IllegalArgumentException("Map doesn't contain an entry with the key " + key + ".");
        }
        return node.getContent();
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
        return values;
    }

    @Override
    public Iterator<Entry<K, V>> iterator() {
        return new ArrayIterator<Entry<K, V>>(entries);
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public Spliterator<Entry<K, V>> spliterator() {
        int characteristics = Spliterator.ORDERED | Spliterator.SORTED
                | (keyAndValueCardinality == DISTINCT_KEYS ? Spliterator.DISTINCT : 0);
        return new ArraySpliterator<Entry<K, V>>(entries, characteristics, entryByKeyComparator);
    }

    @Override
    public Entry<K, V>[] toArray() {
        return entries.clone();
    }
}
