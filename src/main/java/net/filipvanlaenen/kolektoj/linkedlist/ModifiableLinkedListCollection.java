package net.filipvanlaenen.kolektoj.linkedlist;

import static net.filipvanlaenen.kolektoj.Collection.ElementCardinality.DISTINCT_ELEMENTS;
import static net.filipvanlaenen.kolektoj.Collection.ElementCardinality.DUPLICATE_ELEMENTS;

import java.lang.reflect.Array;
import java.util.Iterator;
import java.util.Objects;
import java.util.Spliterator;

import net.filipvanlaenen.kolektoj.Collection;
import net.filipvanlaenen.kolektoj.ModifiableCollection;
import net.filipvanlaenen.kolektoj.array.ArrayIterator;
import net.filipvanlaenen.kolektoj.array.ArraySpliterator;

/**
 * An linked list backed implementation of the {@link net.filipvanlaenen.kolektoj.ModifiableCollection} interface.
 *
 * @param <E> The element type.
 */
public final class ModifiableLinkedListCollection<E> implements ModifiableCollection<E> {
    /**
     * A class implementing a node in a linked list.
     */
    private final class Node {
        /**
         * The element of the node.
         */
        private final E element;
        /**
         * The next node in the linked list.
         */
        private Node next;

        /**
         * Constructor taking the element and the next nodes as its parameters.
         *
         * @param element The element of this node.
         * @param next    The next node in the linked list.
         */
        private Node(final E element, final Node next) {
            this.element = element;
            this.next = next;
        }

        /**
         * Returns the element of this node.
         *
         * @return The element of this node.
         */
        private E getElement() {
            return element;
        }

        /**
         * Returns the next node in the linked list.
         *
         * @return The next node in the linked list.
         */
        private Node getNext() {
            return next;
        }

        /**
         * Sets the next node in the linked list.
         *
         * @param next The next node in the linked list.
         */
        private void setNext(final Node next) {
            this.next = next;
        }
    }

    /**
     * A cached array with the elements.
     */
    private E[] cachedArray;
    /**
     * A boolean flag indicating whether the cachedArray field is dirty.
     */
    private boolean cachedArrayDirty;
    /**
     * The element cardinality.
     */
    private final ElementCardinality elementCardinality;
    /**
     * The head node of the linked list.
     */
    private Node head;
    /**
     * The size of the collection.
     */
    private int size;

    /**
     * Constructs a modifiable linked list collection with the given elements.
     *
     * @param elements The elements of the modifiable linked list collection.
     */
    public ModifiableLinkedListCollection(final E... elements) {
        this(DUPLICATE_ELEMENTS, elements);
    }

    /**
     * Constructs a modifiable linked list collection with the given elements and element cardinality.
     *
     * @param elementCardinality The element cardinality.
     * @param elements           The elements of the modifiable linked list collection.
     */
    public ModifiableLinkedListCollection(final ElementCardinality elementCardinality, final E... elements) {
        this.elementCardinality = elementCardinality;
        for (final E element : elements) {
            add(element);
        }
        cachedArray = elements.clone();
        cachedArrayDirty = elements.length != size;
    }

    @Override
    public boolean add(final E element) {
        if (elementCardinality == DISTINCT_ELEMENTS && contains(element)) {
            return false;
        }
        head = new Node(element, head);
        size++;
        cachedArrayDirty = true;
        return true;
    }

    @Override
    public boolean addAll(final Collection<? extends E> collection) {
        boolean result = false;
        for (E element : collection) {
            result |= add(element);
        }
        cachedArrayDirty |= result;
        return result;
    }

    @Override
    public void clear() {
        head = null;
        size = 0;
        cachedArrayDirty = cachedArray.length != 0;
    }

    @Override
    public boolean contains(final E element) {
        Node current = head;
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
            Node current = head;
            for (int i = 0; i < size; i++) {
                if (!matches[i] && Objects.equals(element, current.getElement())) {
                    matches[i] = true;
                    break;
                }
                current = current.getNext();
            }
        }
        for (boolean match : matches) {
            if (!match) {
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
    public boolean remove(final E element) {
        if (head == null) {
            return false;
        }
        if (Objects.equals(head.getElement(), element)) {
            head = head.getNext();
            size--;
            cachedArrayDirty = true;
            return true;
        }
        Node current = head;
        Node next = current.getNext();
        while (next != null) {
            if (Objects.equals(next.getElement(), element)) {
                current.setNext(next.getNext());
                size--;
                cachedArrayDirty = true;
                return true;
            }
            current = next;
            next = current.getNext();
        }
        return false;
    }

    @Override
    public boolean removeAll(final Collection<? extends E> collection) {
        boolean result = false;
        for (E element : collection) {
            result |= remove(element);
        }
        return result;
    }

    @Override
    public boolean retainAll(final Collection<? extends E> collection) {
        if (head == null) {
            return false;
        }
        boolean[] retain = new boolean[size];
        for (E element : collection) {
            Node current = head;
            for (int i = 0; i < size; i++) {
                if (!retain[i] && Objects.equals(element, current.getElement())) {
                    retain[i] = true;
                    break;
                }
                current = current.getNext();
            }
        }
        boolean result = false;
        int i = 0;
        while (i < retain.length && !retain[i]) {
            head = head.getNext();
            size--;
            cachedArrayDirty = true;
            result = true;
            i++;
        }
        if (head == null) {
            return true;
        }
        Node current = head;
        Node next = current.getNext();
        while (i < retain.length) {
            if (retain[i]) {
                current = next;
            } else {
                current.setNext(next.getNext());
                size--;
                cachedArrayDirty = true;
                result = true;
            }
            next = current == null ? null : current.getNext();
            i++;
        }
        return result;
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
    public E[] toArray() {
        if (cachedArrayDirty) {
            Class<E[]> clazz = (Class<E[]>) cachedArray.getClass();
            cachedArray = (E[]) Array.newInstance(clazz.getComponentType(), size);
            Node current = head;
            for (int i = 0; i < size; i++) {
                cachedArray[i] = current.getElement();
                current = current.getNext();
            }
            cachedArrayDirty = false;
        }
        return cachedArray.clone();
    }
}
