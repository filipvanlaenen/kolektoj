package net.filipvanlaenen.kolektoj.array;

import java.lang.reflect.Array;
import java.util.Comparator;
import java.util.Objects;

import net.filipvanlaenen.kolektoj.Collection;

/**
 * A class with utility methods for array-backed collections.
 */
public final class ArrayUtilities {
    /**
     * Private constructor to avoid instantiation of this utility class.
     */
    private ArrayUtilities() {
    }

    /**
     * Returns a clone of an array, but only with distinct elements. The ordered of the elements is preserved.
     *
     * @param <E>    The element type.
     * @param source The array to clone.
     * @return A new array containing only distinct elements from the source array.
     */
    public static <E> E[] cloneDistinctElements(final E[] source) {
        int originalLength = source.length;
        int resultLength = originalLength;
        boolean[] duplicate = new boolean[originalLength];
        for (int i = 0; i < originalLength; i++) {
            if (!duplicate[i]) {
                for (int j = i + 1; j < originalLength; j++) {
                    if (Objects.equals(source[i], source[j])) {
                        duplicate[j] = true;
                        resultLength--;
                    }
                }
            }
        }
        Class<E[]> clazz = (Class<E[]>) source.getClass();
        E[] result = (E[]) Array.newInstance(clazz.getComponentType(), resultLength);
        for (int i = 0, j = 0; j < resultLength; i++, j++) {
            while (duplicate[i]) {
                i++;
            }
            result[j] = source[i];
        }
        return result;
    }

    /**
     * Returns true if the first <code>size</code> elements of the <code>source</code> array contain an element equal to
     * the <code>element</code>.
     *
     * @param <E>      The element type.
     * @param elements The array that should contain the elements.
     * @param size     The number of elements to check in the source array.
     * @param element  The element.
     * @return True if the first <code>size</code> elements of the <code>source</code> array contain an elements equal
     *         to the <code>element</code>, and false otherwise.
     */
    static <E> boolean contains(final E[] elements, final int size, final E element) {
        for (int i = 0; i < size; i++) {
            if (Objects.equals(element, elements[i])) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns true if the first <code>size</code> elements of the sorted <code>source</code> array contain an element
     * equal to the <code>element</code>.
     *
     * @param <E>        The element type.
     * @param elements   The array that should contain the elements.
     * @param size       The number of elements to check in the source array.
     * @param element    The element.
     * @param comparator The comparator to use.
     * @return True if the first <code>size</code> elements of the <code>source</code> array contain an elements equal
     *         to the <code>element</code>, and false otherwise.
     */
    static <E> boolean contains(final Object[] elements, final int size, final E element,
            final Comparator<E> comparator) {
        int index = findIndex(elements, size, element, comparator);
        return index >= 0 && Objects.equals(element, elements[index]);
    }

    /**
     * Returns true if the first <code>size</code> elements of the <code>source</code> array contain all the elements of
     * the <code>collection</code>.
     *
     * @param <E>        The element type.
     * @param source     The array that should contain the elements.
     * @param size       The number of elements to check in the source array.
     * @param collection The collection.
     * @return True if the first <code>size</code> elements of the <code>source</code> array contain all the elements of
     *         the <code>collection</code>, and false otherwise.
     */
    public static <E> boolean containsAll(final E[] source, final int size, final Collection<?> collection) {
        if (collection.size() > size) {
            return false;
        }
        boolean[] matches = new boolean[size];
        for (Object element : collection) {
            boolean found = false;
            for (int i = 0; i < size; i++) {
                if (!matches[i] && Objects.equals(element, source[i])) {
                    matches[i] = true;
                    found = true;
                    break;
                }
            }
            if (!found) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns true if the first <code>size</code> elements of the sorted <code>source</code> array contain all the
     * elements of the <code>collection</code>.
     *
     * @param <E>        The element type.
     * @param source     The array that should contain the elements.
     * @param size       The number of elements to check in the source array.
     * @param collection The collection.
     * @param comparator The comparator to use.
     * @return True if the first <code>size</code> elements of the <code>source</code> array contain all the elements of
     *         the <code>collection</code>, and false otherwise.
     */
    static <E> boolean containsAll(final Object[] source, final int size, final Collection<?> collection,
            final Comparator<E> comparator) {
        if (collection.size() > size) {
            return false;
        }
        boolean[] matches = new boolean[size];
        for (Object element : collection) {
            E elementAsE = (E) element;
            boolean found = false;
            int below = -1;
            int above = size;
            while (above > below + 1) {
                int middle = (below + above) / 2;
                int comparison = 0;
                try {
                    comparison = comparator.compare(elementAsE, (E) source[middle]);
                } catch (ClassCastException cce) {
                    return false;
                }
                if (comparison == 0) {
                    if (!matches[middle]) {
                        matches[middle] = true;
                        found = true;
                        break;
                    } else {
                        while (middle > 0 && comparator.compare((E) source[middle - 1], (E) source[middle]) == 0) {
                            middle--;
                            if (!matches[middle]) {
                                matches[middle] = true;
                                found = true;
                                break;
                            }
                        }
                        if (!found) {
                            while (middle + 1 < size
                                    && comparator.compare((E) source[middle], (E) source[middle + 1]) == 0) {
                                middle++;
                                if (!matches[middle]) {
                                    matches[middle] = true;
                                    found = true;
                                    break;
                                }
                            }
                        }
                        if (found) {
                            break;
                        }
                        return false;
                    }
                } else if (comparison < 0) {
                    // EQMU: Changing the conditional boundary above produces an equivalent mutant.
                    above = middle;
                } else {
                    below = middle;
                }
            }
            if (!found) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns an index where an element can be found in the first <code>size</code> elements of a sorted array, or -1
     * if it's absent.
     *
     * @param <E>        The element type.
     * @param elements   The array that should contain the elements.
     * @param size       The number of elements to check in the source array.
     * @param element    The element.
     * @param comparator The comparator to use.
     * @return An index where an element can be found in the first <code>size</code> elements of a sorted array, or -1
     *         if it's absent.
     */
    static <E> int findIndex(final Object[] elements, final int size, final E element, final Comparator<E> comparator) {
        int below = -1;
        int above = size;
        while (above > below + 1) {
            int middle = (below + above) / 2;
            int comparison = comparator.compare(element, (E) elements[middle]);
            if (comparison == 0) {
                return middle;
            } else if (comparison < 0) {
                // EQMU: Changing the conditional boundary above produces an equivalent mutant.
                above = middle;
            } else {
                below = middle;
            }
        }
        return -1;
    }

    /**
     * Partitions an array for the Quicksort algorithm using the given comparator.
     *
     * @param <E>        The element type.
     * @param array      The array to partition.
     * @param comparator The comparator to use.
     * @param first      The index of the first element in the array that should be partitioned.
     * @param last       The index of the last element in the array that should be partitioned.
     * @return The index of the pivot element.
     */
    private static <E> int partition(final Object[] array, final Comparator<E> comparator, final int first,
            final int last) {
        E pivot = (E) array[last];
        int index = first - 1;
        for (int j = first; j < last; j++) {
            // EQMU: Changing the conditional boundary below produces an equivalent mutant.
            if (comparator.compare((E) array[j], pivot) <= 0) {
                swap(array, ++index, j);
            }
        }
        swap(array, index + 1, last);
        return index + 1;
    }

    /**
     * Returns an array based on the elements of an array using the given comparator according to the Quicksort
     * algorithm.
     *
     * @param <E>        The element type.
     * @param source     The array to sort.
     * @param comparator The comparator to use.
     * @return The array sorted using the comparator.
     */
    public static <E> Object[] quicksort(final Object[] source, final Comparator<E> comparator) {
        Object[] result = source.clone();
        quicksort(result, comparator, 0, result.length - 1);
        return result;
    }

    /**
     * Sorts an array using the given comparator according to the Quicksort algorithm.
     *
     * @param <E>        The element type.
     * @param array      The array to sort.
     * @param comparator The comparator to use.
     * @param first      The index of the first element in the array that should be sorted.
     * @param last       The index of the last element in the array that should be sorted.
     */
    private static <E> void quicksort(final Object[] array, final Comparator<E> comparator, final int first,
            final int last) {
        // EQMU: Changing the conditional boundary below produces an equivalent mutant.
        if (first < last) {
            int pivotIndex = partition(array, comparator, first, last);
            quicksort(array, comparator, first, pivotIndex - 1);
            quicksort(array, comparator, pivotIndex + 1, last);
        }
    }

    /**
     * Swaps two elements in an array.
     *
     * @param <E>   The element type.
     * @param array The array.
     * @param i     The index of the first element.
     * @param j     The index of the second element.
     */
    private static <E> void swap(final E[] array, final int i, final int j) {
        E value = array[i];
        array[i] = array[j];
        array[j] = value;
    }
}
