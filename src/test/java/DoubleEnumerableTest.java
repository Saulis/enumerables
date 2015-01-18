import org.junit.Test;

import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertFalse;

public class DoubleEnumerableTest {

    @Test
    public void emptySumIsCalculated() {
        Enumerable<Double> empty = Enumerable.empty();

        assertFalse(empty.mapToDouble(x -> x).sum().isPresent());
    }

    @Test
    public void sumIsCalculated() {
        Enumerable<String> strings = Enumerable.of("foo", "bar", "bah");

        Optional<Double> sum = strings.mapToDouble(x -> (double)x.length()).sum();

        assertThat(sum.get(), is(9.0));
    }

    @Test
    public void emptyAverageIsCalculated() {
        Enumerable<Double> empty = Enumerable.empty();

        assertFalse(empty.mapToDouble(x -> x).average().isPresent());
    }

    @Test
    public void averageIsCalculated() {
        DoubleEnumerable doubles = Enumerable.of(1,2,3)
                                             .mapToDouble(x -> x.doubleValue());

        Optional<Double> average = doubles.average();

        assertThat(average.get(), is(2.0));
    }

    @Test
    public void minIsReturned() {
        DoubleEnumerable doubles = Enumerable.of(1,2,3)
                                             .mapToDouble(x -> x.doubleValue());

        assertThat(doubles.min().get(), is(1.0));
    }

    @Test
    public void maxIsReturned() {
        DoubleEnumerable doubles = Enumerable.of(1,2,3)
                                             .mapToDouble(x -> x.doubleValue());

        assertThat(doubles.max().get(), is(3.0));
    }
}
