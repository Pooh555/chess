package main;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.ArrayList;
import javax.swing.JPanel;
import pieces.Bishop;
import pieces.King;
import pieces.Knight;
import pieces.Pawn;
import pieces.Piece;
import pieces.Queen;
import pieces.Rook;

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
    Piece activeP, checkingP; // active piece
    public static Piece castlingP; // castling piece

    // booelan
    boolean canMove;
    boolean validSquare;
    boolean promotion;
    boolean gameover;

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
        } else if (gameover == false) {
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

                        printPiecesPositions(); // for debugging

                        // remove the captured piece from the board during simulation phase
                        copyPieces(simPieces, pieces);
                        activeP.updatePosition();
                        checkCastling();

                        if (castlingP != null) {
                            castlingP.updatePosition();
                            System.out.println("Castled sucesfully.");
                        }

                        if (checkKingInCheck() && checkCheckmate()) {
                            gameover = true; // checkmated
                        } else {
                            if (canPromote()) {
                                promotion = true; // promoted
                                System.out.println("The pawn is promoted sucesfully.");
                            }
                            else
                                changeTurn(); // change player's turn
                        }
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

            if (checkIllegal(activeP) == false && opponentCanCaptureKing() == false)
                validSquare = true;
        }
    }

    private boolean checkIllegal(Piece king) {
        if (king.type == Type.KING)
            for (Piece piece : simPieces)
                if (piece != king && piece.color != king.color && piece.canMove(king.col, king.row))
                    return true;

        return false;
    }

    private boolean opponentCanCaptureKing() {
        Piece king = getKing(false);

        for (Piece piece : simPieces)
            if (piece.color != king.color && piece.canMove(king.col, king.row))
                return true;

        return false;
    }

    private boolean checkKingInCheck() {
        Piece king = getKing(true);

        if (activeP.canMove(king.col, king.row)) {
            checkingP = activeP;
            return true;
        } else
            checkingP = null;

        return false;
    }

    private Piece getKing(boolean opponent) {
        Piece king = null;

        for (Piece piece : simPieces)
            if (opponent) {
                if (piece.type == Type.KING && piece.color != currentColor)
                    king = piece;
            } else {
                if (piece.type == Type.KING && piece.color == currentColor)
                    king = piece;
            }

        return king;

    }

    private boolean checkCheckmate() {
        Piece king = getKing(true);

        if (kingCanMove(king))
            return false;
        else {
            // check if the king can be blocked with a piece or a pawn
            int colDiff = Math.abs(checkingP.col - king.col);
            int rowDiff = Math.abs(checkingP.row = king.row);

            // check the relative position of the king and the checking piece
            if (colDiff == 0) {
                // the checking piece is attacking vertically
                if (checkingP.row < king.row) // the checking piece is above the king
                    for (int row = checkingP.row; row < king.row; row++)
                        for (Piece piece : simPieces)
                            if (piece != king && piece.color != currentColor && piece.canMove(checkingP.col, row))
                                return false;
                if (checkingP.row > king.row) // the checking piece is below the king
                    for (int row = checkingP.row; row > king.row; row--)
                        for (Piece piece : simPieces)
                            if (piece != king && piece.color != currentColor && piece.canMove(checkingP.col, row))
                                return false;
            } else if (rowDiff == 0) {
                // the checking piece is attacking horizontally
                if (checkingP.col < king.col) // the checking piece is to the left of the king
                    for (int col = checkingP.col; col < king.row; col++)
                        for (Piece piece : simPieces)
                            if (piece != king && piece.color != currentColor && piece.canMove(col, checkingP.row))
                                return false;
                if (checkingP.col > king.col) // the checking piece is to the right of the king
                    for (int col = checkingP.col; col > king.row; col--)
                        for (Piece piece : simPieces)
                            if (piece != king && piece.color != currentColor && piece.canMove(col, checkingP.row))
                                return false;
            } else if (colDiff - rowDiff == 0) {
                // the checking piece is attacking diagonally
                if (checkingP.row < king.row) {
                    if (checkingP.col < king.col) // checking piece is in the upper left
                        for (int col = checkingP.col, row = checkingP.row; col < king.col; col++, row++)
                            for (Piece piece : simPieces)
                                if (piece != king && piece.color != currentColor && piece.canMove(col, row))
                                    return false;
                    if (checkingP.col > king.col) // checking piece is in the upper right
                        for (int col = checkingP.col, row = checkingP.row; col > king.col; col--, row++)
                            for (Piece piece : simPieces)
                                if (piece != king && piece.color != currentColor && piece.canMove(col, row))
                                    return false;
                }
                if (checkingP.row > king.row) {
                    if (checkingP.col < king.col) // checking piece is in the lower left
                        for (int col = checkingP.col, row = checkingP.row; col < king.col; col++, row--)
                            for (Piece piece : simPieces)
                                if (piece != king && piece.color != currentColor && piece.canMove(col, row))
                                    return false;
                    if (checkingP.col > king.col) // checking piece is in the lower right
                        for (int col = checkingP.col, row = checkingP.row; col > king.col; col--, row--)
                            for (Piece piece : simPieces)
                                if (piece != king && piece.color != currentColor && piece.canMove(col, row))
                                    return false;
                }
            }
        }

        return true;
    }

    private boolean kingCanMove(Piece king) {
        // check if there is atleast 1 legal square the king can move to
        if (isValidMove(king, -1, -1))
            return true;
        if (isValidMove(king, -1, 0))
            return true;
        if (isValidMove(king, -1, 1))
            return true;
        if (isValidMove(king, 0, -1))
            return true;
        if (isValidMove(king, -0, 1))
            return true;
        if (isValidMove(king, 1, -1))
            return true;
        if (isValidMove(king, 1, 0))
            return true;

        return isValidMove(king, 1, 1);
    }

    private boolean isValidMove(Piece king, int colPlus, int rowPlus) {
        boolean isValidMove = false;

        // temporary update the king's position
        king.col += colPlus;
        king.row += rowPlus;

        if (king.canMove(king.col, king.row)) {
            if (king.hitP != null)
                simPieces.remove(king.hitP.getIndex());
            if (checkIllegal(king) == false) {
                isValidMove = true;
            }
        }

        // reset the king's position
        king.resetPosition();

        // reset the pieces
        copyPieces(pieces, simPieces);

        return isValidMove;
    }

    private void printPiecesPositions() {
        String color;

        for (Piece piece : simPieces) {
            if (piece.color == 0) color = "white";
            else color = "black";

            System.out.println(piece.type + ", " + color +", col: " + piece.col + " row: " + piece.row);
        }
    }

    private void checkCastling() {
        if (castlingP != null) {
            if (castlingP.col == 0) // change the position of the rook in long castling
                castlingP.col += 3;
            else if (castlingP.col == 7) // change the position of the rook in short castling
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

                    // remove the promoted pawn from the board
                    simPieces.remove(activeP.getIndex());
                    copyPieces(simPieces, pieces);

                    // reset states
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
                if (checkIllegal(activeP) || opponentCanCaptureKing() == true) {
                    // highlights the square red if it's illegal to move to
                    g2.setColor(Color.red);
                    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.7f));
                    g2.fillRect(activeP.col * Board.SQUARE_SIZE, activeP.row * Board.SQUARE_SIZE, Board.SQUARE_SIZE,
                            Board.SQUARE_SIZE);
                    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
                } else {
                    // highlights the square white if it's legal to move to
                    g2.setColor(Color.white);
                    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.7f));
                    g2.fillRect(activeP.col * Board.SQUARE_SIZE, activeP.row * Board.SQUARE_SIZE, Board.SQUARE_SIZE,
                            Board.SQUARE_SIZE);
                    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
                }
            }

            activeP.draw(g2);
        }

        // status messages

        // text attributes
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

        if (gameover) {
            String checkmateStr = "";

            if (currentColor == WHITE)
                checkmateStr = "White wins";
            if (currentColor == BLACK)
                checkmateStr = "Black wins";

            g2.setFont(new Font("Fira code", Font.PLAIN, 30));
            g2.setColor(FOREGROUND_TEXT_COLOR);
            g2.drawString(checkmateStr, 200, 420);
        }
    }
}
