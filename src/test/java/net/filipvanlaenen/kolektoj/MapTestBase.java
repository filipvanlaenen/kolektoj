package net.filipvanlaenen.kolektoj;

import static net.filipvanlaenen.kolektoj.Collection.ElementCardinality.DISTINCT_ELEMENTS;
import static net.filipvanlaenen.kolektoj.Collection.ElementCardinality.DUPLICATE_ELEMENTS;
import static net.filipvanlaenen.kolektoj.Map.KeyAndValueCardinality.DISTINCT_KEYS;
import static net.filipvanlaenen.kolektoj.Map.KeyAndValueCardinality.DUPLICATE_KEYS_WITH_DISTINCT_VALUES;
import static net.filipvanlaenen.kolektoj.Map.KeyAndValueCardinality.DUPLICATE_KEYS_WITH_DUPLICATE_VALUES;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Comparator;
import java.util.Objects;
import java.util.Spliterator;

import org.junit.jupiter.api.Test;

import net.filipvanlaenen.kolektoj.Map.Entry;
import net.filipvanlaenen.kolektoj.Map.KeyAndValueCardinality;
import net.filipvanlaenen.kolektoj.MapTestBase.KeyWithCollidingHash;

/**
 * Unit tests on implementations of the {@link net.filipvanlaenen.kolektoj.Map} interface.
 *
 * @param <T>  The subclass type to be tested.
 * @param <TC> The subclass type to be tested, but with a key type with colliding hash values.
 */
public abstract class MapTestBase<T extends Map<Integer, String>, TC extends Map<KeyWithCollidingHash, Integer>> {
    /**
     * The magic number three.
     */
    private static final int THREE = 3;
    /**
     * The magic number six.
     */
    private static final int SIX = 6;
    /**
     * An entry with key null and value null.
     */
    private static final Entry<Integer, String> ENTRY_NULL = new Entry<Integer, String>(null, null);
    /**
     * An entry with key 1 and value one.
     */
    protected static final Entry<Integer, String> ENTRY1 = new Entry<Integer, String>(1, "one");
    /**
     * An entry with key 1 and value bis.
     */
    protected static final Entry<Integer, String> ENTRY1BIS = new Entry<Integer, String>(1, "bis");
    /**
     * An entry with key 2 and value two.
     */
    protected static final Entry<Integer, String> ENTRY2 = new Entry<Integer, String>(2, "two");
    /**
     * An entry with key 3 and value three.
     */
    protected static final Entry<Integer, String> ENTRY3 = new Entry<Integer, String>(3, "three");
    /**
     * An empty map.
     */
    private final Map<Integer, String> empty = createMap();
    /**
     * Map with the integers 1, 2 and 3 mapped to their words.
     */
    private final Map<Integer, String> map123 = createMap(ENTRY1, ENTRY2, ENTRY3);
    /**
     * Map with the integers 1, 2 and 3 mapped to their words, and null to null.
     */
    private final Map<Integer, String> map123null = createMap(ENTRY1, ENTRY2, ENTRY3, ENTRY_NULL);

    /**
     * Class with colliding hash codes.
     */
    public static final class KeyWithCollidingHash {
        /**
         * The value for the key.
         */
        private final int value;

        /**
         * Constructor taking the key value as its parameter.
         *
         * @param value The key value.
         */
        public KeyWithCollidingHash(final int value) {
            this.value = value;
        }

        @Override
        public boolean equals(final Object other) {
            return other != null && getValue() == ((KeyWithCollidingHash) other).getValue();
        }

        /**
         * Returns the key value.
         *
         * @return The key value.
         */
        private int getValue() {
            return value;
        }

        @Override
        public int hashCode() {
            return 0;
        }
    }

    /**
     * A comparator returning that all KeyWithCollidingHash objects are equal.
     */
    protected static final Comparator<KeyWithCollidingHash> KEY_WITH_COLLIDING_HASH_COMPARATOR =
            new Comparator<KeyWithCollidingHash>() {
                @Override
                public int compare(final KeyWithCollidingHash k1, final KeyWithCollidingHash k2) {
                    if (Objects.equals(k1, k2)) {
                        return 0;
                    } else if (k1 == null) {
                        return -1;
                    } else if (k2 == null) {
                        return 1;
                    } else {
                        return k2.getValue() - k1.getValue();
                    }
                }
            };

    /**
     * Creates a map containing the provided entries.
     *
     * @param entries The entries to be included in the map.
     * @return A map containing the provided entries.
     */
    protected abstract T createMap(Entry<Integer, String>... entries);

    /**
     * Creates a map containing the provided entries with a key and value cardinality.
     *
     * @param keyAndValueCardinality The key and value cardinality requested.
     * @param entries                The entries to be included in the map.
     * @return A map containing the provided entries with the key and value cardinality.
     */
    protected abstract T createMap(KeyAndValueCardinality keyAndValueCardinality, Entry<Integer, String>... entries);

    /**
     * Creates a map using keys with colliding hashes containing the provided entries.
     *
     * @param entries The entries to be included in the map.
     * @return A map containing the provided entries.
     */
    protected abstract TC createCollidingKeyHashMap(Entry<KeyWithCollidingHash, Integer>... entries);

    /**
     * Verifies that trying to pass null as an argument to the constructor throws IllegalArgumentException.
     */
    @Test
    public void constructorShouldThrowExceptionIfNullIsPassedAsAnArgument() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> createMap(null));
        assertEquals("Map entries can't be null.", exception.getMessage());
    }

    /**
     * Verifies that trying to pass null as one of many arguments to the constructor throws IllegalArgumentException.
     */
    @Test
    public void constructorShouldThrowExceptionIfNullIsPassedAsOneOfTheArguments() {
        IllegalArgumentException exception =
                assertThrows(IllegalArgumentException.class, () -> createMap(ENTRY1, null));
        assertEquals("Map entries can't be null.", exception.getMessage());
    }

    /**
     * Verifies that the correct length is returned for a map with three entries.
     */
    @Test
    public void sizeShouldReturnThreeForAMapOfThreeEntries() {
        assertEquals(THREE, map123.size());
    }

    /**
     * Verifies that contains returns false for a empty map.
     */
    @Test
    public void containsShouldReturnFalseForAnEmptyMap() {
        assertFalse(empty.contains(ENTRY1));
    }

    /**
     * Verifies that contains returns true for an entry in the map.
     */
    @Test
    public void containsShouldReturnTrueForAnEntryInTheMap() {
        assertTrue(map123.contains(ENTRY1));
    }

    /**
     * Verifies that contains returns true for an entry in a map with duplicate keys.
     */
    @Test
    public void containsShouldReturnTrueForAnEntryInAMapWithDuplicateKeys() {
        Map<Integer, String> map = createMap(DUPLICATE_KEYS_WITH_DISTINCT_VALUES, ENTRY1, ENTRY1BIS, ENTRY3);
        assertTrue(map.contains(ENTRY1));
        assertTrue(map.contains(ENTRY1BIS));
    }

    /**
     * Verifies that contains returns false for an entry not in the map.
     */
    @Test
    public void containsShouldReturnFalseForAnEntryNotInTheMap() {
        assertFalse(map123.contains(new Entry<Integer, String>(0, "zero")));
    }

    /**
     * Verifies that containsKey returns false for a empty map.
     */
    @Test
    public void containsKeyShouldReturnFalseForAnEmptyMap() {
        assertFalse(empty.containsKey(1));
    }

    /**
     * Verifies that containsKey returns true for a key in the map.
     */
    @Test
    public void containsKeyShouldReturnTrueForAKeyInTheMap() {
        assertTrue(map123.containsKey(1));
    }

    /**
     * Verifies that containsKey returns false for a key not in the map.
     */
    @Test
    public void containsKeyShouldReturnFalseForAKeyNotInTheMap() {
        assertFalse(map123.containsKey(0));
    }

    /**
     * Verifies that containsKey returns false for null if not in the map.
     */
    @Test
    public void containsKeyShouldReturnFalseForNullIfNotInTheMap() {
        assertFalse(map123.containsKey(null));
    }

    /**
     * Verifies that containsValue returns true for a value in the map.
     */
    @Test
    public void containsValueShouldReturnTrueForAValueInTheMap() {
        assertTrue(map123.containsValue("one"));
    }

    /**
     * Verifies that containsValue returns true for null if in the map.
     */
    @Test
    public void containsValueShouldReturnTrueForNullIfInTheMap() {
        assertTrue(map123null.containsValue(null));
    }

    /**
     * Verifies that containsValue returns false for a value not in the map.
     */
    @Test
    public void containsValueShouldReturnFalseForAValueNotInTheMap() {
        assertFalse(map123.containsValue("zero"));
    }

    /**
     * Verifies that trying to get an element from an empty map throws IndexOutOfBoundsException.
     */
    @Test
    public void getShouldThrowExceptionWhenCalledOnAnEmptyMap() {
        IndexOutOfBoundsException exception = assertThrows(IndexOutOfBoundsException.class, () -> createMap().get());
        assertEquals("Cannot return an entry from an empty map.", exception.getMessage());
    }

    /**
     * Verifies that when you get an entry from a map, the map contains it.
     */
    @Test
    public void getShouldReturnAnEntryPresentInTheMap() {
        Entry<Integer, String> entry = map123.get();
        assertTrue(map123.contains(entry));
    }

    /**
     * Verifies that when you try to use get with a key not in the map, an IllegalArgumentException is thrown.
     */
    @Test
    public void getShouldThrowExceptionWhenCalledWithAbsentKey() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> map123.get(0));
        assertEquals("Map doesn't contain an entry with the key 0.", exception.getMessage());
    }

    /**
     * Verifies that when you try to use get with a key in the map, the value is returned.
     */
    @Test
    public void getShouldReturnValueForKey() {
        assertEquals("one", map123.get(1));
    }

    /**
     * Verifies that when you try to use getAll with a key not in the map, an IllegalArgumentException is thrown.
     */
    @Test
    public void getAllShouldThrowExceptionWhenCalledWithAbsentKey() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> map123.getAll(0));
        assertEquals("Map doesn't contain entries with the key 0.", exception.getMessage());
    }

    /**
     * Verifies that when you try to use getAll with a key in the map, a collection with the value is returned.
     */
    @Test
    public void getAllShouldReturnSingleValueForKey() {
        Collection<String> actual = map123.getAll(1);
        assertEquals(1, actual.size());
        assertTrue(actual.contains("one"));
    }

    /**
     * Verifies that when you try to use getAll with key null, a collection with the value for null is returned.
     */
    @Test
    public void getAllShouldReturnSingleValueForNull() {
        Collection<String> actual = map123null.getAll(null);
        assertEquals(1, actual.size());
        assertTrue(actual.contains(null));
    }

    /**
     * Verifies that getKeys returns all the keys.
     */
    @Test
    public void getKeysShouldReturnAllKeys() {
        Collection<Integer> actual = map123.getKeys();
        assertEquals(THREE, actual.size());
        assertTrue(actual.contains(1));
        assertTrue(actual.contains(2));
        assertTrue(actual.contains(THREE));
        assertEquals(DISTINCT_ELEMENTS, actual.getElementCardinality());
    }

    /**
     * Verifies that when you try to use getAll with a key in the map that has multiple values, a collection with the
     * values is returned.
     */
    @Test
    public void getAllShouldReturnManyValuesForKey() {
        Map<Integer, String> map = createMap(DUPLICATE_KEYS_WITH_DISTINCT_VALUES, ENTRY1, ENTRY1BIS, ENTRY3);
        Collection<String> actual = map.getAll(1);
        assertEquals(2, actual.size());
        assertTrue(actual.contains("one"));
        assertTrue(actual.contains("bis"));
        assertEquals(DISTINCT_ELEMENTS, actual.getElementCardinality());
    }

    /**
     * Verifies that when you try to use getAll with a key in the map that has duplicate values, a collection with the
     * values is returned.
     */
    @Test
    public void getAllShouldReturnDuplicateValuesForKey() {
        Map<Integer, String> map = createMap(DUPLICATE_KEYS_WITH_DUPLICATE_VALUES, ENTRY1,
                new Entry<Integer, String>(1, "one"), ENTRY1BIS, ENTRY3);
        Collection<String> actual = map.getAll(1);
        assertEquals(THREE, actual.size());
        assertTrue(actual.contains("one"));
        assertTrue(actual.contains("bis"));
        assertEquals(DUPLICATE_ELEMENTS, actual.getElementCardinality());
    }

    /**
     * Verifies that when you try to use getAll with a key in the map that has all the values, a collection with all the
     * values is returned.
     */
    @Test
    public void getAllShouldReturnAllValuesForSingleKey() {
        Map<Integer, String> map = createMap(DUPLICATE_KEYS_WITH_DUPLICATE_VALUES, ENTRY1,
                new Entry<Integer, String>(1, "one"), ENTRY1BIS);
        Collection<String> actual = map.getAll(1);
        assertEquals(THREE, actual.size());
        assertTrue(actual.contains("one"));
        assertTrue(actual.contains("bis"));
        assertEquals(DUPLICATE_ELEMENTS, actual.getElementCardinality());
    }

    /**
     * Verifies that the collection returned by getKeys has distinct elements as its cardinality for a map with distinct
     * keys.
     */
    @Test
    public void getKeysShouldReturnCollectionWithDistinctElementsForMapWithDistinctKeys() {
        Map<Integer, String> map = createMap(DISTINCT_KEYS, ENTRY1, ENTRY2, ENTRY3);
        assertEquals(DISTINCT_ELEMENTS, map.getKeys().getElementCardinality());
    }

    /**
     * Verifies that the collection returned by getKeys has duplicate elements as its cardinality for a map with
     * duplicate keys.
     */
    @Test
    public void getKeysShouldReturnCollectionWithDuplicateElementsForMapWithDuplicateKeys() {
        Map<Integer, String> map = createMap(DUPLICATE_KEYS_WITH_DISTINCT_VALUES, ENTRY1, ENTRY2, ENTRY3);
        assertEquals(DUPLICATE_ELEMENTS, map.getKeys().getElementCardinality());
    }

    /**
     * Verifies that getValues returns all the values.
     */
    @Test
    public void getValuesShouldReturnAllValues() {
        Collection<String> actual = map123.getValues();
        assertEquals(THREE, actual.size());
        assertTrue(actual.contains("one"));
        assertTrue(actual.contains("two"));
        assertTrue(actual.contains("three"));
    }

    /**
     * Verifies that the map produces an array with the entries.
     */
    @Test
    public void toArrayShouldProduceAnArrayWithTheEntriesOfTheMap() {
        Map<Integer, String> map = createMap(ENTRY1, ENTRY2);
        Object[] actual = map.toArray();
        assertEquals(2, actual.length);
        assertTrue(actual[0] == ENTRY1 || actual[1] == ENTRY1);
        assertTrue(actual[0] == ENTRY2 || actual[1] == ENTRY2);
    }

    /**
     * Verifies that the map produces a stream that reduces to the correct key sum, thus verifying that the spliterator
     * is created correctly.
     */
    @Test
    public void streamShouldProduceAStreamThatReducesToTheCorrectSum() {
        assertEquals(SIX, map123.stream().map(e -> e.key()).reduce(0, Integer::sum));
    }

    /**
     * Verifies that the map produces an iterator that when used in a for loop, produces the correct key sum.
     */
    @Test
    public void iteratorShouldProduceCorrectSumInForLoop() {
        int sum = 0;
        for (Entry<Integer, String> entry : map123) {
            sum += entry.key();
        }
        assertEquals(SIX, sum);
    }

    /**
     * Verifies that by default, a hash map has key and value cardinality <code>DISTINCT_KEYS</code>.
     */
    @Test
    public void keyAndValueCardinalityShouldBeDistinctKeysByDefault() {
        assertEquals(DISTINCT_KEYS, createMap().getKeyAndValueCardinality());
    }

    /**
     * Verifies that when another key and value cardinality is specified, getKeyAndValueCardinality returns the
     * specified one.
     */
    @Test
    public void getKeyAndValueCardinalityShouldBeWiredCorrectly() {
        assertEquals(DUPLICATE_KEYS_WITH_DUPLICATE_VALUES,
                createMap(DUPLICATE_KEYS_WITH_DUPLICATE_VALUES).getKeyAndValueCardinality());
    }

    /**
     * Verifies that containsAll returns true when a map is compared with itself.
     */
    @Test
    public void containsAllShouldReturnTrueWhenAMapIsComparedWithItself() {
        assertTrue(map123.containsAll(map123));
    }

    /**
     * Verifies that containsAll returns false when a map doesn't contain all the entries of the map is is compared
     * with.
     */
    @Test
    public void containsAllShouldReturnFalseWhenAMapContainsOtherEntries() {
        assertFalse(map123.containsAll(map123null));
    }

    /**
     * Verifies that contains returns the correct result, both true for presence and false for absence, when the hash
     * code for the keys collides.
     */
    @Test
    public void containsReturnsCorrectResultForCollidingKeyHashCodes() {
        Entry<KeyWithCollidingHash, Integer>[] entries = new Entry[SIX];
        for (int i = 0; i < entries.length; i++) {
            entries[i] = new Entry<KeyWithCollidingHash, Integer>(new KeyWithCollidingHash(i), i);
        }
        Map<KeyWithCollidingHash, Integer> map = createCollidingKeyHashMap(entries);
        for (Entry<KeyWithCollidingHash, Integer> entry : entries) {
            assertTrue(map.contains(entry));
        }
        assertFalse(map.contains(new Entry<KeyWithCollidingHash, Integer>(new KeyWithCollidingHash(-1), -1)));
    }

    /**
     * Verifies that containsKey returns the correct result, both true for presence and false for absence, when the hash
     * code for the keys collides.
     */
    @Test
    public void containsKeyReturnsCorrectResultForCollidingKeyHashCodes() {
        Entry<KeyWithCollidingHash, Integer>[] entries = new Entry[SIX];
        for (int i = 0; i < entries.length; i++) {
            entries[i] = new Entry<KeyWithCollidingHash, Integer>(new KeyWithCollidingHash(i), i);
        }
        Map<KeyWithCollidingHash, Integer> map = createCollidingKeyHashMap(entries);
        for (Entry<KeyWithCollidingHash, Integer> entry : entries) {
            assertTrue(map.containsKey(entry.key()));
        }
        assertFalse(map.containsKey(new KeyWithCollidingHash(-1)));
        assertFalse(map.containsKey(null));
    }

    /**
     * Verifies that containsKey returns the correct result, both true for presence and false for absence, when the hash
     * code for the keys collides and contains null.
     */
    @Test
    public void containsKeyReturnsCorrectResultForCollidingKeyHashCodesWithNull() {
        Entry<KeyWithCollidingHash, Integer>[] entries = new Entry[SIX];
        for (int i = 0; i < entries.length - 1; i++) {
            entries[i] = new Entry<KeyWithCollidingHash, Integer>(new KeyWithCollidingHash(i), i);
        }
        entries[entries.length - 1] = new Entry<KeyWithCollidingHash, Integer>(null, -1);
        Map<KeyWithCollidingHash, Integer> map = createCollidingKeyHashMap(entries);
        for (Entry<KeyWithCollidingHash, Integer> entry : entries) {
            assertTrue(map.containsKey(entry.key()));
        }
        assertFalse(map.containsKey(new KeyWithCollidingHash(-1)));
        assertTrue(map.containsKey(null));
    }

    /**
     * Verifies that the spliterator has the distinct flag not set for maps with duplicate keys and duplicate values.
     */
    @Test
    public void spliteratorShouldNotSetDistinctFlagForMapWithDuplicateKeysAndDuplicateValues() {
        assertFalse(
                createMap(DUPLICATE_KEYS_WITH_DUPLICATE_VALUES).spliterator().hasCharacteristics(Spliterator.DISTINCT));
    }

    /**
     * Verifies that the spliterator has the distinct flag set for maps with duplicate keys and distinct entries.
     */
    @Test
    public void spliteratorShouldSetDistinctFlagForMapWithDuplicateKeysAndDistinctValues() {
        assertTrue(
                createMap(DUPLICATE_KEYS_WITH_DISTINCT_VALUES).spliterator().hasCharacteristics(Spliterator.DISTINCT));
    }

    /**
     * Verifies that the spliterator has the distinct flag set for maps with distinct keys.
     */
    @Test
    public void spliteratorShouldSetDistinctFlagForMapWithDistinctKeys() {
        assertTrue(createMap(DISTINCT_KEYS).spliterator().hasCharacteristics(Spliterator.DISTINCT));
    }
}
