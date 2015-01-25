package com.github.saulis.enumerables;

import java.util.function.BiFunction;
import java.util.function.Function;

public class Accumulator<T, R> implements Function<T, R> {
    private R accumulatedValue;
    private R seed;
    private final BiFunction<R, T, R> reductionFunction;

    public Accumulator(R seed, BiFunction<R, T, R> reductionFunction) {
        this.seed = seed;
        this.accumulatedValue = seed;
        this.reductionFunction = reductionFunction;
    }

    @Override
    public R apply(T t) {
        accumulatedValue = reductionFunction.apply(accumulatedValue, t);

        return accumulatedValue;
    }
}
