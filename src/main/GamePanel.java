package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JPanel;

public class GamePanel extends JPanel implements Runnable {
    public static final int WIDTH = 1100; // panel's width
    public static final int HEIGHT = 800; // panel's height
    public static final Color BACKGROUND_COLOR = new Color(0, 0, 0); // background color
    public int FPS = 360; // FPS

    Thread gameThread;

    public GamePanel() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(BACKGROUND_COLOR);
    }

    public void launchGame() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {

        // game loop
        double drawInterval = 1000000000 / FPS; // 1 tick
        double delta = 0; // how much time hass passed since the last render
        long lastTime = System.nanoTime(); // time of the last render
        long currentTime; // current time

        while (gameThread != null) {
            currentTime = System.nanoTime();

            delta += (currentTime - lastTime) / drawInterval;
            lastTime = currentTime;

            if (delta >= 1) {
                update();
                repaint();
                delta--;
            }
        }

    }

    private void update() {

    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
    }
}
