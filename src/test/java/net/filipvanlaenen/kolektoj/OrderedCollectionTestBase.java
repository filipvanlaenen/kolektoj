package net.filipvanlaenen.kolektoj;

import static net.filipvanlaenen.kolektoj.Collection.ElementCardinality.DISTINCT_ELEMENTS;
import static net.filipvanlaenen.kolektoj.Collection.ElementCardinality.DUPLICATE_ELEMENTS;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Spliterator;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import net.filipvanlaenen.kolektoj.Collection.ElementCardinality;

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
    private final T collection123 = createOrderedCollection(1, 2, 3);
    /**
     * Ordered array collection with the integers 1, 2, 3 and null.
     */
    private final OrderedCollection<Integer> collection123Null = createOrderedCollection(1, 2, 3, null);

    /**
     * Creates an ordered collection containing the provided integers.
     *
     * @param integers The integers to be included in the ordered collection.
     * @return An ordered collection containing the provided integers.
     */
    protected abstract T createOrderedCollection(Integer... integers);

    /**
     * Creates an ordered collection containing the provided integers with a given element cardinality.
     *
     * @param elementCardinality The element cardinality for the ordered collection.
     * @param integers           The integers to be included in the ordered collection.
     * @return An ordered collection containing the provided integers.
     */
    protected abstract T createOrderedCollection(ElementCardinality elementCardinality, Integer... integers);

    /**
     * Creates an ordered collection from another ordered collection.
     *
     * @param orderedCollection The ordered collection to create a new ordered collection from.
     * @return An ordered collection created from the provided ordered collection.
     */
    protected abstract T createOrderedCollection(T orderedCollection);

    /**
     * Verifies that contains returns true for an element in the collection.
     */
    @Test
    public void containsShouldReturnTrueForAnElementInTheCollection() {
        assertTrue(collection123.contains(1));
    }

    /**
     * Verifies that containsAll returns false is the other collection is larger.
     */
    @Test
    public void containsAllShouldReturnFalseIfTheOtherCollectionIsLarger() {
        assertFalse(collection123.containsAll(collection123Null));
    }

    /**
     * Verifies that containsAll returns true if a collection is compared to itself.
     */
    @Test
    public void containsAllShouldReturnTrueWhenComparedToItself() {
        assertTrue(collection123.containsAll(collection123));
    }

    /**
     * Verifies that contains returns false for an element not in the collection.
     */
    @Test
    public void containsShouldReturnFalseForAnElementNotInTheCollection() {
        assertFalse(collection123.contains(0));
    }

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
     * Verifies that when you get an element from a collection, the collection contains it.
     */
    @Test
    public void getShouldReturnAnElementPresentInTheCollection() {
        Integer element = collection123.get();
        assertTrue(collection123.contains(element));
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
     * Verifies that the collection constructed using another ordered collection produces the correct array.
     */
    @Test
    public void constructorUsingOrderedCollectionShouldProduceTheCorrectArray() {
        assertArrayEquals(ARRAY123, createOrderedCollection(collection123).toArray());
    }

    /**
     * Verifies that duplicate elements are removed if a collection with distinct elements is constructed.
     */
    @Test
    public void constructorShouldRemoveDuplicateElementsFromDistinctCollection() {
        OrderedCollection<Integer> collection = createOrderedCollection(DISTINCT_ELEMENTS, 1, 2, 2, THREE, 2, THREE);
        assertEquals(THREE, collection.size());
        assertTrue(collection.contains(1));
        assertTrue(collection.contains(2));
        assertTrue(collection.contains(THREE));
    }

    /**
     * Verifies that by default, a collection can contain duplicate elements.
     */
    @Test
    public void constructorShouldSetElementCardinalityToDuplicateByDefault() {
        assertEquals(DUPLICATE_ELEMENTS, createOrderedCollection().getElementCardinality());
    }

    /**
     * Verifies that when distinct elements are requested, the element cardinality is set to distinct elements.
     */
    @Test
    public void constructorShouldSetElementCardinalityToDistinctElementsWhenSpecified() {
        assertEquals(DISTINCT_ELEMENTS, createOrderedCollection(DISTINCT_ELEMENTS, 1).getElementCardinality());
    }

    /**
     * Verifies that firstIndexOf returns -1 for an element not in the collection.
     */
    @Test
    public void firstIndexOfShouldReturnMinusOneForAnElementNotInTheCollection() {
        assertEquals(-1, collection123.firstIndexOf(0));
    }

    /**
     * Verifies that firstIndexOf returns the correct index for an element in the collection.
     */
    @Test
    public void firstIndexOfShouldReturnIndexForAnElementInTheCollection() {
        assertEquals(1, collection123.firstIndexOf(2));
    }

    /**
     * Verifies that firstIndexOf returns the first index for a duplicate element in the collection.
     */
    @Test
    public void firstIndexOfShouldReturnFirstIndexForDuplicateElementInTheCollection() {
        assertEquals(1, createOrderedCollection(DUPLICATE_ELEMENTS, 1, 2, 2, 2, 2, THREE).firstIndexOf(2));
    }

    /**
     * Verifies that firstIndexOf returns the first index for a duplicate element at the beginning of the collection.
     */
    @Test
    public void firstIndexOfShouldReturnFirstIndexForDuplicateElementAtTheBeginningOfTheCollection() {
        assertEquals(0, createOrderedCollection(DUPLICATE_ELEMENTS, 1, 1, 1, 1, 2, THREE).firstIndexOf(1));
    }

    /**
     * Verifies that indexOf returns -1 for an element not in the collection.
     */
    @Test
    public void indexOfShouldReturnMinusOneForAnElementNotInTheCollection() {
        assertEquals(-1, collection123.indexOf(0));
    }

    /**
     * Verifies that indexOf returns the correct index for an element in the collection.
     */
    @Test
    public void indexOfShouldReturnIndexForAnElementInTheCollection() {
        assertEquals(1, collection123.indexOf(2));
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
