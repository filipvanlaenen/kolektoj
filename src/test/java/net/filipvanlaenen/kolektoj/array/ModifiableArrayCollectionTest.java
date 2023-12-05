package net.filipvanlaenen.kolektoj.array;

import net.filipvanlaenen.kolektoj.ModifiableCollectionTestBase;
import net.filipvanlaenen.kolektoj.Collection.ElementCardinality;

/**
 * Unit tests on the {@link net.filipvanlaenen.kolektoj.array.ModifiableArrayCollection} class.
 */
public final class ModifiableArrayCollectionTest
        extends ModifiableCollectionTestBase<ModifiableArrayCollection<Integer>> {
    @Override
    protected ModifiableArrayCollection<Integer> createModifiableCollection(final Integer... integers) {
        return new ModifiableArrayCollection<Integer>(integers);
    }

    @Override
    protected ModifiableArrayCollection<Integer> createModifiableCollection(final ElementCardinality elementCardinality,
            final Integer... integers) {
        return new ModifiableArrayCollection<Integer>(elementCardinality, integers);
    }
}
