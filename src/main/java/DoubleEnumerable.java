import java.util.Optional;
import java.util.function.BiFunction;

public class DoubleEnumerable extends NumberEnumerable {
    private static BiFunction<Double, Double, Double>
            minFunction = (acc, x) -> Double.min(acc, x);
    private static BiFunction<Double, Double, Double>
            maxFunction = (acc, x) -> Double.max(acc, x);

    public static Optional<Double> min(Enumerable<Double> enumerable) {
        return aggregate(enumerable, minFunction);
    }

    public static Optional<Double> max(Enumerable<Double> enumerable) {
        return aggregate(enumerable, maxFunction);
    }
}
