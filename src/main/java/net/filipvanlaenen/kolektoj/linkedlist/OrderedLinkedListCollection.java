package net.filipvanlaenen.kolektoj.linkedlist;

import static net.filipvanlaenen.kolektoj.Collection.ElementCardinality.DISTINCT_ELEMENTS;
import static net.filipvanlaenen.kolektoj.Collection.ElementCardinality.DUPLICATE_ELEMENTS;

import java.util.Iterator;
import java.util.Objects;
import java.util.Spliterator;

import net.filipvanlaenen.kolektoj.Collection;
import net.filipvanlaenen.kolektoj.OrderedCollection;
import net.filipvanlaenen.kolektoj.array.ArrayIterator;
import net.filipvanlaenen.kolektoj.array.ArraySpliterator;

/**
 * An linked list backed implementation of the {@link net.filipvanlaenen.kolektoj.OrderedCollection} interface.
 *
 * @param <E> The element type.
 */
public final class OrderedLinkedListCollection<E> implements OrderedCollection<E> {
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
     * The tail node of the linked list.
     */
    private ListNode<E> tail;

    /**
     * Constructs a modifiable ordered linked list collection with the given elements.
     *
     * @param elements The elements of the modifiable ordered linked list collection.
     */
    public OrderedLinkedListCollection(final E... elements) {
        this(DUPLICATE_ELEMENTS, elements);
    }

    /**
     * Constructs a modifiable ordered linked list collection with the given elements and element cardinality.
     *
     * @param elementCardinality The element cardinality.
     * @param elements           The elements of the modifiable ordered linked list collection.
     */
    public OrderedLinkedListCollection(final ElementCardinality elementCardinality, final E... elements) {
        this.elementCardinality = elementCardinality;
        for (final E element : elements) {
            add(element);
        }
    }

    /**
     * Constructs a modifiable ordered linked list collection with the elements of the provided collection and the
     * provided element cardinality.
     *
     * @param elementCardinality The element cardinality.
     * @param source             The ordered collection to create a new collection from.
     */
    public OrderedLinkedListCollection(final ElementCardinality elementCardinality,
            final OrderedCollection<? extends E> source) {
        this.elementCardinality = elementCardinality;
        for (final E element : source) {
            add(element);
        }
    }

    /**
     * Constructs a modifiable ordered linked list collection with the given elements.
     *
     * @param source The ordered collection to create a new collection from.
     */
    public OrderedLinkedListCollection(final OrderedCollection<? extends E> source) {
        this(source.getElementCardinality(), source);
    }

    /**
     * Adds an element to the end of the linked list, unless only distinct elements are allowed and the element is
     * already present.
     *
     * @param element The element to be added.
     */
    private void add(final E element) {
        if (elementCardinality == DISTINCT_ELEMENTS && contains(element)) {
            return;
        }
        if (head == null) {
            head = new ListNode<E>(element, head);
            tail = head;
        } else {
            ListNode<E> newNode = new ListNode<E>(element, null);
            tail.setNext(newNode);
            tail = newNode;
        }
        size++;
        cachedArray = null;
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
    public int firstIndexOf(final E element) {
        ListNode<E> current = head;
        int i = 0;
        while (current != null) {
            if (Objects.equals(current.getElement(), element)) {
                return i;
            }
            current = current.getNext();
            i++;
        }
        return -1;
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
            if (cachedArray != null) {
                return (E) cachedArray[index];
            } else {
                if (index == 0) {
                    return head.getElement();
                } else if (index == size - 1) {
                    // EQMU: Replacing integer subtraction with addition above produces an equivalent mutant.
                    return tail.getElement();
                } else {
                    ListNode<E> current = head;
                    for (int i = 0; i < index; i++) {
                        current = current.getNext();
                    }
                    return current.getElement();
                }
            }
        }
    }

    @Override
    public ElementCardinality getElementCardinality() {
        return elementCardinality;
    }

    @Override
    public int indexOf(final E element) {
        return firstIndexOf(element);
    }

    @Override
    public Iterator<E> iterator() {
        return new ArrayIterator<E>(toArray());
    }

    @Override
    public int lastIndexOf(final E element) {
        ListNode<E> current = head;
        int i = 0;
        int result = -1;
        while (current != null) {
            if (Objects.equals(current.getElement(), element)) {
                result = i;
            }
            current = current.getNext();
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
