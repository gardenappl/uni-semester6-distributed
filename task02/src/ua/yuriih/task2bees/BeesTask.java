package ua.yuriih.task2bees;

public class BeesTask implements Runnable {
    public static class Manager {
        private int currentX = 0;
        private int currentY = 0;
        private boolean foundPooh = false;
        private final Forest forest;

        public Manager(Forest forest) {
            this.forest = forest;
        }

        /**
         * @return null if all tasks are complete
         */
        public synchronized BeesTask getNextTask() {
            if (foundPooh || currentY == forest.getHeight())
                return null;

            System.out.printf("Next task: search (%d; %d)\n", currentX, currentY);

            BeesTask task = new BeesTask(forest, currentX, currentY, this);
            currentX++;
            if (currentX == forest.getWidth()) {
                currentX = 0;
                currentY++;
            }
            return task;
        }

        public synchronized void setFoundPooh() {
            foundPooh = true;
        }
    }

    private final int x;
    private final int y;
    private final Forest forest;
    private final Manager manager;

    private BeesTask(Forest forest, int x, int y, Manager manager) {
        this.forest = forest;
        this.x = x;
        this.y = y;
        this.manager = manager;
    }

    @Override
    public void run() {
        boolean foundPooh = forest.checkPooh(x, y);
        if (foundPooh) {
            System.out.println(getMessage("Target has been located, performed punishment"));
            manager.setFoundPooh();
        } else {
            System.out.println(getMessage("Target not found"));
        }
        System.out.println(getMessage("Returning to base"));
    }

    private String getMessage(String message) {
        return String.format("[Thread %d, (%d; %d)] %s", Thread.currentThread().getId(), x, y, message);
    }
}
