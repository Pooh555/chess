package pieces;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

import main.Board;

public class Piece {
    public BufferedImage image;
    public int x, y;
    public int col, row, preCol, preRow;
    public int color;

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

    // get coordinate x
    public int getX(int col) {
        return col * Board.SQUARE_SIZE;
    }

    // get coordinate y
    public int getY(int row) {
        return row * Board.SQUARE_SIZE;
    }

    // get column
    public int getCol(int x) {
        return (x + Board.HALF_SQUARE_SIZE) / Board.SQUARE_SIZE;
    }

    // get row
    public int getRow(int y) {
        return (y + Board.HALF_SQUARE_SIZE) / Board.SQUARE_SIZE;
    }

    // draw the pieces
    public void draw(Graphics2D g2) {
        g2.drawImage(image, x, y, Board.SQUARE_SIZE, Board.SQUARE_SIZE, null); // draw image at (x, y)
    }
}