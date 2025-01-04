package main;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;

import pieces.Piece;

public class Board {
    public final static int MAX_COL = 8; // a chessboard consists of 8 columns
    public final static int MAX_ROW = 8; // a chessboard consists of 8 rows
    public static Piece[][] boardPieces = new Piece[MAX_ROW][MAX_COL]; // an array to store pieces positions on the
                                                                       // board
    public static int[][] boardOccupiedByWhite = new int[MAX_ROW][MAX_COL]; // a simulation board for white
    public static int[][] boardOccupiedByBlack = new int[MAX_ROW][MAX_COL]; // a simulation board for black
    public static int SQUARE_SIZE = GamePanel.HEIGHT / 10;
    public static int HALF_SQUARE_SIZE = SQUARE_SIZE / 2;
    static final Color LIGHT_SQUARE_COLOR = new Color(210, 165, 125);
    static final Color DARK_SQUARE_COLOR = new Color(175, 115, 70);

    public void clearBoard() {
        for (int row = 0; row < MAX_ROW; row++)
            for (int col = 0; col < MAX_COL; col++)
                boardPieces[row][col] = null;
    }

    public void clearOccupiedBoard() {
        for (int row = 0; row < MAX_ROW; row++)
            for (int col = 0; col < MAX_COL; col++) {
                boardOccupiedByWhite[row][col] = 0;
                boardOccupiedByBlack[row][col] = 0;
            }
    }

    public void updatePiecePositions(ArrayList<Piece> pieces) {
        for (Piece piece : pieces)
            boardPieces[piece.row][piece.col] = piece;
    }

    public void draw(Graphics2D g2) {
        // user boolean for performance
        boolean count = false; // false: light square, true: dark square

        // set UI
        if (GamePanel.WIDTH - GamePanel.HEIGHT > GamePanel.WIDTH / 4)
            SQUARE_SIZE = GamePanel.HEIGHT / 8;
        else
            SQUARE_SIZE = GamePanel.WIDTH / 10;
        HALF_SQUARE_SIZE = SQUARE_SIZE / 2;

        // set board color for each square
        for (int row = 0; row < MAX_ROW; row++) {
            for (int col = 0; col < MAX_COL; col++) {
                if (count == false)
                    g2.setColor(LIGHT_SQUARE_COLOR);
                else
                    g2.setColor(DARK_SQUARE_COLOR);

                count = !count;

                g2.fillRect(col * SQUARE_SIZE, row * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);
            }

            count = !count;
        }
    }

    public void updateOccupiedTerratory() {
        /*
         * imaginary color for pieces's terratory for each color
         * used for detecting check, checkmate, and illegal move
         * 0: unoccupied
         * 1: occupied by white
         * 2: occupied by black
         */
        clearOccupiedBoard();

        for (Piece piece : GamePanel.pieces) {
            for (int row = 0; row < MAX_ROW; row++)
                for (int col = 0; col < MAX_COL; col++) {
                    // check for white's occupied squares
                    if (piece.canMoveExtended(col, row) && piece.color == false)
                        boardOccupiedByWhite[row][col] = 1;
                    // check for black's occupied squares
                    if (piece.canMoveExtended(col, row) && piece.color == true)
                        boardOccupiedByBlack[row][col] = 2;
                }
        }
    }

    // debug
    public void printBoard() {
        for (int row = 0; row < MAX_ROW; row++) {
            for (int col = 0; col < MAX_COL; col++) {
                if (boardPieces[row][col] != null)
                    System.out.print(boardPieces[row][col].type + " ");
                else
                    System.out.print("    ");
            }

            System.out.println();
        }

        System.out.println("-----------------------------------------------------");
    }

    public void printOccupiedBoard() {
        for (int row = 0; row < MAX_ROW; row++) {
            for (int col = 0; col < MAX_COL; col++)
                System.out.print(boardOccupiedByWhite[row][col] + " ");

            System.out.println();
        }

        System.out.println("-----------------------------------------------------");

        for (int row = 0; row < MAX_ROW; row++) {
            for (int col = 0; col < MAX_COL; col++)
                System.out.print(boardOccupiedByBlack[row][col] + " ");

            System.out.println();
        }

        System.out.println("-----------------------------------------------------");
        System.out.println("-----------------------------------------------------");
    }
}
