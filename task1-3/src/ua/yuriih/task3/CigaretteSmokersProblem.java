package ua.yuriih.task3;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CigaretteSmokersProblem {
    private enum CigaretteIngredient {
        TOBACCO, PAPER, MATCHES
    }
    
    private static final Map<CigaretteIngredient, SimpleSemaphore> ingredients =
            new ConcurrentHashMap<>();
    private static final SimpleSemaphore tableReady = new SimpleSemaphore(1);
    private static final SimpleSemaphore smokingDone = new SimpleSemaphore(1);
    
    private static class Smoker implements Runnable{
        private final CigaretteIngredient myIngredient;

        public Smoker(CigaretteIngredient hasIngredient) {
            this.myIngredient = hasIngredient;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    tableReady.acquire();
                } catch (InterruptedException ignored) {}

                boolean canMakeCigarette = true;
                for (CigaretteIngredient ingredientType : CigaretteIngredient.values()) {
                    if (ingredientType == myIngredient)
                        continue;
                    if (ingredients.get(ingredientType).availablePermits() == 0) {
                        canMakeCigarette = false;
                        break;
                    }
                }

                if (!canMakeCigarette) {
                    tableReady.release();
                    continue;
                }

                for (CigaretteIngredient ingredientType : CigaretteIngredient.values()) {
                    if (ingredientType == myIngredient)
                        continue;
                    try {
                        ingredients.get(ingredientType).acquire();
                    } catch (InterruptedException ignored) {}
                }
                tableReady.release();

                System.out.printf("[Smoker with %s] Got ingredients! Starting to smoke...\n", myIngredient);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ignored) {}

                System.out.printf("[Smoker with %s] Finished smoking.\n", myIngredient);
                smokingDone.release();
            }
        }
    };
    
    private static final Runnable dealer = () -> {
        while (true) {
            try {
                smokingDone.acquire();
                tableReady.acquire();
            } catch (InterruptedException ignored) {}

            int excludedTypeNum = (int) (Math.random() * 3);

            int typeNum = -1;
            for (CigaretteIngredient ingredientType : CigaretteIngredient.values()) {
                typeNum++;
                if (excludedTypeNum == typeNum)
                    continue;
                System.out.printf("[Dealer] Released %s\n", ingredientType);
                ingredients.get(ingredientType).release();
            }
            tableReady.release();
        }
    };
    
    public static void main(String[] args) {
        ingredients.put(CigaretteIngredient.TOBACCO, new SimpleSemaphore(0));
        ingredients.put(CigaretteIngredient.PAPER, new SimpleSemaphore(0));
        ingredients.put(CigaretteIngredient.MATCHES, new SimpleSemaphore(0));

        new Thread(dealer).start();
        new Thread(new Smoker(CigaretteIngredient.TOBACCO)).start();
        new Thread(new Smoker(CigaretteIngredient.PAPER)).start();
        new Thread(new Smoker(CigaretteIngredient.MATCHES)).start();
    }
}
