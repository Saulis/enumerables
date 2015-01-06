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

}
