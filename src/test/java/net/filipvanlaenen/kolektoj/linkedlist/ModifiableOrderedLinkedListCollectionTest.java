package net.filipvanlaenen.kolektoj.linkedlist;

import net.filipvanlaenen.kolektoj.ModifiableCollectionTestBase;
import net.filipvanlaenen.kolektoj.Collection.ElementCardinality;

/**
 * Unit tests on the {@link net.filipvanlaenen.kolektoj.linkedlist.ModifiableOrderedLinkedListCollection} class.
 */
public final class ModifiableOrderedLinkedListCollectionTest
        extends ModifiableCollectionTestBase<ModifiableOrderedLinkedListCollection<Integer>> {

    @Override
    protected ModifiableOrderedLinkedListCollection<Integer> createModifiableCollection(final Integer... integers) {
        return new ModifiableOrderedLinkedListCollection<Integer>(integers);
    }

    @Override
    protected ModifiableOrderedLinkedListCollection<Integer> createModifiableCollection(
            final ElementCardinality elementCardinality, final Integer... integers) {
        return new ModifiableOrderedLinkedListCollection<Integer>(elementCardinality, integers);
    }
}
