package net.filipvanlaenen.kolektoj;

import static net.filipvanlaenen.kolektoj.Collection.ElementCardinality.DISTINCT_ELEMENTS;
import static net.filipvanlaenen.kolektoj.Collection.ElementCardinality.DUPLICATE_ELEMENTS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Spliterator;

import org.junit.jupiter.api.Test;

import net.filipvanlaenen.kolektoj.Collection.ElementCardinality;

/**
 * Unit tests on implementations of the {@link net.filipvanlaenen.kolektoj.Collection} interface.
 *
 * @param <T> The subclass type to be tested.
 */
public abstract class CollectionTestBase<T extends Collection<Integer>> {
    /**
     * The magic number three.
     */
    private static final int THREE = 3;
    /**
     * The magic number four.
     */
    private static final int FOUR = 4;
    /**
     * The magic number six.
     */
    private static final int SIX = 6;
    /**
     * Collection with the integers 1, 2 and 3.
     */
    private final T collection123 = createCollection(1, 2, 3);
    /**
     * Ordered collection with the integers 1, 2, 3 and null.
     */
    private final Collection<Integer> collection123Null = createCollection(1, 2, 3, null);

    /**
     * Creates an ordered collection from another collection with a given element cardinality.
     *
     * @param collection The collection to create a new collection from.
     * @return An collection created from the provided collection with a given element cardinality.
     */
    protected abstract T createCollection(ElementCardinality elementCardinality, T collection);

    /**
     * Creates a collection containing the provided integers with a given element cardinality.
     *
     * @param elementCardinality The element cardinality for the collection.
     * @param integers           The integers to be included in the collection.
     * @return A collection containing the provided integers.
     */
    protected abstract T createCollection(ElementCardinality elementCardinality, Integer... integers);

    /**
     * Creates a collection containing the provided integers.
     *
     * @param integers The integers to be included in the collection.
     * @return A collection containing the provided integers.
     */
    protected abstract T createCollection(Integer... integers);

    /**
     * Creates an ordered collection from another collection.
     *
     * @param collection The collection to create a new collection from.
     * @return An collection created from the provided collection.
     */
    protected abstract T createCollection(T collection);

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
     * Verifies that containsAll returns false if one element doesn't match.
     */
    @Test
    public void containsAllShouldReturnFalseWhenOneElementDoesNotMatch() {
        assertFalse(collection123.containsAll(createCollection(1, 2, FOUR)));
    }

    /**
     * Verifies that containsAll returns true if the other collection is a subset.
     */
    @Test
    public void containsAllShouldReturnTrueIfTheOtherCollectionIsASubset() {
        assertTrue(collection123Null.containsAll(collection123));
    }

    /**
     * Verifies that contains returns true for an element in the collection.
     */
    @Test
    public void containsShouldReturnTrueForAnElementInTheCollection() {
        assertTrue(collection123.contains(1));
    }

    /**
     * Verifies that contains returns false for null if it isn't in the collection.
     */
    @Test
    public void containsShouldReturnFalseForNullIfNotInTheTheCollection() {
        assertFalse(collection123.contains(null));
    }

    /**
     * Verifies that contains returns false for an element not in the collection.
     */
    @Test
    public void containsShouldReturnFalseForAnElementNotInTheCollection() {
        assertFalse(collection123.contains(0));
    }

    /**
     * Verifies that contains returns true for null if it is in the collection.
     */
    @Test
    public void containsShouldReturnTrueForNullIfInTheTheCollection() {
        assertTrue(collection123Null.contains(null));
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
                assertThrows(IndexOutOfBoundsException.class, () -> createCollection().get());
        assertEquals("Cannot return an element from an empty collection.", exception.getMessage());
    }

    /**
     * Verifies that the correct length is returned for an ordered collection with three elements.
     */
    @Test
    public void sizeShouldReturnThreeForACollectionOfThreeElements() {
        assertEquals(THREE, collection123.size());
    }

    /**
     * Verifies that the collection produces an array with the elements in the same order.
     */
    @Test
    public void toArrayShouldProduceAnArrayWithTheElementsOfTheCollection() {
        Object[] actual = createCollection(1, 2).toArray();
        assertEquals(2, actual.length);
        assertTrue((int) actual[0] == 1 || (int) actual[1] == 1);
        assertTrue((int) actual[0] == 2 || (int) actual[1] == 2);

    }

    /**
     * Verifies that duplicate elements are removed if a collection with distinct elements is constructed.
     */
    @Test
    public void constructorShouldRemoveDuplicateElementsFromDistinctCollection() {
        Collection<Integer> collection = createCollection(DISTINCT_ELEMENTS, 1, 2, 2, THREE, 2, THREE);
        assertEquals(THREE, collection.size());
        assertEquals(THREE, collection.toArray().length);
        assertTrue(collection.contains(1));
        assertTrue(collection.contains(2));
        assertTrue(collection.contains(THREE));
    }

    /**
     * Verifies that duplicate elements are not removed if a collection with duplicate elements is constructed.
     */
    @Test
    public void constructorShouldNotRemoveDuplicateElementsFromDuplicateCollection() {
        Collection<Integer> collection = createCollection(DUPLICATE_ELEMENTS, 1, 2, 2, THREE, 2, THREE);
        assertEquals(SIX, collection.size());
        assertEquals(SIX, collection.toArray().length);
    }

    /**
     * Verifies that by default, a collection can contain duplicate elements.
     */
    @Test
    public void constructorShouldSetElementCardinalityToDuplicateByDefault() {
        assertEquals(DUPLICATE_ELEMENTS, createCollection().getElementCardinality());
    }

    /**
     * Verifies that when distinct elements are requested, the element cardinality is set to distinct elements.
     */
    @Test
    public void constructorShouldSetElementCardinalityToDistinctElementsWhenSpecified() {
        assertEquals(DISTINCT_ELEMENTS, createCollection(DISTINCT_ELEMENTS, 1).getElementCardinality());
    }

    /**
     * Verifies that the constructor removes duplicate elements when a collection is provided.
     */
    @Test
    public void constructorShouldRemoveDuplicateElementsFromConstructor() {
        T collection = createCollection(DUPLICATE_ELEMENTS, 1, 2, 2, THREE, 2, THREE);
        Collection<Integer> clone = createCollection(DISTINCT_ELEMENTS, collection);
        assertEquals(THREE, clone.size());
        assertEquals(THREE, clone.toArray().length);
    }

    /**
     * Verifies that when distinct elements are requested, the element cardinality is set to distinct elements.
     */
    @Test
    public void constructorShouldOverrideElementCardinalityToDistinctElementsWhenSpecified() {
        assertEquals(DISTINCT_ELEMENTS,
                createCollection(DISTINCT_ELEMENTS, createCollection(1, 2, THREE)).getElementCardinality());
    }

    /**
     * Verifies that the collection produces an iterator that produces the correct sum.
     */
    @Test
    public void iteratorShouldProduceCorrectSumInForLoop() {
        int sum = 0;
        for (Integer i : collection123) {
            sum += i;
        }
        assertEquals(SIX, sum);
    }

    /**
     * Verifies that the spliterator has the distinct flag not set for collections with duplicate elements.
     */
    @Test
    public void spliteratorShouldNotSetDistinctFlagForCollectionWithDuplicateElements() {
        assertFalse(createCollection(DUPLICATE_ELEMENTS, 1).spliterator().hasCharacteristics(Spliterator.DISTINCT));
    }

    /**
     * Verifies that the spliterator has the distinct flag set for collections with distinct elements.
     */
    @Test
    public void spliteratorShouldSetDistinctFlagForCollectionWithDistinctElements() {
        assertTrue(createCollection(DISTINCT_ELEMENTS, 1).spliterator().hasCharacteristics(Spliterator.DISTINCT));
    }

    /**
     * Verifies that the collection produces a stream that collects to the correct sum, thus verifying that the
     * spliterator is created correctly.
     */
    @Test
    public void streamShouldProduceAStreamThatCollectsToTheCorrectSum() {
        assertEquals(SIX, collection123.stream().reduce(0, Integer::sum));
    }
}
