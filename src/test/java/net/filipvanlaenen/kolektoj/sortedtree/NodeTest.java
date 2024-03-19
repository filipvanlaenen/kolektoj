package net.filipvanlaenen.kolektoj.sortedtree;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class NodeTest {
    @Test
    public void getKeyShouldBeWiredCorrectlyToConstructor() {
        Node<Integer, Integer> node = new Node<Integer, Integer>(1, 2);
        assertEquals(1, node.getKey());
    }

    @Test
    public void getContentShouldBeWiredCorrectlyToConstructor() {
        Node<Integer, Integer> node = new Node<Integer, Integer>(1, 2);
        assertEquals(2, node.getContent());
    }
}
