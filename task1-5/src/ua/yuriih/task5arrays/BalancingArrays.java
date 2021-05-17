package ua.yuriih.task5arrays;

public class BalancingArrays {
    private static final int THREAD_COUNT = 3;
    private static final int ARRAY_LENGTH = 5;
    private static final int MAX_ELEMENT = 10;
    
    private static final int[][] threadArrays = new int[THREAD_COUNT][];
    private static long[] nextThreadArraySums = new long[THREAD_COUNT];
    private static long[] threadArraySums = new long[THREAD_COUNT];
    
    public static void main(String[] args) {
        CustomCyclicBarrier barrier = new CustomCyclicBarrier(THREAD_COUNT, () -> {
            long[] swap = nextThreadArraySums;
            nextThreadArraySums = threadArraySums;
            threadArraySums = swap;
            
            for (int i = 0; i < THREAD_COUNT; i++) {
                System.out.printf("Thread %d: ", i);
                for (int element : threadArrays[i])
                    System.out.printf("%d, ", element);
                System.out.printf("sum = %d; ", threadArraySums[i]);
            }
            System.out.println();
        });


        for (int threadNum = 0; threadNum < THREAD_COUNT; threadNum++) {
            int i = threadNum;
            threadArrays[i] = new int[ARRAY_LENGTH];
            for (int j = 0; j < ARRAY_LENGTH; j++)
                threadArrays[i][j] = (int)(Math.random() * MAX_ELEMENT);


            new Thread(() -> {
                while (!Thread.interrupted()) {
                    nextThreadArraySums[i] = 0;
                    for (int element : threadArrays[i])
                        nextThreadArraySums[i] += element;

                    try {
//                        Thread.sleep(1000);
                        barrier.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        return;
                    }
                    
                    boolean equal = true;
                    boolean isBiggest = true;
                    boolean isSmallest = true;
                    for (int j = 1; j < THREAD_COUNT; j++) {
                        if (threadArraySums[i] != threadArraySums[(i + j) % THREAD_COUNT]) {
                            equal = false;
                            if (threadArraySums[i] < threadArraySums[(i + j) % THREAD_COUNT])
                                isBiggest = false;
                            if (threadArraySums[i] > threadArraySums[(i + j) % THREAD_COUNT])
                                isSmallest = false;
                        }
                    }
                    if (equal)
                        return;

                    int randomIndex = (int) (Math.random() * ARRAY_LENGTH);
                    if (isBiggest || (!isSmallest && Math.random() < 0.5)) {
                        threadArrays[i][randomIndex]--;
                    } else {
                        threadArrays[i][randomIndex]++;
                    }
                }
            }).start();
        }
    }
}
