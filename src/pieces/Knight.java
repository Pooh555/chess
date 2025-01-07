package pieces;

import main.GamePanel;
import main.Type;

public class Knight extends Piece {
    public Knight(boolean color, int col, int row) {
        super(color, col, row);

        this.type = Type.KNIGHT;

        if (color == GamePanel.WHITE)
            image = getImage("w-knight");
        else
            image = getImage("b-knight");
    }

    @Override
    public boolean canMove(int targetCol, int targetRow) {
        if (isWithinBoard(targetCol, targetRow) && !isInitialSquare(targetCol, targetRow)) {
            if (isEmptySquare(targetCol, targetRow)) {
                // basic knight movement
                if (Math.abs((targetCol - this.preCol) * (targetRow - this.preRow)) == 2)
                    return true;
            } else {
                // capture
                if (Math.abs((targetCol - this.preCol) * (targetRow - this.preRow)) == 2)
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
                // basic knight movement
                if (Math.abs((targetCol - this.preCol) * (targetRow - this.preRow)) == 2)
                    return true;
            } else {
                // capture
                if (Math.abs((targetCol - this.preCol) * (targetRow - this.preRow)) == 2)
                        return true;
            }
        }

        return false;
    }
}
