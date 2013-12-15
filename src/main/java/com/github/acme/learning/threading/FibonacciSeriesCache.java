package com.github.acme.learning.threading;

/**
 * @author alex.dobjanschi
 * @since 4:31 PM 12/15/13
 */
public interface FibonacciSeriesCache {
    /**
     * Gets a fibonacci number for a specific position. If missing, returns <code>null</code>.
     * @param position
     * @return Can be <code>null</code>.
     */
    Long getFibonacciNumberForPosition(int position);

    /**
     * Sets a fibonacci number at given position.
     * @param position
     * @param number
     */
    void setFibonacciNumberForPosition(int position, Long number);
}
