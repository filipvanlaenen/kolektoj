package net.filipvanlaenen.kolektoj.collectors;

import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collector.Characteristics;

import net.filipvanlaenen.kolektoj.Collection;
import net.filipvanlaenen.kolektoj.OrderedCollection;
import net.filipvanlaenen.kolektoj.linkedlist.ModifiableLinkedListCollection;
import net.filipvanlaenen.kolektoj.linkedlist.ModifiableOrderedLinkedListCollection;

/**
 * Collector for the {@link net.filipvanlaenen.kolektoj.array.Collection} type.
 */
public final class Collectors {
    /**
     * A simple implementation of the Collector interface using a record.
     *
     * @param <T> The type of elements to be collected.
     * @param <A> The type of the accumulator.
     * @param <R> The type of the result.
     */
    private record SimpleCollector<T, A, R>(Supplier<A> supplier, BiConsumer<A, T> accumulator,
            BinaryOperator<A> combiner, Function<A, R> finisher, Set<Characteristics> characteristics)
            implements Collector<T, A, R> {
    };

    /**
     * Returns a collector that accumulates the input elements into a new
     * {@link net.filipvanlaenen.kolektoj.Collection}.
     *
     * @param <E> The element type.
     * @return A collector to produce a new {@link net.filipvanlaenen.kolektoj.array.Collection}.
     */
    public static <E> Collector<E, ModifiableLinkedListCollection<E>, Collection<E>> toCollection() {
        return new SimpleCollector<>(ModifiableLinkedListCollection::new, ModifiableLinkedListCollection::add,
                (a, b) -> {
                    a.addAll(b);
                    return a;
                }, a -> Collection.of(a), Set.of(Characteristics.UNORDERED));
    }

    /**
     * Returns a collector that accumulates the input elements into a new
     * {@link net.filipvanlaenen.kolektoj.OrderedCollection}.
     *
     * @param <E> The element type.
     * @return A collector to produce a new {@link net.filipvanlaenen.kolektoj.OrderedCollection}.
     */
    public static <
            E> Collector<E, ModifiableOrderedLinkedListCollection<E>, OrderedCollection<E>> toOrderedCollection() {
        return new SimpleCollector<>(ModifiableOrderedLinkedListCollection::new,
                ModifiableOrderedLinkedListCollection::add, (a, b) -> {
                    a.addAll(b);
                    return a;
                }, a -> OrderedCollection.of(a), Set.of());
    }
}
