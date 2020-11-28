package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import edu.princeton.cs.introcs.StdDraw;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
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
            if (up != null && world[up.getX()][up.getY()] == Tileset.FLOOR) {
                world[position.getX()][position.getY()] = Tileset.FLOOR;
                position = up;
                world[up.getX()][up.getY()] = appearance;
            }
        } else if (key == 's') {
            XYPosn down = down();
            if (down != null && world[down.getX()][down.getY()] == Tileset.FLOOR) {
                world[position.getX()][position.getY()] = Tileset.FLOOR;
                position = down;
                world[down.getX()][down.getY()] = appearance;
            }
        } else if (key == 'd') {
            XYPosn right = right();
            if (right != null && world[right.getX()][right.getY()] == Tileset.FLOOR) {
                world[position.getX()][position.getY()] = Tileset.FLOOR;
                position = right;
                world[right.getX()][right.getY()] = appearance;
            }
        } else if (key == 'a') {
            XYPosn left = left();
            if (left != null && world[left.getX()][left.getY()] == Tileset.FLOOR) {
                world[position.getX()][position.getY()] = Tileset.FLOOR;
                position = left;
                world[left.getX()][left.getY()] = appearance;
            }
        }
    }

    public XYPosn getPosition() {
        return position;
    }

    private static double metric(XYPosn pos1, XYPosn pos2) {
        return Math.pow(pos1.getX() - pos2.getX(), 2) + Math.pow(pos1.getY() - pos2.getY(), 2);
    }

    public void moveCloser(Avatar player) {
        HashMap<XYPosn, Character> directionsAll = new HashMap<>();
        directionsAll.put(up(), 'w');
        directionsAll.put(down(), 's');
        directionsAll.put(right(), 'd');
        directionsAll.put(left(), 'a');
        HashMap<XYPosn, Character> directions = new HashMap<>();
        for (XYPosn pos : directionsAll.keySet()) {
            if (pos != null && world[pos.getX()][pos.getY()] == Tileset.FLOOR) {
                directions.put(pos, directionsAll.get(pos));
            }
        }
        XYPosn nearest = new XYPosn(0, 0);
        double minDist = Double.POSITIVE_INFINITY;
        for (XYPosn pos : directions.keySet()) {
            if (metric(pos, player.position) < minDist) {
                nearest = pos;
            }
        }
        move(directions.get(nearest));
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
        XYPosn init3 = floors.get(RandomUtils.uniform(rand, 0, floors.size()));
        Avatar player = new Avatar(w, Tileset.PLAYER, init1);
        Avatar ghost = new Avatar(w, Tileset.GHOST, init2);
        Avatar key = new Avatar(w, Tileset.KEY, init3);
        char[] keys = "wasd".toCharArray();
        char ghostKey;
        e.render();
        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                player.move(StdDraw.nextKeyTyped());
                ghost.moveCloser(player);
                e.render();
            }
        }
    }
}
