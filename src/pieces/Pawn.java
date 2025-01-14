package pieces;

import main.Board;
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
        if (this.color)
            moveDirection = 1; // black
        else
            moveDirection = -1; // white

        if (isWithinBoard(targetCol, targetRow) && !isInitialSquare(targetCol, targetRow)) {
            if (isEmptySquare(targetCol, targetRow)) {
                // basic pawn movement
                if (!isObstacleOnStraightLine(targetCol, targetRow)) {
                    // 1 square move
                    if (targetCol == preCol && targetRow - preRow == moveDirection)
                        return true;
                    // 2 square move
                    if (targetCol == preCol && targetRow - preRow == 2 * moveDirection && hasMoved == false) {
                        canBeEnPassent = true;
                        return true;
                    }
                }
                // en passent
                if ((targetCol == preCol + 1 || targetCol == preCol - 1) && targetRow - preRow == moveDirection)
                    if (isCapturable(targetCol, targetRow - moveDirection)
                            && Board.boardPieces[targetRow - moveDirection][targetCol].canBeEnPassent)
                        return true;
            } else {
                // capture
                if (targetRow - preRow == moveDirection && (targetCol == preCol + 1 || targetCol == preCol - 1)
                        && isCapturable(targetCol, targetRow))
                    return true;
            }
        }

        return false;
    }

    @Override
    public boolean canMoveExtended(int targetCol, int targetRow) {
        if (this.color)
            moveDirection = 1; // black
        else
            moveDirection = -1; // white

        if (isWithinBoard(targetCol, targetRow) && !isInitialSquare(targetCol, targetRow)) {
            if (targetRow == this.row + moveDirection) {
                if (this.col + 1 < 8)
                    if (targetCol == this.col + 1)
                        return true;
                if (this.col - 1 >= 0)
                    if (targetCol == this.col - 1)
                        return true;
            }
        }

        return false;
    }
}