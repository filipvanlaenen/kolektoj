package net.filipvanlaenen.kolektoj;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

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
     * Verifies that the correct length is returned for an ordered collection with three elements.
     */
    @Test
    public void sizeShouldReturnThreeForACollectionOfThreeElements() {
        assertEquals(THREE, collection123.size());
    }
}
