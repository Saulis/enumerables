package com.github.saulis.enumerables;

import java.util.Iterator;
import java.util.List;

public class SplitIterator<T> implements Iterator<T> {

    private final IteratorSplitter<T> splitter;
    private final List<T> list;
    private int cursor;

    public SplitIterator(IteratorSplitter<T> splitter, List<T> list) {

        this.cursor = 0;
        this.splitter = splitter;
        this.list = list;
    }

    @Override
    public boolean hasNext() {
        while(true) {
            if(list.size() > cursor) {
                return true;
            }

            if(!splitter.hasNext()) {
                return false;
            }

            splitter.iterate();
        }
    }

    @Override
    public T next() {
        return list.get(cursor++);
    }
}
