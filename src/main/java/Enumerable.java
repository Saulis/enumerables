import java.lang.reflect.Array;
import java.util.*;
import java.util.function.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class Enumerable<T> implements Iterable<T> {
    private final Supplier<Iterator<T>> iteratorSupplier;

    /**
     * Constructs a new enumerable object.
     * @param iteratorSupplier Supplier function that will return new iterators.
     */
    public Enumerable(Supplier<Iterator<T>> iteratorSupplier) {
        this.iteratorSupplier = iteratorSupplier;
    }

    /**
     * Constructs an empty enumerable.
     */
    public static <T> Enumerable<T> empty() {
        return new Enumerable<>(() -> new EmptyIterator<>());
    }

    /**
     * Constructs a new enumerable from an iterable object. If the provided
     * iterable is mutable, possible changes also affect the created enumerable.
     * Use copy() and copyOf() to avoid this.
     */
    public static <T> Enumerable<T> of(Iterable<T> items) {
        return new Enumerable<>(() -> items.iterator());
    }

    /**
     * Constructs a new enumerable from an arbitrary number of items.
     */
    public static <T> Enumerable<T> of(T... items) {
        return new Enumerable<>(() -> new ArrayIterator<>(items));
    }

    /**
     * Constructs a new enumerable from an iterable object.
     * The enumerable will iterate the iterable immediately to create a copy.
     */
    public static <T> Enumerable<T> copyOf(Iterable<T> items) {
        return of(items).copy();
    }

    /**
     * Checks if all items match the provided predicate.
     */
    public boolean allMatch(Predicate<T> predicate) {
        return filter(predicate.negate()).isEmpty();
    }

    /**
     * Checks if any item matches the provided predicate.
     */
    public boolean anyMatch(Predicate<T> predicate) {
        return !filter(predicate).isEmpty();
    }

    /**
     * Reduces the enumerable into an average based on the provided mapping function.
     * Converts numbers into doubles to perform the calculations. Use multi reduce
     * with custom Accumulators if you need more precision.
     *
     * As with all reduction functions, average will force iteration.
     */
    public <R extends Number> Optional<Double> average(Function<T, R> mappingFunction) {
        if(isEmpty()) {
            return Optional.empty();
        }

        Accumulator<R, Double> sumAccumulator = new Accumulator<>(0.0, (acc, x) -> acc + x.doubleValue());
        Accumulator<R, Double> countAccumulator = new Accumulator<>(0.0, (acc, x) -> acc + 1.0);

        List<Double> sumAndCount = map(mappingFunction).reduce(sumAccumulator, countAccumulator);

        return Optional.of(sumAndCount.get(0) / sumAndCount.get(1));
    }

    /**
     * Collects the enumerable. Stream collector objects can be used for collecting.
     */
    public <R, A> R collect(Collector<T, A, R> collector) {
        Supplier<A> supplier = collector.supplier();
        BiConsumer<A, T> accumulator = collector.accumulator();
        Function<A, R> finisher = collector.finisher();

        A container = supplier.get();
        forEach(x -> accumulator.accept(container, x));

        return finisher.apply(container);
    }

    /**
     * Concatenates the items with the provided iterable and returns the
     * resulting items as a new enumerable.
     */
    public Enumerable<T> concat(T... items) {
        return new Enumerable<>(() -> new ConcatIterator(this.iterator(), new ArrayIterator<>(items)));
    }

    /**
     * Concatenates the items with the provided iterable and returns the
     * resulting items as a new enumerable.
     */
    public Enumerable<T> concat(Iterable<T> items) {
        return new Enumerable<>(() -> new ConcatIterator(this.iterator(), items.iterator()));
    }

    /**
     * Checks if the enumerable contains the provided item.
     */
    public boolean contains(T item) {
        return anyMatch(x -> x.equals(item));
    }

    /**
     * Returns a copy of the enumerable. The copy is unaffected by changes to the parent.
     *
     * Forces iteration.
     */
    public Enumerable<T> copy() {
        LinkedList<T> list = new LinkedList<>();
        forEach(x -> list.add(x));

        return new Enumerable<>(() -> list.iterator());
    }

    /**
     * Returns the size of the enumerable.
     *
     * Forces iteration, like any other reduce function.
     */
    public int count() {
        return reduce(0, (acc, x) -> acc + 1);
    }

    /**
     * Returns a new enumerable containing only items that match the provided predicate.
     */
    public Enumerable<T> filter(Predicate<T> predicate) {
        return filter((x, i) -> predicate.test(x));
    }

    /**
     * Returns a new enumerable containing only items that match the provided predicate.
     */
    public Enumerable<T> filter(BiPredicate<T,Integer> predicate) {
        return new Enumerable<>(() -> new FilterIterator<>(this, predicate));
    }

    /**
     * Tries to return the first item of the enumerable.
     */
    public Optional<T> findFirst() {
        Iterator<T> iterator = iterator();

        if(iterator.hasNext()) {
            return Optional.of(iterator.next());
        }

        return Optional.empty();
    }

    /**
     * Tries to return the last item of the enumerable.
     *
     * Uses ordering, which forces iteration.
     */
    public Optional<T> findLast() {
        return reverse().findFirst();
    }

    /**
     * Tries to return the first and only item of the enumerable.
     * If more than one items exists, NoSuchElementException is thrown.
     */
    public Optional<T> findSingle() {
        if(sizeIsGreaterThan(1)) {
            throw new NoSuchElementException(
                    "Collection contains more than one element.");
        }

        return findFirst();
    }

    /**
     * Flattens the items with the provided mapping function and returns the
     * resulting items as a new enumerable.
     */
    public <R> Enumerable<R> flatMap(Function<T, R[]> mappingFunction) {
        Iterator<ArrayIterator<R>> iterator =
                map(x -> new ArrayIterator<>(mappingFunction.apply(x))).iterator();

        return new Enumerable<>(() -> new ConcatIterator<>(iterator));
    }

    /**
     * Iterates through all the items an performs the provided actions.
     */
    public void forEach(BiConsumer<T, Integer> consumer) {
        int i = 0;

        for (T t : this) {
            consumer.accept(t, i++);
        }
    }

    /**
     * Collects the enumerable into a grouping according to the provided classifier.
     */
    public <K> Map<K, List<T>> groupBy(Function<T, K> classifier) {
        return collect(Collectors.groupingBy(classifier));
    }

    /**
     * Checks if the enumerable is empty.
     */
    public boolean isEmpty() {
        return sizeIsExactly(0);
    }

    @Override
    public Iterator<T> iterator() {
        return iteratorSupplier.get();
    }

    /**
     * Limits the size of the enumerable to the provided length and returns the
     * resulting items as a new enumerable.
     */
    public Enumerable<T> limit(long maxSize) {
        return new Enumerable<>(() -> new LimitIterator(this, maxSize));
    }

    /**
     * Maps the items using the provided mapping function and returns the
     * resulting items as a new enumerable.
     */
    public <R> Enumerable<R> map(Function<T, R> mappingFunction) {
        return new Enumerable<>(() -> new MapIterator<>(this, mappingFunction));
    }

    /**
     * Finds the maximum of the items using the provided function to map the items
     * into comparable items first.
     *
     * Uses ordering and therefore forces iteration.
     */
    public <R extends Comparable<R>> Optional<T> max(Function<T, R> mappingFunction) {
        return orderByDescending(mappingFunction).findFirst();
    }

    /**
     * Finds the minimum of the items using the provided function to map the items
     * into comparable items first.
     *
     * Uses ordering and therefore forces iteration.
     */
    public <R extends Comparable<R>> Optional<T> min(Function<T, R> mappingFunction) {
        return orderBy(mappingFunction).findFirst();
    }

    /**
     * Checks if none of the items match the provided predicate.
     */
    public boolean noneMatch(Predicate<T> predicate) {
        return filter(predicate).isEmpty();
    }

    /**
     * Casts the items into provided type and returns the resulting items
     * as a new enumerable.
     */
    public <R> Enumerable<R> cast(Class<R> c) {
        return map(x -> (R) x);
    }

    /**
     * Filters the items by type and returns the resulting items as a new
     * enumerable.
     */
    public <R> Enumerable<R> filterType(Class<R> c) {
        return filter(x -> x.getClass().equals(c)).cast(c);
    }

    /**
     * Orders the items using the provided comparator and returns the resulting
     * items as a new enumerable.
     *
     * Forces iteration when first item is fetched.
     */
    public Enumerable<T> orderBy(Comparator<T> comparator) {
        return new Enumerable<>(() -> new OrderIterator<>(iterator(), comparator));
    }

    /**
     * Orders the items using the provided function to map items into comparable
     * items and returns the resulting items as a new enumerable.
     *
     * Forces iteration when first item is fetched.
     */
    public <R extends Comparable<R>> Enumerable<T> orderBy(Function<T, R> function) {
        return orderBy(Comparator.comparing(function));
    }

    /**
     * Orders the items in descending order and returns the resulting items as
     * a new enumerable.
     */
    public <R extends Comparable<R>> Enumerable<T> orderByDescending(Function<T, R> function) {
        return orderByDescending(Comparator.comparing(function));
    }

    /**
     * Orders the items in descending order and returns the resulting items as
     * a new enumerable.
     */
    public Enumerable<T> orderByDescending(Comparator<T> comparator) {
        return orderBy(comparator.reversed());
    }

    /**
     * Performs the provided action for each item and returns the original items
     * as a new enumerable.
     */
    public Enumerable<T> peek(Consumer<T> consumer) {
        return new Enumerable<>(() -> new PeekIterator<>(this, consumer));
    }

    /**
     * Constructs a enumerable consisting of a range of integers. Provided start
     * and end arguments are inclusive.
     */
    public static Enumerable<Integer> range(int from, int to) {
        if(from <= to) {
            return new Enumerable(() -> new FunctionIterator<>(from - 1, x -> x + 1, to - from + 1));
        } else {
            return new Enumerable(() -> new FunctionIterator<>(from + 1, x -> x - 1, from - to + 1));
        }
    }

    /**
     * Reduces the enumerable into a single value using the provided accumulator
     * function.
     *
     * Forces iteration.
     */
    public <R> R reduce(R seed, BiFunction<R, T, R> function) {
        R result = seed;

        Iterator<T> iterator = iterator();

        while(iterator.hasNext()) {
            T next = iterator.next();
            result = function.apply(result, next);
        }

        return result;
    }

    /**
     * Reduces the enumerable into a multiple values using the provided accumulator
     * functions.
     *
     * Forces iteration, but iterates only once.
     */
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

    /**
     * Constructs a new enumerable consisting of repeating values.
     */
    public static <T> Enumerable<T> repeat(Supplier<T> supplier, int iterations) {
        return new Enumerable<>(() -> new RepeatIterator<>(supplier, iterations));
    }

    /**
     * Reverses the items in the enumerable and returns the resulting items
     * as a new enumerable.
     *
     * Forces iteration.
     */
    public Enumerable<T> reverse() {
        LinkedList<T> list = new LinkedList<>();
        forEach(x -> list.add(0, x));

        return new Enumerable<>(() -> list.iterator());
    }

    /**
     * Returns a new enumerable which will create a copy of the enumerable
     * during its first iteration.
     */
    public Enumerable<T> save() {
        LinkedList<T> list = new LinkedList<>();

        return new Enumerable<>(() -> new SaveIterator<>(iterator(), list));
    }

    /**
     * Checks if the number of items in the enumerable is exactly the provided value.
     */
    public boolean sizeIsExactly(long n) {
        return limit(n + 1).count() == n;
    }

    /**
     * Checks if the number of items in the enumerable is greater than the provided value.
     */
    public boolean sizeIsGreaterThan(long n) {
        return limit(n + 1).count() == n + 1;
    }

    /**
     * Checks if the number of items in the enumerable is less than the provided value.
     */
    public boolean sizeIsLessThan(long n) {
        return limit(n).count() < n;
    }

    /**
     * Skips a number of items in the enumerable and returns the remaining items
     * as a new enumerable.
     */
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

    /**
     * Reduces the enumerable into a sum based on the provided mapping function.
     * Will convert numbers into doubles to perform the calculations. Use reduce
     * with a custom Accumulator if you need more precision.
     *
     * As with all reduction functions, sum will force iteration.
     */
    public <R extends Number> Optional<Double> sum(Function<T, R> mappingFunction) {
        if(isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(map(mappingFunction)
                .reduce(0.0, (seed, x) -> seed + x.doubleValue()));
    }

    /**
     * Collects the enumerable into an array with a size provided in the init function.
     */
    public T[] toArray(Function<Integer, T[]> initFunction) {
        Enumerable<T> saved = save();
        T[] array = initFunction.apply(saved.count());

        saved.limit(array.length).forEach((x, i) -> array[i] = x);

        return array;
    }

    /**
     * Collects the enumerable into an array. Determines the type from the first
     * non-null element.
     */
    public T[] toArray() {
        if(isEmpty()) {
            return (T[])new Object[0];
        }

        Optional<T> first = filter(x -> x != null).findFirst();

        if(first.isPresent()) {
            Class<?> firstClass = first.get().getClass();

            return toArray(size -> (T[]) Array.newInstance(firstClass, size));
        }

        return toArray(size -> (T[]) Array.newInstance(Object.class, size));
    }

    /**
     * Collects the enumerable into a list.
     */
    public List<T> toList() {
        return collect(Collectors.toList());
    }

}
