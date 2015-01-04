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
    public void sumIsCalculated() {
        Enumerable<String> strings = Enumerable.of("foo", "bar", "bah");

        Optional<Integer> sum = strings.sum(x -> x.length());

        assertThat(sum.get(), is(9));
    }
}
