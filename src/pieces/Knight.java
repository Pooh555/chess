package pieces;

import main.GamePanel;

public class Knight extends Piece {
    public Knight(boolean color, int col, int row) {
        super(color, col, row);

        if (color == GamePanel.WHITE)
            image = getImage("w-knight");
        else
            image = getImage("b-knight");
    }
}
