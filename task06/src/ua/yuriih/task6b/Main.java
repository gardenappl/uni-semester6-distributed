package ua.yuriih.task6b;

import ua.yuriih.task6b.game.GameOfLife;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        GameOfLife game = new GameOfLife(50, 50, 11, Arrays.asList(
                Color.RED,
                Color.BLUE,
                Color.GREEN
        ));

        //Glider 1
        game.setCurrentCellCiv(1, 3, 1);
        game.setCurrentCellCiv(2, 3, 1);
        game.setCurrentCellCiv(3, 3, 1);
        game.setCurrentCellCiv(3, 2, 1);
        game.setCurrentCellCiv(2, 1, 1);

        //Glider 2
        game.setCurrentCellCiv(10, 3, 2);
        game.setCurrentCellCiv(9, 3, 2);
        game.setCurrentCellCiv(8, 3, 2);
        game.setCurrentCellCiv(8, 2, 2);
        game.setCurrentCellCiv(9, 1, 2);


        //Blinker
        game.setCurrentCellCiv(5, 10, 1);
        game.setCurrentCellCiv(5, 11, 2);
        game.setCurrentCellCiv(5, 12, 3);

        game.start();

        JFrame frame = game.frame;

        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setTitle("Conway's Game of Life");
        frame.setVisible(true);


        Thread.sleep(1000);
    }
}
