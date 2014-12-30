import java.util.Optional;

public class IntEnumerable {

    public static Optional<Integer> min(Enumerable<? extends Integer> enumerable) {
        Optional<? extends Integer> first = enumerable.first();

        if(!first.isPresent()) {
            return Optional.empty();
        }

        return Optional.of(enumerable.flatMap((acc, x) -> Integer.min(acc, x), first.get()));
    }
}
