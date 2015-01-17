import org.junit.Test;

import java.util.ArrayList;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class CopyTest {
    @Test
    public void enumerableDoesNoChangeAfterCopy() {
        ArrayList<String> list = new ArrayList<>();
        list.add("foo");

        Enumerable<String> strings = Enumerable.of(list).copy();

        list.add("bar");
        assertThat(strings.count(), is(1));
    }

    @Test
    public void saveCreatesACopyAfterFirstIteration() {
        ArrayList<String> list = new ArrayList<>();
        list.add("foo");

        Enumerable<String> strings = Enumerable.of(list).save();

        list.add("bar");
        assertThat(strings.count(), is(2)); //count() will trigger iteration.

        list.add("foobar");
        assertThat(strings.count(), is(2));
    }

}
