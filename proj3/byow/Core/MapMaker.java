package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class MapMaker {

    /** Characteristics of World. */
    private TETile[][] world;

    /** Width of World. */
    private final int width;

    /** Height of World. */
    private final int height;

    /** Random Class of particular seed. */
    private Random random;

    /** GeneratorHelper for this mapmaker. */
    private GeneratorHelper genHelp;

    /** Checks if tile isNothing.
     * @param x x
     * @param y y
     * @return is it nothing?
     */
    private boolean isNothing(int x, int y) {
        if (validate(new XYPosn(x, y))) {
            return world[x][y].equals(Tileset.NOTHING);
        }
        return false;
    }

    /** Checks if tile isWall.
     * @param x x
     * @param y y
     * @return is it a wall?
     */
    private boolean isWall(int x, int y) {
        if (validate(new XYPosn(x, y))) {
            return world[x][y].equals(Tileset.WALL);
        }
        return false;
    }

    /** Checks if tile isWall. */
    private boolean isFloor(int x, int y) {
        if (validate(new XYPosn(x, y))) {
            return world[x][y].equals(Tileset.FLOOR);
        }
        return false;
    }

    /** Checks if tile is NOT isWall and isFloor. */
    private boolean isWallFloor(int x, int y) {
        return (!isWall(x, y) && !isFloor(x, y));
    }

    /** Replaces a block IF it is nothing.
     * @param x x
     * @param y y
     * @param tile tile to replace with
     */
    private void replaceBlockIfNothing(int x, int y, TETile tile) {
        if (validate(new XYPosn(x, y)) && isNothing(x, y)) {
            world[x][y] = tile;
        }
    }

    /** Replaces a block if it is not a wall/floor.
     * @param x x
     * @param y y
     * @param tile tile to replace with
     */
    private void replaceBlockIfNotWallFloor(int x, int y, TETile tile) {
        if (validate(new XYPosn(x, y)) && !isWallFloor(x, y)) {
            world[x][y] = tile;
        }
    }

    /** Replaces a block --force.
     * @param x x
     * @param y y
     * @param tile tile to replace with
     */
    private void replaceBlock(int x, int y, TETile tile) {
        if (validate(new XYPosn(x, y))) {
            world[x][y] = tile;
        }
    }

    /** Constructor for MapMaker Class.
     * @param rand random java with seed
     * @param wrld world
     * @param w width of world
     * @param h height of world
     */
    public MapMaker(Random rand, TETile[][] wrld, int w, int h) {
        world = wrld;
        random = rand;
        width = w;
        height = h;
        genHelp= new GeneratorHelper(world);
    }

    /** Puts everything together, takes the map for the world. */
    void makeMap() {
        int xStart = RandomUtils.uniform(random, width / 2 - width/4, width/2 + width/4);
        int yStart = RandomUtils.uniform(random, height / 2 - height / 4, height / 2 + height / 4);
        XYPosn entry = new XYPosn(xStart, yStart);
        System.out.println("Creating the initial hallway.");
        System.out.print("Orientation: ");
        System.out.println(getOrientation(entry));


        System.out.print("Position: ");
        System.out.print(entry.getX());
        System.out.print(" ");
        System.out.println(entry.getY());

        List<XYPosn> k = addMultiSpringHallways(entry);

        System.out.println("***************************************");
        singlePathMaker(k);
        makeFlowersIntoWalls();
    }

    /** Hack to make flowers into walls. */
    void makeFlowersIntoWalls() {
        for (int i = 0; i < width; i += 1) {
            for (int j = 0; j < height; j += 1) {
                if (world[i][j].equals(Tileset.FLOWER)) {
                    world[i][j] = Tileset.WALL;
                }
            }
        }
    }

    /** Creates a hallway that returns positions of new hallways to offspring.
     * @param entry entry
     * @return list of xyposns
     */
    List<XYPosn> addMultiSpringHallways(XYPosn entry) {
        List<List<Integer>> randHallwayParams = newHallwayDirLength(entry);
        List<XYPosn> newXYPosns = new ArrayList<>();

        if (randHallwayParams.isEmpty()) {
            System.out.println("Saw no way to proceed. Ending this line.");
            int xPos = entry.getX();
            int yPos = entry.getY();
            int orient = getOrientation(entry);
            replaceBlockIfNothing(xPos, yPos, Tileset.WALL);
            switch (orient) {
            case 0, 180 -> {
                replaceBlockIfNothing(xPos, yPos + 1, Tileset.WALL);
                replaceBlockIfNothing(xPos, yPos - 1, Tileset.WALL);
                }
            case 90, 270 -> {
                replaceBlockIfNothing(xPos + 1, yPos, Tileset.WALL);
                replaceBlockIfNothing(xPos - 1, yPos, Tileset.WALL);
                }
            }
            return null;
        }

        for (List<Integer> hallParams: randHallwayParams) {
            int dir = hallParams.get(0);
            int len = hallParams.get(1);
            XYPosn newXYPosn = hallwayMaker(entry, len, dir);
            if (newXYPosn != null)
                newXYPosns.add(newXYPosn);
        }

        return newXYPosns;
    }

    /** Makes a single branch of the branching offsprings.
     * @param k list of next positions
     */
    void singlePathMaker(List<XYPosn> k) {
        if (k == null) {
            return;
        }
        for (XYPosn entry : k) {

            int o = (getOrientation(entry));
            boolean room = (RandomUtils.uniform(random, 2) == 0);
            genHelp = new GeneratorHelper(world);

            System.out.print("Orientation: ");
            System.out.println(o);

            System.out.print("Room LuckyVar: ");
            System.out.println(room);

            System.out.print("SmallestRoomImpossible: ");
            System.out.println(genHelp.smallestRoomImpossible(entry, o));

            System.out.print("Position: ");
            System.out.print(entry.getX());
            System.out.print(" ");
            System.out.println(entry.getY());



            if ((o >= 0) && !genHelp.smallestRoomImpossible(entry, o)) {
                if (room) {
                    System.out.println("Trying to make room!");
                    k = genHelp.addMultiSpringRoom(random, entry, o);
                    if (k == null) {
                        k = addMultiSpringHallways(entry);
                    } else {
                        System.out.println("Making the mandatory hallway after room!");
                        singlePathMaker(k, false);
                    }
                } else {
                    System.out.println("Creating Hallway!");
                    k = addMultiSpringHallways(entry);
                }
            } else {
                System.out.println("Creating Hallway!");
                k = addMultiSpringHallways(entry);
            }
            System.out.println("**************************************");
            singlePathMaker(k);
        }
    }

    /** Same as above function, but specifies that we don't want a room.
     * @param k List of new XYPosns
     * @param room false
     */
    void singlePathMaker(List<XYPosn> k, boolean room) {
        if (k == null) {
            return;
        }
        for (XYPosn entry : k) {

            int o = (getOrientation(entry));
            genHelp = new GeneratorHelper(world);

            System.out.print("Orientation: ");
            System.out.println(o);

            System.out.print("Room LuckyVar: ");
            System.out.println(room);

            System.out.print("SmallestRoomImpossible: ");
            System.out.println(genHelp.smallestRoomImpossible(entry, o));

            System.out.print("Position: ");
            System.out.print(entry.getX());
            System.out.print(" ");
            System.out.println(entry.getY());



            if ((o >= 0) && !genHelp.smallestRoomImpossible(entry, o)) {
                if (room) {
                    System.out.println("Trying to make room!");
                    k = genHelp.addMultiSpringRoom(random, entry, o);
                    if (k == null) {
                        k = addMultiSpringHallways(entry);
                    } else {
                        System.out.println("Making the mandatory hallway after room!");
                        singlePathMaker(k, false);
                    }
                } else {
                    System.out.println("Creating Hallway!");
                    k = addMultiSpringHallways(entry);
                }
            } else {
                System.out.println("Creating Hallway!");
                k = addMultiSpringHallways(entry);
            }
            System.out.println("**************************************");
            singlePathMaker(k);
        }
    }


    /** Finds the best hallway direction given an entry, and its length, and randomizes.
     * @param entry starting posn
     * @return list of keys and values
     */
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

        int numSpring = RandomUtils.geometric(random, 0.2);
        List<List<Integer>> offSpringParams = new ArrayList<>();

        if (!keyList.isEmpty()) {
            for (int j = 0; j < Math.min(numSpring, keyList.size()); j += 1) {
                ArrayList<Integer> best = new ArrayList<>();
                int idx = RandomUtils.uniform(random, keyList.size());
                best.add(keyList.get(idx));
                best.add(Math.min(RandomUtils.geometric(random, 0.7) + 2, valueList.get(idx)));
                offSpringParams.add(best);
                keyList.remove(idx);
                valueList.remove(idx);
            }
        }
        return offSpringParams;
    }

    /** Returns longest possible length of a hallway from a point, not including it.
     * @param entry entry position
     * @param dir direction
     * @return
     */
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


    /** Makes a hallway given position, length and direction.
     * @param entry entry position
     * @param length length of hallway
     * @param dir direction of hallway
     * @return position of next hallway/room to be made
     */
    XYPosn hallwayMaker(XYPosn entry, int length, int dir) {
        if (!validate(entry)) {
            return null;
        }

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
            break;
        }
        return new XYPosn(xPos, yPos);
    }

    /** Validates if a point is within the map.
     * @param point point to be validated
     * @return whether point is valid
     */
    private boolean validate(XYPosn point) {
        return point.getX() >= 0 && point.getX() < world.length && point.getY() >= 0 && point.getY() < world[0].length;
    }

    /** Gets the world.
     * @return world
     */
    TETile[][] getWorld() {
        return world;
    }

    /** Gets the GeneratorHelper
     * @return genHelper
     */
    GeneratorHelper getGenHelp() {
        return genHelp;
    }

    /** Gets the predicted orientation of a hallway/room up to this point.
     * @param entry point to be tested
     * @return orientation of new room made at that point
     */
    int getOrientation(XYPosn entry) {
        int xPos = entry.getX();
        int yPos = entry.getY();
        int orientation = -1;
        int floors = 0;
        if (validate(new XYPosn(xPos, yPos - 1))) {
            TETile bottom = world[xPos][yPos - 1];
            if (bottom.equals(Tileset.FLOOR)) {
                orientation = 90;
                floors += 1;
            }
        }
        if (validate(new XYPosn(xPos, yPos + 1))) {
            TETile top = world[xPos][yPos + 1];
            if (top.equals(Tileset.FLOOR)) {
                orientation = 270;
                floors += 1;
            }
        }
        if (validate(new XYPosn(xPos - 1, yPos))) {
            TETile left = world[xPos - 1][yPos];
            if (left.equals(Tileset.FLOOR)) {
                orientation = 0;
                floors += 1;
            }
        }
        if (validate(new XYPosn(xPos + 1, yPos))) {
            TETile right = world[xPos + 1][yPos];
            if (right.equals(Tileset.FLOOR)) {
                orientation = 180;
                floors += 1;
            }
        }

        if (floors > 1) {
            return -1;
        } else {
            return orientation;
        }

    }

}
