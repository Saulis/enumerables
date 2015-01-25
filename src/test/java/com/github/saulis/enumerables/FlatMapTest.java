package com.github.saulis.enumerables;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;

public class FlatMapTest {

    @Test
    public void arraysAreFlatMapped() {
        Enumerable<String> foobars = Enumerable.of("foobar foobar foobar foobar".split(" "));

        Enumerable<String[]> splits = foobars.map(x -> x.split("oo"));
        Enumerable<String> flattened = splits.flatMap(x -> x);

        assertThat(flattened, contains("f", "bar", "f", "bar", "f", "bar", "f", "bar"));

    }

    @Test
    public void iterablesAreFlatMapped() {
        Enumerable<String> foobars = Enumerable.of("foobar foobar foobar foobar".split(" "));

        Enumerable<String> flattened = foobars.flatMap(x -> x.split("oo"));

        assertThat(flattened, contains("f", "bar", "f", "bar", "f", "bar", "f", "bar"));

    }

}
