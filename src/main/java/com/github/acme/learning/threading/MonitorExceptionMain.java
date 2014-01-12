package com.github.acme.learning.threading;

/**
 * @author alex.dobjanschi
 * @since 5:14 PM 1/12/14
 */
public class MonitorExceptionMain {

    private Object monitor = new Object();

    public void doSomething() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000L);
                } catch (InterruptedException e) {}
                System.out.println("Notifying");
                synchronized (monitor) {
                    monitor.notify();
                }
            }
        }).start();


        synchronized (monitor) {
            try {
                System.out.println("Waiting");
                monitor.wait();
                System.out.println("Finished waiting");
            } catch (InterruptedException e) {}
        }
    }


    public static void main(String[] args) {
        MonitorExceptionMain main = new MonitorExceptionMain();
        main.doSomething();
    }
}
