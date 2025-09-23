package net.filipvanlaenen.kolektoj;

import static net.filipvanlaenen.kolektoj.Map.KeyAndValueCardinality.DISTINCT_KEYS;
import static net.filipvanlaenen.kolektoj.Map.KeyAndValueCardinality.DUPLICATE_KEYS_WITH_DISTINCT_VALUES;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import net.filipvanlaenen.kolektoj.Map.Entry;
import net.filipvanlaenen.kolektoj.Map.KeyAndValueCardinality;
import net.filipvanlaenen.kolektoj.MapTestBase.KeyWithCollidingHash;

/**
 * Unit tests on implementations of the {@link net.filipvanlaenen.kolektoj.ModifiableSortedMap} interface.
 *
 * @param <T>  The subclass type to be tested.
 * @param <TC> The subclass type to be tested, but with a key type with colliding hash values.
 */
public abstract class ModifiableSortedMapTestBase<T extends ModifiableSortedMap<Integer, String>,
        TC extends ModifiableSortedMap<KeyWithCollidingHash, Integer>> extends UpdatableSortedMapTestBase<T, TC> {
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
     * The magic number ten.
     */
    private static final int TEN = 10;
    /**
     * An entry with key 4 and value four.
     */
    private static final Entry<Integer, String> ENTRY4 = new Entry<Integer, String>(4, "four");
    /**
     * Map with the integers 1 and 2.
     */
    private static final Map<Integer, String> MAP12 = Map.<Integer, String>of(ENTRY1, ENTRY2);
    /**
     * Map with the integer 4 mapped to its word.
     */
    private static final Map<Integer, String> MAP4 = Map.<Integer, String>of(ENTRY4);
    /**
     * Sorted map with three entries.
     */
    private T map123 = createMap123();

    /**
     * Creates a sorted map with three entries.
     *
     * @return A new sorted map with three entries.
     */
    private T createMap123() {
        return createMap(ENTRY1, ENTRY2, ENTRY3);
    }

    /**
     * Verifies that adding an element to an empty map returns true.
     */
    @Test
    public void addOnAnEmptyMapShouldReturnTrue() {
        assertTrue(createMap().add(1, "one"));
    }

    /**
     * Verifies that adding an element to an empty map increases the size to one.
     */
    @Test
    public void addOnAnEmptyMapShouldIncreaseSize() {
        T map = createMap();
        map.add(1, "one");
        assertEquals(1, map.size());
    }

    /**
     * Verifies that adding an entry with an already present key to a map with distinct keys returns false.
     */
    @Test
    public void addWithDuplicateKeyOnMapWithDistinctKeysShouldReturnFalse() {
        T map = createMap(DISTINCT_KEYS, ENTRY1);
        assertFalse(map.add(1, null));
    }

    /**
     * Verifies that adding an entry with an already present key and value to a map with duplicate keys with distinct
     * values returns false.
     */
    @Test
    public void addWithDuplicateKeyAndValueOnMapWithDuplicateKeysAndDistinctValuesShouldReturnFalse() {
        T map = createMap(DUPLICATE_KEYS_WITH_DISTINCT_VALUES, ENTRY1);
        assertFalse(map.add(1, "one"));
    }

    /**
     * Verifies that adding with a duplicate key stored the hashed value correctly.
     */
    @Test
    public void addWithDuplicateKeyShouldStoreTheEntryCorrectly() {
        T map = createMap(DUPLICATE_KEYS_WITH_DISTINCT_VALUES, ENTRY1, ENTRY2, ENTRY3);
        map.add(1, "bis");
        assertEquals(2, map.getAll(1).size());
    }

    /**
     * Verifies that duplicate values can be added to the same key on a map with duplicate keys and values.
     */
    @Test
    public void addOnAMapWithDuplicateKeysAndDuplicateValuesAllowsDuplicateValues() {
        T map = createMap(KeyAndValueCardinality.DUPLICATE_KEYS_WITH_DUPLICATE_VALUES);
        map.add(1, "one");
        map.add(1, "one");
        assertEquals(2, map.getAll(1).size());
    }

    /**
     * Verifies that adding an empty map returns false.
     */
    @Test
    public void addAllWithEmptyMapShouldReturnFalse() {
        assertFalse(createMap123().addAll(Map.<Integer, String>empty()));
    }

    /**
     * Verifies that adding a map with already present keys on a map with distinct keys returns false.
     */
    @Test
    public void addAllWithDuplicateKeysOnMapWithDistinctKeysShouldReturnFalse() {
        T map = createMap(DISTINCT_KEYS, ENTRY1, ENTRY2, ENTRY3);
        assertFalse(map.addAll(Map.<Integer, String>of(ENTRY1BIS)));
    }

    /**
     * Verifies that adding a map with both present and absent keys adds the entries with absent keys.
     */
    @Test
    public void addAllOnMapWithDistinctKeysShouldAddNewKeysOnly() {
        T map = createMap(DISTINCT_KEYS, ENTRY1, ENTRY2);
        assertTrue(map.addAll(Map.<Integer, String>of(ENTRY3, ENTRY1BIS)));
        assertEquals(THREE, map.size());
    }

    /**
     * Verifies that adding a map with already present keys and values on a map with duplicate keys and distinct values
     * returns false.
     */
    @Test
    public void addAllWithDuplicateKeyAndValuesOnMapWithDuplicateKeysAndDistinctValuesShouldReturnFalse() {
        T map = createMap(DUPLICATE_KEYS_WITH_DISTINCT_VALUES, ENTRY1, ENTRY2, ENTRY3);
        assertFalse(map.addAll(Map.<Integer, String>of(new Entry<Integer, String>(1, "one"))));
    }

    /**
     * Verifies that adding a map with both present and absent keys and values adds the entries with new keys or new
     * values.
     */
    @Test
    public void addAllOnMapWithDuplicateKeysAndDistinctValuesShouldAddNewKeysAndNewValues() {
        T map = createMap(DUPLICATE_KEYS_WITH_DISTINCT_VALUES, ENTRY1, ENTRY2);
        assertTrue(map.addAll(Map.<Integer, String>of(ENTRY3, ENTRY1BIS, new Entry<Integer, String>(1, "one"))));
        assertEquals(FOUR, map.size());
    }

    /**
     * Verifies that adding a map of size one returns true.
     */
    @Test
    public void addAllWithMapWithOneEntryReturnsTrue() {
        assertTrue(createMap().addAll(MAP4));
    }

    /**
     * Verifies that adding a map of size one increases the size of the map.
     */
    @Test
    public void addAllWithMapWithOneEntryIncreasesTheSizeByOne() {
        T map = createMap123();
        map.addAll(MAP4);
        assertEquals(FOUR, map.size());
    }

    /**
     * Verifies that adding a map with a large map returns true. This tests that resizing works as intended. Note that
     * the size of the large map is constructed to match the ratios in the implementation.
     */
    @Test
    public void addAllWithLargeMapReturnsTrue() {
        T map1 = createMap();
        ModifiableMap map2 = ModifiableMap.<Integer, String>empty();
        for (int i = 0; i < TEN; i++) {
            map2.add(SIX + i, "2");
        }
        assertTrue(map1.addAll(map2));
    }

    /**
     * Verifies that adding a map with an entry with a null key adds the entry with the null key.
     */
    @Test
    public void addAllWithMapWithNullKeyAddsTheNullKey() {
        T map = createMap();
        map.addAll(Map.<Integer, String>of(ENTRY1, ENTRY2, ENTRY3, new Entry<Integer, String>(null, null)));
        assertNull(map.get(null));
    }

    /**
     * Verifies that clearing a map sets the size to zero.
     */
    @Test
    public void clearShouldSetMapToBeEmpty() {
        T map = createMap123();
        map.clear();
        assertTrue(map.isEmpty());
        assertFalse(map.containsKey(1));
    }

    /**
     * Verifies that clearing a map empties caches.
     */
    @Test
    public void clearShouldSetCachesToBeEmpty() {
        T map = createMap123();
        map.clear();
        assertEquals(0, map.toArray().length);
        assertFalse(map.contains(ENTRY1));
    }

    /**
     * Verifies that keys are removed from the map when it's cleared.
     */
    @Test
    public void clearShouldRemoveKeys() {
        T map = createMap123();
        map.clear();
        assertTrue(map.getKeys().isEmpty());
    }

    /**
     * Verifies that values are removed from the map when it's cleared.
     */
    @Test
    public void clearShouldRemoveValues() {
        T map = createMap123();
        map.clear();
        assertTrue(map.getValues().isEmpty());
    }

    /**
     * Verifies that trying to remove an absent key throws IllegalArgumentException.
     */
    @Test
    public void removeShouldThrowExceptionForAbsentKey() {
        IllegalArgumentException exception =
                assertThrows(IllegalArgumentException.class, () -> createMap().remove(FOUR));
        assertEquals("Map doesn't contain an entry with the key 4.", exception.getMessage());
    }

    /**
     * Verifies that removing a key returns the associated value.
     */
    @Test
    public void removeShouldReturnTheValueForTheKey() {
        assertEquals("one", createMap123().remove(1));
    }

    /**
     * Verifies that the map is updated when the least key is removed.
     */
    @Test
    public void removeShouldUpdateStructureWhenLeastKeyIsRemoved() {
        T map = createMap(ENTRY1, ENTRY2, ENTRY3);
        map.remove(1);
        assertEquals(ENTRY2, map.getLeast());
    }

    /**
     * Verifies that when all entries are removed, a collection is empty.
     */
    @Test
    public void removeAllWithTheSameEntriesShouldMakeMapEmpty() {
        T map = createMap123();
        map.removeAll(map123);
        assertTrue(map.isEmpty());
    }

    /**
     * Verifies that when some entries are removed, removeAll returns true.
     */
    @Test
    public void removeAllShouldReturnTrueWhenSomeEntriesAreRemoved() {
        T map = createMap123();
        assertTrue(map.removeAll(MAP12));
    }

    /**
     * Verifies that when no entries are removed, removeAll returns false.
     */
    @Test
    public void removeAllShouldReturnFalseWhenNoEntriesAreRemoved() {
        T map = createMap123();
        assertFalse(map.removeAll(MAP4));
    }

    /**
     * Verifies that trying to remove the entry with the greatest key from an empty map throws
     * IndexOutOfBoundsException.
     */
    @Test
    public void removeGreatestShouldThrowExceptionWhenCalledOnAnEmptyMap() {
        IndexOutOfBoundsException exception =
                assertThrows(IndexOutOfBoundsException.class, () -> createMap().removeGreatest());
        assertEquals("Cannot remove an entry from an empty map.", exception.getMessage());
    }

    /**
     * Verifies that <code>removeGreatest</code> returns the greatest entry of a sorted map.
     */
    @Test
    public void removeGreatestShouldReturnTheGreatestEntryInASortedMap() {
        assertEquals(ENTRY3, map123.removeGreatest());
    }

    /**
     * Verifies that <code>removeGreatest</code> removes the greatest entry of a sorted map.
     */
    @Test
    public void removeGreatestShouldRemoveTheGreatestEntryInASortedMap() {
        map123.removeGreatest();
        assertEquals(2, map123.size());
        assertFalse(map123.contains(ENTRY3));
    }

    /**
     * Verifies that removeIf returns false on an empty map.
     */
    @Test
    public void removeIfShouldReturnFalseOnAnEmptyMap() {
        assertFalse(createMap().removeIf(x -> true));
    }

    /**
     * Verifies that removeIf returns false when no elements are removed.
     */
    @Test
    public void removeIfShouldReturnFalseWhenNoElementsAreRemoved() {
        T map = createMap123();
        assertFalse(map.removeIf(x -> false));
        assertEquals(THREE, map.size());
    }

    /**
     * Verifies that removeIf returns true when an element is removed.
     */
    @Test
    public void removeIfShouldReturnTrueWhenAnElementIsRemoved() {
        T map = createMap123();
        assertTrue(map.removeIf(x -> x.key() == 1));
        assertEquals(2, map.size());
    }

    /**
     * Verifies that trying to remove the entry with the least key from an empty map throws IndexOutOfBoundsException.
     */
    @Test
    public void removeLeastShouldThrowExceptionWhenCalledOnAnEmptyMap() {
        IndexOutOfBoundsException exception =
                assertThrows(IndexOutOfBoundsException.class, () -> createMap().removeLeast());
        assertEquals("Cannot remove an entry from an empty map.", exception.getMessage());
    }

    /**
     * Verifies that <code>removeLeast</code> returns the least entry of a sorted map.
     */
    @Test
    public void removeLeastShouldReturnTheLeastEntryInASortedMap() {
        assertEquals(ENTRY1, map123.removeLeast());
    }

    /**
     * Verifies that <code>removeLeast</code> removes the least entry of a sorted map.
     */
    @Test
    public void removeLeastShouldRemoveTheLeastEntryInASortedMap() {
        map123.removeLeast();
        assertEquals(2, map123.size());
        assertFalse(map123.contains(ENTRY1));
    }

    /**
     * Verifies that when some entries are removed, retainAll returns true.
     */
    @Test
    public void retainAllShouldReturnTrueWhenSomeEntriesAreRemoved() {
        T map = createMap123();
        assertTrue(map.retainAll(MAP12));
        assertEquals(2, map.size());
    }

    /**
     * Verifies that when no entries are removed, retainAll returns false.
     */
    @Test
    public void retainAllShouldReturnFalseWhenNoEntriesAreRemoved() {
        ModifiableMap<Integer, String> map = createMap123();
        assertFalse(map.retainAll(Map.<Integer, String>of(ENTRY1, ENTRY2, ENTRY3, ENTRY4)));
        assertEquals(THREE, map.size());
    }
}
