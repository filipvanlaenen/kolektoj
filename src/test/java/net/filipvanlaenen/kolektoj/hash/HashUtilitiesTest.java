package net.filipvanlaenen.kolektoj.hash;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * Unit tests on the {@link net.filipvanlaenen.kolektoj.hash.HashUtilities} class.
 */
public class HashUtilitiesTest {
    /**
     * The magic number five.
     */
    private static final int FIVE = 5;

    /**
     * Class with an integer field that is returned as its hash code.
     */
    private final class IntegerFieldObject {
        /**
         * The integer field that is returned as the object's hash code.
         */
        private final int field;

        /**
         * Constructor with the integer field as its parameter.
         *
         * @param field The integer field to be returned as hash code.
         */
        IntegerFieldObject(final int field) {
            this.field = field;
        }

        @Override
        public boolean equals(final Object other) {
            return this == other;
        }

        @Override
        public int hashCode() {
            return field;
        }
    }

    /**
     * Verifies that the hash value for <code>null</code> is 0.
     */
    @Test
    public void hashShouldReturnZeroForNull() {
        assertEquals(0, HashUtilities.hash(null, 2));
    }

    /**
     * Verifies that the hash value of an object with hash code 5 for a hash size of 2 is 1.
     */
    @Test
    public void hashShouldReturnHashCodeModuloSize() {
        assertEquals(1, HashUtilities.hash(new IntegerFieldObject(FIVE), 2));
    }

    /**
     * Verifies that the hash value is positive even for objects with a negative hash code.
     */
    @Test
    public void hashShouldBePositive() {
        assertEquals(1, HashUtilities.hash(new IntegerFieldObject(-1), 2));
    }
}
