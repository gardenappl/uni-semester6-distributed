package ua.yuriih.task6b.game;

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
            System.err.printf("Updating state [%d; %d]\n", startRow, endRow - 1);
            for (int y = startRow; y < endRow; y++) {
                for (int x = 0; x < game.width; x++) {

                    int currentCiv = game.getCurrentCellCiv(x, y);

                    if (currentCiv != GameOfLife.DEAD_CIV) {
                        int aliveNeighbours = countAliveNeighbours(x, y);
                        if (aliveNeighbours == 2 || aliveNeighbours == 3)
                            game.setNextCellCiv(x, y, currentCiv);
                        else {
                            System.err.printf("Killing (%d; %d), was %d\n", x, y, currentCiv);
                            game.setNextCellCiv(x, y, GameOfLife.DEAD_CIV);
                        }
                    } else {
                        int[] neighbourCivs = getAllNeighbours(x, y);

                        int max = 0, maxIndex = 0, sum = 0;
                        for (int i = 1; i < neighbourCivs.length; i++) {
                            if (neighbourCivs[i] > max) {
                                maxIndex = i;
                                max = neighbourCivs[i];
                            }
                            sum += neighbourCivs[i];
                        }

                        if (sum == 3)
                            game.setNextCellCiv(x, y, maxIndex);
                        else
                            game.setNextCellCiv(x, y, GameOfLife.DEAD_CIV);
                    }
                }
            }
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

    private int[] getAllNeighbours(int cellX, int cellY) {
        int[] neighbourCivs = new int[game.civColors.size() + 1];
        
        for (int x = Math.max(0, cellX - 1); x <= Math.min(game.width - 1, cellX + 1); x++) {
            for (int y = Math.max(0, cellY - 1); y <= Math.min(game.height - 1, cellY + 1); y++) {
                if (x == cellX && y == cellY)
                    continue;
                int civ = game.getCurrentCellCiv(x, y);
                if (civ != GameOfLife.DEAD_CIV)
                    neighbourCivs[civ]++;
            }
        }
        return neighbourCivs;
    }

    private int countAliveNeighbours(int cellX, int cellY) {
        int aliveNeighbours = 0;
        for (int x = Math.max(0, cellX - 1); x <= Math.min(game.width - 1, cellX + 1); x++) {
            for (int y = Math.max(0, cellY - 1); y <= Math.min(game.height - 1, cellY + 1); y++) {
                if (x == cellX && y == cellY)
                    continue;
                if (game.getCurrentCellCiv(x, y) != GameOfLife.DEAD_CIV)
                    aliveNeighbours++;
            }
        }
        return aliveNeighbours;
    }
}
