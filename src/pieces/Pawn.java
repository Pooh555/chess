package pieces;

import main.GamePanel;

public class Pawn extends Piece {
    public Pawn(int color, int col, int row) {
        super(color, col, row);

        if (color == GamePanel.WHITE)
            image = getImage("/pieces/w-pawn");
        else
            image = getImage("/pieces/b-pawn");
    }

    public boolean canMove(int targetCol, int targetRow) {
        if (isWithinBoard(targetCol, targetRow) && isSameSquare(targetCol, targetRow) == false)
            if (Math.abs(targetCol - preCol) + Math.abs(targetRow - preRow) == 1
                    || Math.abs(targetCol - preCol) * Math.abs(targetRow - preRow) == 1)
                if (isValidSquare(targetCol, targetRow))
                    return true;
                    
        return false;
    }
}
