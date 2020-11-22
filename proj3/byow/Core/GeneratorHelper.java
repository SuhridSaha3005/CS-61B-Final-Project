package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.ArrayList;
import java.util.Random;

public class GeneratorHelper {
    private TETile[][] world;

    public GeneratorHelper(TETile[][] world) {
        this.world = world;
    }
    public static class RoomStuff {
        public XYPosn origin;
        public int length;
        public int width;
        public RoomStuff(XYPosn origin, int length, int width) {
            this.origin = origin;
            this.length = length;
            this.width = width;
        }
    }

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

    private boolean validate(XYPosn point) {
        return point.getX() >= 0 && point.getX() < world.length && point.getY() >= 0 && point.getY() < world[0].length;
    }

    private XYPosn randomOrigin(Random rand, XYPosn entry, int orientation) {
        XYPosn origin;
        if (orientation == 0) {
            int bottom = RandomUtils.uniform(rand, 1, 4);
            origin = new XYPosn(entry.getX(), entry.getY() - bottom, world);
            while (!validate(origin) || world[origin.getX()][origin.getY()] != Tileset.NOTHING) {
                bottom = RandomUtils.uniform(rand, 1, 4);
                origin = new XYPosn(entry.getX(), entry.getY() - bottom, world);
            }
        } else if (orientation == 90) {
            int farLeft = RandomUtils.uniform(rand, 1, 4);
            origin = new XYPosn(entry.getX() - farLeft, entry.getY(), world);
            while (!validate(origin) || world[origin.getX()][origin.getY()] != Tileset.NOTHING) {
                farLeft = RandomUtils.uniform(rand, 1, 4);
                origin = new XYPosn(entry.getX() - farLeft, entry.getY(), world);
            }
        } else if (orientation == 180) {
            int bottom = RandomUtils.uniform(rand, 1, 4);
            int farLeft = RandomUtils.uniform(rand, 3, 7);
            origin = new XYPosn(entry.getX() - farLeft, entry.getY() - bottom, world);
            while (!validate(origin) || world[origin.getX()][origin.getY()] != Tileset.NOTHING) {
                bottom = RandomUtils.uniform(rand, 1, 4);
                farLeft = RandomUtils.uniform(rand, 3, 7);
                origin = new XYPosn(entry.getX() - farLeft, entry.getY() - bottom, world);
            }
        } else {
            int bottom = RandomUtils.uniform(rand, 3, 7);
            int farLeft = RandomUtils.uniform(rand, 1, 4);
            origin = new XYPosn(entry.getX() - farLeft, entry.getY() - bottom, world);
            while (!validate(origin) || world[origin.getX()][origin.getY()] != Tileset.NOTHING) {
                bottom = RandomUtils.uniform(rand, 3, 7);
                farLeft = RandomUtils.uniform(rand, 1, 4);
                origin = new XYPosn(entry.getX() - farLeft, entry.getY() - bottom, world);
            }
        }
        return origin;
    }

    public RoomStuff randomRoom(Random rand, XYPosn entry, int orientation) {
        int length, width;
        RoomStuff room = null;
        while (collision(room)) {
            XYPosn origin = randomOrigin(rand, entry, orientation);
            if (orientation == 0) {
                length = RandomUtils.uniform(rand, entry.getY() - origin.getY() + 2, 7);
                width = RandomUtils.uniform(rand, 3, 7);
            } else if (orientation == 90) {
                length = RandomUtils.uniform(rand, 3, 7);
                width = RandomUtils.uniform(rand, entry.getX() - origin.getX() + 2, 7);
            } else if (orientation == 180) {
                length = RandomUtils.uniform(rand, entry.getY() - origin.getY() + 2, 7);
                width = entry.getX() - origin.getX();
            } else {
                length = entry.getY() - origin.getY();
                width = RandomUtils.uniform(rand, entry.getX() - origin.getX() + 2, 7);
            }
            room = new RoomStuff(origin, length, width);
        }
        return room;
    }

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
        if (collision(room1) && collision(room2) && collision(room3) && collision(room4)) {
            System.out.println(entry.getX() + " " + entry.getY() + " " + orientation);
        }
        return collision(room1) && collision(room2) && collision(room3) && collision(room4);
    }

    public ArrayList<XYPosn> addMultiSpringRoom(Random rand, XYPosn entry, int orientation) {
        RoomStuff roomStuff = randomRoom(rand, entry, orientation);
        Room room = new Room(world, roomStuff.origin, entry, roomStuff.width, roomStuff.length);
        room.addRoom();
        int n = RandomUtils.uniform(rand, 1, 3);
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
