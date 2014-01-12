package com.github.acme.learning.threading;

import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author alex.dobjanschi
 * @since 5:03 PM 1/12/14
 */
public class LocksFibonacciComputer implements FibonacciComputer {

    private ArrayList<Long> computedValues = new ArrayList<Long>(50);

    @Override
    public long computeFibonacciAtIndex(int position) {
        if (computedValues.size() < position) {
            // Not yet computed, but will be computed now.
            final ReentrantLock lock = new ReentrantLock();
            lock.lock();

            lock.unlock();
        }

        return computedValues.get(position);
    }
}
