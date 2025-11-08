package net.filipvanlaenen.kolektoj;

import java.util.Comparator;

public interface Range<E> {
    public record All<E>() implements Range<E> {
        @Override
        public boolean isAbove(Comparator<? super E> comparator, E element) {
            return false;
        }

        @Override
        public boolean isBelow(Comparator<? super E> comparator, E element) {
            return false;
        }
    }

    public record EqualTo<E>(E value) implements Range<E> {
        @Override
        public boolean isAbove(Comparator<? super E> comparator, E element) {
            return comparator.compare(element, value) > 0;
        }

        @Override
        public boolean isBelow(Comparator<? super E> comparator, E element) {
            return comparator.compare(element, value) < 0;
        }
    }

    public record GreaterThan<E>(E lower) implements Range<E> {
        public GreaterThanAndLessThan<E> lessThan(E upper) {
            return new GreaterThanAndLessThan<E>(lower, upper);
        }

        public GreaterThanAndLessThanOrEqualTo<E> lessThanOrEqualTo(E upper) {
            return new GreaterThanAndLessThanOrEqualTo<E>(lower, upper);
        }

        @Override
        public boolean isAbove(Comparator<? super E> comparator, E element) {
            return false;
        }

        @Override
        public boolean isBelow(Comparator<? super E> comparator, E element) {
            return comparator.compare(element, lower) <= 0;
        }
    }

    public record GreaterThanOrEqualTo<E>(E lower) implements Range<E> {
        public GreaterThanOrEqualToAndLessThan<E> lessThan(E upper) {
            return new GreaterThanOrEqualToAndLessThan<E>(lower, upper);
        }

        public GreaterThanOrEqualToAndLessThanOrEqualTo<E> lessThanOrEqualTo(E upper) {
            return new GreaterThanOrEqualToAndLessThanOrEqualTo<E>(lower, upper);
        }

        @Override
        public boolean isAbove(Comparator<? super E> comparator, E element) {
            return false;
        }

        @Override
        public boolean isBelow(Comparator<? super E> comparator, E element) {
            return comparator.compare(element, lower) < 0;
        }
    }

    public record LessThan<E>(E upper) implements Range<E> {
        public GreaterThanAndLessThan<E> greaterThan(E lower) {
            return new GreaterThanAndLessThan<E>(lower, upper);
        }

        public GreaterThanOrEqualToAndLessThan<E> greaterThanOrEqualTo(E lower) {
            return new GreaterThanOrEqualToAndLessThan<E>(lower, upper);
        }

        @Override
        public boolean isAbove(Comparator<? super E> comparator, E element) {
            return comparator.compare(element, upper) >= 0;
        }

        @Override
        public boolean isBelow(Comparator<? super E> comparator, E element) {
            return false;
        }
    }

    public record LessThanOrEqualTo<E>(E upper) implements Range<E> {
        public GreaterThanAndLessThanOrEqualTo<E> greaterThan(E lower) {
            return new GreaterThanAndLessThanOrEqualTo<E>(lower, upper);
        }

        public GreaterThanOrEqualToAndLessThanOrEqualTo<E> greaterThanOrEqualTo(E lower) {
            return new GreaterThanOrEqualToAndLessThanOrEqualTo<E>(lower, upper);
        }

        @Override
        public boolean isAbove(Comparator<? super E> comparator, E element) {
            return comparator.compare(element, upper) > 0;
        }

        @Override
        public boolean isBelow(Comparator<? super E> comparator, E element) {
            return false;
        }
    }

    public record GreaterThanAndLessThan<E>(E lower, E upper) implements Range<E> {
        @Override
        public boolean isAbove(Comparator<? super E> comparator, E element) {
            return comparator.compare(element, upper) >= 0;
        }

        @Override
        public boolean isBelow(Comparator<? super E> comparator, E element) {
            return comparator.compare(element, lower) <= 0;
        }
    }

    public record GreaterThanAndLessThanOrEqualTo<E>(E lower, E upper) implements Range<E> {
        @Override
        public boolean isAbove(Comparator<? super E> comparator, E element) {
            return comparator.compare(element, upper) > 0;
        }

        @Override
        public boolean isBelow(Comparator<? super E> comparator, E element) {
            return comparator.compare(element, lower) <= 0;
        }
    }

    public record GreaterThanOrEqualToAndLessThan<E>(E lower, E upper) implements Range<E> {
        @Override
        public boolean isAbove(Comparator<? super E> comparator, E element) {
            return comparator.compare(element, upper) >= 0;
        }

        @Override
        public boolean isBelow(Comparator<? super E> comparator, E element) {
            return comparator.compare(element, lower) < 0;
        }
    }

    public record GreaterThanOrEqualToAndLessThanOrEqualTo<E>(E lower, E upper) implements Range<E> {
        @Override
        public boolean isAbove(Comparator<? super E> comparator, E element) {
            return comparator.compare(element, upper) > 0;
        }

        @Override
        public boolean isBelow(Comparator<? super E> comparator, E element) {
            return comparator.compare(element, lower) < 0;
        }
    }

    public record None<E>() implements Range<E> {
        @Override
        public boolean isAbove(Comparator<? super E> comparator, E element) {
            return true;
        }

        @Override
        public boolean isBelow(Comparator<? super E> comparator, E element) {
            return true;
        }
    }

    static <F> All<F> all() {
        return new All<F>();
    }

    static <F> EqualTo<F> equalTo(F value) {
        return new EqualTo<F>(value);
    }

    static <F> GreaterThan<F> greaterThan(F lower) {
        return new GreaterThan<F>(lower);
    }

    static <F> GreaterThanOrEqualTo<F> greaterThanOrEqualTo(F lower) {
        return new GreaterThanOrEqualTo<F>(lower);
    }

    static <F> LessThan<F> lessThan(F upper) {
        return new LessThan<F>(upper);
    }

    static <F> LessThanOrEqualTo<F> lessThanOrEqualTo(F upper) {
        return new LessThanOrEqualTo<F>(upper);
    }

    static <F> None<F> none() {
        return new None<F>();
    }

    boolean isAbove(Comparator<? super E> comparator, E element);

    boolean isBelow(Comparator<? super E> comparator, E element);
}
