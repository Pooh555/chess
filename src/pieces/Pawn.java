package pieces;

import main.GamePanel;
import main.Type;

public class Pawn extends Piece {
    int moveDirection;

    public Pawn(boolean color, int col, int row) {
        super(color, col, row);

        this.type = Type.PAWN;

        if (color == GamePanel.WHITE)
            image = getImage("w-pawn");
        else
            image = getImage("b-pawn");
    }

    @Override
    public boolean canMove(int targetCol, int targetRow) {
        if (GamePanel.currentColor)
            moveDirection = 1; // black
        else
            moveDirection = -1; // white

        if (isWithinBoard(targetCol, targetRow) && !isInitialSquare(targetCol, targetRow)) {
            if (isEmptySquare(targetCol, targetRow)) {
                // basic queen (rook + bishop) movement
                if (!isObstacleOnStraightLine(targetCol, targetRow)) {
                    if (targetCol == preCol && targetRow - preRow == moveDirection)
                        return true;
                    if (targetCol == preCol && targetRow - preRow == 2 * moveDirection && hasMoved == false) 
                        return true;
                }
            } else {
                if (targetRow == preRow + moveDirection && (targetCol == preCol + 1 || targetCol == preCol - 1) && isCapturable(targetCol, targetRow))
                    return true;
            }
        }

        return false;
    }
}
