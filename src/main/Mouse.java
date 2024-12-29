package main;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Mouse extends MouseAdapter {
    public int x, y;
    public boolean isPressed;

    @Override
    public void mousePressed(MouseEvent e) {
        isPressed = true;
        System.out.println("mouse clicked");
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        isPressed = false;
        System.out.println("mouse released");
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        x = e.getX();
        y = e.getY();
        // System.out.println("mouse location: " + x + ", " + y);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        x = e.getX();
        y = e.getY();
        // System.out.println("mouse location: " + x + ", " + y);
    }
}
