import java.util.Iterator;

public class ConcatIterator<T> implements Iterator<T> {

    private int cursor;
    private Iterator<T>[] iterators;

    public ConcatIterator(Iterator<T>... iterators) {
        this.iterators = iterators;
        this.cursor = 0;
    }

    @Override
    public boolean hasNext() {
        if(cursor < iterators.length) {
            if(iterators[cursor].hasNext()) {
                return true;
            } else if(cursor < iterators.length - 1) {
                return iterators[++cursor].hasNext();
            }
        }

        return false;
    }

    @Override
    public T next() {
        return iterators[cursor].next();
    }
}
