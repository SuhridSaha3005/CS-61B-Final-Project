package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.ArrayList;
import java.util.List;
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
        if (aboveTile.equals(Tileset.GHOST)
                || aboveTile.equals(Tileset.PLAYER)
                || aboveTile.equals(Tileset.KEY)) {
            aboveTile = Tileset.FLOOR;
        }
    }

    public static double distance(Avatar avatar1, Avatar avatar2) {
        return Math.sqrt(
                Math.pow(avatar1.position.getX() - avatar2.position.getX(), 2)
                + Math.pow(avatar1.position.getY() - avatar2.position.getY(), 2));
    }

    public void randomMove(
            Random rand,
            List<XYPosn> keyPosns,
            Avatar player,
            boolean flashlightOn) {
        XYPosn oldPos = position;
        ArrayList<String> keys = new ArrayList<>(List.of("w", "a", "s", "d"));
        if (keyPosns.contains(up())) {
            keys.remove("w");
        } else if (keyPosns.contains(down())) {
            keys.remove("s");
        } else if (keyPosns.contains(right())) {
            keys.remove("d");
        } else if (keyPosns.contains(left())) {
            keys.remove("a");
        }

        if ((manhattan(position, player.getPosn()) < 8) && (flashlightOn)) {
            move(calcMinKey(keys, player).charAt(0));
        }


        if (position == oldPos) {
            while (position == oldPos) {
                move(keys.get(RandomUtils.uniform(rand, keys.size())).charAt(0));
            }
        }
    }

    public String calcMinKey(ArrayList<String> keys, Avatar player) {
        double minim = Double.POSITIVE_INFINITY;
        String bestChar = "";
        for (String k: keys) {
            switch (k) {
                case "w":
                    if (up() != null) {
                        double dist = manhattan(up(), player.getPosn());
                        if (dist < minim) {
                            bestChar = "w";
                            minim = dist;
                        } else if (dist == minim) {
                            if (world[up().getX()][up().getY()] != Tileset.WALL) {
                                bestChar = "w";
                                minim = dist;
                            }
                        }
                    }
                    break;
                case "a":
                    if (left() != null) {
                        double dist = manhattan(left(), player.getPosn());
                        if (dist < minim) {
                            bestChar = "a";
                            minim = dist;
                        } else if (dist == minim) {
                            if (world[left().getX()][left().getY()] != Tileset.WALL) {
                                bestChar = "a";
                                minim = dist;
                            }
                        }
                    }
                    break;
                case "s":
                    if (down() != null) {
                        double dist = manhattan(down(), player.getPosn());
                        if (dist < minim) {
                            bestChar = "s";
                            minim = dist;
                        } else if (dist == minim) {
                            if (world[down().getX()][down().getY()] != Tileset.WALL) {
                                bestChar = "s";
                                minim = dist;
                            }
                        }
                    }
                    break;
                case "d":
                    if (right() != null) {
                        double dist = manhattan(right(), player.getPosn());
                        if (dist < minim) {
                            bestChar = "d";
                            minim = dist;
                        } else if (dist == minim) {
                            if (world[right().getX()][right().getY()] != Tileset.WALL) {
                                bestChar = "d";
                                minim = dist;
                            }
                        }
                    }
                    break;
                default:
                    break;
            }
        }
        return bestChar;
    }

    private double manhattan(XYPosn source, XYPosn point) {
        if ((source == null) || (point == null)) {
            return Double.POSITIVE_INFINITY;
        }
        return (Math.abs(source.getX() - point.getX()) + Math.abs(source.getY() - point.getY()));
    }


    public void changePosn(XYPosn newPosn) {
        world[position.getX()][position.getY()] = aboveTile;
        aboveTile = world[newPosn.getX()][newPosn.getY()];
        position = newPosn;
        world[newPosn.getX()][newPosn.getY()] = appearance;
        if (aboveTile.equals(Tileset.GHOST)
                || aboveTile.equals(Tileset.PLAYER)
                || aboveTile.equals(Tileset.KEY)) {
            aboveTile = Tileset.FLOOR;
        }
    }

    private double euclidean(XYPosn source, XYPosn point) {
        return Math.sqrt(
                Math.pow((source.getX() - point.getX()), 2)
                + Math.pow((source.getY() - point.getY()), 2));
    }

    public XYPosn getPosn() {
        return position;
    }
}
