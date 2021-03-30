package ua.yuriih.task5rookies;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class DumbRecruits {
    private static final int RECRUITS_COUNT = 100;
    private static final int THREAD_COUNT = 2;
    
    private static boolean[] facingLeft = new boolean[RECRUITS_COUNT];
    private static boolean[] nextFacingLeft = new boolean[RECRUITS_COUNT];
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
            
            for (boolean rookieFacesLeft : facingLeft) {
                System.out.print(rookieFacesLeft ? '<' : '>');
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
                    for (int rookie = startRookie; rookie < facingLeft.length; rookie++) {
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
