package net.filipvanlaenen.kolektoj.sortedtree;

import static net.filipvanlaenen.kolektoj.Collection.ElementCardinality.DISTINCT_ELEMENTS;
import static net.filipvanlaenen.kolektoj.Collection.ElementCardinality.DUPLICATE_ELEMENTS;
import static net.filipvanlaenen.kolektoj.Map.KeyAndValueCardinality.DISTINCT_KEYS;
import static net.filipvanlaenen.kolektoj.Map.KeyAndValueCardinality.DUPLICATE_KEYS_WITH_DUPLICATE_VALUES;

import java.util.Comparator;
import java.util.Iterator;
import java.util.Spliterator;

import net.filipvanlaenen.kolektoj.Collection;
import net.filipvanlaenen.kolektoj.Map;
import net.filipvanlaenen.kolektoj.ModifiableCollection;
import net.filipvanlaenen.kolektoj.ModifiableOrderedCollection;
import net.filipvanlaenen.kolektoj.OrderedCollection;
import net.filipvanlaenen.kolektoj.SortedCollection;
import net.filipvanlaenen.kolektoj.SortedMap;
import net.filipvanlaenen.kolektoj.array.ArrayIterator;
import net.filipvanlaenen.kolektoj.array.ArraySpliterator;
import net.filipvanlaenen.kolektoj.array.ArrayUtilities;
import net.filipvanlaenen.kolektoj.array.ModifiableOrderedArrayCollection;
import net.filipvanlaenen.kolektoj.array.OrderedArrayCollection;
import net.filipvanlaenen.kolektoj.array.SortedArrayCollection;
import net.filipvanlaenen.kolektoj.sortedtree.SortedTree.TreeNodesBelowAtAndAbove;

/**
 * An implementation of the {@link net.filipvanlaenen.kolektoj.SortedMap} interface backed by a sorted tree, in
 * particular an AVL tree.
 *
 * @param <K> The key type.
 * @param <V> The value type.
 */
public final class SortedTreeMap<K, V> implements SortedMap<K, V> {
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
     * The comparator to use for comparing entries using the keys only in this sorted map.
     */
    private final Comparator<Entry<K, V>> entryByKeyComparator;
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
     * An ordered collection with the values.
     */
    private final OrderedCollection<V> values;

    /**
     * Constructor taking the entries as its parameter.
     *
     * @param comparator The comparator by which to sort the keys.
     * @param entries    The entries for the map.
     * @throws IllegalArgumentException Thrown if one of the entries is null.
     */
    public SortedTreeMap(final Comparator<? super K> comparator, final Entry<K, V>... entries)
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
    public SortedTreeMap(final KeyAndValueCardinality keyAndValueCardinality, final Comparator<? super K> comparator,
            final Entry<K, V>... entries) throws IllegalArgumentException {
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
    private SortedTreeMap(final KeyAndValueCardinality keyAndValueCardinality, final Comparator<? super K> comparator,
            final Object[] entries) throws IllegalArgumentException {
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
        size = this.entries.length;
        sortedTree = SortedTree.fromSortedEntryArray(comparator, keyAndValueCardinality, this.entries, false);
        ModifiableCollection<K> theKeys = new ModifiableSortedTreeCollection<K>(
                keyAndValueCardinality == DISTINCT_KEYS ? DISTINCT_ELEMENTS : DUPLICATE_ELEMENTS, comparator);
        ModifiableOrderedCollection<V> theValues = new ModifiableOrderedArrayCollection<V>();
        for (Object object : this.entries) {
            Entry<K, V> entry = (Entry<K, V>) object;
            theKeys.add(entry.key());
            theValues.add(entry.value());
        }
        this.keys = new SortedArrayCollection<K>(comparator, theKeys);
        this.values = new OrderedArrayCollection<V>(theValues);
    }

    /**
     * Constructs a map from another map, with the same entries and the same key and value cardinality.
     *
     * @param comparator The comparator by which to sort the keys.
     * @param map        The map to create a new map from.
     */
    public SortedTreeMap(final Comparator<? super K> comparator, final Map<? extends K, ? extends V> map) {
        this(map.getKeyAndValueCardinality(), comparator, map.toArray());
    }

    @Override
    public boolean contains(final Entry<K, V> element) {
        TreeNode<K, Collection<V>> node = sortedTree.getNode(element.key());
        if (node == null) {
            return false;
        } else {
            return node.getContent().contains(element.value());
        }
    }

    @Override
    public boolean containsAll(final Collection<?> collection) {
        return ArrayUtilities.containsAll(entries, size, collection);
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
        if (size == 0) {
            throw new IndexOutOfBoundsException("Cannot return an entry from an empty map.");
        } else {
            TreeNode<K, Collection<V>> root = sortedTree.getRootNode();
            return getAnEntryFromNode(root);
        }
    }

    @Override
    public V get(final K key) throws IllegalArgumentException {
        TreeNode<K, Collection<V>> node = sortedTree.getNode(key);
        if (node == null) {
            throw new IllegalArgumentException("Map doesn't contain an entry with the key " + key + ".");
        }
        return node.getContent().get();
    }

    @Override
    public Collection<V> getAll(final K key) throws IllegalArgumentException {
        TreeNode<K, Collection<V>> node = sortedTree.getNode(key);
        if (node == null) {
            throw new IllegalArgumentException("Map doesn't contain entries with the key " + key + ".");
        }
        return node.getContent();
    }

    /**
     * Creates an entry with the key and a value from a node.
     *
     * @param node The node.
     * @return An entry with the node's key and one of its values.
     */
    private Entry<K, V> getAnEntryFromNode(final TreeNode<K, Collection<V>> node) {
        return new Entry<K, V>(node.getKey(), node.getContent().get());
    }

    @Override
    public Comparator<? super K> getComparator() {
        return comparator;
    }

    @Override
    public Entry<K, V> getGreaterThan(final K key) throws IndexOutOfBoundsException {
        if (size == 0) {
            throw new IndexOutOfBoundsException("Cannot return an entry from an empty map.");
        }
        TreeNode<K, Collection<V>> above = sortedTree.getNodesBelowAtAndAbove(key).above();
        if (above == null) {
            throw new IndexOutOfBoundsException(
                    "Cannot return an entry from the map with a key that's greater than the provided value.");
        }
        return getAnEntryFromNode(above);
    }

    @Override
    public Entry<K, V> getGreaterThanOrEqualTo(final K key) throws IndexOutOfBoundsException {
        if (size == 0) {
            throw new IndexOutOfBoundsException("Cannot return an entry from an empty map.");
        }
        TreeNodesBelowAtAndAbove<K, Collection<V>> belowAtAndAbove = sortedTree.getNodesBelowAtAndAbove(key);
        TreeNode<K, Collection<V>> at = belowAtAndAbove.at();
        if (at == null) {
            TreeNode<K, Collection<V>> above = belowAtAndAbove.above();
            if (above == null) {
                throw new IndexOutOfBoundsException("Cannot return an entry from the map with a key that's greater than"
                        + " or equal to the provided value.");
            }
            return getAnEntryFromNode(above);
        } else {
            return getAnEntryFromNode(at);
        }
    }

    @Override
    public Entry<K, V> getGreatest() throws IndexOutOfBoundsException {
        if (entries.length == 0) {
            throw new IndexOutOfBoundsException("Cannot return an entry from an empty map.");
        }
        TreeNode<K, Collection<V>> node = sortedTree.getGreatest();
        return getAnEntryFromNode(node);
    }

    @Override
    public K getGreatestKey() throws IndexOutOfBoundsException {
        if (entries.length == 0) {
            throw new IndexOutOfBoundsException("Cannot return a key from an empty map.");
        }
        return sortedTree.getGreatest().getKey();
    }

    @Override
    public KeyAndValueCardinality getKeyAndValueCardinality() {
        return keyAndValueCardinality;
    }

    @Override
    public K getKeyGreaterThan(final K key) throws IndexOutOfBoundsException {
        if (size == 0) {
            throw new IndexOutOfBoundsException("Cannot return a key from an empty map.");
        }
        TreeNode<K, Collection<V>> above = sortedTree.getNodesBelowAtAndAbove(key).above();
        if (above == null) {
            throw new IndexOutOfBoundsException(
                    "Cannot return a key from the map that's greater than the provided value.");
        }
        return above.getKey();
    }

    @Override
    public K getKeyGreaterThanOrEqualTo(final K key) throws IndexOutOfBoundsException {
        if (size == 0) {
            throw new IndexOutOfBoundsException("Cannot return a key from an empty map.");
        }
        TreeNodesBelowAtAndAbove<K, Collection<V>> belowAtAndAbove = sortedTree.getNodesBelowAtAndAbove(key);
        TreeNode<K, Collection<V>> at = belowAtAndAbove.at();
        if (at == null) {
            TreeNode<K, Collection<V>> above = belowAtAndAbove.above();
            if (above == null) {
                throw new IndexOutOfBoundsException(
                        "Cannot return a key from the map that's greater than or equal to the provided value.");
            }
            return above.getKey();
        } else {
            return at.getKey();
        }
    }

    @Override
    public K getKeyLessThan(final K key) throws IndexOutOfBoundsException {
        if (size == 0) {
            throw new IndexOutOfBoundsException("Cannot return a key from an empty map.");
        }
        TreeNode<K, Collection<V>> below = sortedTree.getNodesBelowAtAndAbove(key).below();
        if (below == null) {
            throw new IndexOutOfBoundsException(
                    "Cannot return a key from the map that's less than the provided value.");
        }
        return below.getKey();
    }

    @Override
    public K getKeyLessThanOrEqualTo(final K key) throws IndexOutOfBoundsException {
        if (size == 0) {
            throw new IndexOutOfBoundsException("Cannot return a key from an empty map.");
        }
        TreeNodesBelowAtAndAbove<K, Collection<V>> belowAtAndAbove = sortedTree.getNodesBelowAtAndAbove(key);
        TreeNode<K, Collection<V>> at = belowAtAndAbove.at();
        if (at == null) {
            TreeNode<K, Collection<V>> below = belowAtAndAbove.below();
            if (below == null) {
                throw new IndexOutOfBoundsException(
                        "Cannot return a key from the map that's less than or equal to the provided value.");
            }
            return below.getKey();
        } else {
            return at.getKey();
        }
    }

    @Override
    public SortedCollection<K> getKeys() {
        return keys;
    }

    @Override
    public Entry<K, V> getLeast() throws IndexOutOfBoundsException {
        if (entries.length == 0) {
            throw new IndexOutOfBoundsException("Cannot return an entry from an empty map.");
        }
        TreeNode<K, Collection<V>> node = sortedTree.getLeast();
        return getAnEntryFromNode(node);
    }

    @Override
    public K getLeastKey() throws IndexOutOfBoundsException {
        if (entries.length == 0) {
            throw new IndexOutOfBoundsException("Cannot return a key from an empty map.");
        }
        return sortedTree.getLeast().getKey();
    }

    @Override
    public Entry<K, V> getLessThan(final K key) throws IndexOutOfBoundsException {
        if (size == 0) {
            throw new IndexOutOfBoundsException("Cannot return an entry from an empty map.");
        }
        TreeNode<K, Collection<V>> below = sortedTree.getNodesBelowAtAndAbove(key).below();
        if (below == null) {
            throw new IndexOutOfBoundsException(
                    "Cannot return an entry from the map with a key that's less than the provided value.");
        }
        return getAnEntryFromNode(below);
    }

    @Override
    public Entry<K, V> getLessThanOrEqualTo(final K key) throws IndexOutOfBoundsException {
        if (size == 0) {
            throw new IndexOutOfBoundsException("Cannot return an entry from an empty map.");
        }
        TreeNodesBelowAtAndAbove<K, Collection<V>> belowAtAndAbove = sortedTree.getNodesBelowAtAndAbove(key);
        TreeNode<K, Collection<V>> at = belowAtAndAbove.at();
        if (at == null) {
            TreeNode<K, Collection<V>> below = belowAtAndAbove.below();
            if (below == null) {
                throw new IndexOutOfBoundsException("Cannot return an entry from the map with a key that's less than"
                        + " or equal to the provided value.");
            }
            return getAnEntryFromNode(below);
        } else {
            return getAnEntryFromNode(at);
        }
    }

    @Override
    public OrderedCollection<V> getValues() {
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
                | (keyAndValueCardinality == DUPLICATE_KEYS_WITH_DUPLICATE_VALUES ? 0 : Spliterator.DISTINCT);
        return new ArraySpliterator<Entry<K, V>>(entries, characteristics, entryByKeyComparator);
    }

    @Override
    public Object[] toArray() {
        return entries.clone();
    }
}
