package byow.TileEngine;

import java.awt.Color;

/**
 * Contains constant tile objects, to avoid having to remake the same tiles in different parts of
 * the code.
 *
 * You are free to (and encouraged to) create and add your own tiles to this file. This file will
 * be turned in with the rest of your code.
 *
 * Ex:
 *      world[x][y] = Tileset.FLOOR;
 *
 * The style checker may crash when you try to style check this file due to use of unicode
 * characters. This is OK.
 */

public class Tileset {
    private static Color purpleColorDark = new Color(
            Color.yellow.getRed(),
            Color.yellow.getBlue(),
            Color.yellow.getGreen(),
            Color.yellow.getAlpha() - 100);

    private static Color purpleColorBright = new Color(
            Color.yellow.getRed(),
            Color.yellow.getBlue(),
            Color.yellow.getGreen(),
            Color.yellow.getAlpha() - 75);

    public static final TETile AVATAR = new TETile('@', Color.white, Color.black, "you");
    public static final TETile WALL = new TETile('#', new Color(216, 128, 128), Color.darkGray,
            "wall");
    public static final TETile FLOOR = new TETile('·', new Color(128, 192, 128), Color.black,
            "floor");
    public static final TETile NOTHING = new TETile(' ', Color.black, Color.black, "nothing");
    public static final TETile GRASS = new TETile('"', Color.green, Color.black, "grass");
    public static final TETile WATER = new TETile('≈', Color.blue, Color.black, "water");
    public static final TETile FLOWER = new TETile('❀', Color.magenta, Color.pink, "flower");
    public static final TETile LOCKED_DOOR = new TETile('█', Color.orange, Color.black,
            "locked door");
    public static final TETile UNLOCKED_DOOR = new TETile('▢', Color.orange, Color.black,
            "unlocked door");
    public static final TETile SAND = new TETile('▒', Color.yellow, Color.black, "sand");
    public static final TETile MOUNTAIN = new TETile('▲', Color.gray, Color.black, "mountain");
    public static final TETile TREE = new TETile('♠', Color.green, Color.black, "tree");
    public static final TETile PLAYER = new TETile('☺', Color.white, Color.black, "player");
    public static final TETile GHOST = new TETile('☠', Color.red, Color.black, "ghost");
    public static final TETile KEY = new TETile('⚷', Color.yellow, Color.black, "key");
    public static final TETile LAMP = new TETile('☀', purpleColorDark, Color.black, "lamp");


    public static TETile newTextColor(TETile tileType, Color c) {
        return new TETile(
                tileType.character(),
                c,
                tileType.getBackgroundColor(),
                tileType.description());
    }

    public static TETile modTile(double a, TETile tileType) {
        double alpha = Math.min(a, 100);
        Color text = tileType.getTextColor();
        Color bg = tileType.getBackgroundColor();
        return new TETile(tileType.character(),
                new Color(text.getRed(),
                        text.getGreen(),
                        text.getBlue(),
                        (int) ((text.getAlpha()) * alpha / 100)),
                new Color(bg.getRed(),
                        bg.getGreen(),
                        bg.getBlue(),
                        (int) ((bg.getAlpha()) * alpha / 100)),
                tileType.description());
    }

    public static Color getPurpleColorDark() {
        return purpleColorDark;
    }

    public static Color getPurpleColorBright() {
        return purpleColorBright;
    }
}


