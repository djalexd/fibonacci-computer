package com.github.acme.learning.threading;

/**
 * Created by alexpeptan on 15/12/13.
 */
public class FibonacciComputerSingleThread implements FibonacciComputer{
    @Override
    public long computeFibonacciAtIndex(int position) {
        if(position < 0) {
            throw new IllegalArgumentException();
        } else
            if(position == 1 || position == 2){
                return position - 1;
            } else return computeFibonacciAtIndex(position - 2) + computeFibonacciAtIndex(position - 1);
    }
}
