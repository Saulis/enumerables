import java.util.Iterator;
import java.util.List;

public class SplitIterator<T> implements Iterator<T> {

    private final IteratorSplitter<T> iterator;
    private final List<T> list;
    private int cursor;

    public SplitIterator(IteratorSplitter<T> iterator, List<T> list) {

        this.cursor = 0;
        this.iterator = iterator;
        this.list = list;
    }

    @Override
    public boolean hasNext() {
        while(true) {
            if(list.size() > cursor) {
                return true;
            }

            if(!iterator.hasNext()) {
                return false;
            }

            iterator.next();
        }
    }

    @Override
    public T next() {
        return list.get(cursor++);
    }
}
