package ua.yuriih.task7;

import java.awt.*;

public class Duck extends GameEntity implements DrawableEntity {
    private float x;
    private float y;
    private float velocityX;
    private float velocityY;
    private boolean isDead;
    
    public static final float SIZE = 20;
    private static final Color[] COLORS = new Color[] {
            new Color(40, 150, 80),
            new Color(100, 150, 40),
            new Color(40, 140, 100),
    };
    private final int color;
    
    public Duck(DuckHunt game, float x, float y, float velocityX, float velocityY) {
        super(game);
        this.x = x;
        this.y = y;
        this.velocityX = velocityX;
        this.velocityY = velocityY;
        this.color = (int)(Math.random() * COLORS.length);
    }

    @Override
    public void draw(Graphics g) {
        
        g.setColor(isDead ? Color.DARK_GRAY : COLORS[color]);
        g.fillRect((int)x, (int)y, (int)SIZE, (int)SIZE);
    }

    @Override
    public boolean shouldDeregister() {
        return x <= -SIZE || x > DuckHunt.WIDTH || y <= -SIZE || y > DuckHunt.HEIGHT;
    }

    @Override
    public void update() {
        System.err.println("Duck update!");
        if (game.shotBullet && !isDead) {
            if (game.mouseX >= x && game.mouseX < x + SIZE &&
                    game.mouseY >= y && game.mouseY < y + SIZE) {
                for (int i = 0; i < 5; i++) {
                    Particle p = new Particle(
                            game,
                            x + SIZE / 2,
                            y + SIZE / 2,
                            (float) (Math.random() - 0.5) * 5,
                            (float) (Math.random() - 0.5) * 5
                    );
                    game.entitiesToRegister.add(p);
                    game.drawableEntities.add(p);
                }

                isDead = true;
                velocityX = 0;
                velocityY = 5;
            }
        }

        this.x += velocityX;
        this.y += velocityY;
    }
}
