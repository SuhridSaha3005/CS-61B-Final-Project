package byow.Core;


import edu.princeton.cs.introcs.StdDraw;

import java.util.ArrayList;

public class HeadsUpDisplay {
    int keys;
    int width;
    int height;

    public HeadsUpDisplay(int k, int w, int h) {
        keys = k;
        width = w;
        height = h;
    }

    public void update(Avatar player, ArrayList<Avatar> ghost) {
        double textHeight = height  - 1.5;
        StdDraw.text(10, textHeight, "Keys Remaining: " + keys);
        StdDraw.text(width - 15, textHeight, "Distance To Closest Ghost: " + getMinGhostDist(player, ghost));
    }

    private int getMinGhostDist(Avatar player, ArrayList<Avatar> ghosts) {
        int min = width + height + 200;
        for (Avatar g: ghosts) {
            min = Math.min(manhattan(player.getPosn(), g.getPosn()), min);
        }
        return min;
    }

    private int manhattan(XYPosn player, XYPosn ghost) {
        return manhattan(player.getX(), player.getY(), ghost.getX(), ghost.getY());
    }

    public int manhattan(int playerX, int playerY, int ghostX, int ghostY) {
        return Math.abs(playerX - ghostX) + Math.abs(playerY - ghostY);
    }

}
