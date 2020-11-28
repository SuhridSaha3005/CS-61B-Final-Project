package byow.Core;

import edu.princeton.cs.introcs.In;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class SaveNLoad {

    public static void saveGame(String game) throws FileNotFoundException {
        PrintWriter writer = new PrintWriter("byow/core/SavedGame.txt");
        writer.print(game);
        writer.close();
    }

    public static String loadGame() {
        In in = new In("byow/core/SavedGame.txt");
        return in.readString();
    }

    public static void main(String[] args) throws FileNotFoundException {
        saveGame("Test");
        System.out.println(loadGame());
        saveGame("Stuff");
        System.out.println(loadGame());
    }
}
