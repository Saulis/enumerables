package com.github.saulis.enumerables;

import java.util.Iterator;
import java.util.function.Function;
import java.util.function.Predicate;

public class FunctionIterator<T> implements Iterator<T> {

    private final Predicate<Integer> predicate;
    private T seed;
    private final Function<T, T> function;
    private int cursor;

    public FunctionIterator(T seed, Function<T, T> function, Predicate<Integer> predicate) {
        cursor = 0;
        this.seed = seed;
        this.function = function;
        this.predicate = predicate;
    }

    public FunctionIterator(T seed, Function<T, T> function, int iterations) {
        this(seed, function, x -> x < iterations);
    }

    @Override
    public boolean hasNext() {
        return predicate.test(cursor);
    }

    @Override
    public T next() {
        cursor++;
        T value = seed;

        seed = function.apply(value);

        return value;
    }
}
