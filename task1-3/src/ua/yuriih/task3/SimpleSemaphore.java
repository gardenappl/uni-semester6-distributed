package ua.yuriih.task3;

public class SimpleSemaphore {
    private volatile int permits;
//    private final Object lock = new Object();

    public SimpleSemaphore(int permits) {
        this.permits = permits;
    }

    public synchronized void acquire() throws InterruptedException {
        while (permits <= 0) {
            wait();
        }

        permits--;
    }

    public synchronized void release() {
        permits++;

        if (permits > 0) {
            notifyAll();
        }
    }

    public synchronized int availablePermits() {
        return permits;
    }
}
