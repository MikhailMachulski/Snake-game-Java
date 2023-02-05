package com.company;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class Controller {
    private static final int SIZE_OF_SQUARE = 20;
    private static final int UNIT_X = 20;
    private static final int UNIT_Y = UNIT_X;
    private static final int RESOLUTION_X = UNIT_X * SIZE_OF_SQUARE;
    private static final int RESOLUTION_Y = UNIT_Y * SIZE_OF_SQUARE;
    private static final int DELAY_TIME_IN_MILLIS = 500;

    private View view;
    private Graphics graphics;
    private List<Point> snake = new ArrayList<>();
    private Direction direction = Direction.RIGHT;
    private Direction lastDirection = direction;
    private Point applePosition;

    public void start() {
        view.create(RESOLUTION_X, RESOLUTION_Y);
        snake.add(new Point(UNIT_X / 2, UNIT_Y / 2));
        spawnApple();
        move();
    }

    private void spawnApple() {
        do {
            applePosition = new Point(random(UNIT_X), random(UNIT_Y));
        } while (snake.contains(applePosition));
    }

    private int random(int max) {
        return (int) (Math.random() * max);
    }

    private void move() {
        while (true) {
            renderImage();
            delay();
            Point nextPoint = new Point(snake.get(snake.size() - 1));
            switch (direction) {
                case UP -> nextPoint.y--;
                case DOWN -> nextPoint.y++;
                case LEFT -> nextPoint.x--;
                case RIGHT -> nextPoint.x++;
            }
            nextPoint.x = Math.floorMod(nextPoint.x, UNIT_X);
            nextPoint.y = Math.floorMod(nextPoint.y, UNIT_Y);
            if (snake.contains(nextPoint)) {
                System.out.println("You lost");
                return;
            }
            snake.add(nextPoint);
            lastDirection = direction;
            if (nextPoint.equals(applePosition)) {
                spawnApple();
            } else {
                snake.remove(0);
            }
        }
    }

    private void delay() {
        try {
            Thread.sleep(DELAY_TIME_IN_MILLIS);
        } catch (InterruptedException exception) {
            exception.printStackTrace();
        }
    }

    public void handleKeyPress(int keyCode) {
        if (keyCode == KeyEvent.VK_LEFT && lastDirection != Direction.RIGHT) {
            direction = Direction.LEFT;
        }
        if (keyCode == KeyEvent.VK_RIGHT && lastDirection != Direction.LEFT) {
            direction = Direction.RIGHT;
        }
        if (keyCode == KeyEvent.VK_UP && lastDirection != Direction.DOWN) {
            direction = Direction.UP;
        }
        if (keyCode == KeyEvent.VK_DOWN && lastDirection != Direction.UP) {
            direction = Direction.DOWN;
        }
    }

    private void renderImage() {
        BufferedImage image = new BufferedImage(RESOLUTION_X, RESOLUTION_Y, BufferedImage.TYPE_INT_RGB);
        graphics = image.getGraphics();
        drawSnake();
        drawSquare(applePosition, Color.GREEN);
        view.setImage(image);
    }

    private void drawSnake() {
        for (Point point : snake) {
            drawSquare(point, Color.RED);
        }
       drawScore();
    }

    private void drawScore() {
        int score = snake.size() - 1;
        String scoreString = String.valueOf(score);
        for (int i = 0; i < scoreString.length(); i++) {
            Point headPoint = snake.get(snake.size() - 1 - i);
            graphics.setColor(Color.BLACK);
            graphics.setFont(new Font("Arial", Font.BOLD, 16));
            graphics.drawString(String.valueOf(scoreString.charAt(i)), headPoint.x * SIZE_OF_SQUARE + 5,
                    headPoint.y * SIZE_OF_SQUARE + 15);
        }
    }

    private void drawSquare(Point point, Color color) {
        graphics.setColor(color);
        graphics.fillRect(point.x * SIZE_OF_SQUARE, point.y * SIZE_OF_SQUARE, SIZE_OF_SQUARE, SIZE_OF_SQUARE);
    }

    public void setView(View view) {
        this.view = view;
    }

    /*private int random(int min, int max) {
        return (int) (Math.random() * (max - min + 1)) + min;
    }*/

    /* private boolean rand() {
         return Math.random() >= 0.5;
     }*/
}
