package ua.yuriih.task7a;

import java.awt.*;

public class TextLabel implements DrawableEntity {
    private String text;
    private Font font;
    public final int x;
    public final int y;

    public TextLabel(Font font, String text, int x, int y) {
        this.text = text;
        this.font = font;
        this.x = x;
        this.y = y;
    }
    
    @Override
    public void draw(Graphics g) {
        g.setColor(Color.BLACK);
        if (g instanceof Graphics2D) {
            ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
        }
        g.setFont(font);
        g.drawString(text, x, y);
    }
    
    public void setText(String text) {
        this.text = text;
    }
}
