package ua.yuriih.task6a.game;

import javax.swing.*;
import java.awt.*;

public class GameOfLifeFrame extends JFrame {
    private static final Color aliveColor = Color.BLACK;
    private static final Color deadColor = Color.WHITE;
    private static final int SCALE = 10;

    private final GameOfLife game;
    private final boolean[][] graphicsBuffer;

    public GameOfLifeFrame(GameOfLife game) {
        setSize(game.width * SCALE, game.height * SCALE);

        this.game = game;
        this.graphicsBuffer = new boolean[game.height][];
        for (int y = 0; y < game.height; y++) {
            this.graphicsBuffer[y] = new boolean[game.width];
        }
    }

    void updateCurrentBuffer() {
        System.err.println("Setting graphics buffer...");
        game.currentStateLock.readLock().lock();

        synchronized (game) {
            for (int x = 0; x < game.width; x++) {
                for (int y = 0; y < game.height; y++) {
                    graphicsBuffer[y][x] = game.isCellCurrentlyAlive(x, y);
                }
            }
        }
        
        game.currentStateLock.readLock().unlock();
        System.err.println("Set graphics buffer.");
    }

    @Override
    public void paint(Graphics g) {
        System.err.println("Painting...");

        g.setColor(deadColor);
        g.fillRect(0, 0, game.width * SCALE, game.height * SCALE);

        for (int y = 0; y < game.height; y++) {
            for (int x = 0; x < game.width; x++) {
                boolean isAlive = game.isCellCurrentlyAlive(x, y);
                if (isAlive) {
                    g.setColor(aliveColor);
                    g.fillRect(x * SCALE, y * SCALE, SCALE, SCALE);
                }
            }
        }

        System.err.println("Done painting.");
    }
}
