package net.filipvanlaenen.kolektoj;

import java.util.function.Function;
import java.util.function.Predicate;

import net.filipvanlaenen.kolektoj.array.OrderedArrayCollection;

/**
 * Interface defining the signature for all ordered collections.
 *
 * @param <E> The element type.
 */
public interface OrderedCollection<E> extends Collection<E> {
    /**
     * Returns an ordered collection holding a sequence of elements, starting with the provided first element, and with
     * the next elements generated recursively from the first element.
     *
     * @param <E>              The element type.
     * @param <F>              The element type or a subclass.
     * @param firstElement     The first element of the sequence.
     * @param generator        A function generating a next element when given an element.
     * @param numberOfElements The requested number of elements.
     * @return An ordered collection holding a sequence of elements.
     */
    static <E, F extends E> OrderedCollection<E> createSequence(final F firstElement,
            final Function<? super E, E> generator, final int numberOfElements) {
        if (numberOfElements < 1) {
            return (OrderedCollection<E>) OrderedCollection.empty();
        }
        ModifiableOrderedCollection<E> collection = ModifiableOrderedCollection.of(firstElement);
        E element = firstElement;
        for (int i = 1; i < numberOfElements; i++) {
            element = generator.apply(element);
            collection.add(element);
        }
        return new OrderedArrayCollection<E>(collection);
    }

    /**
     * Returns an ordered collection holding a sequence of elements, starting with the provided first element, and with
     * the next elements generated recursively from the first element until a condition evaluates to false.
     *
     * @param <E>            The element type.
     * @param <F>            The element type or a subclass.
     * @param firstElement   The first element of the sequence.
     * @param generator      A function generating a next element when given an element.
     * @param whileCondition A predicate defining a condition to be met by the generated elements to be part of the
     *                       sequence.
     * @return An ordered collection holding a sequence of elements.
     */
    static <E, F extends E> OrderedCollection<E> createSequence(final F firstElement,
            final Function<? super E, E> generator, final Predicate<? super E> whileCondition) {
        if (!whileCondition.test(firstElement)) {
            return (OrderedCollection<E>) OrderedCollection.empty();
        }
        ModifiableOrderedCollection<E> collection = ModifiableOrderedCollection.of(firstElement);
        E element = generator.apply(firstElement);
        while (whileCondition.test(element)) {
            collection.add(element);
            element = generator.apply(element);
        }
        return new OrderedArrayCollection<E>(collection);
    }

    /**
     * Returns an ordered collection holding a sequence of elements generated from a function taking an index as its
     * parameter.
     *
     * @param <E>              The element type.
     * @param generator        A function generating an element from an index.
     * @param numberOfElements The requested number of elements.
     * @return An ordered collection holding a sequence of elements.
     */
    static <E> OrderedCollection<E> createSequence(final Function<Integer, E> generator, final int numberOfElements) {
        if (numberOfElements < 1) {
            return (OrderedCollection<E>) OrderedCollection.empty();
        }
        ModifiableOrderedCollection<E> collection = ModifiableOrderedCollection.of(generator.apply(0));
        for (int i = 1; i < numberOfElements; i++) {
            collection.add(generator.apply(i));
        }
        return new OrderedArrayCollection<E>(collection);
    }

    /**
     * Returns an ordered collection holding a sequence of elements generated from a function taking an index as its
     * parameter until a condition evaluates to false.
     *
     * @param <E>            The element type.
     * @param generator      A function generating an element from an index.
     * @param whileCondition A predicate defining a condition to be met by the generated elements to be part of the
     *                       sequence.
     * @return An ordered collection holding a sequence of elements.
     */
    static <E> OrderedCollection<E> createSequence(final Function<Integer, E> generator,
            final Predicate<? super E> whileCondition) {
        E firstElement = generator.apply(0);
        if (!whileCondition.test(firstElement)) {
            return (OrderedCollection<E>) OrderedCollection.empty();
        }
        ModifiableOrderedCollection<E> collection = ModifiableOrderedCollection.of(firstElement);
        int index = 1;
        E element = generator.apply(index);
        while (whileCondition.test(element)) {
            collection.add(element);
            element = generator.apply(++index);
        }
        return new OrderedArrayCollection<E>(collection);
    }

    /**
     * Returns a new empty ordered collection.
     *
     * @param <E> The element type.
     * @return A new empty ordered collection.
     */
    static <E> OrderedCollection<E> empty() {
        return new OrderedArrayCollection<E>();
    }

    /**
     * Returns a new ordered collection with the specified elements.
     *
     * @param <E>      The element type.
     * @param elements The elements for the new ordered collection.
     * @return A new ordered collection with the specified elements.
     */
    static <E> OrderedCollection<E> of(final E... elements) {
        return new OrderedArrayCollection<E>(elements);
    }

    /**
     * Returns a new ordered collection with the specified element cardinality and the elements.
     *
     * @param <E>                The element type.
     * @param elementCardinality The element cardinality.
     * @param elements           The elements for the new ordered collection.
     * @return A new ordered collection with the specified element cardinality and the elements.
     */
    static <E> OrderedCollection<E> of(final ElementCardinality elementCardinality, final E... elements) {
        return new OrderedArrayCollection<E>(elementCardinality, elements);
    }

    /**
     * Returns the element from the collection at the given position.
     *
     * @param index The position of the element that should be returned.
     * @return The element from the collection at the given position.
     * @throws IndexOutOfBoundsException Thrown if the index is out of bounds.
     */
    E getAt(int index) throws IndexOutOfBoundsException;
}
