package net.filipvanlaenen.kolektoj.linkedlist;

import net.filipvanlaenen.kolektoj.Collection.ElementCardinality;
import net.filipvanlaenen.kolektoj.CollectionTestBase.ElementWithCollidingHash;
import net.filipvanlaenen.kolektoj.ModifiableCollectionTestBase;

/**
 * Unit tests on the {@link net.filipvanlaenen.kolektoj.linkedlist.ModifiableLinkedListCollection} class.
 */
public final class ModifiableLinkedListCollectionTest extends ModifiableCollectionTestBase<
        ModifiableLinkedListCollection<Integer>, ModifiableLinkedListCollection<ElementWithCollidingHash>> {
    @Override
    protected ModifiableLinkedListCollection<Integer> createCollection(final ElementCardinality elementCardinality,
            final ModifiableLinkedListCollection<Integer> collection) {
        return new ModifiableLinkedListCollection<Integer>(elementCardinality, collection);
    }

    @Override
    protected ModifiableLinkedListCollection<Integer> createCollection(
            final ModifiableLinkedListCollection<Integer> collection) {
        return new ModifiableLinkedListCollection<Integer>(collection);
    }

    @Override
    protected ModifiableLinkedListCollection<ElementWithCollidingHash> createCollidingHashValuesCollection(
            final ElementWithCollidingHash... elements) {
        return new ModifiableLinkedListCollection<ElementWithCollidingHash>(elements);
    }

    @Override
    protected ModifiableLinkedListCollection<Integer> createModifiableCollection(
            final ElementCardinality elementCardinality, final Integer... integers) {
        return new ModifiableLinkedListCollection<Integer>(elementCardinality, integers);
    }

    @Override
    protected ModifiableLinkedListCollection<Integer> createModifiableCollection(
            final ElementCardinality elementCardinality, final ModifiableLinkedListCollection<Integer> integers) {
        return new ModifiableLinkedListCollection<Integer>(elementCardinality, integers);
    }

    @Override
    protected ModifiableLinkedListCollection<Integer> createModifiableCollection(final Integer... integers) {
        return new ModifiableLinkedListCollection<Integer>(integers);
    }
}
