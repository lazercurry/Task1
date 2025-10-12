package elements.composite;

import elements.basic.Line;
import elements.basic.Rectangle;
import elements.basic.RoundedRectangle;

import java.awt.*;

public class Tribune extends CompositeElement {
    private static final Color DEFAULT_BACKGROUND_COLOR = new Color(34, 94, 197);
    private static final Color DEFAULT_LINES_COLOR = Color.BLACK;
    private static final Color DISPLAY_ADS_COLOR = Color.RED;
    private static final Color DISPLAY_COLOR = Color.BLACK;

    private static final int DEFAULT_NUMBER_OF_ROWS = 4;
    private static final int DEFAULT_SEATS_PER_ROW = 40;
    private static final int DEFAULT_NUMBER_OF_DISPLAYS = 8;
    private static final int DISPLAY_WIDTH = 300;
    private static final int DISPLAY_Y_OFFSET = 10;
    private static final int DISPLAY_HEIGHT_PADDING = 20;
    private static final int DISPLAY_ROUNDNESS = 5;
    private static final int ROW_LINE_THICKNESS = 6;
    private static final int SEAT_LINE_THICKNESS = 3;
    private static final int SEAT_VERTICAL_OFFSET = 20;

    public Tribune(int x, int y) {
        this.x = x;
        this.y = y;
        initialize();
    }

    @Override
    public void initialize() {
        figures.add(new Rectangle(0, 0, x, y, DEFAULT_BACKGROUND_COLOR));

        int numberOfRows = DEFAULT_NUMBER_OF_ROWS;
        int rowSpacing = y / numberOfRows;

        for (int i = 1; i <= numberOfRows; i++) {
            int lineY = i * rowSpacing;
            figures.add(new Line(0, lineY, x, lineY, DEFAULT_LINES_COLOR, ROW_LINE_THICKNESS));
        }

        int seatsPerRow = DEFAULT_SEATS_PER_ROW;
        int seatWidth = x / seatsPerRow;

        for (int row = 1; row <= numberOfRows; row++) {
            int lineY = row * rowSpacing - rowSpacing / 2;
            for (int seat = 1; seat < seatsPerRow; seat++) {
                int seatX = seat * seatWidth;
                figures.add(new Line(seatX, lineY - rowSpacing / 2 + SEAT_VERTICAL_OFFSET, seatX, lineY + rowSpacing / 2, DEFAULT_LINES_COLOR, SEAT_LINE_THICKNESS));
            }
        }

        figures.add(new Rectangle(0, y, x, rowSpacing, DISPLAY_ADS_COLOR));

        int numberOfDisplays = DEFAULT_NUMBER_OF_DISPLAYS;
        int displayWidth = DISPLAY_WIDTH;
        int availableWidth = x - displayWidth;
        int displayHeight = rowSpacing - DISPLAY_HEIGHT_PADDING;

        for (int i = 0; i < numberOfDisplays; i++) {
            int displayX = i * (availableWidth) / (numberOfDisplays - 1);
            figures.add(new RoundedRectangle(displayX, y + DISPLAY_Y_OFFSET, displayWidth, displayHeight, DISPLAY_COLOR, false, DISPLAY_ROUNDNESS));
        }
    }
}