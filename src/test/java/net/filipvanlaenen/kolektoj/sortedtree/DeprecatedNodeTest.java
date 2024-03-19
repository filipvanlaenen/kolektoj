package net.filipvanlaenen.kolektoj.sortedtree;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * Unit tests on the {@link net.filipvanlaenen.kolektoj.sortedtree.DeprecatedNode} class.
 */
public class DeprecatedNodeTest {
    /**
     * The magic number three.
     */
    private static final int THREE = 3;
    /**
     * A single node with the element 1.
     */
    private static final DeprecatedNode<Integer> NODE1 = new DeprecatedElementNode<Integer>(1);

    /**
     * Verifies that the getter method <code>getElement</code> is wired correctly to the constructor.
     */
    @Test
    public void getElementShouldBeWiredCorrectlyToTheConstructor() {
        assertEquals(1, NODE1.getSortingKey());
    }

    /**
     * Verifies that the setter method <code>setElement</code> is wired correctly to the getter method
     * <code>getElement</code>.
     */
    @Test
    public void setElementShouldBeWiredCorrectlyToGetElement() {
        DeprecatedNode<Integer> node = new DeprecatedElementNode<Integer>(1);
        node.setSortingKey(2);
        assertEquals(2, node.getSortingKey());
    }

    /**
     * Verifies that the method <code>setLeftChild</code> is wired correctly to the getter method
     * <code>getLeftChild</code>.
     */
    @Test
    public void setLeftChildShouldBeWiredCorrectlyToGetLeftChild() {
        DeprecatedNode<Integer> node = new DeprecatedElementNode<Integer>(2);
        node.setLeftChild(NODE1);
        assertEquals(NODE1, node.getLeftChild());
    }

    /**
     * Verifies that the method <code>setRightChild</code> is wired correctly to the getter method
     * <code>getRightChild</code>.
     */
    @Test
    public void setRightChildShouldBeWiredCorrectlyToGetRightChild() {
        DeprecatedNode<Integer> node = new DeprecatedElementNode<Integer>(2);
        node.setRightChild(NODE1);
        assertEquals(NODE1, node.getRightChild());
    }

    /**
     * Verifies that the size of a single node is one.
     */
    @Test
    public void getSizeShouldReturnOneForASingleNode() {
        assertEquals(1, NODE1.getSize());
    }

    /**
     * Verifies that the size of a node with a left and a right child is calculated as the sum of the sizes plus one.
     */
    @Test
    public void getSizeShouldReturnSumOfSizesPlusOneForNodeWithLeftAndRightChild() {
        DeprecatedNode<Integer> node = new DeprecatedElementNode<Integer>(1);
        node.setLeftChild(new DeprecatedElementNode<Integer>(0));
        node.setRightChild(new DeprecatedElementNode<Integer>(2));
        assertEquals(THREE, node.getSize());
    }

    /**
     * Verifies that after updating the height of a single node, <code>calculateBalanceFactor</code> returns zero.
     */
    @Test
    public void calculateBalanceFactorShouldReturnZeroForASingleNodeAfterUpdatingTheHeight() {
        DeprecatedNode<Integer> node = new DeprecatedElementNode<Integer>(1);
        node.updateHeight();
        assertEquals(0, node.calculateBalanceFactor());
    }

    /**
     * Verifies that after updating the height of a node with a left child, <code>calculateBalanceFactor</code> returns
     * minus one.
     */
    @Test
    public void calculateBalanceFactorShouldReturnMinusOneForANodeWithALeftChildAfterUpdatingTheHeights() {
        DeprecatedNode<Integer> node = new DeprecatedElementNode<Integer>(1);
        DeprecatedNode<Integer> leftChild = new DeprecatedElementNode<Integer>(0);
        leftChild.updateHeight();
        node.setLeftChild(leftChild);
        node.updateHeight();
        assertEquals(-1, node.calculateBalanceFactor());
    }

    /**
     * Verifies that after updating the height of a node with a right child, <code>calculateBalanceFactor</code> returns
     * one.
     */
    @Test
    public void calculateBalanceFactorShouldReturnOneForANodeWithARightChildAfterUpdatingTheHeights() {
        DeprecatedNode<Integer> node = new DeprecatedElementNode<Integer>(1);
        DeprecatedNode<Integer> rightChild = new DeprecatedElementNode<Integer>(0);
        rightChild.updateHeight();
        node.setRightChild(rightChild);
        node.updateHeight();
        assertEquals(1, node.calculateBalanceFactor());
    }

    /**
     * Verifies that after updating the height of a node with two right children, <code>calculateBalanceFactor</code>
     * returns two.
     */
    @Test
    public void calculateBalanceFactorShouldReturnTwoForANodeWithTwoRightChildrenAfterUpdatingTheHeights() {
        DeprecatedNode<Integer> node = new DeprecatedElementNode<Integer>(2);
        DeprecatedNode<Integer> rightChild = new DeprecatedElementNode<Integer>(1);
        DeprecatedNode<Integer> rightGrandchild = new DeprecatedElementNode<Integer>(0);
        rightGrandchild.updateHeight();
        rightChild.setRightChild(rightGrandchild);
        rightChild.updateHeight();
        node.setRightChild(rightChild);
        node.updateHeight();
        assertEquals(2, node.calculateBalanceFactor());
    }

    /**
     * Verifies that for a single node, the leftmost child is the node itself.
     */
    @Test
    public void getLeftmostChildShouldReturnNodeItselfForSingleNode() {
        assertEquals(NODE1, NODE1.getLeftmostChild());
    }

    /**
     * Verifies that for a node with two left children, the leftmost child is the left grand-child.
     */
    @Test
    public void getLeftmostChildShouldReturnLeftGrandchildForNodeWithTwoLeftChildren() {
        DeprecatedNode<Integer> node = new DeprecatedElementNode<Integer>(2);
        DeprecatedNode<Integer> childNode = new DeprecatedElementNode<Integer>(1);
        node.setLeftChild(childNode);
        DeprecatedNode<Integer> grandchildNode = new DeprecatedElementNode<Integer>(0);
        childNode.setLeftChild(grandchildNode);
        assertEquals(grandchildNode, node.getLeftmostChild());
    }
}