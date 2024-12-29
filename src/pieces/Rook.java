package pieces;

import main.GamePanel;

public class Rook extends Piece {
    public Rook(int color, int col, int row) {
        super(color, col, row);

        if(color == GamePanel.WHITE) {
            image = getImage("/piece/w-rook");
        }
        if(color == GamePanel.BLACK) {
            image = getImage("/piece/b-rook");
        }
    }
}
