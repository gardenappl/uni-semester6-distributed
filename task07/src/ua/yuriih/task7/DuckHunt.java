package ua.yuriih.task7;

import java.awt.*;
import java.util.ArrayList;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Phaser;
import java.util.concurrent.locks.ReentrantLock;

public class DuckHunt implements Runnable {
    public int mouseX;
    public int mouseY;
    public boolean mouseDown;

    public static final int WIDTH = 640;
    public static final int HEIGHT = 480;
    
    public final Phaser tickPhaser = new Phaser(1);
    public final ReentrantLock stateLock = new ReentrantLock(true);

    //Game state:
    public boolean shotBullet;
    public int bulletsCount = 10;
    public int ducksToSpawn = 10;

    public final ArrayList<GameEntity> entitiesToRegister = new ArrayList<>();
    public final ArrayList<GameEntity> gameEntities = new ArrayList<>();
    public final ArrayList<DrawableEntity> drawableEntities = new ArrayList<>();

    private final TextLabel bulletLabel =
            new TextLabel(Font.getFont(Font.MONOSPACED), "Bullets: " + bulletsCount, 20, 20);
    
    public boolean gameOver = false;
    
    public DuckHunt() {
    }
    
    private void registerEntity(GameEntity entity) {
        System.err.println("Registered game entity");
        gameEntities.add(entity);
        tickPhaser.register();
        new Thread(entity).start();
    }

    private void registerEntity(DrawableEntity entity) {
        drawableEntities.add(entity);
    }

    @Override
    public void run() {
        drawableEntities.add(bulletLabel);

        stateLock.lock();
        while (!Thread.interrupted()) {
            while (!entitiesToRegister.isEmpty())
                registerEntity(entitiesToRegister.remove(entitiesToRegister.size() - 1));
            
            if (ducksToSpawn > 0 && Math.random() < 0.1) {
                boolean fromTheLeft = Math.random() < 0.5;
                Duck duck = new Duck(
                        this,
                        -Duck.SIZE / 2 + (fromTheLeft ? 0 : WIDTH),
                        50 + (float)Math.random() * 100,
                        (float)Math.random() * (fromTheLeft ? 3 : -3),
                        (float)Math.random() * 0.5f
                );
                registerEntity((GameEntity)duck);
                registerEntity((DrawableEntity)duck);
                ducksToSpawn--;
            }

            //pre-tick
            if (mouseDown) {
                if (bulletsCount > 0) {
                    System.err.println("Bullet shot!");
                    shotBullet = true;
                    bulletsCount--;
                    bulletLabel.setText("Bullets: " + bulletsCount);
                }
                mouseDown = false;
            }
            
            if (bulletsCount == 0 && !gameOver) {
                registerEntity(new TextLabel(new Font(Font.MONOSPACED, Font.BOLD, 48),
                        "GAME OVER", 20, HEIGHT / 2));
                gameOver = true;
            }

            //tick

            System.err.println("Tick started");
            tickPhaser.arriveAndAwaitAdvance();
            tickPhaser.arriveAndAwaitAdvance();
            System.err.println("Tick ended");

            stateLock.unlock();
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                break;
            }
            stateLock.lock();

            //post-tick
            shotBullet = false;
            
            for (int i = 0; i < gameEntities.size();) {
                if (gameEntities.get(i).shouldDeregister()) {
                    gameEntities.remove(i);
                } else {
                    i++;
                }
            }
        }
    }
}
