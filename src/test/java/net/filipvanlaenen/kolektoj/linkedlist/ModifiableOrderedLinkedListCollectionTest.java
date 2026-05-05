package net.filipvanlaenen.kolektoj.linkedlist;

import net.filipvanlaenen.kolektoj.Collection.ElementCardinality;
import net.filipvanlaenen.kolektoj.CollectionTestBase.ElementWithCollidingHash;
import net.filipvanlaenen.kolektoj.ModifiableOrderedCollectionTestBase;

/**
 * Unit tests on the {@link net.filipvanlaenen.kolektoj.linkedlist.ModifiableOrderedLinkedListCollection} class.
 */
public final class ModifiableOrderedLinkedListCollectionTest
        extends ModifiableOrderedCollectionTestBase<ModifiableOrderedLinkedListCollection<Integer>,
                ModifiableOrderedLinkedListCollection<ElementWithCollidingHash>> {
    @Override
    protected ModifiableOrderedLinkedListCollection<Integer> createCollection(
            final ModifiableOrderedLinkedListCollection<Integer> collection) {
        return new ModifiableOrderedLinkedListCollection<Integer>(collection);
    }

    @Override
    protected ModifiableOrderedLinkedListCollection<Integer> createCollection(
            final ElementCardinality elementCardinality,
            final ModifiableOrderedLinkedListCollection<Integer> collection) {
        return new ModifiableOrderedLinkedListCollection<Integer>(elementCardinality, collection);
    }

    @Override
    protected ModifiableOrderedLinkedListCollection<ElementWithCollidingHash> createCollidingHashValuesCollection(
            final ElementWithCollidingHash... elements) {
        return new ModifiableOrderedLinkedListCollection<ElementWithCollidingHash>(elements);
    }

    @Override
    protected ModifiableOrderedLinkedListCollection<Integer> createModifiableCollection(
            final ElementCardinality elementCardinality, final Integer... integers) {
        return createModifiableOrderedCollection(elementCardinality, integers);
    }

    @Override
    protected ModifiableOrderedLinkedListCollection<Integer> createModifiableCollection(
            final ElementCardinality elementCardinality,
            final ModifiableOrderedLinkedListCollection<Integer> integers) {
        return new ModifiableOrderedLinkedListCollection<Integer>(elementCardinality, integers);
    }

    @Override
    protected ModifiableOrderedLinkedListCollection<Integer> createModifiableCollection(final Integer... integers) {
        return createModifiableOrderedCollection(integers);
    }

    @Override
    protected ModifiableOrderedLinkedListCollection<Integer> createModifiableOrderedCollection(
            final ElementCardinality elementCardinality, final Integer... integers) {
        return new ModifiableOrderedLinkedListCollection<Integer>(elementCardinality, integers);
    }

    @Override
    protected ModifiableOrderedLinkedListCollection<Integer> createModifiableOrderedCollection(
            final Integer... integers) {
        return new ModifiableOrderedLinkedListCollection<Integer>(integers);
    }
}
