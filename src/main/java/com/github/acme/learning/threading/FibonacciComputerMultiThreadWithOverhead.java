package com.github.acme.learning.threading;

/**
 * Created by alexpeptan on 15/12/13.
 */
public class FibonacciComputerMultiThreadWithOverhead implements FibonacciComputer{
    private boolean firstChildReady = false;
    private boolean secondChildReady = false;
    private final FibonacciComputerMultiThreadWithOverhead parentInstance = this;
    private FibonacciChildComputerMultiThreadWithOverhead child1;
    private FibonacciChildComputerMultiThreadWithOverhead child2;
    private Long result = null;


    @Override
    public long computeFibonacciAtIndex(final int position) {
        // start thread and continue
        // compute fib(position - 2)

        if(position > 2){
            Runnable runnable = new Runnable() {
                public void run(){
                    // child invocation
                    child1 = new FibonacciChildComputerMultiThreadWithOverhead(parentInstance, position - 2, 1);
                    child1.calculateNextFibonacci();
                }
            };
            Thread t = new Thread(runnable);
            t.start();

            // start thread and continue
            // compute fib(position - 1)
            runnable = new Runnable() {
                public void run(){
                    // child invocation
                    child2 = new FibonacciChildComputerMultiThreadWithOverhead(parentInstance, position - 1, 2);
                    child2.calculateNextFibonacci();
                }
            };
            t = new Thread(runnable);
            t.start();

            while(!(firstChildReady && secondChildReady)) {
                // keep waiting
                try{
                    Thread.currentThread().sleep(100); // unless this line exists the tests will never finish. Why?
                }catch(InterruptedException ex){
                    //hide interrupted exception.. cannot throw it.. due to compilation errors.
                }
            }

            // both child threads calculated values.
            // consolidate results after both responses are received.

            result = child1.getResult() + child2.getResult();
        } else {
            if(position == 2) {
                result = 1l;
            } else {
                if(position == 1) {
                    result = 0l;
                } else {
                    throw new IllegalArgumentException("Position value was " + position);
                }
            }
        }

        return result;
    }

    protected void notifyParentThread(int childID){
        if(childID == 1) {
            firstChildReady = true;
        }
        if(childID == 2) {
            secondChildReady = true;
        }
    }
}

class FibonacciChildComputerMultiThreadWithOverhead {
    private FibonacciComputerMultiThreadWithOverhead parentThread;
    private Long result = null;
    private int position = -1;
    private int childID = -1;

    protected FibonacciChildComputerMultiThreadWithOverhead(FibonacciComputerMultiThreadWithOverhead parent, int position, int childID) {
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