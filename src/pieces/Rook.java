package pieces;

import main.GamePanel;

public class Rook extends Piece {
    public Rook(boolean color, int col, int row) {
        super(color, col, row);

        if (color == GamePanel.WHITE)
            image = getImage("w-rook");
        else
            image = getImage("b-rook");
    }
}
