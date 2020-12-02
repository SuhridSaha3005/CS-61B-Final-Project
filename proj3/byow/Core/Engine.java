package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

/** @author Shrihan Agarwal, Suhrid Saha
 * Engine for the Map Generation and Gameplay. */
public class Engine {
    /** Renderer for the Engine. */
    private TERenderer ter = new TERenderer();

    /** Final Variable for Width. */
    public static final int WIDTH = 80;

    /** Final Variable for Height. */
    public static final int HEIGHT = 50;

    /** Variable that stores Seed. */
    private long givenSeed = -1;

    /** Variable that stores the random seed instance. */
    private Random seedRandom;

    /** Array for the world. */
    private TETile[][] world;

    /** Heads Up Display. */
    private HeadsUpDisplay hud;

    /** Overlay/Theme. */
    private Overlay finalMap;

    /** Keys (w,a,s,d) pressed for game */
    private StringBuilder gameKeys;

    /** Player Instance. */
    Avatar player;

    /** Player instance appearance */
    TETile playerTile = Tileset.PLAYER;

    /** Ghosts Instance List. */
    ArrayList<Avatar> ghost;

    /** Number of ghosts in game */
    int numGhosts = 10;

    /** Change Overlay Lamp Wattage */
    int LAMPWATTAGE = 100;

    /** Player input for number of ghosts */
    StringBuilder numGhostsTyped = new StringBuilder();

    /** Turn on/off lighting. */
    boolean lighting = true;

    /** Whether game is over or not. */
    private boolean gameOver;

    /** Current Tile Being Pointed To. */
    private TETile currTile = Tileset.FLOOR;

    /** Constructor for Engine Class. */
    public Engine() {
        hud = new HeadsUpDisplay(3, WIDTH, HEIGHT);
    }


    /** Constructor with seed for Engine Class.
     * @param seed the seed to be initialized with
     */
    public Engine(Long seed) {
        this.givenSeed = seed;
        this.seedRandom = new Random(givenSeed);
    }

    /**
     * Method used for exploring a fresh world.
     * This method should handle all inputs,
     * including inputs from the main menu.
     */
    public void interactWithKeyboard() {
        startGame();
        playGame();
    }

    private boolean drawCustomize() {
        StdDraw.clear(Color.BLACK);
        double w = WIDTH;
        double h = HEIGHT;
        StdDraw.setFont(new Font("Arial", Font.PLAIN, 30));
        StdDraw.setPenColor(playerTile.getTextColor());
        StdDraw.text(0.5 * w, 0.55 * h, "Appearance: ☺ (White- W, Blue- B, Green- G)");
        StdDraw.setPenColor(Color.white);
        StdDraw.text(0.5 * w, 0.5 * h, "No. of Ghosts: " + numGhosts + "(C- Return to Default)");
        if (LAMPWATTAGE == 100) {
            StdDraw.text(0.5 * w, 0.45 * h, "Lamp Wattage: Medium (H- High, M- Medium, L- Low)");
        } else if (LAMPWATTAGE == 150) {
            StdDraw.text(0.5 * w, 0.45 * h, "Lamp Wattage: High (H- High, M- Medium, L- Low)");
        } else if (LAMPWATTAGE == 50) {
            StdDraw.text(0.5 * w, 0.45 * h, "Lamp Wattage: Low (H- High, M- Medium, L- Low)");
        }
        StdDraw.text(0.5 * w, 0.4 * h, "Exit(E)");
        StdDraw.show();
        return true;
    }

    private boolean doCustomize(char c) {
        boolean customize = true;
        if (c == 'w') {
            playerTile = Tileset.newTextColor(playerTile, Color.WHITE);
            drawCustomize();
        } else if (c == 'b') {
            playerTile = Tileset.newTextColor(playerTile, Color.BLUE);
            drawCustomize();
        } else if (c == 'g') {
            playerTile = Tileset.newTextColor(playerTile, Color.GREEN);
            drawCustomize();
        } else if (c == 'e') {
            customize = false;
            interactWithKeyboard();
        } else if (Character.isDigit(c)) {
            numGhostsTyped.append(c);
            numGhosts = Integer.parseInt(numGhostsTyped.toString());
            drawCustomize();
        } else if (c == 'h') {
            LAMPWATTAGE = 150;
            drawCustomize();
        } else if (c == 'l') {
            LAMPWATTAGE = 50;
            drawCustomize();
        } else if (c == 'm') {
            LAMPWATTAGE = 100;
            drawCustomize();
        } else if (c == 'c') {
            numGhostsTyped = new StringBuilder();
            numGhosts = 10;
            drawCustomize();
        }
        return customize;
    }

    private void drawSeedFrame(String r) {
        if (r == null) {
            r = "";
        }
        StdDraw.clear(Color.BLACK);
        double w = WIDTH;
        double h = HEIGHT;
        StdDraw.setPenColor(Color.white);
        Font font = new Font("Arial", Font.PLAIN, 30);
        StdDraw.setFont(font);
        StdDraw.text(w / 2, h / 2, "Seed: " + r);
        StdDraw.show();
    }

    private void playGame() {
        gameOver = false;
        StringBuilder savedGame = new StringBuilder();
        StringBuilder randomSeed = new StringBuilder();
        gameKeys = new StringBuilder();
        boolean gameInitialized = false;
        boolean seedFinished = false;
        boolean quit = false;
        boolean customize = false;
        int i, j;
        i = j = 0;
        while (!gameOver) {
            if (StdDraw.hasNextKeyTyped()) {
                char c = Character.toLowerCase(StdDraw.nextKeyTyped());
                if (!customize && c == 'c') {
                    customize = drawCustomize();
                }
                if (customize) {
                    customize = doCustomize(c);
                }
                if (!customize && c == 'l') {
                    world = loadAndInteractWithKeyboard(SaveNLoad.loadGame());
                } else if (c == ':') {
                    quit = true;
                } else if (quit && c == 'q') {
                    gameOver = true;
                    if (gameInitialized && seedFinished) {
                        SaveNLoad.saveGame(savedGame.toString());
                    }
                } else if (quit && c != 'q') {
                    quit = false;
                } else if (c == 's') {
                    if (randomSeed.length() == 0) {
                        throw new IllegalArgumentException("No seed provided");
                    }
                    if (!seedFinished) {
                        givenSeed = Long.parseLong(randomSeed.toString());
                        seedRandom = new Random(givenSeed);
                        initialize();
                        createWorld();
                        render();
                    }
                    seedFinished = true;
                } else if (c == 'n') {
                    gameInitialized = true;
                    savedGame.append(c);
                    drawSeedFrame(null);
                } else if (Character.isDigit(c) && gameInitialized) {
                    randomSeed.append(c);
                    savedGame.append(c);
                    drawSeedFrame(randomSeed.toString());
                }
                if (gameInitialized && seedFinished) {
                    if ("wasdf".contains(Character.toString(c))) {
                        savedGame.append(c);
                        gameKeys.append(c);
                        player.move(c);
                        if (!updateOverlay(c)) {
                            break;
                        }
                    }
                }
            }
            if (gameInitialized && seedFinished) {
                if (i == 10) {
                    finalMap.modulateLights(j);
                    render();
                    i = 0;
                    j += 1;
                }
                i += 1;
            }
        }
        StdDraw.clear(Color.BLACK);
        StdDraw.show();
    }

    public boolean updateOverlay(char c) {
        if (c == 'f') {
            finalMap.toggleFlashlight();
        }
        ArrayList<XYPosn> ghostPosn = new ArrayList<>();
        for (Avatar g: ghost) {
            g.randomMove(seedRandom,
                    finalMap.getWallObjs(),
                    player,
                    finalMap.getFlashState());
            ghostPosn.add(g.getPosn());
        }
        finalMap.updatePosn(player.getPosn(), ghostPosn, player);
        if (finalMap.isGameOver()) {
            render();
            StdDraw.pause(3000);
            startGame();
            playGame();
            return false;
        }
        return true;
    }

    /**
     * Method used for autograding and testing your code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The engine should
     * behave exactly as if the user typed these characters into the engine using
     * interactWithKeyboard.
     *
     * Recall that strings ending in ":q" should cause the game to quite save. For example,
     * if we do loadAndInteractWithKeyboard("n123sss:q"), we expect the game to run the first
     * 7 commands (n123sss) and then quit and save. If we then do
     * loadAndInteractWithKeyboard("l"), we should be back in the exact same state.
     *
     * In other words, both of these calls:
     *   - loadAndInteractWithKeyboard("n123sss:q")
     *   - loadAndInteractWithKeyboard("lww")
     *
     * should yield the exact same world state as:
     *   - loadAndInteractWithKeyboard("n123sssww")
     * // Fill out this method so that it run the engine using the input
     * // passed in as an argument, and return a 2D tile representation of the
     * // world that would have been drawn if the same inputs had been given
     * // to interactWithKeyboard().
     * //
     * // See proj3.byow.InputDemo for a demo of how you can make a nice clean interface
     * // that works for many different input types.
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] interactWithInputString(String input) {
        input = input.toLowerCase();
        StringBuilder savedGame = new StringBuilder();
        StringBuilder randomSeed = new StringBuilder();
        gameKeys = new StringBuilder();
        boolean gameInitialized = false;
        boolean seedFinished = false;
        boolean quit = false;
        for (int i = 0; i < input.toCharArray().length; i += 1) {
            char c = input.toCharArray()[i];
            if (c == 'l') {
                return interactWithInputString(SaveNLoad.loadGame() + input.substring(i + 1));
            }
            if (c == ':') {
                quit = true;
            } else if (c == 'q') {
                if (quit && gameInitialized && seedFinished) {
                    if (gameKeys.length() == 0) {
                        throw new IllegalArgumentException("No keys (w,a,s,d) pressed for game");
                    }
                    SaveNLoad.saveGame(savedGame.toString());
                    quit = false;
                } else {
                    throw new IllegalArgumentException("Invalid key");
                }
            }
            if ("nwasdf".contains(Character.toString(c)) || Character.isDigit(c)) {
                savedGame.append(c);
                if (quit) {
                    throw new IllegalArgumentException("Invalid key");
                }
                if (c == 'n') {
                    if (gameInitialized) {
                        throw new IllegalArgumentException("Invalid key");
                    }
                    gameInitialized = true;
                } else if (Character.isDigit(c)) {
                    if (seedFinished) {
                        throw new IllegalArgumentException("Invalid key");
                    } else {
                        randomSeed.append(c);
                    }
                } else {
                    if (c == 's') {
                        seedFinished = true;
                        givenSeed = Long.parseLong(randomSeed.toString());
                        seedRandom = new Random(givenSeed);
                    }
                    if (seedFinished) {
                        gameKeys.append(c);
                    } else {
                        throw new IllegalArgumentException("Keys must begin with 's'");
                    }
                }
            }
        }
        createWorld();
        runWorldKeys();
        return world;
    }




    public TETile[][] loadAndInteractWithKeyboard(String input) {
        input = input.toLowerCase();
        gameOver = false;
        StringBuilder savedGame = new StringBuilder();
        StringBuilder randomSeed = new StringBuilder();
        gameKeys = new StringBuilder();
        boolean gameInitialized = false;
        boolean seedFinished = false;
        boolean quit = false;
        for (char c : input.toCharArray()) {
            if (c == 'l') {
                loadAndInteractWithKeyboard(SaveNLoad.loadGame());
            }
            if (c == ':') {
                quit = true;
            } else if (c == 'q') {
                if (quit && gameInitialized && seedFinished) {
                    if (gameKeys.length() == 0) {
                        throw new IllegalArgumentException("No keys (w,a,s,d) pressed for game");
                    }
                    SaveNLoad.saveGame(savedGame.toString());
                    gameOver = true;
                } else {
                    throw new IllegalArgumentException("Invalid key");
                }
            }
            if ("nwasdf".contains(Character.toString(c)) || Character.isDigit(c)) {
                savedGame.append(c);
                if (quit) {
                    throw new IllegalArgumentException("Invalid key");
                }
                if (c == 'n') {
                    if (gameInitialized) {
                        throw new IllegalArgumentException("Invalid key");
                    }
                    gameInitialized = true;
                } else if (Character.isDigit(c)) {
                    if (seedFinished) {
                        throw new IllegalArgumentException("Invalid key");
                    } else {
                        randomSeed.append(c);
                    }
                } else {
                    if (c == 's') {
                        seedFinished = true;
                        givenSeed = Long.parseLong(randomSeed.toString());
                        seedRandom = new Random(givenSeed);
                    }
                    if (seedFinished) {
                        gameKeys.append(c);
                    } else {
                        throw new IllegalArgumentException("Keys must begin with 's'");
                    }
                }
            }
        }
        return loadAndInteractWithKeyboardPt2(savedGame);
    }

    public TETile[][] loadAndInteractWithKeyboardPt2(StringBuilder savedGame) {
        boolean quit = false;
        if (givenSeed != -1) {
            initialize();
            createWorld();
            render();
            runWorldKeys();
            if (!gameOver) {
                int i, j;
                i = j = 0;
                while (!gameOver) {
                    if (StdDraw.hasNextKeyTyped()) {
                        char c = Character.toLowerCase(StdDraw.nextKeyTyped());
                        if (quit) {
                            if (c == 'q') {
                                gameOver = true;
                                SaveNLoad.saveGame(savedGame.toString());
                            } else {
                                quit = false;
                            }
                        }
                        if (c == ':') {
                            quit = true;
                        }
                        if ("wasdf".contains(Character.toString(c))) {
                            savedGame.append(c);
                            gameKeys.append(c);
                            player.move(c);
                            if (c == 'f') {
                                finalMap.toggleFlashlight();
                            }
                            ArrayList<XYPosn> ghostPosn = new ArrayList<>();
                            for (Avatar g: ghost) {
                                g.randomMove(seedRandom,
                                        finalMap.getWallObjs(),
                                        player,
                                        finalMap.getFlashState());
                                ghostPosn.add(g.getPosn());
                            }
                            finalMap.updatePosn(player.getPosn(), ghostPosn, player);
                            if (finalMap.isGameOver()) {
                                render();
                                StdDraw.pause(3000);
                                startGame();
                                playGame();
                                return null;
                            }
                        }
                    }
                    if (i == 10) {
                        finalMap.modulateLights(j);
                        render();
                        i = 0;
                        j += 1;
                    }
                    i += 1;
                }
            }
            StdDraw.clear(Color.BLACK);
            StdDraw.show();
            return world;
        } else {
            return null;
        }
    }

    /** Mutates to a randomly generated map with instance seed.
     * @return TETile array corresponding to generated map
     */
    void createWorld() {
        int height = HEIGHT - 3;

        world = new TETile[WIDTH][height];
        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < height; y += 1) {
                world[x][y] = Tileset.NOTHING;
            }
        }

        MapMaker map = new MapMaker(seedRandom, world, WIDTH, height);
        map.makeMap();

        finalMap = new Overlay(seedRandom, world, WIDTH, height);
        if (LAMPWATTAGE != 100) {
            finalMap.changeWattage(LAMPWATTAGE);
        }
        player = new Avatar(world, playerTile, finalMap.addPlayerRandPosn());
        ArrayList<XYPosn> ghostPosns = finalMap.addGhostRandPosn(numGhosts);
        ghost = new ArrayList<>();
        for (int i = 0; i < numGhosts; i++) {
            ghost.add(new Avatar(world, Tileset.GHOST, ghostPosns.get(i)));
        }
        finalMap.updatePosn(player.getPosn(), ghostPosns, player);
    }

    void runWorldKeys() {
        for (char typed : gameKeys.toString().toCharArray()) {
            player.move(typed);
            if (typed == 'f') {
                finalMap.toggleFlashlight();
            }
            ArrayList<XYPosn> ghostPosn = new ArrayList<>();
            for (Avatar g: ghost) {
                g.randomMove(seedRandom, finalMap.getWallObjs(), player, finalMap.getFlashState());
                ghostPosn.add(g.getPosn());
            }
            finalMap.updatePosn(player.getPosn(), ghostPosn, player);
        }
    }

    /** Initializes the Map Instance. */
    void initialize() {
        ter.initialize(WIDTH, HEIGHT);
    }

    private void startGame() {
        StdDraw.setCanvasSize(WIDTH * 16, HEIGHT * 16);
        StdDraw.clear(Color.BLACK);
        double w = WIDTH;
        double h = HEIGHT;
        StdDraw.setXscale(0, w);
        StdDraw.setYscale(0, h);
        StdDraw.enableDoubleBuffering();
        StdDraw.setPenColor(Color.white);
        Font font1 = new Font("Arial", Font.BOLD, 40);
        StdDraw.setFont(font1);
        StdDraw.text(w / 2, 0.75 * h, "MENU");
        Font font2 = new Font("Arial", Font.ITALIC, 30);
        StdDraw.setFont(font2);
        StdDraw.text(w / 2, h * 0.575, "New Game(N)");
        StdDraw.text(w / 2, h * 0.525, "Load Game(L)");
        StdDraw.text(w / 2, h * 0.475, "Customize(C)");
        StdDraw.text(w / 2, h * 0.425, "Quit(:Q)");
        StdDraw.show();
    }

    /** Renders the map instance. */
    void render() {
        StdDraw.show();
        if (lighting) {
            ter.renderFrame(finalMap.getDarkWorld());
        } else {
            ter.renderFrame(world);
        }
        if (StdDraw.mouseY() < HEIGHT - 10) {
            currTile = world
                    [(int) Math.floor(StdDraw.mouseX())]
                    [(int) Math.floor(StdDraw.mouseY())];
        }
        hud.updateAll(player,
                ghost,
                finalMap.getKeys().size(),
                finalMap.getPlayerLives(),
                finalMap.getDisplayString(),
                finalMap.getDisplayColor(),
                currTile);
        StdDraw.show();
    }

    /** Gets the seed, as desired.
     * @return seed in engine
     */
    long getSeed() {
        return givenSeed;
    }

    /** Gets the world.
     * @return world in engine
     */
    TETile[][] getWorld() {
        return world;
    }

    /** Gets the randomSeed.
     * @return randomSeed in engine
     */
    Random getSeedRandom() {
        return seedRandom;
    }
}
