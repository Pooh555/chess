package main;

import java.awt.Graphics2D;

public class Board {
    // Chessboard is 8x8
    final int MAX_COL = 8;
    final int MAX_ROW = 8;
    public static final int SQUARE_SIZE = 100; // width and height of 1 chess board square
    public static final int HALF_SQUARE_SIZE = SQUARE_SIZE / 2;

    public void draw(Graphics2D g2) {
        for (int row = 0; row < MAX_ROW; row++)
            for (int col = 0; col < MAX_COL; col++) {

            }
    }

}
