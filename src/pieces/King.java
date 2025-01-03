package pieces;

import main.Board;
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
        if (isWithinBoard(targetCol, targetRow)) {
            // System.out.println("precol: " + this.preCol + ", prerow: " + this.preRow);
            
            if (Board.boardPieces[targetRow][targetCol] == null)
                if (Math.abs(targetCol - this.preCol) + Math.abs(targetRow - this.preRow) == 1
                        || Math.abs((targetCol - this.preCol) * (targetRow - this.preRow)) == 1)
                    return true;
        }

        return false;
    }
}
