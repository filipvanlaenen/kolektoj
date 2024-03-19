package net.filipvanlaenen.kolektoj.sortedtree;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static net.filipvanlaenen.kolektoj.Collection.ElementCardinality.DISTINCT_ELEMENTS;
import java.util.Comparator;
import java.util.Objects;

import org.junit.jupiter.api.Test;

public class SortedTreeTest {
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
    private static final SortedTree<Integer, Integer> EMPTY_TREE = createEmptyTree();
    private static final SortedTree<Integer, Integer> TINY_TREE = createTree(1);
    private static final SortedTree<Integer, Integer> SMALL_TREE = createTree(3);
    private static final SortedTree<Integer, Integer> LARGE_TREE = createTree(20);

    private static SortedTree<Integer, Integer> createEmptyTree() {
        return new SortedTree<Integer, Integer>(COMPARATOR, DISTINCT_ELEMENTS);
    }

    private static SortedTree<Integer, Integer> createTree(final int size) {
        SortedTree<Integer, Integer> tree = createEmptyTree();
        for (int key = 1; key <= size; key++) {
            tree.add(key, key + 1);
        }
        return tree;
    }

    @Test
    public void containsShouldReturnFalseForEmptyTree() {
        assertFalse(EMPTY_TREE.containsKey(1));
    }

    @Test
    public void containsShouldReturnTrueForKeyInTinyTree() {
        assertTrue(TINY_TREE.containsKey(1));
    }

    @Test
    public void containsShouldReturnFalseForKeyAbsentInTinyTree() {
        assertFalse(TINY_TREE.containsKey(0));
        assertFalse(TINY_TREE.containsKey(100));
    }

    @Test
    public void containsShouldReturnTrueForKeysInSmallTree() {
        for (int key = 1; key <= 3; key++) {
            assertTrue(SMALL_TREE.containsKey(key));
        }
    }

    @Test
    public void containsShouldReturnFalseForKeysAbsentInSmallTree() {
        assertFalse(SMALL_TREE.containsKey(0));
        assertFalse(SMALL_TREE.containsKey(100));
    }

    @Test
    public void containsShouldReturnTrueForKeysInLargeTree() {
        for (int key = 1; key <= 20; key++) {
            assertTrue(LARGE_TREE.containsKey(key));
        }
    }

    @Test
    public void containsShouldReturnFalseForKeysAbsentInLargeTree() {
        assertFalse(LARGE_TREE.containsKey(0));
        assertFalse(LARGE_TREE.containsKey(100));
    }

    @Test
    public void getNodeShouldReturnNullForEmptyTree() {
        assertNull(EMPTY_TREE.getRootNode());
    }

    @Test
    public void getNodeShouldReturnNodeWithKeyAndValueForTinyTree() {
        Node<Integer, Integer> node = TINY_TREE.getRootNode();
        assertEquals(1, node.getKey());
        assertEquals(2, node.getContent());
    }

    @Test
    public void getNodeWithKeyShouldReturnNullForEmptyTree() {
        assertNull(EMPTY_TREE.getNode(1));
    }

    @Test
    public void getNodeWithKeyShouldReturnNodeWithKeyAndValueForTinyTree() {
        Node<Integer, Integer> node = TINY_TREE.getNode(1);
        assertEquals(1, node.getKey());
        assertEquals(2, node.getContent());
    }

    @Test
    public void getNodeWithKeyShouldReturnNullForKeyAbsentInTinyTree() {
        assertNull(TINY_TREE.getNode(0));
        assertNull(TINY_TREE.getNode(100));
    }

    @Test
    public void getNodeWithKeyShouldReturnNodeWithKeyAndValueForSmallTree() {
        for (int key = 1; key <= 3; key++) {
            Node<Integer, Integer> node = SMALL_TREE.getNode(key);
            assertEquals(key, node.getKey());
            assertEquals(key + 1, node.getContent());
        }
    }

    @Test
    public void getNodeWithKeyShouldReturnNullForKeyAbsentInSmallTree() {
        assertNull(SMALL_TREE.getNode(0));
        assertNull(SMALL_TREE.getNode(100));
    }

    @Test
    public void getNodeWithKeyShouldReturnNodeWithKeyAndValueForLargeTree() {
        for (int key = 1; key <= 20; key++) {
            Node<Integer, Integer> node = LARGE_TREE.getNode(key);
            assertEquals(key, node.getKey());
            assertEquals(key + 1, node.getContent());
        }
    }

    @Test
    public void getNodeWithKeyShouldReturnNullForKeyAbsentInLargeTree() {
        assertNull(LARGE_TREE.getNode(0));
        assertNull(LARGE_TREE.getNode(100));
    }
}
