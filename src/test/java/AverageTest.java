import org.junit.Test;

import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertFalse;

public class AverageTest {

    @Test
    public void emptyAverageIsCalculated() {
        Enumerable<Integer> empty = Enumerable.empty();

        assertFalse(empty.average(x -> x).isPresent());
    }

    @Test
    public void averageIsCalculated() {
        Enumerable<Integer> range = Enumerable.range(1, 10);

        Optional<Double> average = range.average(x -> x);

        assertThat(average.get(), is(5.5));
    }
}
