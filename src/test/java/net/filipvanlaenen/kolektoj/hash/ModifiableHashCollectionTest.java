package net.filipvanlaenen.kolektoj.hash;

import net.filipvanlaenen.kolektoj.Collection.ElementCardinality;
import net.filipvanlaenen.kolektoj.CollectionTestBase.ElementWithCollidingHash;
import net.filipvanlaenen.kolektoj.ModifiableCollectionTestBase;

/**
 * Unit tests on the {@link net.filipvanlaenen.kolektoj.hash.ModifiableHashCollection} class.
 */
public final class ModifiableHashCollectionTest extends ModifiableCollectionTestBase<ModifiableHashCollection<Integer>,
        ModifiableHashCollection<ElementWithCollidingHash>> {
    @Override
    protected ModifiableHashCollection<Integer> createCollection(final ElementCardinality elementCardinality,
            final ModifiableHashCollection<Integer> collection) {
        return new ModifiableHashCollection<Integer>(elementCardinality, collection);
    }

    @Override
    protected ModifiableHashCollection<Integer> createCollection(final ModifiableHashCollection<Integer> collection) {
        return new ModifiableHashCollection<Integer>(collection);
    }

    @Override
    protected ModifiableHashCollection<ElementWithCollidingHash> createCollidingHashValuesCollection(
            final ElementWithCollidingHash... elements) {
        return new ModifiableHashCollection<ElementWithCollidingHash>(elements);
    }

    @Override
    protected ModifiableHashCollection<Integer> createModifiableCollection(final ElementCardinality elementCardinality,
            final ModifiableHashCollection<Integer> integers) {
        return new ModifiableHashCollection<Integer>(elementCardinality, integers);
    }

    @Override
    protected ModifiableHashCollection<Integer> createModifiableCollection(final ElementCardinality elementCardinality,
            final Integer... integers) {
        return new ModifiableHashCollection<Integer>(elementCardinality, integers);
    }

    @Override
    protected ModifiableHashCollection<Integer> createModifiableCollection(final Integer... integers) {
        return new ModifiableHashCollection<Integer>(integers);
    }
}
