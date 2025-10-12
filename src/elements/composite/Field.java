package elements.composite;

import elements.basic.Circle;
import elements.basic.Line;
import elements.basic.Rectangle;

import java.awt.*;

public class Field extends CompositeElement {

    private static final Color FIELD_COLOR = new Color(181, 228, 31);
    private static final Color LINE_COLOR = Color.WHITE;
    private static final int LINE_THICKNESS = 6;
    private static final double LINE_VERTICAL_POSITION_FACTOR = 1.0 / 3.0;
    private static final int FIELD_X_START = 0;
    private static final int FIELD_Y_START = 0;

    public Field(int x, int y) {
        this.x = x;
        this.y = y;
        initialize();
    }

    @Override
    public void initialize() {
        figures.add(
                new Rectangle(FIELD_X_START, FIELD_Y_START, x, y, FIELD_COLOR)
        );

        int lineY = (int)(y * LINE_VERTICAL_POSITION_FACTOR);
        figures.add(
                new Line(FIELD_X_START, lineY, x, lineY, LINE_COLOR, LINE_THICKNESS)
        );
    }
}