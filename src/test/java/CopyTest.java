import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertTrue;

public class CopyTest {
    @Test
    public void enumerableDoesNoChangeAfterCopy() {
        ArrayList<String> list = new ArrayList<>();
        list.add("foo");

        Enumerable<String> strings = Enumerable.of(list).copy();
        assertTrue(strings.sizeIsExactly(1));

        list.add("bar");
        assertTrue(strings.sizeIsExactly(1));
    }

}
