package main;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;

public class GamePanel extends JPanel {
    public static final int WIDTH = 1100; // Panel's width
    public static final int HEIGHT = 500; // Panel's height
    public static final Color BACKGROUND_COLOR = new Color(0, 0, 0); // Background color

    public GamePanel() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(BACKGROUND_COLOR);
    }
}
