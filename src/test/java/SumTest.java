import org.junit.Test;

import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertFalse;

public class SumTest {

    @Test
    public void emptySumIsCalculated() {
        Enumerable<Integer> empty = Enumerable.empty();

        assertFalse(empty.sum(x -> x).isPresent());
    }

    @Test
    public void integerSumIsCalculated() {
        Enumerable<String> strings = Enumerable.of("foo", "bar", "bah");

        Optional<Double> sum = strings.sum(x -> x.length());

        assertThat(sum.get(), is(9.0));
    }

    @Test
    public void longSumIsCalculated() {
        Enumerable<String> strings = Enumerable.of("foo", "bar", "bah");

        Optional<Double> sum = strings.sum(x -> (long)x.length());

        assertThat(sum.get(), is(9.0));
    }

    @Test
    public void doubleSumIsCalculated() {
        Enumerable<String> strings = Enumerable.of("foo", "bar", "bah");

        Optional<Double> sum = strings.sum(x -> (double) x.length());

        assertThat(sum.get(), is(9.0));
    }

    @Test
    public void floatSumIsCalculated() {
        Enumerable<String> strings = Enumerable.of("foo", "bar", "bah");

        Optional<Double> sum = strings.sum(x -> (float) x.length());

        assertThat(sum.get(), is(9.0));
    }
}
