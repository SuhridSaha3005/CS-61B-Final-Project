package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.ArrayList;
import java.util.Random;

/** Helps with finding random room from a given entry with orientation */
public class GeneratorHelper {

    /** Characteristics of world. */
    TETile[][] world;

    /** Constructor that creates instance in given world */
    public GeneratorHelper(TETile[][] world) {
        this.world = world;
    }

    /** Helper class for creating room.
     * It stores 3 variables - origin (bottom-left corner), length, width. */
    public static class RoomStuff {
        XYPosn origin;
        int length;
        int width;

        /** Constructor for RoomStuff class */
        public RoomStuff(XYPosn origin, int length, int width) {
            this.origin = origin;
            this.length = length;
            this.width = width;
        }
    }

    /** Checks if a room can be built in given space.
     * @param room possible room space with origin, length and width
     * @return Is everything in said space empty?
     */
    private boolean collision(RoomStuff room) {
        if (room == null) {
            return true;
        }
        int x0 = room.origin.getX();
        int y0 = room.origin.getY();
        XYPosn point;
        for (int x = x0; x <= x0 + room.width; x += 1) {
            for (int y = y0; y <= y0 + room.length; y += 1) {
                point = new XYPosn(x, y, world);
                if (!validate(point) || world[x][y] != Tileset.NOTHING) {
                    return true;
                }
            }
        }
        return false;
    }

    /** Checks if given point has valid coordinates in world.
     * @param point XYPosn instance (x, y)
     * @return Are x and y non-negative and within the edges of the world?
     */
    private boolean validate(XYPosn point) {
        return point.getX() >= 0 && point.getX() < world.length
                && point.getY() >= 0 && point.getY() < world[0].length;
    }

    /** Gives a random possible origin from a point of entry.
     * @param rand Random seed
     * @param entry point of entry
     * @param orientation Orientation of Hallway from which room originates
     * @return A random point that could be the origin of room from entry
     */
    private XYPosn randomOrigin(Random rand, XYPosn entry, int orientation) {
        XYPosn origin;

        /* Keeps track of collisions with possible origin points */
        int numCollide = 0;

        /* Orientation could be 0 (right), 90 (up), 180 (left) or 270 (down).
        * In each orientation case, origin is created differently.
        * Origin must be a valid bottom-left point from point of entry. */

        if (orientation == 0) {
            int bottom = RandomUtils.uniform(rand, 1, 5);
            origin = new XYPosn(entry.getX(), entry.getY() - bottom, world);
            while (!validate(origin) || world[origin.getX()][origin.getY()] != Tileset.NOTHING) {
                if (numCollide > 200) {
                    return null;
                }
                bottom = RandomUtils.uniform(rand, 1, 5);
                origin = new XYPosn(entry.getX(), entry.getY() - bottom, world);
                numCollide += 1;
            }
        } else if (orientation == 90) {
            int farLeft = RandomUtils.uniform(rand, 1, 5);
            origin = new XYPosn(entry.getX() - farLeft, entry.getY(), world);
            while (!validate(origin) || world[origin.getX()][origin.getY()] != Tileset.NOTHING) {
                if (numCollide > 200) {
                    return null;
                }
                farLeft = RandomUtils.uniform(rand, 1, 5);
                origin = new XYPosn(entry.getX() - farLeft, entry.getY(), world);
                numCollide += 1;
            }
        } else if (orientation == 180) {
            int bottom = RandomUtils.uniform(rand, 1, 5);
            int farLeft = RandomUtils.uniform(rand, 3, 9);
            origin = new XYPosn(entry.getX() - farLeft, entry.getY() - bottom, world);
            while (!validate(origin) || world[origin.getX()][origin.getY()] != Tileset.NOTHING) {
                if (numCollide > 200) {
                    return null;
                }
                bottom = RandomUtils.uniform(rand, 1, 5);
                farLeft = RandomUtils.uniform(rand, 3, 9);
                origin = new XYPosn(entry.getX() - farLeft, entry.getY() - bottom, world);
                numCollide += 1;
            }
        } else {
            int bottom = RandomUtils.uniform(rand, 3, 9);
            int farLeft = RandomUtils.uniform(rand, 1, 5);
            origin = new XYPosn(entry.getX() - farLeft, entry.getY() - bottom, world);
            while (!validate(origin) || world[origin.getX()][origin.getY()] != Tileset.NOTHING) {
                if (numCollide > 200) {
                    return null;
                }
                bottom = RandomUtils.uniform(rand, 3, 9);
                farLeft = RandomUtils.uniform(rand, 1, 5);
                origin = new XYPosn(entry.getX() - farLeft, entry.getY() - bottom, world);
                numCollide += 1;
            }
        }
        return origin;
    }

    /** For a random yet valid room from entry, this gives room's origin, length & width.
     * @param rand Random seed
     * @param entry Point of entry to room
     * @param orientation Orientation of Hallway from which room originates
     * @return Random RoomStuff instance for a valid room that stores origin, length & width
     */
    public RoomStuff randomRoom(Random rand, XYPosn entry, int orientation) {
        int length, width;
        RoomStuff room = null;
        int numCollide = 0;
        while (collision(room)) {
            XYPosn origin = randomOrigin(rand, entry, orientation);
            if ((numCollide > 500) || (origin == null)) {
                return null;
            }
            if (orientation == 0) {
                length = RandomUtils.uniform(rand, entry.getY() - origin.getY() + 2, 9);
                width = RandomUtils.uniform(rand, 3, 9);
            } else if (orientation == 90) {
                length = RandomUtils.uniform(rand, 3, 9);
                width = RandomUtils.uniform(rand, entry.getX() - origin.getX() + 2, 9);
            } else if (orientation == 180) {
                length = RandomUtils.uniform(rand, entry.getY() - origin.getY() + 2, 9);
                width = entry.getX() - origin.getX();
            } else {
                length = entry.getY() - origin.getY();
                width = RandomUtils.uniform(rand, entry.getX() - origin.getX() + 2, 9);
            }
            room = new RoomStuff(origin, length, width);
            numCollide += 1;
        }
        return room;
    }

    /** Checks whether any room is possible from given point of entry.
     * @param entry Point of entry to room
     * @param orientation Orientation of Hallway from which a room could originate
     * @return Can the smallest possible room be created?
     */
    boolean smallestRoomImpossible(XYPosn entry, int orientation) {
        RoomStuff room1, room2, room3, room4;
        if (orientation == 0) {
            room1 = new RoomStuff(new XYPosn(entry.getX(), entry.getY() - 1, world), 3, 3);
            room2 = new RoomStuff(new XYPosn(entry.getX(), entry.getY() - 2, world), 3, 3);
            room3 = new RoomStuff(new XYPosn(entry.getX(), entry.getY() + 1, world), 3, 3);
            room4 = new RoomStuff(new XYPosn(entry.getX(), entry.getY() + 2, world), 3, 3);
        } else if (orientation == 90) {
            room1 = new RoomStuff(new XYPosn(entry.getX() - 2, entry.getY(), world), 3, 3);
            room2 = new RoomStuff(new XYPosn(entry.getX() - 1, entry.getY(), world), 3, 3);
            room3 = new RoomStuff(new XYPosn(entry.getX() + 1, entry.getY(), world), 3, 3);
            room4 = new RoomStuff(new XYPosn(entry.getX() + 2, entry.getY(), world), 3, 3);
        } else if (orientation == 180) {
            room1 = new RoomStuff(new XYPosn(entry.getX() - 3, entry.getY() - 1, world), 3, 3);
            room2 = new RoomStuff(new XYPosn(entry.getX() - 3, entry.getY() - 2, world), 3, 3);
            room3 = new RoomStuff(new XYPosn(entry.getX() - 3, entry.getY() + 1, world), 3, 3);
            room4 = new RoomStuff(new XYPosn(entry.getX() - 3, entry.getY() + 2, world), 3, 3);
        } else {
            room1 = new RoomStuff(new XYPosn(entry.getX() - 1, entry.getY() - 3, world), 3, 3);
            room2 = new RoomStuff(new XYPosn(entry.getX() - 2, entry.getY() - 3, world), 3, 3);
            room3 = new RoomStuff(new XYPosn(entry.getX() + 1, entry.getY() - 3, world), 3, 3);
            room4 = new RoomStuff(new XYPosn(entry.getX() + 2, entry.getY() - 3, world), 3, 3);
        }
        return collision(room1) && collision(room2) && collision(room3) && collision(room4);
    }

    /** Creates and adds a random room using Room class and randomRoom method
     * @param rand Random seed
     * @param entry Point of entry to room
     * @param orientation Orientation of hallway from which room originates
     * @return List of 1 to 3 hallways that could shoot from the room
     */
    public ArrayList<XYPosn> addMultiSpringRoom(Random rand, XYPosn entry, int orientation) {
        RoomStuff roomStuff = randomRoom(rand, entry, orientation);
        if (roomStuff == null)  {
            return null;
        }
        Room room = new Room(world, roomStuff.origin, entry, roomStuff.width, roomStuff.length);
        room.addRoom();
        int n = RandomUtils.uniform(rand, 1, 4);
        ArrayList<XYPosn> exits = new ArrayList<>();
        XYPosn wall;
        for (int i = 0; i < n; i += 1) {
            wall = room.getRandomWall(rand);
            if (!exits.contains(wall)) {
                exits.add(wall);
            }
        }
        return exits;
    }
}
