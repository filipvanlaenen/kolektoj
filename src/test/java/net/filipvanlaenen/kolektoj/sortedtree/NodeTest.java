package net.filipvanlaenen.kolektoj.sortedtree;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

/**
 * Unit tests on the {@link net.filipvanlaenen.kolektoj.sortedtree.Node} class.
 */
public class NodeTest {
    /**
     * The magic number three.
     */
    private static final int THREE = 3;
    /**
     * A node to run unit tests on.
     */
    private static final Node<Integer, Integer> NODE12 = createTestNode12();

    /**
     * Factory method to create a test node.
     *
     * @return A node with key 1 and content 2.
     */
    private static Node<Integer, Integer> createTestNode12() {
        return new Node<Integer, Integer>(1, 2);
    }

    /**
     * Verifies that the key is wired correctly to the constructor.
     */
    @Test
    public void getKeyShouldBeWiredCorrectlyToConstructor() {
        assertEquals(1, NODE12.getKey());
    }

    /**
     * Verifies that the content is wired correctly to the constructor.
     */
    @Test
    public void getContentShouldBeWiredCorrectlyToConstructor() {
        assertEquals(2, NODE12.getContent());
    }

    /**
     * Verifies that the getter for the height is wired correctly to the setter.
     */
    @Test
    public void getHeightShouldBeWiredCorrectyToGetHeight() {
        Node<Integer, Integer> node = createTestNode12();
        node.setHeight(THREE);
        assertEquals(THREE, node.getHeight());
    }

    /**
     * Verifies that by default, the left child is <code>null</code>.
     */
    @Test
    public void getLeftChildShouldReturnNullByDefault() {
        assertNull(NODE12.getLeftChild());
    }

    /**
     * Verifies that the getter for the left child is wired correctly to the setter.
     */
    @Test
    public void getLeftChildShouldBeWiredCorrectlyToSetLeftChild() {
        Node<Integer, Integer> parent = createTestNode12();
        Node<Integer, Integer> child = createTestNode12();
        parent.setLeftChild(child);
        assertEquals(child, parent.getLeftChild());
    }

    /**
     * Verifies that by default, the right child is <code>null</code>.
     */
    @Test
    public void getRightChildShouldReturnNullByDefault() {
        assertNull(NODE12.getRightChild());
    }

    /**
     * Verifies that the getter for the right child is wired correctly to the setter.
     */
    @Test
    public void getRightChildShouldBeWiredCorrectlyToSetRightChild() {
        Node<Integer, Integer> parent = createTestNode12();
        Node<Integer, Integer> child = createTestNode12();
        parent.setRightChild(child);
        assertEquals(child, parent.getRightChild());
    }

    /**
     * Verifies that by default, the leftmost child is the node itself.
     */
    @Test
    public void getLeftmostChildShouldReturnNullByDefault() {
        assertEquals(NODE12, NODE12.getLeftmostChild());
    }

    /**
     * Verifies that the getter for the leftmost child navigates to the leftmost grandchild.
     */
    @Test
    public void getLeftmostChildShouldReturnLeftmostGrandchild() {
        Node<Integer, Integer> parent = createTestNode12();
        Node<Integer, Integer> child = createTestNode12();
        Node<Integer, Integer> grandchild = createTestNode12();
        parent.setLeftChild(child);
        child.setLeftChild(grandchild);
        assertEquals(grandchild, parent.getLeftmostChild());
    }

    /**
     * Verifies that by default, the size is one.
     */
    @Test
    public void getSizeShouldReturnOneByDefault() {
        assertEquals(1, NODE12.getSize());
    }

    /**
     * Verifies that the size is three for a node with both a left and a right child.
     */
    @Test
    public void getSizeShouldReturnThreeForNodeWithLeftAndRightChild() {
        Node<Integer, Integer> parent = createTestNode12();
        Node<Integer, Integer> leftChild = createTestNode12();
        Node<Integer, Integer> rightChild = createTestNode12();
        parent.setLeftChild(leftChild);
        parent.setRightChild(rightChild);
        assertEquals(THREE, parent.getSize());
    }
}
