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
        if (isWithinBoard(targetCol, targetRow) && !isInitialSquare(targetCol, targetRow)) {
            if (isEmptySquare(targetCol, targetRow)) {
                // basic king movement
                if (Math.abs(targetCol - this.preCol) + Math.abs(targetRow - this.preRow) == 1
                        || Math.abs((targetCol - this.preCol) * (targetRow - this.preRow)) == 1)
                    return true;
                if (canCastle(targetCol, targetRow));
                    return true;
            } else {
                // capture
                if (Math.abs(targetCol - this.preCol) + Math.abs(targetRow - this.preRow) == 1
                        || Math.abs((targetCol - this.preCol) * (targetRow - this.preRow)) == 1)
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
                // basic king movement
                if (Math.abs(targetCol - this.preCol) + Math.abs(targetRow - this.preRow) == 1
                        || Math.abs((targetCol - this.preCol) * (targetRow - this.preRow)) == 1)
                    return true;
            } else {
                if (Math.abs(targetCol - this.preCol) + Math.abs(targetRow - this.preRow) == 1
                        || Math.abs((targetCol - this.preCol) * (targetRow - this.preRow)) == 1)
                    return true;
            }
        }

        return false;
    }

    @Override
    public boolean canCastle(int targetCol, int targetRow) {
        if (isWithinBoard(targetCol, targetRow) && !isInitialSquare(targetCol, targetRow)) {
            // white
            if (this.hasMoved == false && this.color == false && Board.boardPieces[targetCol + 1][targetRow] != null) {
                // short castle
                if (targetCol == 6 && targetRow == 7 && Board.boardPieces[targetCol + 1][targetRow].hasMoved != true && Board.boardPieces[targetCol + 1][targetRow].color == false)
                    return true;
                // long castle
                if (targetCol == 2 && targetRow == 7 && Board.boardPieces[targetCol - 2][targetRow].hasMoved != true && Board.boardPieces[targetCol + 1][targetRow].color == false)
                    return true;
            }
            // black
            if (this.hasMoved == false && this.color == true) {
                // short castle
                if (targetCol == 6 && targetRow == 0 && Board.boardPieces[targetCol + 1][targetRow].hasMoved != true && Board.boardPieces[targetCol + 1][targetRow].color == true)
                    return true;
                // long castle
                if (targetCol == 2 && targetRow == 0 && Board.boardPieces[targetCol - 2][targetRow].hasMoved != true && Board.boardPieces[targetCol + 1][targetRow].color == true)
                    return true;
            }
        }

        return false;
    }
}
