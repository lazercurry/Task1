import elements.basic.Drawable;
import elements.basic.Line;
import elements.basic.Rectangle;
import elements.composite.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class GuiAnimateFrame extends JFrame {
    private final int windowHeight;
    private final int windowWidth;

    private final DrawingPanel drawingPanel;

    private Ball ball;
    private Human human;
    private Gates gates;
    private Score score;

    private static final int PAUSE_AFTER_GOAL = 400;
    private static final int FPS_TIMER_DELAY_MS = 5;

    private static final int BALL_OFFSET_X = 50;
    private static final int BALL_OFFSET_Y = 200;
    private static final int HUMAN_OFFSET_X = 300;
    private static final int HUMAN_OFFSET_Y = 280;
    private static final int GATES_OFFSET_X = 300;
    private static final int GATES_OFFSET_Y = 300;
    private static final int SCORE_OFFSET_X = 80;
    private static final int SCORE_OFFSET_Y = 0;
    private static final int FIELD_TRIBUNE_HEIGHT_DIVIDER = 4;

    private static final int BALL_KICK_SPEED_X = 8;
    private static final int BALL_KICK_SPEED_Y = 0;
    private static final int GOAL_LINE_OFFSET_X = 150;
    private static final int GOAL_LINE_OFFSET_Y = 100;

    private enum AnimationState {
        WAITING, KICKING, BALL_FLYING, GOAL_SCORED, PAUSE
    }

    private AnimationState currentState = AnimationState.WAITING;
    private int stateTimer = 0;

    public GuiAnimateFrame(int windowWidth, int windowHeight) {
        this.windowWidth = windowWidth;
        this.windowHeight = windowHeight;
        if (windowWidth <= 0) {
            throw new IllegalArgumentException("Incorrect width!");
        }
        if (windowHeight <= 0) {
            throw new IllegalArgumentException("Incorrect height!");
        }

        drawingPanel = new DrawingPanel();
        setupBackGround();
        initializeScene();
        drawingPanel.setFocusable(true);
        add(drawingPanel);

        setBasicSettings();
        setupTimers();
        setupKeyListener();

        setVisible(true);
    }

    private void setupKeyListener() {
        drawingPanel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    if (currentState == AnimationState.WAITING) {
                        currentState = AnimationState.KICKING;
                        stateTimer = 0;
                        human.startKick();
                    }
                }
            }
        });

    }

    private void setupBackGround() {
        Field field = new Field(windowWidth, windowHeight);
        drawingPanel.addFigure(field);

        Tribune leftTribune = new Tribune(windowWidth, windowHeight / FIELD_TRIBUNE_HEIGHT_DIVIDER);
        drawingPanel.addFigure(leftTribune);
    }

    private void initializeScene() {
        ball = new Ball(windowWidth / 2 - BALL_OFFSET_X, windowHeight / 2 + BALL_OFFSET_Y);
        drawingPanel.addFigure(ball);

        human = new Human(windowWidth / 2 - HUMAN_OFFSET_X, windowHeight / 2 - HUMAN_OFFSET_Y);
        drawingPanel.addFigure(human);

        gates = new Gates(windowWidth - GATES_OFFSET_X, windowHeight / 2 + GATES_OFFSET_Y);
        drawingPanel.addFigure(gates);

        score = new Score(windowWidth / 2 - SCORE_OFFSET_X, SCORE_OFFSET_Y);
        drawingPanel.addFigure(score);
    }

    private void setupTimers() {
        Timer fpsTimer = new Timer(FPS_TIMER_DELAY_MS, e -> {
            updateAnimation();
            drawingPanel.repaint();
        });

        fpsTimer.start();
    }

    private void updateAnimation() {
        stateTimer++;

        switch (currentState) {
            case KICKING:
                if (!human.isKicking()) {
                    currentState = AnimationState.BALL_FLYING;
                    stateTimer = 0;
                    ball.setSpeed(BALL_KICK_SPEED_X, BALL_KICK_SPEED_Y);
                }
                break;

            case BALL_FLYING:
                if (ball.getX() >= windowWidth - GOAL_LINE_OFFSET_X && ball.getY() <= windowHeight - GOAL_LINE_OFFSET_Y) {
                    currentState = AnimationState.GOAL_SCORED;
                    stateTimer = 0;
                    ball.stop();
                    score.incrementScore();
                } else if (ball.getX() > windowWidth || ball.getY() < 0) {
                    currentState = AnimationState.GOAL_SCORED;
                    stateTimer = 0;
                    ball.stop();
                }
                break;

            case GOAL_SCORED:
                currentState = AnimationState.PAUSE;
                stateTimer = 0;
                break;

            case PAUSE:
                if (stateTimer >= PAUSE_AFTER_GOAL) {
                    ball.resetPosition();
                    currentState = AnimationState.WAITING;
                    stateTimer = 0;
                }
                break;
        }
    }

    private void setBasicSettings() {
        setTitle("Football Animation");
        setSize(windowWidth, windowHeight);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }
}