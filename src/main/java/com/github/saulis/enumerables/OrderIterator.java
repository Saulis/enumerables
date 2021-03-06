package com.github.saulis.enumerables;

import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class OrderIterator<T> implements Iterator<T> {
    private Iterator<T> iterator;
    private final Comparator<T> comparator;
    private Iterator<T> orderedIterator;

    public OrderIterator(Iterator<T> iterator, Comparator<T> comparator) {
        this.iterator = iterator;
        this.comparator = comparator;
    }

    private Iterator<T> getOrderedIterator() {
        if(orderedIterator == null) {
            List<T> list = getList();
            list.sort(comparator);

            orderedIterator = list.iterator();
        }

        return orderedIterator;
    }

    private List<T> getList() {
        List<T> list = new LinkedList<>();
        iterator.forEachRemaining(x -> list.add(x));

        return list;
    }

    @Override
    public boolean hasNext() {
        return getOrderedIterator().hasNext();
    }

    @Override
    public T next() {
        return getOrderedIterator().next();
    }
}