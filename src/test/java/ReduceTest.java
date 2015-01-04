import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class ReduceTest {
    @Test
    public void stringsAreReduced() {
        Enumerable<String> enumerable = Enumerable.of("foo", "bar");

        String result = enumerable.reduce("", (acc, x) -> acc + x);

        assertThat(result, is("foobar"));
    }
}
