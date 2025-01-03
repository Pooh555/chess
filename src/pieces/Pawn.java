package pieces;

import main.GamePanel;
import main.Type;

public class Pawn extends Piece {
    public Pawn(boolean color, int col, int row) {
        super(color, col, row);

        this.type = Type.PAWN;

        if (color == GamePanel.WHITE)
            image = getImage("w-pawn");
        else
            image = getImage("b-pawn");
    }
}
