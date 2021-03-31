package ua.yuriih.task5rookies;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class DumbRookies {
    private static final int ROOKIES_COUNT = 100;
    private static final int THREAD_COUNT = 2;
    
    private static boolean[] facingLeft = new boolean[ROOKIES_COUNT];
    private static boolean[] nextFacingLeft = new boolean[ROOKIES_COUNT];
    private static boolean isStatic = true;
    private static final Thread[] threads = new Thread[THREAD_COUNT];

    public static void main(String[] args) {
        for (int i = 0; i < facingLeft.length; i++)
            facingLeft[i] = Math.random() < 0.5;

        
        CyclicBarrier barrier = new CyclicBarrier(THREAD_COUNT, () -> {
            if (isStatic) {
                for (Thread thread : threads)
                    thread.interrupt();
            }
            
            for (int rookie = 0; rookie < facingLeft.length; rookie++) {
                if (rookie % (facingLeft.length / THREAD_COUNT) == 0)
                    System.out.print("|||");
                System.out.print(facingLeft[rookie] ? '<' : '>');
            }
            System.out.println();

            boolean[] swap = nextFacingLeft;
            nextFacingLeft = facingLeft;
            facingLeft = swap;

            isStatic = true;
        });
        

        for (int i = 0; i < THREAD_COUNT; i++) {
            int finalI = i;
            threads[i] = new Thread(() -> {
                while (!Thread.interrupted()) {
                    int startRookie = (facingLeft.length / THREAD_COUNT) * finalI;
                    int endRookie = Math.min(startRookie + facingLeft.length / THREAD_COUNT, facingLeft.length);
                    for (int rookie = startRookie; rookie < endRookie; rookie++) {
                        if (facingLeft[rookie]) {
                            nextFacingLeft[rookie] = rookie == 0 || facingLeft[rookie - 1];
                        } else {
                            //facing right
                            nextFacingLeft[rookie] = !(rookie == facingLeft.length - 1 || !facingLeft[rookie + 1]);
                        }

                        if (nextFacingLeft[rookie] != facingLeft[rookie])
                            isStatic = false;
                    }

                    try {
                        barrier.await();
                    } catch (InterruptedException e) {
                        return;
                    } catch (BrokenBarrierException e) {
                        e.printStackTrace();
                        return;
                    }
                }
            });

            threads[i].start();
        }
    }
}
