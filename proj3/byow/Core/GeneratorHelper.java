package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

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
    }

    private boolean collision(RoomStuff room) {
        if (room == null) {
            return true;
        }
        int x0 = room.origin.getX();
        int y0 = room.origin.getY();
        for (int x = x0; x <= x0 + room.width; x += 1) {
            for (int y = y0; x <= y0 + room.length; y += 1) {
                if (world[x][y] != Tileset.NOTHING) {
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
            int bottom = RandomUtils.uniform(rand, 0, 4);
            origin = new XYPosn(entry.getX(), entry.getY() - bottom, world);
            while (!validate(origin) || world[origin.getX()][origin.getY()] != Tileset.NOTHING) {
                bottom = RandomUtils.uniform(rand, 0, 4);
                origin = new XYPosn(entry.getX(), entry.getY() - bottom, world);
            }
        } else if (orientation == 90) {
            int farLeft = RandomUtils.uniform(rand, 0, 4);
            origin = new XYPosn(entry.getX() - farLeft, entry.getY(), world);
            while (!validate(origin) || world[origin.getX()][origin.getY()] != Tileset.NOTHING) {
                farLeft = RandomUtils.uniform(rand, 0, 4);
                origin = new XYPosn(entry.getX() - farLeft, entry.getY(), world);
            }
        } else {
            int bottom = RandomUtils.uniform(rand, 1, 4);
            int farLeft = RandomUtils.uniform(rand, 1, 4);
            origin = new XYPosn(entry.getX() - farLeft, entry.getY() - bottom, world);
            while (!validate(origin) || world[origin.getX()][origin.getY()] != Tileset.NOTHING) {
                bottom = RandomUtils.uniform(rand, 1, 4);
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
                length = RandomUtils.uniform(rand, entry.getY() - origin.getY() + 1, 6);
                width = RandomUtils.uniform(rand, 1, 6);
            } else if (orientation == 90) {
                length = RandomUtils.uniform(rand, 1, 6);
                width = RandomUtils.uniform(rand, entry.getX() - origin.getX() + 1, 6);
            } else if (orientation == 180) {
                length = RandomUtils.uniform(rand, entry.getY() - origin.getY() + 1, 6);
                width = entry.getX() - origin.getX();
            } else {
                length = entry.getY() - origin.getY();
                width = RandomUtils.uniform(rand, entry.getX() - origin.getX() + 1, 6);
            }
            room = new RoomStuff();
            room.length = length;
            room.width = width;
            room.origin = origin;
        }
        return room;
    }
}
