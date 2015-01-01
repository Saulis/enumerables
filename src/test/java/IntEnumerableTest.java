import org.junit.Test;

import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;

public class IntEnumerableTest {

    @Test
    public void emptyMinIsReturned() {
        Enumerable<Integer> integers = Enumerable.empty();

        Optional<Integer> min = IntEnumerable.min(integers);

        assertFalse(min.isPresent());
    }
    
    @Test
    public void positiveMinIsReturned() {
        Enumerable<Integer> enumerable = Enumerable.enumerable(1, 2, 3);

        assertThat(IntEnumerable.min(enumerable).get(), is(1));
    }

    @Test
    public void negativeMinIsReturned() {
        Enumerable<Integer> enumerable = Enumerable.enumerable(-1, -2, -3);

        assertThat(IntEnumerable.min(enumerable).get(), is(-3));
    }

    @Test
    public void emptyMaxIsReturned() {
        Enumerable<Integer> integers = Enumerable.empty();

        Optional<Integer> max = IntEnumerable.max(integers);

        assertFalse(max.isPresent());
    }

    @Test
    public void positiveMaxIsReturned() {
        Enumerable<Integer> enumerable = Enumerable.enumerable(1, 2, 3);

        assertThat(IntEnumerable.max(enumerable).get(), is(3));
    }

    @Test
    public void negativeMaxIsReturned() {
        Enumerable<Integer> enumerable = Enumerable.enumerable(-1, -2, -3);

        assertThat(IntEnumerable.max(enumerable).get(), is(-1));
    }

}