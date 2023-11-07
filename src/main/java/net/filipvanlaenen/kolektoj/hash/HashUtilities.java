package net.filipvanlaenen.kolektoj.hash;

/**
 * A class with utility methods for hash-backed collections.
 */
final class HashUtilities {
    /**
     * Private constructor to avoid instantiation of this utility class.
     */
    private HashUtilities() {
    }

    /**
     * Returns the hash value for an object in a hash of size <code>size</code>.
     *
     * @param object The object to hash.
     * @param size   The size of the hash.
     * @return The hash value for the object for the hash size.
     */
    static int hash(final Object object, final int size) {
        return object == null ? 0 : Math.floorMod(object.hashCode(), size);
    }
}
