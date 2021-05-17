package ua.yuriih.task6a;

import ua.yuriih.task6a.game.GameOfLife;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        GameOfLife game = new GameOfLife(50, 50, 11);

        //Glider
        game.setCellCurrentlyAlive(1, 5, true);
        game.setCellCurrentlyAlive(2, 5, true);
        game.setCellCurrentlyAlive(3, 5, true);
        game.setCellCurrentlyAlive(3, 4, true);
        game.setCellCurrentlyAlive(2, 3, true);

        game.start();

        JFrame frame = game.frame;

        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setTitle("Conway's Game of Life");
        frame.setVisible(true);
    }
}
