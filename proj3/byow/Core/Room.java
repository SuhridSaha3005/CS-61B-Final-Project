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

    private boolean checkAdjacent(XYPosn xy, TETile tile) {
        boolean hasPath = false;
        if (up(xy) != null) {
            hasPath = (hasPath || (world[up(xy).getX()][up(xy).getY()].equals(tile)));
        }
        if (down(xy) != null) {
            hasPath = (hasPath || (world[down(xy).getX()][down(xy).getY()].equals(tile)));
        }
        if (left(xy) != null) {
            hasPath = (hasPath || (world[left(xy).getX()][left(xy).getY()].equals(tile)));
        }
        if (right(xy) != null) {
            hasPath = (hasPath || (world[right(xy).getX()][right(xy).getY()].equals(tile)));
        }
        return hasPath;
    }

    private XYPosn up(XYPosn position) {
        if (position.getY() + 1 == world[0].length) {
            return null;
        } else {
            return new XYPosn(position.getX(), position.getY() + 1);
        }
    }

    private XYPosn down(XYPosn position) {
        if (position.getY() == 0) {
            return null;
        } else {
            return new XYPosn(position.getX(), position.getY() - 1);
        }
    }

    private XYPosn right(XYPosn position) {
        if (position.getX() + 1 == world.length) {
            return null;
        } else {
            return new XYPosn(position.getX() + 1, position.getY());
        }
    }

    private XYPosn left(XYPosn position) {
        if (position.getX() == 0) {
            return null;
        } else {
            return new XYPosn(position.getX() - 1, position.getY());
        }
    }

    /** Gives a random non-corner wall for a hallway to shoot from.
     * @param rand random seed
     * @return random element from this.otherWalls
     */
    public XYPosn getRandomWall(Random rand) {
        int randIndex = RandomUtils.uniform(rand, 0, otherWalls.size() - 1);
        int i = 0;
        while (!checkAdjacent(otherWalls.get(randIndex), Tileset.NOTHING)) {
            if (i > 500) {
                break;
            }
            randIndex = RandomUtils.uniform(rand, 0, otherWalls.size() - 1);
            i += 1;
        }
        return otherWalls.get(randIndex);
    }
}
