package ua.yuriih.task4garden;


import java.io.IOException;
import java.io.PrintStream;

public class GardenSimulation {
    public static void main(String[] args) {
        Garden garden = new Garden(10, 10);

        new Thread(new RunnableGardener(garden)).start();
        new Thread(new RunnableNature(garden)).start();
        new Thread(new RunnablePrinter(garden, "ConsolePrinter", System.out)).start();

        try (PrintStream stream = new PrintStream("output.txt")) {
            new Thread(new RunnablePrinter(garden, "FilePrinter", stream)).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
