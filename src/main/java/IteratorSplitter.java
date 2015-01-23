import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.function.BiPredicate;

public class IteratorSplitter<T>{

    private final Iterator<T> iterator;
    private final HashMap<BiPredicate<T, Integer>, List<T>> matchedItems;
    private final List<T> remainder;
    private final Enumerable<BiPredicate<T, Integer>> predicates;
    private int cursor;

    public IteratorSplitter(Iterable<T> iterable, BiPredicate<T, Integer>... predicates) {
        this.cursor = 0;
        this.matchedItems = new HashMap<>();
        this.remainder = new ArrayList<>();
        this.iterator = iterable.iterator();

        this.predicates = Enumerable.of(predicates);
        this.predicates.forEach(x -> matchedItems.put(x, new ArrayList<>()));
    }

    public Iterator<T> getIterator(BiPredicate<T, Integer> predicate) {
        return new SplitIterator<>(this, matchedItems.get(predicate));
    }

    public Iterator<T> getRemainderIterator() {
        return new SplitIterator<>(this, remainder);
    }

    public boolean hasNext() {
        return iterator.hasNext();
    }

    public void iterate() {
        T next = iterator.next();

        Enumerable<BiPredicate<T,Integer>> matchedPredicates =
                predicates.filter(x -> x.test(next, cursor));

        if(matchedPredicates.isEmpty()) {
            remainder.add(next);
        } else {
            matchedPredicates.forEach(x -> matchedItems.get(x).add(next));
        }

        cursor++;
    }
}
