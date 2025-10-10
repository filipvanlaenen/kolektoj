package net.filipvanlaenen.kolektoj.sortedtree;

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
import net.filipvanlaenen.kolektoj.array.ArrayCollection;
import net.filipvanlaenen.kolektoj.array.ArrayIterator;
import net.filipvanlaenen.kolektoj.array.ArraySpliterator;
import net.filipvanlaenen.kolektoj.array.ArrayUtilities;
import net.filipvanlaenen.kolektoj.array.ModifiableArrayCollection;
import net.filipvanlaenen.kolektoj.sortedtree.SortedTree.TreeNodesBelowAtAndAbove;

/**
 * An implementation of the {@link net.filipvanlaenen.kolektoj.UpdatableSortedMap} interface backed by a sorted tree, in
 * particular an AVL tree.
 *
 * @param <K> The key type.
 * @param <V> The value type.
 */
public final class UpdatableSortedTreeMap<K, V> implements UpdatableSortedMap<K, V> {
    /**
     * A sorted array with the entries.
     */
    private Object[] cachedArray;
    /**
     * A boolean flag indicating whether the cachedArray field is dirty.
     */
    private boolean cachedArrayDirty;
    /**
     * The comparator for the keys.
     */
    private final Comparator<? super K> comparator;
    /**
     * The comparator to use for comparing entries using the keys only in this sorted map.
     */
    private final Comparator<Entry<K, V>> entryByKeyComparator;
    /**
     * The key and value cardinality.
     */
    private final KeyAndValueCardinality keyAndValueCardinality;
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
    private final SortedTree<K, ModifiableCollection<V>> sortedTree;
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
    public UpdatableSortedTreeMap(final Comparator<? super K> comparator, final Entry<K, V>... entries)
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
    public UpdatableSortedTreeMap(final KeyAndValueCardinality keyAndValueCardinality,
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
    private UpdatableSortedTreeMap(final KeyAndValueCardinality keyAndValueCardinality,
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
            cachedArray = ArrayUtilities.quicksort(ArrayUtilities.cloneDistinctElements(entries), entryByKeyComparator);
            cachedArrayDirty = cachedArray.length != entries.length;
        } else {
            cachedArray = ArrayUtilities.quicksort(entries, entryByKeyComparator);
            cachedArrayDirty = false;
        }
        size = this.cachedArray.length;
        sortedTree = SortedTree.fromSortedEntryArray(comparator, keyAndValueCardinality, this.cachedArray, true);
        ModifiableCollection<K> theKeys = new ModifiableSortedTreeCollection<K>(
                keyAndValueCardinality == DISTINCT_KEYS ? DISTINCT_ELEMENTS : DUPLICATE_ELEMENTS, comparator);
        ModifiableCollection<V> theValues = new ModifiableArrayCollection<V>();
        for (Object object : entries) {
            Entry<K, V> entry = (Entry<K, V>) object;
            theKeys.add(entry.key());
            theValues.add(entry.value());
        }
        this.keys = new SortedTreeCollection<K>(comparator, theKeys);
        this.values = new ModifiableArrayCollection<V>(theValues);
    }

    /**
     * Constructs a map from another map, with the same entries and the same key and value cardinality.
     *
     * @param comparator The comparator by which to sort the keys.
     * @param map        The map to create a new map from.
     */
    public UpdatableSortedTreeMap(final Comparator<? super K> comparator, final Map<? extends K, ? extends V> map) {
        this(map.getKeyAndValueCardinality(), comparator, map.toArray());
    }

    @Override
    public boolean contains(final Entry<K, V> element) {
        TreeNode<K, ModifiableCollection<V>> node = sortedTree.getNode(element.key());
        return node != null && node.getContent().contains(element.value());
    }

    @Override
    public boolean containsAll(final Collection<?> collection) {
        // TODO: Use SortedTree
        return ArrayUtilities.containsAll(toArray(), size, collection);
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
            TreeNode<K, ModifiableCollection<V>> node = sortedTree.getRootNode();
            return getAnEntryFromNode(node);
        }
    }

    @Override
    public V get(final K key) throws IllegalArgumentException {
        TreeNode<K, ModifiableCollection<V>> node = sortedTree.getNode(key);
        if (node == null) {
            throw new IllegalArgumentException("Map doesn't contain an entry with the key " + key + ".");
        }
        return node.getContent().get();
    }

    @Override
    public Collection<V> getAll(final K key) throws IllegalArgumentException {
        TreeNode<K, ModifiableCollection<V>> node = sortedTree.getNode(key);
        if (node == null) {
            throw new IllegalArgumentException("Map doesn't contain entries with the key " + key + ".");
        }
        return new ArrayCollection<V>(node.getContent());
    }

    /**
     * Creates an entry with the key and a value from a node.
     *
     * @param node The node.
     * @return An entry with the node's key and one of its values.
     */
    private Entry<K, V> getAnEntryFromNode(final TreeNode<K, ModifiableCollection<V>> node) {
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
        TreeNode<K, ModifiableCollection<V>> above = sortedTree.getNodesBelowAtAndAbove(key).above();
        if (above == null) {
            throw new IndexOutOfBoundsException(
                    "Cannot return an entry from the map with a key that's greater than the provided value.");
        } else {
            return getAnEntryFromNode(above);
        }
    }

    @Override
    public Entry<K, V> getGreaterThanOrEqualTo(final K key) throws IndexOutOfBoundsException {
        if (size == 0) {
            throw new IndexOutOfBoundsException("Cannot return an entry from an empty map.");
        }
        TreeNodesBelowAtAndAbove<K, ModifiableCollection<V>> belowAtAndAbove = sortedTree.getNodesBelowAtAndAbove(key);
        TreeNode<K, ModifiableCollection<V>> at = belowAtAndAbove.at();
        if (at == null) {
            TreeNode<K, ModifiableCollection<V>> above = belowAtAndAbove.above();
            if (above == null) {
                throw new IndexOutOfBoundsException("Cannot return an entry from the map with a key that's greater than"
                        + " or equal to the provided value.");
            } else {
                return getAnEntryFromNode(above);
            }
        } else {
            return getAnEntryFromNode(at);
        }
    }

    @Override
    public Entry<K, V> getGreatest() throws IndexOutOfBoundsException {
        if (size == 0) {
            throw new IndexOutOfBoundsException("Cannot return an entry from an empty map.");
        } else {
            TreeNode<K, ModifiableCollection<V>> node = sortedTree.getGreatest();
            return getAnEntryFromNode(node);
        }
    }

    @Override
    public K getGreatestKey() throws IndexOutOfBoundsException {
        if (size == 0) {
            throw new IndexOutOfBoundsException("Cannot return a key from an empty map.");
        } else {
            return sortedTree.getGreatest().getKey();
        }
    }

    @Override
    public KeyAndValueCardinality getKeyAndValueCardinality() {
        return keyAndValueCardinality;
    }

    @Override
    public SortedCollection<K> getKeys() {
        return keys;
    }

    @Override
    public Entry<K, V> getLeast() throws IndexOutOfBoundsException {
        if (size == 0) {
            throw new IndexOutOfBoundsException("Cannot return an entry from an empty map.");
        } else {
            TreeNode<K, ModifiableCollection<V>> node = sortedTree.getLeast();
            return getAnEntryFromNode(node);
        }
    }

    @Override
    public K getLeastKey() throws IndexOutOfBoundsException {
        if (size == 0) {
            throw new IndexOutOfBoundsException("Cannot return a key from an empty map.");
        } else {
            return sortedTree.getLeast().getKey();
        }
    }

    @Override
    public Entry<K, V> getLessThan(final K key) throws IndexOutOfBoundsException {
        if (size == 0) {
            throw new IndexOutOfBoundsException("Cannot return an entry from an empty map.");
        }
        TreeNode<K, ModifiableCollection<V>> below = sortedTree.getNodesBelowAtAndAbove(key).below();
        if (below == null) {
            throw new IndexOutOfBoundsException(
                    "Cannot return an entry from the map with a key that's less than the provided value.");
        } else {
            return getAnEntryFromNode(below);
        }
    }

    @Override
    public Entry<K, V> getLessThanOrEqualTo(final K key) throws IndexOutOfBoundsException {
        if (size == 0) {
            throw new IndexOutOfBoundsException("Cannot return an entry from an empty map.");
        }
        TreeNodesBelowAtAndAbove<K, ModifiableCollection<V>> belowAtAndAbove = sortedTree.getNodesBelowAtAndAbove(key);
        TreeNode<K, ModifiableCollection<V>> at = belowAtAndAbove.at();
        if (at == null) {
            TreeNode<K, ModifiableCollection<V>> below = belowAtAndAbove.below();
            if (below == null) {
                throw new IndexOutOfBoundsException("Cannot return an entry from the map with a key that's less than"
                        + " or equal to the provided value.");
            } else {
                return getAnEntryFromNode(below);
            }
        } else {
            return getAnEntryFromNode(at);
        }
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
    public int size() {
        return size;
    }

    @Override
    public Spliterator<Entry<K, V>> spliterator() {
        int characteristics = Spliterator.ORDERED | Spliterator.SORTED
                | (keyAndValueCardinality == DUPLICATE_KEYS_WITH_DUPLICATE_VALUES ? 0 : Spliterator.DISTINCT);
        return new ArraySpliterator<Entry<K, V>>(toArray(), characteristics, entryByKeyComparator);
    }

    @Override
    public Object[] toArray() {
        if (cachedArrayDirty) {
            cachedArray = SortedTree.uncompact(sortedTree.toArray(), cachedArray);
            cachedArrayDirty = false;
        }
        return cachedArray.clone();
    }

    @Override
    public V update(final K key, final V value) throws IllegalArgumentException {
        TreeNode<K, ModifiableCollection<V>> node = sortedTree.getNode(key);
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

    @Override
    public boolean update(final K key, final V oldValue, final V newValue) throws IllegalArgumentException {
        TreeNode<K, ModifiableCollection<V>> node = sortedTree.getNode(key);
        if (node == null || !node.getContent().contains(oldValue)) {
            throw new IllegalArgumentException(
                    "Map doesn't contain an entry with the key " + key + " and value " + oldValue + ".");
        }
        if (Objects.equals(oldValue, newValue)) {
            return false;
        }
        ModifiableCollection<V> content = node.getContent();
        if (keyAndValueCardinality == DUPLICATE_KEYS_WITH_DISTINCT_VALUES && content.contains(newValue)) {
            return false;
        }
        content.remove(oldValue);
        content.add(newValue);
        values.remove(oldValue);
        values.add(newValue);
        return true;
    }
}
