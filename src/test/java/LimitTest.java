import com.google.inject.Inject;
import org.jukito.JukitoRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
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
public class LimitTest {

    @Inject
    Iterable<String> stringIterable;

    @Inject
    Iterator<String> stringIterator;

    @Before
    public void setup() {
        when(stringIterable.iterator()).thenReturn(stringIterator);
        when(stringIterator.hasNext()).thenReturn(true);
        when(stringIterator.next()).thenReturn("foobar");

    }

    @Test
    public void firstItemIsTaken() {
        Enumerable<Integer> integers = Enumerable.range(1, 10);

        Optional<Integer> first = integers.findFirst();

        assertTrue(first.isPresent());
        assertThat(first.get(), is(1));
    }

    @Test
    public void firstFiveItemsAreTaken() {
        Enumerable<Integer> integers = Enumerable.range(1, 10);

        Enumerable<Integer> limited = integers.limit(5);

        assertTrue(limited.sizeIsExactly(5));
        assertThat(limited, contains(1, 2, 3, 4, 5));
    }

    @Test
    public void emptyIsReturnedOnFirst() {
        Enumerable<Object> enumerable = Enumerable.empty();

        Optional<Object> first = enumerable.findFirst();

        assertFalse(first.isPresent());
    }

    @Test
    public void firstCallsNextOnlyOnce() {
        Enumerable<String> strings = Enumerable.of(stringIterable);

        strings.findFirst();

        verify(stringIterator, times(1)).hasNext();
        verify(stringIterator, times(1)).next();
    }

    @Test
    public void limitCallsNextOnlyOnce() {
        Enumerable<String> strings = Enumerable.of(stringIterable);

        strings.limit(1).count();

        verify(stringIterator, times(1)).hasNext();
        verify(stringIterator, times(1)).next();
    }

    @Test
    public void itemsFromTooShortListAreTaken() {
        Enumerable<Integer> integers = Enumerable.range(1, 3);

        Enumerable<Integer> limited = integers.limit(5);

        assertTrue(limited.sizeIsExactly(3));
        assertThat(limited, contains(1, 2, 3));
    }

    @Test
    public void itemsFromEmptyListAreNotTaken() {
        Enumerable<Integer> empty = Enumerable.empty();

        Enumerable<Integer> limited = empty.limit(3);

        assertTrue(limited.isEmpty());
    }

    @Test
    public void negativeLimitReturnsEmpty() {
        assertTrue(Enumerable.of(1, 2, 3).limit(-1).isEmpty());
    }

    @Test
    public void lastItemIsTaken() {
        assertThat(Enumerable.of(1,2,3).findLast().get(), is(3));
    }

    @Test
    public void iteratorIsCalledCorrectNumberOfTimes() {
        ArrayList<Integer> calls = new ArrayList<>();

        Enumerable<Integer> limit = Enumerable.range(1, 10)
                                              .peek(x -> calls.add(x))
                                              .limit(5);

        assertThat(limit.count(), is(calls.size()));
    }

}
