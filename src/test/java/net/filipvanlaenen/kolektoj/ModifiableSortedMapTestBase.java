package net.filipvanlaenen.kolektoj;

import net.filipvanlaenen.kolektoj.MapTestBase.KeyWithCollidingHash;

/**
 * Unit tests on implementations of the {@link net.filipvanlaenen.kolektoj.ModifiableSortedMap} interface.
 *
 * @param <T>  The subclass type to be tested.
 * @param <TC> The subclass type to be tested, but with a key type with colliding hash values.
 */
public abstract class ModifiableSortedMapTestBase<T extends ModifiableSortedMap<Integer, String>,
        TC extends ModifiableSortedMap<KeyWithCollidingHash, Integer>> extends MapTestBase<T, TC> {
}
