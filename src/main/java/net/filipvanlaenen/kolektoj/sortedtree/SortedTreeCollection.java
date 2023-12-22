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
public class SortedTreeCollection<E extends Comparable<E>> implements SortedCollection<E> {
    private final class Node {
        private E element;
        private int height;
        private Node leftChild;
        private Node rightChild;

        private Node(final E element) {
            this.element = element;
        }

        private int calculateBalanceFactor() {
            return height(rightChild) - height(leftChild);
        }

        private int getChildSize(final Node child) {
            return child == null ? 0 : child.getSize();
        }

        private E getElement() {
            return element;
        }

        private int getHeight() {
            return height;
        }

        private Node getLeftChild() {
            return leftChild;
        }

        private Node getLeftmostChild() {
            return leftChild == null ? this : leftChild;
        }

        private Node getRightChild() {
            return rightChild;
        }

        private int getSize() {
            return 1 + getChildSize(leftChild) + getChildSize(rightChild);
        }

        private int height(final Node node) {
            return node != null ? node.getHeight() : -1;
        }

        private void setElement(final E newElement) {
            this.element = newElement;

        }

        private void setLeftChild(final Node newLeftChild) {
            leftChild = newLeftChild;
        }

        private void setRightChild(final Node newRightChild) {
            rightChild = newRightChild;
        }

        private void updateHeight() {
            height = Math.max(height(leftChild), height(rightChild)) + 1;
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
     * The element cardinality.
     */
    private final Comparator<E> comparator;
    private final ElementCardinality elementCardinality;
    private Node root;
    /**
     * The size of the collection.
     */
    private int size;

    public SortedTreeCollection(final Comparator<E> comparator, final E... elements) {
        this(DUPLICATE_ELEMENTS, comparator, elements);
    }

    public SortedTreeCollection(final ElementCardinality elementCardinality, final Comparator<E> comparator,
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
        cachedArrayDirty = size != originalSize;
        return cachedArrayDirty;
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
        boolean[] matches = new boolean[size];
        Class<E> componentType = (Class<E>) cachedArray.getClass().getComponentType();
        for (Object element : collection) {
            findMatch(root, matches, 0, (E) element);
        }
        for (boolean match : matches) {
            if (!match) {
                return false;
            }
        }
        return true;
    }

    private void findMatch(final Node node, final boolean[] matches, final int index, E element) {
        if (node == null) {
            return;
        }
        E castElement = (E) element;
        if (!matches[index] && comparator.compare(node.getElement(), castElement) == 0) {
            matches[index] = true;
            return;
        }
    }

    private Node deleteNodeAndUpdateSize(final E element, final Node node) {
        if (node == null) {
            return null;
        } else if (comparator.compare(element, node.getElement()) < 0) {
            node.setLeftChild(deleteNodeAndUpdateSize(element, node.getLeftChild()));
            return node;
        } else if (comparator.compare(element, node.getElement()) > 0) {
            node.setRightChild(deleteNodeAndUpdateSize(element, node.getRightChild()));
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
            return node;
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
            return node;
        } else if (comparator.compare(element, node.getElement()) > 0) {
            node.setRightChild(insertNodeAndUpdateSize(element, node.getRightChild()));
            return node;
        } else if (elementCardinality == DISTINCT_ELEMENTS) {
            return node;
        } else {
            node.setRightChild(insertNodeAndUpdateSize(element, node.getRightChild()));
            return node;
        }
    }

    @Override
    public Iterator<E> iterator() {
        return new ArrayIterator<E>(toArray());
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
        cachedArrayDirty = size != originalSize;
        return cachedArrayDirty;
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
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean retainAll(final Collection<? extends E> collection) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public Spliterator<E> spliterator() {
        return new ArraySpliterator<E>(toArray(), Spliterator.ORDERED | Spliterator.SORTED
                | (elementCardinality == DISTINCT_ELEMENTS ? Spliterator.DISTINCT : 0));
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
