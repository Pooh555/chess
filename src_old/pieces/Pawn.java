package pieces;

import main.GamePanel;
import main.Type;

public class Pawn extends Piece {
    public Pawn(int color, int col, int row) {
        super(color, col, row);

        type = Type.PAWN;

        if (color == GamePanel.WHITE)
            image = getImage("/pieces/w-pawn");
        else
            image = getImage("/pieces/b-pawn");
    }

    public boolean canMove(int targetCol, int targetRow) {
        if (isWithinBoard(targetCol, targetRow) && isSameSquare(targetCol, targetRow) == false) {
            // define the move value based on its color
            int moveValue;

            if (color == GamePanel.WHITE)
                moveValue = -1; // move upward
            else
                moveValue = 1; // move downward

            // check if there is something in front of the pawn
            hitP = getHitP(targetCol, targetRow);

            // 1 square move
            if (targetCol == preCol && targetRow == preRow + moveValue && hitP == null)
                return true;

            // 2 square move    
            if (targetCol == preCol && targetRow == preRow + (2 * moveValue) && hitP == null && hasMoved == false
                    && pieceIsOnStraightLine(targetCol, targetRow) == false)
                return true;

            // diagonal movement and capture
            if (Math.abs(targetCol - preCol) == 1 && targetRow == preRow + moveValue && hitP != null
                    && hitP.color != color)
                return true;

            // en passent
            if (Math.abs(targetCol - preCol) == 1 && targetRow == preRow + moveValue)
                for (Piece piece : GamePanel.simPieces)
                    if (piece.col == targetCol && piece.row == preRow && piece.twoSteps == true) {
                        hitP = piece;
                        return true;
                    }
        }

        return false;
    }
}
