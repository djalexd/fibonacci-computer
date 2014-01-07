package com.github.acme.learning.threading;

/**
 * Created by alexpeptan on 03/01/14.
 */
public class FibonacciComputerWithThreadSignalingDataHolder implements FibonacciComputer{

    @Override
    public long computeFibonacciAtIndex(int position) {
        FibonacciDataHolder.initializeDataHolderIfNotYetInitialized();
        System.out.println("computeFibonacciAtIndex " + position);
        if(position < 1) {
            throw new IllegalArgumentException("Position value was " + position);
        }

        if(FibonacciDataHolder.waitNotifyObjects[position] == null) {
            FibonacciDataHolder.waitNotifyObjects[position] = new MyWaitNotify();
        }

        return FibonacciDataHolder.getFibonacci(position);
    }


    class MonitorObject {}

    class MyWaitNotify {
        MonitorObject myMonitorObject;
        boolean wasSignaled = false;

        MyWaitNotify() {
            myMonitorObject = new MonitorObject();
        }

        public void doWait() {
            synchronized (myMonitorObject){
                while(!wasSignaled) {
                    try {
                        myMonitorObject.wait();
                    } catch(InterruptedException e) {}// hide it for the moment
                }
            }
        }

        public void doNotify(){
            synchronized (myMonitorObject){
                wasSignaled = true;
                myMonitorObject.notifyAll();
            }
        }
    }

    static class FibonacciDataHolder {
        private static boolean initialized = false;
        private static long[] computedData = new long[50];
        private static MyWaitNotify[] waitNotifyObjects = new MyWaitNotify[50];
        private static boolean[] computationStarted = new boolean[50];

        public static void initializeDataHolderIfNotYetInitialized()
        {
            if(!initialized){
                initialized = true;
                System.out.println("Data holder initialized");// nu se initializeaza ca si static block.. trebuie sa mai citesc despre asta
                for(int i = 0; i < 50; i++){
                    computedData[i] = -1;
                    computationStarted[i] = false;
                }
                computedData[1] = 0;
                computedData[2] = 1;
            }
        }

        public static long getFibonacci(final int position) {
            System.out.println("getFibonacci " + position);
            if(computedData[position] != -1) {
                return computedData[position];
            }
            else {
                if(!computationStarted[position]){
                    // first thread to engage this computation
                    computationStarted[position] = true;
                    System.out.println("start computation for position " + position);

                    System.out.println("start thread to get value for position " + (position - 2));
                    // compute data for lower levels
                    Runnable runnable = new Runnable() {
                        public void run(){
                            setFibonacci(position - 2, new FibonacciComputerWithThreadSignalingDataHolder().computeFibonacciAtIndex(position - 2));
                            //results consolidation, if the case
                            if(computedData[position - 3] != -1) {
                                setFibonacci(position - 1, computedData[position - 2] + computedData[position - 3]);
                            }
                            if(computedData[position - 1] != -1) {
                                setFibonacci(position, computedData[position - 2] + computedData[position - 1]);
                            }
                        }
                    };
                    Thread t = new Thread(runnable);
                    t.start();

                    System.out.println("start thread to get value for position " + (position - 1));
                    Runnable runnable2 = new Runnable() {
                        public void run(){
                            setFibonacci(position - 1, new FibonacciComputerWithThreadSignalingDataHolder().computeFibonacciAtIndex(position - 1));
                            //results consolidation, if the case
                            if(computedData[position - 2] != -1) {
                                setFibonacci(position, computedData[position - 2] + computedData[position - 1]);
                            }
                            if(computedData[position] != -1) {
                                setFibonacci(position + 1, computedData[position - 1] + computedData[position]);
                            }
                        }
                    };
                    Thread t2 = new Thread(runnable2);
                    t2.start();

                    System.out.println("lock computation on current level ->" + position +" position so other threads will not initiate computation ");
                    // lock computation on current level -> position so other threads will not initiate computation
                    waitNotifyObjects[position].doWait();
                } else {
                    // computation already started for this position. Will sleep and wait for signal to wake up and read the result.
                    System.out.println("Computation already started for this position: " + position +". Will sleep and wait for signal to wake up and read the result.");
                    waitNotifyObjects[position].doWait(); // duplicate line for clarity
                }
            }

            return computedData[position];
        }


        public static void setFibonacci(int position, long value) {
            computedData[position] = value;

            //notification step
            if(null != waitNotifyObjects[position]) {
                waitNotifyObjects[position].doNotify();
            }
        }
    }
}
