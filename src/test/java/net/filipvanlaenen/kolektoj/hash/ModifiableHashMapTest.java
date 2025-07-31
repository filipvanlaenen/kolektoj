package net.filipvanlaenen.kolektoj.hash;

import static net.filipvanlaenen.kolektoj.Map.KeyAndValueCardinality.DISTINCT_KEYS;
import static net.filipvanlaenen.kolektoj.Map.KeyAndValueCardinality.DUPLICATE_KEYS_WITH_DISTINCT_VALUES;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import net.filipvanlaenen.kolektoj.Map;
import net.filipvanlaenen.kolektoj.Map.Entry;
import net.filipvanlaenen.kolektoj.Map.KeyAndValueCardinality;
import net.filipvanlaenen.kolektoj.MapTestBase;
import net.filipvanlaenen.kolektoj.MapTestBase.KeyWithCollidingHash;
import net.filipvanlaenen.kolektoj.ModifiableMap;

/**
 * Unit tests on the {@link net.filipvanlaenen.kolektoj.hash.ModifiableHashMap} class.
 */
public final class ModifiableHashMapTest
        extends MapTestBase<ModifiableHashMap<Integer, String>, ModifiableHashMap<KeyWithCollidingHash, Integer>> {
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
     * An entry with key null and value null.
     */
    private static final Entry<Integer, String> ENTRY_NULL = new Entry<Integer, String>(null, null);
    /**
     * An entry with key 1 and value one.
     */
    private static final Entry<Integer, String> ENTRY1 = new Entry<Integer, String>(1, "one");
    /**
     * An entry with key 1 and value bis.
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
     * An entry with key 4 and value four.
     */
    private static final Entry<Integer, String> ENTRY4 = new Entry<Integer, String>(4, "four");
    /**
     * Map with the integers 1 and 2.
     */
    private static final Map<Integer, String> MAP12 = new HashMap<Integer, String>(ENTRY1, ENTRY2);
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
     * Map with the integer 4 mapped to its word.
     */
    private static final Map<Integer, String> MAP4 = new HashMap<Integer, String>(ENTRY4);

    @Override
    protected ModifiableHashMap<Integer, String> createMap(final Entry<Integer, String>... entries) {
        return new ModifiableHashMap<Integer, String>(entries);
    }

    @Override
    protected ModifiableHashMap<Integer, String> createMap(final KeyAndValueCardinality keyAndValueCardinality,
            final Entry<Integer, String>... entries) {
        return new ModifiableHashMap<Integer, String>(keyAndValueCardinality, entries);
    }

    @Override
    protected ModifiableHashMap<KeyWithCollidingHash, Integer> createCollidingKeyHashMap(
            final Entry<KeyWithCollidingHash, Integer>... entries) {
        return new ModifiableHashMap<KeyWithCollidingHash, Integer>(entries);
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
     * Verifies that a map can be constructed from another map.
     */
    @Test
    public void constructorShouldCreateMapFromAnotherMap() {
        ModifiableMap<Integer, String> map = new ModifiableHashMap<Integer, String>(MAP123);
        assertTrue(map.containsSame(MAP123));
    }

    /**
     * Verifies that adding an element to an empty map returns true.
     */
    @Test
    public void addOnAnEmptyMapShouldReturnTrue() {
        assertTrue(new ModifiableHashMap<Integer, Integer>().add(1, 1));
    }

    /**
     * Verifies that adding an entry with an already present key to a map with distinct keys returns false.
     */
    @Test
    public void addWithDuplicateKeyOnMapWithDistinctKeysShouldReturnFalse() {
        ModifiableMap<Integer, String> map = new ModifiableHashMap<Integer, String>(DISTINCT_KEYS, ENTRY1);
        assertFalse(map.add(1, null));
    }

    /**
     * Verifies that adding an entry with an already present key and value to a map with duplicate keys with distinct
     * values returns false.
     */
    @Test
    public void addWithDuplicateKeyAndValueOnMapWithDuplicateKeysAndDistinctValuesShouldReturnFalse() {
        ModifiableMap<Integer, String> map =
                new ModifiableHashMap<Integer, String>(DUPLICATE_KEYS_WITH_DISTINCT_VALUES, ENTRY1);
        assertFalse(map.add(1, "one"));
    }

    /**
     * Verifies that adding with a duplicate key stored the hashed value correctly.
     */
    @Test
    public void addWithDuplicateKeyShouldStoreTheEntryCorrectly() {
        ModifiableMap<Integer, String> map =
                new ModifiableHashMap<Integer, String>(DUPLICATE_KEYS_WITH_DISTINCT_VALUES, ENTRY1, ENTRY2, ENTRY3);
        map.add(1, "bis");
        assertEquals(2, map.getAll(1).size());
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
     * Verifies that adding keys with colliding hash values still returns the correct values for a key.
     */
    @Test
    public void mapShouldContainValueForKeysWithCollidingHashValuesAfterHavingItAdded() {
        ModifiableMap<KeyWithCollidingHash, Integer> map = new ModifiableHashMap<KeyWithCollidingHash, Integer>();
        KeyWithCollidingHash key1 = new KeyWithCollidingHash(1);
        KeyWithCollidingHash key2 = new KeyWithCollidingHash(2);
        map.add(key1, 1);
        map.add(key2, 2);
        assertEquals(1, map.get(key1));
        assertEquals(2, map.get(key2));
    }

    /**
     * Verifies that adding an empty map returns false.
     */
    @Test
    public void addAllWithEmptyMapShouldReturnFalse() {
        assertFalse(createNewMap().addAll(new HashMap<Integer, String>()));
    }

    /**
     * Verifies that adding a map with already present keys on a map with distinct keys returns false.
     */
    @Test
    public void addAllWithDuplicateKeysOnMapWithDistinctKeysShouldReturnFalse() {
        ModifiableMap<Integer, String> map =
                new ModifiableHashMap<Integer, String>(DISTINCT_KEYS, ENTRY1, ENTRY2, ENTRY3);
        assertFalse(map.addAll(new HashMap<Integer, String>(ENTRY1BIS)));
    }

    /**
     * Verifies that adding a map with both present and absent keys adds the entries with absent keys.
     */
    @Test
    public void addAllOnMapWithDistinctKeysShouldAddNewKeysOnly() {
        ModifiableMap<Integer, String> map = new ModifiableHashMap<Integer, String>(DISTINCT_KEYS, ENTRY1, ENTRY2);
        assertTrue(map.addAll(new HashMap<Integer, String>(ENTRY3, ENTRY1BIS)));
        assertEquals(THREE, map.size());
    }

    /**
     * Verifies that adding a map with already present keys and values on a map with duplicate keys and distinct values
     * returns false.
     */
    @Test
    public void addAllWithDuplicateKeyAndValuesOnMapWithDuplicateKeysAndDistinctValuesShouldReturnFalse() {
        ModifiableMap<Integer, String> map =
                new ModifiableHashMap<Integer, String>(DUPLICATE_KEYS_WITH_DISTINCT_VALUES, ENTRY1, ENTRY2, ENTRY3);
        assertFalse(map.addAll(new HashMap<Integer, String>(new Entry<Integer, String>(1, "one"))));
    }

    /**
     * Verifies that adding a map with both present and absent keys and values adds the entries with new keys or new
     * values.
     */
    @Test
    public void addAllOnMapWithDuplicateKeysAndDistinctValuesShouldAddNewKeysAndNewValues() {
        ModifiableMap<Integer, String> map =
                new ModifiableHashMap<Integer, String>(DUPLICATE_KEYS_WITH_DISTINCT_VALUES, ENTRY1, ENTRY2);
        assertTrue(map.addAll(new HashMap<Integer, String>(ENTRY3, ENTRY1BIS, new Entry<Integer, String>(1, "one"))));
        assertEquals(FOUR, map.size());
    }

    /**
     * Verifies that adding a map of size one returns true.
     */
    @Test
    public void addAllWithMapWithOneEntryReturnsTrue() {
        assertTrue(createNewMap().addAll(MAP4));
    }

    /**
     * Verifies that adding a map of size one increases the size of the map.
     */
    @Test
    public void addAllWithMapWithOneEntryIncreasesTheSizeByOne() {
        ModifiableMap<Integer, String> map = createNewMap();
        map.addAll(MAP4);
        assertEquals(FOUR, map.size());
    }

    /**
     * Verifies that adding a map with a large map returns true. This tests that resizing works as intended. Note that
     * the size of the large map is constructed to match the ratios in the implementation.
     */
    @Test
    public void addAllWithLargeMapReturnsTrue() {
        ModifiableMap<Integer, String> map1 = createNewMap();
        ModifiableMap<Integer, String> map2 = new ModifiableHashMap<Integer, String>();
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
        ModifiableMap<Integer, String> map = new ModifiableHashMap<Integer, String>();
        map.addAll(MAP123NULL);
        assertNull(map.get(null));
    }

    /**
     * Verifies that adding a map with keys with colliding hash values returns the correct values for a key.
     */
    @Test
    public void mapShouldContainValueForKeysWithCollidingHashValuesAfterHavingItAddedAsAMap() {
        ModifiableMap<KeyWithCollidingHash, Integer> map1 = new ModifiableHashMap<KeyWithCollidingHash, Integer>();
        ModifiableMap<KeyWithCollidingHash, Integer> map2 = new ModifiableHashMap<KeyWithCollidingHash, Integer>();
        KeyWithCollidingHash key1 = new KeyWithCollidingHash(1);
        KeyWithCollidingHash key2 = new KeyWithCollidingHash(2);
        map2.add(key1, 1);
        map2.add(key2, 2);
        map1.addAll(map2);
        assertEquals(1, map1.get(key1));
        assertEquals(2, map1.get(key2));
    }

    /**
     * Verifies that clearing a map sets the size to zero.
     */
    @Test
    public void clearShouldSetMapToBeEmpty() {
        ModifiableMap<Integer, String> map = createNewMap();
        map.clear();
        assertTrue(map.isEmpty());
        assertFalse(map.containsKey(1));
    }

    /**
     * Verifies that keys are removed from the map when it's cleared.
     */
    @Test
    public void clearShouldRemoveKeys() {
        ModifiableMap<Integer, String> map = createNewMap();
        map.clear();
        assertTrue(map.getKeys().isEmpty());
    }

    /**
     * Verifies that values are removed from the map when it's cleared.
     */
    @Test
    public void clearShouldRemoveValues() {
        ModifiableMap<Integer, String> map = createNewMap();
        map.clear();
        assertTrue(map.getValues().isEmpty());
    }

    /**
     * Verifies that trying to remove an absent key throws IllegalArgumentException.
     */
    @Test
    public void removeShouldThrowExceptionForAbsentKey() {
        IllegalArgumentException exception =
                assertThrows(IllegalArgumentException.class, () -> createNewMap().remove(FOUR));
        assertEquals("Map doesn't contain an entry with the key 4.", exception.getMessage());
    }

    /**
     * Verifies that removing a key returns the associated value.
     */
    @Test
    public void removeShouldReturnTheValueForTheKey() {
        assertEquals("one", createNewMap().remove(1));
    }

    /**
     * Verifies that when multiple entries have the same key, and some of them are removed, getAll still returns all
     * values. This ensures that the hashed array is rehashed as holes appear in the overflow after removing entries.
     */
    @Test
    public void removeShouldRehashIfHolesAppear() {
        ModifiableMap<Integer, String> map =
                new ModifiableHashMap<Integer, String>(DUPLICATE_KEYS_WITH_DISTINCT_VALUES);
        map.add(1, "a");
        map.add(1, "b");
        map.add(1, "c");
        map.add(1, "d");
        map.add(1, "e");
        map.add(1, "f");
        map.add(1, "g");
        map.remove(1);
        assertEquals(SIX, map.getAll(1).size());
    }

    /**
     * Verifies that when all entries are removed, a collection is empty.
     */
    @Test
    public void removeAllWithTheSameEntriesShouldMakeMapEmpty() {
        ModifiableMap<Integer, String> map = createNewMap();
        map.removeAll(MAP123);
        assertTrue(map.isEmpty());
    }

    /**
     * Verifies that when some entries are removed, removeAll returns true.
     */
    @Test
    public void removeAllShouldReturnTrueWhenSomeEntriesAreRemoved() {
        ModifiableMap<Integer, String> map = createNewMap();
        assertTrue(map.removeAll(MAP12));
    }

    /**
     * Verifies that when no entries are removed, removeAll returns false.
     */
    @Test
    public void removeAllShouldReturnFalseWhenNoEntriesAreRemoved() {
        ModifiableMap<Integer, String> map = createNewMap();
        assertFalse(map.removeAll(new ModifiableHashMap<Integer, String>(ENTRY4)));
    }

    /**
     * Verifies that when multiple entries have the same key, and some of them are removed, getAll still returns all
     * values. This ensures that the hashed array is rehashed as holes appear in the overflow after removing entries.
     */
    @Test
    public void removeAllShouldRehashIfHolesAppear() {
        ModifiableMap<Integer, String> map =
                new ModifiableHashMap<Integer, String>(DUPLICATE_KEYS_WITH_DISTINCT_VALUES);
        map.add(1, "a");
        map.add(1, "b");
        map.add(1, "c");
        map.add(1, "d");
        map.add(1, "e");
        map.add(1, "f");
        map.add(1, "g");
        map.removeAll(ModifiableMap.of(1, "a"));
        assertEquals(SIX, map.getAll(1).size());
    }

    /**
     * Verifies that when multiple entries have the same key, and some of them are removed, getAll still returns all
     * values. This ensures that the hashed array is rehashed as holes appear in the overflow after removing entries.
     */
    @Test
    public void removeAllShouldRehashIfManyEntriesAreRemoved() {
        ModifiableMap<Integer, String> map1 = new ModifiableHashMap<Integer, String>();
        ModifiableMap<Integer, String> map2 = new ModifiableHashMap<Integer, String>();
        for (int i = 0; i < TEN; i++) {
            map1.add(i, "" + i);
            map2.add(i, "" + i);
        }
        map1.removeAll(map2);
        assertTrue(map1.isEmpty());
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
        assertFalse(createNewMap().removeIf(x -> false));
    }

    /**
     * Verifies that removeIf returns true when an element is removed.
     */
    @Test
    public void removeIfShouldReturnTrueWhenAnElementIsRemoved() {
        assertTrue(createNewMap().removeIf(x -> x.key() == 1));
    }

    /**
     * Verifies that when some entries are removed, retainAll returns true.
     */
    @Test
    public void retainAllShouldReturnTrueWhenSomeEntriesAreRemoved() {
        ModifiableMap<Integer, String> map = createNewMap();
        assertTrue(map.retainAll(MAP12));
    }

    /**
     * Verifies that when no entries are removed, retainAll returns false.
     */
    @Test
    public void retainAllShouldReturnFalseWhenNoEntriesAreRemoved() {
        ModifiableMap<Integer, String> map = createNewMap();
        assertFalse(map.retainAll(MAP123));
    }

    /**
     * Verifies that when all entries are retained, a map remains intact.
     */
    @Test
    public void retainAllWithTheSameEntriessShouldNotRemoveEntries() {
        ModifiableMap<Integer, String> map = createNewMap();
        map.retainAll(MAP123);
        assertEquals(THREE, map.size());
        assertTrue(map.contains(ENTRY1));
        assertTrue(map.contains(ENTRY2));
        assertTrue(map.contains(ENTRY3));
    }

    /**
     * Verifies that when only absent entries should be retained, retainAll empties the map.
     */
    @Test
    public void retainAllWithAbsentEntriesOnlyClearsTheMap() {
        ModifiableMap<Integer, String> map = createNewMap();
        map.retainAll(MAP4);
        assertTrue(map.isEmpty());
    }

    /**
     * Verifies that when multiple entries have the same key, and some of them are not retained, getAll still returns
     * all values. This ensures that the hashed array is rehashed as holes appear in the overflow after removing entries
     * in the retainAll method.
     */
    @Test
    public void retainAllShouldRehashIfHolesAppear() {
        ModifiableMap<Integer, String> map1 =
                new ModifiableHashMap<Integer, String>(DUPLICATE_KEYS_WITH_DISTINCT_VALUES);
        ModifiableMap<Integer, String> map2 =
                new ModifiableHashMap<Integer, String>(DUPLICATE_KEYS_WITH_DISTINCT_VALUES);
        map1.add(1, "a");
        map1.add(1, "b");
        map1.add(1, "c");
        map1.add(1, "d");
        map1.add(1, "e");
        map1.add(1, "f");
        map1.add(1, "g");
        map2.add(1, "b");
        map2.add(1, "c");
        map2.add(1, "d");
        map2.add(1, "e");
        map2.add(1, "f");
        map2.add(1, "g");
        map1.retainAll(map2);
        assertEquals(SIX, map1.getAll(1).size());
    }

    /**
     * Verifies that trying to update an absent key throws IllegalArgumentException.
     */
    @Test
    public void updateShouldThrowExceptionForAbsentKey() {
        IllegalArgumentException exception =
                assertThrows(IllegalArgumentException.class, () -> createNewMap().update(FOUR, "four"));
        assertEquals("Map doesn't contain an entry with the key 4.", exception.getMessage());
    }

    /**
     * Verifies that updating a key with a new value stores the new value for the key.
     */
    @Test
    public void updateShouldStoreTheNewValueForTheKey() {
        ModifiableMap<Integer, String> map = createNewMap();
        map.update(1, "bis");
        assertEquals("bis", map.get(1));
        assertTrue(map.containsValue("bis"));
        assertTrue(map.contains(ENTRY1BIS));
        assertFalse(map.containsValue("one"));
        assertFalse(map.contains(ENTRY1));
    }

    /**
     * Verifies that updating a key with a new value returns the old value for the key.
     */
    @Test
    public void updateShouldReturnTheOldValueForTheKey() {
        assertEquals("one", createNewMap().update(1, "bis"));
    }
}
