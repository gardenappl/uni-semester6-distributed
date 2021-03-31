package ua.yuriih.task6a.game;

import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class GameOfLife {
    private static final int SIMULATION_INTERVAL = 500;
    private static final int DRAWING_INTERVAL = 100;

    private boolean[][] currentState;
    private boolean[][] nextState;

    final CyclicBarrier drawFrameBarrier;
    final Semaphore canSwap;

    public final int width;
    public final int height;

    private final int threadCount;

    public final GameOfLifeFrame frame;

    public GameOfLife(int width, int height, int threadCount) {
        this.threadCount = threadCount;
        this.width = width;
        this.height = height;
        if (this.threadCount > this.height)
            throw new IllegalArgumentException("Thread count must be lower or equal to row count");

        currentState = new boolean[height][];
        nextState = new boolean[height][];
        for (int i = 0; i < height; i++) {
            currentState[i] = new boolean[width];
            nextState[i] = new boolean[width];
        }

        frame = new GameOfLifeFrame(this);

        drawFrameBarrier = new CyclicBarrier(threadCount, () -> {
            try {
                Thread.sleep(SIMULATION_INTERVAL);
                swapBuffers();
            } catch (InterruptedException e) {
                return;
            }
        });

        canSwap = new Semaphore(1);
    }

    public void start() {
        int extraRows = height % threadCount;
        for (int row = 0; row < height;) {
            int rowCount = height / threadCount;
            if (extraRows > 0) {
                extraRows--;
                rowCount++;
            }
            Thread gameOfLifeThread = new Thread(new GameOfLifeRunnable(this, row, row + rowCount));
            gameOfLifeThread.setDaemon(true);
            gameOfLifeThread.start();

            row += rowCount;
        }
        Thread graphicsThread = new Thread(() -> {
            while (!Thread.interrupted()) {
                try {
                    Thread.sleep(DRAWING_INTERVAL);
                } catch (InterruptedException e) {
                    break;
                }
                frame.repaint();
            }
        });
        graphicsThread.setDaemon(true);
        graphicsThread.start();
    }

    public void setCellCurrentlyAlive(int x, int y, boolean isAlive) {
        currentState[y][x] = isAlive;
    }

    public boolean isCellCurrentlyAlive(int x, int y) {
        return currentState[y][x];
    }

    public void setNextCellAlive(int x, int y, boolean isAlive) {
        nextState[y][x] = isAlive;
    }

    private void swapBuffers() throws InterruptedException {
        canSwap.acquire();
        boolean[][] swap = currentState;
        currentState = nextState;
        nextState = swap;
        canSwap.release();
    }
}
