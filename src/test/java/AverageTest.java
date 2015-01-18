import org.junit.Test;

import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertFalse;

public class AverageTest {

    @Test
    public void emptyAverageIsCalculated() {
        Enumerable<Double> empty = Enumerable.empty();

        assertFalse(empty.average(x -> x).isPresent());
    }

    @Test
    public void doubleAverageIsCalculated() {
        Optional<Double> average = Enumerable.of(1,2,3)
                                     .average(x -> x.doubleValue());

        assertThat(average.get(), is(2.0));
    }

    @Test
    public void longAverageIsCalculated() {
        Optional<Double> average = Enumerable.of(1,2,3)
                                        .average(x -> x.longValue());

        assertThat(average.get(), is(2.0));
    }

    @Test
    public void integerAverageIsCalculated() {
        Optional<Double> average = Enumerable.range(1, 10)
                                          .average(x -> x.intValue());

        assertThat(average.get(), is(5.5));
    }
}
