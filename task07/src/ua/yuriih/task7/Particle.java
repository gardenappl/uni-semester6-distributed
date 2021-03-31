package ua.yuriih.task7;

import java.awt.*;

public class Particle extends GameEntity implements DrawableEntity {
    private float x;
    private float y;
    private float velocityX;
    private float velocityY;
    
    private static final int SIZE = 4;
    private static final Color COLOR = new Color(210, 0, 0);
    
    public Particle(DuckHunt game, float x, float y, float velocityX, float velocityY) {
        super(game);
        this.x = x;
        this.y = y;
        this.velocityX = velocityX;
        this.velocityY = velocityY;
    }
    
    @Override
    public void draw(Graphics g) {
        g.setColor(COLOR);
        g.fillRect((int)x, (int)y, SIZE, SIZE);
    }

    @Override
    public void update() {
        this.velocityY += 0.2;
        
        this.x += this.velocityX;
        this.y += this.velocityY;
    }

    @Override
    public boolean shouldDeregister() {
        return x <= -SIZE || x > DuckHunt.WIDTH - SIZE || y <= -SIZE || y > DuckHunt.HEIGHT - SIZE;
    }
}
