import java.util.Iterator;

public class TakeIterator<T> implements Iterator<T> {

    private final Iterator<T> iterator;
    private final long n;
    private int cursor;

    public TakeIterator(Iterable<T> iterable, long n) {
        this.cursor = 0;
        this.iterator = iterable.iterator();
        this.n = n;
    }

    @Override
    public boolean hasNext() {
        return iterator.hasNext() && cursor < n;
    }

    @Override
    public T next() {
        cursor++;
        return iterator.next();
    }
}
