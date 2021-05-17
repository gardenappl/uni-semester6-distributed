package ua.yuriih.task7b;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Main {
    public static void main(String[] args) {
        DuckHunt game = new DuckHunt();
        GamePanel panel = new GamePanel(game);

        JFrame frame = new JFrame();

        frame.add(panel);
        frame.setTitle("Duck Hunt 2");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        frame.addKeyListener(new GameKeyAdapter(game));
        frame.setSize(DuckHunt.WIDTH, DuckHunt.HEIGHT);
        frame.setVisible(true);

        Thread gameThread = new Thread(game);
        gameThread.setDaemon(true);
        gameThread.start();
    }
}
