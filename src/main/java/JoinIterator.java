import java.util.Iterator;
import java.util.LinkedList;

public class JoinIterator<T> implements Iterator<T> {

//    private final Iterator<T> iterator;
//    private final ArrayIterator<T> joinedIterator;
    private int cursor;
    private Iterator<T>[] iterators;

    public JoinIterator(Iterator<T>... iterators) {
        this.iterators = iterators;
//        iterator = iterable.iterator();
//        joinedIterator = new ArrayIterator<>(items);


//        iterators[0] = iterator;
//        iterators[1] = joinedIterator;
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
