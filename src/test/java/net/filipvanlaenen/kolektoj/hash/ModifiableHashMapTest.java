package net.filipvanlaenen.kolektoj.hash;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import net.filipvanlaenen.kolektoj.Collection;
import net.filipvanlaenen.kolektoj.Map;
import net.filipvanlaenen.kolektoj.Map.Entry;
import net.filipvanlaenen.kolektoj.ModifiableMap;

/**
 * Unit tests on the {@link net.filipvanlaenen.kolektoj.hash.ModifiableHashMap} class.
 */
public class ModifiableHashMapTest {
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
    private static final Entry<Integer, String> ENTRY1 = new Entry<Integer, String>(1, "one");
    /**
     * An entry with key 2 and value two.
     */
    private static final Entry<Integer, String> ENTRY2 = new Entry<Integer, String>(2, "two");
    /**
     * An entry with key 3 and value three.
     */
    private static final Entry<Integer, String> ENTRY3 = new Entry<Integer, String>(3, "three");
    /**
     * Map with the integers 1, 2 and 3 mapped to their words.
     */
    private static final ModifiableMap<Integer, String> MAP123 = createNewMap();
    /**
     * Map with the integers 1, 2 and 3 mapped to their words, and null to null.
     */
    private static final Map<Integer, String> MAP123NULL =
            new HashMap<Integer, String>(ENTRY1, ENTRY2, ENTRY3, ENTRY_NULL);

    /**
     * Class with colliding hash codes.
     */
    private final class KeyWithCollidingHash {
        @Override
        public boolean equals(final Object other) {
            return this == other;
        }

        @Override
        public int hashCode() {
            return 0;
        }
    }

    /**
     * Creates a new map for unit testing.
     *
     * @return A new map for unit testing.
     */
    private static ModifiableMap<Integer, String> createNewMap() {
        return new ModifiableHashMap<Integer, String>(ENTRY1, ENTRY2, ENTRY3);
    }

    /**
     * Verifies that the correct length is returned for a map with three entries.
     */
    @Test
    public void sizeShouldReturnThreeForAMapOfThreeEntries() {
        assertEquals(THREE, MAP123.size());
    }

    /**
     * Verifies that contains returns true for an entry in the map.
     */
    @Test
    public void containsShouldReturnTrueForAnEntryInTheMap() {
        assertTrue(MAP123.contains(ENTRY1));
    }

    /**
     * Verifies that contains returns false for an entry not in the map.
     */
    @Test
    public void containsShouldReturnFalseForAnEntryNotInTheMap() {
        assertFalse(MAP123.contains(new Entry<Integer, String>(0, "zero")));
    }

    /**
     * Verifies that containsKey returns true for a key in the map.
     */
    @Test
    public void containsKeyShouldReturnTrueForAKeyInTheMap() {
        assertTrue(MAP123.containsKey(1));
    }

    /**
     * Verifies that containsKey returns false for a key not in the map.
     */
    @Test
    public void containsKeyShouldReturnFalseForAKeyNotInTheMap() {
        assertFalse(MAP123.containsKey(0));
    }

    /**
     * Verifies that containsKey returns false for null if not in the map.
     */
    @Test
    public void containsKeyShouldReturnFalseForNullIfNotInTheMap() {
        assertFalse(MAP123.containsKey(null));
    }

    /**
     * Verifies that containsValue returns true for a value in the map.
     */
    @Test
    public void containsValueShouldReturnTrueForAValueInTheMap() {
        assertTrue(MAP123.containsValue("one"));
    }

    /**
     * Verifies that containsValue returns true for null if in the map.
     */
    @Test
    public void containsValueShouldReturnTrueForNullIfInTheMap() {
        assertTrue(MAP123NULL.containsValue(null));
    }

    /**
     * Verifies that containsValue returns false for a value not in the map.
     */
    @Test
    public void containsValueShouldReturnFalseForAValueNotInTheMap() {
        assertFalse(MAP123.containsValue("zero"));
    }

    /**
     * Verifies that trying to get an element from an empty map throws IndexOutOfBoundsException.
     */
    @Test
    public void getShouldThrowExceptionWhenCalledOnAnEmptyMap() {
        IndexOutOfBoundsException exception =
                assertThrows(IndexOutOfBoundsException.class, () -> new ModifiableHashMap<Integer, String>().get());
        assertEquals("Cannot return an entry from an empty map.", exception.getMessage());
    }

    /**
     * Verifies that when you get an entry from a map, the map contains it.
     */
    @Test
    public void getShouldReturnAnEntryPresentInTheMap() {
        Entry<Integer, String> entry = MAP123.get();
        assertTrue(MAP123.contains(entry));
    }

    /**
     * Verifies that when you try to use get with a key not in the map, an IllegalArgumentException is thrown.
     */
    @Test
    public void getShouldThrowExceptionWhenCalledWithAbsentKey() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> MAP123.get(0));
        assertEquals("Map doesn't contain an entry with the key 0.", exception.getMessage());
    }

    /**
     * Verifies that when you try to use get with a key in the map, the value is returned.
     */
    @Test
    public void getShouldReturnValueForKey() {
        assertEquals("one", MAP123.get(1));
    }

    /**
     * Verifies that when you try to use getAll with a key not in the map, an IllegalArgumentException is thrown.
     */
    @Test
    public void getAllShouldThrowExceptionWhenCalledWithAbsentKey() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> MAP123.getAll(0));
        assertEquals("Map doesn't contain entries with the key 0.", exception.getMessage());
    }

    /**
     * Verifies that when you try to use getAll with a key in the map, a collection with the value is returned.
     */
    @Test
    public void getAllShouldReturnSingleValueForKey() {
        Collection<String> actual = MAP123.getAll(1);
        assertEquals(1, actual.size());
        assertTrue(actual.contains("one"));
    }

    /**
     * Verifies that when you try to use getAll with a key in the map that has multiple values, a collection with the
     * values is returned.
     */
    @Test
    public void getAllShouldReturnManyValuesForKey() {
        ModifiableMap<Integer, String> map =
                new ModifiableHashMap<Integer, String>(ENTRY1, new Entry<Integer, String>(1, "two"), ENTRY3);
        Collection<String> actual = map.getAll(1);
        assertEquals(2, actual.size());
        assertTrue(actual.contains("one"));
        assertTrue(actual.contains("two"));
    }

    /**
     * Verifies that getKeys returns all the keys.
     */
    @Test
    public void getKeysShouldReturnAllKeys() {
        Collection<Integer> actual = MAP123.getKeys();
        assertEquals(THREE, actual.size());
        assertTrue(actual.contains(1));
        assertTrue(actual.contains(2));
        assertTrue(actual.contains(THREE));
    }

    /**
     * Verifies that getValues returns all the values.
     */
    @Test
    public void getValuesShouldReturnAllKeys() {
        Collection<String> actual = new ModifiableHashMap<Integer, String>(ENTRY1, ENTRY2, ENTRY3).getValues();
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

        ModifiableMap<Integer, String> map = new ModifiableHashMap<Integer, String>(ENTRY1, ENTRY2);
        Entry<Integer, String>[] actual = map.toArray();
        assertTrue(actual.length == 2 && (actual[0] == ENTRY1 || actual[1] == ENTRY1)
                && (actual[0] == ENTRY2 || actual[1] == ENTRY2));
    }

    /**
     * Verifies that the map produces a stream that reduces to the correct key sum, thus verifying that the spliterator
     * is created correctly.
     */
    @Test
    public void streamShouldProduceAStreamThatReducesToTheCorrectSum() {
        assertEquals(SIX, MAP123.stream().map(e -> e.key()).reduce(0, Integer::sum));
    }

    /**
     * Verifies that the map produces an iterator that when used in a for loop, produces the correct key sum.
     */
    @Test
    public void iteratorShouldProduceCorrectSumInForLoop() {
        int sum = 0;
        for (Entry<Integer, String> entry : MAP123) {
            sum += entry.key();
        }
        assertEquals(SIX, sum);
    }

    /**
     * Verifies that adding an element to an empty map returns true.
     */
    @Test
    public void addOnAnEmptyMapShouldReturnTrue() {
        assertTrue(new ModifiableHashMap<Integer, Integer>().add(1, 1));
    }

    /**
     * Verifies that after adding an entry to an empty map, the size is increased to one.
     */
    @Test
    public void sizeShouldBeOneAfterAddingAnEntryToAnEmptyMap() {
        ModifiableMap<Integer, Integer> map = new ModifiableHashMap<Integer, Integer>();
        map.add(1, 1);
        assertEquals(1, map.size());
    }

    /**
     * Verifies that after adding an entry to a map, the map contains the entry added.
     */
    @Test
    public void mapShouldContainAnElementAfterHavingItAdded() {
        ModifiableMap<Integer, String> map = createNewMap();
        map.add(0, "zero");
        assertTrue(map.contains(new Entry<Integer, String>(0, "zero")));
    }

    /**
     * Verifies that after adding an entry to a map, the map contains the key of the entry added.
     */
    @Test
    public void mapShouldContainKeyAfterHavingItAdded() {
        ModifiableMap<Integer, String> map = createNewMap();
        map.add(0, "zero");
        assertTrue(map.containsKey(0));
    }

    /**
     * Verifies that after adding an entry to a map, the map contains the null key of the entry added.
     */
    @Test
    public void mapShouldContainNullKeyAfterHavingItAdded() {
        ModifiableMap<Integer, String> map = createNewMap();
        map.add(null, "zero");
        assertTrue(map.containsKey(null));
    }

    /**
     * Verifies that after adding an entry to a map, the map contains the value of the entry added.
     */
    @Test
    public void mapShouldContainValueAfterHavingItAdded() {
        ModifiableMap<Integer, String> map = createNewMap();
        map.add(0, "zero");
        assertTrue(map.containsValue("zero"));
    }

    /**
     * Verifies that after adding an entry to a map, the map contains the null value of the entry added.
     */
    @Test
    public void mapShouldContainNullValueAfterHavingItAdded() {
        ModifiableMap<Integer, String> map = createNewMap();
        map.add(0, null);
        assertTrue(map.containsValue(null));
    }

    /**
     * Verifies that after adding an entry to a map, the map contains the value for the key added.
     */
    @Test
    public void mapShouldContainValueForKeyAfterHavingItAdded() {
        ModifiableMap<Integer, String> map = createNewMap();
        map.add(0, "zero");
        assertEquals("zero", map.get(0));
    }

    /**
     * Verifies that after adding an entry to a map, the map contains the null value for the null key added.
     */
    @Test
    public void mapShouldContainNullValueForNullKeyAfterHavingItAdded() {
        ModifiableMap<Integer, String> map = createNewMap();
        map.add(null, null);
        assertNull(map.get(null));
    }

    /**
     * Verifying that adding keys with colleding hash values still returns the correct values for a key.
     */
    @Test
    public void mapShouldContainValueForKeysWithCollidingHashValuesAfterHavingItAdded() {
        ModifiableMap<KeyWithCollidingHash, Integer> map = new ModifiableHashMap<KeyWithCollidingHash, Integer>();
        KeyWithCollidingHash key1 = new KeyWithCollidingHash();
        KeyWithCollidingHash key2 = new KeyWithCollidingHash();
        map.add(key1, 1);
        map.add(key2, 2);
        assertEquals(1, map.get(key1));
        assertEquals(2, map.get(key2));
    }
}
