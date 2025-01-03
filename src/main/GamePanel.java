package main;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.lang.Thread;
import java.util.ArrayList;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.Timer;

import pieces.Bishop;
import pieces.King;
import pieces.Knight;
import pieces.Pawn;
import pieces.Piece;
import pieces.Queen;
import pieces.Rook;

public class GamePanel extends JPanel implements Runnable {
    // game information
    public static int WIDTH = 1100; // window's width
    public static int HEIGHT = 800; // window's height
    public static Dimension windowSize = new Dimension(WIDTH, HEIGHT); // window size
    static final int FPS = 360; // game FPS and tick rate
    Thread gameThread; // main game thread
    Board board = new Board(); // visual chessboard

    // devices
    Mouse mouse = new Mouse();

    // pieces
    public static ArrayList<Piece> pieces = new ArrayList<>();
    public static ArrayList<Piece> simPieces = new ArrayList<>();
    public static ArrayList<Piece> promotionPieces = new ArrayList<>();
    public static boolean isLegalMove, isCastle;
    Piece activePiece, hitPiece; // piece that is being held, piece that is about to be captured
    int moveDirection = currentColor ? 1 : -1; // pawn's moving direction

    // color
    private BufferedImage backgroundImage;
    static String backgroundImagePath = "/res/wallpaper/wallpaper.jpg";
    public static final Color ACTIVE_SQUARE_COLOR = new Color(255, 255, 255);
    public static final boolean WHITE = false;
    public static final boolean BLACK = true;
    public static final Color BACKGROUND_COLOR = new Color(0, 0, 0);
    public static final Color FOREGROUND_TEXT_COLOR = new Color(255, 201, 201);

    // game state
    public static boolean currentColor = WHITE; // the game starts with white
    public static boolean gameStatus = true; // the game is ongoing
    public static boolean promotionState; // a pawn is being promoted

    public GamePanel() {
        setPreferredSize(windowSize); // set initial window size
        setBackground(Color.BLACK); // set initial window background
        addMouseMotionListener(mouse);
        addMouseListener(mouse);

        System.out.println("Mouse listenser is activated.");

        // load the background image
        try (InputStream input = getClass().getResourceAsStream(backgroundImagePath)) {
            if (input == null)
                throw new IllegalArgumentException("Background image not found at " + backgroundImagePath);

            backgroundImage = ImageIO.read(input);

            System.out.println("Wallpaper is loaded successfully");
        } catch (Exception e) {
            System.err.println("Failed to load background image: " + e.getMessage());
        }

        // update the window's size
        resizeDisplayComponents();

        board.clearBoard(); // clear the chessboard
        board.clearOccupiedBoard(); // clear terratory simulation board
        setPieces(); // set up the pieces on the board
        copyPieces(pieces, simPieces); // copy the static pieces' positions to dynamic pieces' positions
        board.updatePiecePositions(pieces);
        board.updateOccupiedTerratory();
    }

    public void launchGame() {
        gameThread = new Thread(this); // initiate a new game thread
        gameThread.start(); // calls the run() method
    }

    @Override
    public void run() {
        // game loop
        double tickInterval = 1000000000 / FPS; // 1 game tick (in nanoseconds)
        double delta = 0; // difference between the last tick and the current time
        long previousTickTime = System.nanoTime(); // time of the last tick
        long currentTime; // current time

        while (gameThread != null) {
            currentTime = System.nanoTime();

            delta += (currentTime - previousTickTime) / tickInterval;
            previousTickTime = currentTime;

            if (delta >= 1) {
                update();
                repaint();
                delta--;
            }
        }
    }

    private void update() {
        if (promotionState) {
            promoting();
        } else if (gameStatus != false) {
            if (mouse.isPressed) {
                // check if the player is holding a piece
                if (activePiece == null) {
                    board.clearBoard(); // clear the chessboard
                    board.updatePiecePositions(pieces); // update pieces' positions on the board

                    // if the player is not holding a piece, pick the piece on the current square
                    // with the same color up
                    for (Piece piece : simPieces)
                        if (piece.color == currentColor
                                && piece.col == mouse.x / Board.SQUARE_SIZE
                                && piece.row == mouse.y / Board.SQUARE_SIZE) {
                            activePiece = piece;

                            // System.out.println("piece col: " + piece.col + ", piece row: " + piece.row +
                            // ", square size: " + Board.SQUARE_SIZE);
                        }
                } else {
                    // if the player is holding a piece, simulate a calculating (thinking) phase
                    // board.printBoard();
                    board.printOccupiedBoard();
                    simulate();
                }
            }
            if (mouse.isPressed == false) {
                if (activePiece != null) {
                    if (isLegalMove) {

                        // -------------------- //
                        // move confirmed //
                        // -------------------- //

                        System.out.println("Legal move.");

                        // set active piece state
                        activePiece.hasMoved = true;

                        // removed the captured piece
                        if (Board.boardPieces[activePiece.row][activePiece.col] != null) {
                            for (Piece piece : simPieces)
                                if (piece.col == activePiece.col && piece.row == activePiece.row
                                        && piece.color != activePiece.color)
                                    hitPiece = piece;

                            if (hitPiece != null)
                                simPieces.remove(hitPiece);
                        }

                        // removed the en passent piece
                        if (activePiece.type == Type.PAWN)
                            if (Board.boardPieces[activePiece.row - moveDirection][activePiece.col] != null) {
                                for (Piece piece : simPieces)
                                    if (piece.col == activePiece.col && piece.row == activePiece.row - moveDirection
                                            && piece.color != activePiece.color && piece.type == Type.PAWN)
                                        hitPiece = piece;

                                if (hitPiece != null)
                                    simPieces.remove(hitPiece);
                            }

                        // castle
                        if (isCastle) {
                            // black long castle
                            if (activePiece.col == 2 && activePiece.row == 0)
                                for (Piece piece : simPieces)
                                    if (piece.type == Type.ROOK && piece.col == 0 && piece.row == 0)
                                        piece.col = 3;
                            // black short castle
                            if (activePiece.col == 6 && activePiece.row == 0)
                                for (Piece piece : simPieces)
                                    if (piece.type == Type.ROOK && piece.col == 7 && piece.row == 0)
                                        piece.col = 5;
                        }

                        // Promotion
                        if (promote()) {
                            activePiece.updatePosition();
                            promotionState = true; // promoted
                            System.out.println("The pawn is promoted sucesfully.");
                        } else
                            changeTurn(); // change player's turn

                        // changeTurn();
                    } else {
                        resetPositionState();
                    }

                }
            }
        }
    }

    public void setPieces() {
        // white pieces
        // pieces.add(new Pawn(WHITE, 0, 6));
        // pieces.add(new Pawn(WHITE, 1, 6));
        // pieces.add(new Pawn(WHITE, 2, 6));
        // pieces.add(new Pawn(WHITE, 3, 6));
        // pieces.add(new Pawn(WHITE, 4, 6));
        // pieces.add(new Pawn(WHITE, 5, 6));
        // pieces.add(new Pawn(WHITE, 6, 6));
        // pieces.add(new Pawn(WHITE, 7, 6));
        // pieces.add(new Knight(WHITE, 1, 7));
        // pieces.add(new Knight(WHITE, 6, 7));
        // pieces.add(new Bishop(WHITE, 2, 7));
        // pieces.add(new Bishop(WHITE, 5, 7));
        pieces.add(new Rook(WHITE, 0, 7));
        pieces.add(new Rook(WHITE, 7, 7));
        pieces.add(new Queen(WHITE, 3, 7));
        pieces.add(new King(WHITE, 4, 7));

        // black
        pieces.add(new Pawn(BLACK, 0, 1));
        pieces.add(new Pawn(BLACK, 1, 1));
        pieces.add(new Pawn(BLACK, 2, 1));
        pieces.add(new Pawn(BLACK, 3, 1));
        pieces.add(new Pawn(BLACK, 4, 1));
        pieces.add(new Pawn(BLACK, 5, 1));
        pieces.add(new Pawn(BLACK, 6, 1));
        pieces.add(new Pawn(BLACK, 7, 1));
        // pieces.add(new Knight(BLACK, 1, 0));
        // pieces.add(new Knight(BLACK, 6, 0));
        // pieces.add(new Bishop(BLACK, 2, 0));
        // pieces.add(new Bishop(BLACK, 5, 0));
        pieces.add(new Rook(BLACK, 0, 0));
        pieces.add(new Rook(BLACK, 7, 0));
        pieces.add(new Queen(BLACK, 3, 0));
        pieces.add(new King(BLACK, 4, 0));
    }

    private boolean promote() {
        if (activePiece.type == Type.PAWN)
            if (currentColor == WHITE && activePiece.row == 0 || currentColor == BLACK && activePiece.row == 7) {
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
                        case KNIGHT -> simPieces.add(new Knight(currentColor, activePiece.col, activePiece.row));
                        case BISHOP -> simPieces.add(new Bishop(currentColor, activePiece.col, activePiece.row));
                        case ROOK -> simPieces.add(new Rook(currentColor, activePiece.col, activePiece.row));
                        case QUEEN -> simPieces.add(new Queen(currentColor, activePiece.col, activePiece.row));
                        default -> {
                        }
                    }

                    // remove the promoted pawn from the board
                    simPieces.remove(activePiece.getIndex());
                    copyPieces(simPieces, pieces);

                    // reset states
                    changeTurn();
                }
            }
    }

    private void copyPieces(ArrayList<Piece> source, ArrayList<Piece> target) {
        target.clear(); // clear target ArrayList

        // copy source to target
        for (int i = 0; i < source.size(); i++)
            target.add(source.get(i));
    }

    public void simulate() {
        // update the position of the piece being held
        if (activePiece != null) {
            activePiece.isActive = true;
            activePiece.x = mouse.x - Board.HALF_SQUARE_SIZE;
            activePiece.y = mouse.y - Board.HALF_SQUARE_SIZE;
            activePiece.col = activePiece.getCol(activePiece.x);
            activePiece.row = activePiece.getRow(activePiece.y);
            isLegalMove = activePiece.canMove(activePiece.col, activePiece.row);
            isCastle = activePiece.canCastle(activePiece.col, activePiece.row);
        }

        // debug
        // System.out.println("Piece color: " + activePiece.color + ", piece col: " +
        // activePiece.x / Board.SQUARE_SIZE + ", piece row: " + activePiece.y /
        // Board.SQUARE_SIZE);
        // System.out.println("piece x: " + activePiece.x + ", piece y: " +
        // activePiece.y);
        // System.out.println("mouse x: " + mouse.x + ", mouse y: " + mouse.y);
    }

    private void changeTurn() {
        // reset en passent state
        for (Piece piece : simPieces)
            if (piece.color == currentColor && piece.type == Type.PAWN)
                piece.resetEnPassentState();

        copyPieces(simPieces, pieces);
        activePiece.updatePosition();
        currentColor = !currentColor; // change color
        moveDirection = currentColor ? 1 : -1; // change pawn's move direction based on the active color
        activePiece = null;
        promotionState = false;

        board.clearBoard();
        board.updatePiecePositions(pieces);
        board.clearOccupiedBoard();
        board.updateOccupiedTerratory();

        System.out.println("Legal move, the side has changed.");
        System.out.println("current player: " + currentColor + ", pawn's direction: " + moveDirection);
    }

    private void resetPositionState() {
        // invalid move, reset all states
        System.out.println("Illegal move, position's states are reset.");

        promotionState = false;

        copyPieces(pieces, simPieces);
        activePiece.resetPosition();

        activePiece = null;
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;

        if (backgroundImage != null)
            drawCroppedImage(g, backgroundImage);

        // draw the board
        board.draw(g2);

        // draw the pieces
        for (Piece piece : simPieces)
            piece.draw(g2);

        if (activePiece != null) {
            // Display active square color
            g2.setColor(ACTIVE_SQUARE_COLOR);
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.7f));
            g2.fillRect(activePiece.col * Board.SQUARE_SIZE, activePiece.row * Board.SQUARE_SIZE, Board.SQUARE_SIZE,
                    Board.SQUARE_SIZE);
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));

            activePiece.draw(g2);
        }

        // status messages

        // text attributes
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setFont(new Font("Fira code", Font.PLAIN, (int) (0.03 * WIDTH)));
        g2.setColor(FOREGROUND_TEXT_COLOR);

        if (promotionState) {
            g2.drawString("Promote to:", (int) (0.75 * WIDTH), (int) (0.25 * HEIGHT));

            for (Piece piece : promotionPieces)
                g2.drawImage(piece.image, piece.getX(piece.col), piece.getY(piece.row), Board.SQUARE_SIZE,
                        Board.SQUARE_SIZE, null);
        } else {
            if (currentColor == WHITE)
                g2.drawString("White's turn", (int) (0.75 * WIDTH), (int) (0.75 * HEIGHT));
            else
                g2.drawString("Black's turn", (int) (0.75 * WIDTH), (int) (0.25 * HEIGHT));
        }

        if (gameStatus == false) {
            String checkmateStr = "";

            if (currentColor == WHITE)
                checkmateStr = "White wins";
            if (currentColor == BLACK)
                checkmateStr = "Black wins";

            g2.setFont(new Font("Fira code", Font.PLAIN, (int) (0.05 * WIDTH)));
            g2.setColor(FOREGROUND_TEXT_COLOR);
            g2.drawString(checkmateStr, 200, 420);
        }
    }

    private void drawCroppedImage(Graphics g, BufferedImage image) {
        // image size
        int imageWidth = image.getWidth();
        int imageHeight = image.getHeight();

        // calculate the cropping region
        int cropX = 0, cropY = 0;
        int cropWidth = imageWidth, cropHeight = imageHeight;

        // calculate the scaling factors for the image to fit the panel
        double panelAspect = (double) WIDTH / HEIGHT;
        double imageAspect = (double) imageWidth / imageHeight;

        if (panelAspect > imageAspect) {
            // panel is wider than the image
            cropHeight = (int) (imageWidth / panelAspect);
            cropY = (imageHeight - cropHeight) / 2;
        } else {
            // panel is taller than the image
            cropWidth = (int) (imageHeight * panelAspect);
            cropX = (imageWidth - cropWidth) / 2;
        }

        // Draw the cropped image to fill the panel
        g.drawImage(
                image,
                0, 0, WIDTH, HEIGHT, // destination rectangle (panel dimensions)
                cropX, cropY, cropX + cropWidth, cropY + cropHeight, // source rectangle (cropped region)
                this);
    }

    public void resizeDisplayComponents() {
        addComponentListener(new ComponentAdapter() {
            private Timer resizeTimer;

            @Override
            public void componentResized(ComponentEvent e) {
                if (resizeTimer != null) {
                    resizeTimer.stop(); // stop the previous timer if it's still running
                }

                resizeTimer = new Timer(100, _ -> {
                    WIDTH = getWidth();
                    HEIGHT = getHeight();
                    Board.SQUARE_SIZE = Math.min(WIDTH, HEIGHT) / 8;

                    for (Piece piece : pieces) {
                        piece.x = piece.getX(piece.col);
                        piece.y = piece.getY(piece.row);
                    }

                    repaint();
                    System.out.println("New WIDTH: " + WIDTH + ", New HEIGHT: " + HEIGHT);
                });

                resizeTimer.setRepeats(false); // execute only once
                resizeTimer.start();
            }
        });
    }
}