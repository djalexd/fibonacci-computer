package com.github.acme.learning.threading;

import org.fest.assertions.api.Assertions;
import org.junit.Test;

/**
 * @author alex.dobjanschi
 * @since 3:57 PM 12/15/13
 */
public abstract class AbstractFibonacciTest {

    /**
     * Get a new instance of {@link FibonacciComputer}.
     * @return
     */
    protected abstract FibonacciComputer computer();

    @Test(expected = IllegalArgumentException.class)
    public void shouldRejectNegativeNumbers() {
        // when
        // then
        computer().computeFibonacciAtIndex(-2);
        // assert
    }

    @Test
    public void shouldComputeFibonacciCorrectly1() {
        // when
        // then
        long value1 = computer().computeFibonacciAtIndex(5);
        // assert
        Assertions.assertThat(value1).isEqualTo(3);
    }

    @Test
    public void shouldComputeFibonacciCorrectly2() {
        // when
        // then
        long value1 = computer().computeFibonacciAtIndex(10);
        // assert
        Assertions.assertThat(value1).isEqualTo(34);
    }
}
