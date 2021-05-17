package ua.yuriih.task3;

import java.util.concurrent.Semaphore;

public class SleepingBarberProblem {
    //binary semaphores
    //set to fair = true which ensures FIFO
    private static final Semaphore barberReady = new Semaphore(0, true);
    private static final Semaphore customersReady = new Semaphore(0);

    private static final Semaphore haircutReady = new Semaphore(0);
    private static final Semaphore haircutDone = new Semaphore(0);
    
    private static volatile Customer currentCustomer = null;
    
    private static class Customer implements Runnable {
        private final int id;
        private final int arrivalTime;
        
        public Customer(int id, int arrivalTimeMillis) {
            this.id = id;
            this.arrivalTime = arrivalTimeMillis;
        }

        public int getId() {
            return id;
        }

        @Override
        public void run() {
            try {
                Thread.sleep(arrivalTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            customersReady.release();
            
            System.out.printf("[Customer %d] Arrived at shop.\n", getId());

            try {
                barberReady.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            currentCustomer = this;
            haircutReady.release();
            System.out.printf("[Customer %d] Starting to get haircut...\n", getId());

            try {
                haircutDone.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.printf("[Customer %d] Got haircut, leaving.\n", getId());
        }
    }
    
    private static final Runnable barber = () -> {
        while (true) {
            try {
                customersReady.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            barberReady.release();
            try {
                haircutReady.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.printf("[Barber] Serving customer %d...\n", currentCustomer.getId());

            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.printf("[Barber] Haircut complete for customer %d.\n", currentCustomer.getId());
            currentCustomer = null;
            haircutDone.release();
        }
    };

    public static void main(String[] args) {
        new Thread(barber).start();
        for (int i = 0; i < 10; i++) {
            new Thread(new Customer(i, i * 100)).start();
        }
        for (int i = 0; i < 10; i++) {
            new Thread(new Customer(10 + i, 1000 + i * 500)).start();
        }
    }
}
