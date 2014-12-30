import org.junit.Test;

import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;

public class DoubleEnumerableTest {

    @Test
    public void emptyMinIsReturned() {
        Enumerable<Double> doubles = new Enumerable<>();

        Optional<Double> min = DoubleEnumerable.min(doubles);

        assertFalse(min.isPresent());
    }
    
    @Test
    public void positiveMinIsReturned() {
        Enumerable<Double> enumerable = Enumerable.enumerable(1.0, 2.0, 3.0);

        assertThat(DoubleEnumerable.min(enumerable).get(), is(1.0));
    }

    @Test
    public void negativeMinIsReturned() {
        Enumerable<Double> enumerable = Enumerable.enumerable(-1.0, -2.0, -3.0);

        assertThat(DoubleEnumerable.min(enumerable).get(), is(-3.0));
    }

    @Test
    public void emptyMaxIsReturned() {
        Enumerable<Double> doubles = new Enumerable<>();

        Optional<Double> max = DoubleEnumerable.max(doubles);

        assertFalse(max.isPresent());
    }

    @Test
    public void positiveMaxIsReturned() {
        Enumerable<Double> enumerable = Enumerable.enumerable(1.0, 2.0, 3.0);

        assertThat(DoubleEnumerable.max(enumerable).get(), is(3.0));
    }

    @Test
    public void negativeMaxIsReturned() {
        Enumerable<Double> enumerable = Enumerable.enumerable(-1.0, -2.0, -3.0);

        assertThat(DoubleEnumerable.max(enumerable).get(), is(-1.0));
    }

}