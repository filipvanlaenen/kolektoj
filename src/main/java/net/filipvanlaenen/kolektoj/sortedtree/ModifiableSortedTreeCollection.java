package net.filipvanlaenen.kolektoj.sortedtree;

import static net.filipvanlaenen.kolektoj.Collection.ElementCardinality.DISTINCT_ELEMENTS;
import static net.filipvanlaenen.kolektoj.Collection.ElementCardinality.DUPLICATE_ELEMENTS;

import java.lang.reflect.Array;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.function.Predicate;

import net.filipvanlaenen.kolektoj.Collection;
import net.filipvanlaenen.kolektoj.SortedCollection;
import net.filipvanlaenen.kolektoj.array.ArrayIterator;
import net.filipvanlaenen.kolektoj.array.ArraySpliterator;

/**
 * An implementation of the {@link net.filipvanlaenen.kolektoj.SortedCollection} interface backed by a sorted tree, in
 * particular an AVL tree.
 *
 * @param <E> The element type.
 */
public final class ModifiableSortedTreeCollection<E extends Comparable<E>> implements SortedCollection<E> {
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
    /**
     * The root node of the collection.
     */
    private Node<E> root;
    /**
     * The size of the collection.
     */
    private int size;
    private final SortedTree<E> sortedTree;

    public ModifiableSortedTreeCollection(final Comparator<E> comparator, final E... elements) {
        this(DUPLICATE_ELEMENTS, comparator, elements);
    }

    public ModifiableSortedTreeCollection(final ElementCardinality elementCardinality, final Comparator<E> comparator,
            final E... elements) {
        this.comparator = comparator;
        this.elementCardinality = elementCardinality;
        for (E element : elements) {
            add(element);
        }
        sortedTree = new SortedTree<E>(comparator, elementCardinality);
        cachedArray = elements.clone();
        cachedArrayDirty = elements.length != size;
    }

    @Override
    public boolean add(final E element) {
        int originalSize = size;
        root = insertNodeAndUpdateSize(element, root);
        root.updateHeight();
        root = root.rebalance();
        boolean changed = size != originalSize;
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

    private int addNodesToCachedArray(final Node<E> node, final int index) {
        if (node == null) {
            return index;
        }
        int result = addNodesToCachedArray(node.getLeftChild(), index);
        cachedArray[result++] = node.getElement();
        return addNodesToCachedArray(node.getRightChild(), result);
    }

    @Override
    public void clear() {
        root = null;
        size = 0;
        cachedArrayDirty = cachedArray.length != 0;
    }

    private int collectUnmatchedForRemoval(final E[] removeArray, final int removeArraySize, final Node<E> node,
            final boolean[] matched, final int index) {
        if (node == null) {
            return removeArraySize;
        }
        int result = removeArraySize;
        if (!matched[index]) {
            removeArray[result++] = node.getElement();
        }
        result = collectUnmatchedForRemoval(removeArray, result, node.getLeftChild(), matched, index + 1);
        int leftSize = node.getLeftChild() == null ? 0 : node.getLeftChild().getSize();
        result = collectUnmatchedForRemoval(removeArray, result, node.getRightChild(), matched, index + leftSize + 1);
        return result;
    }

    @Override
    public boolean contains(final E element) {
        return sortedTree.contains(root, element);
    }

    @Override
    public boolean containsAll(final Collection<?> collection) {
        return sortedTree.containsAll(root, size, (Class<E>) cachedArray.getClass().getComponentType(), collection);
    }

    private Node<E> deleteNodeAndUpdateSize(final E element, final Node<E> node) {
        if (node == null) {
            return null;
        } else if (comparator.compare(element, node.getElement()) < 0) {
            node.setLeftChild(deleteNodeAndUpdateSize(element, node.getLeftChild()));
            node.updateHeight();
            return node;
        } else if (comparator.compare(element, node.getElement()) > 0) {
            node.setRightChild(deleteNodeAndUpdateSize(element, node.getRightChild()));
            node.updateHeight();
            return node;
        } else if (node.getLeftChild() == null && node.getRightChild() == null) {
            size--;
            return null;
        } else if (node.getLeftChild() == null) {
            size--;
            return node.getRightChild();
        } else if (node.getRightChild() == null) {
            size--;
            return node.getLeftChild();
        } else {
            Node<E> inOrderSuccessor = node.getRightChild().getLeftmostChild();
            node.setElement(inOrderSuccessor.getElement());
            node.setRightChild(deleteNodeAndUpdateSize(inOrderSuccessor.getElement(), node.getRightChild()));
            node.updateHeight();
            return node;
        }
    }

    private boolean findAndMarkMatch(final Node<E> node, final boolean[] matched, final int index, final E element) {
        if (node == null) {
            return false;
        }
        int comparison = comparator.compare(element, node.getElement());
        if (!matched[index] && comparison == 0) {
            matched[index] = true;
            return true;
        } else if (comparison < 0) {
            return findAndMarkMatch(node.getLeftChild(), matched, index + 1, element);
        } else if (comparison > 0) {
            int leftSize = node.getLeftChild() == null ? 0 : node.getLeftChild().getSize();
            return findAndMarkMatch(node.getRightChild(), matched, index + leftSize + 1, element);
        } else if (elementCardinality == DISTINCT_ELEMENTS) {
            return false;
        } else if (findAndMarkMatch(node.getLeftChild(), matched, index + 1, element)) {
            return true;
        } else {
            int leftSize = node.getLeftChild() == null ? 0 : node.getLeftChild().getSize();
            return findAndMarkMatch(node.getRightChild(), matched, index + leftSize + 1, element);
        }
    }

    @Override
    public E get() throws IndexOutOfBoundsException {
        if (root == null) {
            throw new IndexOutOfBoundsException("Cannot return an element from an empty collection.");
        } else {
            return root.getElement();
        }
    }

    @Override
    public E getAt(final int index) throws IndexOutOfBoundsException {
        if (index >= size) {
            throw new IndexOutOfBoundsException(
                    "Cannot return an element at a position beyond the size of the collection.");
        } else {
            return getAt(root, index);
        }
    }

    private E getAt(final Node<E> node, final int index) {
        int leftSize = node.getLeftChild().getSize();
        if (leftSize < index) {
            return getAt(node.getRightChild(), index - leftSize - 1);
        } else if (leftSize == index) {
            return node.getElement();
        } else {
            return getAt(node.getLeftChild(), index);
        }
    }

    @Override
    public ElementCardinality getElementCardinality() {
        return elementCardinality;
    }

    private Node<E> insertNodeAndUpdateSize(final E element, final Node<E> node) {
        if (node == null) {
            size++;
            return new Node<E>(element);
        } else if (comparator.compare(element, node.getElement()) < 0) {
            node.setLeftChild(insertNodeAndUpdateSize(element, node.getLeftChild()));
            node.updateHeight();
            return node;
        } else if (comparator.compare(element, node.getElement()) > 0) {
            node.setRightChild(insertNodeAndUpdateSize(element, node.getRightChild()));
            node.updateHeight();
            return node;
        } else if (elementCardinality == DISTINCT_ELEMENTS) {
            return node;
        } else {
            node.setRightChild(insertNodeAndUpdateSize(element, node.getRightChild()));
            node.updateHeight();
            return node;
        }
    }

    @Override
    public Iterator<E> iterator() {
        return new ArrayIterator<E>(toArray());
    }

    private int markForRemoval(final E[] deleteArray, final int size, final Node<E> node,
            final Predicate<? super E> predicate) {
        if (node == null) {
            return size;
        }
        int result = size;
        if (predicate.test(node.getElement())) {
            deleteArray[result++] = node.getElement();
        }
        result = markForRemoval(deleteArray, result, node.getLeftChild(), predicate);
        result = markForRemoval(deleteArray, result, node.getRightChild(), predicate);
        return result;
    }

    @Override
    public boolean remove(final E element) {
        if (root == null) {
            return false;
        }
        int originalSize = size;
        root = deleteNodeAndUpdateSize(element, root);
        if (root != null) {
            root.updateHeight();
            root = root.rebalance();
        }
        boolean changed = size != originalSize;
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
        if (index >= size) {
            throw new IndexOutOfBoundsException(
                    "Cannot remove an element at a position beyond the size of the collection.");
        } else {
            E result = getAt(root, index);
            remove(result);
            return result;
        }
    }

    @Override
    public boolean removeIf(final Predicate<? super E> predicate) {
        Class<E[]> clazz = (Class<E[]>) cachedArray.getClass();
        E[] removeArray = (E[]) Array.newInstance(clazz.getComponentType(), size);
        int removeArraySize = markForRemoval(removeArray, 0, root, predicate);
        for (int i = 0; i < removeArraySize; i++) {
            remove(removeArray[i]);
        }
        return removeArraySize > 0;
    }

    @Override
    public boolean retainAll(final Collection<? extends E> collection) {
        boolean[] matched = new boolean[size];
        Class<E> componentType = (Class<E>) cachedArray.getClass().getComponentType();
        for (Object element : collection) {
            if (componentType.isInstance(element)) {
                findAndMarkMatch(root, matched, 0, (E) element);
            }
        }
        Class<E[]> clazz = (Class<E[]>) cachedArray.getClass();
        E[] removeArray = (E[]) Array.newInstance(clazz.getComponentType(), size);
        int removeArraySize = collectUnmatchedForRemoval(removeArray, 0, root, matched, 0);
        for (int i = 0; i < removeArraySize; i++) {
            remove(removeArray[i]);
        }
        boolean changed = removeArraySize > 0;
        cachedArrayDirty = cachedArrayDirty || changed;
        return changed;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public Spliterator<E> spliterator() {
        return new ArraySpliterator<E>(toArray(), Spliterator.ORDERED | Spliterator.SORTED
                | (elementCardinality == DISTINCT_ELEMENTS ? Spliterator.DISTINCT : 0), comparator);
    }

    @Override
    public E[] toArray() {
        if (cachedArrayDirty) {
            Class<E[]> clazz = (Class<E[]>) cachedArray.getClass();
            cachedArray = (E[]) Array.newInstance(clazz.getComponentType(), size);
            addNodesToCachedArray(root, 0);
            cachedArrayDirty = false;
        }
        return cachedArray.clone();
    }
}
