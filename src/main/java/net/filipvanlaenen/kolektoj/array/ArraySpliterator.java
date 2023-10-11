package net.filipvanlaenen.kolektoj.array;

import java.lang.reflect.Array;
import java.util.Spliterator;
import java.util.function.Consumer;

/**
 * An implementation of {@link java.util.Spliterator} that is backed by an array. This implementation provides a
 * spliterator that is both sized and subsized, but depending on the content and the order of the elements, it may have
 * additional characteristics.
 *
 * @param <E> The element type.
 */
public final class ArraySpliterator<E> implements Spliterator<E> {
    /**
     * The characteristics of the spliterator.
     */
    private final int characteristics;
    /**
     * The elements of the spliterator.
     */
    private final E[] elements;
    /**
     * The spliterator's current position.
     */
    private int index;

    /**
     * Constructor taking the elements and additional characteristics as its arguments.
     *
     * @param elements                  The elements for the spliterator.
     * @param additionalCharacteristics The characteristics for the spliterator in addition to SIZED and SUBSIZED.
     */
    public ArraySpliterator(final E[] elements, final int additionalCharacteristics) {
        this.elements = elements.clone();
        this.characteristics = SIZED | SUBSIZED | additionalCharacteristics;
    }

    /**
     * Constructor taking the elements and additional characteristics as its arguments, in addition to the starting
     * (inclusive) and ending (exclusive) position in the element array.
     *
     * @param elements        The elements for the spliterator.
     * @param from            The position of the first element to be included.
     * @param to              The position just after the last element to be included.
     * @param characteristics The characteristics for the spliterator in addition to SIZED and SUBSIZED.
     */
    private ArraySpliterator(final E[] elements, final int from, final int to, final int characteristics) {
        int size = to - from;
        Class<E[]> clazz = (Class<E[]>) elements.getClass();
        this.elements = (E[]) Array.newInstance(clazz.getComponentType(), size);
        System.arraycopy(elements, from, this.elements, 0, size);
        this.characteristics = characteristics;
    }

    @Override
    public int characteristics() {
        return characteristics;
    }

    @Override
    public long estimateSize() {
        return (long) (elements.length - index);
    }

    @Override
    public boolean tryAdvance(final Consumer<? super E> action) {
        if (index < elements.length) {
            action.accept(elements[index++]);
            return true;
        }
        return false;
    }

    @Override
    public Spliterator<E> trySplit() {
        int currentSize = elements.length - index;
        int splitIndex = index + currentSize / 2;
        if (index < splitIndex) {
            int originalIndex = index;
            index = splitIndex;
            return new ArraySpliterator<E>(elements, originalIndex, splitIndex, characteristics);
        } else {
            return null;
        }
    }
}
