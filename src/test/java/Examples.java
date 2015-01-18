import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Collectors;

public class Examples {

    @Test
    public void constructing() {

        // From arbitrary number of elements
        Enumerable<Integer> ints = Enumerable.of(1, 2, 3);

        // From array
        Enumerable<String> strings = Enumerable.of(new String[]{"a", "b", "c"});

        // From any Iterable
        Enumerable<Double> doubles = Enumerable.of(new ArrayList<>());

        Enumerable<Integer> empty = Enumerable.empty();
        // -> []

        Enumerable<Integer> range = Enumerable.range(1, 5);
        // -> [1,2,3,4,5]

        Enumerable<Integer> repeat = Enumerable.repeat(() -> 1, 5);
        // -> [1,1,1,1,1]
    }

    @Test
    public void mapping() {

        HashMap<String, Integer[]> hashMap = new HashMap<>();
        hashMap.put("foo", new Integer[]{1, 2});
        hashMap.put("bar", new Integer[]{3, 4, 5});

        Enumerable<String> keys = Enumerable.of(hashMap.keySet());

        keys.map(x -> x + hashMap.get(x).length);
        // -> ["foo2", "bar3"]

        keys.flatMap(x -> hashMap.get(x));
        // -> [1,2,3,4,5]
    }

    @Test
    public void filtering() {

        Enumerable<Integer> range = Enumerable.range(1, 10);

        range.filter(x -> x % 2 == 0);
        // -> [2,4,6,8,10]

        Enumerable.of(1, "foo", 2.0f, "bar").filterType(String.class);
        // -> ["foo", "bar"]

        range.allMatch(x -> x > 0);
        // true

        range.noneMatch(x -> x > 10);
        // true

        range.anyMatch(x -> x == 5);
        // true

        range.contains(11);
        // false
    }

    @Test
    public void sorting() {
        Enumerable.of("a", "foo3", "bar").orderBy(x -> x.length());
        // -> ["a", "bar", "foo3"]

        Enumerable.of("a", "foo3", "bar").orderByDescending(x -> x.length());
        // -> ["foo3", "bar", "a"]

        Enumerable.range(1, 10).reverse();
        // -> [10,9,8,7,6,5,4,3,2,1]
    }

    @Test
    public void reducing() {
        Enumerable.of("foo", "bar").reduce("", (seed, x) -> seed + x);
        // -> "foobar"

        // You can also use the Accumulator overload
        Enumerable.of("foo", "bar").reduce(
                new Accumulator<>("", (seed, x) -> seed + x));
        // -> "foobar"

        // For performance, multi reduce can be used to run multiple
        // accumulators during a single iteration.
        Enumerable.of("foo", "bar").reduce(
                new Accumulator<String, Object>("", (seed, x) -> seed + x),
                new Accumulator<String, Object>(0, (seed, x) -> (int)seed + x.length()));
        // -> ["foobar", 6]

        Enumerable.range(1, 3).sum(x -> x);
        // -> 6.0

        Enumerable.range(1, 3).average(x -> x);
        // -> 3.0

        Enumerable.of(1, 2, 3).min(x -> x);
        // -> 1

        Enumerable.of(1, 2, 3).max(x -> x);
        // -> 3
    }

    @Test
    public void collecting() {
        Enumerable<Integer> range = Enumerable.range(1, 10);

        range.limit(5);
        // -> [1,2,3,4,5]

        range.skip(5);
        // -> [6,7,8,9,10]

        range.findFirst();
        // -> 1

        range.findLast();
        // -> 10

        // Enumerable.range(1, 10).findSingle();
        // -> NoSuchElementException

        Enumerable.of(1).findSingle();
        // -> 1

        // Stream Collectors are also supported
        Enumerable.of(1,2,3).collect(Collectors.toSet());

        // Shortcut for collecting a list
        Enumerable.of(1,2,3).toList();

        // Shortcut for creating an array
        Enumerable.of(1,2,3).toArray(size -> new Integer[size]);
    }

    @Test
    public void miscFunctions() {
        // concatenates with an Iterable<T> or an array
        Enumerable.of(1).concat(2,3);
        // -> [1,2,3]

        // sizeIs* functions can be used to determine sizes without iterating
        // the collection through (like count() does)
        Enumerable.of(1,2,3).sizeIsExactly(3); //true
        Enumerable.of(1,2,3).sizeIsLessThan(1); // false
        Enumerable.of(1,2,3).sizeIsGreaterThan(1); //true

        // copy() can be used to store the collection on first iteration so that
        // it's parent won't be iterated again.
        Enumerable<Integer> ints = Enumerable.of(1,2,3).copy();
    }
}
