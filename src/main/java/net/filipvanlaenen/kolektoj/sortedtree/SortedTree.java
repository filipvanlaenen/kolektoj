package net.filipvanlaenen.kolektoj.sortedtree;

import static net.filipvanlaenen.kolektoj.Collection.ElementCardinality.DISTINCT_ELEMENTS;

import java.lang.reflect.Array;
import java.util.Comparator;
import java.util.function.Predicate;

import net.filipvanlaenen.kolektoj.Collection;
import net.filipvanlaenen.kolektoj.Collection.ElementCardinality;

/**
 * A class implementing an AVL tree.
 *
 * @param <E> The element type.
 */
final class SortedTree<E extends Comparable<E>> {
    /**
     * The comparator to use for comparing the elements in this collection.
     */
    private final Comparator<E> comparator;
    /**
     * The element cardinality.
     */
    private final ElementCardinality elementCardinality;
    /**
     * The type of the elements.
     */
    private final Class<E> elementType;
    /**
     * The root node of the tree.
     */
    private Node<E> root;
    /**
     * The size of the tree.
     */
    private int size;

    /**
     * Creates and empty sorted tree.
     *
     * @param comparator         The comparator.
     * @param elementCardinality The element cardinality.
     * @param elementType        The element type.
     */
    SortedTree(final Comparator<E> comparator, final ElementCardinality elementCardinality,
            final Class<E> elementType) {
        this(comparator, elementCardinality, null, 0, elementType);
    }

    private SortedTree(final Comparator<E> comparator, final ElementCardinality elementCardinality, final Node<E> root,
            final int size, final Class<E> elementType) {
        this.comparator = comparator;
        this.elementType = elementType;
        this.elementCardinality = elementCardinality;
        this.root = root;
        this.size = size;
    }

    /**
     * Adds an element to this tree and returns whether it increased the size of the tree.
     *
     * @param element The element to be added to the tree.
     * @return True if the size of the tree increased after adding the element.
     */
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

    /**
     * Removes all elements from the tree.
     */
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

    /**
     * Returns whether the tree contains the element.
     *
     * @param element The element.
     * @return True if the tree contains the element.
     */
    boolean contains(final E element) {
        return contains(root, element);
    }

    /**
     * Returns whether the subtree starting at the provided node contains the element.
     *
     * @param node    The node.
     * @param element The element.
     * @return True if the subtree at the provided node contains the element.
     */
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

    /**
     * Returns whether this tree contains all the elements of the provided collection, with the same number of
     * occurrences.
     *
     * @param collection The collection to compare elements against.
     * @return True if all of the elements in the provided collection are present in this tree with the same number of
     *         occurrences.
     */
    boolean containsAll(final Collection<?> collection) {
        if (collection.size() > size) {
            return false;
        }
        boolean[] matched = new boolean[size];
        for (Object element : collection) {
            if (!(elementType.isInstance(element) && findAndMarkMatch(root, matched, 0, (E) element))) {
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
     * @param <E> The element type.
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

    /**
     * Returns the element from the tree at the given position.
     *
     * @param index The position of the element that should be returned.
     * @return The element from the tree at the given position.
     */
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

    /**
     * Returns the element of the root node of the tree.
     *
     * @return The element of the root node of the tree.
     */
    E getRootElement() {
        return root.getElement();
    }

    /**
     * Returns the number of elements in the tree.
     *
     * @return The number of elements in the tree.
     */
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

    /**
     * Removes an element from this tree if it is present.
     *
     * @param element The element to be removed from the tree.
     * @return True if the element was present in the tree.
     */
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

    /**
     * Removes all elements from this tree that satisfy the given predicate, and returns whether it decreased the size
     * of the tree.
     *
     * @param predicate The predicate to be applied to each element of the tree.
     * @return True if the size of the tree decreased after removing the elements that satisfied the given predicate.
     */
    boolean removeIf(final Predicate<? super E> predicate) {
        E[] removeArray = (E[]) Array.newInstance(elementType, size);
        int removeArraySize = markForRemoval(removeArray, 0, root, predicate);
        for (int i = 0; i < removeArraySize; i++) {
            remove(removeArray[i]);
        }
        return removeArraySize > 0;
    }

    /**
     * Retains elements from a collection in this tree and removes all other, and returns whether it decreased the size
     * of the tree.
     *
     * @param collection A collection with elements to retain in this tree.
     * @return True if the size of the tree decreased after retaining only the elements from the provided collection.
     */
    boolean retainAll(final Collection<? extends E> collection) {
        boolean[] matched = new boolean[size];
        for (Object element : collection) {
            if (elementType.isInstance(element)) {
                findAndMarkMatch(root, matched, 0, (E) element);
            }
        }
        E[] removeArray = (E[]) Array.newInstance(elementType, size);
        int removeArraySize = collectUnmatchedForRemoval(removeArray, 0, root, matched, 0);
        for (int i = 0; i < removeArraySize; i++) {
            remove(removeArray[i]);
        }
        return removeArraySize > 0;
    }

    /**
     * Returns the content of this tree as an array.
     *
     * @return An array containing the elements of this tree.
     */
    E[] toArray() {
        E[] array = (E[]) Array.newInstance(elementType, size);
        addNodesToArray(array, root, 0);
        return array.clone();
    }
}
