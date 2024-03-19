package net.filipvanlaenen.kolektoj.sortedtree;

import java.util.Comparator;

/**
 * A class implementing an AVL tree.
 *
 * @param <K> The sorting key type.
 * @param <C> The content type.
 */
class SortedTree<K, C> {
    private final Comparator<K> comparator;
    private Node<K, C> root;

    SortedTree(Comparator<K> comparator) {
        this.comparator = comparator;
    }

    void add(final K key, final C content) {
        root = new Node<K, C>(key, content);
    }

    boolean contains(final K key) {
        return root != null && comparator.compare(key, root.getKey()) == 0;
    }

    Node<K, C> getNode() {
        return root;
    }

    Node<K, C> getNode(final K key) {
        if (root == null) {
            return null;
        }
        if (comparator.compare(key, root.getKey()) == 0) {
            return root;
        } else {
            return null;
        }
    }
}
