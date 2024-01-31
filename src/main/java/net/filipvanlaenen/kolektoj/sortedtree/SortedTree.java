package net.filipvanlaenen.kolektoj.sortedtree;

import static net.filipvanlaenen.kolektoj.Collection.ElementCardinality.DISTINCT_ELEMENTS;

import java.lang.reflect.Array;
import java.util.Comparator;
import java.util.function.Predicate;

import net.filipvanlaenen.kolektoj.Collection;
import net.filipvanlaenen.kolektoj.Collection.ElementCardinality;

final class SortedTree<E extends Comparable<E>> {
    /**
     * The comparator to use for comparing the elements in this collection.
     */
    private final Comparator<E> comparator;
    private final Class<E> componentType;
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

    SortedTree(final Comparator<E> comparator, final ElementCardinality elementCardinality,
            final Class<E> componentType) {
        this(comparator, elementCardinality, null, 0, componentType);
    }

    private SortedTree(final Comparator<E> comparator, final ElementCardinality elementCardinality, final Node<E> root,
            final int size, final Class<E> componentType) {
        this.comparator = comparator;
        this.componentType = componentType;
        this.elementCardinality = elementCardinality;
        this.root = root;
        this.size = size;
    }

    boolean add(final E element) {
        int originalSize = size;
        root = insertNodeAndUpdateSize(element, root);
        root.updateHeight();
        root = root.rebalance();
        return size != originalSize;
    }

    private int addNodesToArray(final E[] array, final Node<E> node, final int index) {
        if (node == null) {
            return index;
        }
        int result = addNodesToArray(array, node.getLeftChild(), index);
        array[result++] = node.getElement();
        return addNodesToArray(array, node.getRightChild(), result);
    }

    void clear() {
        root = null;
        size = 0;
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

    boolean contains(E element) {
        return contains(root, element);
    }

    boolean contains(final Node<E> node, final E element) {
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

    boolean containsAll(final Collection<?> collection) {
        if (collection.size() > size) {
            return false;
        }
        boolean[] matched = new boolean[size];
        for (Object element : collection) {
            if (!(componentType.isInstance(element) && findAndMarkMatch(root, matched, 0, (E) element))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Creates a sorted tree from a sorted array from the provided first to last index.
     *
     * @param sortedArray The array with the elements, sorted.
     * @param firstIndex  The first index to be included in the tree.
     * @param lastIndex   The last index to be included in the tree.
     * @return A sorted tree with the elements from the sorted array.
     */
    private static <E extends Comparable<E>> Node<E> createSortedTree(final E[] sortedArray, final int firstIndex,
            final int lastIndex) {
        int middleIndex = firstIndex + (lastIndex - firstIndex) / 2;
        Node<E> node = new Node<E>(sortedArray[middleIndex]);
        if (middleIndex > firstIndex) {
            node.setLeftChild(createSortedTree(sortedArray, firstIndex, middleIndex - 1));
        }
        if (middleIndex < lastIndex) {
            node.setRightChild(createSortedTree(sortedArray, middleIndex + 1, lastIndex));
        }
        return node;
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

    static <E extends Comparable<E>> SortedTree<E> fromSortedArray(final Comparator<E> comparator,
            final ElementCardinality elementCardinality, final E[] sortedArray) {
        int size = sortedArray.length;
        return new SortedTree<E>(comparator, elementCardinality,
                size == 0 ? null : createSortedTree(sortedArray, 0, size - 1), size,
                (Class<E>) sortedArray.getClass().getComponentType());
    }

    E getAt(final int index) throws IndexOutOfBoundsException {
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

    E getRootElement() {
        return root.getElement();
    }

    int getSize() {
        return size;
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

    private int markForRemoval(final E[] deleteArray, final int index, final Node<E> node,
            final Predicate<? super E> predicate) {
        if (node == null) {
            return index;
        }
        int newIndex = index;
        if (predicate.test(node.getElement())) {
            deleteArray[newIndex++] = node.getElement();
        }
        newIndex = markForRemoval(deleteArray, newIndex, node.getLeftChild(), predicate);
        newIndex = markForRemoval(deleteArray, newIndex, node.getRightChild(), predicate);
        return newIndex;
    }

    boolean remove(final E element) {
        if (root == null) {
            return false;
        }
        int originalSize = size;
        root = deleteNodeAndUpdateSize(element, root);
        if (root != null) {
            root.updateHeight();
            root = root.rebalance();
        }
        return size != originalSize;
    }

    boolean removeIf(final Predicate<? super E> predicate) {
        E[] removeArray = (E[]) Array.newInstance(componentType, size);
        int removeArraySize = markForRemoval(removeArray, 0, root, predicate);
        for (int i = 0; i < removeArraySize; i++) {
            remove(removeArray[i]);
        }
        return removeArraySize > 0;
    }

    boolean retainAll(final Collection<? extends E> collection) {
        boolean[] matched = new boolean[size];
        for (Object element : collection) {
            if (componentType.isInstance(element)) {
                findAndMarkMatch(root, matched, 0, (E) element);
            }
        }
        E[] removeArray = (E[]) Array.newInstance(componentType, size);
        int removeArraySize = collectUnmatchedForRemoval(removeArray, 0, root, matched, 0);
        for (int i = 0; i < removeArraySize; i++) {
            remove(removeArray[i]);
        }
        return removeArraySize > 0;
    }

    E[] toArray() {
        E[] array = (E[]) Array.newInstance(componentType, size);
        addNodesToArray(array, root, 0);
        return array.clone();
    }
}
