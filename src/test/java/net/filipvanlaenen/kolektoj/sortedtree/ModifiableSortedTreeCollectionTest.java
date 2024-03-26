package net.filipvanlaenen.kolektoj.sortedtree;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Comparator;
import java.util.Objects;

import org.junit.jupiter.api.Test;

import net.filipvanlaenen.kolektoj.Collection.ElementCardinality;
import net.filipvanlaenen.kolektoj.Collection;
import net.filipvanlaenen.kolektoj.ModifiableCollection;
import net.filipvanlaenen.kolektoj.ModifiableCollectionTestBase;
import net.filipvanlaenen.kolektoj.ModifiableSortedCollection;

/**
 * Unit tests on the {@link net.filipvanlaenen.kolektoj.sortedtree.ModifiableSortedTreeCollection} class.
 */
public final class ModifiableSortedTreeCollectionTest
        extends ModifiableCollectionTestBase<ModifiableSortedTreeCollection<Integer>> {
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
     * A comparator ordering integers in the natural order, but in addition handling <code>null</code> as the lowest
     * value.
     */
    private static final Comparator<Integer> COMPARATOR = new Comparator<Integer>() {
        @Override
        public int compare(final Integer i1, final Integer i2) {
            if (Objects.equals(i1, i2)) {
                return 0;
            } else if (i1 == null) {
                return -1;
            } else if (i2 == null) {
                return 1;
            } else if (i1 < i2) {
                return -1;
            } else {
                return 1;
            }
        }
    };

    @Override
    protected ModifiableSortedTreeCollection<Integer> createModifiableCollection(final Integer... integers) {
        return new ModifiableSortedTreeCollection<Integer>(COMPARATOR, integers);
    }

    @Override
    protected ModifiableSortedTreeCollection<Integer> createModifiableCollection(
            final ElementCardinality elementCardinality, final Integer... integers) {
        return new ModifiableSortedTreeCollection<Integer>(elementCardinality, COMPARATOR, integers);
    }

    /**
     * Creates a new modifiable sorted collection to run the unit tests on.
     *
     * @return A new modifiable sorted collection to run the unit tests on.
     */
    private ModifiableSortedCollection<Integer> createCollection513() {
        return createModifiableCollection(FIVE, 1, THREE);
    }

    /**
     * Verifies that adding element happens according to the comparator.
     */
    @Test
    public void addShouldInsertAnNewElementAccordingToSorting() {
        ModifiableCollection<Integer> collection = createCollection513();
        collection.add(2);
        assertArrayEquals(new Integer[] {1, 2, THREE, FIVE}, collection.toArray());
    }

    /**
     * Verifies that adding elements from a collection happens according to the comparator.
     */
    @Test
    public void addAllShouldInsertAnNewElementsAccordingToSorting() {
        ModifiableCollection<Integer> collection = createCollection513();
        collection.addAll(Collection.of(2, FOUR));
        assertArrayEquals(new Integer[] {1, 2, THREE, FOUR, FIVE}, collection.toArray());
    }

    /**
     * Verifies that getting an element at an index occurs according to sorting.
     */
    @Test
    public void getAtShouldReturnElementAtIndexAfterSorting() {
        assertEquals(THREE, createCollection513().getAt(1));
    }

    /**
     * Verifies that removing an element at an index occurs according to sorting.
     */
    @Test
    public void removeAtShouldReturnElementAtIndexAfterSorting() {
        assertEquals(THREE, createCollection513().removeAt(1));
    }

    /**
     * Verifies that removing an element at an index occurs according to sorting.
     */
    @Test
    public void removeAtShouldRemoveAtIndexAfterSorting() {
        ModifiableSortedCollection<Integer> collection = createCollection513();
        collection.removeAt(1);
        assertArrayEquals(new Integer[] {1, FIVE}, collection.toArray());
    }

    /**
     * Verifies that trying to remove an element at an index beyond the size of the collection throws
     * IndexOutOfBoundsException.
     */
    @Test
    public void removeAtShouldThrowExceptionWhenCalledBeyondCollectionSize() {
        IndexOutOfBoundsException exception =
                assertThrows(IndexOutOfBoundsException.class, () -> createCollection513().removeAt(THREE));
        assertEquals("Cannot remove an element at a position beyond the size of the collection.",
                exception.getMessage());
    }
}
