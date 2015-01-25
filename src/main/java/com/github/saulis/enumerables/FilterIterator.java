package com.github.saulis.enumerables;

import java.util.Iterator;
import java.util.function.BiPredicate;

public class FilterIterator<T> implements Iterator<T> {

    private final Iterator<T> iterator;
    private final BiPredicate<T, Integer> predicate;
    private int cursor;
    private T nextItemToReturn = null;

    public FilterIterator(Iterable<T> iterable, BiPredicate<T,Integer> predicate) {
        iterator = iterable.iterator();
        this.predicate = predicate;
        this.cursor = 0;
    }

    @Override
    public boolean hasNext() {
        while(iterator.hasNext()) {
            T next = iterator.next();
            if(predicate.test(next, cursor++)) {
                nextItemToReturn = next;

                return true;
            }
        }

        return false;
    }

    @Override
    public T next() {
        T next = nextItemToReturn;
        nextItemToReturn = null;

        return next;
    }
}
