package main;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Mouse extends MouseAdapter {
    public int x, y;
    public boolean isPressed;

    @Override
    public void mousePressed(MouseEvent mouseEvent) {
        isPressed = true;
    }
    
    @Override
    public void mouseReleased(MouseEvent mouseEvent) {
        isPressed = false;
    }

    @Override
    public void mouseDragged(MouseEvent mouseEvent) {
        x = mouseEvent.getX();
        y = mouseEvent.getY();
    }

    @Override
    public void mouseMoved(MouseEvent mouseEvent) {
        x = mouseEvent.getX();
        y = mouseEvent.getY();
    }
}
