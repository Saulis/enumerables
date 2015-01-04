import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class SizeTest {
    @Test
    public void sizeIsExactly() {
        Enumerable<Integer> enumerable = Enumerable.of(1, 1, 2, 3, 5, 8);

        assertTrue(enumerable.sizeIsExactly(6));
    }
}
