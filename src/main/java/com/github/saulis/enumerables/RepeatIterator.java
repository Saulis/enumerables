package com.github.saulis.enumerables;

import java.util.Iterator;
import java.util.function.Supplier;

public class RepeatIterator<T> implements Iterator<T> {

    private final Supplier<T> supplier;
    private final int iterations;
    private int cursor;

    public RepeatIterator(Supplier<T> supplier, int iterations) {
        this.supplier = supplier;
        this.iterations = iterations;
        this.cursor = 0;
    }

    @Override
    public boolean hasNext() {
        return cursor < iterations;
    }

    @Override
    public T next() {
        cursor++;

        return supplier.get();
    }
}
