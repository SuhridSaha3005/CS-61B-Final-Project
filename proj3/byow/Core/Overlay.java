package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import edu.princeton.cs.introcs.StdDraw;
import edu.princeton.cs.introcs.Stopwatch;

import java.awt.*;
import java.util.*;

public class Overlay {
    private final TETile[][] world;
    private final double[][] luminosity;
    private final int width;
    private final int height;
    private final HashMap<TETile, ArrayList<XYPosn>> tilePosn;
    private final HashMap<XYPosn, Integer> lightState;
    private final HashMap<Integer, Integer> lightChange;
    private XYPosn playerPosn;
    private XYPosn doorPosn;
    private ArrayList<XYPosn> ghostPosn;
    private final ArrayList<XYPosn> lampPosn;
    private ArrayList<XYPosn> keyPosn;
    private final Random rand;
    private final Random rand2Flicker = new Random();
    private boolean gameOver = false;
    private ArrayList<XYPosn> keyWhileLit;
    private boolean keyLit;
    private int playerLives;
    private String displayString;
    private Color displayColor;
    private Stopwatch sw;

    private final double LAMPFALLOFF = 1.8;
    private final double PLAYERFALLOFF = 1.2;
    private final int LAMPFLICKERYNESS = 10;
    private final int KEYDISPLAYTIME = 1000;
    private final int LAMPWATTAGE = 100;

    /** Overlays objects in the world and adds color and lighting. */
    public Overlay(Random r, TETile[][] wrld, int w, int h) {
        world = wrld;
        width = w;
        height = h;
        rand = r;
        tilePosn = new HashMap<>();
        ghostPosn = new ArrayList<>();
        keyPosn = new ArrayList<>();
        keyWhileLit = new ArrayList<>();
        luminosity = new double[width][height];
        lightState = new HashMap<>();
        lightChange = new HashMap<>();
        sw = new Stopwatch();
        doorPosn = null;
        playerLives = 3;
        displayColor = Tileset.purpleColorBright;
        displayString = "WELCOME TO THE MAZE. ESCAPE IF YOU DARE.";

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
        lampPosn = addLampsRandPosn(Math.max(tilePosn.get(Tileset.FLOOR).size() / 100, 1));
        keyPosn = addKeysRandPosn(3);
        for (XYPosn singLampPosn: lampPosn) {
            brighten(singLampPosn, LAMPWATTAGE, LAMPFALLOFF);
            lightState.put(singLampPosn, 0);
        }

        lightChange.put(0, LAMPFLICKERYNESS);
        lightChange.put(1, -LAMPFLICKERYNESS);
        lightChange.put(2, 2 * LAMPFLICKERYNESS);
        lightChange.put(3, LAMPFLICKERYNESS);
        lightChange.put(4, LAMPFLICKERYNESS);
        lightChange.put(5, -3 * LAMPFLICKERYNESS);
        lightChange.put(6, -LAMPFLICKERYNESS);
        lightChange.put(7, 2 * LAMPFLICKERYNESS);
        lightChange.put(8, -LAMPFLICKERYNESS);
        lightChange.put(9, -LAMPFLICKERYNESS);
    }

    public ArrayList<XYPosn> addKeysRandPosn(int num) {
        return addObjs(Tileset.KEY, Tileset.FLOOR, num);
    }

    public XYPosn addDoorRandPosn() {
        return addObj(Tileset.LOCKED_DOOR, Tileset.WALL);
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

    public void updatePosn(XYPosn newPlayerPosn, ArrayList<XYPosn> newGhostPosn, Avatar player) {
        brighten(playerPosn, -70, PLAYERFALLOFF);
        updateGhostPosn(newGhostPosn);
        updatePlayerPosn(newPlayerPosn);
        brighten(newPlayerPosn, 70, PLAYERFALLOFF);
        ArrayList<XYPosn> temp = (ArrayList<XYPosn>) keyPosn.clone();
        for (XYPosn k: keyPosn) {
            if ((k.getX() == newPlayerPosn.getX()) && (k.getY() == newPlayerPosn.getY())) {
                if (keyLit) {
                    keyWhileLit.add(k);
                }
                temp.remove(k);
                displayString = "KEY COLLECTED. " + temp.size() + " KEYS REMAIN.";
                displayColor = Color.yellow;
            }
        }
        keyPosn = temp;
        if ((doorPosn == null) && (keyPosn.size() == 0)) {
            doorPosn = addDoorRandPosn();
            displayString = "YOU FOUND ALL THE KEYS. BUT CAN YOU FIND THE EXIT?";
            displayColor = Tileset.purpleColorBright;
        }

        if (doorPosn != null) {
            if ((doorPosn.getX() == newPlayerPosn.getX()) && (doorPosn.getY() == newPlayerPosn.getY())) {
                gameOver = true;
                displayString = "WELL PLAYED. YOU ESCAPED IN " + sw.elapsedTime() + " SECONDS.";
                displayColor = Color.white;
                gameOver = true;
            }
        }

        for (XYPosn g: ghostPosn) {
            if (euclidean(g, playerPosn) < 1.2) {
                player.changePosn(getFloors().get(RandomUtils.uniform(rand, getFloors().size())));
                for (XYPosn g2 : ghostPosn) {
                    if (euclidean(g2, player.getPosn()) < 3) {
                        player.changePosn(getFloors().get(RandomUtils.uniform(rand, getFloors().size())));
                        break;
                    }
                }
                playerLives -= 1;
                displayString = "YOU DIED. YOU HAVE " + playerLives + " LIVES LEFT.";
                displayColor = Color.red;
                if (playerLives <= 0) {
                    displayString = "YOU FAILED TO ESCAPE THE MAZE. GAME OVER.";
                    gameOver = true;
                }
            }
        }
    }

    public ArrayList<XYPosn> getWallObjs() {
        ArrayList<XYPosn> newList = new ArrayList<>(keyPosn);
        newList.addAll(lampPosn);
        return newList;
    }

    public ArrayList<XYPosn> getKeys() {
        return keyPosn;
    }

    public ArrayList<XYPosn> getFloors() {
        return tilePosn.get(Tileset.FLOOR);
    }

    public int getPlayerLives() {
        return playerLives;
    }

    public String getDisplayString() {
        return displayString;
    }

    public Color getDisplayColor() {
        return displayColor;
    }

    public void modulateLights(int ticks) {
        int randIndex = RandomUtils.uniform(rand2Flicker, lampPosn.size());
        XYPosn singLampPosn = lampPosn.get(randIndex);
        brighten(singLampPosn, lightChange.get(lightState.get(singLampPosn) % 10), LAMPFALLOFF);
        lightState.put(singLampPosn, lightState.get(singLampPosn) + 1);
        if (ticks % KEYDISPLAYTIME == 0) {
            for (XYPosn k: keyPosn) {
                addLuminosity(k, k, 100, 1000);
                keyLit = true;
            }
        } else if (ticks % KEYDISPLAYTIME == 50) {
            keyLit = false;
            for (XYPosn k: keyPosn) {
                addLuminosity(k, k, -100, 1000);
            }
            if (keyWhileLit.size() != 0) {
                for (XYPosn k : keyWhileLit) {
                    addLuminosity(k, k, -100, 1000);
                }
                for (XYPosn k: keyPosn) {
                    addLuminosity(k, k, -100, 1000);
                }
            }
            keyWhileLit = new ArrayList<>();
        }
    }


    private boolean validate(XYPosn point) {
        return point.getX() >= 0 && point.getX() < world.length && point.getY() >= 0 && point.getY() < world[0].length;
    }

    private void brighten(XYPosn sourcePosn, double wattage, double falloff) {
        if (sourcePosn == null) {
            return;
        }
        for (int x = sourcePosn.getX() - 20; x < sourcePosn.getX() + 20; x += 1) {
            for (int y = sourcePosn.getY() - 20; y < sourcePosn.getY() + 20; y += 1) {
                XYPosn point = new XYPosn(x, y);
                if (validate(point) && (euclidean(sourcePosn, point) < 20)) {
                    addLuminosity(sourcePosn, point, wattage, falloff);
                }
            }
        }
    }

    private double euclidean(XYPosn source, XYPosn point) {
        return Math.sqrt(Math.pow((source.getX() - point.getX()), 2) + Math.pow((source.getY() - point.getY()), 2));
    }

    private void addLuminosity(XYPosn sourcePosn, XYPosn point, double wattage, double falloff) {
        luminosity[point.getX()][point.getY()] += wattage / Math.max((Math.pow(euclidean(sourcePosn, point), falloff)), 1);
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public TETile[][] getDarkWorld() {
        TETile[][] darkWorld = new TETile[width][height];

        for (int i = 0; i < width; i += 1) {
            for (int j = 0; j < height; j += 1) {
                darkWorld[i][j] = Tileset.modTile(Math.max(Math.min(luminosity[i][j], 100), 0), world[i][j]);
            }
        }
        return darkWorld;
    }

}
