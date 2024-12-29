package pieces;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

import main.Board;
import main.GamePanel;
import main.Type;

public class Piece {
    public Type type;
    public BufferedImage image;
    public int x, y;
    public int col, row, preCol, preRow;
    public int color;
    public Piece hitP;
    public boolean hasMoved, twoSteps;

    public Piece(int color, int col, int row) {
        this.color = color;
        this.col = col;
        this.row = row;

        x = getX(col);
        y = getY(row);

        preCol = col;
        preRow = row;
    }

    // get image
    public BufferedImage getImage(String imagePath) {
        BufferedImage image = null;

        try {
            System.out.println("/res" + imagePath + ".png");
            image = ImageIO.read(getClass().getResourceAsStream("/res" + imagePath + ".png"));
        } catch (IOException e) {
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

    public void updatePosition() {
        // check for en passent
        if (type == Type.PAWN) 
            if (Math.abs(row - preRow) == 2) 
                twoSteps = true;

        x = getX(col);
        y = getY(row);
        preCol = getCol(x);
        preRow = getRow(y);
        hasMoved = true;
    }

    public void resetPosition() {
        col = preCol;
        row = preRow;
        x = getX(col);
        y = getY(row);
    }

    public boolean canMove(int targetCol, int targetRow) {
        return false;
    }

    public boolean isWithinBoard(int targetCol, int targetRow) {
        return targetCol >= 0 && targetCol <= 7 && targetRow >= 0 && targetRow <= 7;
    }

    public boolean isSameSquare(int targetCol, int targetRow) {
        return targetCol == preCol && targetRow == preRow;
    }

    public Piece getHitP(int targetCol, int targetRow) {
        for (Piece piece : GamePanel.simPieces) {
            if (piece.col == targetCol && piece.row == targetRow && piece != this) {
                return piece;
            }
        }

        return null;
    }

    public boolean isValidSquare(int targetCol, int targetRow) {
        hitP = getHitP(targetCol, targetRow);

        if (hitP == null)
            return true; // this square is empty
        else if (hitP.color != this.color)
            return true; // can be captured if the color is different
        else
            hitP = null;

        return false;
    }

    public boolean pieceIsOnStraightLine(int targetCol, int targetRow) {
        // to the left
        for (int i = preCol - 1; i > targetCol; i--) {
            for (Piece piece : GamePanel.simPieces) {
                if (piece.col == i && piece.row == targetRow) {
                    hitP = piece;
                    return true;
                }
            }
        }

        // to the right
        for (int i = preCol + 1; i < targetCol; i++) {
            for (Piece piece : GamePanel.simPieces) {
                if (piece.col == i && piece.row == targetRow) {
                    hitP = piece;
                    return true;
                }
            }
        }

        // to the top
        for (int i = preRow - 1; i > targetRow; i--) {
            for (Piece piece : GamePanel.simPieces) {
                if (piece.col == targetCol && piece.row == i) {
                    hitP = piece;
                    return true;
                }
            }
        }

        // to the bottom
        for (int i = preRow + 1; i < targetRow; i++) {
            for (Piece piece : GamePanel.simPieces) {
                if (piece.col == targetCol && piece.row == i) {
                    hitP = piece;
                    return true;
                }
            }
        }

        return false;
    }

    public boolean pieceIsOnDiagonalLine(int targetCol, int targetRow) {
        if (targetRow < preRow) {
            // to the top left
            for (int i = preCol - 1; i > targetCol; i--) {
                int diff = Math.abs(i - preCol);

                for (Piece piece : GamePanel.simPieces) {
                    if (piece.col == i && piece.row == preRow - diff) {
                        hitP = piece;
                        return true;
                    }
                }
            }

            // to the top right
            for (int i = preCol + 1; i < targetCol; i++) {
                int diff = Math.abs(i - preCol);

                for (Piece piece : GamePanel.simPieces) {
                    if (piece.col == i && piece.row == preRow - diff) {
                        hitP = piece;
                        return true;
                    }
                }
            }
        }

        if (targetRow > preRow) {
            // to the bottom left
            for (int i = preCol - 1; i > targetCol; i--) {
                int diff = Math.abs(i - preCol);

                for (Piece piece : GamePanel.simPieces) {
                    if (piece.col == i && piece.row == preRow + diff) {
                        hitP = piece;
                        return true;
                    }
                }
            }

            // to the bottom right
            for (int i = preCol + 1; i < targetCol; i++) {
                int diff = Math.abs(i - preCol);

                for (Piece piece : GamePanel.simPieces) {
                    if (piece.col == i && piece.row == preRow + diff) {
                        hitP = piece;
                        return true;
                    }
                }
            }

        }

        return false;
    }

    public void draw(Graphics2D g2) {
        g2.drawImage(image, x, y, Board.SQUARE_SIZE, Board.SQUARE_SIZE, null); // draw image at (x, y)
    }
}
