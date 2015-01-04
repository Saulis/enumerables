import org.junit.Test;

import java.util.Set;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsIterableContainingInOrder.contains;

public class CollectTest {
    @Test
    public void enumerableIsCollected() {
        Enumerable<Integer> enumerable = Enumerable.of(1, 1, 2, 3, 5, 8);

        Set<Integer> set = enumerable.collect(Collectors.toSet());

        assertThat(set, contains(1,2,3,5,8));
    }


}
