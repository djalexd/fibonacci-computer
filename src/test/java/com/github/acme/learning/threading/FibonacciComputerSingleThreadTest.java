package com.github.acme.learning.threading;

/**
 * Created by alexpeptan on 15/12/13.
 */
public class FibonacciComputerSingleThreadTest extends AbstractFibonacciTest{
    @Override
    protected FibonacciComputer computer() {
        return new FibonacciComputerSingleThread();
    }
}
