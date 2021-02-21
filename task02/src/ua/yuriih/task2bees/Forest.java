package ua.yuriih.task2bees;

public class Forest {
    private static final int MIN_DELAY = 0;
    private static final int MAX_DELAY_RANDOM = 0;

    private final int width;
    private final int height;
    private final int poohX;
    private final int poohY;

    public Forest(int width, int height, int poohX, int poohY) {
        this.width = width;
        this.height = height;
        this.poohX = poohX;
        this.poohY = poohY;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public boolean checkPooh(int x, int y) {
        try {
            Thread.sleep(MIN_DELAY + (long) (Math.random() * MAX_DELAY_RANDOM));
        } catch (InterruptedException ignored) {
        }
        return x == poohX && y == poohY;
    }
}
