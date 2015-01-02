import java.util.Iterator;
import java.util.function.Predicate;

public class FilterIterator<T> implements Iterator<T> {

    private final Iterator<T> iterator;
    private final Predicate<T> predicate;
    private T nextItemToReturn = null;

    public FilterIterator(Iterable<T> iterable, Predicate<T> predicate) {
        iterator = iterable.iterator();
        this.predicate = predicate;
    }

    @Override
    public boolean hasNext() {
        while(iterator.hasNext()) {
            T next = iterator.next();
            if(predicate.test(next)) {
                nextItemToReturn = next;

                return true;
            }
        }

        return false;
    }

    @Override
    public T next() {
        T next = nextItemToReturn;
        nextItemToReturn = null;

        return next;
    }
}
