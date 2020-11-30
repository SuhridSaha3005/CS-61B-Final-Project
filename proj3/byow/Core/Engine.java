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

    /** Turn on/off lighting. */
    boolean lighting = false;

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

    private void playGame() {
        gameOver = false;
        StringBuilder savedGame = new StringBuilder();
        StringBuilder randomSeed = new StringBuilder();
        gameKeys = new StringBuilder();
        boolean gameInitialized = false;
        boolean seedFinished = false;
        boolean quit = false;
        boolean customize = false;
        int i,j;
        i = j = 0;
        while (!gameOver) {
            if (StdDraw.hasNextKeyTyped()) {
                char c = StdDraw.nextKeyTyped();
                if (c == 'c') {
                    customize = true;
                    StdDraw.clear(Color.BLACK);
                    double w = WIDTH;
                    double h = HEIGHT;
                    StdDraw.setFont(new Font("Arial", Font.PLAIN, 30));
                    StdDraw.setPenColor(Color.white);
                    StdDraw.text(w/2, 0.575*h, "White(W): ☺");
                    StdDraw.setPenColor(Color.blue);
                    StdDraw.text(w/2, 0.525*h, "Blue(B): ☺");
                    StdDraw.setPenColor(Color.green);
                    StdDraw.text(w/2, 0.475*h, "Green(G): ☺");
                    StdDraw.setPenColor(Color.yellow);
                    StdDraw.text(w/2, 0.425*h, "Exit(E)");
                    StdDraw.show();
                }
                if (customize) {
                    if (c == 'w') {
                        playerTile = Tileset.newTextColor(playerTile, Color.WHITE);
                        customize = false;
                        interactWithKeyboard();
                    } else if (c == 'b') {
                        playerTile = Tileset.newTextColor(playerTile, Color.BLUE);
                        customize = false;
                        interactWithKeyboard();
                    } else if (c == 'g') {
                        playerTile = Tileset.newTextColor(playerTile, Color.GREEN);
                        customize = false;
                        interactWithKeyboard();
                    } else if (c == 'e') {
                        customize = false;
                        interactWithKeyboard();
                    }
                }
                if (c == 'l') {
                    world = loadAndInteractWithKeyboard(SaveNLoad.loadGame());
                } else if (c == ':') {
                    quit = true;
                } else if (quit && c == 'q') {
                    gameOver = true;
                    if (gameInitialized && seedFinished) {
                        SaveNLoad.saveGame(savedGame.toString());
                    }
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
                    StdDraw.clear(Color.BLACK);
                    double w = WIDTH;
                    double h = HEIGHT;
                    StdDraw.setPenColor(Color.white);
                    Font font = new Font("Arial", Font.PLAIN, 30);
                    StdDraw.setFont(font);
                    StdDraw.text(w/2, h/2, "Seed: ");
                    StdDraw.show();
                } else if (Character.isDigit(c) && gameInitialized) {
                    randomSeed.append(c);
                    savedGame.append(c);
                    StdDraw.clear(Color.BLACK);
                    double w = WIDTH;
                    double h = HEIGHT;
                    StdDraw.setPenColor(Color.white);
                    Font font = new Font("Arial", Font.PLAIN, 30);
                    StdDraw.setFont(font);
                    StdDraw.text(w/2, h/2, "Seed: " + randomSeed.toString());
                    StdDraw.show();
                }
                if (gameInitialized && seedFinished) {
                    if ("wasdf".contains(Character.toString(c))) {
                        savedGame.append(c);
                        gameKeys.append(c);
                        player.move(c);
                        if (c == 'f') {
                            finalMap.toggleFlashlight();
                        }
                        ArrayList<XYPosn> ghostPosn = new ArrayList<>();
                        for (Avatar g: ghost) {
                            g.randomMove(seedRandom, finalMap.getWallObjs(), player, finalMap.getFlashState());
                            ghostPosn.add(g.getPosn());
                        }
                        finalMap.updatePosn(player.getPosn(), ghostPosn, player);
                        if (finalMap.isGameOver()) {
                            render();
                            StdDraw.pause(3000);
                            startGame();
                            playGame();
                            return;
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
        // If you comment out this below part upto and including StdDraw.show(), the autograder stops complaining abt the StdDraw.
        if (givenSeed != -1) {
            initialize();
            createWorld();
            render();
            runWorldKeys();
            if (!gameOver) {
                int i,j;
                i = j = 0;
                while (!gameOver) {
                    if (StdDraw.hasNextKeyTyped()) {
                        char c = StdDraw.nextKeyTyped();
                        if (c == 'q') {
                            gameOver = true;
                            if (gameInitialized && seedFinished) {
                                SaveNLoad.saveGame(savedGame.toString());
                            }
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
                                g.randomMove(seedRandom, finalMap.getWallObjs(), player, finalMap.getFlashState());
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
        int numGhosts = 10;


        world = new TETile[WIDTH][height];
        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < height; y += 1) {
                world[x][y] = Tileset.NOTHING;
            }
        }

        MapMaker map = new MapMaker(seedRandom, world, WIDTH, height);
        map.makeMap();

        finalMap = new Overlay(seedRandom, world, WIDTH, height);
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
        StdDraw.text(w/2, 0.75*h, "MENU");
        Font font2 = new Font("Arial", Font.ITALIC, 30);
        StdDraw.setFont(font2);
        StdDraw.text(w/2, h*0.575, "New Game(N)");
        StdDraw.text(w/2, h*0.525, "Load Game(L)");
        StdDraw.text(w/2, h*0.475, "Customize(C)");
        StdDraw.text(w/2, h*0.425, "Quit(:Q)");
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
            currTile = world[(int) Math.floor(StdDraw.mouseX())][(int) Math.floor(StdDraw.mouseY())];
        }
        hud.updateAll(  player,
                        ghost,
                        finalMap.getKeys().size(),
                        finalMap.getPlayerLives(),
                        finalMap.getDisplayString(),
                        finalMap.getDisplayColor(),
                        currTile
                    );
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

    public static void main(String[] args) {
        Engine e = new Engine();
        // e.loadAndInteractWithKeyboard("n123saww:qlwsdddd");
        e.interactWithKeyboard();
    }
}
