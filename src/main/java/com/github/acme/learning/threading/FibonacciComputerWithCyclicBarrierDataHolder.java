package com.github.acme.learning.threading;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * Created by alexpeptan on 07/01/14.
 */
public class FibonacciComputerWithCyclicBarrierDataHolder implements FibonacciComputer {
    @Override
    public long computeFibonacciAtIndex(int position) {
        FibonacciDataHolder.initializeDataHolderIfNotYetInitialized();
        System.out.println("computeFibonacciAtIndex " + position);
        if(position < 1) {
            throw new IllegalArgumentException("Position value was " + position);
        }

        if(FibonacciDataHolder.alreadyComputedData(position)) {
            return FibonacciDataHolder.getFibonacci(position);
        } else {
            CyclicBarrier initialWaitingBarrier = new CyclicBarrier(2);
            CyclicBarrierRunnable initialBarrierRunnable = new CyclicBarrierRunnable(initialWaitingBarrier, position);
            new Thread(initialBarrierRunnable).start();

            try {
                initialWaitingBarrier.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            }
            System.out.println("There is life after MAIN WAIT notification!!! in " + Thread.currentThread().getName());


            System.out.println("Back in the initial computeFibonacciAtIndex " + position);
            System.out.println("Value for position " + position + " is " + FibonacciDataHolder.getFibonacci(position));
            return FibonacciDataHolder.getFibonacci(position);
        }
    }

    class CyclicBarrierRunnable implements Runnable{
        private CyclicBarrier sendNotificationBarrier;
        private int position;

        public CyclicBarrierRunnable(CyclicBarrier waitingNotificationBarrier, int position){
            this.sendNotificationBarrier = waitingNotificationBarrier;
            this.position = position;
        }

        @Override
        public void run() {
            System.out.println("Running CyclicBarrierRunnable for position " + position);
            if(FibonacciDataHolder.alreadyComputedData(position)){
                System.out.println("Already computed data for " + position);
                // do nothing
                // maybe some synchronization - run DummyCyclicBarrierRunnable twice
                sendNotificationThroughBarrier();
            } else {
                // data not computed
                System.out.println("Data not computed for " + position + ". Have to run another 2 CyclicBarrierRunnable for newWaitingNotificationBarrier");
                System.out.println("Already computed data for " + position);
                CyclicBarrier newWaitingNotificationBarrier = new CyclicBarrier(3);
                CyclicBarrierRunnable newBarrierRunnable1 = new CyclicBarrierRunnable(newWaitingNotificationBarrier, position - 2);
                new Thread(newBarrierRunnable1).start();

                CyclicBarrierRunnable newBarrierRunnable2 = new CyclicBarrierRunnable(newWaitingNotificationBarrier, position - 1);
                new Thread(newBarrierRunnable2).start();

                System.out.println("waitNotificationThroughBarrier for " + position + " after running the two threads.");
                waitNotificationThroughBarrier(newWaitingNotificationBarrier);

                System.out.println("Values calculated for positions " + (position - 2) + " and " + (position - 1) + ". Consolidate results.");
                // after it wakes up, the data is certainly computed for positions position - 2 and position - 1.
                FibonacciDataHolder.consolidateCalculatedValues(position);

                System.out.println("Send notification through barrier.");
                // notify that the value was calculated
                sendNotificationThroughBarrier();

            }

        }

        private void sendNotificationThroughBarrier() {
            try {
                System.out.println(Thread.currentThread().getName() + " waiting at a barrier for send notification");
                this.sendNotificationBarrier.await();// 3 -> 2 or 2 -> 1
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            }
            System.out.println("There is life after SEND notification!!!" + Thread.currentThread().getName());
        }

        private void waitNotificationThroughBarrier(CyclicBarrier newWaitingNotificationBarrier) {
            try {
                System.out.println(Thread.currentThread().getName() + " waiting at a barrier for wait notification");
                newWaitingNotificationBarrier.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            }
            System.out.println("There is life after WAIT notification!!! in " + Thread.currentThread().getName());
        }


    }


    static class FibonacciDataHolder {
        private static boolean initialized = false;
        private static long[] computedData = new long[50];

        public static void initializeDataHolderIfNotYetInitialized()
        {
            if(!initialized){
                initialized = true;
                System.out.println("Data holder initialized");// nu se initializeaza ca si static block.. trebuie sa mai citesc despre asta
                for(int i = 0; i < 50; i++){
                    computedData[i] = -1;
                }
                computedData[1] = 0;
                computedData[2] = 1;
            }
        }

        public static long getFibonacci(final int position) {
            System.out.println("getFibonacci " + position);
            return computedData[position];
        }

        public static void setFibonacci(int position, long value) {
            computedData[position] = value;
        }

        private static boolean alreadyComputedData(int position) {
            return computedData[position] != -1;
        }

        private static void consolidateCalculatedValues(int position) {
            setFibonacci(position, computedData[position - 2] + computedData[position - 1]);
        }
    }
}