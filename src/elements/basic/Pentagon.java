package elements.basic;

import java.awt.*;

public class Pentagon implements Drawable {
    private int[] xDots = new int[5];
    private int[] yDots = new int[5];
    private Color color;

    public Pentagon(int centerX, int centerY, int size, Color color) {
        int radius = size / 2;

        for (int i = 0; i < 5; i++) {
            double angle = 2 * Math.PI * i / 5 - Math.PI / 2;
            xDots[i] = centerX + (int)(radius * Math.cos(angle));
            yDots[i] = centerY + (int)(radius * Math.sin(angle));
        }
        this.color = color;
    }

    @Override
    public void draw(Graphics2D g2d) {
        g2d.setColor(color);
        g2d.fillPolygon(xDots, yDots, 5);
    }
}