import java.util.Iterator;

public class SkipIterator<T> implements Iterator<T> {

    private final Iterator<T> iterator;
    private final long n;
    private int cursor;

    public SkipIterator(Iterable<T> iterable, long n) {
        this.cursor = 0;
        this.iterator = iterable.iterator();
        this.n = n;
    }

    @Override
    public boolean hasNext() {
        while(cursor++ < n) {
            iterator.next();
        }

        return iterator.hasNext();
    }

    @Override
    public T next() {
        cursor++;
        return iterator.next();
    }
}
