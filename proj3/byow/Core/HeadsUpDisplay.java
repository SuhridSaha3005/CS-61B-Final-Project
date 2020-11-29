package byow.Core;


import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class HeadsUpDisplay {
    private int keys;
    private int width;
    private int height;
    private int lives;
    private TETile currTile;
    static DecimalFormat df = new DecimalFormat("0.00");

    public HeadsUpDisplay(int k, int w, int h) {
        keys = k;
        width = w;
        height = h;
        lives = 3;
        currTile = Tileset.FLOOR;
    }

    public void updateKeyAndDistance(Avatar player, ArrayList<Avatar> ghost) {
        StdDraw.setPenColor(Tileset.purpleColorDark);
        Font font = new Font("Times New Roman", Font.BOLD, 15);
        StdDraw.setFont(font);
        double textHeight = height  - 1;
        StdDraw.text(15, textHeight, "Keys Remaining: " + keys);
        StdDraw.text(width - 15, textHeight, "Closest Ghost: "
                + df.format(getMinGhostDist(player, ghost)));
        StdDraw.text(15, textHeight - 1, "Lives Remaining: " + lives);
        StdDraw.text(width - 15, textHeight - 1, "Current Tile: " + currTile.description());
    }

    public void updateMainString(String center, Color color) {
        StdDraw.setPenColor(color);
        Font font = new Font("Times New Roman", Font.BOLD, 20);
        StdDraw.setFont(font);
        double textHeight = height  - 1.5;
        double textWidth = ( (double) width / 2);
        StdDraw.text(textWidth, textHeight, center);
        Font font2 = new Font("Monaco", Font.BOLD, 14);
        StdDraw.setFont(font2);
    }

    private double getMinGhostDist(Avatar player, ArrayList<Avatar> ghosts) {
        double min = Double.POSITIVE_INFINITY;
        for (Avatar g: ghosts) {
            if (Avatar.distance(player,g) < min) {
                min = Avatar.distance(player,g);
            }
        }
        return min;
    }

    public void changeKeys(int keyNum) {
        keys = keyNum;
    }

    public void changeLives(int life) {
        lives = life;
    }

    public void changeTile(TETile tile) {
        currTile = tile;
    }

    public void updateAll(Avatar player, ArrayList<Avatar> ghosts, int keyNum, int lifeNum, String center, Color color, TETile currTile) {
        StdDraw.setPenColor(Tileset.purpleColorDark);
        updateKeyAndDistance(player, ghosts);
        changeKeys(keyNum);
        changeLives(lifeNum);
        changeTile(currTile);
        updateMainString(center, color);
    }
}
