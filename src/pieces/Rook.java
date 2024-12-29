package pieces;

import main.GamePanel;

public class Rook extends Piece {
    public Rook(int color, int col, int row) {
        super(color, col, row);

        if (color == GamePanel.WHITE)
            image = getImage("/pieces/w-rook");
        else
            image = getImage("/pieces/b-rook");
    }

    public boolean canMove(int targetCol, int targetRow) {
        if (isWithinBoard(targetCol, targetRow) && isSameSquare(targetCol, targetRow) == false)
            if (targetCol == preCol || targetRow == preRow)
                if (isValidSquare(targetCol, targetRow) && pieceIsOnStraightLine(targetCol, targetRow) == false)
                    return true;
                    
        return false;
    }
}
