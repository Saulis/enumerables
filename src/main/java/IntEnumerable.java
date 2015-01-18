import java.util.Iterator;
import java.util.Optional;
import java.util.function.Supplier;

public class IntEnumerable extends NumberEnumerable<Integer> {
    public IntEnumerable(Supplier<Iterator<Integer>> iteratorSupplier) {
        super(iteratorSupplier);
    }

    public Optional<Double> average() {
        return average(new Accumulator<>(0.0, (acc, x) -> acc + x),
                       new Accumulator<>(0.0, (acc, x) -> acc + 1.0));
    }

    public Optional<Integer> sum() {
        return sum(0, (acc, x) -> acc + x);
    }
}
