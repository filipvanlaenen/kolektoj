package net.filipvanlaenen.kolektoj.collectors;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import net.filipvanlaenen.kolektoj.Collection;
import net.filipvanlaenen.kolektoj.Map;
import net.filipvanlaenen.kolektoj.ModifiableCollection;
import net.filipvanlaenen.kolektoj.ModifiableOrderedCollection;
import net.filipvanlaenen.kolektoj.OrderedCollection;

/**
 * Unit tests on the {@link net.filipvanlaenen.kolektoj.collectors.Collectors} class.
 */
public class CollectorsTest {
    /**
     * The magic number three.
     */
    private static final int THREE = 3;
    /**
     * The magic number five.
     */
    private static final int FIVE = 5;

    /**
     * Verifies that collecting a stream produces a collection correctly.
     */
    @Test
    public void toCollectionShouldCollectCorrectly() {
        Collection<String> collection = Collection.of("one", "two", "three");
        Collection<String> result = collection.stream().collect(Collectors.toCollection());
        assertTrue(result.containsSame(collection));
    }

    /**
     * Verifies that collecting a stream produces a map correctly.
     */
    @Test
    public void toMapShouldCollectCorrectly() {
        Collection<String> collection = Collection.of("one", "two", "three");
        Map<Integer, String> result = collection.stream().collect(Collectors.toMap(s -> s.length(), s -> s));
        Map<Integer, String> expected = Map.of(THREE, "one", THREE, "two", FIVE, "three");
        assertTrue(result.containsSame(expected));
    }

    /**
     * Verifies that collecting a stream produces a modifiable collection correctly.
     */
    @Test
    public void toModifiableCollectionShouldCollectCorrectly() {
        Collection<String> collection = Collection.of("one", "two", "three");
        ModifiableCollection<String> result = collection.stream().collect(Collectors.toModifiableCollection());
        assertTrue(result.containsSame(collection));
    }

    /**
     * Verifies that collecting a stream produces a modifiable ordered collection correctly.
     */
    @Test
    public void toModifiableOrderedCollectionShouldCollectCorrectly() {
        OrderedCollection<String> collection = OrderedCollection.of("one", "two", "three");
        ModifiableOrderedCollection<String> result =
                collection.stream().collect(Collectors.toModifiableOrderedCollection());
        assertTrue(result.containsSame(collection));
    }

    /**
     * Verifies that collecting a stream produces an ordered collection correctly.
     */
    @Test
    public void toOrderedCollectionShouldCollectCorrectly() {
        OrderedCollection<String> collection = OrderedCollection.of("one", "two", "three");
        OrderedCollection<String> result = collection.stream().collect(Collectors.toOrderedCollection());
        assertTrue(result.containsSame(collection));
    }
}
