import java.util.Optional;

public class IntEnumerable extends NumberEnumerable {

    public static Optional<Integer> min(Enumerable<Integer> enumerable) {
        return aggregate(enumerable, (acc, x) -> Integer.min(acc, x));
    }

    public static Optional<Integer> max(Enumerable<Integer> enumerable) {
        return aggregate(enumerable, (acc, x) -> Integer.max(acc, x));
    }


}
