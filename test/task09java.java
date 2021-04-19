package ua.yuriih.test1;

import java.util.ArrayList;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;


//Task 9: auction
public class Main {
    private static final int THREAD_COUNT = 3;

    private static final class DuePayment {
        public final int boughtOnTurn;
        public final int userId;

        public DuePayment(int boughtOnTurn, int userId) {
            this.boughtOnTurn = boughtOnTurn;
            this.userId = userId;
        }
    }

    private static final int BANNED = -1;


    //Stop trading for lot if everyone skipped >= 3 turns
    private static int[] skippedTurns = new int[THREAD_COUNT];
    private static int[] proposedPrices = new int[THREAD_COUNT];
    private static int[] nextPrices = new int[THREAD_COUNT];
    private static int[] banTimeout = new int[THREAD_COUNT];

    private static final int FINAL_LOT = 3;
    private static int currentLot;
    private static int currentTurn;
    private static ReentrantLock paymentsLock = new ReentrantLock();
    private static ArrayList<DuePayment> duePayments = new ArrayList<>();

    public static void main(String[] args) {

        CyclicBarrier barrier = new CyclicBarrier(THREAD_COUNT, () -> {
            int[] swapBuffers = nextPrices;
            nextPrices = proposedPrices;
            proposedPrices = swapBuffers;

            //Check due payments
            for (DuePayment duePayment : duePayments) {
                if (currentTurn - duePayment.boughtOnTurn >= 2) {
                    System.out.printf("User %d is banned!\n", duePayment.userId);
                    banTimeout[duePayment.userId] = 3;
                }
            }

            boolean productSold = true;
            for (int i = 0; i < THREAD_COUNT; i++) {
                if (skippedTurns[i] < 3) {
                    productSold = false;
                    break;
                }
            }
            if (productSold) {
                int maxPrice = 0, buyer = 0;
                for (int i = 0; i < THREAD_COUNT; i++) {
                    if (proposedPrices[i] > maxPrice) {
                        buyer = i;
                        maxPrice  = proposedPrices[i];
                    }
                }
                System.out.println("Max price: " + maxPrice);
                System.out.println("Buyer: " + buyer);
                duePayments.add(new DuePayment(currentTurn, buyer));
                currentLot++;
                for (int i = 0; i < THREAD_COUNT; i++) {
                    proposedPrices[i] = 0;
                    if (skippedTurns[i] != BANNED)
                        skippedTurns[i] = 0;
                }
                if (currentLot == 3) {
                    System.out.println("Stopping auction.");
                    return;
                }
                System.out.printf("Now selling lot %d\n", currentLot);
            }
            currentTurn++;
        });


        for (int threadNum = 0; threadNum < THREAD_COUNT; threadNum++) {
            int i = threadNum;
            Thread thread = new Thread(() -> {
                while (currentLot < FINAL_LOT) {
                    if (banTimeout[i] > 0) {
                        System.out.printf("[User %d] I'm banned!\n", i);
                        banTimeout[i]--;
                        skippedTurns[i] = BANNED;
                    } else if (Math.random() < 0.3) {
                        int maxPrice = 0;
                        for (int proposedPrice : proposedPrices) {
                            if (proposedPrice > maxPrice)
                                maxPrice = proposedPrice;
                        }
                        nextPrices[i] = maxPrice + 100 + (int) (Math.random() * 100);
                        System.out.printf("[User %d] Raising the price to %d\n", i, nextPrices[i]);
                    } else {
                        System.out.printf("[User %d] Skipping turn\n", i);
                        nextPrices[i] = proposedPrices[i];
                        skippedTurns[i]++;
                    }

                    //Check due payments
                    paymentsLock.lock();
                    for (int j = 0; j < duePayments.size();) {
                        if (duePayments.get(j).userId == i) {
                            if (Math.random() < 0.5) {
                                System.out.printf("[User %d] I'm paying.\n", i);
                                duePayments.remove(j);
                            } else {
                                System.out.printf("[User %d] I'll pay later.\n", i);
                                j++;
                            }
                        } else {
                            j++;
                        }
                    }
                    paymentsLock.unlock();

                    try {
                        barrier.await();
                    } catch (InterruptedException | BrokenBarrierException e) {
                        e.printStackTrace();
                    }
                }
            });
            thread.start();
        }
    }
}
