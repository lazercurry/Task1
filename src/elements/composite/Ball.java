package elements.composite;

import elements.basic.Animated;
import elements.basic.Circle;
import elements.basic.Pentagon;

import java.awt.*;

public class Ball extends CompositeElement implements Animated {
    private static final int DEFAULT_RADIUS = 75;
    private static final Color DEFAULT_BALL_COLOR = Color.WHITE;
    private static final Color DEFAULT_PENTAGON_COLOR = Color.BLACK;

    private static final int NUM_SURROUNDING_PENTAGONS = 5;
    private static final double START_ANGLE_RAD = -Math.PI / 2;
    private static final double FULL_CIRCLE_RAD = 2 * Math.PI;
    private static final double SURROUND_RADIUS_FACTOR = 2.0 / 3.0;
    private static final double PENTAGON_SIZE_FACTOR = 0.5;

    private final int RADIUS;
    private final Color BALL_COLOR;
    private final Color PENTAGON_COLOR;

    private int initialX;
    private int initialY;
    private int speedX;
    private int speedY;
    private boolean isMoving;

    public Ball(int x, int y) {
        this(x, y, DEFAULT_RADIUS, DEFAULT_BALL_COLOR, DEFAULT_PENTAGON_COLOR);
    }

    public Ball(int x, int y, int radius, Color ballColor, Color pentagonColor) {
        this.x = x;
        this.y = y;
        this.initialX = x;
        this.initialY = y;
        this.speedX = 0;
        this.speedY = 0;
        this.isMoving = false;
        this.RADIUS = radius;
        this.BALL_COLOR = ballColor;
        this.PENTAGON_COLOR = pentagonColor;
        initialize();
    }

    @Override
    public void initialize() {
        int pentagonSize = (int)(RADIUS * PENTAGON_SIZE_FACTOR);

        figures.add(new Circle(x, y, RADIUS, BALL_COLOR));
        figures.add(new Pentagon(x, y, pentagonSize, PENTAGON_COLOR));

        int surroundRadius = (int)(RADIUS * SURROUND_RADIUS_FACTOR);
        for (int i = 0; i < NUM_SURROUNDING_PENTAGONS; i++) {
            double angle = FULL_CIRCLE_RAD * i / NUM_SURROUNDING_PENTAGONS + START_ANGLE_RAD;
            int pentagonX = x + (int)(surroundRadius * Math.cos(angle));
            int pentagonY = y + (int)(surroundRadius * Math.sin(angle));
            figures.add(new Pentagon(pentagonX, pentagonY, pentagonSize, PENTAGON_COLOR));
        }
    }

    @Override
    public void update() {
        if (isMoving) {
            x += speedX;
            y += speedY;

            figures.clear();
            initialize();
        }
    }

    public void setSpeed(int speedX, int speedY) {
        this.speedX = speedX;
        this.speedY = speedY;
        this.isMoving = true;
    }

    public void stop() {
        this.speedX = 0;
        this.speedY = 0;
        this.isMoving = false;
    }

    public void resetPosition() {
        this.x = initialX;
        this.y = initialY;
        stop();
        figures.clear();
        initialize();
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}