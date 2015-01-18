import java.util.Iterator;
import java.util.Optional;
import java.util.function.Supplier;

public class LongEnumerable extends NumberEnumerable<Long> {
    public LongEnumerable(Supplier<Iterator<Long>> iteratorSupplier) {
        super(iteratorSupplier);
    }

    public Optional<Double> average() {
        return average(new Accumulator<>(0.0, (acc, x) -> acc + x),
                       new Accumulator<>(0.0, (acc, x) -> acc + 1.0));
    }

    public Optional<Long> sum() {
        return sum(0l, (acc, x) -> acc + x);
    }
}
