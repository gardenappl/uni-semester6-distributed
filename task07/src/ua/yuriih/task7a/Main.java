package ua.yuriih.task7a;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        DuckHunt game = new DuckHunt();
        GamePanel panel = new GamePanel(game);

        JFrame frame = new JFrame();

        frame.add(panel);
        frame.setTitle("Duck Hunt");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        frame.setSize(DuckHunt.WIDTH, DuckHunt.HEIGHT);
        frame.setVisible(true);

        Thread gameThread = new Thread(game);
        gameThread.setDaemon(true);
        gameThread.start();
    }
}
