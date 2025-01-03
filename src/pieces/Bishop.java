package pieces;

import main.GamePanel;
import main.Type;

public class Bishop extends Piece {
    public Bishop(boolean color, int col, int row) {
        super(color, col, row);

        this.type = Type.BISHOP;

        if (color == GamePanel.WHITE)
            image = getImage("w-bishop");
        else
            image = getImage("b-bishop");
    }

    @Override
    public boolean canMove(int targetCol, int targetRow) {
        if (isWithinBoard(targetCol, targetRow) && !isInitialSquare(targetCol, targetRow)) {
            if (isEmptySquare(targetCol, targetRow) && !isObstacleOnDiagonalLine(targetCol, targetRow)) {
                // basic bishop movement
                if (Math.abs(targetCol - preCol) - Math.abs(targetRow - preRow) == 0)
                    return true;
            } else {
                if (!isObstacleOnDiagonalLine(targetCol, targetRow))
                    if (Math.abs(targetCol - preCol) - Math.abs(targetRow - preRow) == 0)
                        if (isCapturable(targetCol, targetRow))
                            return true;
            }
        }

        return false;
    }
}
