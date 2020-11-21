package byow.Core;

import byow.TileEngine.TETile;

import java.util.Arrays;

public class XYPosn {
    private int x;
    private int y;
    private TETile[][] world;

    public XYPosn(int xPos, int yPos) {
        x = xPos;
        y = yPos;
    }

    public XYPosn(int xPos, int yPos, TETile[][] w) {
        x = xPos;
        y = yPos;
        world = w;
    }

    int getX() {
        return x;
    }

    int getY() {
        return y;
    }

    TETile[][] getWorld() {
        return world;
    }

    @Override
    public String toString() {
        return "XYPosn{" +
                "x=" + x +
                ", y=" + y +
                ", world=" + Arrays.toString(world) +
                '}';
    }
}