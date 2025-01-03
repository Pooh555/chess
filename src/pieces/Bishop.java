package pieces;

import main.GamePanel;
import main.Type;

public class Bishop extends Piece {
    public Bishop(boolean color, int col, int row) {
        super(color, col, row);

        this.type = Type.BISHOP;

        if (color == GamePanel.WHITE)
            image = getImage("w-bishop");
        else
            image = getImage("b-bishop");
    }
}
