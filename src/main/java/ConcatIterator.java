import java.util.Iterator;

public class ConcatIterator<T> implements Iterator<T> {

    private Iterator<T> currentIterator;
    private Iterator<? extends Iterator<T>> iterators;

    public ConcatIterator(Iterator<? extends Iterator<T>> iterators) {
        this.iterators = iterators;
        this.currentIterator = null;
    }

    public ConcatIterator(Iterator<T>... iterators) {
        this(new ArrayIterator<>(iterators));
    }

    @Override
    public boolean hasNext() {
        while((currentIterator == null || !currentIterator.hasNext())
                && iterators.hasNext()) {
            currentIterator = iterators.next();
        }

        return currentIterator.hasNext();
    }

    @Override
    public T next() {
        return currentIterator.next();
    }
}
