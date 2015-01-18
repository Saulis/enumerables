import org.junit.Test;

import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertFalse;

public class LongEnumerableTest {

    @Test
    public void emptySumIsCalculated() {
        Enumerable<Long> empty = Enumerable.empty();

        assertFalse(empty.mapToLong(x -> x.longValue()).sum().isPresent());
    }

    @Test
    public void sumIsCalculated() {
        Enumerable<String> strings = Enumerable.of("foo", "bar", "bah");

        Optional<Long> sum = strings.mapToLong(x -> (long)x.length()).sum();

        assertThat(sum.get(), is(9l));
    }

    @Test
    public void emptyAverageIsCalculated() {
        Enumerable<Long> empty = Enumerable.empty();

        assertFalse(empty.mapToLong(x -> x).average().isPresent());
    }

    @Test
    public void averageIsCalculated() {
        LongEnumerable longs = Enumerable.of(1,2,3).mapToLong(x -> x.longValue());

        Optional<Double> average = longs.average();

        assertThat(average.get(), is(2.0));
    }

    @Test
    public void minIsReturned() {
        LongEnumerable longs = Enumerable.of(1,2,3).mapToLong(x -> x.longValue());

        assertThat(longs.min().get(), is(1l));
    }

    @Test
    public void maxIsReturned() {
        LongEnumerable longs = Enumerable.of(1,2,3).mapToLong(x -> x.longValue());

        assertThat(longs.max().get(), is(3l));
    }
}
