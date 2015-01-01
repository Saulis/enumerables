import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class Enumerable<T> implements Iterable<T> {
    private  ArrayList<T> items;
    private final Supplier<Iterator<T>> iteratorSupplier;

    public Enumerable(Supplier<Iterator<T>> iteratorSupplier) {
        this.iteratorSupplier = iteratorSupplier;
        items = null;
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
        ArrayList<R> mappedItems = new ArrayList<>();

        forEach(x -> mappedItems.add(func.apply(x)));

        return Enumerable.enumerable(mappedItems);
    }

    public Enumerable<T> filter(Predicate<T> predicate) {
        ArrayList<T> filteredItems = new ArrayList<>();

        forEach(x -> {
            if(predicate.test(x)) {
                filteredItems.add(x);
            }
        });

        return Enumerable.enumerable(filteredItems);
    }

    public List<T> toList() {
        return getItems();
    }

    public Optional<T> first() {
        Iterator<T> iterator = iterator();

        if(iterator.hasNext()) {
            return Optional.of(iterator.next());
        }

        return Optional.empty();
    }

    public int count() {
        return getItems().size();
    }

    private ArrayList<T> getItems() {
        if(items == null) {
            items = iterateItems();
        }

        return items;
    }

    private ArrayList<T> iterateItems() {
        ArrayList<T> items = new ArrayList<>();

        forEach(x -> items.add(x));

        return items;
    }

    @Override
    public Iterator<T> iterator() {
        return iteratorSupplier.get();
    }
}
