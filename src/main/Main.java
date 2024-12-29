package main;

import javax.swing.JFrame;

public class Main {
    public static void main(String[] args) {
        JFrame window = new JFrame("Chess"); // Create new window
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Close the program when the window is closed
        window.setResizable(false); // Window is not resizable
        window.setLocationRelativeTo(null); // Window will appear in the center of the monitor
        window.setVisible(true); // Window is visible on the screen
    }
}