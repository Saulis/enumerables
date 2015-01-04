import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsIterableContainingInOrder.contains;
import static org.junit.Assert.assertTrue;

public class EnumerableTest {

    private final String[] stringArray = new String[] { "foo", "bar" };

    @Test
    public void emptyEnumerableIsConstructed() {
        Enumerable<Integer> empty = Enumerable.empty();

        assertTrue(empty.sizeIsExactly(0));
    }

    @Test
    public void enumerableIsUnchangedAfterConstruction() {
        ArrayList<String> strings = new ArrayList<>();
        strings.add("foo");
        Enumerable<String> enumerable = Enumerable.of(strings);

        strings.add("bar");

        assertThat(enumerable, not(hasItem("bar")));
    }

    //TODO: move to IntEnumerable
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
    public void emptyRangeIsConstructed() {
        Enumerable<Integer> range = Enumerable.range(1, 0);

        assertThat(range, contains(1, 0)); //NOT!
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
    public void itemsAreFlattened() {
        Enumerable<String> enumerable = Enumerable.of("foo", "bar");

        String aggregate = enumerable.reduce("", (acc, x) -> acc + x);

        assertThat(aggregate, is("foobar"));
    }

    @Test
    public void sizeIsExactly() {
        Enumerable<Integer> enumerable = Enumerable.of(1, 1, 2, 3, 5, 8);

        assertTrue(enumerable.sizeIsExactly(6));
    }

    @Test
    public void enumerableIsCollected() {
        Enumerable<Integer> enumerable = Enumerable.of(1, 1, 2, 3, 5, 8);

        Set<Integer> set = enumerable.collect(Collectors.toSet());

        assertThat(set, contains(1,2,3,5,8));
    }


}
