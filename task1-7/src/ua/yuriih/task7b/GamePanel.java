package ua.yuriih.task7b;

import ua.yuriih.task7b.entities.DrawableEntity;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class GamePanel extends JPanel {
    private DuckHunt game;
    
    public GamePanel(DuckHunt game) {
        this.game = game;
        
        this.setSize(DuckHunt.WIDTH, DuckHunt.HEIGHT);

        setDoubleBuffered(true);
    }

    @Override
    public void paint(Graphics g) {
        game.stateLock.lock();
//        System.err.println("Drawing...");
        if (game.gameOver && game.won)
            g.setColor(new Color(170, 220, 170));
        else if (game.gameOver)
            g.setColor(new Color(220, 170, 170));
        else
        g.setColor(new Color(170, 220, 220));
        g.fillRect(0, 0, DuckHunt.WIDTH, DuckHunt.HEIGHT);

        for (DrawableEntity entity : game.drawableEntities) {
            entity.draw(g);
        }
        game.stateLock.unlock();

        Toolkit.getDefaultToolkit().sync();
        repaint();
    }
}
