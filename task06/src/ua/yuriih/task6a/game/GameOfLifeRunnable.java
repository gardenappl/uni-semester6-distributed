package ua.yuriih.task6a.game;

import java.util.concurrent.BrokenBarrierException;

public class GameOfLifeRunnable implements Runnable {
    private final GameOfLife game;
    private final int startRow;
    private final int endRow;

    public GameOfLifeRunnable(GameOfLife game, int startRow, int endRow) {
        this.game = game;

        this.startRow = startRow;
        this.endRow = endRow;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            game.currentStateLock.readLock().lock();
            System.err.printf("Updating state [%d; %d]\n", startRow, endRow - 1);
            for (int y = startRow; y < endRow; y++) {
                for (int x = 0; x < game.width; x++) {
                    int aliveNeighbours = countAliveNeighbours(x, y);

                    if (game.isCellCurrentlyAlive(x, y)) {
                        game.setNextCellAlive(x, y, aliveNeighbours == 2 || aliveNeighbours == 3);
                    } else {
                        game.setNextCellAlive(x, y, aliveNeighbours == 3);
                    }
                }
            }
            game.currentStateLock.readLock().unlock();
            System.err.printf("Updated state [%d; %d]\n", startRow, endRow - 1);
            try {
                game.drawFrameBarrier.await();
            } catch (InterruptedException e) {
                return;
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
                return;
            }
        }
    }

    private int countAliveNeighbours(int cellX, int cellY) {
        int aliveNeighbours = 0;
        for (int x = Math.max(0, cellX - 1); x <= Math.min(game.width - 1, cellX + 1); x++) {
            for (int y = Math.max(0, cellY - 1); y <= Math.min(game.height - 1, cellY + 1); y++) {
                if (x == cellX && y == cellY)
                    continue;
                if (game.isCellCurrentlyAlive(x, y))
                    aliveNeighbours++;
            }
        }
        return aliveNeighbours;
    }
}
