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
                if (canCastle(targetCol, targetRow))
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
        if (isWithinBoard(targetCol, targetRow) && !isInitialSquare(targetCol, targetRow)
                && isObstacleOnStraightLine(targetCol, targetRow) == false) {
            // white
            if (this.hasMoved == false && this.color == false) {
                // short castle
                if (targetCol + 1 >= 0 && targetCol + 1 < 8 && Board.boardPieces[targetCol + 1][targetRow] != null)
                    if (targetCol == 6 && targetRow == 7 && Board.boardPieces[targetCol + 1][targetRow].hasMoved != true)
                        if (Board.boardOccupiedByBlack[7][5] == 0 && Board.boardOccupiedByBlack[7][6] == 0)
                            return true;
                // long castle
                if (targetCol - 2 >= 0 && targetCol - 2 < 8 && Board.boardPieces[targetCol - 2][targetRow] != null)
                    if (targetCol == 2 && targetRow == 7 && Board.boardPieces[targetCol - 2][targetRow].hasMoved != true)
                        if (Board.boardOccupiedByBlack[7][2] == 0 && Board.boardOccupiedByBlack[7][3] == 0)
                            return true;
            }
            // black
            if (this.hasMoved == false && this.color == true) {
                // short castle
                if (targetCol + 1 >= 0 && targetCol + 1 < 8 && Board.boardPieces[0][7] != null)
                    if (targetCol == 6 && targetRow == 0 && Board.boardPieces[0][7].hasMoved != true) 
                        if (Board.boardOccupiedByWhite[0][5] == 0 && Board.boardOccupiedByWhite[0][6] == 0)
                            return true;
                // long castle
                if (targetCol - 2 >= 0 && targetCol - 2 < 8 && Board.boardPieces[0][0] != null)
                    if (targetCol == 2 && targetRow == 0 && Board.boardPieces[0][0].hasMoved != true) {
                        System.out.println("NIGGA");
                        if (Board.boardOccupiedByWhite[0][2] == 0 && Board.boardOccupiedByWhite[0][3] == 0)
                            return true;
                    }
            }
        }

        return false;
    }
}
