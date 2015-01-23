import java.util.Iterator;
import java.util.function.Consumer;

public class PeekIterator<T> implements Iterator<T> {

    private final Consumer<T> consumer;
    private final Iterator<T> iterator;

    public PeekIterator(Iterable<T> iterable, Consumer<T> consumer) {

        iterator = iterable.iterator();
        this.consumer = consumer;
    }

    @Override
    public boolean hasNext() {
        return iterator.hasNext();
    }

    @Override
    public T next() {
        T next = iterator.next();

        consumer.accept(next);

        return next;
    }
}
