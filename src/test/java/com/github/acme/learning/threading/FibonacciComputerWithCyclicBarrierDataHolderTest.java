package com.github.acme.learning.threading;

/**
 * Created by alexpeptan on 08/01/14.
 */
public class FibonacciComputerWithCyclicBarrierDataHolderTest extends AbstractFibonacciTest  {
    @Override
    protected FibonacciComputer computer() {
        return new FibonacciComputerWithCyclicBarrierDataHolder();
    }
}