package ua.yuriih.task4garden;

import java.io.PrintStream;

public class RunnablePrinter implements Runnable {
    private final Garden garden;
    private final PrintStream stream;
    private final String name;

    public RunnablePrinter(Garden garden, String name, PrintStream stream) {
        this.garden = garden;
        this.stream = stream;
        this.name = name;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(1000);
                System.err.printf("[%s] Woke up, getting read lock...\n", name);
                garden.getLock().lockReading();
            } catch (InterruptedException ignored) {}

            System.err.printf("[%s] Gathering garden information...\n", name);
            try {
                Thread.sleep(500);
            } catch (InterruptedException ignored) {}

            stream.println("Garden:");
            for (int y = 0; y < garden.getHeight(); y++) {
                for (int x = 0; x < garden.getWidth(); x++) {
                    if (garden.isPlantFresh(x, y))
                        stream.print('✅');
                    else
                        stream.print("\uD83C\uDD71️");
                }
                stream.println();
            }
            stream.flush();
            System.err.printf("[%s] Finished printing...\n", name);
            garden.getLock().unlockReading();
        }
    }
}
