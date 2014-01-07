package com.github.acme.learning.threading;

/**
 * Created by alexpeptan on 07/01/14.
 */
public class FibonacciComputerWithReadWriteLockDataHolderTest extends AbstractFibonacciTest {
    @Override
    protected FibonacciComputer computer() {
        return new FibonacciComputerWithReadWriteLockDataHolder();
    }
}
