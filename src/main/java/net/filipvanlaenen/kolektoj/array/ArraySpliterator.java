package net.filipvanlaenen.kolektoj.array;

import java.lang.reflect.Array;
import java.util.Spliterator;
import java.util.function.Consumer;

final class ArraySpliterator<E> implements Spliterator<E> {
    private final int characteristics;
    private final E[] elements;
    private int index;

    ArraySpliterator(final E[] elements, final int additionalCharacteristics) {
        this.elements = elements.clone();
        this.characteristics = SIZED | SUBSIZED | additionalCharacteristics;
    }

    private ArraySpliterator(final E[] elements, final int from, final int to, final int characteristics) {
        int size = to - from;
        Class<E[]> clazz = (Class<E[]>) elements.getClass();
        this.elements = (E[]) Array.newInstance(clazz.getComponentType(), size);
        System.arraycopy(elements, from, this.elements, 0, size);
        this.characteristics = characteristics;
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

    @Override
    public long estimateSize() {
        return (long) (elements.length - index);
    }

    @Override
    public int characteristics() {
        return characteristics;
    }
}