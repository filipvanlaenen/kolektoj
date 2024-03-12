package net.filipvanlaenen.kolektoj;

import net.filipvanlaenen.kolektoj.MapTestBase.KeyWithCollidingHash;

public abstract class ModifiableSortedMapTestBase<T extends ModifiableSortedMap<Integer, String>, TC extends ModifiableSortedMap<KeyWithCollidingHash, Integer>>
        extends MapTestBase<T, TC> {
}
