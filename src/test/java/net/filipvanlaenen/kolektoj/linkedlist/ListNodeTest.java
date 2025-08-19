package net.filipvanlaenen.kolektoj.linkedlist;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * Unit tests on the {@link net.filipvanlaenen.kolektoj.linkedlist.ListNode} class.
 */
public class ListNodeTest {
    /**
     * Verifies that <code>getElement</code> returns the element of the node.
     */
    @Test
    public void getElementShouldReturnElement() {
        ListNode<Integer> node1 = new ListNode<Integer>(1, null);
        assertEquals(1, node1.getElement());
    }

    /**
     * Verifies that <code>getNext</code> returns the next node of a node.
     */
    @Test
    public void getNextShouldReturnNextNode() {
        ListNode<Integer> node2 = new ListNode<Integer>(2, null);
        ListNode<Integer> node1 = new ListNode<Integer>(1, node2);
        assertEquals(node2, node1.getNext());
    }
}
