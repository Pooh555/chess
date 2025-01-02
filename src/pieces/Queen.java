package pieces;

import main.GamePanel;

public class Queen extends Piece {
    public Queen(boolean color, int col, int row) {
        super(color, col, row);

        if (color == GamePanel.WHITE)
            image = getImage("w-queen");
        else
            image = getImage("b-queen");
    }
}
