package com.github.acme.learning.threading;

/**
 * Created by alexpeptan on 04/01/14.
 */
public class FibonacciComputerWithThreadSignalingDataHolderTest extends AbstractFibonacciTest {
    @Override
    protected FibonacciComputer computer() {
        return new FibonacciComputerWithThreadSignalingDataHolder();
    }
}
