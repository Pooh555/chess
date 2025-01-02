package main;

import javax.swing.JFrame;

public class Main {
    public static void main(String[] args) {
        JFrame window = new JFrame("Chess"); // create new window

        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // close the program when the window is closed
        window.setResizable(true); // window is resizable

        GamePanel gp = new GamePanel(); // initialize a new GamePanel

        window.add(gp); // add GamePanel to the window
        window.pack(); // adjust the window size to the panel
        window.setLocationRelativeTo(null); // window will appear in the center of the monitor
        window.setVisible(true); // window is visible on the screen

        System.out.println("GamePanel initializezd.");

        gp.launchGame(); // lauch Chess

        System.out.println("Chess lauched.");
    }
}