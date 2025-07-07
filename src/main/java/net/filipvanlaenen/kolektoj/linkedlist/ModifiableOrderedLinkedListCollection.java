package net.filipvanlaenen.kolektoj.linkedlist;

import static net.filipvanlaenen.kolektoj.Collection.ElementCardinality.DISTINCT_ELEMENTS;
import static net.filipvanlaenen.kolektoj.Collection.ElementCardinality.DUPLICATE_ELEMENTS;

import java.util.Iterator;
import java.util.Objects;
import java.util.Spliterator;
import java.util.function.Predicate;

import net.filipvanlaenen.kolektoj.Collection;
import net.filipvanlaenen.kolektoj.ModifiableOrderedCollection;
import net.filipvanlaenen.kolektoj.OrderedCollection;
import net.filipvanlaenen.kolektoj.array.ArrayIterator;
import net.filipvanlaenen.kolektoj.array.ArraySpliterator;

/**
 * An linked list backed implementation of the {@link net.filipvanlaenen.kolektoj.ModifiableOrderedCollection}
 * interface.
 *
 * @param <E> The element type.
 */
public final class ModifiableOrderedLinkedListCollection<E> implements ModifiableOrderedCollection<E> {
    /**
     * A cached array with the elements.
     */
    private Object[] cachedArray;
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
    private Node<E> head;
    /**
     * The size of the collection.
     */
    private int size;

    /**
     * Constructs a modifiable linked list collection with the given elements.
     *
     * @param elements The elements of the modifiable linked list collection.
     */
    public ModifiableOrderedLinkedListCollection(final E... elements) {
        this(DUPLICATE_ELEMENTS, elements);
    }

    /**
     * Constructs a modifiable linked list collection with the given elements and element cardinality.
     *
     * @param elementCardinality The element cardinality.
     * @param elements           The elements of the modifiable linked list collection.
     */
    public ModifiableOrderedLinkedListCollection(final ElementCardinality elementCardinality, final E... elements) {
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
        head = new Node<E>(element, head);
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
        return result;
    }

    @Override
    public boolean addAllAt(final int index, final OrderedCollection<? extends E> collection) {
        if (index > size) {
            throw new IndexOutOfBoundsException(
                    "Cannot add the elements of another collection at a position beyond the size of the collection.");
        }
        if (collection.isEmpty()) {
            return false;
        }
        Node<E> newListHead = null;
        Node<E> newListTail = null;
        for (E element : collection) {
            if (elementCardinality != DISTINCT_ELEMENTS || !contains(element)) {
                if (newListHead == null) {
                    newListHead = new Node<E>(element, null);
                    newListTail = newListHead;
                } else {
                    Node<E> newNode = new Node<E>(element, null);
                    newListTail.setNext(newNode);
                    newListTail = newNode;
                }
                size++;
            }
        }
        if (newListHead == null) {
            return false;
        }
        if (index == 0) {
            newListTail.setNext(head);
            head = newListHead;
        } else {
            Node<E> current = head;
            for (int i = 0; i < index - 1; i++) {
                current = current.getNext();
            }
            newListTail.setNext(current.getNext());
            current.setNext(newListHead);
        }
        cachedArrayDirty = true;
        return true;
    }

    @Override
    public boolean addAt(final int index, final E element) throws IndexOutOfBoundsException {
        if (index > size) {
            throw new IndexOutOfBoundsException(
                    "Cannot add an element at a position beyond the size of the collection.");
        } else {
            if (elementCardinality == DISTINCT_ELEMENTS && contains(element)) {
                return false;
            }
            if (index == 0) {
                head = new Node<E>(element, head);
            } else {
                Node<E> current = head;
                for (int i = 0; i < index - 1; i++) {
                    current = current.getNext();
                }
                Node<E> newNode = new Node<E>(element, current.getNext());
                current.setNext(newNode);
            }
            size++;
            cachedArrayDirty = true;
            return true;
        }
    }

    @Override
    public void clear() {
        head = null;
        size = 0;
        cachedArrayDirty = cachedArray.length != 0;
    }

    @Override
    public boolean contains(final E element) {
        Node<E> current = head;
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
            Node<E> current = head;
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
    public E getAt(final int index) throws IndexOutOfBoundsException {
        if (index >= size) {
            throw new IndexOutOfBoundsException(
                    "Cannot return an element at a position beyond the size of the collection.");
        } else {
            if (!cachedArrayDirty) {
                return (E) cachedArray[index];
            } else {
                Node<E> current = head;
                for (int i = 0; i < index; i++) {
                    current = current.getNext();
                }
                return current.getElement();
            }
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
        Node<E> current = head;
        Node<E> next = current.getNext();
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
    public E removeAt(final int index) throws IndexOutOfBoundsException {
        if (index >= size) {
            throw new IndexOutOfBoundsException(
                    "Cannot remove an element at a position beyond the size of the collection.");
        } else {
            E element = null;
            if (index == 0) {
                element = head.getElement();
                head = head.getNext();
            } else {
                Node<E> current = head;
                for (int i = 0; i < index - 1; i++) {
                    current = current.getNext();
                }
                Node<E> next = current.getNext();
                element = next.getElement();
                current.setNext(next.getNext());
            }
            size--;
            cachedArrayDirty = true;
            return element;
        }
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
    public boolean removeIf(final Predicate<? super E> predicate) {
        if (head == null) {
            return false;
        }
        boolean[] retain = new boolean[size];
        Node<E> current = head;
        for (int i = 0; i < size; i++) {
            retain[i] = !predicate.test(current.getElement());
            current = current.getNext();
        }
        return retainAndMarkCachedArrayDirty(retain);
    }

    @Override
    public boolean retainAll(final Collection<? extends E> collection) {
        if (head == null) {
            return false;
        }
        boolean[] retain = new boolean[size];
        for (E element : collection) {
            Node<E> current = head;
            for (int i = 0; i < size; i++) {
                if (!retain[i] && Objects.equals(element, current.getElement())) {
                    retain[i] = true;
                    break;
                }
                current = current.getNext();
            }
        }
        return retainAndMarkCachedArrayDirty(retain);
    }

    /**
     * Retains the elements according to a retention array and marks the cached array dirty flag if necessary.
     *
     * @param retain The retention array.
     * @return True if at least one element was removed.
     */
    private boolean retainAndMarkCachedArrayDirty(final boolean[] retain) {
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
        Node<E> current = head;
        Node<E> next = current.getNext();
        i++;
        while (i < retain.length) {
            if (retain[i]) {
                current = next;
            } else {
                current.setNext(next.getNext());
                size--;
                cachedArrayDirty = true;
                result = true;
            }
            next = current.getNext();
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
        int characteristics =
                Spliterator.ORDERED | (elementCardinality == DISTINCT_ELEMENTS ? Spliterator.DISTINCT : 0);
        return new ArraySpliterator<E>(toArray(), characteristics);
    }

    @Override
    public Object[] toArray() {
        if (cachedArrayDirty) {
            cachedArray = new Object[size];
            Node<E> current = head;
            for (int i = 0; i < size; i++) {
                cachedArray[i] = current.getElement();
                current = current.getNext();
            }
            cachedArrayDirty = false;
        }
        return cachedArray.clone();
    }
}
