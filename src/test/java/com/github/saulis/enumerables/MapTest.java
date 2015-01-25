package com.github.saulis.enumerables;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsIterableContainingInOrder.contains;

public class MapTest {
    @Test
    public void itemsAreMapped() {
        Enumerable<String> enumerable = Enumerable.of("foo", "bar");

        Enumerable<String> mapped = enumerable.map(x -> x + "bar");

        assertThat(mapped, contains("foobar", "barbar"));
    }
}
