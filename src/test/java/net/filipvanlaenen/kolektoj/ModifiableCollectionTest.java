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
     * Collection with the integer 1.
     */
    private final ModifiableCollection<Integer> collection1 = ModifiableCollection.of(1);
    /**
     * Collection with the integers 1, 2 and 3.
     */
    private final ModifiableCollection<Integer> collection123 = ModifiableCollection.of(1, 2, THREE);

    /**
     * Verifies that the intersection of no collections is empty.
     */
    @Test
    public void intersectionOfNoCollectionsShouldBeEmpty() {
        assertTrue(ModifiableCollection.intersectionOf().isEmpty());
    }

    /**
     * Verifies that the intersection of one collection is that collection.
     */
    @Test
    public void intersectionOfOneCollectionShouldBeTheSameCollection() {
        assertTrue(collection1.containsSame(ModifiableCollection.intersectionOf(collection1)));
    }

    /**
     * Verifies that the intersection of three collections only contains the common elements.
     */
    @Test
    public void intersectionOfThreeCollectionsShouldOnlyContainTheCommonElements() {
        assertTrue(collection1
                .containsSame(ModifiableCollection.intersectionOf(collection123, collection1, Collection.of(1, 2))));
    }

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
        assertFalse(collection1.isEmpty());
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
        ModifiableCollection<Number> clone = ModifiableCollection.<Number>of(collection123);
        assertTrue(clone.containsSame(collection123));
    }

    /**
     * Verifies that the of factory method using a collection and element cardinality clones a collection.
     */
    @Test
    public void ofWithElementCardinalityAndCollectionShoudlReturnAClone() {
        Collection<Integer> collection = Collection.<Integer>of(1, 1, 2, THREE);
        ModifiableCollection<Number> clone = ModifiableCollection.<Number>of(DISTINCT_ELEMENTS, collection);
        assertEquals(DISTINCT_ELEMENTS, clone.getElementCardinality());
        assertTrue(clone.containsSame(collection123));
    }
}
