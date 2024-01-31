package net.filipvanlaenen.kolektoj.sortedtree;

import static net.filipvanlaenen.kolektoj.Collection.ElementCardinality.DISTINCT_ELEMENTS;
import static net.filipvanlaenen.kolektoj.Collection.ElementCardinality.DUPLICATE_ELEMENTS;

import java.util.Comparator;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.function.Predicate;

import net.filipvanlaenen.kolektoj.Collection;
import net.filipvanlaenen.kolektoj.ModifiableSortedCollection;
import net.filipvanlaenen.kolektoj.array.ArrayIterator;
import net.filipvanlaenen.kolektoj.array.ArraySpliterator;

/**
 * An implementation of the {@link net.filipvanlaenen.kolektoj.ModifiableSortedCollection} interface backed by a sorted
 * tree, in particular an AVL tree.
 *
 * @param <E> The element type.
 */
public final class ModifiableSortedTreeCollection<E extends Comparable<E>> implements ModifiableSortedCollection<E> {
    /**
     * A cached array with the elements.
     */
    private E[] cachedArray;
    /**
     * A boolean flag indicating whether the cachedArray field is dirty.
     */
    private boolean cachedArrayDirty;
    /**
     * The comparator to use for comparing the elements in this collection.
     */
    private final Comparator<E> comparator;
    /**
     * The element cardinality.
     */
    private final ElementCardinality elementCardinality;
    private final SortedTree<E> sortedTree;

    public ModifiableSortedTreeCollection(final Comparator<E> comparator, final E... elements) {
        this(DUPLICATE_ELEMENTS, comparator, elements);
    }

    public ModifiableSortedTreeCollection(final ElementCardinality elementCardinality, final Comparator<E> comparator,
            final E... elements) {
        this.comparator = comparator;
        this.elementCardinality = elementCardinality;
        sortedTree =
                new SortedTree<E>(comparator, elementCardinality, (Class<E>) elements.getClass().getComponentType());
        for (E element : elements) {
            add(element);
        }
        cachedArray = elements.clone();
        cachedArrayDirty = elements.length != size();
    }

    @Override
    public boolean add(final E element) {
        boolean changed = sortedTree.add(element);
        cachedArrayDirty = cachedArrayDirty || changed;
        return changed;
    }

    @Override
    public boolean addAll(final Collection<? extends E> collection) {
        boolean result = false;
        for (E element : collection) {
            result |= add(element);
        }
        return result;
    }

    @Override
    public void clear() {
        sortedTree.clear();
        cachedArrayDirty = cachedArray.length != 0;
    }

    @Override
    public boolean contains(final E element) {
        return sortedTree.contains(element);
    }

    @Override
    public boolean containsAll(final Collection<?> collection) {
        return sortedTree.containsAll(collection);
    }

    @Override
    public E get() throws IndexOutOfBoundsException {
        if (sortedTree.getSize() == 0) {
            throw new IndexOutOfBoundsException("Cannot return an element from an empty collection.");
        } else {
            return sortedTree.getRootElement();
        }
    }

    @Override
    public E getAt(final int index) throws IndexOutOfBoundsException {
        return sortedTree.getAt(index);
    }

    @Override
    public ElementCardinality getElementCardinality() {
        return elementCardinality;
    }

    @Override
    public Iterator<E> iterator() {
        return new ArrayIterator<E>(toArray());
    }

    @Override
    public boolean remove(final E element) {
        boolean changed = sortedTree.remove(element);
        cachedArrayDirty = cachedArrayDirty || changed;
        return changed;
    }

    @Override
    public boolean removeAll(final Collection<? extends E> collection) {
        boolean result = false;
        for (E element : collection) {
            result |= remove(element);
        }
        return result;
    }

    @Override
    public E removeAt(final int index) throws IndexOutOfBoundsException {
        if (index >= sortedTree.getSize()) {
            throw new IndexOutOfBoundsException(
                    "Cannot remove an element at a position beyond the size of the collection.");
        } else {
            E result = getAt(index);
            remove(result);
            return result;
        }
    }

    @Override
    public boolean removeIf(final Predicate<? super E> predicate) {
        return sortedTree.removeIf(predicate);
    }

    @Override
    public boolean retainAll(final Collection<? extends E> collection) {
        boolean changed = sortedTree.retainAll(collection);
        cachedArrayDirty = cachedArrayDirty || changed;
        return changed;
    }

    @Override
    public int size() {
        return sortedTree.getSize();
    }

    @Override
    public Spliterator<E> spliterator() {
        return new ArraySpliterator<E>(toArray(), Spliterator.ORDERED | Spliterator.SORTED
                | (elementCardinality == DISTINCT_ELEMENTS ? Spliterator.DISTINCT : 0), comparator);
    }

    @Override
    public E[] toArray() {
        if (cachedArrayDirty) {
            cachedArray = sortedTree.toArray();
            cachedArrayDirty = false;
        }
        return cachedArray.clone();
    }
}
