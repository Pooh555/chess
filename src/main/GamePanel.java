package main;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JPanel;

import pieces.Bishop;
import pieces.King;
import pieces.Knight;
import pieces.Pawn;
import pieces.Piece;
import pieces.Queen;
import pieces.Rook;

import java.util.ArrayList;
import javax.print.attribute.standard.CopiesSupported;

public class GamePanel extends JPanel implements Runnable {
    // GAME INFO
    public static final int WIDTH = 1100; // panel's width
    public static final int HEIGHT = 800; // panel's height
    public int FPS = 360; // FPS
    Thread gameThread;
    Board board = new Board(); // initialize board
    Mouse mouse = new Mouse(); // initialize mouse

    // color
    public static final Color BACKGROUND_COLOR = new Color(0, 0, 0);
    public static final Color FOREGROUND_TEXT_COLOR = new Color(255, 201, 201);
    public static final int WHITE = 0;
    public static final int BLACK = 1;
    int currentColor = WHITE;

    // pieces
    public static ArrayList<Piece> pieces = new ArrayList<>();
    public static ArrayList<Piece> simPieces = new ArrayList<>();
    ArrayList<Piece> promotionPieces = new ArrayList<>();
    Piece activeP; // active piece
    public static Piece castlingP; // castling piece

    // booelan
    boolean canMove;
    boolean validSquare;
    boolean promotion;

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
        if (promotion) {
            promoting();
        } else {
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
            if (mouse.isPressed == false) {
                if (activeP != null) {
                    if (validSquare) {

                        // move confirmed

                        // remove the captured piece from the board during simulation phase
                        copyPieces(simPieces, pieces);
                        activeP.updatePosition();
                        checkCastling();

                        if (castlingP != null)
                            castlingP.updatePosition();

                        if (canPromote())
                            promotion = true;
                        else
                            changeTurn();
                    } else {
                        // invalid move, reset all states
                        copyPieces(pieces, simPieces);
                        activeP.resetPosition();
                        activeP = null;
                    }
                }
            }
        }
    }

    private void simulate() {
        canMove = false;
        validSquare = false;

        // reset pieces list every loop to restore the removed piece
        copyPieces(pieces, simPieces);

        // reset the castling piece's position
        if (castlingP != null) {
            castlingP.col = castlingP.preCol;
            castlingP.x = castlingP.getX(castlingP.col);
            castlingP = null;
        }

        // update the piece's position when being held
        activeP.x = mouse.x - Board.HALF_SQUARE_SIZE;
        activeP.y = mouse.y - Board.HALF_SQUARE_SIZE;
        activeP.col = activeP.getCol(activeP.x);
        activeP.row = activeP.getRow(activeP.y);

        // check if the hovering square is valid or not
        if (activeP.canMove(activeP.col, activeP.row)) {
            canMove = true;

            // if a piece is captured, it is removed from the list
            if (activeP.hitP != null)
                simPieces.remove(activeP.hitP.getIndex());

            validSquare = true;
        }
    }

    private void checkCastling() {
        if (castlingP != null) {
            if (castlingP.col == 0)
                castlingP.col += 3;
            else if (castlingP.col == 7)
                castlingP.col -= 2;

            castlingP.x = castlingP.getX(castlingP.col);
        }
    }

    private void changeTurn() {
        if (currentColor == WHITE) {
            currentColor = BLACK;

            // reset 2 steps for black pawns
            for (Piece piece : pieces)
                if (piece.color == BLACK)
                    piece.twoSteps = false;
        } else {
            currentColor = WHITE;

            // reset 2 steps for white pawns
            for (Piece piece : pieces)
                if (piece.color == WHITE)
                    piece.twoSteps = false;
        }

        activeP = null;
    }

    private boolean canPromote() {
        if (activeP.type == Type.PAWN)
            if (currentColor == WHITE && activeP.row == 0 || currentColor == BLACK && activeP.row == 7) {
                promotionPieces.clear();
                promotionPieces.add(new Knight(currentColor, 9, 2));
                promotionPieces.add(new Bishop(currentColor, 9, 3));
                promotionPieces.add(new Rook(currentColor, 9, 4));
                promotionPieces.add(new Queen(currentColor, 9, 5));

                return true;
            }

        return false;
    }

    private void promoting() {
        if (mouse.isPressed)
            for (Piece piece : promotionPieces) {
                if (piece.col == mouse.x / Board.SQUARE_SIZE && piece.row == mouse.y / Board.SQUARE_SIZE) {
                    switch (piece.type) {
                        case KNIGHT -> simPieces.add(new Knight(currentColor, activeP.col, activeP.row));
                        case BISHOP -> simPieces.add(new Bishop(currentColor, activeP.col, activeP.row));
                        case ROOK -> simPieces.add(new Rook(currentColor, activeP.col, activeP.row));
                        case QUEEN -> simPieces.add(new Queen(currentColor, activeP.col, activeP.row));
                        default -> {
                        }
                    }

                    simPieces.remove(activeP.getIndex());
                    copyPieces(simPieces, pieces);

                    activeP = null;
                    promotion = false;

                    changeTurn();
                }
            }
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

        if (activeP != null) {
            if (canMove) {
                g2.setColor(Color.white);
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.7f));
                g2.fillRect(activeP.col * Board.SQUARE_SIZE, activeP.row * Board.SQUARE_SIZE, Board.SQUARE_SIZE,
                        Board.SQUARE_SIZE);
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
            }

            activeP.draw(g2);
        }

        // status messages
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setFont(new Font("Fira code", Font.PLAIN, 30));
        g2.setColor(FOREGROUND_TEXT_COLOR);

        if (promotion) {
            g2.drawString("Promote to:", 840, 150);

            for (Piece piece : promotionPieces)
                g2.drawImage(piece.image, piece.getX(piece.col), piece.getY(piece.row), Board.SQUARE_SIZE,
                        Board.SQUARE_SIZE, null);
        } else {
            if (currentColor == WHITE)
                g2.drawString("White's turn", 840, 550);
            else
                g2.drawString("Black's turn", 840, 250);
        }
    }
}
