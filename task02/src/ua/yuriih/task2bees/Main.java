package ua.yuriih.task2bees;

import java.util.Random;

public class Main {
    private static final int FOREST_WIDTH = 10;
    private static final int FOREST_HEIGHT = 5;

    public static void main(String[] args) {
        Random rng = new Random();
        int poohX = rng.nextInt(FOREST_WIDTH);
        int poohY = rng.nextInt(FOREST_HEIGHT);
        Forest forest = new Forest(FOREST_WIDTH, FOREST_HEIGHT, poohX, poohY);

        System.out.printf("[Generated forest with size (%d; %d)]\n", forest.getWidth(), forest.getHeight());
        System.out.printf("[Pooh moved to (%d; %d)]\n", poohX, poohY);

        BeesTask.Manager tasksManager = new BeesTask.Manager(forest);

        for (int i = 0; i < Runtime.getRuntime().availableProcessors(); i++) {
            new Thread(() -> {
                while (true) {
                    BeesTask task = tasksManager.getNextTask();
                    if (task == null)
                        break;
                    task.run();
                }
                System.out.printf("[Thread %d] No more tasks\n", Thread.currentThread().getId());
            }).start();
        }
    }
}
