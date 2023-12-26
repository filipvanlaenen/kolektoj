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
     * A class implementing a node in a sorted tree.
     */
    private final class Node {
        /**
         * The element of the node.
         */
        private E element;
        /**
         * The height of the node.
         */
        private int height;
        /**
         * The left child, with elements that compare less than the element of this node.
         */
        private Node leftChild;
        /**
         * The right child, with elements that compare greater than the element of this node.
         */
        private Node rightChild;

        /**
         * Constructor taking an element as its parameter.
         *
         * @param element The element for this node.
         */
        private Node(final E element) {
            this.element = element;
        }

        /**
         * Calculates the balance factor for this node.
         *
         * @return The balance factor for this node.
         */
        private int calculateBalanceFactor() {
            return getHeight(rightChild) - getHeight(leftChild);
        }

        /**
         * Returns the element of this node.
         *
         * @return The element of this node.
         */
        private E getElement() {
            return element;
        }

        /**
         * Returns the height of this node.
         *
         * @return The height of this node.
         */
        private int getHeight() {
            return height;
        }

        /**
         * Helper method returning -1 if the provided parameter is <code>null</code>, and the height of the node
         * otherwise.
         *
         * @param node The node.
         * @return The height of the node, or -1 if the provided parameter is <code>null</code>.
         */
        private int getHeight(final Node node) {
            return node == null ? -1 : node.getHeight();
        }

        /**
         * Returns the left child of this node.
         *
         * @return The left child of this node.
         */
        private Node getLeftChild() {
            return leftChild;
        }

        /**
         * Returns the leftmost child by descending as far as possible to the left starting from this node.
         *
         * @return The leftmost child of this node.
         */
        private Node getLeftmostChild() {
            return leftChild == null ? this : leftChild;
        }

        /**
         * Returns the right child of this node.
         *
         * @return The right child of this node.
         */
        private Node getRightChild() {
            return rightChild;
        }

        /**
         * Returns the size of this node, which is the size of the left and the right child plus one.
         *
         * @return The size of this node.
         */
        private int getSize() {
            return 1 + getSize(leftChild) + getSize(rightChild);
        }

        /**
         * Helper method returning 0 if the provided parameter is <code>null</code>, and the size of the node otherwise.
         *
         * @param node The node.
         * @return The size of the node, or 0 if the provided parameter is <code>null</code>.
         */
        private int getSize(final Node node) {
            return node == null ? 0 : node.getSize();
        }

        /**
         * Sets the element of this node.
         *
         * @param element The new element for this node.
         */
        private void setElement(final E element) {
            this.element = element;
        }

        /**
         * Sets the left child of this node.
         *
         * @param leftChild The left child for this node.
         */
        private void setLeftChild(final Node leftChild) {
            this.leftChild = leftChild;
        }

        /**
         * Sets the right child of this node.
         *
         * @param rightChild The right child for this node.
         */
        private void setRightChild(final Node rightChild) {
            this.rightChild = rightChild;
        }

        /**
         * Updates the height of this node.
         */
        private void updateHeight() {
            height = Math.max(getHeight(leftChild), getHeight(rightChild)) + 1;
        }
    }

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
    private Node root;
    /**
     * The size of the collection.
     */
    private int size;

    public ModifiableSortedTreeCollection(final Comparator<E> comparator, final E... elements) {
        this(DUPLICATE_ELEMENTS, comparator, elements);
    }

    public ModifiableSortedTreeCollection(final ElementCardinality elementCardinality, final Comparator<E> comparator,
            final E... elements) {
        this.comparator = comparator;
        this.elementCardinality = elementCardinality;
        for (final E element : elements) {
            add(element);
        }
        cachedArray = elements.clone();
        cachedArrayDirty = elements.length != size;
    }

    @Override
    public boolean add(final E element) {
        int originalSize = size;
        root = insertNodeAndUpdateSize(element, root);
        root.updateHeight();
        root = rebalance(root);
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

    private int addNodesToCachedArray(final Node node, final int index) {
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

    private int collectUnmatchedForRemoval(final E[] removeArray, final int removeArraySize, final Node node,
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
        return contains(root, element);
    }

    private boolean contains(final Node node, final E element) {
        if (node == null) {
            return false;
        }
        int comparison = comparator.compare(element, node.getElement());
        if (comparison == 0) {
            return true;
        } else if (comparison < 0) {
            return contains(node.getLeftChild(), element);
        } else {
            return contains(node.getRightChild(), element);
        }
    }

    @Override
    public boolean containsAll(final Collection<?> collection) {
        if (collection.size() > size) {
            return false;
        }
        boolean[] matched = new boolean[size];
        Class<E> componentType = (Class<E>) cachedArray.getClass().getComponentType();
        for (Object element : collection) {
            if (!(componentType.isInstance(element) && findAndMarkMatch(root, matched, 0, (E) element))) {
                return false;
            }
        }
        return true;
    }

    private Node deleteNodeAndUpdateSize(final E element, final Node node) {
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
            Node inOrderSuccessor = node.getRightChild().getLeftmostChild();
            node.setElement(inOrderSuccessor.getElement());
            node.setRightChild(deleteNodeAndUpdateSize(inOrderSuccessor.getElement(), node.getRightChild()));
            node.updateHeight();
            return node;
        }
    }

    private boolean findAndMarkMatch(final Node node, final boolean[] matched, final int index, final E element) {
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

    private E getAt(final Node node, final int index) {
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

    private Node insertNodeAndUpdateSize(final E element, final Node node) {
        if (node == null) {
            size++;
            return new Node(element);
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

    private int markForRemoval(final E[] deleteArray, final int size, final Node node,
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

    private Node rebalance(final Node node) {
        int balanceFactor = node.calculateBalanceFactor();
        if (balanceFactor < -1) {
            if (node.getLeftChild().calculateBalanceFactor() <= 0) {
                return rotateRight(node);
            } else {
                node.setLeftChild(rotateLeft(node.getLeftChild()));
                return rotateRight(node);
            }
        } else if (balanceFactor > 1) {
            if (node.getRightChild().calculateBalanceFactor() >= 0) {
                return rotateLeft(node);
            } else {
                node.setRightChild(rotateRight(node.getRightChild()));
                return rotateLeft(node);
            }
        }
        return node;
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
            root = rebalance(root);
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

    private Node rotateLeft(final Node node) {
        Node rightChild = node.getRightChild();
        node.setRightChild(rightChild.getLeftChild());
        rightChild.setLeftChild(node);
        node.updateHeight();
        rightChild.updateHeight();
        return rightChild;
    }

    private Node rotateRight(final Node node) {
        Node leftChild = node.getLeftChild();
        node.setLeftChild(leftChild.getRightChild());
        leftChild.setRightChild(node);
        node.updateHeight();
        leftChild.updateHeight();
        return leftChild;
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
