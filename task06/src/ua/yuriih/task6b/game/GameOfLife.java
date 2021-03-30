package ua.yuriih.task6b.game;

import java.awt.*;
import java.util.List;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class GameOfLife {
    private static final int SIMULATION_INTERVAL = 500;
    private static final int DRAWING_INTERVAL = 250;

    private int[][] currentState;
    private int[][] nextState;

    final CyclicBarrier drawFrameBarrier;
    final Semaphore canSwap;

    public final int width;
    public final int height;
    
    public static final int DEAD_CIV = 0;
    final List<Color> civColors;

    private final int threadCount;

    public final GameOfLifeFrame frame;

    public GameOfLife(int width, int height, int threadCount, List<Color> civs) {
        this.threadCount = threadCount;
        this.width = width;
        this.height = height;
        this.civColors = civs;

        if (this.threadCount > this.height)
            throw new IllegalArgumentException("Thread count must be lower or equal to row count");

        currentState = new int[height][];
        nextState = new int[height][];
        for (int i = 0; i < height; i++) {
            currentState[i] = new int[width];
            nextState[i] = new int[width];
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

    public void setCurrentCellCiv(int x, int y, int civType) {
        currentState[y][x] = civType;
    }

    public int getCurrentCellCiv(int x, int y) {
        return currentState[y][x];
    }

    public void setNextCellCiv(int x, int y, int civType) {
        nextState[y][x] = civType;
    }

    private void swapBuffers() throws InterruptedException {
        canSwap.acquire();
        int[][] swap = currentState;
        currentState = nextState;
        nextState = swap;
        canSwap.release();
    }
}
