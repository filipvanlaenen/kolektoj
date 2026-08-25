package net.filipvanlaenen.kolektoj;

import static net.filipvanlaenen.kolektoj.Collection.ElementCardinality.DISTINCT_ELEMENTS;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

/**
 * Unit tests on the {@link net.filipvanlaenen.kolektoj.array.ModifiableOrderedCollection} class.
 */
public class ModifiableOrderedCollectionTest {
    /**
     * The magic number three.
     */
    private static final int THREE = 3;
    /**
     * The magic number four.
     */
    private static final int FOUR = 4;
    /**
     * The magic number five.
     */
    private static final int FIVE = 5;
    /**
     * Collection with the integer 1.
     */
    private final ModifiableOrderedCollection<Integer> collection1 = ModifiableOrderedCollection.of(1);
    /**
     * Collection with the integer 1 and 2.
     */
    private final OrderedCollection<Integer> collection12 = OrderedCollection.of(1, 2);
    /**
     * Collection with the integers 1, 2 and 3.
     */
    private final ModifiableOrderedCollection<Integer> collection123 = ModifiableOrderedCollection.of(1, 2, THREE);

    /**
     * Verifies that the difference of one collection is that collection.
     */
    @Test
    public void differenceOfOneCollectionShouldBeTheSameCollection() {
        assertTrue(collection1.containsSame(ModifiableOrderedCollection.differenceOf(collection1)));
    }

    /**
     * Verifies that the difference of three collections only contains the elements of the first collection that aren't
     * present in any of the other.
     */
    @Test
    public void differenceOfThreeCollectionsShouldOnlyContainTheElementsFromTheFirstCollectionNotInTheOthers() {
        assertTrue(Collection.of(3)
                .containsSame(ModifiableOrderedCollection.differenceOf(collection123, collection1, collection12)));
    }

    /**
     * Verifies that the intersection of one collection is that collection.
     */
    @Test
    public void intersectionOfOneCollectionShouldBeTheSameCollection() {
        assertTrue(collection1.containsSame(ModifiableOrderedCollection.intersectionOf(collection1)));
    }

    /**
     * Verifies that the intersection of three collections only contains the common elements.
     */
    @Test
    public void intersectionOfThreeCollectionsShouldOnlyContainTheCommonElements() {
        assertTrue(collection1
                .containsSame(ModifiableOrderedCollection.intersectionOf(collection123, collection1, collection12)));
    }

    /**
     * Verifies that an empty modifiable ordered collection is empty.
     */
    @Test
    public void isEmptyShouldReturnTrueForAnEmptyCollection() {
        assertTrue(ModifiableOrderedCollection.empty().isEmpty());
    }

    /**
     * Verifies that a collection containing an element is not empty.
     */
    @Test
    public void isEmptyShouldReturnFalseForACollectionContainingAnElement() {
        assertFalse(collection1.isEmpty());
    }

    /**
     * Verifies that a modifiable ordered collection with a specific element cardinality receives that element
     * cardinality.
     */
    @Test
    public void ofWithElementCardinalityShouldReturnACollectionWithTheElementCardinality() {
        assertEquals(DISTINCT_ELEMENTS, ModifiableOrderedCollection.of(DISTINCT_ELEMENTS, 1).getElementCardinality());
    }

    /**
     * Verifies that a modifiable ordered collection with a specific element cardinality receives that element
     * cardinality.
     */
    @Test
    public void ofWithElementCardinalityAndCollectionShouldReturnACollectionWithTheElementCardinality() {
        ModifiableOrderedCollection<Number> clone =
                ModifiableOrderedCollection.of(DISTINCT_ELEMENTS, OrderedCollection.of(1, 1));
        assertEquals(DISTINCT_ELEMENTS, clone.getElementCardinality());
        assertEquals(1, clone.size());
    }

    /**
     * Verifies that the of factory method using a collection clones a collection.
     */
    @Test
    public void ofWithCollectionShouldReturnAClone() {
        ModifiableOrderedCollection<Number> clone = ModifiableOrderedCollection.<Number>of(collection123);
        assertArrayEquals(collection123.toArray(), clone.toArray());
    }

    /**
     * Verifies that the of factory method using a collection and from and to indices clones a collection.
     */
    @Test
    public void ofWithCollectionAndIndicesShoudlReturnAClone() {
        OrderedCollection<Integer> collection = OrderedCollection.<Integer>of(1, 2, THREE, FOUR, FIVE);
        ModifiableOrderedCollection<Number> slice = ModifiableOrderedCollection.<Number>of(collection, 1, THREE);
        assertTrue(slice.containsSame(Collection.of(2, THREE)));
    }

    /**
     * Verifies that <code>addAllFirst</code> adds the elements to the start of an ordered collection.
     */
    @Test
    public void addAllFirstShouldAddElementToStart() {
        ModifiableOrderedCollection<Integer> collection = ModifiableOrderedCollection.of(1, 2);
        assertTrue(collection.addAllFirst(OrderedCollection.of(THREE, FOUR)));
        assertArrayEquals(new Integer[] {THREE, FOUR, 1, 2}, collection.toArray());
    }

    /**
     * Verifies that <code>addAllFirst</code> returns <code>false</code> when trying to add duplicate elements.
     */
    @Test
    public void addAllFirstShouldReturnFalseWhenAddingDuplicateElements() {
        ModifiableOrderedCollection<Integer> collection =
                ModifiableOrderedCollection.of(DISTINCT_ELEMENTS, 1, 2, THREE);
        assertFalse(collection.addAllFirst(OrderedCollection.of(THREE)));
        assertArrayEquals(new Integer[] {1, 2, THREE}, collection.toArray());
    }

    /**
     * Verifies that <code>addAllLast</code> adds the elements to the end of an ordered collection.
     */
    @Test
    public void addAllLastShouldAddElementToEnd() {
        ModifiableOrderedCollection<Integer> collection = ModifiableOrderedCollection.of(1, 2);
        assertTrue(collection.addAllLast(OrderedCollection.of(THREE, FOUR)));
        assertArrayEquals(new Integer[] {1, 2, THREE, FOUR}, collection.toArray());
    }

    /**
     * Verifies that <code>addAllLast</code> returns <code>false</code> when trying to add duplicate elements.
     */
    @Test
    public void addAllLastShouldReturnFalseWhenAddingDuplicateElements() {
        ModifiableOrderedCollection<Integer> collection =
                ModifiableOrderedCollection.of(DISTINCT_ELEMENTS, 1, 2, THREE);
        assertFalse(collection.addAllLast(OrderedCollection.of(1)));
        assertArrayEquals(new Integer[] {1, 2, THREE}, collection.toArray());
    }

    /**
     * Verifies that addFirst adds an element to the start of an ordered collection.
     */
    @Test
    public void addFirstShouldAddElementToStart() {
        OrderedCollection<Integer> expected = OrderedCollection.of(FOUR, 1, 2, THREE);
        ModifiableOrderedCollection<Integer> actual = ModifiableOrderedCollection.of(1, 2, THREE);
        assertTrue(actual.addFirst(FOUR));
        assertTrue(actual.containsSame(expected));
    }

    /**
     * Verifies that addFirst doesn't add an element to the start of an ordered collection with distinct elements if the
     * element is already present.
     */
    @Test
    public void addFirstShouldNotAddDuplicateElementToStart() {
        OrderedCollection<Integer> expected = OrderedCollection.of(1, 2, THREE);
        ModifiableOrderedCollection<Integer> actual = ModifiableOrderedCollection.of(DISTINCT_ELEMENTS, 1, 2, THREE);
        assertFalse(actual.addFirst(THREE));
        assertTrue(actual.containsSame(expected));
    }

    /**
     * Verifies that addLast adds an element to the end of an ordered collection.
     */
    @Test
    public void addLastShouldAddElementToEnd() {
        OrderedCollection<Integer> expected = OrderedCollection.of(1, 2, THREE, FOUR);
        ModifiableOrderedCollection<Integer> actual = ModifiableOrderedCollection.of(1, 2, THREE);
        assertTrue(actual.addLast(FOUR));
        assertTrue(actual.containsSame(expected));
    }

    /**
     * Verifies that addLast doesn't add an element to the end of an ordered collection with distinct elements if the
     * element is already present.
     */
    @Test
    public void addLastShouldNotAddDuplicateElementToEnd() {
        OrderedCollection<Integer> expected = OrderedCollection.of(1, 2, THREE);
        ModifiableOrderedCollection<Integer> actual = ModifiableOrderedCollection.of(DISTINCT_ELEMENTS, 1, 2, THREE);
        assertFalse(actual.addLast(1));
        assertTrue(actual.containsSame(expected));
    }

    /**
     * Verifies that removeFirst removes the first element of an ordered collection.
     */
    @Test
    public void removeFirstShouldRemoveElementAtStart() {
        OrderedCollection<Integer> expected = OrderedCollection.of(2, THREE);
        ModifiableOrderedCollection<Integer> actual = ModifiableOrderedCollection.of(1, 2, THREE);
        assertEquals(1, actual.removeFirst());
        assertTrue(actual.containsSame(expected));
    }

    /**
     * Verifies that removeLast removes the last element of an ordered collection.
     */
    @Test
    public void removeLastShouldRemoveElementAtEnd() {
        OrderedCollection<Integer> expected = OrderedCollection.of(1, 2);
        ModifiableOrderedCollection<Integer> actual = ModifiableOrderedCollection.of(1, 2, THREE);
        assertEquals(THREE, actual.removeLast());
        assertTrue(actual.containsSame(expected));
    }

    /**
     * Verifies that the union of no collections is empty.
     */
    @Test
    public void unionOfNoCollectionsShouldBeEmpty() {
        assertTrue(ModifiableOrderedCollection.unionOf().isEmpty());
    }

    /**
     * Verifies that the union of one collection is that collection.
     */
    @Test
    public void unionOfOneCollectionShouldBeTheSameCollection() {
        assertTrue(collection1.containsSame(ModifiableOrderedCollection.unionOf(collection1)));
    }

    /**
     * Verifies that the union of three collections only all elements.
     */
    @Test
    public void unionOfThreeCollectionsShouldContainAllElements() {
        assertArrayEquals(Collection.of(1, 2, THREE, 1, 1, 2).toArray(),
                ModifiableOrderedCollection.unionOf(collection123, collection1, collection12).toArray());
    }

    /**
     * Verifies that the union of no collections is empty.
     */
    @Test
    public void unionOfNoCollectionsWithDistinctElementsShouldBeEmpty() {
        assertTrue(ModifiableOrderedCollection.unionOf(DISTINCT_ELEMENTS).isEmpty());
    }

    /**
     * Verifies that the union of one collection is that collection.
     */
    @Test
    public void unionOfOneCollectionWithDistinctElementsShouldBeTheSameCollection() {
        assertTrue(collection1.containsSame(ModifiableOrderedCollection.unionOf(DISTINCT_ELEMENTS, collection1)));
    }

    /**
     * Verifies that the union of three collections only all elements.
     */
    @Test
    public void unionOfThreeCollectionsWithDistinctElementsShouldContainAllDistinctElements() {
        assertArrayEquals(collection123.toArray(), ModifiableOrderedCollection
                .unionOf(DISTINCT_ELEMENTS, collection123, collection1, collection12).toArray());
    }

}
