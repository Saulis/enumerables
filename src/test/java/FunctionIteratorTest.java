import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;

public class FunctionIteratorTest {
    @Test
    public void rangeIsConstructed() {
        Enumerable<Integer> range
                = new Enumerable<>(() -> new FunctionIterator<>(1, x -> x + 1, 10));

        assertThat(range, contains(1, 2, 3, 4, 5, 6, 7, 8, 9, 10));
    }
}