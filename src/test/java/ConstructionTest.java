import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsIterableContainingInOrder.contains;
import static org.junit.Assert.assertTrue;

public class ConstructionTest {
    private final String[] stringArray = new String[] { "foo", "bar" };

    @Test
    public void emptyEnumerableIsConstructed() {
        Enumerable<Integer> empty = Enumerable.empty();

        assertTrue(empty.sizeIsExactly(0));
    }

    //This is a known side-effect because the original collection is not copied.
    @Test
    public void enumerableChangesAfterConstruction() {
        ArrayList<String> list = new ArrayList<>();
        list.add("foo");

        Enumerable<String> strings = Enumerable.of(list);
        assertTrue(strings.sizeIsExactly(1));

        list.add("bar");
        assertTrue(strings.sizeIsExactly(2));
    }

    @Test
    public void enumerableDoesNotChangeAfterConstruction() {
        ArrayList<String> list = new ArrayList<>();
        list.add("foo");

        Enumerable<String> strings = Enumerable.copyOf(list);
        assertTrue(strings.sizeIsExactly(1));

        list.add("bar");
        assertTrue(strings.sizeIsExactly(1));
    }

    @Test
    public void ascendingRangeIsConstructed() {
        Enumerable<Integer> range = Enumerable.range(1, 5);

        assertThat(range, contains(1,2,3,4,5));
    }

    @Test
    public void descendingRangeIsConstructed() {
        Enumerable<Integer> range = Enumerable.range(1, -5);

        assertThat(range, contains(1,0,-1,-2,-3,-4,-5));
    }

    @Test
    public void invertedRangeIsConstructed() {
        Enumerable<Integer> range = Enumerable.range(2, -2);

        assertThat(range, contains(2,1,0,-1,-2));
    }

    @Test
    public void rangeWithSingleValueConstructed() {
        Enumerable<Integer> range = Enumerable.range(1, 1);

        assertThat(range, contains(1));
    }

    @Test
    public void enumerableIsConstructedFromArray() {
        Enumerable<String> enumerable = Enumerable.of(stringArray);

        assertThat(enumerable, contains("foo", "bar"));
    }

    @Test
    public void enumerableIsConstructedFromArguments() {
        Enumerable<String> enumerable = Enumerable.of("foo", "bar");

        assertThat(enumerable, contains("foo", "bar"));
    }

    @Test
    public void enumerableIsConstructedFromCollection() {
        Enumerable<String> enumerable =
                Enumerable.of(Arrays.asList(stringArray));

        assertThat(enumerable, contains("foo", "bar"));
    }

    @Test
    public void repeatingEnumerableIsConstructed() {
        Enumerable<Integer> ints = Enumerable.repeat(() -> 1, 5);

        assertThat(ints, contains(1,1,1,1,1));
    }
}
