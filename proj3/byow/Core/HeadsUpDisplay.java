package byow.Core;


import edu.princeton.cs.introcs.StdDraw;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class HeadsUpDisplay {
    private int keys;
    private final int width;
    private int height;
    static DecimalFormat df = new DecimalFormat("0.00");

    public HeadsUpDisplay(int k, int w, int h) {
        keys = k;
        width = w;
        height = h;
    }

    public void update(Avatar player, ArrayList<Avatar> ghost) {
        double textHeight = height  - 1.5;
        StdDraw.text(10, textHeight, "Keys Remaining: " + keys);
        StdDraw.text(width - 15, textHeight, "Distance To Closest Ghost: "
                + df.format(getMinGhostDist(player, ghost)));
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
}
