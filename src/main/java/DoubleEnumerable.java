import java.util.Optional;

public class DoubleEnumerable extends NumberEnumerable {

    public static Optional<Double> min(Enumerable<Double> enumerable) {
        return aggregate(enumerable, (acc, x) -> Double.min(acc, x));
    }

    public static Optional<Double> max(Enumerable<Double> enumerable) {
        return aggregate(enumerable, (acc, x) -> Double.max(acc, x));
    }
}
