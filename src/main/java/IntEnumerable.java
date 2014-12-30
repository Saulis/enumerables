import java.util.Optional;
import java.util.function.BiFunction;

public class IntEnumerable extends NumberEnumerable {

    private static BiFunction<Integer, Integer, Integer>
            minFunction = (acc, x) -> Integer.min(acc, x);
    private static BiFunction<Integer, Integer, Integer>
            maxFunction = (acc, x) -> Integer.max(acc, x);

    public static Optional<Integer> min(Enumerable<Integer> enumerable) {
        return aggregate(enumerable, minFunction);
    }

    public static Optional<Integer> max(Enumerable<Integer> enumerable) {
        return aggregate(enumerable, maxFunction);
    }


}
