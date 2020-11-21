package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MapMaker {

    /** Characteristics of World. */
    private TETile[][] world;
    private final int width;
    private final int height;
    private Random random;


    private boolean isNothing(int x, int y) {
        return world[x][y].equals(Tileset.NOTHING);
    }

    private boolean isWall(int x, int y) {
        return world[x][y].equals(Tileset.WALL);
    }

    private boolean isFloor(int x, int y) {
        return world[x][y].equals(Tileset.FLOOR);
    }

    private boolean isWallFloor(int x, int y) {
        return (!isWall(x, y) && !isFloor(x, y));
    }

    /** Replaces a block IF it is nothing. */
    private void replaceBlockIfNothing(int x, int y, TETile tile) {
        if (isNothing(x, y)) {
            world[x][y] = tile;
        }
    }

    private void replaceBlock(int x, int y, TETile tile) {
            world[x][y] = tile;
    }

    /** Constructor for MapMaker Class. */
    public MapMaker(Random rand, TETile[][] wrld, int w, int h) {
        world = wrld;
        random = rand;
        width = w;
        height = h;
    }

    /* XYPosn addHallway(XYPosn entry) {
        List<Integer> randHallwayParams = newHallwayDirLength(entry);
        if (randHallwayParams.isEmpty()) {
            throw new NullPointerException("Saw no way to proceed. Ending.");
        }
        int dir = randHallwayParams.get(0);
        int len = randHallwayParams.get(1);
        return hallwayMaker(entry, len, dir);
    } */

    void makeMap() {
        int xStart = RandomUtils.uniform(random, 3, width - 4);
        int yStart = RandomUtils.uniform(random, 3, height - 4);
        List<XYPosn> k = addMultiSpringHallways(new XYPosn(40, 10));
        singlePathMaker(k);
    }

    List<XYPosn> addMultiSpringHallways(XYPosn entry) {
        List<List<Integer>> randHallwayParams = newHallwayDirLength(entry);
        List<XYPosn> newXYPosns = new ArrayList<>();

        if (randHallwayParams.isEmpty()) {
            System.out.println("Saw no way to proceed. Ending this line.");
            return null;
        }

        for (List<Integer> hallParams: randHallwayParams) {
            int dir = hallParams.get(0);
            int len = hallParams.get(1);
            newXYPosns.add(hallwayMaker(entry, len, dir));
        }

        return newXYPosns;
    }

    void singlePathMaker(List<XYPosn> k) {
        for (int i = 0; i < 10; i += 1) {
            if (k == null) {
                continue;
            }
            for (XYPosn entry : k) {
                k = addMultiSpringHallways(entry);
                singlePathMaker(k);
            }
        }
    }


    /** Finds the best hallway direction given an entry, and its length, and randomizes. */
    List<List<Integer>> newHallwayDirLength(XYPosn entry) {
        ArrayList<Integer> keyList = new ArrayList<>();
        keyList.add(0);
        keyList.add(90);
        keyList.add(180);
        keyList.add(270);

        ArrayList<Integer> valueList = new ArrayList<>();
        valueList.add(longestHallway(entry, 0));
        valueList.add(longestHallway(entry, 90));
        valueList.add(longestHallway(entry, 180));
        valueList.add(longestHallway(entry, 270));

        int i = 0;
        while (valueList.contains(0)) {
            if (valueList.get(i) == 0) {
                keyList.remove(i);
                valueList.remove(i);
                i -= 1;
            }
            i += 1;
        }

        int numSpring = RandomUtils.geometric(random, 0.7);
        List<List<Integer>> offSpringParams = new ArrayList<>();

        if (!keyList.isEmpty()) {
            for (int j = 0; j < Math.min(numSpring, keyList.size()); j += 1) {
                ArrayList<Integer> best = new ArrayList<>();
                int idx = RandomUtils.uniform(random, keyList.size());
                System.out.println(idx);
                best.add(keyList.get(idx));
                best.add(Math.min(RandomUtils.geometric(random, 0.4) + 2, valueList.get(idx)));
                offSpringParams.add(best);
                keyList.remove(idx);
                valueList.remove(idx);
            }
        }
        return offSpringParams;
    }

    /** Returns longest possible length of a hallway from a point, not including it. */
    int longestHallway(XYPosn entry, int dir) {
        int xPos = entry.getX();
        int yPos = entry.getY();
        int length = 0;

        switch (dir) {
            case 0:
                while ((xPos <= width - 3) && isWallFloor(xPos + 1, yPos)) {
                    length += 1;
                    xPos += 1;
                }
                break;
            case 90:
                while ((yPos <= height - 3) && isWallFloor(xPos, yPos + 1)) {
                    length += 1;
                    yPos += 1;
                }
                break;
            case 180:
                while ((xPos >= 2) && isWallFloor(xPos - 1, yPos)) {
                    length += 1;
                    xPos -= 1;
                }
                break;
            case 270:
                while ((yPos >= 2) && isWallFloor(xPos, yPos - 1)) {
                    length += 1;
                    yPos -= 1;
                }
                break;
        }
        return length;
    }

    /** TESTING PURPOSES */
    void longestHallwayMaker(XYPosn entry, int dir) {
        hallwayMaker(entry, longestHallway(entry, dir), dir);
    }

    /** Makes a hallway given position, length and direction. */
    XYPosn hallwayMaker(XYPosn entry, int length, int dir) {
        int xPos = entry.getX();
        int yPos = entry.getY();
        XYPosn newPosn;

        if (length == 0) {
            replaceBlock(xPos, yPos, Tileset.FLOWER);
        }

        switch (dir) {
            case 0:
                replaceBlockIfNothing(xPos - 1, yPos + 1, Tileset.WALL);
                replaceBlockIfNothing(xPos - 1, yPos - 1, Tileset.WALL);
                replaceBlockIfNothing(xPos - 1, yPos, Tileset.WALL);
                while (length > 0) {
                    replaceBlock(xPos, yPos, Tileset.FLOOR);
                    replaceBlockIfNothing(xPos, yPos + 1, Tileset.WALL);
                    replaceBlockIfNothing(xPos, yPos - 1, Tileset.WALL);
                    xPos += 1;
                    length -= 1;
                }
                newPosn = new XYPosn(xPos, yPos);
                break;
            case 90:
                replaceBlockIfNothing(xPos + 1, yPos - 1, Tileset.WALL);
                replaceBlockIfNothing(xPos - 1, yPos - 1, Tileset.WALL);
                replaceBlockIfNothing(xPos, yPos - 1, Tileset.WALL);
                while (length > 0) {
                    replaceBlock(xPos, yPos, Tileset.FLOOR);
                    replaceBlockIfNothing(xPos - 1, yPos, Tileset.WALL);
                    replaceBlockIfNothing(xPos + 1, yPos, Tileset.WALL);
                    yPos += 1;
                    length -= 1;
                }
                newPosn = new XYPosn(xPos, yPos);
                break;
            case 180:
                replaceBlockIfNothing(xPos + 1, yPos + 1, Tileset.WALL);
                replaceBlockIfNothing(xPos + 1, yPos - 1, Tileset.WALL);
                replaceBlockIfNothing(xPos + 1, yPos, Tileset.WALL);
                while (length > 0) {
                    replaceBlock(xPos, yPos, Tileset.FLOOR);
                    replaceBlockIfNothing(xPos, yPos + 1, Tileset.WALL);
                    replaceBlockIfNothing(xPos, yPos - 1, Tileset.WALL);
                    xPos -= 1;
                    length -= 1;
                }
                newPosn = new XYPosn(xPos, yPos);
                break;
            case 270:
                replaceBlockIfNothing(xPos + 1, yPos + 1, Tileset.WALL);
                replaceBlockIfNothing(xPos - 1, yPos + 1, Tileset.WALL);
                replaceBlockIfNothing(xPos, yPos + 1, Tileset.WALL);
                while (length > 0) {
                    replaceBlock(xPos, yPos, Tileset.FLOOR);
                    replaceBlockIfNothing(xPos - 1, yPos, Tileset.WALL);
                    replaceBlockIfNothing(xPos + 1, yPos, Tileset.WALL);
                    yPos -= 1;
                    length -= 1;
                }
                newPosn = new XYPosn(xPos, yPos);
                break;
        }
        replaceBlock(xPos, yPos, Tileset.WALL);
        return new XYPosn(xPos, yPos);
    }

}
