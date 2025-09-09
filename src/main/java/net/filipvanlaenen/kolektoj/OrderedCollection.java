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
     * @param <F>              The element type.
     * @param <G>              The element type or a subclass.
     * @param firstElement     The first element of the sequence.
     * @param generator        A function generating a next element when given an element.
     * @param numberOfElements The requested number of elements.
     * @return An ordered collection holding a sequence of elements.
     */
    static <F, G extends F> OrderedCollection<F> createSequence(final G firstElement,
            final Function<? super F, F> generator, final int numberOfElements) {
        if (numberOfElements < 1) {
            return (OrderedCollection<F>) OrderedCollection.empty();
        }
        ModifiableOrderedCollection<F> collection = ModifiableOrderedCollection.of(firstElement);
        F element = firstElement;
        for (int i = 1; i < numberOfElements; i++) {
            element = generator.apply(element);
            collection.add(element);
        }
        return new OrderedArrayCollection<F>(collection);
    }

    /**
     * Returns an ordered collection holding a sequence of elements, starting with the provided first element, and with
     * the next elements generated recursively from the first element until a condition evaluates to false.
     *
     * @param <F>            The element type.
     * @param <G>            The element type or a subclass.
     * @param firstElement   The first element of the sequence.
     * @param generator      A function generating a next element when given an element.
     * @param whileCondition A predicate defining a condition to be met by the generated elements to be part of the
     *                       sequence.
     * @return An ordered collection holding a sequence of elements.
     */
    static <F, G extends F> OrderedCollection<F> createSequence(final G firstElement,
            final Function<? super F, F> generator, final Predicate<? super F> whileCondition) {
        if (!whileCondition.test(firstElement)) {
            return (OrderedCollection<F>) OrderedCollection.empty();
        }
        ModifiableOrderedCollection<F> collection = ModifiableOrderedCollection.of(firstElement);
        F element = generator.apply(firstElement);
        while (whileCondition.test(element)) {
            collection.add(element);
            element = generator.apply(element);
        }
        return new OrderedArrayCollection<F>(collection);
    }

    /**
     * Returns an ordered collection holding a sequence of elements generated from a function taking an index as its
     * parameter.
     *
     * @param <F>              The element type.
     * @param generator        A function generating an element from an index.
     * @param numberOfElements The requested number of elements.
     * @return An ordered collection holding a sequence of elements.
     */
    static <F> OrderedCollection<F> createSequence(final Function<Integer, F> generator, final int numberOfElements) {
        if (numberOfElements < 1) {
            return (OrderedCollection<F>) OrderedCollection.empty();
        }
        ModifiableOrderedCollection<F> collection = ModifiableOrderedCollection.of(generator.apply(0));
        for (int i = 1; i < numberOfElements; i++) {
            collection.add(generator.apply(i));
        }
        return new OrderedArrayCollection<F>(collection);
    }

    /**
     * Returns an ordered collection holding a sequence of elements generated from a function taking an index as its
     * parameter until a condition evaluates to false.
     *
     * @param <F>            The element type.
     * @param generator      A function generating an element from an index.
     * @param whileCondition A predicate defining a condition to be met by the generated elements to be part of the
     *                       sequence.
     * @return An ordered collection holding a sequence of elements.
     */
    static <F> OrderedCollection<F> createSequence(final Function<Integer, F> generator,
            final Predicate<? super F> whileCondition) {
        F firstElement = generator.apply(0);
        if (!whileCondition.test(firstElement)) {
            return (OrderedCollection<F>) OrderedCollection.empty();
        }
        ModifiableOrderedCollection<F> collection = ModifiableOrderedCollection.of(firstElement);
        int index = 1;
        F element = generator.apply(index);
        while (whileCondition.test(element)) {
            collection.add(element);
            element = generator.apply(++index);
        }
        return new OrderedArrayCollection<F>(collection);
    }

    /**
     * Returns a new empty ordered collection.
     *
     * @param <F> The element type.
     * @return A new empty ordered collection.
     */
    static <F> OrderedCollection<F> empty() {
        return new OrderedArrayCollection<F>();
    }

    /**
     * Returns a new ordered collection with the specified element cardinality and the elements.
     *
     * @param <F>                The element type.
     * @param elementCardinality The element cardinality.
     * @param elements           The elements for the new ordered collection.
     * @return A new ordered collection with the specified element cardinality and the elements.
     */
    static <F> OrderedCollection<F> of(final ElementCardinality elementCardinality, final F... elements) {
        return new OrderedArrayCollection<F>(elementCardinality, elements);
    }

    /**
     * Returns a new ordered collection with the specified elements.
     *
     * @param <F>      The element type.
     * @param elements The elements for the new ordered collection.
     * @return A new ordered collection with the specified elements.
     */
    static <F> OrderedCollection<F> of(final F... elements) {
        return new OrderedArrayCollection<F>(elements);
    }

    /**
     * Returns a new ordered collection cloned from the provided ordered collection.
     *
     * @param <F>        The element type.
     * @param collection The original ordered collection.
     * @return A new ordered collection cloned from the provided ordered collection.
     */
    static <F> OrderedCollection<F> of(final OrderedCollection<? extends F> collection) {
        return new OrderedArrayCollection<F>(collection);
    }

    /**
     * Returns the element from the collection at the given position.
     *
     * @param index The position of the element that should be returned.
     * @return The element from the collection at the given position.
     * @throws IndexOutOfBoundsException Thrown if the index is out of bounds.
     */
    E getAt(int index) throws IndexOutOfBoundsException;

    /**
     * Returns the first element in the collection.
     *
     * @return The first element in the collection.
     * @throws IndexOutOfBoundsException Thrown if the collection is empty.
     */
    default E getFirst() throws IndexOutOfBoundsException {
        if (size() == 0) {
            throw new IndexOutOfBoundsException("Cannot return an element from an empty collection.");
        } else {
            return getAt(0);
        }
    }

    /**
     * Returns the last element in the collection.
     *
     * @return The last element in the collection.
     * @throws IndexOutOfBoundsException Thrown if the collection is empty.
     */
    default E getLast() throws IndexOutOfBoundsException {
        if (size() == 0) {
            throw new IndexOutOfBoundsException("Cannot return an element from an empty collection.");
        } else {
            return getAt(size() - 1);
        }
    }

    /**
     * Returns the index of an occurrence of the specified element, or -1 if this collection does not contain the
     * element.
     *
     * @param element The element for which an index should be returned.
     * @return The index of an occurrence of the element, or -1 is the collection doesn't contain it.
     */
    int indexOf(E element);
}
