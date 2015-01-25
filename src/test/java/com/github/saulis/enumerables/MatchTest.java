package com.github.saulis.enumerables;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class MatchTest {
    @Test
    public void anyItemIsMatched() {
        Enumerable<String> strings = Enumerable.of("foo", "bar");

        assertFalse(strings.anyMatch(x -> x.startsWith("a")));
        assertTrue(strings.anyMatch(x -> x.startsWith("b")));
    }

    @Test
    public void allItemsAreMatched() {
        Enumerable<String> strings = Enumerable.of("foo", "bar");

        assertFalse(strings.allMatch(x -> x.equals("foo")));
        assertTrue(strings.allMatch(x -> x.length() == 3));
    }

    @Test
    public void noItemsMatch() {
        Enumerable<String> strings = Enumerable.of("foo", "bar");

        assertFalse(strings.noneMatch(x -> x.equals("foo")));
        assertTrue(strings.noneMatch(x -> x.length() == 2));
    }

    @Test
    public void itemIsContained() {
        assertTrue(Enumerable.of("foo", "bar").contains("bar"));
    }
}
