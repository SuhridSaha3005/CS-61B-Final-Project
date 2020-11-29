package byow.Core;

import edu.princeton.cs.introcs.In;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.nio.file.Paths;

public class SaveNLoad {

    public static void saveGame(String game) {
        try {
            PrintWriter writer = new PrintWriter("SavedGame.txt");
            writer.print(game);
            writer.close();
        } catch (FileNotFoundException f) {
            System.out.println("Could not find file.");
        }
    }

    public static String loadGame() {
        In in = new In("byow/core/SavedGame.txt");
        return in.readString();
    }

    public static void main(String[] args) {
        System.out.println(loadGame());
    }
}
