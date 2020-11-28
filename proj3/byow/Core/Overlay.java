package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.*;

public class Overlay {
    private TETile[][] world;
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
            }
        }

        lampPosn = addLampsRandPosn(10);
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
        XYPosn replacePosn = oldTilePosn.get(randIndex);
        world[replacePosn.getX()][replacePosn.getY()] = newTile;
        tilePosn.get(oldTile).remove(randIndex);
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
            if (!temp.contains(randIndex)) {
                XYPosn replacePosn = oldTilePosn.get(randIndex);
                world[replacePosn.getX()][replacePosn.getY()] = newTile;
                temp.add(randIndex);
                newObjPosn.add(replacePosn);
                tilePosn.get(oldTile).remove(randIndex);
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
        updateGhostPosn(newGhostPosn);
        updatePlayerPosn(newPlayerPosn);
    }

    public ArrayList<XYPosn> get(TETile tileType) {
        return tilePosn.get(tileType);
    }

}
