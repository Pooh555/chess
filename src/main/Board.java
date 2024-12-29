package main;

import java.awt.Color;
import java.awt.Graphics2D;

public class Board {
    // chessboard is 8*8
    // each square is 100*100 pixels
    final int MAX_COL = 8;
    final int MAX_ROW = 8;
    public static final int SQUARE_SIZE = 100;
    public static final int HALF_SQUARE_SIZE = SQUARE_SIZE / 2;

    // paint the chessboard
    public void draw(Graphics2D g2) {

        int count = 0;
        Color lightSquare = new Color(210, 165, 125);
        Color darkSquare = new Color(175, 115, 70);

        for (int row = 0; row < MAX_ROW; row++) {
            for (int col = 0; col < MAX_COL; col++) {
                if (count == 0) {
                    g2.setColor(lightSquare);
                    count++;
                }
                else {
                    g2.setColor(darkSquare);
                    count--;
                }

                g2.fillRect(col * SQUARE_SIZE, row * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);
            }
            
            if (count == 0) count++;
            else count--;
        }
    }

}
