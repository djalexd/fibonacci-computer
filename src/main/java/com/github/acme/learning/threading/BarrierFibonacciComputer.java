package com.github.acme.learning.threading;

import java.util.concurrent.*;

/**
 * @author alex.dobjanschi
 * @since 4:26 PM 1/12/14
 */
public class BarrierFibonacciComputer implements FibonacciComputer {

    private ExecutorService executor = Executors.newFixedThreadPool(4);

    @Override
    public long computeFibonacciAtIndex(int position) {

        int positionThatWillBeCalculated = getMaximumPositionBefore(position);

        if (positionThatWillBeCalculated < position) {
            // Somehow wait for 'positionToBeCalculated' to be calculated
            // and wait with a lock to compute from 'A' to 'position'
        } else {
            // wait for 'position' to be calculated and return it.
        }

        Future<Long> f1 = executor.submit(new ComputeFibonacciAtPosition(position - 2));
        Future<Long> f2 = executor.submit(new ComputeFibonacciAtPosition(position - 1));

        try {
            return f1.get() + f2.get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    private synchronized int getMaximumPositionBefore(int position) {
        return -1;
    }


    final class ComputeFibonacciAtPosition implements Callable<Long> {
        private int position;
        ComputeFibonacciAtPosition(int position) {
            this.position = position;
        }

        @Override
        public Long call() throws Exception {
            return new BarrierFibonacciComputer().computeFibonacciAtIndex(this.position);
        }
    }
}
