package net.filipvanlaenen.kolektoj.collectors;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import net.filipvanlaenen.kolektoj.Collection;

/**
 * Unit tests on the {@link net.filipvanlaenen.kolektoj.collectors.Collectors} class.
 */
public class CollectorsTest {
    /**
     * Verifies that collecting a stream produces a collection correctly.
     */
    @Test
    public void toCollectionShouldCollectCorrectly() {
        Collection<String> collection = Collection.of("one", "two", "three");
        Collection<String> result = collection.stream().collect(Collectors.toCollection());
        assertTrue(result.containsSame(collection));
    }
}
