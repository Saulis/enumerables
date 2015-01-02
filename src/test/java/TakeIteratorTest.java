import com.google.inject.Inject;
import org.jukito.JukitoRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Iterator;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsIterableContainingInOrder.contains;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(JukitoRunner.class)
public class TakeIteratorTest {

    @Inject
    Iterable<String> stringIterable;

    @Inject
    Iterator<String> stringIterator;

    @Test
    public void firstItemIsTaken() {
        Enumerable<Integer> integers = Enumerable.range(1, 10);

        Optional<Integer> first = integers.first();

        assertTrue(first.isPresent());
        assertThat(first.get(), is(1));
    }

    @Test
    public void firstFiveItemsAreTaken() {
        Enumerable<Integer> integers = Enumerable.range(1, 10);

        Enumerable<Integer> take = integers.take(5);

        assertTrue(take.sizeIsExactly(5));
        assertThat(take, contains(1, 2, 3, 4, 5));
    }

    @Test
    public void emptyIsReturnedOnFirst() {
        Enumerable<Object> enumerable = Enumerable.empty();

        Optional<Object> first = enumerable.first();

        assertFalse(first.isPresent());
    }

    @Test
    public void firstCallsNextOnlyOnce() {
        when(stringIterable.iterator()).thenReturn(stringIterator);
        when(stringIterator.hasNext()).thenReturn(true);
        when(stringIterator.next()).thenReturn("foobar");
        Enumerable<String> strings = Enumerable.of(stringIterable);

        strings.first();

        verify(stringIterator, times(1)).hasNext();
        verify(stringIterator, times(1)).next();
    }

    @Test
    public void itemsFromTooShortListAreTaken() {
        Enumerable<Integer> integers = Enumerable.range(1, 3);

        Enumerable<Integer> take = integers.take(5);

        assertTrue(take.sizeIsExactly(3));
        assertThat(take, contains(1,2,3));
    }

    @Test
    public void itemsFromEmptyListAreNotTaken() {
        Enumerable<Integer> empty = Enumerable.empty();

        Enumerable<Integer> take = empty.take(3);

        assertTrue(take.isEmpty());
    }
}
