package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import edu.princeton.cs.introcs.StdDraw;

import java.util.ArrayList;
import java.util.Random;

public class Avatar {
    private final TETile[][] world;
    private final TETile appearance;
    private XYPosn position;

    public Avatar(TETile[][] world, TETile appearance, XYPosn position) {
        this.world = world;
        this.appearance = appearance;
        world[position.getX()][position.getY()] = appearance;
        this.position = position;
    }

    private XYPosn up(TETile[][] world, XYPosn pos) {
        if (pos.getY() + 1 == world[0].length) {
            return null;
        } else {
            return new XYPosn(pos.getX(), pos.getY() + 1, world);
        }
    }

    private XYPosn down(TETile[][] world, XYPosn pos) {
        if (pos.getY() == 0) {
            return null;
        } else {
            return new XYPosn(pos.getX(), pos.getY() - 1, world);
        }
    }

    private XYPosn right(TETile[][] world, XYPosn pos) {
        if (pos.getX() + 1 == world.length) {
            return null;
        } else {
            return new XYPosn(pos.getX() + 1, pos.getY(), world);
        }
    }

    private XYPosn left(TETile[][] world, XYPosn pos) {
        if (pos.getX() == 0) {
            return null;
        } else {
            return new XYPosn(pos.getX() - 1, pos.getY(), world);
        }
    }

    public void move(char key) {
        if (key == 'w') {
            XYPosn up = up(world, position);
            if (up != null && world[up.getX()][up.getY()] == Tileset.FLOOR) {
                world[position.getX()][position.getY()] = Tileset.FLOOR;
                position = up;
                world[up.getX()][up.getY()] = appearance;
            }
        } else if (key == 's') {
            XYPosn down = down(world, position);
            if (down != null && world[down.getX()][down.getY()] == Tileset.FLOOR) {
                world[position.getX()][position.getY()] = Tileset.FLOOR;
                position = down;
                world[down.getX()][down.getY()] = appearance;
            }
        } else if (key == 'd') {
            XYPosn right = right(world, position);
            if (right != null && world[right.getX()][right.getY()] == Tileset.FLOOR) {
                world[position.getX()][position.getY()] = Tileset.FLOOR;
                position = right;
                world[right.getX()][right.getY()] = appearance;
            }
        } else if (key == 'a') {
            XYPosn left = left(world, position);
            if (left != null && world[left.getX()][left.getY()] == Tileset.FLOOR) {
                world[position.getX()][position.getY()] = Tileset.FLOOR;
                position = left;
                world[left.getX()][left.getY()] = appearance;
            }
        }
    }

    public static void main(String[] args) {
        Engine e = new Engine();
        e.initialize();
        e.interactWithInputString("n3005s");
        TETile[][] w = e.getWorld();
        ArrayList<XYPosn> floors = new ArrayList<>();
        for (int x = 0; x < w.length; x += 1) {
            for (int y = 0; y < w[0].length; y += 1) {
                if (w[x][y] == Tileset.FLOOR) {
                    floors.add(new XYPosn(x, y, w));
                }
            }
        }
        Random rand = new Random(5000);
        XYPosn init1 = floors.get(RandomUtils.uniform(rand, 0, floors.size()));
        XYPosn init2 = floors.get(RandomUtils.uniform(rand, 0, floors.size()));
        Avatar player = new Avatar(w, Tileset.PLAYER, init1);
        Avatar ghost = new Avatar(w, Tileset.GHOST, init2);
        ArrayList<Character> input = new ArrayList<>();
        char[] keys = "wasd".toCharArray();
        char ghostKey;
        e.render();
        while (input.size() < 100) {
            if (StdDraw.hasNextKeyTyped()) {
                input.add(StdDraw.nextKeyTyped());
                player.move(input.get(input.size() - 1));
                XYPosn oldPos = ghost.position;
                while (ghost.position == oldPos) {
                    ghostKey = keys[RandomUtils.uniform(rand, 0, keys.length)];
                    ghost.move(ghostKey);
                    e.render();
                }
            }
        }
    }
}
