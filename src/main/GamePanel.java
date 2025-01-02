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
import java.io.File;
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
    Board board = new Board();

    // pieces
    public static ArrayList<Piece> pieces = new ArrayList<>();
    public static ArrayList<Piece> simPieces = new ArrayList<>();

    // color
    private BufferedImage backgroundImage;
    static String backgroundImagePath = "/res/wallpaper/wallpaper.jpg";    
    public static final boolean WHITE = false;
    public static final boolean BLACK = true;
    boolean currentColor = WHITE; // the game starts with white

    public GamePanel() {
        setPreferredSize(windowSize); // set initial window size
        setBackground(Color.BLACK); // set initial window background

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
        pieces.add(new Bishop(BLACK, 5, 0 ));
        pieces.add(new Rook(BLACK, 0, 0));
        pieces.add(new Rook(BLACK, 7, 0));
        pieces.add(new Queen(BLACK, 3, 0));
        pieces.add(new King(BLACK, 4, 0));
    }

    private void copyPieces(ArrayList<Piece> source, ArrayList<Piece> target) {
        target.clear(); // clear target ArrayList

        // copy source to target
        for(int i = 0; i < source.size(); i++)
            target.add(source.get(i));
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D)g;

        if (backgroundImage != null)
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this); // ccale image to fit pane

        // draw the board
        board.draw(g2);

        // draw the pieces
        for(Piece piece : simPieces) 
            piece.draw(g2);
    }
}
