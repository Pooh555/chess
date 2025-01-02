package main;

import java.awt.Color;
import java.awt.Graphics2D;

public class Board {
    final int MAX_COL = 8;
    final int MAX_ROW = 8;
    public static int SQUARE_SIZE = GamePanel.HEIGHT / 10;
    public static int HALF_SQUARE_SIZE = SQUARE_SIZE / 2;
    static final Color LIGHT_SQUARE_COLOR = new Color(210, 165, 125);
    static final Color DARK_SQUARE_COLOR = new Color(175, 115, 70);

    public void draw(Graphics2D g2) {
        // user boolean for performance
        boolean count = false; // false: light square, true: dark square

        // set UI
        if (GamePanel.HEIGHT <= GamePanel.WIDTH)
            SQUARE_SIZE = GamePanel.HEIGHT / 8;
        else
            SQUARE_SIZE = GamePanel.WIDTH / 10;

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

        // System.out.println("The board is drawn sucessfully.");
    }
}
