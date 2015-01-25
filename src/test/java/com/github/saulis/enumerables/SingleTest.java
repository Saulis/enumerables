package com.github.saulis.enumerables;

import org.junit.Test;

import java.util.NoSuchElementException;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertFalse;

public class SingleTest {

    @Test
    public void emptyIsReturnedOnEmptyCollection() {
        assertFalse(Enumerable.empty().findSingle().isPresent());
    }

    @Test(expected = NoSuchElementException.class)
    public void exceptionIsThrownOnTooLargeCollection() {
        Enumerable.of(1, 2).findSingle();
    }

    @Test
    public void singleItemIsReturned() {
        assertThat(Enumerable.of(1).findSingle().get(), is(1));
    }

}
