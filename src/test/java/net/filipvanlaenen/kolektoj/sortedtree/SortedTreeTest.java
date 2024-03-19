package net.filipvanlaenen.kolektoj.sortedtree;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
    private static final SortedTree<Integer, Integer> EMPTY_TREE = new SortedTree(COMPARATOR);

    @Test
    public void containsShouldReturnFalseForEmptyTree() {
        assertFalse(EMPTY_TREE.contains(1));
    }

    @Test
    public void getNodeShouldReturnNullForEmptyTree() {
        assertNull(EMPTY_TREE.getNode());
    }

    @Test
    public void getNodeWithKeyShouldReturnNullForEmptyTree() {
        assertNull(EMPTY_TREE.getNode(1));
    }

    @Test
    public void containsShouldReturnTrueForKeyAddedToEmptyTree() {
        SortedTree<Integer, Integer> sortedTree = new SortedTree(COMPARATOR);
        sortedTree.add(1, 2);
        assertTrue(sortedTree.contains(1));
    }

    @Test
    public void containsShouldReturnFalseForKeyNotAddedToEmptyTree() {
        SortedTree<Integer, Integer> sortedTree = new SortedTree(COMPARATOR);
        sortedTree.add(1, 2);
        assertFalse(sortedTree.contains(0));
    }

    @Test
    public void getNodeShouldReturnNodeWithKeyAndValueAddedToEmptyTree() {
        SortedTree<Integer, Integer> sortedTree = new SortedTree(COMPARATOR);
        sortedTree.add(1, 2);
        Node<Integer, Integer> node = sortedTree.getNode();
        assertEquals(1, node.getKey());
        assertEquals(2, node.getContent());
    }

    @Test
    public void getNodeWithParameterShouldReturnNodeWithKeyAndValueAddedToEmptyTreeForKey() {
        SortedTree<Integer, Integer> sortedTree = new SortedTree(COMPARATOR);
        sortedTree.add(1, 2);
        Node<Integer, Integer> node = sortedTree.getNode(1);
        assertEquals(1, node.getKey());
        assertEquals(2, node.getContent());
    }

    @Test
    public void getNodeWithParameterShouldReturnNullForKeyNotAddedToEmptyTree() {
        SortedTree<Integer, Integer> sortedTree = new SortedTree(COMPARATOR);
        sortedTree.add(1, 2);
        assertNull(sortedTree.getNode(0));
    }
}
