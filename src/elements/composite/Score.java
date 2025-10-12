package elements.composite;

import elements.basic.Line;
import elements.basic.Rectangle;

import java.awt.*;

public class Score extends CompositeElement {
    private static final Color DEFAULT_BACKGROUND_COLOR = Color.BLACK;
    private static final Color DEFAULT_NUMBERS_COLOR = Color.WHITE;

    private static final int DEFAULT_SCALE = 5;
    private static final int DEFAULT_LINE_THICKNESS = 16;

    private static final int DEFAULT_SCORE_HEIGHT = 35;
    private static final int DEFAULT_SCORE_WIDTH = 80;
    private static final int DIGIT_SPACING_RAW = 40;
    private static final int FIRST_DIGIT_X_OFFSET = 10;
    private static final int DIGIT_Y_OFFSET = 5;
    private static final int MAX_SCORE = 99;

    private final Color backgroundColor;
    private final Color numbersColor;
    private final int scale;
    private final int lineThickness;
    private final int digitSpacing;
    private final int scoreHeight;
    private final int scoreWidth;

    private int currentScore = 0;

    private class Digit extends CompositeElement {
        private final int x;
        private final int y;
        private final int width;
        private final int height;
        private final Color color;
        private final int digit;

        private static final int DIGIT_WIDTH_RAW = 20;
        private static final int DIGIT_HEIGHT_RAW = 20;

        public Digit(int x, int y, int digit, Color color) {
            this.x = x;
            this.y = y;
            this.digit = digit;
            this.color = color;
            this.width = DIGIT_WIDTH_RAW * scale;
            this.height = DIGIT_HEIGHT_RAW * scale;
            initialize();
        }

        @Override
        public void initialize() {
            int x1 = x;
            int y1 = y;
            int x2 = x + width;
            int y2 = y + height;
            int yMid = y + height / 2;

            switch (digit) {
                case 0:
                    figures.add(new Line(x1, y1, x2, y1, color, lineThickness));
                    figures.add(new Line(x1, y2, x2, y2, color, lineThickness));
                    figures.add(new Line(x1, y1, x1, y2, color, lineThickness));
                    figures.add(new Line(x2, y1, x2, y2, color, lineThickness));
                    break;
                case 1:
                    figures.add(new Line(x2, y1, x2, y2, color, lineThickness));
                    break;
                case 2:
                    figures.add(new Line(x1, y1, x2, y1, color, lineThickness));
                    figures.add(new Line(x2, y1, x2, yMid, color, lineThickness));
                    figures.add(new Line(x1, yMid, x2, yMid, color, lineThickness));
                    figures.add(new Line(x1, yMid, x1, y2, color, lineThickness));
                    figures.add(new Line(x1, y2, x2, y2, color, lineThickness));
                    break;
                case 3:
                    figures.add(new Line(x1, y1, x2, y1, color, lineThickness));
                    figures.add(new Line(x2, y1, x2, y2, color, lineThickness));
                    figures.add(new Line(x1, yMid, x2, yMid, color, lineThickness));
                    figures.add(new Line(x1, y2, x2, y2, color, lineThickness));
                    break;
                case 4:
                    figures.add(new Line(x1, y1, x1, yMid, color, lineThickness));
                    figures.add(new Line(x1, yMid, x2, yMid, color, lineThickness));
                    figures.add(new Line(x2, y1, x2, y2, color, lineThickness));
                    break;
                case 5:
                    figures.add(new Line(x1, y1, x2, y1, color, lineThickness));
                    figures.add(new Line(x1, y1, x1, yMid, color, lineThickness));
                    figures.add(new Line(x1, yMid, x2, yMid, color, lineThickness));
                    figures.add(new Line(x2, yMid, x2, y2, color, lineThickness));
                    figures.add(new Line(x1, y2, x2, y2, color, lineThickness));
                    break;
                case 6:
                    figures.add(new Line(x1, y1, x2, y1, color, lineThickness));
                    figures.add(new Line(x1, y1, x1, y2, color, lineThickness));
                    figures.add(new Line(x1, yMid, x2, yMid, color, lineThickness));
                    figures.add(new Line(x2, yMid, x2, y2, color, lineThickness));
                    figures.add(new Line(x1, y2, x2, y2, color, lineThickness));
                    break;
                case 7:
                    figures.add(new Line(x1, y1, x2, y1, color, lineThickness));
                    figures.add(new Line(x2, y1, x2, y2, color, lineThickness));
                    break;
                case 8:
                    figures.add(new Line(x1, y1, x2, y1, color, lineThickness));
                    figures.add(new Line(x1, y2, x2, y2, color, lineThickness));
                    figures.add(new Line(x1, y1, x1, y2, color, lineThickness));
                    figures.add(new Line(x2, y1, x2, y2, color, lineThickness));
                    figures.add(new Line(x1, yMid, x2, yMid, color, lineThickness));
                    break;
                case 9:
                    figures.add(new Line(x1, y1, x2, y1, color, lineThickness));
                    figures.add(new Line(x1, y1, x1, yMid, color, lineThickness));
                    figures.add(new Line(x1, yMid, x2, yMid, color, lineThickness));
                    figures.add(new Line(x2, y1, x2, y2, color, lineThickness));
                    figures.add(new Line(x1, y2, x2, y2, color, lineThickness));
                    break;
            }
        }
    }

    public Score(int x, int y) {
        this(x, y, DEFAULT_BACKGROUND_COLOR, DEFAULT_NUMBERS_COLOR, DEFAULT_SCALE, DEFAULT_LINE_THICKNESS);
    }

    public Score(int x, int y, Color backgroundColor, Color numbersColor, int scale, int lineThickness) {
        this.x = x;
        this.y = y;
        this.backgroundColor = backgroundColor;
        this.numbersColor = numbersColor;
        this.scale = scale;
        this.lineThickness = lineThickness;
        this.digitSpacing = DIGIT_SPACING_RAW * scale;
        this.scoreHeight = DEFAULT_SCORE_HEIGHT * scale;
        this.scoreWidth = DEFAULT_SCORE_WIDTH * scale;
        initialize();
    }

    @Override
    public void initialize() {
        figures.clear();
        figures.add(new Rectangle(x, y, scoreWidth, scoreHeight, backgroundColor));

        int digitY = y + DIGIT_Y_OFFSET * scale;
        int firstDigit = currentScore / 10;
        int secondDigit = currentScore % 10;

        figures.add(new Digit(x + FIRST_DIGIT_X_OFFSET * scale, digitY, firstDigit, numbersColor));
        figures.add(new Digit(x + FIRST_DIGIT_X_OFFSET * scale + digitSpacing, digitY, secondDigit, numbersColor));
    }

    public void incrementScore() {
        currentScore++;
        if (currentScore > MAX_SCORE) {
            resetScore();
        }
        initialize();
    }

    public void resetScore() {
        currentScore = 0;
        initialize();
    }

    public int getScore() {
        return currentScore;
    }
}