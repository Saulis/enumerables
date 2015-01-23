import java.lang.reflect.Array;
import java.util.*;
import java.util.function.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

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

    /*
     * Returns a new enumerable constructed from the specified iterable.
     * Enumerable will iterate the iterable immediately to create a copy.
     */
    public static <T> Enumerable<T> copyOf(Iterable<T> items) {
        return of(items).copy();
    }

    public boolean allMatch(Predicate<T> predicate) {
        return filter(predicate.negate()).isEmpty();
    }

    public boolean anyMatch(Predicate<T> predicate) {
        return !filter(predicate).isEmpty();
    }

    /*
        Reduces the enumerable into an average based on the provided mapping function.
        Will convert numbers into doubles to perform the calculations. Use multi reduce
        with custom Accumulators if you need more precision.

        As with all reduction functions, average will force iteration.
     */
    public <R extends Number> Optional<Double> average(Function<T, R> mappingFunction) {
        if(map(mappingFunction).isEmpty()) {
            return Optional.empty();
        }

        Accumulator<R, Double> sumAccumulator = new Accumulator<>(0.0, (acc, x) -> acc + x.doubleValue());
        Accumulator<R, Double> countAccumulator = new Accumulator<>(0.0, (acc, x) -> acc + 1.0);

        List<Double> sumAndCount = map(mappingFunction).reduce(sumAccumulator, countAccumulator);

        return Optional.of(sumAndCount.get(0) / sumAndCount.get(1));
    }

    public <R, A> R collect(Collector<T, A, R> collector) {
        Supplier<A> supplier = collector.supplier();
        BiConsumer<A, T> accumulator = collector.accumulator();
        Function<A, R> finisher = collector.finisher();

        A container = supplier.get();
        forEach(x -> accumulator.accept(container, x));

        return finisher.apply(container);
    }

    public Enumerable<T> concat(T... items) {
        return new Enumerable<>(() -> new ConcatIterator(this.iterator(), new ArrayIterator<>(items)));
    }

    public Enumerable<T> concat(Iterable<T> items) {
        return new Enumerable<>(() -> new ConcatIterator(this.iterator(), items.iterator()));
    }

    public boolean contains(T item) {
        return anyMatch(x -> x.equals(item));
    }

    /*
        Returns a copy of the enumerable. Will force iteration and is unaffected
        by changes to the parent.
     */
    public Enumerable<T> copy() {
        LinkedList<T> list = new LinkedList<>();
        forEach(x -> list.add(x));

        return new Enumerable<>(() -> list.iterator());
    }

    public int count() {
        return reduce(0, (acc, x) -> acc + 1);
    }

    public Enumerable<T> filter(Predicate<T> predicate) {
        return filter((x,i) -> predicate.test(x));
    }

    public Enumerable<T> filter(BiPredicate<T,Integer> predicate) {
        return new Enumerable<>(() -> new FilterIterator<>(this, predicate));
    }

    public Optional<T> findFirst() {
        Iterator<T> iterator = iterator();

        if(iterator.hasNext()) {
            return Optional.of(iterator.next());
        }

        return Optional.empty();
    }

    public Optional<T> findLast() {
        return reverse().findFirst();
    }

    public Optional<T> findSingle() {
        if(sizeIsGreaterThan(1)) {
            throw new NoSuchElementException(
                    "Collection contains more than one element.");
        }

        return findFirst();
    }

    public <R> Enumerable<R> flatMap(Function<T, R[]> function) {
        Iterator<ArrayIterator<R>> iterator =
                map(x -> new ArrayIterator<>(function.apply(x))).iterator();

        return new Enumerable<>(() -> new ConcatIterator<>(iterator));
    }

    public void forEach(BiConsumer<T, Integer> consumer) {
        int i = 0;

        for (T t : this) {
            consumer.accept(t, i++);
        }
    }

    public <K> Map<K, List<T>> groupBy(Function<T, K> function) {
        return collect(Collectors.groupingBy(function));
    }

    public boolean isEmpty() {
        return sizeIsExactly(0);
    }

    @Override
    public Iterator<T> iterator() {
        return iteratorSupplier.get();
    }

    public Enumerable<T> limit(long maxSize) {
        return new Enumerable<>(() -> new LimitIterator(this, maxSize));
    }

    public <R> Enumerable<R> map(Function<T, R> function) {
        return new Enumerable<>(() -> new MapIterator<>(this, function));
    }

    public <R extends Comparable<R>> Optional<T> max(Function<T, R> function) {
        return orderByDescending(function).findFirst();
    }

    public <R extends Comparable<R>> Optional<T> min(Function<T, R> function) {
        return orderBy(function).findFirst();
    }

    public boolean noneMatch(Predicate<T> predicate) {
        return filter(predicate).isEmpty();
    }

    public <R> Enumerable<R> cast(Class<R> c) {
        return map(x -> (R) x);
    }

    public <R> Enumerable<R> filterType(Class<R> c) {
        return filter(x -> x.getClass().equals(c)).cast(c);
    }

    public Enumerable<T> orderBy(Comparator<T> comparator) {
        return new Enumerable<>(() -> new OrderIterator<>(iterator(), comparator));
    }

    public <R extends Comparable<R>> Enumerable<T> orderBy(Function<T, R> function) {
        return orderBy(Comparator.comparing(function));
    }

    public <R extends Comparable<R>> Enumerable<T> orderByDescending(Function<T, R> function) {
        return orderByDescending(Comparator.comparing(function));
    }

    public Enumerable<T> orderByDescending(Comparator<T> comparator) {
        return orderBy(comparator.reversed());
    }

    public Enumerable<T> peek(Consumer<T> consumer) {
        return new Enumerable<>(() -> new PeekIterator<>(this, consumer));
    }

    public static Enumerable<Integer> range(int from, int to) {
        if(from <= to) {
            return new Enumerable(() -> new FunctionIterator<>(from - 1, x -> x + 1, to - from + 1));
        } else {
            return new Enumerable(() -> new FunctionIterator<>(from + 1, x -> x - 1, from - to + 1));
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

    public <R> List<R> reduce(Accumulator<T, R>... reductions) {
        List<R> res = new ArrayList<>();
        Iterator<T> iterator = iterator();

        while(iterator.hasNext()) {
            T next = iterator.next();

            for(int i=0;i<reductions.length;i++) {
                res.add(i, reductions[i].apply(next));
            }
        }

        return res.subList(0, reductions.length);
    }

    public static <T> Enumerable<T> repeat(Supplier<T> supplier, int iterations) {
        return new Enumerable<>(() -> new RepeatIterator<>(supplier, iterations));
    }

    public Enumerable<T> reverse() {
        LinkedList<T> list = new LinkedList<>();
        forEach(x -> list.add(0, x));

        return new Enumerable<>(() -> list.iterator());
    }

    /**
        Returns a new enumerable which will create a copy of the enumerable
        during first iteration.
     */
    public Enumerable<T> save() {
        LinkedList<T> list = new LinkedList<>();

        return new Enumerable<>(() -> new SaveIterator<>(iterator(), list));
    }

    public boolean sizeIsExactly(long n) {
        return limit(n + 1).count() == n;
    }

    public boolean sizeIsGreaterThan(long n) {
        return limit(n + 1).count() == n + 1;
    }

    public boolean sizeIsLessThan(long n) {
        return limit(n).count() < n;
    }

    public Enumerable<T> skip(long n) {
        return new Enumerable<>(() -> new SkipIterator<>(this, n));
    }

    /**
     * Splits the enumerable into multiple enumerables using the provided predicate(s).
     * @param predicates Predicates to match with.
     * @return An array of enumerables splitted by the provided predicate(s).
     *         Resulting array will always have the size of number predicates +
     *         one. Last element in the array will hold items that don't match
     *         any of the provided predicates.
     */
    public Enumerable<T>[] split(Predicate<T>... predicates) {
        Enumerable<BiPredicate<T, Integer>> biPredicates =
                Enumerable.of(predicates).map(x -> (y, i) -> x.test(y));

        return split(biPredicates.toArray());
    }

    /**
     * Splits the enumerable into multiple enumerables using the provided predicate(s).
     * @param predicates Predicates to match with.
     * @return An array of enumerables splitted by the provided predicate(s).
     *         Resulting array will always have the size of number predicates +
     *         one. Last element in the array will hold items that don't match
     *         any of the provided predicates.
     */
    public Enumerable<T>[] split(BiPredicate<T, Integer>... predicates) {
        IteratorSplitter<T> splitter = new IteratorSplitter<>(this, predicates);

        return Enumerable.of(predicates)
                .map(x -> new Enumerable<T>(() -> splitter.getIterator(x)))
                .concat(new Enumerable<T>(() -> splitter.getRemainderIterator()))
                .toArray();
    }

    /*
        Reduces the enumerable into a sum based on the provided mapping function.
        Will convert numbers into doubles to perform the calculations. Use reduce
        with a custom Accumulator if you need more precision.

        As with all reduction functions, sum will force iteration.
     */
    public <R extends Number> Optional<Double> sum(Function<T, R> mappingFunction) {
        if(isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(map(mappingFunction)
                .reduce(0.0, (seed, x) -> seed + x.doubleValue()));
    }

    /*
        Collects the enumerable into an array with a size provided in the init function.
     */
    public T[] toArray(Function<Integer, T[]> initFunction) {
        Enumerable<T> saved = save();
        T[] array = initFunction.apply(saved.count());

        saved.limit(array.length).forEach((x, i) -> array[i] = x);

        return array;
    }

    /*
        Collects the whole enumerable into an array.
     */
    public T[] toArray() {
        Optional<T> first = findFirst();

        if(!first.isPresent()) {
            return (T[])new Object[0];
        }

        Class<?> aClass = first.get().getClass();

        return toArray(size -> (T[]) Array.newInstance(aClass, size));
    }

    public List<T> toList() {
        return collect(Collectors.toList());
    }

}
