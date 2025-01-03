package pieces;

import main.Board;
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
            if (Board.boardPieces[targetRow][targetCol] == null)
                // basic bishop movement
                if ((targetRow - this.preRow) == 0) {
                    if (Math.abs((targetRow - this.preRow) / (targetCol - this.preCol)) == 1)
                        return true;
                } else {
                    if (Math.abs((targetCol - this.preCol) / (targetRow - this.preRow)) == 1)
                        return true;
                }
        }

        return false;
    }
}
