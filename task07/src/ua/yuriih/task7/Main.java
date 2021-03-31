package ua.yuriih.task7;

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

        Thread thread = new Thread(game);
        thread.setDaemon(true);
        thread.start();
    }
}
