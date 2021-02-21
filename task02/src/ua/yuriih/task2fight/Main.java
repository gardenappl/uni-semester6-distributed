package ua.yuriih.task2fight;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ForkJoinPool;

public class Main {
    public static void main(String[] args) {
        ArrayList<Integer> fightersXi = new ArrayList<>(10);
        for (int i = 1; i < 10; i++)
            fightersXi.add(i * 1000);
        Collections.shuffle(fightersXi);

        ForkJoinPool pool = new ForkJoinPool();
        int winnerIndex = pool.invoke(new Tournament(fightersXi));

        System.out.printf("Ultimate winner is %d (Xi: %d)\n", winnerIndex, fightersXi.get(winnerIndex));
    }
}
