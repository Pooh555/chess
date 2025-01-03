package pieces;

import main.Board;
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
            if (Board.boardPieces[targetRow][targetCol] == null) {
                // basic knight movement
                if (Math.abs((targetCol - this.preCol) * (targetRow - this.preRow)) == 2)
                    return true;
            } else {
                if (Math.abs((targetCol - this.preCol) * (targetRow - this.preRow)) == 2)
                    if (isCapturable(targetCol, targetRow))
                        return true;
            }
        }

        return false;
    }
}
