package com.github.acme.learning.threading;

import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created by alexpeptan on 07/01/14.
 *
 *    No implementation with locks is possible for this problem because one lock should be unlocked
 *  from other threads, once the data prepared in other threads is computed and it can be used by
 *  the "parent" thread.
 *
 *  Next attempt: using Cyclic Barriers
 *
 */
public class FibonacciComputerWithReadWriteLockDataHolder implements FibonacciComputer {

    @Override
    public long computeFibonacciAtIndex(int position) {
        FibonacciDataHolder.initializeDataHolderIfNotYetInitialized();
        System.out.println("computeFibonacciAtIndex " + position);
        if(position < 1) {
            throw new IllegalArgumentException("Position value was " + position);
        }

//        if(FibonacciDataHolder.waitNotifyObjects[position] == null) {
//            FibonacciDataHolder.waitNotifyObjects[position] = new MyWaitNotify();
//        }

        return FibonacciDataHolder.getFibonacci(position);
    }


//    class MonitorObject {}
//
//    class MyWaitNotify {
//        MonitorObject myMonitorObject = new MonitorObject();
//        boolean wasSignaled = false;
//
//        public void doWait() {
//            synchronized (myMonitorObject){
//                while(!wasSignaled) {
//                    try {
//                        myMonitorObject.wait();
//                    } catch(InterruptedException e) {}// hide it for the moment
//                }
//            }
//        }
//
//        public void doNotify(){
//            synchronized (myMonitorObject){
//                wasSignaled = true;
//                myMonitorObject.notifyAll();
//            }
//        }
//    }

    static class FibonacciDataHolder {
        private static boolean initialized = false;
        private static long[] computedData = new long[50];
//        private static MyWaitNotify[] waitNotifyObjects = new MyWaitNotify[50];
        private static ReentrantReadWriteLock[] locks = new ReentrantReadWriteLock[50];
//        private static boolean[] computationStarted = new boolean[50];

        public static void initializeDataHolderIfNotYetInitialized()
        {
            if(!initialized){
                initialized = true;
                System.out.println("Data holder initialized");// nu se initializeaza ca si static block.. trebuie sa mai citesc despre asta
                for(int i = 0; i < 50; i++){
                    computedData[i] = -1;
//                    computationStarted[i] = false;
                    locks[i] = new ReentrantReadWriteLock();
                }
                computedData[1] = 0;
                computedData[2] = 1;
            }
        }

        public static long getFibonacci(final int position) {
            System.out.println("getFibonacci " + position);
            if(computedData[position] != -1) {
                // dummy use of read locks.
                locks[position].readLock().lock();
                long result = computedData[position];
                locks[position].readLock().unlock();
                return result;
            }
            else {
                locks[position].writeLock().lock();

                if(computedData[position] == -1){
                    // first thread to engage this computation
//                    computationStarted[position] = true;
                    System.out.println("start computation for position " + position);

                    System.out.println("start thread to get value for position " + (position - 2));
                    // compute data for lower levels
                    Runnable runnable = new Runnable() {
                        public void run(){

                            FibonacciComputerWithReadWriteLockDataHolder child1;
                            child1 = new FibonacciComputerWithReadWriteLockDataHolder();
                            long c1 = child1.computeFibonacciAtIndex(position - 2);
                            computedData[position - 2] = c1;
                            //results consolidation, if the case
                            if(computedData[position - 3] != -1) {
                                setFibonacci(position - 1, c1 + computedData[position - 3]);
                                //notification step
//                                if(null != waitNotifyObjects[position - 1]){
//                                    waitNotifyObjects[position - 1].doNotify();
//                                }

                            }
                            if(computedData[position - 1] != -1) {
                                setFibonacci(position, c1 + computedData[position - 1]);
                                //notification step
//                                if(null != waitNotifyObjects[position]) {
//                                    waitNotifyObjects[position].doNotify();
//                                }
                            }
                        }
                    };
                    Thread t = new Thread(runnable);
                    t.start();

                    System.out.println("start thread to get value for position " + (position - 1));
                    Runnable runnable2 = new Runnable() {
                        public void run(){

                            FibonacciComputerWithReadWriteLockDataHolder child2;
                            child2 = new FibonacciComputerWithReadWriteLockDataHolder();
                            long c2 = child2.computeFibonacciAtIndex(position - 1);
                            computedData[position - 1] = c2;
                            //results consolidation, if the case
                            if(computedData[position - 2] != -1) {
                                setFibonacci(position, computedData[position - 2] + c2);
                                //notification step
//                                if(null != waitNotifyObjects[position]) {
//                                    waitNotifyObjects[position].doNotify();
//                                }
                            }
                            if(computedData[position] != -1) {
                                setFibonacci(position + 1, c2 + computedData[position]);
                                //notification step
//                                if(null != waitNotifyObjects[position + 1]) {
//                                    waitNotifyObjects[position + 1].doNotify();
//                                }
                            }
                        }
                    };
                    Thread t2 = new Thread(runnable2);
                    t2.start();

//                    System.out.println("lock computation on current level ->" + position +" position so other threads will not initiate computation ");
//                    // lock computation on current level -> position so other threads will not initiate computation
//                    waitNotifyObjects[position].doWait();
                } else {
                    // computation already done for this position.
                    System.out.println("Computation already done for this position: " + position);
                }
            }

            return computedData[position];
        }


        public static void setFibonacci(int position, long value) {
            computedData[position] = value;

            locks[position].writeLock().unlock();
        }
    }
}
