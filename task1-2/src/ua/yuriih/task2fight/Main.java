package ua.yuriih.task2fight;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ForkJoinPool;

public class Main {
    private static final int FIGHTERS_COUNT = 50;

    public static void main(String[] args) {
        ArrayList<Integer> fightersXi = new ArrayList<>(FIGHTERS_COUNT);
        for (int i = 1; i < FIGHTERS_COUNT; i++)
            fightersXi.add(i * 1000);
        Collections.shuffle(fightersXi);

        int winnerIndex = ForkJoinPool.commonPool().invoke(new Tournament(fightersXi));

        System.out.printf("Ultimate winner is %d (Xi: %d)\n", winnerIndex, fightersXi.get(winnerIndex));
    }
}
