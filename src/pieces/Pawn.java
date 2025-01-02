package pieces;

import main.GamePanel;

public class Pawn extends Piece {
    public Pawn(boolean color, int col, int row) {
        super(color, col, row);

        if (color == GamePanel.WHITE)
            image = getImage("w-pawn");
        else
            image = getImage("b-pawn");
    }
}