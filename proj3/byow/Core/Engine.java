package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.ArrayList;
import java.util.Random;

public class Engine {
    TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 80;
    public static final int HEIGHT = 50;
    public int SEED = -1;
    Random RANDOM;
    TETile[][] world;
    StringBuilder gameSave = null;


    /**
     * Method used for exploring a fresh world. This method should handle all inputs,
     * including inputs from the main menu.
     */
    public void interactWithKeyboard() {
    }

    /**
     * Method used for autograding and testing your code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The engine should
     * behave exactly as if the user typed these characters into the engine using
     * interactWithKeyboard.
     *
     * Recall that strings ending in ":q" should cause the game to quite save. For example,
     * if we do interactWithInputString("n123sss:q"), we expect the game to run the first
     * 7 commands (n123sss) and then quit and save. If we then do
     * interactWithInputString("l"), we should be back in the exact same state.
     *
     * In other words, both of these calls:
     *   - interactWithInputString("n123sss:q")
     *   - interactWithInputString("lww")
     *
     * should yield the exact same world state as:
     *   - interactWithInputString("n123sssww")
     *
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] interactWithInputString(String input) {
        // TODO: Fill out this method so that it run the engine using the input
        // passed in as an argument, and return a 2D tile representation of the
        // world that would have been drawn if the same inputs had been given
        // to interactWithKeyboard().
        //
        // See proj3.byow.InputDemo for a demo of how you can make a nice clean interface
        // that works for many different input types.

        input = input.toLowerCase();
        StringBuilder seed = null;
        for (int i = 0; i < input.length(); i += 1) {
            String letter = (input.substring(i, i + 1));
            switch (letter) {
                case "n":
                    gameSave = new StringBuilder(letter);
                    seed = new StringBuilder();
                    break;
                case "s":
                    if ((seed != null) && gameSave != null) {
                        gameSave.append(letter);
                        SEED = Integer.parseInt(seed.toString());
                        RANDOM = new Random(SEED);
                    } else {
                        throw new IllegalArgumentException("Map was not initialized with n.");
                    }
                    break;
                case "l":
                    if ((gameSave) == null) {
                        throw new IllegalArgumentException("No game in save state.");
                    }
                    return interactWithInputString(gameSave.append(input.substring(i + 1)).toString());
                default:
                    try {
                        int l = Integer.parseInt(letter);
                    } catch (NumberFormatException nfe) {
                        gameSave.append(letter);
                        continue;
                    }
                    if ((seed == null) || (gameSave == null)) {
                        continue;
                    }
                    seed.append(letter);
                    gameSave.append(letter);
            }
        }
        if (SEED != -1) {
            TETile[][] finalWorldFrame = createWorld();
            return finalWorldFrame;
        }
        return null;
    }

    TETile[][] createWorld() {
        world = new TETile[WIDTH][HEIGHT];
        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                world[x][y] = Tileset.NOTHING;
            }
        }

        MapMaker map = new MapMaker(RANDOM, world, WIDTH, HEIGHT);
        map.makeMap();
        return world;
    }

    void render() {
        ter.initialize(WIDTH, HEIGHT);
        ter.renderFrame(world);
    }

}
