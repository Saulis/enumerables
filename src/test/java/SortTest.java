import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;

public class SortTest {

    @Test
    public void itemsAreSorted() {
        Enumerable<String> strings = Enumerable.of("foo", "br", "bar4");

        Enumerable<String> sorted = strings.sort(x -> x.length());

        assertThat(sorted, contains("br", "foo", "bar4"));
    }

    @Test
    public void itemsAreSortedReversed() {
        Enumerable<Integer> ints = Enumerable.of(2, 3, 1);

        Enumerable<Integer> sorted = ints.sortReversed(x -> x);

        assertThat(sorted, contains(3,2,1));
    }
}
