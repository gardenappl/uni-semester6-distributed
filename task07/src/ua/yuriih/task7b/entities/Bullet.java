package ua.yuriih.task7b.entities;

import ua.yuriih.task7b.DuckHunt;

import java.awt.*;

public class Bullet extends GameEntity implements DrawableEntity {
    private float x;
    private float y;
    private float velocityY;

    public static final float SIZE = 4;

    public Bullet(DuckHunt game, float x, float y, float velocityY) {
        super(game);

        this.x = x;
        this.y = y;
        this.velocityY = velocityY;
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect((int)x, (int)y, (int)SIZE, (int)SIZE);
    }

    @Override
    public void update() {
        this.y += velocityY;
    }

    @Override
    public boolean shouldDeregister() {
        return this.y < -SIZE;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }
}
