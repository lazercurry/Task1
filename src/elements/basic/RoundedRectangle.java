package elements.basic;

import java.awt.*;

public class RoundedRectangle implements Drawable {
    private int x;
    private int y;
    private int width;
    private int height;
    private Color color;
    private boolean fill = true;
    private int strokeWidth = 1;

    public RoundedRectangle(int x, int y, int width, int height, Color color) {
        this.x = x;
        this.y = y;
        if (width <= 0) {
            throw new IllegalArgumentException("Incorrect width(Cannot be negative or zero)");
        }
        this.width = width;
        if (height <= 0) {
            throw new IllegalArgumentException("Incorrect height(Cannot be negative or zero)");
        }
        this.height = height;
        this.color = color;
    }

    public RoundedRectangle(int x, int y, int width, int height, Color color, boolean fill, int strokeWidth) {
        this(x, y, width, height, color);
        this.fill = fill;
        this.strokeWidth = strokeWidth;
    }

    @Override
    public void draw(Graphics2D g2d) {
        g2d.setStroke(new BasicStroke(strokeWidth));
        g2d.setColor(color);
        if (fill)
            g2d.fillRoundRect(x, y, width, height, width / 4, height / 4);
        else
            g2d.drawRoundRect(x, y, width, height, width / 4, height / 4);
    }
}
