package net.filipvanlaenen.kolektoj.array;

import java.lang.reflect.Array;
import java.util.Objects;

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
     * Returns a clone of an array, but only with distinct elements.
     *
     * @param source The array to clone.
     * @param <E>    The element type.
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
}
