package net.filipvanlaenen.kolektoj.collectors;

import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

import net.filipvanlaenen.kolektoj.Collection;
import net.filipvanlaenen.kolektoj.linkedlist.ModifiableLinkedListCollection;

/**
 * Collector for the {@link net.filipvanlaenen.kolektoj.array.Collection} type.
 *
 * @param <E> The element type.
 */
public final class CollectionCollector<E> implements Collector<E, ModifiableLinkedListCollection<E>, Collection<E>> {
    @Override
    public BiConsumer<ModifiableLinkedListCollection<E>, E> accumulator() {
        return ModifiableLinkedListCollection::add;
    }

    @Override
    public Set<Characteristics> characteristics() {
        return Set.of(Characteristics.UNORDERED);
    }

    @Override
    public BinaryOperator<ModifiableLinkedListCollection<E>> combiner() {
        return (a, b) -> {
            a.addAll(b);
            return a;
        };
    }

    @Override
    public Function<ModifiableLinkedListCollection<E>, Collection<E>> finisher() {
        return a -> Collection.of(a);
    }

    @Override
    public Supplier<ModifiableLinkedListCollection<E>> supplier() {
        return ModifiableLinkedListCollection::new;
    }

    /**
     * Returns a collector that accumulates the input elements into a new
     * {@link net.filipvanlaenen.kolektoj.array.Collection}.
     *
     * @param <F> The element type.
     * @return A collector to produce a new {@link net.filipvanlaenen.kolektoj.array.Collection}.
     */
    public static <F> CollectionCollector<F> toCollection() {
        return new CollectionCollector<F>();
    }
}
