package ua.yuriih.task6a;

import ua.yuriih.task6a.game.GameOfLife;

import javax.swing.*;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        GameOfLife game = new GameOfLife(50, 50, 11);

        //Glider
        game.setCellCurrentlyAlive(1, 3, true);
        game.setCellCurrentlyAlive(2, 3, true);
        game.setCellCurrentlyAlive(3, 3, true);
        game.setCellCurrentlyAlive(3, 2, true);
        game.setCellCurrentlyAlive(2, 1, true);

        System.err.println(game.isCellCurrentlyAlive(5, 4));

        game.start();

        JFrame frame = game.frame;

        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setTitle("Conway's Game of Life");
        frame.setVisible(true);


        Thread.sleep(1000);
        System.err.println(game.isCellCurrentlyAlive(5, 5));
    }
}
