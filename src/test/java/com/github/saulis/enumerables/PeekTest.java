package com.github.saulis.enumerables;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class PeekTest {

    private ArrayList<Integer> peekedItems;

    @Before
    public void setup() {
        peekedItems = new ArrayList<>();
    }

    @Test
    public void emptyEnumerableIsPeeked() {
        Enumerable<Integer> empty = Enumerable.empty();
        int count = empty.peek(x -> peekedItems.add(x)).count();

        assertThat(count, is(peekedItems.size()));
    }

    @Test
    public void enumerableIsPeeked() {
        Enumerable<Integer> range = Enumerable.range(1, 10);

        int count = range.peek(x -> peekedItems.add(x)).count();

        assertThat(count, is(peekedItems.size()));
    }

}
