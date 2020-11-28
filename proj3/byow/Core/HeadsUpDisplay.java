package byow.Core;


import edu.princeton.cs.introcs.StdDraw;

public class HeadsUpDisplay {
    int keys = 0;
    int ghosts;
    int width;
    int height;

    public HeadsUpDisplay(int k, int w, int h) {
        keys = k;
        width = w;
        height = h;
    }

    public void update() {
        double textHeight = height  - 1.5;
        StdDraw.text(10, textHeight, "Keys Remaining: " + keys);
        StdDraw.text(width - 10, textHeight, "Closest Ghost: " + 0);
    }

    public int manhattan(int playerX, int playerY, int ghostX, int ghostY) {
        return Math.abs(playerX - ghostX) + Math.abs(playerY - ghostY);
    }

}
