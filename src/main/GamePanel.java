package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;

import pieces.Bishop;
import pieces.King;
import pieces.Knight;
import pieces.Pawn;
import pieces.Piece;
import pieces.Queen;
import pieces.Rook;

import java.util.ArrayList;

public class GamePanel extends JPanel implements Runnable {
    // GAME INFO
    public static final int WIDTH = 1100; // panel's width
    public static final int HEIGHT = 800; // panel's height
    public int FPS = 360; // FPS
    Thread gameThread;
    Board board = new Board(); // initialize board
    Mouse mouse = new Mouse(); // initialize mouse

    // COLOR
    public static final Color BACKGROUND_COLOR = new Color(0, 0, 0);
    public static final int WHITE = 0;
    public static final int BLACK = 1;
    int currentColor = WHITE;

    // PIECES
    public static ArrayList<Piece> pieces = new ArrayList<>();
    public static ArrayList<Piece> simPieces = new ArrayList<>();
    Piece activeP; // active piece

    public GamePanel() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(BACKGROUND_COLOR);
        System.out.println("board initialized");
        addMouseMotionListener(mouse);
        addMouseListener(mouse);
        System.out.println("mouse initialized");
        setPieces();
        copyPieces(pieces, simPieces);
        System.out.println("pieces initialized");
    }

    public void launchGame() {
        gameThread = new Thread(this);
        gameThread.start();

        System.out.println("Chess launched");
    }

    public void setPieces() {
        // white side
        pieces.add(new Pawn(WHITE, 0, 6));
        pieces.add(new Pawn(WHITE, 1, 6));
        pieces.add(new Pawn(WHITE, 2, 6));
        pieces.add(new Pawn(WHITE, 3, 6));
        pieces.add(new Pawn(WHITE, 4, 6));
        pieces.add(new Pawn(WHITE, 5, 6));
        pieces.add(new Pawn(WHITE, 6, 6));
        pieces.add(new Pawn(WHITE, 7, 6));
        pieces.add(new Knight(WHITE, 1, 7));
        pieces.add(new Knight(WHITE, 6, 7));
        pieces.add(new Bishop(WHITE, 2, 7));
        pieces.add(new Bishop(WHITE, 5, 7));
        pieces.add(new Rook(WHITE, 0, 7));
        pieces.add(new Rook(WHITE, 7, 7));
        pieces.add(new Queen(WHITE, 3, 7));
        pieces.add(new King(WHITE, 4, 7));

        // black side
        pieces.add(new Pawn(BLACK, 0, 1));
        pieces.add(new Pawn(BLACK, 1, 1));
        pieces.add(new Pawn(BLACK, 2, 1));
        pieces.add(new Pawn(BLACK, 3, 1));
        pieces.add(new Pawn(BLACK, 4, 1));
        pieces.add(new Pawn(BLACK, 5, 1));
        pieces.add(new Pawn(BLACK, 6, 1));
        pieces.add(new Pawn(BLACK, 7, 1));
        pieces.add(new Knight(BLACK, 1, 0));
        pieces.add(new Knight(BLACK, 6, 0));
        pieces.add(new Bishop(BLACK, 2, 0));
        pieces.add(new Bishop(BLACK, 5, 0));
        pieces.add(new Rook(BLACK, 0, 0));
        pieces.add(new Rook(BLACK, 7, 0));
        pieces.add(new Queen(BLACK, 3, 0));
        pieces.add(new King(BLACK, 4, 0));
    }

    private void copyPieces(ArrayList<Piece> source, ArrayList<Piece> target) {
        target.clear();

        for (int i = 0; i < source.size(); i++) {
            target.add(source.get(i));
        }
    }

    @Override
    public void run() {

        // game loop
        double drawInterval = 1000000000 / FPS; // 1 tick
        double delta = 0; // how much time hass passed since the last render
        long lastTime = System.nanoTime(); // time of the last render
        long currentTime; // current time

        while (gameThread != null) {
            currentTime = System.nanoTime();

            delta += (currentTime - lastTime) / drawInterval;
            lastTime = currentTime;

            if (delta >= 1) {
                update();
                repaint();
                delta--;
            }
        }

    }

    private void update() {
        if (mouse.isPressed) {
            if (activeP == null) {
                for (Piece piece : simPieces) {
                    if (piece.color == currentColor
                            && piece.col == mouse.x / Board.SQUARE_SIZE
                            && piece.row == mouse.y / Board.SQUARE_SIZE)
                        activeP = piece;
                }
            } else {
                // when the player is holding a piece, simulate piece movement
                simulate();
            }
        }
    }

    private void simulate() {
        // update the piece's position when being held
        activeP.x = mouse.x - Board.HALF_SQUARE_SIZE;
        activeP.y = mouse.y - Board.HALF_SQUARE_SIZE;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g; // convert Graphics g into Graphics2D because the function "draw" accepts g as
                                        // Graphics2D object

        // board
        board.draw(g2);

        // pieces
        for (Piece p : simPieces) {
            p.draw(g2);
        }
    }
}
