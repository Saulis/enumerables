import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasItems;

public class RangeIteratorTest {
    @Test
    public void fibonacciIsIterated() {
        Enumerable<Integer> fibonacci
                = new Enumerable<>(() -> new RangeIterator<>(1, x -> x + x, 10));

        assertThat(fibonacci, hasItems(1, 1, 2, 3, 5, 8, 13, 21, 34, 55));
    }

    @Test
    public void rangeIsConstructed() {
        Enumerable<Integer> range
                = new Enumerable<>(() -> new RangeIterator<>(0, x -> x + 1, 10));

        assertThat(range, contains(1, 2, 3, 4, 5, 6, 7, 8, 9, 10));
    }
}