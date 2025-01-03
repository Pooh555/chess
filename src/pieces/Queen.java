package pieces;

import main.GamePanel;
import main.Type;

public class Queen extends Piece {
    public Queen(boolean color, int col, int row) {
        super(color, col, row);

        this.type = Type.QUEEN;

        if (color == GamePanel.WHITE)
            image = getImage("w-queen");
        else
            image = getImage("b-queen");
    }

    @Override
    public boolean canMove(int targetCol, int targetRow) {
        if (isWithinBoard(targetCol, targetRow) && !isInitialSquare(targetCol, targetRow)) {
            if (isEmptySquare(targetCol, targetRow)) {
                // basic queen (rook + bishop) movement
                if (!isObstacleOnStraightLine(targetCol, targetRow))
                    if (targetCol == preCol || targetRow == preRow)
                        return true;
                if (!isObstacleOnDiagonalLine(targetCol, targetRow))
                    if (Math.abs(targetCol - preCol) - Math.abs(targetRow - preRow) == 0)
                        return true;
            } else {
                if (targetCol == preCol || targetRow == preRow)
                    if (!isObstacleOnStraightLine(targetCol, targetRow))
                        if (isCapturable(targetCol, targetRow))
                            return true;
                if (Math.abs(targetCol - preCol) - Math.abs(targetRow - preRow) == 0)
                    if (!isObstacleOnDiagonalLine(targetCol, targetRow))
                        if (isCapturable(targetCol, targetRow))
                            return true;
            }
        }

        return false;
    }

    @Override
    public boolean canMoveExtended(int targetCol, int targetRow) {
        if (isWithinBoard(targetCol, targetRow) && !isInitialSquare(targetCol, targetRow)) {
            if (isEmptySquare(targetCol, targetRow)) {
                // basic queen (rook + bishop) movement
                if (!isObstacleOnStraightLine(targetCol, targetRow))
                    if (targetCol == preCol || targetRow == preRow)
                        return true;
                if (!isObstacleOnDiagonalLine(targetCol, targetRow))
                    if (Math.abs(targetCol - preCol) - Math.abs(targetRow - preRow) == 0)
                        return true;
            } else {
                if (targetCol == preCol || targetRow == preRow)
                    if (!isObstacleOnStraightLine(targetCol, targetRow))
                            return true;
                if (Math.abs(targetCol - preCol) - Math.abs(targetRow - preRow) == 0)
                    if (!isObstacleOnDiagonalLine(targetCol, targetRow))
                            return true;
            }
        }

        return false;
    }
}
