package net.filipvanlaenen.kolektoj.sortedtree;

import static org.junit.jupiter.api.Assertions.*;
import static net.filipvanlaenen.kolektoj.Collection.ElementCardinality.DISTINCT_ELEMENTS;

import java.util.Comparator;
import java.util.Objects;

import org.junit.jupiter.api.Test;

import net.filipvanlaenen.kolektoj.Collection;
import net.filipvanlaenen.kolektoj.Collection.ElementCardinality;

/**
 * Unit tests on the {@link net.filipvanlaenen.kolektoj.sortedtree.SortedTree} class.
 */
public class SortedTreeTest {
    /**
     * The magic number three.
     */
    private static final int THREE = 3;
    /**
     * The magic number five.
     */
    private static final int FIVE = 5;
    /**
     * The magic number twenty.
     */
    private static final int TWENTY = 20;
    /**
     * The magic number twenty-one.
     */
    private static final int TWENTY_ONE = 21;
    /**
     * The magic number hundred.
     */
    private static final int HUNDRED = 100;
    /**
     * A comparator ordering integers in the natural order, but in addition handling <code>null</code> as the lowest
     * value.
     */
    private static final Comparator<Integer> COMPARATOR = new Comparator<Integer>() {
        @Override
        public int compare(final Integer i1, final Integer i2) {
            if (Objects.equals(i1, i2)) {
                return 0;
            } else if (i1 == null) {
                return -1;
            } else if (i2 == null) {
                return 1;
            } else if (i1 < i2) {
                return -1;
            } else {
                return 1;
            }
        }
    };
    /**
     * A comparator ordering integers in the natural order, but modulo five, and handling <code>null</code> as the
     * lowest value.
     */
    protected static final Comparator<Integer> MOD5COMPARATOR = new Comparator<Integer>() {
        @Override
        public int compare(final Integer i1, final Integer i2) {
            if (Objects.equals(i1, i2)) {
                return 0;
            } else if (i1 == null) {
                return -1;
            } else if (i2 == null) {
                return 1;
            } else {
                int m1 = i1 % FIVE;
                int m2 = i2 % FIVE;
                if (m1 == m2) {
                    return 0;
                } else if (m1 < m2) {
                    return -1;
                } else {
                    return 1;
                }
            }
        }
    };
    /**
     * An empty tree to run unit tests on.
     */
    private static final SortedTree<Integer, String> EMPTY_TREE = createEmptyTree();
    /**
     * A tiny tree to run unit tests on, containing only one element.
     */
    private static final SortedTree<Integer, String> TINY_TREE = createTree(1);
    /**
     * A small tree to run unit tests on, containing three elements.
     */
    private static final SortedTree<Integer, String> SMALL_TREE = createTree(THREE);
    /**
     * A large tree to run unit tests on, containing twenty elements.
     */
    private static final SortedTree<Integer, String> LARGE_TREE = createTree(TWENTY);

    /**
     * Factory method to create an empty tree.
     *
     * @return An empty tree.
     */
    private static SortedTree<Integer, String> createEmptyTree() {
        return new SortedTree<Integer, String>(COMPARATOR, DISTINCT_ELEMENTS);
    }

    /**
     * Factory method to create a tree of a provided size.
     *
     * @param size The size of the tree.
     * @return A tree with the given size.
     */
    private static SortedTree<Integer, String> createTree(final int size) {
        SortedTree<Integer, String> tree = createEmptyTree();
        for (int key = 1; key <= size; key++) {
            tree.add(key, Integer.toString(key));
        }
        return tree;
    }

    /**
     * Verifies that adding an element to an empty tree returns true.
     */
    @Test
    public void addShouldReturnTrueOnAnEmptyTree() {
        assertTrue(createEmptyTree().add(1, "1"));
    }

    /**
     * Verifies that trying to add an element that is already present returns false.
     */
    @Test
    public void addShouldReturnFalseOnATreeWithDistinct() {
        assertFalse(createTree(1).add(1, "1"));
    }

    /**
     * Verifies that adding an element to an empty tree increases the size to one.
     */
    @Test
    public void addShouldIncreaseSizeToOneForEmptyTree() {
        SortedTree<Integer, String> tree = createTree(1);
        assertEquals(1, tree.getSize());
    }

    /**
     * Verifies that clearing a tree sets its size to zero.
     */
    @Test
    public void clearShouldSetSizeToZero() {
        SortedTree<Integer, String> tree = createTree(1);
        tree.clear();
        assertEquals(0, tree.getSize());
    }

    /**
     * Verifies that clearing a tree sets its root node to <code>null</code>.
     */
    @Test
    public void clearShouldSetRootNodeToNull() {
        SortedTree<Integer, String> tree = createTree(1);
        tree.clear();
        assertNull(tree.getRootNode());
    }

    /**
     * Verifies that containsAllKeys returns true when all keys are present in the tree.
     */
    @Test
    public void containsAllKeysShouldReturnTrueIfAllKeysArePresent() {
        assertTrue(SMALL_TREE.containsAllKeys(Collection.of(1, 2, THREE)));
    }

    /**
     * Verifies that containsAllKeys returns false on an empty tree.
     */
    @Test
    public void containsAllKeysShouldReturnFalseEmptyKey() {
        assertFalse(EMPTY_TREE.containsAllKeys(Collection.of(1)));
    }

    /**
     * Verifies that containsAllKeys returns false when at least one key is absent in the tree.
     */
    @Test
    public void containsAllKeysShouldReturnFalseAKeyIsAbsent() {
        assertFalse(SMALL_TREE.containsAllKeys(Collection.of(1, TWENTY)));
    }

    /**
     * Verifies that an empty tree doesn't contain an element.
     */
    @Test
    public void containsKeyShouldReturnFalseForEmptyTree() {
        assertFalse(EMPTY_TREE.containsKey(1));
    }

    /**
     * Verifies that a tiny tree contains its element.
     */
    @Test
    public void containsKeyShouldReturnTrueForKeyInTinyTree() {
        assertTrue(TINY_TREE.containsKey(1));
    }

    /**
     * Verifies that a tiny tree doesn't contain elements it doesn't contain.
     */
    @Test
    public void containsKeyShouldReturnFalseForKeyAbsentInTinyTree() {
        assertFalse(TINY_TREE.containsKey(0));
        assertFalse(TINY_TREE.containsKey(HUNDRED));
    }

    /**
     * Verifies that a small tree contains its element.
     */
    @Test
    public void containsKeyShouldReturnTrueForKeysInSmallTree() {
        for (int key = 1; key <= THREE; key++) {
            assertTrue(SMALL_TREE.containsKey(key));
        }
    }

    /**
     * Verifies that a small tree doesn't contain elements it doesn't contain.
     */
    @Test
    public void containsKeyShouldReturnFalseForKeysAbsentInSmallTree() {
        assertFalse(SMALL_TREE.containsKey(0));
        assertFalse(SMALL_TREE.containsKey(HUNDRED));
    }

    /**
     * Verifies that a large tree contains its element.
     */
    @Test
    public void containsKeyShouldReturnTrueForKeysInLargeTree() {
        for (int key = 1; key <= TWENTY; key++) {
            assertTrue(LARGE_TREE.containsKey(key));
        }
    }

    /**
     * Verifies that a large tree doesn't contain elements it doesn't contain.
     */
    @Test
    public void containsKeyShouldReturnFalseForKeysAbsentInLargeTree() {
        assertFalse(LARGE_TREE.containsKey(0));
        assertFalse(LARGE_TREE.containsKey(HUNDRED));
    }

    /**
     * Verifies that firstIndexOf returns -1 for an element not in a small tree.
     */
    @Test
    public void firstIndexOfShouldReturnMinusOneForAnElementNotInTheTree() {
        assertEquals(-1, SMALL_TREE.firstIndexOf(0));
    }

    /**
     * Verifies that firstIndexOf returns the correct index for an element in a small tree.
     */
    @Test
    public void firstIndexOfShouldReturnIndexForAnElementInTheTree() {
        assertEquals(0, SMALL_TREE.firstIndexOf(1));
        assertEquals(1, SMALL_TREE.firstIndexOf(2));
        assertEquals(2, SMALL_TREE.firstIndexOf(THREE));
    }

    /**
     * Verifies that firstIndexOf returns the correct index for a duplicate element in a tree.
     */
    @Test
    public void firstIndexOfShouldReturnFirstIndexForADuplicateElementInTheTree() {
        SortedTree<Integer, String> sortedTree =
                new SortedTree<Integer, String>(MOD5COMPARATOR, ElementCardinality.DUPLICATE_ELEMENTS);
        for (int i = 1; i <= TWENTY_ONE; i++) {
            sortedTree.add(i, Integer.toString(i));
        }
        for (int i = 1; i <= TWENTY_ONE; i++) {
            assertEquals(i, sortedTree.getAt(sortedTree.firstIndexOf(i)).getKey());
        }
    }

    /**
     * Verifies that firstIndexOf returns an index lower than lastIndexOf.
     */
    @Test
    public void firstIndexOfShouldReturnALowerIndexThanLastIndexOf() {
        SortedTree<Integer, String> sortedTree =
                new SortedTree<Integer, String>(MOD5COMPARATOR, ElementCardinality.DUPLICATE_ELEMENTS);
        for (int i = 1; i <= TWENTY; i++) {
            sortedTree.add(i, Integer.toString(i));
            sortedTree.add(i, Integer.toString(i));
        }
        for (int i = 1; i <= TWENTY; i++) {
            assertTrue(sortedTree.firstIndexOf(i) < sortedTree.lastIndexOf(i));
        }
    }

    /**
     * Verifies that <code>getRootNode</code> returns <code>null</code> for an empty tree.
     */
    @Test
    public void getRootNodeShouldReturnNullForEmptyTree() {
        assertNull(EMPTY_TREE.getRootNode());
    }

    /**
     * Verifies that <code>getRootNode</code> returns the root node for a tiny tree.
     */
    @Test
    public void getRootNodeShouldReturnNodeWithKeyAndValueForTinyTree() {
        TreeNode<Integer, String> node = TINY_TREE.getRootNode();
        assertEquals(1, node.getKey());
        assertEquals("1", node.getContent());
    }

    /**
     * Verifies that <code>getGreatest</code> returns the rightmost node.
     */
    @Test
    public void getGreatestShouldReturnRightmostNode() {
        TreeNode<Integer, String> node = LARGE_TREE.getGreatest();
        assertEquals(TWENTY, node.getKey());
        assertEquals("20", node.getContent());
    }

    /**
     * Verifies that <code>getLeast</code> returns the leftmost node.
     */
    @Test
    public void getLeastShouldReturnLeftmostNode() {
        TreeNode<Integer, String> node = LARGE_TREE.getLeast();
        assertEquals(1, node.getKey());
        assertEquals("1", node.getContent());
    }

    /**
     * Verifies that <code>getNode</code> with a key returns <code>null</code> for an empty tree.
     */
    @Test
    public void getNodeWithKeyShouldReturnNullForEmptyTree() {
        assertNull(EMPTY_TREE.getNode(1));
    }

    /**
     * Verifies that <code>getNode</code> with the correct key returns the root node for a tiny tree.
     */
    @Test
    public void getNodeWithKeyShouldReturnRootNodeForTinyTree() {
        TreeNode<Integer, String> node = TINY_TREE.getNode(1);
        assertEquals(1, node.getKey());
        assertEquals("1", node.getContent());
    }

    /**
     * Verifies that <code>getNode</code> with an absent key returns <code>null</code> for a tiny tree.
     */
    @Test
    public void getNodeWithKeyShouldReturnNullForKeyAbsentInTinyTree() {
        assertNull(TINY_TREE.getNode(0));
        assertNull(TINY_TREE.getNode(HUNDRED));
    }

    /**
     * Verifies that <code>getNode</code> with the correct key returns a node for a small tree.
     */
    @Test
    public void getNodeWithKeyShouldReturnNodeWithKeyAndValueForSmallTree() {
        for (int key = 1; key <= THREE; key++) {
            TreeNode<Integer, String> node = SMALL_TREE.getNode(key);
            assertEquals(key, node.getKey());
            assertEquals(Integer.toString(key), node.getContent());
        }
    }

    /**
     * Verifies that <code>getNode</code> with an absent key returns <code>null</code> for a small tree.
     */
    @Test
    public void getNodeWithKeyShouldReturnNullForKeyAbsentInSmallTree() {
        assertNull(SMALL_TREE.getNode(0));
        assertNull(SMALL_TREE.getNode(HUNDRED));
    }

    /**
     * Verifies that <code>getNode</code> with the correct key returns a node for a large tree.
     */
    @Test
    public void getNodeWithKeyShouldReturnNodeWithKeyAndValueForLargeTree() {
        for (int key = 1; key <= TWENTY; key++) {
            TreeNode<Integer, String> node = LARGE_TREE.getNode(key);
            assertEquals(key, node.getKey());
            assertEquals(Integer.toString(key), node.getContent());
        }
    }

    /**
     * Verifies that <code>getNode</code> with an absent key returns <code>null</code> for a large tree.
     */
    @Test
    public void getNodeWithKeyShouldReturnNullForKeyAbsentInLargeTree() {
        assertNull(LARGE_TREE.getNode(0));
        assertNull(LARGE_TREE.getNode(HUNDRED));
    }

    /**
     * Verifies that indexOf returns -1 for an element not in a small tree.
     */
    @Test
    public void indexOfShouldReturnMinusOneForAnElementNotInTheTree() {
        assertEquals(-1, SMALL_TREE.indexOf(0));
    }

    /**
     * Verifies that indexOf returns the correct index for an element in a small tree.
     */
    @Test
    public void indexOfShouldReturnIndexForAnElementInTheTree() {
        assertEquals(0, SMALL_TREE.indexOf(1));
        assertEquals(1, SMALL_TREE.indexOf(2));
        assertEquals(2, SMALL_TREE.indexOf(THREE));
    }

    /**
     * Verifies that indexOf returns the correct index for a duplicate element in a tree.
     */
    @Test
    public void indexOfShouldReturnFirstIndexForADuplicateElementInTheTree() {
        SortedTree<Integer, String> sortedTree =
                new SortedTree<Integer, String>(MOD5COMPARATOR, ElementCardinality.DUPLICATE_ELEMENTS);
        for (int i = 1; i <= TWENTY_ONE; i++) {
            sortedTree.add(i, Integer.toString(i));
        }
        for (int i = 1; i <= TWENTY_ONE; i++) {
            assertEquals(i, sortedTree.getAt(sortedTree.indexOf(i)).getKey());
        }
    }

    /**
     * Verifies that lastIndexOf returns -1 for an element not in a small tree.
     */
    @Test
    public void lastIndexOfShouldReturnMinusOneForAnElementNotInTheTree() {
        assertEquals(-1, SMALL_TREE.lastIndexOf(0));
    }

    /**
     * Verifies that lastIndexOf returns the correct index for an element in a small tree.
     */
    @Test
    public void lastIndexOfShouldReturnIndexForAnElementInTheTree() {
        assertEquals(0, SMALL_TREE.lastIndexOf(1));
        assertEquals(1, SMALL_TREE.lastIndexOf(2));
        assertEquals(2, SMALL_TREE.lastIndexOf(THREE));
    }

    /**
     * Verifies that lastIndexOf returns the correct index for a duplicate element in a tree.
     */
    @Test
    public void lastIndexOfShouldReturnFirstIndexForADuplicateElementInTheTree() {
        SortedTree<Integer, String> sortedTree =
                new SortedTree<Integer, String>(MOD5COMPARATOR, ElementCardinality.DUPLICATE_ELEMENTS);
        for (int i = 1; i <= TWENTY; i++) {
            sortedTree.add(i, Integer.toString(i));
        }
        for (int i = 1; i <= TWENTY; i++) {
            assertEquals(i, sortedTree.getAt(sortedTree.lastIndexOf(i)).getKey());
        }
    }

    /**
     * Verifies that removing an element from an empty tree returns false.
     */
    @Test
    public void removeShouldReturnFalseOnAnEmptyTree() {
        assertFalse(createEmptyTree().remove(1));
    }

    /**
     * Verifies that trying to remove an absent key returns false.
     */
    @Test
    public void removeShouldReturnFalseOnAbsentKey() {
        assertFalse(createTree(1).remove(2));
    }

    /**
     * Verifies that trying to remove a present key returns true.
     */
    @Test
    public void removeShouldReturnTrueOnPresentKey() {
        assertTrue(createTree(1).remove(1));
    }

    /**
     * Verifies that the size decreases after removing a present key.
     */
    @Test
    public void removeShouldDecreaseSizeForPresentKey() {
        SortedTree<Integer, String> tree = createTree(THREE);
        tree.remove(1);
        assertEquals(2, tree.getSize());
    }

    /**
     * Verifies that removeIf with a predicate that never matches returns false.
     */
    @Test
    public void removeIfShouldReturnFalseWhenNoMatch() {
        assertFalse(createTree(THREE).removeIf(k -> k == 0));
    }

    /**
     * Verifies that removeIf with a predicate that matches returns true.
     */
    @Test
    public void removeIfShouldReturnTrueWhenMatch() {
        assertTrue(createTree(THREE).removeIf(k -> k % 2 == 0));
    }

    /**
     * Verifies that removeIf with a predicate that matches removes a node from the tree.
     */
    @Test
    public void removeIfShouldRemoveNodeWithMatchingKey() {
        SortedTree<Integer, String> tree = createTree(THREE);
        tree.removeIf(k -> k == 2);
        assertFalse(tree.containsKey(2));
    }

    /**
     * Verifies that retainAllKeys should return true when not all nodes in the tree are retained.
     */
    @Test
    public void retainAllKeysShouldReturnTrueIfNotAllNodesWereRetained() {
        SortedTree<Integer, String> tree = createTree(THREE);
        assertTrue(tree.retainAllKeys(Collection.of(1, 2)));
    }

    /**
     * Verifies that retainAllKeys should return false when all nodes in the tree are retained.
     */
    @Test
    public void retainAllKeysShouldReturnFalseIfAllNodesWereRetained() {
        SortedTree<Integer, String> tree = createTree(THREE);
        assertFalse(tree.retainAllKeys(Collection.of(1, 2, THREE)));
    }

    /**
     * Verifies that retainAllKeys doesn't retain a node with an absent key.
     */
    @Test
    public void retainAllKeysShouldNotRetainANodeWithAnAbsentKey() {
        SortedTree<Integer, String> tree = createTree(THREE);
        tree.retainAllKeys(Collection.of(1, 2));
        assertFalse(tree.containsKey(THREE));
    }

    /**
     * Verifies that toArray returns an empty array for an empty tree.
     */
    @Test
    public void toArrayShouldReturnAnEmptyArrayForAnEmptyTree() {
        assertEquals(0, EMPTY_TREE.toArray().length);
    }

    /**
     * Verifies that toArray returns an array with three elements for a small tree.
     */
    @Test
    public void toArrayShouldReturnASmallArrayForASmallTree() {
        TreeNode<Integer, String>[] nodeArray = SMALL_TREE.toArray();
        for (int i = 1; i <= THREE; i++) {
            TreeNode<Integer, String> node = nodeArray[i - 1];
            assertEquals(i, node.getKey());
            assertEquals(Integer.toString(i), node.getContent());
        }
    }
}
