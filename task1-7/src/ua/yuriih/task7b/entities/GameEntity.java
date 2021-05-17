package ua.yuriih.task7b.entities;

import ua.yuriih.task7b.DuckHunt;

public abstract class GameEntity implements Runnable {
    protected final DuckHunt game;
    
    public GameEntity(DuckHunt game) {
        this.game = game;
    }
    
    public abstract void update();
    
    public abstract boolean shouldDeregister();
    
    @Override
    public final void run() {
        while (!Thread.interrupted()) {
            game.tickPhaser.arriveAndAwaitAdvance();
            update();
            if (shouldDeregister()) {
                System.err.println("Deregistered entity");
                game.tickPhaser.arriveAndDeregister();
                break;
            } else {
                game.tickPhaser.arriveAndAwaitAdvance();
            }
        }
    }
}
