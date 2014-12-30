import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

public class Enumerable<T> implements Iterable<T> {
    protected final Iterable<T> items;

    public Enumerable() {
        items = new ArrayList<>();
    }

    public Enumerable(Iterable<T> items) {
        this.items = items;
    }

    public static <T> Enumerable<T> enumerable(Iterable<T> items) {
        return new Enumerable<>(items);
    }

    public static <T> Enumerable<T> enumerable(T... items) {
        return new Enumerable<>(Arrays.asList(items));
    }

    public static Enumerable<Integer> range(int from, int to) {
        if(from > to) {
            return new Enumerable();
        }

        ArrayList<Integer> range = new ArrayList<>();

        for(int i = from;i <= to; i++) {
            range.add(i);
        }

        return new Enumerable(range);

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

        return new Enumerable(mappedItems);
    }

    public Enumerable<T> filter(Predicate<T> predicate) {
        ArrayList<T> filteredItems = new ArrayList<>();

        forEach(x -> {
            if(predicate.test(x)) {
                filteredItems.add(x);
            }
        });

        return new Enumerable(filteredItems);
    }

    public List<T> toList() {
        ArrayList<T> list = new ArrayList<>();

        forEach(x -> list.add(x));

        return list;
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
        return items.iterator();
    }
}
