import java.util.Optional;
import java.util.function.BiFunction;

public class IntEnumerable {

    private static BiFunction<Integer, Integer, Integer>
            minFunction = (acc, x) -> Integer.min(acc, x);
    private static BiFunction<Integer, Integer, Integer>
            maxFunction = (acc, x) -> Integer.max(acc, x);

    private static Optional<Integer> flatMapIntegers(
            Enumerable<Integer> enumerable,
            BiFunction<Integer, Integer, Integer> aggregateFunction) {
        Optional<Integer> first = enumerable.first();

        if(!first.isPresent()) {
            return Optional.empty();
        }

        return Optional.of(enumerable.flatMap(aggregateFunction, first.get()));
    }

    public static Optional<Integer> min(Enumerable<Integer> enumerable) {
        return flatMapIntegers(enumerable, minFunction);
    }

    public static Optional<Integer> max(Enumerable<Integer> enumerable) {
        return flatMapIntegers(enumerable, maxFunction);
    }


}
