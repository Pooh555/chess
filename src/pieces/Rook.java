package pieces;

import main.GamePanel;
import main.Type;

public class Rook extends Piece {
    public Rook(boolean color, int col, int row) {
        super(color, col, row);

        this.type = Type.ROOK;

        if (color == GamePanel.WHITE)
            image = getImage("w-rook");
        else
            image = getImage("b-rook");
    }
}
