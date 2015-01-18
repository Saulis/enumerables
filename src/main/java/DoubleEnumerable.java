import java.util.Iterator;
import java.util.Optional;
import java.util.function.Supplier;

public class DoubleEnumerable extends NumberEnumerable<Double> {
    public DoubleEnumerable(Supplier<Iterator<Double>> iteratorSupplier) {
        super(iteratorSupplier);
    }

    public Optional<Double> average() {
        return average(new Accumulator<>(0.0, (acc, x) -> acc + x),
                       new Accumulator<>(0.0, (acc, x) -> acc + 1.0));
    }

    public Optional<Double> sum() {
        return sum(0.0, (acc, x) -> acc + x);
    }
}
