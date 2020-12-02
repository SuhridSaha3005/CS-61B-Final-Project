package byow.Core;

import byow.TileEngine.TETile;

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
        return "XYPosn{"
                + "x=" + x
                + ", y=" + y
                + '}';
    }

    @Override
    public boolean equals(Object obj) {
        XYPosn other = (XYPosn) obj;
        return ((this.getX() == other.getX()) && (this.getY() == other.getY()));
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
