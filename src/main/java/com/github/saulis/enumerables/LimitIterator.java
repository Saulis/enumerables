package com.github.saulis.enumerables;

import java.util.Iterator;

public class LimitIterator<T> implements Iterator<T> {

    private final Iterator<T> iterator;
    private final long n;
    private int cursor;

    public LimitIterator(Iterable<T> iterable, long n) {
        this.cursor = 0;
        this.iterator = iterable.iterator();
        this.n = n;
    }

    @Override
    public boolean hasNext() {
        return cursor < n && iterator.hasNext();
    }

    @Override
    public T next() {
        cursor++;
        return iterator.next();
    }
}
