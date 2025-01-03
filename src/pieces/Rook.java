package pieces;

import main.Board;
import main.GamePanel;
import main.Type;

public class Rook extends Piece {
    public Rook(boolean color, int col, int row) {
        super(color, col, row);

        this.type = Type.ROOK;

        if (color == GamePanel.WHITE)
            image = getImage("w-rook");
        else
            image = getImage("b-rook");
    }

    @Override
    public boolean canMove(int targetCol, int targetRow) {
        if (isWithinBoard(targetCol, targetRow) && !isInitialSquare(targetCol, targetRow)) {
            if (Board.boardPieces[targetRow][targetCol] == null)
                // basic rook movement
                if (targetCol == preCol || targetRow == preRow) 
                        return true;
        }

        return false;
    }
}
