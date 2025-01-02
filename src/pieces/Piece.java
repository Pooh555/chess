package pieces;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import main.Board;

public class Piece {
    public BufferedImage image; // piece icon
    public int x, y; // piece's position in pixels
    public int col, row, preCol, preRow; // piece's position in row and column
    public boolean color; // piece's color

    public Piece(boolean color, int col, int row) {
        this.color = color;
        this.col = col;
        this.row = row;

        x = getX(col);
        y = gety(row);

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

    public int gety(int row) {
        return row * Board.SQUARE_SIZE;
    }

    public void draw(Graphics2D g2) {
        x = getX(col);
        y = gety(row);

        g2.drawImage(image, x, y, Board.SQUARE_SIZE, Board.SQUARE_SIZE, null);
    }
}
