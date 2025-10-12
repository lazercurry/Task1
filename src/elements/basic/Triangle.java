package elements.basic;

import java.awt.*;

public class Triangle implements Drawable {
    private int[] xDots = new int[3];
    private int[] yDots = new int[3];
    private Color color;
    private int strokeWidth = 1;

    public Triangle(int x1, int y1, int x2, int y2, int x3, int y3, Color color) {
        xDots[0] = x1;
        xDots[1] = x2;
        xDots[2] = x3;
        yDots[0] = y1;
        yDots[1] = y2;
        yDots[2] = y3;
        this.color = color;
    }

    public Triangle(int x1, int y1, int x2, int y2, int x3, int y3, Color color, int strokeWidth) {
        this(x1, y1, x2, y2, x3, y3, color);
        this.strokeWidth = strokeWidth;
    }

    @Override
    public void draw(Graphics2D g2d) {
        g2d.setStroke(new BasicStroke(strokeWidth));
        g2d.setColor(color);
        g2d.drawPolygon(xDots, yDots, 3);
    }
}
