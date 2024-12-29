package main;

import javax.swing.JFrame;

public class Main {
    public static void main(String[] args) {
        JFrame window = new JFrame("Chess"); // Create new window
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Close the program when the window is closed
        window.setResizable(false); // Window is not resizable

        GamePanel gp = new GamePanel(); // Initialize a new GamePanel
        window.add(gp); // Add GamePanel to the window
        window.pack(); // Adjust the window size to the panel

        window.setLocationRelativeTo(null); // Window will appear in the center of the monitor
        window.setVisible(true); // Window is visible on the screen

        gp.launchGame(); // Lauch Chess
    }
}