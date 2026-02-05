package net.filipvanlaenen.kolektoj;

import static net.filipvanlaenen.kolektoj.Collection.ElementCardinality.DISTINCT_ELEMENTS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * Unit tests on the {@link net.filipvanlaenen.kolektoj.array.ModifiableCollection} class.
 */
public class ModifiableCollectionTest {
    /**
     * The magic number three.
     */
    private static final int THREE = 3;

    /**
     * Verifies that an empty modifiable collection is empty.
     */
    @Test
    public void isEmptyShouldReturnTrueForAnEmptyCollection() {
        assertTrue(ModifiableCollection.empty().isEmpty());
    }

    /**
     * Verifies that a collection containing an element is not empty.
     */
    @Test
    public void isEmptyShouldReturnFalseForACollectionContainingAnElement() {
        assertFalse(ModifiableCollection.of(1).isEmpty());
    }

    /**
     * Verifies that a modifiable collection with a specific element cardinality receives that element cardinality.
     */
    @Test
    public void ofWithElementCardinalityShouldReturnACollectionWithTheElementCardinality() {
        assertEquals(DISTINCT_ELEMENTS, ModifiableCollection.of(DISTINCT_ELEMENTS, 1).getElementCardinality());
    }

    /**
     * Verifies that the of factory method using a collection clones a collection.
     */
    @Test
    public void ofWithCollectionShoudlReturnAClone() {
        Collection<Integer> collection = Collection.<Integer>of(1, 2, THREE);
        ModifiableCollection<Number> clone = ModifiableCollection.<Number>of(collection);
        assertTrue(clone.containsSame(collection));
    }

    /**
     * Verifies that the of factory method using a collection and element cardinality clones a collection.
     */
    @Test
    public void ofWithElementCardinalityAndCollectionShoudlReturnAClone() {
        Collection<Integer> collection = Collection.<Integer>of(1, 1, 2, THREE);
        ModifiableCollection<Number> clone = ModifiableCollection.<Number>of(DISTINCT_ELEMENTS, collection);
        assertEquals(DISTINCT_ELEMENTS, clone.getElementCardinality());
        assertTrue(clone.containsSame(Collection.<Integer>of(1, 2, THREE)));
    }
}
