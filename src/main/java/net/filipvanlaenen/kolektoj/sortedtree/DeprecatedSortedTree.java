package net.filipvanlaenen.kolektoj.sortedtree;

import static net.filipvanlaenen.kolektoj.Collection.ElementCardinality.DISTINCT_ELEMENTS;

import java.lang.reflect.Array;
import java.util.Comparator;
import java.util.Objects;
import java.util.function.Predicate;

import net.filipvanlaenen.kolektoj.Collection;
import net.filipvanlaenen.kolektoj.Collection.ElementCardinality;

/**
 * A class implementing an AVL tree.
 *
 * @param <E> The element type.
 */
abstract class DeprecatedSortedTree<E, N extends DeprecatedNode<E>> {
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
    private DeprecatedNode<E> root;
    /**
     * The size of the tree.
     */
    private int size;

    protected DeprecatedSortedTree(final Comparator<E> comparator, final ElementCardinality elementCardinality,
            final DeprecatedNode<E> root, final int size, final Class<E> elementType) {
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
    boolean add(final E element, DeprecatedNode<E> newNode) {
        int originalSize = size;
        root = insertNodeAndUpdateSize(element, newNode, root);
        root.updateHeight();
        root = root.rebalance();
        return size != originalSize;
    }

    private int addNodesToArray(final E[] array, final DeprecatedNode<E> node, final int index) {
        if (node == null) {
            return index;
        }
        int result = addNodesToArray(array, node.getLeftChild(), index);
        array[result++] = node.getSortingKey();
        return addNodesToArray(array, node.getRightChild(), result);
    }

    /**
     * Removes all elements from the tree.
     */
    void clear() {
        root = null;
        size = 0;
    }

    private int collectUnmatchedForRemoval(final E[] removeArray, final int removeArraySize, final DeprecatedNode<E> node,
            final boolean[] matched, final int index) {
        if (node == null) {
            return removeArraySize;
        }
        int result = removeArraySize;
        if (!matched[index]) {
            removeArray[result++] = node.getSortingKey();
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
        DeprecatedNode<E> node = findNode(root, element);
        return node != null && Objects.equals(element, node.getSortingKey());
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

    private DeprecatedNode<E> deleteNodeAndUpdateSize(final E element, final DeprecatedNode<E> node) {
        if (node == null) {
            return null;
        } else if (comparator.compare(element, node.getSortingKey()) < 0) {
            node.setLeftChild(deleteNodeAndUpdateSize(element, node.getLeftChild()));
            node.updateHeight();
            return node;
        } else if (comparator.compare(element, node.getSortingKey()) > 0) {
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
            DeprecatedNode<E> inOrderSuccessor = node.getRightChild().getLeftmostChild();
            node.setSortingKey(inOrderSuccessor.getSortingKey());
            node.setRightChild(deleteNodeAndUpdateSize(inOrderSuccessor.getSortingKey(), node.getRightChild()));
            node.updateHeight();
            return node;
        }
    }

    private boolean findAndMarkMatch(final DeprecatedNode<E> node, final boolean[] matched, final int index, final E element) {
        if (node == null) {
            return false;
        }
        int comparison = comparator.compare(element, node.getSortingKey());
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

    E find(final E element) {
        DeprecatedNode<E> node = findNode(element);
        return node == null ? null : node.getSortingKey();
    }

    DeprecatedNode<E> findNode(final E element) {
        return findNode(root, element);
    }

    /**
     * Returns the node containing the element, if the element is present, starting at the provided node .
     *
     * @param node    The node.
     * @param element The element.
     * @return The node containing the element if there is one, and <code>null</code> otherwise.
     */
    private DeprecatedNode<E> findNode(final DeprecatedNode<E> node, final E element) {
        if (node == null) {
            return null;
        }
        int comparison = comparator.compare(element, node.getSortingKey());
        if (comparison == 0) {
            return node;
        } else if (comparison < 0) {
            return findNode(node.getLeftChild(), element);
        } else {
            return findNode(node.getRightChild(), element);
        }
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
    private E getAt(final DeprecatedNode<E> node, final int index) {
        int leftSize = node.getLeftChild().getSize();
        if (leftSize < index) {
            return getAt(node.getRightChild(), index - leftSize - 1);
        } else if (leftSize == index) {
            return node.getSortingKey();
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
        return root.getSortingKey();
    }

    /**
     * Returns the number of elements in the tree.
     *
     * @return The number of elements in the tree.
     */
    int getSize() {
        return size;
    }

    private DeprecatedNode<E> insertNodeAndUpdateSize(E element, DeprecatedNode<E> newNode, final DeprecatedNode<E> node) {
        if (node == null) {
            size++;
            return newNode;
        } else if (comparator.compare(element, node.getSortingKey()) < 0) {
            node.setLeftChild(insertNodeAndUpdateSize(element, newNode, node.getLeftChild()));
            node.updateHeight();
            return node;
        } else if (comparator.compare(element, node.getSortingKey()) > 0) {
            node.setRightChild(insertNodeAndUpdateSize(element, newNode, node.getRightChild()));
            node.updateHeight();
            return node;
        } else if (elementCardinality == DISTINCT_ELEMENTS) {
            return node;
        } else {
            node.setRightChild(insertNodeAndUpdateSize(element, newNode, node.getRightChild()));
            node.updateHeight();
            return node;
        }
    }

    private int markForRemoval(final E[] deleteArray, final int index, final DeprecatedNode<E> node,
            final Predicate<? super E> predicate) {
        if (node == null) {
            return index;
        }
        int newIndex = index;
        if (predicate.test(node.getSortingKey())) {
            deleteArray[newIndex++] = node.getSortingKey();
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
