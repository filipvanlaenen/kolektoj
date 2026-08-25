package net.filipvanlaenen.kolektoj.hash;

import static net.filipvanlaenen.kolektoj.Collection.ElementCardinality.DISTINCT_ELEMENTS;
import static net.filipvanlaenen.kolektoj.Collection.ElementCardinality.DUPLICATE_ELEMENTS;

import java.util.Iterator;
import java.util.Objects;
import java.util.Spliterator;
import java.util.function.Predicate;

import net.filipvanlaenen.kolektoj.Collection;
import net.filipvanlaenen.kolektoj.Map.Entry;
import net.filipvanlaenen.kolektoj.ModifiableCollection;
import net.filipvanlaenen.kolektoj.array.ArrayIterator;
import net.filipvanlaenen.kolektoj.array.ArraySpliterator;
import net.filipvanlaenen.kolektoj.array.ArrayUtilities;

/**
 * A hash backed implementation of the {@link net.filipvanlaenen.kolektoj.ModifiableCollection} interface.
 *
 * @param <E> The element type.
 */
public final class ModifiableHashCollection<E> implements ModifiableCollection<E> {
    /**
     * The ratio by which the number of entries should be multiplied to construct the hashed array.
     */
    private static final int HASHING_RATIO = 3;
    /**
     * The minimal ratio between the size of the map and the size of the hashed array.
     */
    private static final int MINIMAL_HASHING_RATIO = 2;
    /**
     * The maximal ratio between the size of the map and the size of the hashed array.
     */
    private static final int MAXIMAL_HASHING_RATIO = 4;
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
    private Object[] elements;
    /**
     * A hashed array with the elements.
     */
    private Object[] hashedElements;
    /**
     * The size of the hashed array with the elements.
     */
    private int hashedElementsSize;
    /**
     * The size of the collection.
     */
    private int size;

    /**
     * Constructs a modifiable hash collection from another collection, with the same elements and the same element
     * cardinality.
     *
     * @param source The collection to create a new collection from.
     */
    public ModifiableHashCollection(final Collection<? extends E> source) throws IllegalArgumentException {
        this.elementCardinality = source.getElementCardinality();
        this.elements = source.toArray();
        this.size = this.elements.length;
        this.hashedElementsSize = calculateHashedElementsSize(this.elements);
        this.hashedElements = HashUtilities.createHashedMapFromElements(this.elements, this.hashedElementsSize);
    }

    /**
     * Constructs a modifiable hash collection with the given elements. The element cardinality is defaulted to
     * <code>DUPLICATE_ELEMENTS</code>.
     *
     * @param elements The elements of the collection.
     */
    public ModifiableHashCollection(final E... elements) throws IllegalArgumentException {
        this.elementCardinality = DUPLICATE_ELEMENTS;
        this.elements = elements.clone();
        this.size = this.elements.length;
        this.hashedElementsSize = calculateHashedElementsSize(this.elements);
        this.hashedElements = HashUtilities.createHashedMapFromElements(this.elements, this.hashedElementsSize);
    }

    /**
     * Constructs a modifiable hash collection with the provided element cardinality and the elements of the provided
     * collection.
     *
     * @param elementCardinality The element cardinality.
     * @param source             The collection to create a new collection from.
     */
    public ModifiableHashCollection(final ElementCardinality elementCardinality, final Collection<? extends E> source)
            throws IllegalArgumentException {
        this.elementCardinality = elementCardinality;
        if (elementCardinality == DISTINCT_ELEMENTS) {
            this.elements = ArrayUtilities.cloneDistinctElements(source.toArray());
        } else {
            this.elements = source.toArray();
        }
        this.size = this.elements.length;
        this.hashedElementsSize = calculateHashedElementsSize(this.elements);
        this.hashedElements = HashUtilities.createHashedMapFromElements(this.elements, this.hashedElementsSize);
    }

    /**
     * Constructs a modifiable hash collection with the given elements and element cardinality.
     *
     * @param elementCardinality The element cardinality.
     * @param elements           The elements of the collection.
     */
    public ModifiableHashCollection(final ElementCardinality elementCardinality, final E... elements)
            throws IllegalArgumentException {
        this.elementCardinality = elementCardinality;
        if (elementCardinality == DISTINCT_ELEMENTS) {
            this.elements = ArrayUtilities.cloneDistinctElements(elements);
        } else {
            this.elements = elements.clone();
        }
        this.size = this.elements.length;
        this.hashedElementsSize = calculateHashedElementsSize(this.elements);
        this.hashedElements = HashUtilities.createHashedMapFromElements(this.elements, this.hashedElementsSize);
    }

    @Override
    public boolean add(final E element) {
        if (elementCardinality == DISTINCT_ELEMENTS && contains(element)) {
            return false;
        }
        if (size == elements.length) {
            resizeElementsTo(elements.length + STRIDE);
        }
        elements[size++] = element;
        // EQMU: Changing the conditional boundary below produces an equivalent mutant.
        if (size * MINIMAL_HASHING_RATIO > hashedElementsSize) {
            resizeHashedEntriesTo(size);
        } else {
            int i = HashUtilities.hash(element, hashedElementsSize);
            while (hashedElements[i] != null) {
                i = Math.floorMod(i + 1, hashedElementsSize);
            }
            hashedElements[i] = new Entry(element, element);
        }
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
            resizeElementsTo(size + numberOfNewElements + STRIDE);
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
        // EQMU: Changing the conditional boundary below produces an equivalent mutant.
        if (size * MINIMAL_HASHING_RATIO > hashedElementsSize) {
            resizeHashedEntriesTo(size);
        } else {
            for (E element : collection) {
                int i = HashUtilities.hash(element, hashedElementsSize);
                Entry entry = new Entry(element, element);
                boolean contains = false;
                while (hashedElements[i] != null) {
                    contains = contains || hashedElements[i].equals(entry);
                    i = Math.floorMod(i + 1, hashedElementsSize);
                }
                if (!contains || elementCardinality != DISTINCT_ELEMENTS) {
                    hashedElements[i] = entry;
                }
            }
        }

        return size != originalSize;
    }

    /**
     * Calculates the size of the array with the hashed elements for an array of elements.
     *
     * @param theElements The elements to be hashed.
     * @return The size of the array with the hashed elements.
     */
    private int calculateHashedElementsSize(final Object[] theElements) {
        return theElements.length * HASHING_RATIO;
    }

    @Override
    public void clear() {
        size = 0;
        // EQMU: Changing the conditional boundary below produces an equivalent mutant.
        // EQMU: Negating the conditional below produces an equivalent mutant.
        if (elements.length > STRIDE) {
            // EQMU: Removing the call to resizeTo below produces an equivalent mutant.
            resizeElementsTo(STRIDE);
        }
        resizeHashedEntriesTo(size);
    }

    @Override
    public boolean contains(final E element) {
        if (hashedElementsSize == 0) {
            return false;
        }
        Entry entry = new Entry(element, element);
        int index = HashUtilities.hash(element, hashedElementsSize);
        while (hashedElements[index] != null) {
            if (hashedElements[index].equals(entry)) {
                return true;
            }
            index = Math.floorMod(index + 1, hashedElementsSize);
        }
        return false;
    }

    @Override
    public boolean containsAll(final Collection<?> collection) {
        return ArrayUtilities.containsAll(elements, size, collection);
    }

    @Override
    public E get() throws IndexOutOfBoundsException {
        if (size == 0) {
            throw new IndexOutOfBoundsException("Cannot return an element from an empty collection.");
        } else {
            return (E) elements[0];
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
        if (contains(element)) {
            for (int i = 0; i < size; i++) {
                if (Objects.equals(element, elements[i])) {
                    elements[i] = elements[size - 1];
                    size--;
                    // EQMU: Changing the conditional boundary below produces an equivalent mutant.
                    // EQMU: Replacing integer subtraction with addition below produces an equivalent mutant.
                    // EQMU: Negating the conditional below produces an equivalent mutant.
                    if (size < elements.length - STRIDE) {
                        // EQMU: Removing the call to resizeTo below produces an equivalent mutant.
                        resizeElementsTo(size);
                    }
                    // EQMU: Changing the conditional boundary below produces an equivalent mutant.
                    // EQMU: Negating the conditional below produces an equivalent mutant.
                    // EQMU: Replacing integer multiplication with division below produces an equivalent mutant.
                    if (size * MAXIMAL_HASHING_RATIO < hashedElementsSize) {
                        resizeHashedEntriesTo(size);
                        return true;
                    }
                    Entry entry = new Entry(element, element);
                    int index = HashUtilities.hash(element, hashedElementsSize);
                    while (hashedElements[index] != null) {
                        if (hashedElements[index].equals(entry)) {
                            hashedElements[index] = null;
                            if (hashedElements[Math.floorMod(index + 1, hashedElementsSize)] != null) {
                                resizeHashedEntriesTo(size);
                            }
                            return true;
                        }
                        index = Math.floorMod(index + 1, hashedElementsSize);
                    }
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean removeAll(final Collection<? extends E> collection) {
        boolean result = false;
        for (E element : collection) {
            result = remove(element) || result;
        }
        return result;
    }

    @Override
    public boolean removeIf(final Predicate<? super E> predicate) {
        boolean result = false;
        for (Object object : toArray()) {
            E element = (E) object;
            if (predicate.test(element)) {
                remove(element);
                result = true;
            }
        }
        return result;
    }

    /**
     * Resizes the array to the new length. It is assumed that the new length is not less than the current size.
     *
     * @param newLength The new length for the array.
     */
    private void resizeElementsTo(final int newLength) {
        Object[] newElements = new Object[newLength];
        System.arraycopy(elements, 0, newElements, 0, size);
        elements = newElements;
    }

    /**
     * Resizes the hashed entries array to the new base length. The base length will be multiplied by a ratio to
     * calculate the actual new length for the hashed entries array.
     *
     * @param newBaseLength The new base length for the hashed entries array.
     */
    private void resizeHashedEntriesTo(final int newBaseLength) {
        hashedElementsSize = newBaseLength * HASHING_RATIO;
        Object[] hashedArray = new Object[hashedElementsSize];
        for (int i = 0; i < size; i++) {
            E element = (E) elements[i];
            Entry entry = new Entry(element, element);
            int j = HashUtilities.hash(element, hashedElementsSize);
            while (hashedArray[j] != null) {
                j = Math.floorMod(j + 1, hashedElementsSize);
            }
            hashedArray[j] = entry;
        }
        hashedElements = hashedArray;
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
        Object[] objects = toArray();
        boolean result = false;
        for (int i = 0; i < retain.length; i++) {
            if (!retain[i]) {
                remove((E) objects[i]);
                result = true;
            }
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
    public Object[] toArray() {
        Object[] result = new Object[size];
        System.arraycopy(elements, 0, result, 0, size);
        return result;
    }
}
