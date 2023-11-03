package net.filipvanlaenen.kolektoj.array;

import java.lang.reflect.Array;
import java.util.Objects;

import net.filipvanlaenen.kolektoj.Collection;

/**
 * A class with utility methods.
 */
final class ArrayUtilities {
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
    static <E> E[] cloneDistinctElements(final E[] source) {
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
    static <E> boolean containsAll(final E[] source, final int size, final Collection<?> collection) {
        if (collection.size() > size) {
            return false;
        }
        boolean[] matches = new boolean[size];
        for (Object element : collection) {
            for (int i = 0; i < size; i++) {
                if (!matches[i] && Objects.equals(element, source[i])) {
                    matches[i] = true;
                    break;
                }
            }
        }
        for (boolean match : matches) {
            if (!match) {
                return false;
            }
        }
        return true;
    }
}
