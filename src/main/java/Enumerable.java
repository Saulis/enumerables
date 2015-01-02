import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class Enumerable<T> implements Iterable<T> {
    private final Supplier<Iterator<T>> iteratorSupplier;

    public Enumerable(Supplier<Iterator<T>> iteratorSupplier) {
        this.iteratorSupplier = iteratorSupplier;
    }

    public static <T> Enumerable<T> empty() {
        return new Enumerable<>(() -> new EmptyIterator<>());
    }

    public static <T> Enumerable<T> enumerable(Iterable<T> items) {
        return new Enumerable<>(() -> items.iterator());
    }

    public static <T> Enumerable<T> enumerable(T... items) {
        return new Enumerable<>(() -> new ArrayIterator<>(items));
    }

    public static Enumerable<Integer> range(int from, int to) {
        if(from <= to) {
            return new Enumerable<>(() -> new RangeIterator<>(from - 1, x -> x + 1, to - from + 1));
        } else {
            return new Enumerable<>(() -> new RangeIterator<>(from + 1, x -> x - 1, from - to + 1));
        }
    }

    public <R> R flatMap(BiFunction<R, T, R> function, R seed) {
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


    public List<T> toList() {
        ArrayList<T> items = new ArrayList<>();

        forEach(x -> items.add(x));

        return items;
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
        Integer count = take(n + 1).flatMap((acc, x) -> acc + 1, 0);

        return count == n;
    }
}
