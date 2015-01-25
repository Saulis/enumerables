package com.github.saulis.enumerables;

import java.util.Iterator;
import java.util.function.Function;

public class MapIterator<T, R> implements Iterator<R> {

    private final Iterator<T> iterator;
    private final Function<T, R> func;

    public MapIterator(Iterable<T> iterable, Function<T, R> mapFunction) {
        iterator = iterable.iterator();
        func = mapFunction;
    }

    @Override
    public boolean hasNext() {
        return iterator.hasNext();
    }

    @Override
    public R next() {
        return func.apply(iterator.next());
    }
}
