package ua.yuriih.task4garden;


public class Garden {
    private final boolean[][] plantWilted;
    private final int width;
    private final int height;
    private final SimpleReadWriteLock lock = new SimpleReadWriteLock();
    
    public Garden(int width, int height) {
        this.width = width;
        this.height = height;
        this.plantWilted = new boolean[height][];
        for (int i = 0; i < height; i++)
            plantWilted[i] = new boolean[width];
    }
    
    public boolean isPlantFresh(int x, int y) {
        return !plantWilted[y][x];
    }

    public void setPlantFreshness(int x, int y, boolean isFresh) {
        plantWilted[y][x] = !isFresh;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
    
    public SimpleReadWriteLock getLock() {
        return lock;
    }
}
