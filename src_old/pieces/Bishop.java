package pieces;

import main.GamePanel;
import main.Type;

public class Bishop extends Piece {
    public Bishop(int color, int col, int row) {
        super(color, col, row);

        type = Type.BISHOP;

        if (color == GamePanel.WHITE)
            image = getImage("/pieces/w-bishop");
        else
            image = getImage("/pieces/b-bishop");
    }

    public boolean canMove(int targetCol, int targetRow) {
        if (isWithinBoard(targetCol, targetRow) && isSameSquare(targetCol, targetRow) == false)
            if (Math.abs(targetCol - preCol) - Math.abs(targetRow - preRow) == 0)
                if (isValidSquare(targetCol, targetRow) && pieceIsOnDiagonalLine(targetCol, targetRow) == false)
                    return true;
                    
        return false;
    }
}
