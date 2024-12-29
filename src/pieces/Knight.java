package pieces;

import main.GamePanel;

public class Knight extends Piece {
    public Knight(int color, int col, int row) {
        super(color, col, row);

        if(color == GamePanel.WHITE) {
            image = getImage("/piece/w-knight");
        }
        if(color == GamePanel.BLACK) {
            image = getImage("/piece/b-knight");
        }
    }
}
