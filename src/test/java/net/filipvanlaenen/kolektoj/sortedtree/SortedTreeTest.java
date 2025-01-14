package net.filipvanlaenen.kolektoj.sortedtree;

import static org.junit.jupiter.api.Assertions.*;
import static net.filipvanlaenen.kolektoj.Collection.ElementCardinality.DISTINCT_ELEMENTS;

import java.util.Comparator;
import java.util.Objects;

import org.junit.jupiter.api.Test;

/**
 * Unit tests on the {@link net.filipvanlaenen.kolektoj.sortedtree.SortedTree} class.
 */
public class SortedTreeTest {
    /**
     * The magic number three.
     */
    private static final int THREE = 3;
    /**
     * The magic number twenty.
     */
    private static final int TWENTY = 20;
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
        Node<Integer, String> node = TINY_TREE.getRootNode();
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
        Node<Integer, String> node = TINY_TREE.getNode(1);
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
            Node<Integer, String> node = SMALL_TREE.getNode(key);
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
            Node<Integer, String> node = LARGE_TREE.getNode(key);
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
}
