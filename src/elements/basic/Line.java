package elements.basic;

import java.awt.*;

public class Line implements Drawable {
    private int strokeWidth = 1;

    private class Dot{
        private int x;
        private int y;

        public Dot(int x, int y){
            this.x = x;
            this.y = y;
        }
        public int getX(){
            return x;
        }
        public int getY(){
            return y;
        }
    }

    private Dot dot1;
    private Dot dot2;
    private Color color;

    public Line(int x1, int y1, int x2, int y2, Color color){
        dot1 = new Dot(x1, y1);
        dot2 = new Dot(x2, y2);
        this.color = color;
    }

    public Line(int x1, int y1, int x2, int y2, Color color, int strokeWidth){

        this(x1, y1, x2, y2, color);
        this.strokeWidth = strokeWidth;

    }

    @Override
    public void draw(Graphics2D g2d){
        g2d.setStroke(new BasicStroke(strokeWidth));
        g2d.setColor(color);
        g2d.drawLine(dot1.getX(), dot1.getY(), dot2.getX(), dot2.getY());
    }
}
