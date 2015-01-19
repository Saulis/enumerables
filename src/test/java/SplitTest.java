import org.junit.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertTrue;

public class SplitTest {

    @Test
    public void emptyIsSplit() {
        List<Enumerable<Object>> split = Enumerable.empty().split(x -> true);

        assertTrue(split.get(0).isEmpty());
        assertTrue(split.get(1).isEmpty());
    }

    @Test
    public void enumerableIsSplit() {
        List<Enumerable<Integer>> evensAndOdds =
                Enumerable.range(1, 10)
                          .split(x -> x % 2 == 0, x -> x % 2 != 0);

        assertThat(evensAndOdds.get(0), contains(2,4,6,8,10));
        assertThat(evensAndOdds.get(1), contains(1,3,5,7,9));
    }
}
