package elements.composite;

import elements.basic.Line;
import elements.basic.Triangle;

import java.awt.*;

public class Gates extends CompositeElement {
    private static final Color DEFAULT_GATES_COLOR = Color.BLACK;
    private static final int DEFAULT_SCALE = 4;
    private static final int DEFAULT_LINE_THICKNESS = 6;
    private static final int DEFAULT_WIDTH = 100;
    private static final int DEFAULT_HEIGHT = 200;
    private static final int DEFAULT_LINES_COUNT = 4;

    private final Color gatesColor;
    private final int lineThickness;
    private final int widthGates;
    private final int heightGates;
    private final int linesCount;

    public Gates(int x, int y) {
        this(x, y, DEFAULT_GATES_COLOR, DEFAULT_SCALE, DEFAULT_LINE_THICKNESS, DEFAULT_WIDTH, DEFAULT_HEIGHT, DEFAULT_LINES_COUNT);
    }

    public Gates(int x, int y, Color gatesColor, int scale, int lineThickness, int baseWidth, int baseHeight, int linesCount) {
        this.x = x;
        this.y = y;
        this.gatesColor = gatesColor;
        this.lineThickness = lineThickness;
        this.widthGates = baseWidth * scale;
        this.heightGates = baseHeight * scale;
        this.linesCount = linesCount;
        initialize();
    }

    @Override
    public void initialize() {
        figures.clear();
        figures.add(new Triangle(
                x, y,
                x + widthGates, y,
                x, y - heightGates,
                gatesColor,
                lineThickness
        ));

        for (int i = 1; i <= linesCount; i++) {
            int yLeft = y - (i * heightGates) / (linesCount + 1);
            int xBottom = x + (i * widthGates) / (linesCount + 1);

            figures.add(new Line(
                    x, yLeft,
                    xBottom, y,
                    gatesColor,
                    lineThickness
            ));
        }
    }
}