package net.filipvanlaenen.kolektoj;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * Unit tests on implementations of the {@link net.filipvanlaenen.kolektoj.ModifiableOrderedCollection} interface.
 *
 * @param <T> The subclass type to be tested.
 */
public abstract class ModifiableOrderedCollectionTestBase<T extends ModifiableOrderedCollection<Integer>>
        extends ModifiableCollectionTestBase<T> {
    /**
     * Ordered collection with the integers 1, 2 and 3.
     */
    private final T collection123 = createModifiableOrderedCollection(1, 2, 3);

    /**
     * Creates a modifiable ordered collection containing the provided integers.
     *
     * @param integers The integers to be included in the modifiable ordered collection.
     * @return A modifiable ordered collection containing the provided integers.
     */
    protected abstract T createModifiableOrderedCollection(Integer... integers);

    /**
     * Verifies that when you get an element from a collection by its index, the correct element is returned.
     */
    @Test
    public void getAtShouldReturnCorrectElement() {
        assertEquals(2, collection123.getAt(1));
    }
}
