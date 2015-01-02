import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsIterableContainingInOrder.contains;

public class EnumerableTest {

    private final String[] stringArray = new String[] { "foo", "bar" };

    @Test
    public void emptyEnumerableIsConstructed() {
        Enumerable<Integer> empty = Enumerable.empty();

        assertThat(empty.count(), is(0));
    }

    @Test
    public void enumerableIsUnchangedAfterConstruction() {
        ArrayList<String> strings = new ArrayList<>();
        strings.add("foo");
        Enumerable<String> enumerable = Enumerable.enumerable(strings);

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
        Enumerable<String> enumerable = Enumerable.enumerable(stringArray);

        assertThat(enumerable.toList(), contains("foo", "bar"));
    }

    @Test
    public void enumerableIsConstructedFromArguments() {
        Enumerable<String> enumerable = Enumerable.enumerable("foo", "bar");

        assertThat(enumerable.toList(), contains("foo", "bar"));
    }

    @Test
    public void enumerableIsConstructedFromCollection() {
        Enumerable<String> enumerable =
                Enumerable.enumerable(Arrays.asList(stringArray));

        assertThat(enumerable.toList(), contains("foo", "bar"));
    }

    @Test
    public void itemsAreFiltered() {
        Enumerable<String> enumerable =
                Enumerable.enumerable("foo", "foobar", "bar");

        Enumerable<String> filtered = enumerable.filter(x -> x.length() == 3);

        assertThat(filtered, contains("foo", "bar"));
    }

    @Test
    public void itemsAreFlattened() {
        Enumerable<String> enumerable = Enumerable.enumerable("foo", "bar");

        String aggregate = enumerable.flatMap((acc, x) -> acc + x, "");

        assertThat(aggregate, is("foobar"));
    }

}
