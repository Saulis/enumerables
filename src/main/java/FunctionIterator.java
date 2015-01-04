import java.util.Iterator;
import java.util.function.Function;

public class FunctionIterator<T> implements Iterator<T> {

    private T seed;
    private final Function<T, T> function;
    private final int iterations;
    private int cursor;

    public FunctionIterator(T seed, Function<T, T> function, int iterations) {
        cursor = 0;
        this.seed = seed;
        this.function = function;
        this.iterations = iterations;
    }

    @Override
    public boolean hasNext() {
        return cursor < iterations;
    }

    @Override
    public T next() {
        cursor++;

        seed = function.apply(seed);

        return seed;
    }
}
