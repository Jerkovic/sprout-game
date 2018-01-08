package com.binarybrains.sprout.desktop;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Generates a Template for bitmask tiles
 */
public class TileGen {
    static public void main(String args[]) throws Exception {

        // https://gamedevelopment.tutsplus.com/tutorials/how-to-use-tile-bitmasking-to-auto-tile-your-level-layouts--cms-25673
        // https://github.com/tutsplus/Tile-Bitmasking/blob/master/Source%20Files/Tiles/Dynamic_Tile_Sheet.png

        try {
            int width = 16*4, height = 16*4;

            BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

            int imageW = 16;
            int imageH = 16;

            Graphics2D ig2 = bi.createGraphics();
            Color col = Color.black;
            Boolean oddRow = false;
            Boolean oddCol = false;
            int counter = 1;
            for (int y = 0; y < height; y += imageH) {
                if ((y/16) % 2 == 1) {
                    oddRow = true;
                } else {
                    oddRow = false;
                }
                for (int x = 0; x < width; x += imageW) {
                    if ((x/16) % 2 == 1) {
                        oddCol = true;
                    } else {
                        oddCol = false;
                    }

                    if (oddRow && oddCol) {
                        col = Color.blue;
                    }
                    if (oddRow && !oddCol) {
                        col = Color.blue;
                    }

                    if (!oddRow && oddCol) {
                        col = Color.blue;
                    }
                    if (!oddRow && !oddCol) {
                        col = Color.blue;
                    }

                    ig2.setColor(col);
                    ig2.fillRect (x, y, imageW, imageH);
                    // draw lines
                    if (counter == 1) drawLines(ig2, x, y, true, true, true, true);
                    if (counter == 2) drawLines(ig2, x, y, true, false, true, true);
                    if (counter == 3) drawLines(ig2, x, y, false, true, true, true);
                    if (counter == 4) drawLines(ig2, x, y, false, false, true, true);

                    if (counter == 5) drawLines(ig2, x, y, true, true, false, true);
                    if (counter == 6) drawLines(ig2, x, y, true, false, false, true);
                    if (counter == 7) drawLines(ig2, x, y, false, true, false, true);
                    if (counter == 8) drawLines(ig2, x, y, false, false, false, true);

                    if (counter == 9) drawLines(ig2, x, y, true, true, true, false);
                    if (counter == 10) drawLines(ig2, x, y, true, false, true, false);
                    if (counter == 11) drawLines(ig2, x, y, false, true, true, false);
                    if (counter == 12) drawLines(ig2, x, y, false, false, true, false);

                    if (counter == 13) drawLines(ig2, x, y, true, true, false, false);
                    if (counter == 14) drawLines(ig2, x, y, true, false, false, false);
                    if (counter == 15) drawLines(ig2, x, y, false, true, false, false);
                    // if (counter == 16) drawLines(ig2, x, y, false, false, false, false);
                    counter ++;
                }

            }

            ImageIO.write(bi, "PNG", new File("c:\\yourImageName.png"));

        } catch (IOException ie) {
            ie.printStackTrace();
        }

    }

    public static void drawLines(Graphics2D ig2, int x, int y, Boolean left, Boolean top, Boolean right, Boolean bottom) {
        ig2.setColor(Color.black);
        if (left) ig2.drawLine (x, y, x, y+15);
        if (top) ig2.drawLine (x, y, x+15, y);
        if (right) ig2.drawLine (x+15, y, x+15, y+15);
        if (bottom) ig2.drawLine (x, y+15, x+15, y+15);
    }
}
