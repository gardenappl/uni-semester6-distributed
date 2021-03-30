package ua.yuriih.task6a.game;

import javax.swing.*;
import java.awt.*;

public class GameOfLifeFrame extends JFrame {
    private static final Color aliveColor = Color.BLACK;
    private static final Color deadColor = Color.WHITE;
    private static final int SCALE = 10;

    private final GameOfLife game;

    public GameOfLifeFrame(GameOfLife game) {
        setSize(game.width * SCALE, game.height * SCALE);

        this.game = game;
    }

    @Override
    public void paint(Graphics g) {
        try {
            game.canSwap.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return;
        }
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
        game.canSwap.release();
    }
}
