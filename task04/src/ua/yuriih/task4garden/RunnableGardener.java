package ua.yuriih.task4garden;

public class RunnableGardener implements Runnable {
    private final Garden garden;
    
    public RunnableGardener(Garden garden) {
        this.garden = garden;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(1000);
                System.err.println("[Gardener] Woke up, getting write lock...");
                garden.getLock().lockWriting();
            } catch (InterruptedException ignored) {}

            System.err.println("[Gardener] Maintaining freshness...");

            for (int x = 0; x < garden.getWidth(); x++) {
                for (int y = 0; y < garden.getHeight(); y++) {

                    if (!garden.isPlantFresh(x, y)) {
                        System.err.printf("[Gardener] Watering plant at (%d; %d)...\n", x, y);
                        garden.setPlantFreshness(x, y, true);
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException ignored) {}
                        System.err.printf("[Gardener] Watered plant at (%d; %d).\n", x, y);
                    }

                }
            }

            System.err.println("[Gardener] Enough work for now.");
            garden.getLock().unlockWriting();
        }
    }
}
