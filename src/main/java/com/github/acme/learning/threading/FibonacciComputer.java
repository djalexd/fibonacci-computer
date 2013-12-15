package com.github.acme.learning.threading;

/**
 * @author alex.dobjanschi
 * @since 3:54 PM 12/15/13
 */
public interface FibonacciComputer {
    /**
     * Computes the fibonacci number at given <code>position</code>.
     * @param position
     * @return
     */
    long computeFibonacciAtIndex(int position);
}
