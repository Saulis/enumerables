import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class SizeTest {
    @Test
    public void sizeIsExactly() {
        Enumerable<Integer> enumerable = Enumerable.of(1, 1, 2, 3, 5, 8);

        assertTrue(enumerable.sizeIsExactly(6));
    }

    @Test
    public void negativeSizeIsExactlyIsHandled() {
        assertFalse(Enumerable.of(1).sizeIsExactly(-1));
    }

    @Test
    public void emptySizeIsExactly() {
        assertTrue(Enumerable.empty().sizeIsExactly(0));
    }

    @Test
    public void sizeIsGreaterThan() {
        assertTrue(Enumerable.of(1).sizeIsGreaterThan(0));
    }

    @Test
    public void emptySizeIsGreaterThan() {
        assertFalse(Enumerable.empty().sizeIsGreaterThan(0));
    }

    @Test
    public void negativeSizeIsGreaterThan() {
        assertTrue(Enumerable.empty().sizeIsGreaterThan(-1));
    }

    @Test
    public void sizeIsLessThan() {
        assertTrue(Enumerable.of(1).sizeIsLessThan(2));
    }

    @Test
    public void emptySizeIsLessThan() {
        assertFalse(Enumerable.empty().sizeIsLessThan(0));
    }

    @Test
    public void negativeSizeIsLessThan() {
        assertFalse(Enumerable.empty().sizeIsLessThan(-1));
    }
}
