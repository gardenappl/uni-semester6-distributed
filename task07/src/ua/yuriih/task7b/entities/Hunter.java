package ua.yuriih.task7b.entities;

import ua.yuriih.task7b.DuckHunt;

import java.awt.*;

public class Hunter extends GameEntity implements DrawableEntity {
    private float x;
    private float y;

    private boolean lastShot = false;
    
    private static final int RADIUS = 25;
    private static final Color COLOR_1 = new Color(200, 190, 130);
    private static final Color COLOR_2 = Color.BLACK;

    public Hunter(DuckHunt game, float x, float y) {
        super(game);

        this.x = x;
        this.y = y;
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(COLOR_1);
        g.fillRect((int)x - RADIUS, (int)y - RADIUS, RADIUS * 2, RADIUS);
        g.setColor(COLOR_2);
        g.fillRect((int)x - RADIUS, (int)y, RADIUS * 2, RADIUS);
    }

    @Override
    public void update() {
        if (game.keyLeft && !game.keyRight)
            this.x -= 3;
        else if (game.keyRight && !game.keyLeft)
            this.x += 3;

        if (game.bulletsCount > 0) {
            if (game.keyShoot && !lastShot) {
                lastShot = true;

                Bullet bullet = new Bullet(game, x, y, -15f);
                game.entitiesToRegister.add(bullet);
                game.drawableEntities.add(bullet);

                game.spendBullet();
            }
        }

        if (!game.keyShoot)
            lastShot = false;
    }

    @Override
    public boolean shouldDeregister() {
        return false;
    }
}
