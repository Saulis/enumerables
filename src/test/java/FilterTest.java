import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsIterableContainingInOrder.contains;
import static org.junit.Assert.assertTrue;

public class FilterTest {

    @Test
    public void itemsAreFiltered() {
        Enumerable<String> enumerable =
                Enumerable.of("foo", "foobar", "bar");

        Enumerable<String> filtered = enumerable.filter(x -> x.length() == 3);

        assertThat(filtered, contains("foo", "bar"));
    }

    @Test
    public void noMatchesAreFound() {
        Enumerable<String> enumerable = Enumerable.of("foo", "bah");

        Enumerable<String> bar = enumerable.filter(x -> x.equals("bar"));

        assertTrue(bar.sizeIsExactly(0));
    }

    @Test
    public void emptyCollectionIsFiltered() {
        Enumerable<Object> empty = Enumerable.empty();

        Enumerable<Object> filtered = empty.filter(x -> true);

        assertTrue(filtered.sizeIsExactly(0));
    }

    @Test
    public void itemsAreFilteredByType() {
        assertThat(Enumerable.of("foo", 1).filterType(Integer.class), contains(1));
    }
}
