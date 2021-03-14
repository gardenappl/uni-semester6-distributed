package ua.yuriih.task4garden;

public class SimpleReadWriteLock {
    private int readers = 0;
    private static final int WRITING = -1;

    synchronized void lockWriting() throws InterruptedException {
        while (readers != 0) {
            wait();
        }
        readers = WRITING;
    }

    synchronized void lockReading() throws InterruptedException {
        while (readers == WRITING) {
            wait();
        }
        readers++;
    }

    synchronized void unlockReading() throws IllegalMonitorStateException {
        if (readers <= 0) {
            throw new IllegalMonitorStateException("Tried to unlock read lock when nobody is reading.");
        } else {
            readers--;
            notifyAll();
        }
    }

    synchronized void unlockWriting() throws IllegalMonitorStateException {
        if (readers != WRITING) {
            throw new IllegalMonitorStateException("Tried to unlock write lock when nobody is writing.");
        } else {
            readers = 0;
            notifyAll();
        }
    }
}
