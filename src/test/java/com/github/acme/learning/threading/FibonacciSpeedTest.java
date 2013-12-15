package com.github.acme.learning.threading;

import org.fest.assertions.api.Assertions;
import org.junit.Test;

/**
 * @author alex.dobjanschi
 * @since 4:24 PM 12/15/13
 */
public abstract class FibonacciSpeedTest {

    protected abstract FibonacciComputer singleThreadedComputer();

    protected abstract FibonacciComputer multiThreadedComputer();

    @Test
    public void shouldHaveAFactorOf2InSpeed() {
        // when
        // then
        long time1 = computeFibonacciNumberAtIndexAndReturnExecutionTime(singleThreadedComputer(), 20);
        long time2 = computeFibonacciNumberAtIndexAndReturnExecutionTime(multiThreadedComputer(), 20);
        // assert
        Assertions.assertThat(time2).isLessThanOrEqualTo(time1 >> 1);
    }


    protected long computeFibonacciNumberAtIndexAndReturnExecutionTime(
            final FibonacciComputer computer, int position) {

        final long start = System.currentTimeMillis();

        computer.computeFibonacciAtIndex(position);

        final long end = System.currentTimeMillis();
        return end - start;
    }

}
