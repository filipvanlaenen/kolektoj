package net.filipvanlaenen.kolektoj.hash;

import static net.filipvanlaenen.kolektoj.Map.KeyAndValueCardinality.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import net.filipvanlaenen.kolektoj.Map;
import net.filipvanlaenen.kolektoj.Map.Entry;

/**
 * Unit tests on the {@link net.filipvanlaenen.kolektoj.hash.HashUtilities} class.
 */
public class HashUtilitiesTest {
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
     * An entry with key 1 and value one.
     */
    private static final Entry<Integer, String> ENTRY1 = new Entry<Integer, String>(1, "one");
    /**
     * Another entry with key 1 and value one.
     */
    private static final Entry<Integer, String> OTHER_ENTRY1 = new Entry<Integer, String>(1, "one");
    /**
     * Another entry with key 1 and value bis.
     */
    private static final Entry<Integer, String> ENTRY1BIS = new Entry<Integer, String>(1, "bis");
    /**
     * An entry with key 2 and value two.
     */
    private static final Entry<Integer, String> ENTRY2 = new Entry<Integer, String>(2, "two");
    /**
     * An entry with key 3 and value three.
     */
    private static final Entry<Integer, String> ENTRY3 = new Entry<Integer, String>(3, "three");

    /**
     * Class with an integer field that is returned as its hash code.
     */
    private final class IntegerFieldObject {
        /**
         * The integer field that is returned as the object's hash code.
         */
        private final int field;

        /**
         * Constructor with the integer field as its parameter.
         *
         * @param field The integer field to be returned as hash code.
         */
        IntegerFieldObject(final int field) {
            this.field = field;
        }

        @Override
        public boolean equals(final Object other) {
            return this == other;
        }

        @Override
        public int hashCode() {
            return field;
        }
    }

    /**
     * Verifies that the hash value for <code>null</code> is 0.
     */
    @Test
    public void hashShouldReturnZeroForNull() {
        assertEquals(0, HashUtilities.hash(null, 2));
    }

    /**
     * Verifies that the hash value of an object with hash code 5 for a hash size of 2 is 1.
     */
    @Test
    public void hashShouldReturnHashCodeModuloSize() {
        assertEquals(1, HashUtilities.hash(new IntegerFieldObject(FIVE), 2));
    }

    /**
     * Verifies that the hash value is positive even for objects with a negative hash code.
     */
    @Test
    public void hashShouldBePositive() {
        assertEquals(1, HashUtilities.hash(new IntegerFieldObject(-1), 2));
    }

    /**
     * Verifies that the entries are populated correctly when keys should be distinct. The method is tested through the
     * constructor of <code>HashMap</code>.
     */
    @Test
    public void populateMapFromEntriesShouldProduceCorrectEntriesForDistinctKeys() {
        Map<Integer, String> map =
                new HashMap<Integer, String>(DISTINCT_KEYS, ENTRY1, ENTRY1BIS, OTHER_ENTRY1, ENTRY2, ENTRY3);
        assertEquals(THREE, map.size());
    }

    /**
     * Verifies that the entries are populated correctly when keys can be duplicate but the values must be distinct. The
     * method is tested through the constructor of <code>HashMap</code>.
     */
    @Test
    public void populateMapFromEntriesShouldProduceCorrectEntriesForDuplicateKeysWithDistinctValues() {
        Map<Integer, String> map = new HashMap<Integer, String>(DUPLICATE_KEYS_WITH_DISTINCT_VALUES, ENTRY1, ENTRY1BIS,
                OTHER_ENTRY1, ENTRY2, ENTRY3);
        assertEquals(FOUR, map.size());
    }

    /**
     * Verifies that the entries are populated correctly when keys and the values can be duplicate. The method is tested
     * through the constructor of <code>HashMap</code>.
     */
    @Test
    public void populateMapFromEntriesShouldProduceCorrectEntriesForDuplicateKeysAndValues() {
        Map<Integer, String> map = new HashMap<Integer, String>(DUPLICATE_KEYS_WITH_DUPLICATE_VALUES, ENTRY1, ENTRY1BIS,
                OTHER_ENTRY1, ENTRY2, ENTRY3);
        assertEquals(FIVE, map.size());
    }

    /**
     * Verifies that the hashed entries are populated correctly when keys and the values can be duplicate. The method is
     * tested through the constructor of <code>HashMap</code>.
     */
    @Test
    public void populateMapFromEntriesShouldProduceCorrectHashedEntriesForDuplicateKeysAndValues() {
        Map<Integer, String> map = new HashMap<Integer, String>(DUPLICATE_KEYS_WITH_DUPLICATE_VALUES, ENTRY1, ENTRY1BIS,
                OTHER_ENTRY1, ENTRY2, ENTRY3);
        assertEquals(THREE, map.getAll(1).size());
    }

    /**
     * Verifies that trying to populate a map with an entry that's null throws an <code>IllegalArgumentException</code>.
     */
    @Test
    public void populateMapFromEntriesShouldThroughIllegalArgumentExceptionForNullEntry() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> new HashMap<Integer, String>(ENTRY1, null, ENTRY2, ENTRY3));
        assertEquals("Map entries can't be null.", exception.getMessage());
    }
}
