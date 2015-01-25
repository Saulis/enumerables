package com.github.saulis.enumerables;

import org.junit.Test;

import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;

public class OrderTest {

    @Test
    public void itemsAreSorted() {
        Enumerable<String> strings = Enumerable.of("foo", "br", "bar4");

        Enumerable<String> sorted = strings.orderBy(x -> x.length());

        assertThat(sorted, contains("br", "foo", "bar4"));
    }

    @Test
    public void itemsAreSortedDescending() {
        Enumerable<Integer> ints = Enumerable.of(2, 3, 1);

        Enumerable<Integer> sorted = ints.orderByDescending(x -> x);

        assertThat(sorted, contains(3,2,1));
    }

    @Test
    public void minIsReturned() {
        Enumerable<Integer> ints = Enumerable.of(2, 3, 1);

        Optional<Integer> min = ints.min(x -> x);

        assertThat(min.get(), is(1));
    }

    @Test
    public void maxIsReturned() {
        Enumerable<String> strings = Enumerable.of("foo", "br", "bar4");

        Optional<String> max = strings.max(x -> x.length());

        assertThat(max.get(), is("bar4"));
    }

    @Test
    public void negativeMaxIsReturned() {
        Enumerable<Integer> enumerable = Enumerable.of(-1, -2, -3);

        assertThat(enumerable.max(x -> x).get(), is(-1));
    }

    @Test
    public void negativeMinIsReturned() {
        Enumerable<Integer> enumerable = Enumerable.of(-1, -2, -3);

        assertThat(enumerable.min(x -> x).get(), is(-3));
    }

    @Test
    public void enumerableIsReversed() {
        assertThat(Enumerable.of(1, 2, 3).reverse(), contains(3, 2, 1));
    }


}
