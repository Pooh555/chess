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
    public int col, row, preCol, preRow; // piece's position in row and column
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

    /*
     * public Piece getHitPiece(int targetCol, int targetRow, ArrayList<Piece>
     * simPieces) {
     * for (Piece piece : simPieces)
     * if (piece == Board.boardPieces[targetRow][targetCol])
     * return piece;
     * 
     * return null;
     * }
     */

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

    public boolean canMove(int targetCol, int targetRow) {
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
