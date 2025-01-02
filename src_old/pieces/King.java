package pieces;

import main.GamePanel;
import main.Type;

public class King extends Piece {
    public King(int color, int col, int row) {
        super(color, col, row);

        type = Type.KING;

        if (color == GamePanel.WHITE)
            image = getImage("/pieces/w-king");
        else
            image = getImage("/pieces/b-king");
    }

    public boolean canMove(int targetCol, int targetRow) {
        // movement
        if (isWithinBoard(targetCol, targetRow) && isSameSquare(targetCol, targetRow) == false)
            if (Math.abs(targetCol - preCol) + Math.abs(targetRow - preRow) == 1
                    || Math.abs(targetCol - preCol) * Math.abs(targetRow - preRow) == 1)
                if (isValidSquare(targetCol, targetRow))
                    return true;

        // castling
        if (hasMoved == false) {
            // short castling
            if (targetCol == preCol + 2 && targetRow == preRow
                    && pieceIsOnStraightLine(targetCol, targetRow) == false) {
                for (Piece piece : GamePanel.simPieces) {
                    if (piece.col == preCol + 3 && piece.row == preRow && piece.hasMoved == false) {
                        GamePanel.castlingP = piece;
                        return true;
                    }
                }
            }

            // long castling
            if (targetCol == preCol - 2 && targetRow == preRow
                    && pieceIsOnStraightLine(targetCol, targetRow) == false) {
                Piece p[] = new Piece[2];
                for (Piece piece : GamePanel.simPieces) {
                    if (piece.col == preCol - 3 && piece.row == targetRow)
                        p[0] = piece;
                    if (piece.col == preCol - 4 && piece.row == targetRow)
                        p[1] = piece;
                    if(p[0] == null && p[1] != null && p[1].hasMoved == false) {
                        GamePanel.castlingP = p[1];
                        return true;
                    }
                }
            }
        }

        return false;
    }
}
