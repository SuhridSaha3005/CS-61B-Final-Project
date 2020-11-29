package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.Random;

public class Avatar {
    private final TETile[][] world;
    private final TETile appearance;
    private XYPosn position;
    private TETile aboveTile;

    public Avatar(TETile[][] world, TETile appearance, XYPosn position) {
        this.world = world;
        this.appearance = appearance;
        world[position.getX()][position.getY()] = appearance;
        this.position = position;
        this.aboveTile = Tileset.FLOOR;
    }

    private XYPosn up() {
        if (position.getY() + 1 == world[0].length) {
            return null;
        } else {
            return new XYPosn(position.getX(), position.getY() + 1, world);
        }
    }

    private XYPosn down() {
        if (position.getY() == 0) {
            return null;
        } else {
            return new XYPosn(position.getX(), position.getY() - 1, world);
        }
    }

    private XYPosn right() {
        if (position.getX() + 1 == world.length) {
            return null;
        } else {
            return new XYPosn(position.getX() + 1, position.getY(), world);
        }
    }

    private XYPosn left() {
        if (position.getX() == 0) {
            return null;
        } else {
            return new XYPosn(position.getX() - 1, position.getY(), world);
        }
    }

    public void move(char key) {
        if (key == 'w') {
            XYPosn up = up();
            if (up != null && world[up.getX()][up.getY()] != Tileset.WALL) {
                world[position.getX()][position.getY()] = aboveTile;
                aboveTile = world[up.getX()][up.getY()];
                position = up;
                world[up.getX()][up.getY()] = appearance;
            }
        } else if (key == 's') {
            XYPosn down = down();
            if (down != null && world[down.getX()][down.getY()] != Tileset.WALL) {
                world[position.getX()][position.getY()] = aboveTile;
                aboveTile = world[down.getX()][down.getY()];
                position = down;
                world[down.getX()][down.getY()] = appearance;
            }
        } else if (key == 'd') {
            XYPosn right = right();
            if (right != null && world[right.getX()][right.getY()] != Tileset.WALL) {
                world[position.getX()][position.getY()] = aboveTile;
                aboveTile = world[right.getX()][right.getY()];
                position = right;
                world[right.getX()][right.getY()] = appearance;
            }
        } else if (key == 'a') {
            XYPosn left = left();
            if (left != null && world[left.getX()][left.getY()] != Tileset.WALL) {
                world[position.getX()][position.getY()] = aboveTile;
                aboveTile = world[left.getX()][left.getY()];
                position = left;
                world[left.getX()][left.getY()] = appearance;
            }
        }
        if (aboveTile.equals(Tileset.GHOST) || aboveTile.equals(Tileset.PLAYER)) {
            aboveTile = Tileset.FLOOR;
        }
    }

    static double distance(Avatar avatar1, Avatar avatar2) {
        return Math.sqrt(Math.pow(avatar1.position.getX() - avatar2.position.getX(), 2)
                + Math.pow(avatar1.position.getY() - avatar2.position.getY(), 2));
    }

    public void randomMove(Random rand) {
        XYPosn oldPos = position;
        while (position == oldPos) {
            char[] keys = "wasd".toCharArray();
            move(keys[RandomUtils.uniform(rand, 0, 4)]);
        }
    }

    public XYPosn getPosn() {
        return position;
    }
}
