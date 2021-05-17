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
        game.setCurrentCellCiv(1, 25, 1);
        game.setCurrentCellCiv(2, 25, 1);
        game.setCurrentCellCiv(3, 25, 1);
        game.setCurrentCellCiv(3, 26, 1);
        game.setCurrentCellCiv(2, 27, 1);

        //Glider 2
        game.setCurrentCellCiv(10, 25, 2);
        game.setCurrentCellCiv(9, 25, 2);
        game.setCurrentCellCiv(8, 25, 2);
        game.setCurrentCellCiv(8, 26, 2);
        game.setCurrentCellCiv(9, 27, 2);


        //Blinker
        game.setCurrentCellCiv(45, 16, 3);
        game.setCurrentCellCiv(45, 17, 3);
        game.setCurrentCellCiv(45, 18, 3);

        game.start();

        JFrame frame = game.frame;

        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setTitle("Conway's Game of Life");
        frame.setVisible(true);


        Thread.sleep(1000);
    }
}
