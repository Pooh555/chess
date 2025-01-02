package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.lang.Thread;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JPanel;

public class GamePanel extends JPanel implements Runnable {
    public static int WIDTH = 1100; // window's width
    public static int HEIGHT = 1100; // window's height
    public static Dimension windowSize = new Dimension(WIDTH, HEIGHT); // window size
    static final int FPS = 360; // game FPS and tick rate
    Thread gameThread; // main game thread
    Board board = new Board();

    public GamePanel() {
        setPreferredSize(windowSize); // set initial window size
        setBackground(Color.BLACK); // set initial window background

        // update the window's size
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                WIDTH = getWidth(); // update WIDTH
                HEIGHT = getHeight(); // update HEIGHT

                System.out.println("New WIDTH: " + WIDTH + ", New HEIGHT: " + HEIGHT);
            }
        });
    }

    public void launchGame() {
        gameThread = new Thread(this); // initiate a new game thread
        gameThread.start(); // calls the run() method
    }

    @Override
    public void run() {
        // game loop
        double tickInterval = 1000000000 / FPS; // 1 game tick (in nanoseconds)
        double delta = 0; // difference between the last tick and the current time
        long previousTickTime = System.nanoTime(); // time of the last tick
        long currentTime; // current time

        while (gameThread != null) {
            currentTime = System.nanoTime();

            delta += (currentTime - previousTickTime) / tickInterval;
            previousTickTime = currentTime;

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

        Graphics2D g2 = (Graphics2D)g;

        board.draw(g2);
    }
}
