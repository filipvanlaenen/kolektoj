package net.filipvanlaenen.kolektoj;

import java.util.Comparator;

/**
 * Interface defining what a range is.
 *
 * @param <E> The elemnt type.
 */
public interface Range<E> {
    /**
     * A range including all values.
     *
     * @param <E> The element type.
     */
    public record All<E>() implements Range<E> {
        @Override
        public boolean isAbove(final Comparator<? super E> comparator, final E element) {
            return false;
        }

        @Override
        public boolean isBelow(final Comparator<? super E> comparator, final E element) {
            return false;
        }
    }

    /**
     * A range with only one value.
     *
     * @param value The value.
     * @param <E>   The element type.
     */
    public record EqualTo<E>(E value) implements Range<E> {
        @Override
        public boolean isAbove(final Comparator<? super E> comparator, final E element) {
            return comparator.compare(element, value) > 0;
        }

        @Override
        public boolean isBelow(final Comparator<? super E> comparator, final E element) {
            return comparator.compare(element, value) < 0;
        }
    }

    /**
     * A range with all values greater than a lower boundary.
     *
     * @param lower The lower boundary.
     * @param <E>   The element type.
     */
    public record GreaterThan<E>(E lower) implements Range<E> {
        /**
         * Returns a new range restricting the current range to values less than an upper boundary.
         *
         * @param upper The upper boundary.
         * @return A new range restricting the current range to values less than an upper boundary.
         */
        public GreaterThanAndLessThan<E> lessThan(final E upper) {
            return new GreaterThanAndLessThan<E>(lower, upper);
        }

        /**
         * Returns a new range restricting the current range to values less than or equal to an upper boundary.
         *
         * @param upper The upper boundary.
         * @return A new range restricting the current range to values less than or equal to an upper boundary.
         */
        public GreaterThanAndLessThanOrEqualTo<E> lessThanOrEqualTo(final E upper) {
            return new GreaterThanAndLessThanOrEqualTo<E>(lower, upper);
        }

        @Override
        public boolean isAbove(final Comparator<? super E> comparator, final E element) {
            return false;
        }

        @Override
        public boolean isBelow(final Comparator<? super E> comparator, final E element) {
            return comparator.compare(element, lower) <= 0;
        }
    }

    /**
     * A range with all values greater than a lower boundary and less than an upper boundary.
     *
     * @param lower The lower boundary.
     * @param upper The upper boundary.
     * @param <E>   The element type.
     */
    public record GreaterThanAndLessThan<E>(E lower, E upper) implements Range<E> {
        @Override
        public boolean isAbove(final Comparator<? super E> comparator, final E element) {
            return comparator.compare(element, upper) >= 0;
        }

        @Override
        public boolean isBelow(final Comparator<? super E> comparator, final E element) {
            return comparator.compare(element, lower) <= 0;
        }
    }

    /**
     * A range with all values greater than a lower boundary and less than or equal to an upper boundary.
     *
     * @param lower The lower boundary.
     * @param upper The upper boundary.
     * @param <E>   The element type.
     */
    public record GreaterThanAndLessThanOrEqualTo<E>(E lower, E upper) implements Range<E> {
        @Override
        public boolean isAbove(final Comparator<? super E> comparator, final E element) {
            return comparator.compare(element, upper) > 0;
        }

        @Override
        public boolean isBelow(final Comparator<? super E> comparator, final E element) {
            return comparator.compare(element, lower) <= 0;
        }
    }

    /**
     * A range with all values greater than or equal to a lower boundary.
     *
     * @param lower The lower boundary.
     * @param <E>   The element type.
     */
    public record GreaterThanOrEqualTo<E>(E lower) implements Range<E> {
        /**
         * Returns a new range restricting the current range to values less than an upper boundary.
         *
         * @param upper The upper boundary.
         * @return A new range restricting the current range to values less than an upper boundary.
         */
        public GreaterThanOrEqualToAndLessThan<E> lessThan(final E upper) {
            return new GreaterThanOrEqualToAndLessThan<E>(lower, upper);
        }

        /**
         * Returns a new range restricting the current range to values less than or equal to an upper boundary.
         *
         * @param upper The upper boundary.
         * @return A new range restricting the current range to values less than or equal to an upper boundary.
         */
        public GreaterThanOrEqualToAndLessThanOrEqualTo<E> lessThanOrEqualTo(final E upper) {
            return new GreaterThanOrEqualToAndLessThanOrEqualTo<E>(lower, upper);
        }

        @Override
        public boolean isAbove(final Comparator<? super E> comparator, final E element) {
            return false;
        }

        @Override
        public boolean isBelow(final Comparator<? super E> comparator, final E element) {
            return comparator.compare(element, lower) < 0;
        }
    }

    /**
     * A range with all values greater than or equal to a lower boundary and less than an upper boundary.
     *
     * @param lower The lower boundary.
     * @param upper The upper boundary.
     * @param <E>   The element type.
     */
    public record GreaterThanOrEqualToAndLessThan<E>(E lower, E upper) implements Range<E> {
        @Override
        public boolean isAbove(final Comparator<? super E> comparator, final E element) {
            return comparator.compare(element, upper) >= 0;
        }

        @Override
        public boolean isBelow(final Comparator<? super E> comparator, final E element) {
            return comparator.compare(element, lower) < 0;
        }
    }

    /**
     * A range with all values greater than or equal to a lower boundary and less than or equal to an upper boundary.
     *
     * @param lower The lower boundary.
     * @param upper The upper boundary.
     * @param <E>   The element type.
     */
    public record GreaterThanOrEqualToAndLessThanOrEqualTo<E>(E lower, E upper) implements Range<E> {
        @Override
        public boolean isAbove(final Comparator<? super E> comparator, final E element) {
            return comparator.compare(element, upper) > 0;
        }

        @Override
        public boolean isBelow(final Comparator<? super E> comparator, final E element) {
            return comparator.compare(element, lower) < 0;
        }
    }

    /**
     * A range with all values less than an upper boundary.
     *
     * @param upper The upper boundary.
     * @param <E>   The element type.
     */
    public record LessThan<E>(E upper) implements Range<E> {
        /**
         * Returns a new range restricting the current range to values greater than a lower boundary.
         *
         * @param lower The lower boundary.
         * @return A new range restricting the current range to values greater than a lower boundary.
         */
        public GreaterThanAndLessThan<E> greaterThan(final E lower) {
            return new GreaterThanAndLessThan<E>(lower, upper);
        }

        /**
         * Returns a new range restricting the current range to values greater than or equal to a lower boundary.
         *
         * @param lower The lower boundary.
         * @return A new range restricting the current range to values greater than or equal to a lower boundary.
         */
        public GreaterThanOrEqualToAndLessThan<E> greaterThanOrEqualTo(final E lower) {
            return new GreaterThanOrEqualToAndLessThan<E>(lower, upper);
        }

        @Override
        public boolean isAbove(final Comparator<? super E> comparator, final E element) {
            return comparator.compare(element, upper) >= 0;
        }

        @Override
        public boolean isBelow(final Comparator<? super E> comparator, final E element) {
            return false;
        }
    }

    /**
     * A range with all values less than or equal to an upper boundary.
     *
     * @param upper The upper boundary.
     * @param <E>   The element type.
     */
    public record LessThanOrEqualTo<E>(E upper) implements Range<E> {
        /**
         * Returns a new range restricting the current range to values greater than a lower boundary.
         *
         * @param lower The lower boundary.
         * @return A new range restricting the current range to values greater than a lower boundary.
         */
        public GreaterThanAndLessThanOrEqualTo<E> greaterThan(final E lower) {
            return new GreaterThanAndLessThanOrEqualTo<E>(lower, upper);
        }

        /**
         * Returns a new range restricting the current range to values greater than or equal to a lower boundary.
         *
         * @param lower The lower boundary.
         * @return A new range restricting the current range to values greater than or equal to a lower boundary.
         */
        public GreaterThanOrEqualToAndLessThanOrEqualTo<E> greaterThanOrEqualTo(final E lower) {
            return new GreaterThanOrEqualToAndLessThanOrEqualTo<E>(lower, upper);
        }

        @Override
        public boolean isAbove(final Comparator<? super E> comparator, final E element) {
            return comparator.compare(element, upper) > 0;
        }

        @Override
        public boolean isBelow(final Comparator<? super E> comparator, final E element) {
            return false;
        }
    }

    /**
     * A range containing no values.
     *
     * @param <E> The element type.
     */
    public record None<E>() implements Range<E> {
        @Override
        public boolean isAbove(final Comparator<? super E> comparator, final E element) {
            return true;
        }

        @Override
        public boolean isBelow(final Comparator<? super E> comparator, final E element) {
            return true;
        }
    }

    /**
     * Returns a range with all values.
     *
     * @param <F> The element type.
     * @return A range with all values.
     */
    static <F> All<F> all() {
        return new All<F>();
    }

    /**
     * Returns a range with a single value.
     *
     * @param <F>   The element type.
     * @param value The value.
     * @return A range with a single value.
     */
    static <F> EqualTo<F> equalTo(final F value) {
        return new EqualTo<F>(value);
    }

    /**
     * Returns a range with all values greater than a lower boundary.
     *
     * @param <F>   The element type.
     * @param lower The lower boundary.
     * @return A range with all values greater than a lower boundary.
     */
    static <F> GreaterThan<F> greaterThan(final F lower) {
        return new GreaterThan<F>(lower);
    }

    /**
     * Returns a range with all values greater than or equal to a lower boundary.
     *
     * @param <F>   The element type.
     * @param lower The lower boundary.
     * @return A range with all values greater than or equal to a lower boundary.
     */
    static <F> GreaterThanOrEqualTo<F> greaterThanOrEqualTo(final F lower) {
        return new GreaterThanOrEqualTo<F>(lower);
    }

    /**
     * Returns a range with all values less than an upper boundary.
     *
     * @param <F>   The element type.
     * @param upper The upper boundary.
     * @return A range with all values less than an upper boundary.
     */
    static <F> LessThan<F> lessThan(final F upper) {
        return new LessThan<F>(upper);
    }

    /**
     * Returns a range with all values less than an or equal to upper boundary.
     *
     * @param <F>   The element type.
     * @param upper The upper boundary.
     * @return A range with all values less than an or equal to upper boundary.
     */
    static <F> LessThanOrEqualTo<F> lessThanOrEqualTo(final F upper) {
        return new LessThanOrEqualTo<F>(upper);
    }

    /**
     * Returns a range with no values.
     *
     * @param <F> The element type.
     * @return A range with no values.
     */
    static <F> None<F> none() {
        return new None<F>();
    }

    /**
     * Returns true if the provided element is above the range according to the provided comparator.
     *
     * @param comparator The comparator.
     * @param element    The element.
     * @return True if the provided element is above the range according to the provided comparator, and false
     *         otherwise.
     */
    boolean isAbove(Comparator<? super E> comparator, E element);

    /**
     * Returns true if the provided element is below the range according to the provided comparator.
     *
     * @param comparator The comparator.
     * @param element    The element.
     * @return True if the provided element is below the range according to the provided comparator, and false
     *         otherwise.
     */
    boolean isBelow(Comparator<? super E> comparator, E element);
}
