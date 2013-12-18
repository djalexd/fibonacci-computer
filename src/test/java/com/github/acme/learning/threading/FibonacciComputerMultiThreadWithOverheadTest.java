package com.github.acme.learning.threading;

/**
 * Created by alexpeptan on 18/12/13.
 */
public class FibonacciComputerMultiThreadWithOverheadTest extends AbstractFibonacciTest{
    @Override
    protected FibonacciComputer computer() {
        return new FibonacciComputerMultiThreadWithOverhead();
    }
}
