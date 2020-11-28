package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.*;

public class Overlay {
    private TETile[][] world;
    private double[][] luminosity;
    private final int width;
    private final int height;
    private HashMap<TETile, ArrayList<XYPosn>> tilePosn;
    private XYPosn playerPosn;
    private ArrayList<XYPosn> ghostPosn;
    private ArrayList<XYPosn> lampPosn;
    private Random rand;

    /** Overlays objects in the world and adds color and lighting. */
    public Overlay(Random r, TETile[][] wrld, int w, int h) {
        world = wrld;
        width = w;
        height = h;
        rand = r;
        tilePosn = new HashMap<>();
        ghostPosn = new ArrayList<>();
        luminosity = new double[width][height];

        for (int i = 0; i < width; i += 1) {
            for (int j = 0; j < height; j += 1) {
                if (world[i][j] == null) {
                    throw new IllegalArgumentException("Position" + i + j + "in World are Null.");
                } else if (tilePosn.containsKey(world[i][j])) {
                    tilePosn.get(world[i][j]).add(new XYPosn(i, j));
                } else {
                    ArrayList<XYPosn> a = new ArrayList<>();
                    a.add(new XYPosn(i, j));
                    tilePosn.put(world[i][j], a);
                }
                luminosity[i][j] = 0.0;
            }
        }

        lampPosn = addLampsRandPosn(15);
        for (XYPosn singLampPosn: lampPosn) {
            brighten(singLampPosn, 100);
        }

        StringJoiner sj = new StringJoiner(System.lineSeparator());
        for (double[] row : luminosity) {
            sj.add(Arrays.toString(row));
        }
        System.out.println(sj.toString());
    }

    public XYPosn addPlayerRandPosn() {
        return addObj(Tileset.PLAYER, Tileset.FLOOR);
    }

    public ArrayList<XYPosn> addGhostRandPosn(int num) {
        return addObjs(Tileset.GHOST, Tileset.FLOOR, num);
    }

    public ArrayList<XYPosn> addLampsRandPosn(int num) {
        return addObjs(Tileset.LAMP, Tileset.FLOOR, num);
    }

    private XYPosn addObj(TETile newTile, TETile oldTile) {
        if (!tilePosn.containsKey(oldTile)) {
            throw new IllegalArgumentException("Map currently does not have this tile.");
        }
        ArrayList<XYPosn> oldTilePosn = tilePosn.get(oldTile);
        int randIndex = RandomUtils.uniform(rand, oldTilePosn.size());
        while (world[oldTilePosn.get(randIndex).getX()][oldTilePosn.get(randIndex).getY()] != oldTile) {
            randIndex = RandomUtils.uniform(rand, oldTilePosn.size());
        }
        XYPosn replacePosn = oldTilePosn.get(randIndex);
        world[replacePosn.getX()][replacePosn.getY()] = newTile;
        return replacePosn;
    }

    private ArrayList<XYPosn> addObjs(TETile newTile, TETile oldTile, int numReplace) {
        if (!tilePosn.containsKey(oldTile)) {
            throw new IllegalArgumentException("Map currently does not have this tile.");
        }
        if (numReplace > tilePosn.get(oldTile).size()) {
            throw new IllegalArgumentException("Adding more objects than can be added.");
        }
        ArrayList<XYPosn> oldTilePosn = tilePosn.get(oldTile);
        ArrayList<Integer> temp = new ArrayList<>();
        ArrayList<XYPosn> newObjPosn = new ArrayList<>();
        while (numReplace > 0) {
            int randIndex = RandomUtils.uniform(rand, oldTilePosn.size());
            while (world[oldTilePosn.get(randIndex).getX()][oldTilePosn.get(randIndex).getY()] != oldTile) {
                randIndex = RandomUtils.uniform(rand, oldTilePosn.size());
            }
            if (!temp.contains(randIndex)) {
                XYPosn replacePosn = oldTilePosn.get(randIndex);
                world[replacePosn.getX()][replacePosn.getY()] = newTile;
                temp.add(randIndex);
                newObjPosn.add(replacePosn);
                numReplace -= 1;
            }
        }
        return newObjPosn;
    }

    private void updatePlayerPosn(XYPosn newPosn) {
        tilePosn.get(Tileset.FLOOR).add(playerPosn);
        playerPosn = newPosn;
    }

    private void updateGhostPosn(ArrayList<XYPosn> newPosn) {
        for (XYPosn singGhostPosn: ghostPosn) {
            tilePosn.get(Tileset.FLOOR).add(singGhostPosn);
        }

        ghostPosn = newPosn;
    }

    public void updatePosn(XYPosn newPlayerPosn, ArrayList<XYPosn> newGhostPosn) {
        brighten(playerPosn, -50);
        updateGhostPosn(newGhostPosn);
        updatePlayerPosn(newPlayerPosn);
        brighten(newPlayerPosn, 50);
    }

    public ArrayList<XYPosn> get(TETile tileType) {
        return tilePosn.get(tileType);
    }


    private boolean validate(XYPosn point) {
        return point.getX() >= 0 && point.getX() < world.length && point.getY() >= 0 && point.getY() < world[0].length;
    }

    private void brighten(XYPosn sourcePosn, double wattage) {
        if (sourcePosn == null) {
            return;
        }
        for (int x = sourcePosn.getX() - 20; x < sourcePosn.getX() + 20; x += 1) {
            for (int y = sourcePosn.getY() - 20; y < sourcePosn.getY() + 20; y += 1) {
                XYPosn point = new XYPosn(x, y);
                if (validate(point) && (euclidean(sourcePosn, point) < 20)) {
                    addLuminosity(sourcePosn, point, wattage);
                }
            }
        }
    }

    private double euclidean(XYPosn source, XYPosn point) {
        return Math.sqrt(Math.pow((source.getX() - point.getX()), 2) + Math.pow((source.getY() - point.getY()), 2));
    }

    private void addLuminosity(XYPosn sourcePosn, XYPosn point, double wattage) {
        if (luminosity[point.getX()][point.getY()] < 100) {
            luminosity[point.getX()][point.getY()] += wattage / Math.max((Math.pow(euclidean(sourcePosn, point), 1.5)), 1);
        }
    }

    public TETile[][] getDarkWorld() {
        TETile[][] darkWorld = new TETile[width][height];

        for (int i = 0; i < width; i += 1) {
            for (int j = 0; j < height; j += 1) {
                darkWorld[i][j] = Tileset.modTile(luminosity[i][j], world[i][j]);
            }
        }
        return darkWorld;
    }

}
