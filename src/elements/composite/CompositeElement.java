package elements.composite;

import elements.basic.Drawable;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

abstract public class CompositeElement implements Drawable, Initializable {
    protected List<Drawable> figures = new ArrayList<>();
    protected int x;
    protected int y;

    @Override
    public void draw(Graphics2D g2d){
        for(Drawable figure : figures){
            figure.draw(g2d);
        }
    }
}
