package elements.composite;

import elements.basic.Animated;
import elements.basic.Circle;
import elements.basic.Line;

import java.awt.*;

public class Human extends CompositeElement implements Animated {
    private static final Color DEFAULT_HEAD_COLOR = new Color(255, 220, 177);
    private static final Color DEFAULT_BODY_COLOR = Color.BLACK;

    private static final int DEFAULT_SCALE = 3;
    private static final int DEFAULT_LINE_THICKNESS = 6;

    private static final int HEAD_RADIUS = 30;
    private static final int HEAD_Y_OFFSET = 30;

    private static final int HAND_RADIUS = 4;
    private static final int HAND_Y_OFFSET = 25;
    private static final int LEFT_HAND_X_OFFSET = -10;
    private static final int RIGHT_HAND_X_OFFSET = 10;

    private static final int SHOULDERS_Y_OFFSET = 40;
    private static final int SHOULDERS_X_LEFT_OFFSET = -12;
    private static final int SHOULDERS_X_RIGHT_OFFSET = 12;

    private static final int BODY_TOP_Y_OFFSET = 60;
    private static final int BODY_LENGTH = 80;

    private static final int ARMS_LENGTH = 50;
    private static final int ARMS_Y_OFFSET_FROM_TOP = 20;

    private static final int LEG_X_OFFSET = 30;
    private static final int LEG_Y_OFFSET = 60;

    private static final int KICK_ANGLE_MAX = 60;
    private static final int KICK_LEG_LENGTH = 60;
    private static final int KICK_DURATION_FRAMES = 15;

    private final Color headColor;
    private final Color bodyColor;
    private final int scale;
    private final int lineThickness;

    private boolean isKicking = false;
    private int kickPhase = 0;

    public Human(int x, int y) {
        this(x, y, DEFAULT_HEAD_COLOR, DEFAULT_BODY_COLOR, DEFAULT_SCALE, DEFAULT_LINE_THICKNESS);
    }

    public Human(int x, int y, Color headColor, Color bodyColor, int scale, int lineThickness) {
        this.x = x;
        this.y = y;
        this.headColor = headColor;
        this.bodyColor = bodyColor;
        this.scale = scale;
        this.lineThickness = lineThickness;
        initialize();
    }

    @Override
    public void initialize() {
        figures.clear();

        figures.add(new Circle(x, y + HEAD_Y_OFFSET * scale, HEAD_RADIUS * scale, headColor, lineThickness));

        figures.add(new Circle(x + LEFT_HAND_X_OFFSET * scale, y + HAND_Y_OFFSET * scale, HAND_RADIUS * scale, bodyColor, lineThickness));
        figures.add(new Circle(x + RIGHT_HAND_X_OFFSET * scale, y + HAND_Y_OFFSET * scale, HAND_RADIUS * scale, bodyColor, lineThickness));

        figures.add(new Line(x + SHOULDERS_X_LEFT_OFFSET * scale, y + SHOULDERS_Y_OFFSET * scale, x + SHOULDERS_X_RIGHT_OFFSET * scale, y + SHOULDERS_Y_OFFSET * scale, bodyColor, lineThickness));

        int bodyTop = y + BODY_TOP_Y_OFFSET * scale;
        int bodyBottom = bodyTop + BODY_LENGTH * scale;
        figures.add(new Line(x, bodyTop, x, bodyBottom, bodyColor, lineThickness));

        int armsY = bodyTop + ARMS_Y_OFFSET_FROM_TOP * scale;
        figures.add(new Line(x - ARMS_LENGTH * scale, armsY, x + ARMS_LENGTH * scale, armsY, bodyColor, lineThickness));

        if (isKicking && kickPhase < KICK_DURATION_FRAMES) {
            double progress = (double) kickPhase / KICK_DURATION_FRAMES;
            int angle = (int) (KICK_ANGLE_MAX * Math.sin(Math.PI * progress));
            double radians = Math.toRadians(angle);
            int kickX = x + LEG_X_OFFSET * scale + (int) (KICK_LEG_LENGTH * scale * Math.sin(radians));
            int kickY = bodyBottom + (int) (KICK_LEG_LENGTH * scale * Math.cos(radians));

            figures.add(new Line(x, bodyBottom, x - LEG_X_OFFSET * scale, bodyBottom + LEG_Y_OFFSET * scale, bodyColor, lineThickness));
            figures.add(new Line(x, bodyBottom, kickX, kickY, bodyColor, lineThickness));
        } else {
            figures.add(new Line(x, bodyBottom, x - LEG_X_OFFSET * scale, bodyBottom + LEG_Y_OFFSET * scale, bodyColor, lineThickness));
            figures.add(new Line(x, bodyBottom, x + LEG_X_OFFSET * scale, bodyBottom + LEG_Y_OFFSET * scale, bodyColor, lineThickness));
        }
    }

    public void startKick() {
        isKicking = true;
        kickPhase = 0;
    }

    public boolean isKicking() {
        return isKicking && kickPhase < KICK_DURATION_FRAMES;
    }

    @Override
    public void update() {
        if (isKicking) {
            kickPhase++;
            if (kickPhase >= KICK_DURATION_FRAMES) {
                isKicking = false;
                kickPhase = 0;
            }
            initialize();
        }
    }
}