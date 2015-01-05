import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertTrue;

public class SkipTest {

    @Test
    public void allItemsAreSkipped() {
        assertTrue(Enumerable.of(1, 2, 3).skip(3).isEmpty());
    }

    @Test
    public void itemsAreSkipped() {
        assertThat(Enumerable.of(1,2,3).skip(2), contains(3));
    }

    @Test
    public void moreThanLengthItemsAreSkipped() {
        assertTrue(Enumerable.empty().skip(1).isEmpty());
    }
}
