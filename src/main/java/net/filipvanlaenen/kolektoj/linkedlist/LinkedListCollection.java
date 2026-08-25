package net.filipvanlaenen.kolektoj.linkedlist;

import static net.filipvanlaenen.kolektoj.Collection.ElementCardinality.DISTINCT_ELEMENTS;
import static net.filipvanlaenen.kolektoj.Collection.ElementCardinality.DUPLICATE_ELEMENTS;

import java.util.Iterator;
import java.util.Objects;
import java.util.Spliterator;

import net.filipvanlaenen.kolektoj.Collection;
import net.filipvanlaenen.kolektoj.array.ArrayIterator;
import net.filipvanlaenen.kolektoj.array.ArraySpliterator;

/**
 * An linked list backed implementation of the {@link net.filipvanlaenen.kolektoj.Collection} interface.
 *
 * @param <E> The element type.
 */
public final class LinkedListCollection<E> implements Collection<E> {
    /**
     * A cached array with the elements.
     */
    private Object[] cachedArray;
    /**
     * The element cardinality.
     */
    private final ElementCardinality elementCardinality;
    /**
     * The head node of the linked list.
     */
    private ListNode<E> head;
    /**
     * The size of the collection.
     */
    private int size;

    /**
     * Constructs a linked list collection with the elements of the provided collection and its element cardinality.
     *
     * @param source The collection to create a new collection from.
     */
    public LinkedListCollection(final Collection<? extends E> source) {
        this.elementCardinality = source.getElementCardinality();
        for (final E element : source) {
            add(element);
        }
    }

    /**
     * Constructs a linked list collection with the given elements.
     *
     * @param elements The elements of the linked list collection.
     */
    public LinkedListCollection(final E... elements) {
        this(DUPLICATE_ELEMENTS, elements);
    }

    /**
     * Constructs a linked list collection with the elements of the provided collection and the provided element
     * cardinality.
     *
     * @param elementCardinality The element cardinality.
     * @param source             The collection to create a new collection from.
     */
    public LinkedListCollection(final ElementCardinality elementCardinality, final Collection<? extends E> source) {
        this.elementCardinality = elementCardinality;
        for (final E element : source) {
            add(element);
        }
    }

    /**
     * Constructs a linked list collection with the given elements and element cardinality.
     *
     * @param elementCardinality The element cardinality.
     * @param elements           The elements of the linked list collection.
     */
    public LinkedListCollection(final ElementCardinality elementCardinality, final E... elements) {
        this.elementCardinality = elementCardinality;
        for (final E element : elements) {
            add(element);
        }
    }

    private void add(final E element) {
        if (elementCardinality == DISTINCT_ELEMENTS && contains(element)) {
            return;
        }
        head = new ListNode<E>(element, head);
        size++;
    }

    @Override
    public boolean contains(final E element) {
        ListNode<E> current = head;
        while (current != null) {
            if (Objects.equals(current.getElement(), element)) {
                return true;
            }
            current = current.getNext();
        }
        return false;
    }

    @Override
    public boolean containsAll(final Collection<?> collection) {
        if (collection.size() > size) {
            return false;
        }
        boolean[] matches = new boolean[size];
        for (Object element : collection) {
            boolean found = false;
            ListNode<E> current = head;
            for (int i = 0; i < size; i++) {
                if (!matches[i] && Objects.equals(element, current.getElement())) {
                    matches[i] = true;
                    found = true;
                    break;
                }
                current = current.getNext();
            }
            if (!found) {
                return false;
            }
        }
        return true;
    }

    @Override
    public E get() throws IndexOutOfBoundsException {
        if (head == null) {
            throw new IndexOutOfBoundsException("Cannot return an element from an empty collection.");
        } else {
            return head.getElement();
        }
    }

    @Override
    public ElementCardinality getElementCardinality() {
        return elementCardinality;
    }

    @Override
    public Iterator<E> iterator() {
        return new ArrayIterator<E>(toArray());
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public Spliterator<E> spliterator() {
        return new ArraySpliterator<E>(toArray(), elementCardinality == DISTINCT_ELEMENTS ? Spliterator.DISTINCT : 0);
    }

    @Override
    public Object[] toArray() {
        if (cachedArray == null) {
            cachedArray = new Object[size];
            ListNode<E> current = head;
            for (int i = 0; i < size; i++) {
                cachedArray[i] = current.getElement();
                current = current.getNext();
            }
        }
        return cachedArray.clone();
    }
}
