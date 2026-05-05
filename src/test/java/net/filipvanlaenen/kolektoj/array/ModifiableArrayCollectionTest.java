package net.filipvanlaenen.kolektoj.array;

import net.filipvanlaenen.kolektoj.Collection.ElementCardinality;
import net.filipvanlaenen.kolektoj.CollectionTestBase.ElementWithCollidingHash;
import net.filipvanlaenen.kolektoj.ModifiableCollectionTestBase;

/**
 * Unit tests on the {@link net.filipvanlaenen.kolektoj.array.ModifiableArrayCollection} class.
 */
public final class ModifiableArrayCollectionTest extends ModifiableCollectionTestBase<
        ModifiableArrayCollection<Integer>, ModifiableArrayCollection<ElementWithCollidingHash>> {
    @Override
    protected ModifiableArrayCollection<Integer> createCollection(final ElementCardinality elementCardinality,
            final ModifiableArrayCollection<Integer> collection) {
        return new ModifiableArrayCollection<Integer>(elementCardinality, collection);
    }

    @Override
    protected ModifiableArrayCollection<Integer> createCollection(final ModifiableArrayCollection<Integer> collection) {
        return new ModifiableArrayCollection<Integer>(collection);
    }

    @Override
    protected ModifiableArrayCollection<ElementWithCollidingHash> createCollidingHashValuesCollection(
            final ElementWithCollidingHash... elements) {
        return new ModifiableArrayCollection<ElementWithCollidingHash>(elements);
    }

    @Override
    protected ModifiableArrayCollection<Integer> createModifiableCollection(final ElementCardinality elementCardinality,
            final ModifiableArrayCollection<Integer> integers) {
        return new ModifiableArrayCollection<Integer>(elementCardinality, integers);
    }

    @Override
    protected ModifiableArrayCollection<Integer> createModifiableCollection(final ElementCardinality elementCardinality,
            final Integer... integers) {
        return new ModifiableArrayCollection<Integer>(elementCardinality, integers);
    }

    @Override
    protected ModifiableArrayCollection<Integer> createModifiableCollection(final Integer... integers) {
        return new ModifiableArrayCollection<Integer>(integers);
    }
}
