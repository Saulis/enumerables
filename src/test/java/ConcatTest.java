import org.junit.Test;

import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;

public class ConcatTest {

    @Test
    public void emptyIsConcatByIntegers() {
        Enumerable<Integer> empty = Enumerable.empty();

        Enumerable<Integer> join = empty.concat(1, 2, 3);

        assertThat(join, contains(1,2,3));
    }

    @Test
    public void multipleCollectionsAreConcat() {
        Enumerable<String> foo = Enumerable.of("foo");
        Enumerable<String> bar = Enumerable.of("bar");
        List<String> ints = Enumerable.range(1, 3)
                                      .map(x -> x.toString())
                                      .collect(Collectors.toList());

        Enumerable<String> joined = foo.concat("nice").concat(bar).concat(ints);

        assertThat(joined, contains("foo", "nice", "bar", "1", "2", "3"));
    }
}
