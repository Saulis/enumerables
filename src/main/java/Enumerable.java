import javax.swing.text.html.Option;
import java.util.*;
import java.util.function.*;
import java.util.stream.Collector;

public class Enumerable<T> implements Iterable<T> {
    private final Supplier<Iterator<T>> iteratorSupplier;

    public Enumerable(Supplier<Iterator<T>> iteratorSupplier) {
        this.iteratorSupplier = iteratorSupplier;
    }

    public static <T> Enumerable<T> empty() {
        return new Enumerable<>(() -> new EmptyIterator<>());
    }

    public static <T> Enumerable<T> of(Iterable<T> items) {
        return new Enumerable<>(() -> items.iterator());
    }

    public static <T> Enumerable<T> of(T... items) {
        return new Enumerable<>(() -> new ArrayIterator<>(items));
    }

    public static Enumerable<Integer> range(int from, int to) {
        if(from <= to) {
            return new Enumerable<>(() -> new RangeIterator<>(from - 1, x -> x + 1, to - from + 1));
        } else {
            return new Enumerable<>(() -> new RangeIterator<>(from + 1, x -> x - 1, from - to + 1));
        }
    }

    public <R> R reduce(R seed, BiFunction<R, T, R> function) {
        R result = seed;

        Iterator<T> iterator = iterator();

        while(iterator.hasNext()) {
            T next = iterator.next();
            result = function.apply(result, next);
        }

        return result;
    }

    public <R> Enumerable<R> map(Function<T, R> func) {
        return new Enumerable<>(() -> new MapIterator<>(this, func));
    }

    public Enumerable<T> take(long n) {
        return new Enumerable<>(() -> new TakeIterator(this, n));
    }

    public Enumerable<T> filter(Predicate<T> predicate) {
        return new Enumerable<>(() -> new FilterIterator<>(this, predicate));
    }

    public Enumerable<T> join(T... items) {
        return new Enumerable<>(() -> new JoinIterator(this.iterator(), new ArrayIterator<>(items)));
    }

    public Enumerable<T> join(Iterable<T> items) {
        return new Enumerable<>(() -> new JoinIterator(this.iterator(), items.iterator()));
    }

    public Optional<T> first() {
        Iterator<T> iterator = iterator();

        if(iterator.hasNext()) {
            return Optional.of(iterator.next());
        }

        return Optional.empty();
    }

    @Override
    public Iterator<T> iterator() {
        return iteratorSupplier.get();
    }

    public boolean sizeIsExactly(long n) {
        Integer count = take(n + 1).reduce(0, (acc, x) -> acc + 1);

        return count == n;
    }

    public boolean isEmpty() {
        return sizeIsExactly(0);
    }

    public <R, A> R collect(Collector<T, A, R> collector) {
        Supplier<A> supplier = collector.supplier();
        BiConsumer<A, T> accumulator = collector.accumulator();
        Function<A, R> finisher = collector.finisher();

        A container = supplier.get();
        forEach(x -> accumulator.accept(container, x));

        return finisher.apply(container);
    }

    public Enumerable<T> sort(Comparator<T> comparator) {
        return new Enumerable<>(() -> new SortIterator<>(iterator(), comparator));
    }

    public <R extends Comparable<R>> Enumerable<T> sort(Function<T, R> function) {
        return sort(Comparator.comparing(function));
    }

    public Enumerable<T> sortReversed(Comparator<T> comparator) {
        return sort(comparator.reversed());
    }

    public <R extends Comparable<R>> Enumerable<T> sortReversed(Function<T, R> function) {
        return sortReversed(Comparator.comparing(function));
    }

    public <R extends Comparable<R>> Optional<T> min(Function<T, R> function) {
        return sort(function).first();
    }

    public <R extends Comparable<R>> Optional<T> max(Function<T, R> function) {
        return sortReversed(function).first();
    }

    public Optional<Integer> sum(Function<T, Integer> function) {
        if(isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(reduce(0, (acc, x) -> acc + function.apply(x)));
    }

    public Optional<Double> average(Function<T, Integer> function) {
        if(isEmpty()) {
            return Optional.empty();
        }

        AbstractMap.SimpleEntry<Double, Double> sumAndCount =
            reduce(new AbstractMap.SimpleEntry<>(0.0, 0.0), (acc, x) ->
                new AbstractMap.SimpleEntry<>(acc.getKey() + function.apply(x),
                                              acc.getValue() + 1.0));

        return Optional.of(sumAndCount.getKey() / sumAndCount.getValue());
    }
}
