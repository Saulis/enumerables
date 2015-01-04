import java.util.Optional;
import java.util.function.BiFunction;

class NumberEnumerable {
    protected static <T extends Number> Optional<T> aggregate(
            Enumerable<T> enumerable,
            BiFunction<T, T, T> aggregateFunction) {
        Optional<T> first = enumerable.first();

        if(!first.isPresent()) {
            return Optional.empty();
        }

        return Optional.of(enumerable.reduce(first.get(), aggregateFunction));
    }

}
