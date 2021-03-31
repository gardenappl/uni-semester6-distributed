package ua.yuriih.task7b;


import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class GameKeyAdapter extends KeyAdapter {
    private final DuckHunt game;

    public GameKeyAdapter(DuckHunt game) {
        this.game = game;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT -> game.keyLeft = true;
            case KeyEvent.VK_RIGHT -> game.keyRight = true;
            case KeyEvent.VK_SPACE -> game.keyShoot = true;
        }
    }
    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT -> game.keyLeft = false;
            case KeyEvent.VK_RIGHT -> game.keyRight = false;
            case KeyEvent.VK_SPACE -> game.keyShoot = false;
        }
    }
}
