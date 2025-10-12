package elements.basic;

import java.awt.*;

public class Rectangle implements Drawable {
    private int x;
    private int y;
    private int width;
    private int height;
    private Color color;

    public Rectangle(int x, int y, int width, int height, Color color){
        this.x = x;
        this.y = y;
        if (width <= 0){
            throw new IllegalArgumentException("Incorrect width(Cannot be negative or zero)");
        }
        this.width = width;
        if (height <= 0){
            throw new IllegalArgumentException("Incorrect height(Cannot be negative or zero)");
        }
        this.height = height;
        this.color = color;
    }

    @Override
    public void draw(Graphics2D g2d){
        g2d.setColor(color);
        g2d.fillRect(x,y,width, height);
    }
}
