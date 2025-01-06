package pieces;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import main.Board;
import main.GamePanel;
import main.Type;

public class Piece {
    public Type type; // piece type
    public BufferedImage image; // piece icon
    public int x, y; // piece's position in pixels
    public int col, row, preCol, preRow, count; // piece's position in row and column
    public boolean color, isActive, hasMoved, canBeEnPassent; // piece's , active?, has moved?

    public Piece(boolean color, int col, int row) {
        this.color = color;
        this.col = col;
        this.row = row;

        x = getX(col);
        y = getY(row);

        preCol = col;
        preRow = row;
    }

    public BufferedImage getImage(String imagePath) {
        BufferedImage image = null;

        // load pieces icon to display on the board
        try {
            image = ImageIO.read(getClass().getResourceAsStream("/res/pieces/" + imagePath + ".png"));
            System.out.println("/res/pieces/" + imagePath + ".png is loaded properly.");
        } catch (Exception e) {
            System.out.println("/res/pieces/" + imagePath + ".png cannot be loaded properly.");
            e.printStackTrace();
        }

        return image;
    }

    public int getX(int col) {
        return col * Board.SQUARE_SIZE;
    }

    public int getY(int row) {
        return row * Board.SQUARE_SIZE;
    }

    public int getCol(int x) {
        return (x + Board.HALF_SQUARE_SIZE) / Board.SQUARE_SIZE;
    }

    public int getRow(int y) {
        return (y + Board.HALF_SQUARE_SIZE) / Board.SQUARE_SIZE;
    }

    public int getIndex() {
        for (int index = 0; index < GamePanel.simPieces.size(); index++) {
            if (GamePanel.simPieces.get(index) == this) {
                return index;
            }
        }

        return 0;
    }

    public Piece getAttackingPiece() {
        for (Piece piece : GamePanel.pieces)
            if (piece.canMoveExtended(this.col, this.row)) {
                System.out.println(piece.type + " is attacking " + this.type + '.');

                return piece;
            }

        return null;
    }

    public boolean canMove(int targetCol, int targetRow) {
        return false;
    }

    public boolean canMoveExtended(int targetCol, int targetRow) {
        return false;
    }

    public boolean canMoveSim() {
        return true;
    }

    public boolean canCastle(int targetCol, int targetRow) {
        return false;
    }

    public boolean canBeCaptured(Piece attackingPiece) {
        // for (Piece piece : GamePanel.pieces)
        if (attackingPiece.color == GamePanel.BLACK)
            if (Board.boardCanMoveByWhite[attackingPiece.row][attackingPiece.col] == 1)
                return true;
        if (attackingPiece.color == GamePanel.WHITE)
            if (Board.boardCanMoveByBlack[attackingPiece.row][attackingPiece.col] == 1)
                return true;
        // if (piece.type == Type.PAWN) {
        // if (piece.canMoveExtended(attackingPiece.col, attackingPiece.row))
        // return true;
        // }
        // else {
        // if (piece.canMove(attackingPiece.col, attackingPiece.row)) {
        // System.out.println(piece.type);
        // return true;
        // }
        // }

        return false;
    }

    public boolean canBeBlocked(Piece attackingPiece) {
        if (canBeBlockedDiagonally(attackingPiece))
            return canBeBlockedDiagonally(attackingPiece);
        if (canBeBlockedStraight(attackingPiece))
            return canBeBlockedStraight(attackingPiece);

        return false;
    }

    public boolean canBeBlockedDiagonally(Piece checkingPiece) {
        if (this.color == GamePanel.BLACK) {
            if (checkingPiece.row < this.row && checkingPiece.col < this.col) {
                // the cheking piece is to the top left
                for (int i = this.col - 1; i > checkingPiece.col; i--) {
                    int diff = Math.abs(i - preCol);

                    if (Board.boardCanMoveByBlack[this.row - diff][this.col - diff] == 2)
                        return true;
                }
            }
            if (checkingPiece.row < this.row && checkingPiece.col > this.col) {
                // the cheking piece is to the top left
                for (int i = this.col + 1; i < checkingPiece.col; i++) {
                    int diff = Math.abs(i - preCol);

                    if (Board.boardCanMoveByBlack[this.row - diff][this.col + diff] == 2)
                        return true;
                }
            }
            if (checkingPiece.row > this.row && checkingPiece.col < this.col) {
                // the cheking piece is to the bottom left
                for (int i = this.col - 1; i > checkingPiece.col; i--) {
                    int diff = Math.abs(i - preCol);

                    if (Board.boardCanMoveByBlack[this.row + diff][this.col - diff] == 2)
                        return true;
                }
            }
            if (checkingPiece.row > this.row && checkingPiece.col > this.col) {
                // the cheking piece is to the top left
                for (int i = this.col + 1; i < checkingPiece.col; i++) {
                    int diff = Math.abs(i - preCol);

                    if (Board.boardCanMoveByBlack[this.row + diff][this.col + diff] == 2)
                        return true;
                }
            }
        }
        if (this.color == GamePanel.WHITE) {
            if (checkingPiece.row < this.row && checkingPiece.col < this.col) {
                // the cheking piece is to the top left
                for (int i = this.col - 1; i > checkingPiece.col; i--) {
                    int diff = Math.abs(i - preCol);

                    if (Board.boardCanMoveByWhite[this.row - diff][this.col - diff] == 2)
                        return true;
                }
            }
            if (checkingPiece.row < this.row && checkingPiece.col > this.col) {
                // the cheking piece is to the top left
                for (int i = this.col + 1; i < checkingPiece.col; i++) {
                    int diff = Math.abs(i - preCol);

                    if (Board.boardCanMoveByWhite[this.row - diff][this.col + diff] == 2)
                        return true;
                }
            }
            if (checkingPiece.row > this.row && checkingPiece.col < this.col) {
                // the cheking piece is to the bottom left
                for (int i = this.col - 1; i > checkingPiece.col; i--) {
                    int diff = Math.abs(i - preCol);

                    if (Board.boardCanMoveByWhite[this.row + diff][this.col - diff] == 2)
                        return true;
                }
            }
            if (checkingPiece.row > this.row && checkingPiece.col > this.col) {
                // the cheking piece is to the top left
                for (int i = this.col + 1; i < checkingPiece.col; i++) {
                    int diff = Math.abs(i - preCol);

                    return true;
                }
            }
        }

        return false;
    }

    public boolean canBeBlockedStraight(Piece checkingPiece) {
        if (this.color == GamePanel.BLACK) {
            // the attacking piece is to the left
            if (this.col > checkingPiece.col && this.row == checkingPiece.row)
                for (int i = checkingPiece.col + 1; i < this.col; i++)
                    if (Board.boardCanMoveByBlack[this.row][i] == 2)
                        return true;
            // the attacking piece is to the right
            if (this.col < checkingPiece.col && this.row == checkingPiece.row)
                for (int i = checkingPiece.col - 1; i > this.col; i--)
                    if (Board.boardCanMoveByBlack[this.row][i] == 2)
                        return true;
            // the attacking piece is to the top
            if (this.col == checkingPiece.col && this.row > checkingPiece.row)
                for (int i = checkingPiece.row + 1; i < this.row; i++)
                    if (Board.boardCanMoveByBlack[i][this.col] == 2)
                        return true;
            // the attacking piece is to the bottom
            if (this.col == checkingPiece.col && this.row < checkingPiece.row)
                for (int i = checkingPiece.row - 1; i > this.row; i--)
                    if (Board.boardCanMoveByBlack[i][this.col] == 2)
                        return true;
        }
        if (this.color == GamePanel.WHITE) {
            // the attacking piece is to the left
            if (this.col > checkingPiece.col && this.row == checkingPiece.row)
                for (int i = checkingPiece.col + 1; i < this.col; i++)
                    if (Board.boardCanMoveByWhite[this.row][i] == 1)
                        return true;
            // the attacking piece is to the right
            if (this.col < checkingPiece.col && this.row == checkingPiece.row)
                for (int i = checkingPiece.col - 1; i > this.col; i--)
                    if (Board.boardCanMoveByWhite[this.row][i] == 1)
                        return true;
            // the attacking piece is to the top
            if (this.col == checkingPiece.col && this.row > checkingPiece.row)
                for (int i = checkingPiece.row + 1; i < this.row; i++)
                    if (Board.boardCanMoveByWhite[i][this.col] == 1)
                        return true;
            // the attacking piece is to the bottom
            if (this.col == checkingPiece.col && this.row < checkingPiece.row)
                for (int i = checkingPiece.row - 1; i > this.row; i--)
                    if (Board.boardCanMoveByWhite[i][this.col] == 1)
                        return true;
        }

        return false;
    }

    public boolean isWithinBoard(int targetCol, int targetRow) {
        if (targetCol >= 0 && targetCol <= 7 && targetRow >= 0 && targetRow <= 7)
            return true;
        else
            System.out.println("Exception: Piece is out of bound.");

        return false;
    }

    public boolean isInitialSquare(int targetCol, int targetRow) {
        return targetCol == preCol && targetRow == preRow;
    }

    public boolean isEmptySquare(int targetCol, int targetRow) {
        return Board.boardPieces[targetRow][targetCol] == null;
    }

    public boolean isCapturable(int targetCol, int targetRow) {
        Piece targetPiece = Board.boardPieces[targetRow][targetCol];

        if (targetPiece != null && targetPiece.color != GamePanel.currentColor
                && targetPiece.type != Type.KING)
            return true;

        return false;
    }

    public boolean isPieceUnderAttack() {
        // System.out.println(this.color);
        // System.out.println("col: " + this.col + ", row: " + this.row);

        if (this.color == GamePanel.WHITE)
            if (Board.boardOccupiedByBlack[this.row][this.col] == 2) {
                // System.out.println(Board.boardOccupiedByBlack[this.row][this.col]);
                return true;
            }
        if (this.color == GamePanel.BLACK) {
            if (Board.boardOccupiedByWhite[this.row][this.col] == 1) {
                // System.out.println(Board.boardOccupiedByWhite[this.row][this.col]);
                return true;
            }
        }

        return false;
    }

    public boolean isPieceUnderDoubleAttack() {
        count = 0;

        if (isPieceUnderAttack()) {
            for (Piece piece : GamePanel.simPieces) {
                if (piece.color != this.color && piece.canMoveExtended(this.col, this.row))
                    count++;
                if (count == 2)
                    return true;
            }
        }

        return false;
    }

    public boolean isObstacleOnDiagonalLine(int targetCol, int targetRow) {
        if (targetRow < preRow) {
            // to the top left
            for (int i = preCol - 1; i > targetCol; i--) {
                int diff = Math.abs(i - preCol);

                for (Piece piece : GamePanel.simPieces)
                    if (piece.col == i && piece.row == preRow - diff)
                        return true;
            }

            // to the top right
            for (int i = preCol + 1; i < targetCol; i++) {
                int diff = Math.abs(i - preCol);

                for (Piece piece : GamePanel.simPieces)
                    if (piece.col == i && piece.row == preRow - diff)
                        return true;
            }
        }

        if (targetRow > preRow) {
            // to the bottom left
            for (int i = preCol - 1; i > targetCol; i--) {
                int diff = Math.abs(i - preCol);

                for (Piece piece : GamePanel.simPieces)
                    if (piece.col == i && piece.row == preRow + diff)
                        return true;

            }

            // to the bottom right
            for (int i = preCol + 1; i < targetCol; i++) {
                int diff = Math.abs(i - preCol);

                for (Piece piece : GamePanel.simPieces)
                    if (piece.col == i && piece.row == preRow + diff)
                        return true;
            }

        }

        return false;
    }

    public boolean isObstacleOnDiagonalLineExtended(int targetCol, int targetRow) {
        if (targetRow < preRow) {
            // to the top left
            for (int i = preCol - 1; i > targetCol; i--) {
                int diff = Math.abs(i - preCol);

                for (Piece piece : GamePanel.simPieces)
                    if (piece.col == i && piece.row == preRow - diff
                            && (piece.type != Type.KING && piece.color != GamePanel.currentColor))
                        return true;
            }

            // to the top right
            for (int i = preCol + 1; i < targetCol; i++) {
                int diff = Math.abs(i - preCol);

                for (Piece piece : GamePanel.simPieces)
                    if (piece.col == i && piece.row == preRow - diff
                            && (piece.type != Type.KING && piece.color != GamePanel.currentColor))
                        return true;
            }
        }

        if (targetRow > preRow) {
            // to the bottom left
            for (int i = preCol - 1; i > targetCol; i--) {
                int diff = Math.abs(i - preCol);

                for (Piece piece : GamePanel.simPieces)
                    if (piece.col == i && piece.row == preRow + diff
                            && (piece.type != Type.KING && piece.color != GamePanel.currentColor))
                        return true;

            }

            // to the bottom right
            for (int i = preCol + 1; i < targetCol; i++) {
                int diff = Math.abs(i - preCol);

                for (Piece piece : GamePanel.simPieces)
                    if (piece.col == i && piece.row == preRow + diff
                            && (piece.type != Type.KING && piece.color != GamePanel.currentColor))
                        return true;
            }

        }

        return false;
    }

    public boolean isObstacleOnStraightLine(int targetCol, int targetRow) {
        // to the left
        for (int i = preCol - 1; i > targetCol; i--) {
            for (Piece piece : GamePanel.simPieces) {
                if (piece.col == i && piece.row == targetRow)
                    return true;
            }
        }

        // to the right
        for (int i = preCol + 1; i < targetCol; i++) {
            for (Piece piece : GamePanel.simPieces) {
                if (piece.col == i && piece.row == targetRow)
                    return true;
            }
        }

        // to the top
        for (int i = preRow - 1; i > targetRow; i--) {
            for (Piece piece : GamePanel.simPieces) {
                if (piece.col == targetCol && piece.row == i)
                    return true;
            }
        }

        // to the bottom
        for (int i = preRow + 1; i < targetRow; i++) {
            for (Piece piece : GamePanel.simPieces) {
                if (piece.col == targetCol && piece.row == i)
                    return true;
            }
        }

        return false;
    }

    public boolean isObstacleOnStraightLineExtended(int targetCol, int targetRow) {
        // to the left
        for (int i = preCol - 1; i > targetCol; i--) {
            for (Piece piece : GamePanel.simPieces) {
                if (piece.col == i && piece.row == targetRow
                        && (piece.type != Type.KING && piece.color != GamePanel.currentColor))
                    return true;
            }
        }

        // to the right
        for (int i = preCol + 1; i < targetCol; i++) {
            for (Piece piece : GamePanel.simPieces) {
                if (piece.col == i && piece.row == targetRow
                        && (piece.type != Type.KING && piece.color != GamePanel.currentColor))
                    return true;
            }
        }

        // to the top
        for (int i = preRow - 1; i > targetRow; i--) {
            for (Piece piece : GamePanel.simPieces) {
                if (piece.col == targetCol && piece.row == i
                        && (piece.type != Type.KING && piece.color != GamePanel.currentColor))
                    return true;
            }
        }

        // to the bottom
        for (int i = preRow + 1; i < targetRow; i++) {
            for (Piece piece : GamePanel.simPieces) {
                if (piece.col == targetCol && piece.row == i
                        && (piece.type != Type.KING && piece.color != GamePanel.currentColor))
                    return true;
            }
        }

        return false;
    }

    public void resetEnPassentState() {
        canBeEnPassent = false;
    }

    public void updatePosition() {
        // used to check for en passent in the next tempo
        if (type == Type.PAWN)
            if (Math.abs(row - preRow) == 2)
                canBeEnPassent = true;

        x = getX(col);
        y = getY(row);
        preCol = getCol(x);
        preRow = getRow(y);
    }

    public void resetPosition() {
        col = preCol;
        row = preRow;
        x = getX(col);
        y = getY(row);
    }

    public void draw(Graphics2D g2) {
        if (isActive)
            g2.drawImage(image, x, y, Board.SQUARE_SIZE, Board.SQUARE_SIZE, null);
        else {
            x = getX(col);
            y = getY(row);

            g2.drawImage(image, x, y, Board.SQUARE_SIZE, Board.SQUARE_SIZE, null);
        }
    }
}
