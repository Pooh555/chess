package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.lang.Thread;
import java.util.ArrayList;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

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
    Piece activePiece; // piece that is being held or selected

    // color
    private BufferedImage backgroundImage;
    static String backgroundImagePath = "/res/wallpaper/wallpaper.jpg";
    public static final boolean WHITE = false;
    public static final boolean BLACK = true;
    boolean currentColor = WHITE; // the game starts with white

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
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                WIDTH = getWidth(); // update WIDTH
                HEIGHT = getHeight(); // update HEIGHT

                System.out.println("New WIDTH: " + WIDTH + ", New HEIGHT: " + HEIGHT);
            }
        });

        setPieces(); // set up the pieces on the board
        copyPieces(pieces, simPieces); // copi'es the static pieces' positions to dynamic pieces' positions
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
        if (mouse.isPressed) {
            // check if the player is holding a piece
            if (activePiece == null) {
                // if the player is not holding a piece, pick the piece on the current square with the same color up
                for (Piece piece : simPieces) {
                    if (piece.color == currentColor
                            && piece.col == mouse.x / Board.SQUARE_SIZE
                            && piece.row == mouse.y / Board.SQUARE_SIZE) {
                        activePiece = piece;
                            
                        System.out.println("piece col: " + piece.col + ", piece row: " + piece.row + ", square size: " + Board.SQUARE_SIZE);
                    }
                
                    System.out.println("Piece: " + piece.type);
                }
            } else {
                // if the player is holding a piece, simulate a calculating (thinking) phase
                simulate(); 
            }
        }
    }

    public void setPieces() {
        // white pieces
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

        // black
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
        target.clear(); // clear target ArrayList

        // copy source to target
        for (int i = 0; i < source.size(); i++)
            target.add(source.get(i));
    }

    public void simulate() {
        // update the position of the piece being held
        activePiece.x = mouse.x;
        activePiece.y = mouse.y;

        System.out.println("Piece type: " + activePiece.type + ", piece col: " + activePiece.x / Board.SQUARE_SIZE + ", piece row: " + activePiece.y / Board.SQUARE_SIZE);
        System.out.println("piece x: " + activePiece.x + ", piece y: " + activePiece.y);
        System.out.println("mouse x: " + mouse.x + ", mouse y: " + mouse.y);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;

        // draw the wallpaper
        if (backgroundImage != null)
            drawCroppedImage(g, backgroundImage);

        // draw the board
        board.draw(g2);

        // draw the pieces
        for (Piece piece : simPieces)
            piece.draw(g2);
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
}
