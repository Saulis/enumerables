package com.github.saulis.enumerables;

import org.junit.Test;

import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertThat;

public class GroupTest {

    @Test
    public void itemsAreGroupedByLength() {
        Enumerable<String> strings = Enumerable.of("foo", "bar", "baaaar", "fooooooo");

        Map<Integer, List<String>> grouped = strings.groupBy(x -> x.length());

        assertThat(grouped.get(3), contains("foo", "bar"));
        assertThat(grouped.get(6), contains("baaaar"));
        assertThat(grouped.get(8), contains("fooooooo"));
    }
}
