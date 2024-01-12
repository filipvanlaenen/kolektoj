package net.filipvanlaenen.kolektoj;

import static net.filipvanlaenen.kolektoj.Collection.ElementCardinality.DISTINCT_ELEMENTS;
import static net.filipvanlaenen.kolektoj.Collection.ElementCardinality.DUPLICATE_ELEMENTS;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Spliterator;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import net.filipvanlaenen.kolektoj.Collection.ElementCardinality;
import net.filipvanlaenen.kolektoj.array.OrderedArrayCollection;

/**
 * Unit tests on implementations of the {@link net.filipvanlaenen.kolektoj.OrderedCollection} interface.
 *
 * @param <T> The subclass type to be tested.
 */
public abstract class OrderedCollectionTestBase<T extends OrderedCollection<Integer>> {
    /**
     * The magic number three.
     */
    private static final int THREE = 3;
    /**
     * An array containing the numbers 1, 2 and 3.
     */
    private static final Integer[] ARRAY123 = new Integer[] {1, 2, THREE};
    /**
     * Ordered array collection with the integers 1, 2 and 3.
     */
    private final OrderedCollection<Integer> collection123 = createOrderedCollection(1, 2, 3);

    /**
     * Creates an ordered collection containing the provided integers.
     *
     * @param integers The integers to be included in the ordered collection.
     * @return An ordered collection containing the provided integers.
     */
    protected abstract T createOrderedCollection(Integer... integers);

    /**
     * Creates an ordered collection containing the provided integers.
     *
     * @param elementCardinality The element cardinality for the ordered collection.
     * @param integers           The integers to be included in the ordered collection.
     * @return An ordered collection containing the provided integers.
     */
    protected abstract T createOrderedCollection(ElementCardinality elementCardinality, Integer... integers);

    /**
     * Verifies that the correct length is returned for an ordered collection with three elements.
     */
    @Test
    public void sizeShouldReturnThreeForACollectionOfThreeElements() {
        assertEquals(THREE, collection123.size());
    }

    /**
     * Verifies that when you get an element from a collection by its index, the correct element is returned.
     */
    @Test
    public void getAtShouldReturnCorrectElement() {
        assertEquals(2, collection123.getAt(1));
    }

    /**
     * Verifies that trying to get an element from an empty collection throws IndexOutOfBoundsException.
     */
    @Test
    public void getShouldThrowExceptionWhenCalledOnAnEmptyCollection() {
        IndexOutOfBoundsException exception =
                assertThrows(IndexOutOfBoundsException.class, () -> createOrderedCollection().get());
        assertEquals("Cannot return an element from an empty collection.", exception.getMessage());
    }

    /**
     * Verifies that when you try to get an element from a collection beyond the length, getAt throws
     * IndexOutOfBoundsException.
     */
    @Test
    public void getAtShouldThrowIndexOutOfBoundsExceptionWhenCalledBeyondSize() {
        IndexOutOfBoundsException exception =
                assertThrows(IndexOutOfBoundsException.class, () -> collection123.getAt(THREE));
        assertEquals("Cannot return an element at a position beyond the size of the collection.",
                exception.getMessage());
    }

    /**
     * Verifies that the collection produces an array with the elements in the same order.
     */
    @Test
    public void toArrayShouldProduceAnArrayWithTheElementsOfTheCollection() {
        assertArrayEquals(ARRAY123, collection123.toArray());
    }

    /**
     * Verifies that the collection produces an iterator that produces the elements in the correct order.
     */
    @Test
    public void iteratorShouldProduceCorrectStringInForLoop() {
        String actual = "";
        for (Integer i : collection123) {
            actual += i;
        }
        assertEquals("123", actual);
    }

    /**
     * Verifies that the spliterator has the ordered flag set.
     */
    @Test
    public void spliteratorShouldSetOrderedFlag() {
        assertTrue(collection123.spliterator().hasCharacteristics(Spliterator.ORDERED));
    }

    /**
     * Verifies that the spliterator has the distinct flag not set for collections with duplicate elements.
     */
    @Test
    public void spliteratorShouldNotSetDistinctFlagForCollectionWithDuplicateElements() {
        assertFalse(
                createOrderedCollection(DUPLICATE_ELEMENTS, 1).spliterator().hasCharacteristics(Spliterator.DISTINCT));
    }

    /**
     * Verifies that the spliterator has the distinct flag set for collections with distinct elements.
     */
    @Test
    public void spliteratorShouldSetDistinctFlagForCollectionWithDistinctElements() {
        assertTrue(
                createOrderedCollection(DISTINCT_ELEMENTS, 1).spliterator().hasCharacteristics(Spliterator.DISTINCT));
    }

    /**
     * Verifies that the collection produces a stream that collects to the correct string, thus verifying that the
     * spliterator is created correctly.
     */
    @Test
    public void streamShouldProduceAStreamThatCollectsToTheCorrectString() {
        assertEquals("1,2,3", collection123.stream().map(Object::toString).collect(Collectors.joining(",")));
    }
}
