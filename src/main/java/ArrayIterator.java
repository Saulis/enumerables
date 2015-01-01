import java.util.Iterator;

public class ArrayIterator<T> implements Iterator<T> {
    private final T[] items;
    private int cursor;

    public ArrayIterator(T[] items) {
        this.cursor = 0;
        this.items = items;
    }

    @Override
    public boolean hasNext() {
        return cursor < items.length;
    }

    @Override
    public T next() {
        return items[cursor++];
    }
}
