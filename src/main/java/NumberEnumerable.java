import java.util.Iterator;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Supplier;

public abstract class NumberEnumerable<T extends Comparable<T>> extends Enumerable<T> {
    protected NumberEnumerable(Supplier<Iterator<T>> iteratorSupplier) {
        super(iteratorSupplier);
    }

    /*
        Reduces the enumerable into an average.
     */
    public abstract Optional<Double> average();

    protected Optional<Double> average(Accumulator<T, Double> sumAccumulator,
                                       Accumulator<T, Double> countAccumulator) {
        if(isEmpty()) {
            return Optional.empty();
        }

        Double[] sumAndCount =
                reduce(sumAccumulator,
                        countAccumulator)
                        .toArray(new Double[2]);

        return Optional.of(sumAndCount[0] / sumAndCount[1]);
    }

    /*
        Reduces the enumerable into a sum of integers.
    */
    public abstract Optional<T> sum();

    protected Optional<T> sum(T seed, BiFunction<T, T, T> additionFunction) {
        if(isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(reduce(seed, additionFunction));
    }

    public Optional<T> max() {
        return max(x -> x);
    }

    public Optional<T> min() {
        return min(x -> x);
    }
}
