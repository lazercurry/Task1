package elements.basic;

import java.awt.*;

public class Circle implements Drawable {
    private int strokeWidth = 1;
    private int x;
    private int y;
    private int radius;
    private Color color;

    public Circle(int x, int y, int radius, Color color) {
        this.x = x;
        this.y = y;
        if (radius <= 0) {
            throw new IllegalArgumentException("Incorrect radius!");
        }
        this.radius = radius;
        this.color = color;
    }

    public Circle(int x, int y, int radius, Color color, int strokeWidth) {
        this(x, y, radius, color);
        this.strokeWidth = strokeWidth;
    }


    @Override
    public void draw(Graphics2D g2d) {
        g2d.setStroke(new BasicStroke(strokeWidth));
        g2d.setColor(color);
        g2d.fillOval(x - radius, y - radius, radius * 2, radius * 2);
    }
}
