import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class SortIterator<T> implements Iterator<T> {
    private Iterator<T> iterator;
    private final Comparator<T> comparator;
    private Iterator<T> sortedIterator;

    public SortIterator(Iterator<T> iterator, Comparator<T> comparator) {
        this.iterator = iterator;
        this.comparator = comparator;
    }

    private Iterator<T> getSortedIterator() {
        if(sortedIterator == null) {
            List<T> list = getList();
            list.sort(comparator);

            sortedIterator = list.iterator();
        }

        return sortedIterator;
    }

    private List<T> getList() {
        List<T> list = new LinkedList<>();
        iterator.forEachRemaining(x -> list.add(x));

        return list;
    }

    @Override
    public boolean hasNext() {
        return getSortedIterator().hasNext();
    }

    @Override
    public T next() {
        return getSortedIterator().next();
    }
}