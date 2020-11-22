package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.ArrayList;
import java.util.Random;

public class Room {
    private final TETile[][] world;
    private final ArrayList<XYPosn> cornerWalls;
    private final ArrayList<XYPosn> otherWalls;
    private final ArrayList<XYPosn> floor;

    public Room(TETile[][] world, XYPosn origin, XYPosn entry, int width, int length) {
        this.world = world;
        cornerWalls = new ArrayList<>();
        otherWalls = new ArrayList<>();
        floor = new ArrayList<>();
        for (int x = origin.getX(); x <= origin.getX() + width; x += 1) {
            for (int y = origin.getY(); y <= origin.getY() + length; y += 1) {
                if ((x == origin.getX() || x == origin.getX() + width)
                        && (y == origin.getY() || y == origin.getY() + length)) {
                    cornerWalls.add(new XYPosn(x, y, world));
                }
                if (x == entry.getX() && y == entry.getY()) {
                    floor.add(new XYPosn(x, y, world));
                } else if (x == origin.getX() || x == origin.getX() + width
                        || y == origin.getY() || y == origin.getY() + length) {
                    otherWalls.add(new XYPosn(x, y, world));
                } else {
                    floor.add(new XYPosn(x, y, world));
                }
            }
        }
    }

    public void addRoom() {
        for (XYPosn wall : cornerWalls) {
            world[wall.getX()][wall.getY()] = Tileset.WALL;
        }
        for (XYPosn wall : otherWalls) {
            world[wall.getX()][wall.getY()] = Tileset.WALL;
        }
        for (XYPosn tile : floor) {
            world[tile.getX()][tile.getY()] = Tileset.FLOOR;
        }
    }

    public XYPosn getRandomWall(Random rand) {
        return otherWalls.get(RandomUtils.uniform(rand, 0, otherWalls.size() - 1));
    }
}
