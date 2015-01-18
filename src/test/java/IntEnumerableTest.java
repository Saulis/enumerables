import org.junit.Test;

import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertFalse;

public class IntEnumerableTest {

    @Test
    public void emptySumIsCalculated() {
        Enumerable<Integer> empty = Enumerable.empty();

        assertFalse(empty.mapToInt(x -> x.intValue()).sum().isPresent());
    }

    @Test
    public void integerSumIsCalculated() {
        Enumerable<String> strings = Enumerable.of("foo", "bar", "bah");

        Optional<Integer> sum = strings.mapToInt(x -> x.length()).sum();

        assertThat(sum.get(), is(9));
    }

    @Test
    public void emptyAverageIsCalculated() {
        Enumerable<Integer> empty = Enumerable.empty();

        assertFalse(empty.mapToInt(x -> x).average().isPresent());
    }

    @Test
    public void averageIsCalculated() {
        IntEnumerable range = Enumerable.range(1, 10);

        Optional<Double> average = range.average();

        assertThat(average.get(), is(5.5));
    }

    @Test
    public void minIsReturned() {
        assertThat(Enumerable.range(1, 10).min().get(), is(1));
    }

    @Test
    public void maxIsReturned() {
        assertThat(Enumerable.range(1, 10).max().get(), is(10));
    }
}
