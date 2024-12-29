package pieces;

import main.GamePanel;

public class Queen extends Piece {
    public Queen(int color, int col, int row) {
        super(color, col, row);

        if (color == GamePanel.WHITE)
            image = getImage("/pieces/w-queen");
        else
            image = getImage("/pieces/b-queen");
    }

    public boolean canMove(int targetCol, int targetRow) {
        if (isWithinBoard(targetCol, targetRow) && isSameSquare(targetCol, targetRow) == false) {
            // straight movement
            if (targetCol == preCol || targetRow == preRow)
                if (isValidSquare(targetCol, targetRow) && pieceIsOnStraightLine(targetCol, targetRow) == false)
                    return true;
            // diagonal movement
            if (Math.abs(targetCol - preCol) - Math.abs(targetRow - preRow) == 0)
                if (isValidSquare(targetCol, targetRow) && pieceIsOnDiagonalLine(targetCol, targetRow) == false)
                    return true;
        }

        return false;
    }
}
