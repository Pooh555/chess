package pieces;

import main.GamePanel;
import main.Type;

public class King extends Piece {
    public King(boolean color, int col, int row) {
        super(color, col, row);

        this.type = Type.KING;

        if (color == GamePanel.WHITE)
            image = getImage("w-king");
        else
            image = getImage("b-king");
    }

    @Override
    public boolean canMove(int targetCol, int targetRow) {
        if (isWithinBoard(targetCol, targetRow) && !isInitialSquare(targetCol, targetRow)) {
            if (isEmptySquare(targetCol, targetRow)) {
                // basic king movement
                if (Math.abs(targetCol - this.preCol) + Math.abs(targetRow - this.preRow) == 1
                        || Math.abs((targetCol - this.preCol) * (targetRow - this.preRow)) == 1)
                    return true;
            } else {
                if (Math.abs(targetCol - this.preCol) + Math.abs(targetRow - this.preRow) == 1
                        || Math.abs((targetCol - this.preCol) * (targetRow - this.preRow)) == 1)
                    if (isCapturable(targetCol, targetRow))
                        return true;
            }
        }

        return false;
    }
}
