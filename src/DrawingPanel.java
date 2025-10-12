import elements.basic.Animated;
import elements.basic.Drawable;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class DrawingPanel extends JPanel {
    private final List<Drawable> figures;

    public DrawingPanel() {
        figures = new ArrayList<>();
    }

    public void addFigure(Drawable figure){
        figures.add(figure);
    }


    @Override
    public void paintComponent(final Graphics gr) {
        super.paintComponent(gr);
        var gr2d = (Graphics2D) gr;
        for (Drawable figure : figures) {
            if (figure instanceof Animated) {
                ((Animated) figure).update();
            }
            figure.draw(gr2d);
        }
    }
}
