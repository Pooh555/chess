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
            if (GamePanel.currentColor == GamePanel.WHITE) {
                if (Board.boardOccupiedByBlack[targetRow][targetCol] == 0) {
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
            }
            if (GamePanel.currentColor == GamePanel.BLACK) {
                if (Board.boardOccupiedByWhite[targetRow][targetCol] == 0) {
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
    public boolean canMoveSim() {
        if (GamePanel.currentColor == GamePanel.WHITE) {
            // System.out.println("Looking for checkmate for white.");

            if ((this.row - 1 >= 0 && this.row - 1 < Board.MAX_ROW) && (this.col - 1 >= 0 && this.col - 1 < Board.MAX_COL))
                if (Board.boardOccupiedByWhite[this.row - 1][this.col - 1] == 0)
                    if (Board.boardPieces[this.row - 1][this.col - 1] == null)
                        return true;
                    else if (Board.boardPieces[this.row - 1][this.col - 1].color != this.color)
                        return true;
            if ((this.row - 1 >= 0 && this.row - 1 < Board.MAX_ROW) && (this.col >= 0 && this.col < Board.MAX_COL))
                if (Board.boardOccupiedByWhite[this.row - 1][this.col] == 0)
                    if (Board.boardPieces[this.row - 1][this.col] == null)
                        return true;
                    else if (Board.boardPieces[this.row - 1][this.col].color != this.color)
                        return true;
            if ((this.row - 1 >= 0 && this.row - 1 < Board.MAX_ROW) && (this.col + 1 >= 0 && this.col + 1 < Board.MAX_COL))
                if (Board.boardOccupiedByWhite[this.row - 1][this.col + 1] == 0)
                    if (Board.boardPieces[this.row - 1][this.col + 1] == null)
                        return true;
                    else if (Board.boardPieces[this.row - 1][this.col + 1].color != this.color)
                        return true;
            if ((this.row >= 0 && this.row < Board.MAX_ROW) && (this.col - 1 >= 0 && this.col - 1 < Board.MAX_COL))
                if (Board.boardOccupiedByWhite[this.row][this.col - 1] == 0)
                    if (Board.boardPieces[this.row][this.col - 1] == null)
                        return true;
                    else if (Board.boardPieces[this.row][this.col - 1].color != this.color)
                        return true;
            if ((this.row >= 0 && this.row < Board.MAX_ROW) && (this.col + 1 >= 0 && this.col + 1 < Board.MAX_COL))
                if (Board.boardOccupiedByWhite[this.row][this.col + 1] == 0)
                    if (Board.boardPieces[this.row][this.col + 1] == null)
                        return true;
                    else if (Board.boardPieces[this.row][this.col + 1].color != this.color)
                        return true;
            if ((this.row + 1 >= 0 && this.row + 1 < Board.MAX_ROW) && (this.col - 1 >= 0 && this.col - 1 < Board.MAX_COL))
                if (Board.boardOccupiedByWhite[this.row + 1][this.col - 1] == 0)
                    if (Board.boardPieces[this.row + 1][this.col - 1] == null)
                        return true;
                    else if (Board.boardPieces[this.row + 1][this.col - 1].color != this.color)
                        return true;
            if ((this.row + 1 >= 0 && this.row + 1 < Board.MAX_ROW) && (this.col >= 0 && this.col < Board.MAX_COL))
                if (Board.boardOccupiedByWhite[this.row + 1][this.col] == 0)
                    if (Board.boardPieces[this.row + 1][this.col] == null)
                        return true;
                    else if (Board.boardPieces[this.row + 1][this.col].color != this.color)
                        return true;
            if ((this.row + 1 >= 0 && this.row + 1 < Board.MAX_ROW) && (this.col + 1 >= 0 && this.col + 1 < Board.MAX_COL))
                if (Board.boardOccupiedByWhite[this.row + 1][this.col + 1] == 0)
                    if (Board.boardPieces[this.row + 1][this.col + 1] == null)
                        return true;
                    else if (Board.boardPieces[this.row + 1][this.col + 1].color != this.color)
                        return true;
        }
        if (GamePanel.currentColor == GamePanel.BLACK) {
            // System.out.println("Looking for checkmate for black.");

            if ((this.row - 1 >= 0 && this.row - 1 < Board.MAX_ROW) && (this.col - 1 >= 0 && this.col - 1 < Board.MAX_COL))
                if (Board.boardOccupiedByBlack[this.row - 1][this.col - 1] == 0)
                    if (Board.boardPieces[this.row - 1][this.col - 1] == null)
                        return true;
                    else if (Board.boardPieces[this.row - 1][this.col - 1].color != this.color)
                        return true;
            if ((this.row - 1 >= 0 && this.row - 1 < Board.MAX_ROW) && (this.col >= 0 && this.col < Board.MAX_COL))
                if (Board.boardOccupiedByBlack[this.row - 1][this.col] == 0)
                    if (Board.boardPieces[this.row - 1][this.col] == null)
                        return true;
                    else if (Board.boardPieces[this.row - 1][this.col].color != this.color)
                        return true;
            if ((this.row - 1 >= 0 && this.row - 1 < Board.MAX_ROW) && (this.col + 1 >= 0 && this.col + 1 < Board.MAX_COL))
                if (Board.boardOccupiedByBlack[this.row - 1][this.col + 1] == 0)
                    if (Board.boardPieces[this.row - 1][this.col + 1] == null)
                        return true;
                    else if (Board.boardPieces[this.row - 1][this.col + 1].color != this.color)
                        return true;
            if ((this.row >= 0 && this.row < Board.MAX_ROW) && (this.col - 1 >= 0 && this.col - 1 < Board.MAX_COL))
                if (Board.boardOccupiedByBlack[this.row][this.col - 1] == 0)
                    if (Board.boardPieces[this.row][this.col - 1] == null)
                        return true;
                    else if (Board.boardPieces[this.row][this.col - 1].color != this.color)
                        return true;
            if ((this.row >= 0 && this.row < Board.MAX_ROW) && (this.col + 1 >= 0 && this.col + 1 < Board.MAX_COL))
                if (Board.boardOccupiedByBlack[this.row][this.col + 1] == 0)
                    if (Board.boardPieces[this.row][this.col + 1] == null)
                        return true;
                    else if (Board.boardPieces[this.row][this.col + 1].color != this.color)
                        return true;
            if ((this.row + 1 >= 0 && this.row + 1 < Board.MAX_ROW) && (this.col - 1 >= 0 && this.col - 1 < Board.MAX_COL))
                if (Board.boardOccupiedByBlack[this.row + 1][this.col - 1] == 0)
                    if (Board.boardPieces[this.row + 1][this.col - 1] == null)
                        return true;
                    else if (Board.boardPieces[this.row + 1][this.col - 1].color != this.color)
                        return true;
            if ((this.row + 1 >= 0 && this.row + 1 < Board.MAX_ROW) && (this.col >= 0 && this.col < Board.MAX_COL))
                if (Board.boardOccupiedByBlack[this.row + 1][this.col] == 0)
                    if (Board.boardPieces[this.row + 1][this.col] == null)
                        return true;
                    else if (Board.boardPieces[this.row + 1][this.col].color != this.color)
                        return true;
            if ((this.row + 1 >= 0 && this.row + 1 < Board.MAX_ROW) && (this.col + 1 >= 0 && this.col + 1 < Board.MAX_COL))
                if (Board.boardOccupiedByBlack[this.row + 1][this.col + 1] == 0)
                    if (Board.boardPieces[this.row + 1][this.col + 1] == null)
                        return true;
                    else if (Board.boardPieces[this.row + 1][this.col + 1].color != this.color)
                        return true;
        }

        return false;
    }

    @Override
    public boolean canCastle(int targetCol, int targetRow) {

        /*
         * I'm done. I'm just gonna hard code. :P
         */

        if (isWithinBoard(targetCol, targetRow) && !isInitialSquare(targetCol, targetRow)
                && isObstacleOnStraightLine(targetCol, targetRow) == false) {
            // white
            if (this.hasMoved == false && this.color == false) {
                // short castle
                if (targetCol + 1 >= 0 && targetCol + 1 < Board.MAX_ROW && Board.boardPieces[7][7] != null)
                    if (targetCol == 6 && targetRow == 7 && Board.boardPieces[7][7].hasMoved != true)
                        if (Board.boardOccupiedByBlack[7][5] == 0 && Board.boardOccupiedByBlack[7][6] == 0)
                            return true;
                // long castle
                if (targetCol - 2 >= 0 && targetCol - 2 < Board.MAX_ROW && Board.boardPieces[7][0] != null)
                    if (targetCol == 2 && targetRow == 7 && Board.boardPieces[7][0].hasMoved != true)
                        if (Board.boardOccupiedByBlack[7][2] == 0 && Board.boardOccupiedByBlack[7][3] == 0)
                            return true;
            }
            // black
            if (this.hasMoved == false && this.color == true) {
                // short castle
                if (targetCol + 1 >= 0 && targetCol + 1 < Board.MAX_ROW && Board.boardPieces[0][7] != null)
                    if (targetCol == 6 && targetRow == 0 && Board.boardPieces[0][7].hasMoved != true)
                        if (Board.boardOccupiedByWhite[0][5] == 0 && Board.boardOccupiedByWhite[0][6] == 0)
                            return true;
                // long castle
                if (targetCol - 2 >= 0 && targetCol - 2 < Board.MAX_ROW && Board.boardPieces[0][0] != null)
                    if (targetCol == 2 && targetRow == 0 && Board.boardPieces[0][0].hasMoved != true) {
                        if (Board.boardOccupiedByWhite[0][2] == 0 && Board.boardOccupiedByWhite[0][3] == 0)
                            return true;
                    }
            }
        }

        return false;
    }
}
