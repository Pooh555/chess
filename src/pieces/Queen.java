package pieces;

import main.Board;
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
            if (Board.boardPieces[targetRow][targetCol] == null) {
                // basic queen (rook + bishop) movement
                if (targetCol == preCol || targetRow == preRow)
                    return true;
                if (Math.abs(targetCol - preCol) - Math.abs(targetRow - preRow) == 0)
                    return true;
            }
        }

        return false;
    }
}
