package pieces;

import main.GamePanel;
import main.Type;

public class Knight extends Piece {
    public Knight(boolean color, int col, int row) {
        super(color, col, row);

        this.type = Type.KNIGHT;

        if (color == GamePanel.WHITE)
            image = getImage("w-knight");
        else
            image = getImage("b-knight");
    }
}
