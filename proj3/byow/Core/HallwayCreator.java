package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class HallwayCreator {

    /** Characteristics of hallway. */
    private int length;
    private int xPos;
    private int yPos;
    private ArrayList<XYPosn> walls;

    /** Characteristics of World. */
    private TETile[][] world;
    private int width;
    private int height;
    private Random random;


    /** Creates a hallway of a particular type, of specified length. */
    public HallwayCreator(Random random, TETile[][] world, int xpos, int ypos, int len, int type, int total) {
        if ((xpos < 1) || (xpos >= world.length - 1)) {
            throw new IndexOutOfBoundsException("X-Position specified is out of bounds: " + xpos);
        }
        if ((ypos < 1) || (ypos >= world[0].length - 1)) {
            throw new IndexOutOfBoundsException("Y-Position specified is out of bounds: " + ypos);
        }
        XYPosn nextPos = hallwayConstructor(random, world, xpos, ypos, len, type);
        /* world = createOffspring(nextPos, type, total); */
    }

    /** Creates a hallway of a particular type, of random length. */
    public HallwayCreator(Random random, TETile[][] world, int xpos, int ypos, int type, int total) {
        if ((xpos < 1) || (xpos >= world.length - 1)) {
            throw new IndexOutOfBoundsException("X-Position specified is out of bounds: " + xpos);
        }
        if ((ypos < 1) || (ypos >= world[0].length - 1)) {
            throw new IndexOutOfBoundsException("Y-Position specified is out of bounds: " + ypos);
        }

        int lengthGeom = RandomUtils.geometric(random, 0.4) + 2;
        XYPosn nextPos = hallwayConstructor(random, world, xpos, ypos, lengthGeom, type);
        world = createOffspring(nextPos, type, total);
    }

    private XYPosn hallwayConstructor(Random rand, TETile[][] worldArr, int xpos, int ypos, int len, int type) {
        this.xPos = xpos;
        this.yPos = ypos;
        this.length = len;
        this.world = worldArr;
        this.width = world.length;
        this.height = world[0].length;
        this.walls = new ArrayList<>();
        this.random = rand;

        XYPosn nextPos = switch (type) {
            case 0 -> addUpHallway();
            case 1 -> addDownHallway();
            case 2 -> addLeftHallway();
            case 3 -> addRightHallway();
            default -> throw new NullPointerException("Type Received Not Valid: " + type);
        };
        return nextPos;
    }

    TETile[][] createOffspring(XYPosn nextPos, int prevType, int total) {
        xPos = nextPos.getX();
        yPos = nextPos.getY();
        if (total <= 0) {
            TETile[][] finalWorld = nextPos.getWorld();
            if (!finalWorld[xPos][yPos].equals(Tileset.FLOOR)) {
                finalWorld[xPos][yPos] = Tileset.WALL;
            }
            switch (prevType) {
                case 0, 1 -> {
                    if (!finalWorld[xPos + 1][yPos].equals(Tileset.FLOOR)) {
                        finalWorld[xPos + 1][yPos] = Tileset.WALL;
                    }
                    if (!finalWorld[xPos - 1][yPos].equals(Tileset.FLOOR)) {
                        finalWorld[xPos - 1][yPos] = Tileset.WALL;
                    }
                }
                case 2, 3 -> {
                    if (!finalWorld[xPos][yPos + 1].equals(Tileset.FLOOR)) {
                        finalWorld[xPos][yPos + 1] = Tileset.WALL;
                    }
                    if (!finalWorld[xPos][yPos - 1].equals(Tileset.FLOOR)) {
                        finalWorld[xPos][yPos - 1] = Tileset.WALL;
                    }
                }
                default -> throw new NullPointerException("Type Received Not Valid: " + prevType);
            }
            return finalWorld;
        }

        int newType = typeChecker(prevType, xPos, yPos);

        HallwayCreator h = new HallwayCreator(random, world, nextPos.getX(), nextPos.getY(), newType, total - 1);
        return h.getWorld();
    }

    private int typeChecker(int prevType, int xPos, int yPos) {
        int newType;
        switch (prevType) {
            case 0, 1 -> {
                newType = List.of(2, 3).get(RandomUtils.uniform(random, 2));
                if ((newType == 2) && (xPos <= 1)) {
                    newType = 3;
                } else if ((newType == 3) && (xPos >= width - 2)) {
                    newType = 2;
                }
            }
            case 2, 3 -> {
                newType = List.of(0, 1).get(RandomUtils.uniform(random, 2));
                if ((newType == 1) && (yPos <= 1)) {
                    newType = 0;
                } else if ((newType == 1) && (yPos >= height - 2)) {
                    newType = 1;
                }
            }
            default -> throw new NullPointerException("Type Received Not Valid: " + prevType);
        }
        ;
        return newType;
    }



    XYPosn addUpHallway() {
        if ((yPos + length) >= height) {
            length = height - yPos - 1;
        }
        if ((xPos + 1 > width) || (xPos - 1 < 0)) {
            throw new IndexOutOfBoundsException();
        }

        if (!world[xPos][yPos - 1].equals(Tileset.FLOOR)) {
            world[xPos][yPos - 1] = Tileset.WALL;
        }
        if (!world[xPos - 1][yPos - 1].equals(Tileset.FLOOR)) {
            world[xPos - 1][yPos - 1] = Tileset.WALL;
        }
        if (!world[xPos + 1][yPos - 1].equals(Tileset.FLOOR)) {
            world[xPos + 1][yPos - 1] = Tileset.WALL;
        }

        for (int y = yPos; y < yPos + length; y += 1) {
            world[xPos][y] = Tileset.FLOOR;
            if (!world[xPos + 1][y].equals(Tileset.FLOOR)) {
                world[xPos + 1][y] = Tileset.WALL;
            }
            if (!world[xPos - 1][y].equals(Tileset.FLOOR)) {
                world[xPos - 1][y] = Tileset.WALL;
            }
            walls.add(new XYPosn(xPos + 1, y));
            walls.add(new XYPosn(xPos - 1, y));
        }
        return new XYPosn(xPos, yPos + length, world);
    }


    XYPosn addDownHallway() {
        if ((yPos - length) < 0) {
            length = yPos;
        }
        if ((xPos + 1 > width) || (xPos - 1 < 0)) {
            throw new IndexOutOfBoundsException();
        }

        if (!world[xPos][yPos + 1].equals(Tileset.FLOOR)) {
            world[xPos][yPos + 1] = Tileset.WALL;
        }
        world[xPos][yPos + 1] = Tileset.WALL;
        if (!world[xPos - 1][yPos + 1].equals(Tileset.FLOOR)) {
            world[xPos - 1][yPos + 1] = Tileset.WALL;
        }
        if (!world[xPos + 1][yPos + 1].equals(Tileset.FLOOR)) {
            world[xPos + 1][yPos + 1] = Tileset.WALL;
        }

        for (int y = yPos; y > yPos - length; y -= 1) {
            world[xPos][y] = Tileset.FLOOR;
            if (!world[xPos + 1][y].equals(Tileset.FLOOR)) {
                world[xPos + 1][y] = Tileset.WALL;
            }
            if (!world[xPos - 1][y].equals(Tileset.FLOOR)) {
                world[xPos - 1][y] = Tileset.WALL;
            }
            walls.add(new XYPosn(xPos + 1, y));
            walls.add(new XYPosn(xPos - 1, y));
        }

        return new XYPosn(xPos, yPos - length, world);
    }


    XYPosn addLeftHallway() {
        if ((xPos - length) < 0) {
            length = xPos;
        }
        if ((yPos + 1 > height) || (yPos - 1 < 0)) {
            throw new IndexOutOfBoundsException();
        }

        if (!world[xPos + 1][yPos].equals(Tileset.FLOOR)) {
            world[xPos + 1][yPos] = Tileset.WALL;
        }
        if (!world[xPos + 1][yPos - 1].equals(Tileset.FLOOR)) {
            world[xPos + 1][yPos - 1] = Tileset.WALL;
        }
        if (!world[xPos + 1][yPos + 1].equals(Tileset.FLOOR)) {
            world[xPos + 1][yPos + 1] = Tileset.WALL;
        }

        for (int x = xPos; x > xPos - length; x -= 1) {
            world[x][yPos] = Tileset.FLOOR;
            if (!world[x][yPos + 1].equals(Tileset.FLOOR)) {
                world[x][yPos + 1] = Tileset.WALL;
            }
            if (!world[x][yPos - 1].equals(Tileset.FLOOR)) {
                world[x][yPos - 1] = Tileset.WALL;
            }
            walls.add(new XYPosn(x, yPos + 1));
            walls.add(new XYPosn(x, yPos - 1));
        }
        return new XYPosn(xPos - length, yPos, world);
    }



    XYPosn addRightHallway() {
        if ((xPos + length) >= width) {
            length = width - xPos - 1;
        }
        if ((yPos + 1 > height) || (yPos - 1 < 0)) {
            throw new IndexOutOfBoundsException();
        }
        if (!world[xPos - 1][yPos].equals(Tileset.FLOOR)) {
            world[xPos - 1][yPos] = Tileset.WALL;
        }
        if (!world[xPos - 1][yPos - 1].equals(Tileset.FLOOR)) {
            world[xPos - 1][yPos - 1] = Tileset.WALL;
        }
        if (!world[xPos - 1][yPos + 1].equals(Tileset.FLOOR)) {
            world[xPos - 1][yPos + 1] = Tileset.WALL;
        }

        for (int x = xPos; x < xPos + length; x += 1) {
            world[x][yPos] = Tileset.FLOOR;
            if (!world[x][yPos + 1].equals(Tileset.FLOOR)) {
                world[x][yPos + 1] = Tileset.WALL;
            }
            if (!world[x][yPos - 1].equals(Tileset.FLOOR)) {
                world[x][yPos - 1] = Tileset.WALL;
            }
            walls.add(new XYPosn(x, yPos + 1));
            walls.add(new XYPosn(x, yPos - 1));
        }
        return new XYPosn(xPos + length, yPos, world);
    }


    ArrayList<XYPosn> getWalls() {
        return walls;
    }

    TETile[][] getWorld() {
        return world;
    }

}
