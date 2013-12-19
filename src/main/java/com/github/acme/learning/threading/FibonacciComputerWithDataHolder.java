package com.github.acme.learning.threading;

/**
 * Created by alexpeptan on 19/12/13.
 */
public class FibonacciComputerWithDataHolder implements FibonacciComputer {
    private boolean child1Computed = false;
    private boolean child2Computed = false;
    private final FibonacciComputerWithDataHolder parentInstance = this;
    private FibonacciChildComputerWithDataHolder child1;
    private FibonacciChildComputerWithDataHolder child2;
    private Long result = null;


    @Override
    public long computeFibonacciAtIndex(final int position) {

        FibonacciDataHolder.initializeDataHolderIfNotYetInitialized();

        if(position < 1) {
            throw new IllegalArgumentException("Position value was " + position);
        }
        // start thread and continue
        // compute fib(position - 2)
        long alreadyComputedValue;


        if((alreadyComputedValue = FibonacciDataHolder.getFibonacci(position)) == -1 ){
            long alreadyComputedChild1 = FibonacciDataHolder.getFibonacci(position - 2);
            if(alreadyComputedChild1 == -1){
                Runnable runnable = new Runnable() {
                    public void run(){
                        // child invocation
                        child1 = new FibonacciChildComputerWithDataHolder(parentInstance, position - 2, 1);
                        child1.calculateNextFibonacci();
                    }
                };
                Thread t = new Thread(runnable);
                t.start();
                child1Computed = false;
            } else {
                child1Computed = true;
            }

            // start thread and continue
            // compute fib(position - 1)
            long alreadyComputedChild2 = FibonacciDataHolder.getFibonacci(position - 1);
            if(alreadyComputedChild2 == -1){
                Runnable runnable = new Runnable() {
                    public void run(){
                        // child invocation
                        child2 = new FibonacciChildComputerWithDataHolder(parentInstance, position - 1, 2);
                        child2.calculateNextFibonacci();
                    }
                };
                Thread t = new Thread(runnable);
                t.start();
                child2Computed = false;
            } else {
                child2Computed = true;
            }


            while(!(child1Computed && child2Computed)) {
                // keep waiting
                try{
                    Thread.currentThread().sleep(100); // unless this line exists the tests will never finish. Why?
                }catch(InterruptedException ex){
                    //hide interrupted exception.. cannot throw it.. due to compilation errors.
                }
            }

            // both child threads calculated values.
            // consolidate results after both responses are received.

            long child1Value, child2Value;

            if(alreadyComputedChild1 == -1){
                child1Value = child1.getResult();
                FibonacciDataHolder.setFibonacci(position - 2, child1Value);
            } else {
                child1Value = alreadyComputedChild1;
            }

            if(alreadyComputedChild2 == -1) {
                child2Value = child2.getResult();
                FibonacciDataHolder.setFibonacci(position - 1, child2Value);
            } else {
                child2Value = alreadyComputedChild2;
            }

            result = child1Value + child2Value;
        } else {
            result = alreadyComputedValue;
        }

        return result;
    }

    protected void notifyParentThread(int childID){
        if(childID == 1) {
            child1Computed = true;
        }
        if(childID == 2) {
            child2Computed = true;
        }
    }
}

class FibonacciDataHolder {
    private static boolean initialized = false;
    private static long[] computedData = new long[50];

    public static void initializeDataHolderIfNotYetInitialized()
    {
        if(!initialized){
            initialized = true;
            System.out.println("Data holder initialized");// un se initializeaza ca si static block.. trebuie sa mai citesc despre asta
            for(int i = 0; i < 50; i++){
                computedData[i] = -1;
            }
            computedData[1] = 0;
            computedData[2] = 1;
        }
    }

    public static long getFibonacci(int position) {
        return computedData[position];
    }


    public static void setFibonacci(int position, long value) {
        computedData[position] = value;
    }
}

class FibonacciChildComputerWithDataHolder{
    private FibonacciComputerWithDataHolder parentThread;
    private Long result = null;
    private int position = -1;
    private int childID = -1;

    protected FibonacciChildComputerWithDataHolder(FibonacciComputerWithDataHolder parent, int position, int childID) {
        parentThread = parent;
        this.position = position;
        this.childID = childID;
    }

    protected long calculateNextFibonacci(){
        result = new FibonacciComputerMultiThreadWithOverhead().computeFibonacciAtIndex(position);

        // when done notify parent
        parentThread.notifyParentThread(childID);

        return result;
    }

    public Long getResult() {
        return result;
    }
}
