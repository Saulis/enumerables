import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertTrue;

public class SplitTest {

    @Test
    public void emptyIsSplit() {
        Enumerable<Object>[] split = Enumerable.empty().split(x -> true);

        assertTrue(split[0].isEmpty());
        assertTrue(split[1].isEmpty());
    }

    @Test
    public void enumerableIsSplit() {
        Enumerable<Integer>[] evensAndOdds =
                Enumerable.range(1, 10)
                          .split(x -> x % 2 == 0, x -> x % 2 != 0);

        assertThat(evensAndOdds[0], contains(2,4,6,8,10));
        assertThat(evensAndOdds[0], contains(2,4,6,8,10));
        assertThat(evensAndOdds[0], contains(2,4,6,8,10));
        assertThat(evensAndOdds[1], contains(1,3,5,7,9));
    }

    @Test
    public void splittedEnumerablesAreReiterable() {
        Enumerable<Integer>[] split = Enumerable.range(1, 10).split(x -> x < 5);

        assertThat(split[1], contains(5,6,7,8,9,10));
        assertThat(split[0], contains(1,2,3,4));
        assertThat(split[0], contains(1,2,3,4));
        assertThat(split[1], contains(5,6,7,8,9,10));
        assertThat(split[0], contains(1,2,3,4));
    }

    @Test
    public void enumerableIsSplitByIndex() {
        Enumerable<Integer>[] split = Enumerable.range(1, 10).split((x, i) -> i < 5);

        assertThat(split[0], contains(1, 2, 3, 4, 5));
        assertThat(split[1], contains(6,7,8,9,10));
    }

    @Test
    public void splitIsLazy() {
        List<Integer> itemsFetched = new ArrayList<>();
        Enumerable<Integer> infiniteEnumerable =
                new Enumerable(() -> new FunctionIterator<>(1, x -> x + 1, x -> true));

        Enumerable<Integer>[] split = infiniteEnumerable
                .peek(x -> itemsFetched.add(x))
                .split(x -> x % 2 == 0, x -> x % 3 == 0);

        assertThat(split[0].skip(1).limit(5), contains(4,6,8,10,12));
        assertThat(split[1].limit(5), contains(3, 6, 9, 12, 15));
        assertThat(split[2].limit(5), contains(1,5,7,11,13));
        assertThat(itemsFetched.size(), is(15));
    }
}
