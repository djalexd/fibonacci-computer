package com.github.acme.learning.threading;

/**
 * Created by alexpeptan on 19/12/13.
 */
public class FibonacciComputerWithDataHolderTest extends AbstractFibonacciTest  {
    @Override
    protected FibonacciComputer computer() {
        return new FibonacciComputerWithDataHolder();
    }
}
