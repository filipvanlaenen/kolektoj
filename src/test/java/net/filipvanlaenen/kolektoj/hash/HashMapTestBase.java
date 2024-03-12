package net.filipvanlaenen.kolektoj.hash;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import net.filipvanlaenen.kolektoj.Map;
import net.filipvanlaenen.kolektoj.Map.Entry;
import net.filipvanlaenen.kolektoj.MapTestBase;
import net.filipvanlaenen.kolektoj.hash.HashMapTestBase.KeyWithCollidingHash;

/**
 * 
 *
 * @param <T>
 * @param <TC> The subclass type to be tested using a key with colliding hash values.
 */
public abstract class HashMapTestBase<T extends Map<Integer, String>, TC extends Map<KeyWithCollidingHash, Integer>>
        extends MapTestBase<T> {
    /**
     * The magic number six.
     */
    private static final int SIX = 6;

    /**
     * Class with colliding hash codes.
     */
    public static final class KeyWithCollidingHash {
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
     * Creates a map using keys with colliding hashes containing the provided entries.
     *
     * @param entries The entries to be included in the map.
     * @return A map containing the provided entries.
     */
    protected abstract TC createCollidingKeyHashMap(Entry<KeyWithCollidingHash, Integer>... entries);

    /**
     * Verifies that contains returns the correct result, both true for presence and false for absence, when the hash
     * code for the keys collides.
     */
    @Test
    public void containsReturnsCorrectResultForCollidingKeyHashCodes() {
        Entry<KeyWithCollidingHash, Integer>[] entries = new Entry[SIX];
        for (int i = 0; i < entries.length; i++) {
            entries[i] = new Entry<KeyWithCollidingHash, Integer>(new KeyWithCollidingHash(), i);
        }
        Map<KeyWithCollidingHash, Integer> map = createCollidingKeyHashMap(entries);
        for (Entry<KeyWithCollidingHash, Integer> entry : entries) {
            assertTrue(map.contains(entry));
        }
        assertFalse(map.contains(new Entry<KeyWithCollidingHash, Integer>(new KeyWithCollidingHash(), -1)));
    }

    /**
     * Verifies that containsKey returns the correct result, both true for presence and false for absence, when the hash
     * code for the keys collides.
     */
    @Test
    public void containsKeyReturnsCorrectResultForCollidingKeyHashCodes() {
        Entry<KeyWithCollidingHash, Integer>[] entries = new Entry[SIX];
        for (int i = 0; i < entries.length; i++) {
            entries[i] = new Entry<KeyWithCollidingHash, Integer>(new KeyWithCollidingHash(), i);
        }
        Map<KeyWithCollidingHash, Integer> map = createCollidingKeyHashMap(entries);
        for (Entry<KeyWithCollidingHash, Integer> entry : entries) {
            assertTrue(map.containsKey(entry.key()));
        }
        assertFalse(map.containsKey(new KeyWithCollidingHash()));
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
            entries[i] = new Entry<KeyWithCollidingHash, Integer>(new KeyWithCollidingHash(), i);
        }
        entries[entries.length - 1] = new Entry<KeyWithCollidingHash, Integer>(null, -1);
        Map<KeyWithCollidingHash, Integer> map = createCollidingKeyHashMap(entries);
        for (Entry<KeyWithCollidingHash, Integer> entry : entries) {
            assertTrue(map.containsKey(entry.key()));
        }
        assertFalse(map.containsKey(new KeyWithCollidingHash()));
        assertTrue(map.containsKey(null));
    }
}
