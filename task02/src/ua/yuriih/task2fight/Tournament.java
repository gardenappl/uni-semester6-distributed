package ua.yuriih.task2fight;

import java.util.List;
import java.util.Random;
import java.util.concurrent.RecursiveTask;

public class Tournament extends RecursiveTask<Integer> {
    //private static final Random RNG = new Random();
    private final List<Integer> fightersXi;
    private final int startIndex;
    private final int endIndex;

    public Tournament(List<Integer> fightersXi) {
        this.fightersXi = fightersXi;
        this.startIndex = 0;
        this.endIndex = fightersXi.size() - 1;
    }

    public Tournament(List<Integer> fightersXi, int startIndex, int endIndex) {
        this.fightersXi = fightersXi;
        this.startIndex = startIndex;
        this.endIndex = endIndex;
    }

    @Override
    protected Integer compute() {
        if (endIndex == startIndex)
            return startIndex;

        if (endIndex == startIndex + 1)
            return getWinner(startIndex, endIndex);

        int midIndex = (startIndex + endIndex) / 2;

        Tournament subTour1 = new Tournament(fightersXi, startIndex, midIndex);
        Tournament subTour2 = new Tournament(fightersXi, midIndex + 1, endIndex);
        subTour2.fork();
        return getWinner(subTour1.compute(), subTour2.join());
    }

    private int getWinner(int fighterIndex1, int fighterIndex2) {
//        boolean firstWins = RNG.nextInt(fightersXi.get(fighterIndex1) + fightersXi.get(fighterIndex2))
//                < fightersXi.get(fighterIndex1);
        boolean firstWins = fightersXi.get(fighterIndex1) > fightersXi.get(fighterIndex2);


        System.out.printf("Fighter %d (Xi: %d) %s fighter %d (Xi: %d)\n",
                fighterIndex1, fightersXi.get(fighterIndex1),
                firstWins ? "defeats" : "loses to",
                fighterIndex2, fightersXi.get(fighterIndex2));
        return firstWins ? fighterIndex1 : fighterIndex2;
    }
}
