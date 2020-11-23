package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.ArrayList;
import java.util.Random;

/** Class for creating a room in given world. */
public class Room {

    /** Characteristics of World. */
    private final TETile[][] world;

    /** Walls at the 4 corners of room. */
    private final ArrayList<XYPosn> cornerWalls;

    /** All non-corner Walls of room. */
    private final ArrayList<XYPosn> otherWalls;

    /** All tiles within 4 walls of the room along with point of entry. */
    private final ArrayList<XYPosn> floor;

    /** Constructor for room class.
     * @param world world
     * @param origin bottom-left corner of room
     * @param entry point of entry to room
     * @param width how wide is the room? (xMax - xMin)
     * @param length how long is the room? (yMax - yMin)
     */
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
                } else if (x == entry.getX() && y == entry.getY()) {
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

    /** Creates the room - with floor & walls - in given world. */
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

    /** Gives a random non-corner wall for a hallway to shoot from.
     * @param rand random seed
     * @return random element from this.otherWalls
     */
    public XYPosn getRandomWall(Random rand) {
        return otherWalls.get(RandomUtils.uniform(rand, 0, otherWalls.size() - 1));
    }
}
