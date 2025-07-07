package net.filipvanlaenen.kolektoj.collectors;

import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collector.Characteristics;

import net.filipvanlaenen.kolektoj.Collection;
import net.filipvanlaenen.kolektoj.ModifiableCollection;
import net.filipvanlaenen.kolektoj.ModifiableOrderedCollection;
import net.filipvanlaenen.kolektoj.OrderedCollection;
import net.filipvanlaenen.kolektoj.linkedlist.ModifiableLinkedListCollection;
import net.filipvanlaenen.kolektoj.linkedlist.ModifiableOrderedLinkedListCollection;

/**
 * A utility class providing various collectors.
 */
public final class Collectors {
    /**
     * A simple implementation of the Collector interface using a record.
     *
     * @param supplier        The supplier for the collector.
     * @param accumulator     The accumulator for the collector.
     * @param combiner        The combiner for the collector.
     * @param finisher        The finisher for the collector.
     * @param characteristics The characteristics of the collector.
     * @param <E>             The type of elements to be collected.
     * @param <A>             The type of the accumulator.
     * @param <R>             The type of the result.
     */
    private record SimpleCollector<E, A, R>(Supplier<A> supplier, BiConsumer<A, E> accumulator,
            BinaryOperator<A> combiner, Function<A, R> finisher, Set<Characteristics> characteristics)
            implements Collector<E, A, R> {
    };

    /**
     * Private constructor to avoid instantiation of this utility class.
     */
    private Collectors() {
    }

    /**
     * Returns a collector that accumulates the input elements into a new
     * {@link net.filipvanlaenen.kolektoj.Collection}.
     *
     * @param <E> The element type.
     * @return A collector to produce a new {@link net.filipvanlaenen.kolektoj.Collection}.
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
     * {@link net.filipvanlaenen.kolektoj.ModifiableCollection}.
     *
     * @param <E> The element type.
     * @return A collector to produce a new {@link net.filipvanlaenen.kolektoj.ModifiableCollection}.
     */
    public static <
            E> Collector<E, ModifiableLinkedListCollection<E>, ModifiableCollection<E>> toModifiableCollection() {
        return new SimpleCollector<>(ModifiableLinkedListCollection::new, ModifiableLinkedListCollection::add,
                (a, b) -> {
                    a.addAll(b);
                    return a;
                }, a -> ModifiableCollection.of(a), Set.of(Characteristics.UNORDERED));
    }

    /**
     * Returns a collector that accumulates the input elements into a new
     * {@link net.filipvanlaenen.kolektoj.ModifiableOrderedCollection}.
     *
     * @param <E> The element type.
     * @return A collector to produce a new {@link net.filipvanlaenen.kolektoj.ModifiableOrderedCollection}.
     */
    public static <E> Collector<E, ModifiableOrderedLinkedListCollection<E>,
            ModifiableOrderedCollection<E>> toModifiableOrderedCollection() {
        return new SimpleCollector<>(ModifiableOrderedLinkedListCollection::new,
                ModifiableOrderedLinkedListCollection::addLast, (a, b) -> {
                    a.addAllLast(b);
                    return a;
                }, a -> ModifiableOrderedCollection.of(a), Set.of());
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
                ModifiableOrderedLinkedListCollection::addLast, (a, b) -> {
                    a.addAllLast(b);
                    return a;
                }, a -> OrderedCollection.of(a), Set.of());
    }
}
