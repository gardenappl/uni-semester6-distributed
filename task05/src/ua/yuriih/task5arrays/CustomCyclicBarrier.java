package ua.yuriih.task5arrays;

public class CustomCyclicBarrier {
    private final Runnable barrierAction;
    public final int parties;
    private final TheBarrier theBarrier;

    public CustomCyclicBarrier(int parties, Runnable barrierAction) {
        if (parties <= 0)
            throw new IllegalArgumentException("CyclicBarrier works with 1 or more parties.");
        this.parties = parties;
        this.theBarrier = new TheBarrier();
        theBarrier.arrivedIndex = 0;
        theBarrier.exitedIndex = 0;
        this.barrierAction = barrierAction;
    }
    
    public void await() throws InterruptedException {
        //Wait for barrier threads to wake up before entering synchronized method
        while (theBarrier.arrivedIndex == parties) {
            Thread.yield();
        }
        theBarrier.await();
    }


    private class TheBarrier {
        int arrivedIndex;
        private int exitedIndex;

        synchronized void await() throws InterruptedException {
            int currentIndex = arrivedIndex + 1;
            arrivedIndex = currentIndex;

            while (arrivedIndex < parties) {
                wait();
            }

            //First thread to exit the barrier
            if (currentIndex == parties) {
                barrierAction.run();
                notifyAll();
            }

            exitedIndex++;

            //Last thread to exit the barrier
            if (exitedIndex == parties) {
                //Reset barrier
                exitedIndex = 0;
                arrivedIndex = 0;
            }
        }
    }
}
