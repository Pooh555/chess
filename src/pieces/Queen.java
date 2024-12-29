package pieces;

import main.GamePanel;

public class Queen extends Piece {
    public Queen(int color, int col, int row) {
        super(color, col, row);

        if(color == GamePanel.WHITE) {
            image = getImage("/piece/w-queen");
        }
        if(color == GamePanel.BLACK) {
            image = getImage("/piece/b-queen");
        }
    }
}

