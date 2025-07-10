package net.filipvanlaenen.kolektoj.collectors;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import net.filipvanlaenen.kolektoj.Collection;
import net.filipvanlaenen.kolektoj.Map;
import net.filipvanlaenen.kolektoj.ModifiableCollection;
import net.filipvanlaenen.kolektoj.ModifiableMap;
import net.filipvanlaenen.kolektoj.ModifiableOrderedCollection;
import net.filipvanlaenen.kolektoj.OrderedCollection;
import net.filipvanlaenen.kolektoj.UpdatableMap;

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
     * Verifies that collecting a parallel stream produces a collection correctly.
     */
    @Test
    public void toCollectionOnParallelStreamShouldCollectCorrectly() {
        Collection<String> collection = Collection.of("one", "two", "three");
        Collection<String> result = collection.stream().parallel().collect(Collectors.toCollection());
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
     * Verifies that collecting a parallel stream produces a map correctly.
     */
    @Test
    public void toMapOnParallelStreamShouldCollectCorrectly() {
        Collection<String> collection = Collection.of("one", "two", "three");
        Map<Integer, String> result = collection.stream().parallel().collect(Collectors.toMap(s -> s.length(), s -> s));
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
     * Verifies that collecting a parallel stream produces a modifiable collection correctly.
     */
    @Test
    public void toModifiableCollectionOnParallelStreamShouldCollectCorrectly() {
        Collection<String> collection = Collection.of("one", "two", "three");
        ModifiableCollection<String> result =
                collection.stream().parallel().collect(Collectors.toModifiableCollection());
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
     * Verifies that collecting a stream produces modifiable map correctly.
     */
    @Test
    public void toModifiableMapShouldCollectCorrectly() {
        Collection<String> collection = Collection.of("one", "two", "three");
        ModifiableMap<Integer, String> result =
                collection.stream().collect(Collectors.toModifiableMap(s -> s.length(), s -> s));
        Map<Integer, String> expected = Map.of(THREE, "one", THREE, "two", FIVE, "three");
        assertTrue(result.containsSame(expected));
    }

    /**
     * Verifies that collecting a parallel stream produces modifiable map correctly.
     */
    @Test
    public void toModifiableMapOnParallelStreamShouldCollectCorrectly() {
        Collection<String> collection = Collection.of("one", "two", "three");
        ModifiableMap<Integer, String> result =
                collection.stream().parallel().collect(Collectors.toModifiableMap(s -> s.length(), s -> s));
        Map<Integer, String> expected = Map.of(THREE, "one", THREE, "two", FIVE, "three");
        assertTrue(result.containsSame(expected));
    }

    /**
     * Verifies that collecting a parallel stream produces a modifiable ordered collection correctly.
     */
    @Test
    public void toModifiableOrderedCollectionOnParallelStreamShouldCollectCorrectly() {
        OrderedCollection<String> collection = OrderedCollection.of("one", "two", "three");
        ModifiableOrderedCollection<String> result =
                collection.stream().parallel().collect(Collectors.toModifiableOrderedCollection());
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

    /**
     * Verifies that collecting a parallel stream produces an ordered collection correctly.
     */
    @Test
    public void toOrderedCollectionOnParallelStreamShouldCollectCorrectly() {
        OrderedCollection<String> collection = OrderedCollection.of("one", "two", "three");
        OrderedCollection<String> result = collection.stream().parallel().collect(Collectors.toOrderedCollection());
        assertTrue(result.containsSame(collection));
    }

    /**
     * Verifies that collecting a stream produces an updatable map correctly.
     */
    @Test
    public void toUpdatableMapShouldCollectCorrectly() {
        Collection<String> collection = Collection.of("one", "two", "three");
        UpdatableMap<Integer, String> result =
                collection.stream().collect(Collectors.toUpdatableMap(s -> s.length(), s -> s));
        Map<Integer, String> expected = Map.of(THREE, "one", THREE, "two", FIVE, "three");
        assertTrue(result.containsSame(expected));
    }

    /**
     * Verifies that collecting a parallel stream produces an updatable map correctly.
     */
    @Test
    public void toUpdatableMapOnParallelStreamShouldCollectCorrectly() {
        Collection<String> collection = Collection.of("one", "two", "three");
        UpdatableMap<Integer, String> result =
                collection.stream().parallel().collect(Collectors.toUpdatableMap(s -> s.length(), s -> s));
        Map<Integer, String> expected = Map.of(THREE, "one", THREE, "two", FIVE, "three");
        assertTrue(result.containsSame(expected));
    }
}
