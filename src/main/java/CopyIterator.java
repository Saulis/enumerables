import java.util.Iterator;
import java.util.List;

public class CopyIterator<T> implements Iterator<T> {

    private Iterator<T> iterator;
    private final List<T> list;

    public CopyIterator(Iterator<T> iterator, List<T> list) {
        this.iterator = list.isEmpty() ? iterator : list.iterator();
        this.list = list;
    }

    @Override
    public boolean hasNext() {
        return iterator.hasNext();
    }

    @Override
    public T next() {
        T next = iterator.next();

        if(!list.contains(next)) {
            list.add(next);
        }

        return next;
    }
}
