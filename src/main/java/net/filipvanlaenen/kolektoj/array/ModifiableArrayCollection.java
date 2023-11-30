package net.filipvanlaenen.kolektoj.array;

import static net.filipvanlaenen.kolektoj.Collection.ElementCardinality.DISTINCT_ELEMENTS;
import static net.filipvanlaenen.kolektoj.Collection.ElementCardinality.DUPLICATE_ELEMENTS;

import java.lang.reflect.Array;
import java.util.Iterator;
import java.util.Objects;
import java.util.Spliterator;
import java.util.function.Predicate;

import net.filipvanlaenen.kolektoj.Collection;
import net.filipvanlaenen.kolektoj.ModifiableCollection;

/**
 * An array backed implementation of the {@link net.filipvanlaenen.kolektoj.ModifiableCollection} interface.
 *
 * @param <E> The element type.
 */
public final class ModifiableArrayCollection<E> implements ModifiableCollection<E> {
    /**
     * The stride for resizing the elements array.
     */
    private static final int STRIDE = 5;
    /**
     * The element cardinality.
     */
    private final ElementCardinality elementCardinality;
    /**
     * An array with the elements.
     */
    private E[] elements;
    /**
     * The size of the collection.
     */
    private int size;

    /**
     * Constructs a modifiable array collection from another collection, with the same elements and the same element
     * cardinality.
     *
     * @param source The collection to create a new collection from.
     */
    public ModifiableArrayCollection(final Collection<E> source) {
        this(source.getElementCardinality(), source.toArray());
    }

    /**
     * Constructs a modifiable array collection with the given elements.
     *
     * @param elements The elements of the modifiable array collection.
     */
    public ModifiableArrayCollection(final E... elements) {
        this(DUPLICATE_ELEMENTS, elements);
    }

    /**
     * Constructs a modifiable array collection with the given elements and element cardinality.
     *
     * @param elementCardinality The element cardinality.
     * @param elements           The elements of the modifiable array collection.
     */
    public ModifiableArrayCollection(final ElementCardinality elementCardinality, final E... elements) {
        this.elementCardinality = elementCardinality;
        if (elementCardinality == DISTINCT_ELEMENTS) {
            this.elements = ArrayUtilities.cloneDistinctElements(elements);
        } else {
            this.elements = elements.clone();
        }
        size = this.elements.length;
    }

    @Override
    public boolean add(final E element) {
        if (elementCardinality == DISTINCT_ELEMENTS && contains(element)) {
            return false;
        }
        if (size == elements.length) {
            resizeTo(elements.length + STRIDE);
        }
        elements[size++] = element;
        return true;
    }

    @Override
    public boolean addAll(final Collection<? extends E> collection) {
        if (collection.isEmpty()) {
            return false;
        }
        int originalSize = size;
        int numberOfNewElements = collection.size();
        // EQMU: Changing the conditional boundary below produces an equivalent mutant.
        if (size + numberOfNewElements > elements.length) {
            resizeTo(size + numberOfNewElements + STRIDE);
        }
        if (elementCardinality == DISTINCT_ELEMENTS) {
            for (E element : collection) {
                if (!contains(element)) {
                    elements[size++] = element;
                }
            }
        } else {
            System.arraycopy(collection.toArray(), 0, elements, size, numberOfNewElements);
            size += numberOfNewElements;
        }
        return size != originalSize;
    }

    @Override
    public void clear() {
        size = 0;
        // EQMU: Changing the conditional boundary below produces an equivalent mutant.
        // EQMU: Negating the conditional below produces an equivalent mutant.
        if (elements.length > STRIDE) {
            // EQMU: Removing the call to resizeTo below produces an equivalent mutant.
            resizeTo(STRIDE);
        }
    }

    @Override
    public boolean contains(final E element) {
        return ArrayUtilities.contains(elements, size, element);
    }

    @Override
    public boolean containsAll(final Collection<?> collection) {
        return ArrayUtilities.containsAll(elements, size, collection);
    }

    /**
     * Creates a new element type array with a given length.
     *
     * @param length The length of the array.
     * @return An array of the given length with the element type.
     */
    private E[] createNewArray(final int length) {
        Class<E[]> clazz = (Class<E[]>) elements.getClass();
        return (E[]) Array.newInstance(clazz.getComponentType(), length);
    }

    @Override
    public E get() throws IndexOutOfBoundsException {
        if (elements.length == 0) {
            throw new IndexOutOfBoundsException("Cannot return an element from an empty collection.");
        } else {
            return elements[0];
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
        for (int i = 0; i < size; i++) {
            if (elements[i].equals(element)) {
                elements[i] = elements[size - 1];
                size--;
                // EQMU: Changing the conditional boundary below produces an equivalent mutant.
                // EQMU: Replacing integer subtraction with addition below produces an equivalent mutant.
                // EQMU: Negating the conditional below produces an equivalent mutant.
                if (size < elements.length - STRIDE) {
                    // EQMU: Removing the call to resizeTo below produces an equivalent mutant.
                    resizeTo(size);
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean removeAll(final Collection<? extends E> collection) {
        boolean result = false;
        for (E element : collection) {
            for (int i = 0; i < size; i++) {
                if (Objects.equals(element, elements[i])) {
                    elements[i] = elements[size - 1];
                    size--;
                    result = true;
                    break;
                }
            }
        }
        // EQMU: Changing the conditional boundary below produces an equivalent mutant.
        // EQMU: Replacing integer subtraction with addition below produces an equivalent mutant.
        // EQMU: Negating the conditional below produces an equivalent mutant.
        if (size < elements.length - STRIDE) {
            // EQMU: Removing the call to resizeTo below produces an equivalent mutant.
            resizeTo(size);
        }
        return result;
    }

    @Override
    public boolean removeIf(Predicate<? super E> predicate) {
        boolean[] retain = new boolean[size];
        for (int i = 0; i < size; i++) {
            retain[i] = !predicate.test(elements[i]);
        }
        return retainAndResize(retain);
    }

    /**
     * Resizes the array to the new length. It is assumed that the new length is not less than the current size.
     *
     * @param newLength The new length for the array.
     */
    private void resizeTo(final int newLength) {
        E[] newElements = createNewArray(newLength);
        System.arraycopy(elements, 0, newElements, 0, size);
        elements = newElements;
    }

    @Override
    public boolean retainAll(final Collection<? extends E> collection) {
        boolean[] retain = new boolean[size];
        for (E element : collection) {
            for (int i = 0; i < size; i++) {
                if (!retain[i] && Objects.equals(element, elements[i])) {
                    retain[i] = true;
                    break;
                }
            }
        }
        return retainAndResize(retain);
    }

    /**
     * Retains the elements according to a retention array and resizes if necessary.
     *
     * @param retain The retention array.
     * @return True if at least one element was removed.
     */
    private boolean retainAndResize(final boolean[] retain) {
        int i = 0;
        boolean result = false;
        while (i < size) {
            if (retain[i]) {
                i++;
            } else {
                elements[i] = elements[size - 1];
                retain[i] = retain[size - 1];
                size--;
                result = true;
            }
        }
        // EQMU: Changing the conditional boundary below produces an equivalent mutant.
        // EQMU: Replacing integer subtraction with addition below produces an equivalent mutant.
        // EQMU: Negating the conditional below produces an equivalent mutant.
        if (size < elements.length - STRIDE) {
            // EQMU: Removing the call to resizeTo below produces an equivalent mutant.
            resizeTo(size);
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
        E[] result = createNewArray(size);
        System.arraycopy(elements, 0, result, 0, size);
        return result;
    }
}
