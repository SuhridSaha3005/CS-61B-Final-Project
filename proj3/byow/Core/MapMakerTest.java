package byow.Core;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import org.junit.Test;

import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertEquals;

public class MapMakerTest {

    @Test
    public void longestHallwayTest() {
        Engine e = new Engine();
        MapMaker m = new MapMaker(e.RANDOM, e.world, Engine.WIDTH, Engine.HEIGHT);
        assertEquals(4, m.longestHallway(new XYPosn(5, 10), 180));
        assertEquals(5, m.longestHallway(new XYPosn(Engine.WIDTH - 7, 10), 0));
        assertEquals(1, m.longestHallway(new XYPosn(10, 2), 270));
        assertEquals(0, m.longestHallway(new XYPosn(10, 1), 270));
        assertEquals(3, m.longestHallway(new XYPosn(1, Engine.HEIGHT - 5), 90));
        assertEquals(0, m.longestHallway(new XYPosn(10, Engine.HEIGHT), 90));
        e.world[2][10] = Tileset.WALL;
        assertEquals(2, m.longestHallway(new XYPosn(5, 10), 180));
        e.world[2][10] = Tileset.FLOOR;
        assertEquals(2, m.longestHallway(new XYPosn(5, 10), 180));
        e.world[2][10] = Tileset.NOTHING;
        assertEquals(4, m.longestHallway(new XYPosn(5, 10), 180));
    }

    public static void hallwayMakerTest() {
        Engine e = new Engine();
        MapMaker m = new MapMaker(e.RANDOM, e.world, Engine.WIDTH, Engine.HEIGHT);

        m.hallwayMaker(new XYPosn(10, 15), 4, 0);
        m.hallwayMaker(new XYPosn(10, 15), 4, 90);
        m.hallwayMaker(new XYPosn(10, 15), 4, 180);
        m.hallwayMaker(new XYPosn(10, 15), 4, 270);

        m.hallwayMaker(new XYPosn(18, 15), 8, 0);
        m.hallwayMaker(new XYPosn(20, 15), 4, 90);
        m.hallwayMaker(new XYPosn(20, 15), 4, 180);
        m.hallwayMaker(new XYPosn(20, 15), 4, 270);

        m.hallwayMaker(new XYPosn(33, 10), 8, 90);
        m.hallwayMaker(new XYPosn(30, 13), 5, 0);
        m.hallwayMaker(new XYPosn(30, 17), 5, 0);

        m.hallwayMaker(new XYPosn(43, 10), 12, 90);
        m.hallwayMaker(new XYPosn(40, 13), 5, 0);
        m.hallwayMaker(new XYPosn(40, 17), 5, 0);

        m.hallwayMaker(new XYPosn(50, 15), 5, 90);
        m.hallwayMaker(new XYPosn(50, 14), 5, 270);

        m.hallwayMaker(new XYPosn(50, 15), 5, 90);
        m.hallwayMaker(new XYPosn(50, 14), 5, 270);
        m.hallwayMaker(new XYPosn(50, 15), 5, 0);
        m.hallwayMaker(new XYPosn(50, 14), 5, 0);

        m.hallwayMaker(new XYPosn(50, 15), 5, 90);
        m.hallwayMaker(new XYPosn(50, 14), 5, 270);
        m.hallwayMaker(new XYPosn(50, 15), 5, 0);
        m.hallwayMaker(new XYPosn(50, 14), 5, 0);

        m.hallwayMaker(new XYPosn(60, 15), 5, 0);
        m.hallwayMaker(new XYPosn(65, 15), 5, 90);

        m.hallwayMaker(new XYPosn(75, 15), 5, 180);
        m.hallwayMaker(new XYPosn(70, 15), 5, 90);

        e.render();
    }

    public static void hallwayScamTest() {
        Random random = new Random(45);
        Engine e = new Engine();
        MapMaker m = new MapMaker(e.RANDOM, e.world, Engine.WIDTH, Engine.HEIGHT);

        for (int i = 0; i < 200; i += 1) {
            int x = RandomUtils.uniform(random, 7, 73);
            int y = RandomUtils.uniform(random, 7, 23);
            int len = RandomUtils.uniform(random, 3, 7);
            int idx = RandomUtils.uniform(random, 0, 4);
            List<Integer> l = List.of(0, 90, 180, 270);
            m.hallwayMaker(new XYPosn(x, y), len, l.get(idx));
        }

        e.render();
    }


    public static void hallwayLongestMakerTest() {
        Engine e = new Engine();
        MapMaker m = new MapMaker(e.RANDOM, e.world, Engine.WIDTH, Engine.HEIGHT);

        m.longestHallwayMaker(new XYPosn(10, 15), 0);
        // m.longestHallwayMaker(new XYPosn(10, 15),90);
        // m.longestHallwayMaker(new XYPosn(10, 15), 180);
        m.longestHallwayMaker(new XYPosn(10, 15), 270);

        /* m.longestHallwayMaker(new XYPosn(18, 15), 0);
        m.longestHallwayMaker(new XYPosn(20, 15), 90);
        m.longestHallwayMaker(new XYPosn(20, 15), 180);
        m.longestHallwayMaker(new XYPosn(20, 15), 270);

        m.longestHallwayMaker(new XYPosn(33, 10), 90);
        m.longestHallwayMaker(new XYPosn(30, 13), 0);
        m.longestHallwayMaker(new XYPosn(30, 17), 0);

        m.longestHallwayMaker(new XYPosn(43, 10), 90);
        m.longestHallwayMaker(new XYPosn(40, 13), 0);
        m.longestHallwayMaker(new XYPosn(40, 17), 0);

        m.longestHallwayMaker(new XYPosn(50, 15), 90);
        m.longestHallwayMaker(new XYPosn(50, 14), 270);

        m.longestHallwayMaker(new XYPosn(50, 15), 90);
        m.longestHallwayMaker(new XYPosn(50, 14), 270);
        m.longestHallwayMaker(new XYPosn(50, 15), 0);
        m.longestHallwayMaker(new XYPosn(50, 14), 0);

        m.longestHallwayMaker(new XYPosn(50, 15), 90);
        m.longestHallwayMaker(new XYPosn(50, 14), 270);
        m.longestHallwayMaker(new XYPosn(50, 15), 0);
        m.longestHallwayMaker(new XYPosn(50, 14), 0); */

        e.render();
    }

    public static void newHallwayTest() {
        Engine e = new Engine();
        MapMaker m = new MapMaker(e.RANDOM, e.world, Engine.WIDTH, Engine.HEIGHT);

        System.out.println(m.newHallwayDirLength(new XYPosn(10, 15)));

        e.render();
    }

    @Test
    public void randomizedGeneratorTest() {
        Engine e = new Engine();
        MapMaker m = new MapMaker(new Random(), e.world, Engine.WIDTH, Engine.HEIGHT);
        for (int i  = 1; i < 10000; i *= 3) {
            System.out.println(m.newHallwayDirLength(new XYPosn(10, 15)));
        }
    }

    public static void addRandomHallwaysTest() {
        Engine e = new Engine();
        MapMaker m = new MapMaker(new Random(6), e.world, Engine.WIDTH, Engine.HEIGHT);
        List<XYPosn> k = m.addMultiSpringHallways(new XYPosn(40, 10));
        singlePath(m, k);
        e.render();
    }

    public static void singlePath(MapMaker m, List<XYPosn> k) {
        for (int i = 0; i < 10; i += 1) {
            if (k == null) {
                continue;
            }
            for (XYPosn entry : k) {
                k = m.addMultiSpringHallways(entry);
                singlePath(m, k);
            }
        }
    }

    public static void makeMapTest() {
        Engine e = new Engine();
        MapMaker m = new MapMaker(new Random(2), e.world, Engine.WIDTH, Engine.HEIGHT);
        m.makeMap();
        e.render();
    }

    public static void generatorTest1() {
        Engine e = new Engine();
        GeneratorHelper g = new GeneratorHelper(e.world);
        XYPosn entry = new XYPosn(40, 10);
        GeneratorHelper.RoomStuff roomInfo  = g.randomRoom(new Random(), entry, 270);
        Room r = new Room(e.world, roomInfo.origin, entry, roomInfo.width, roomInfo.length);
        r.addRoom();
        e.render();
    }


    public static void main(String[] args) {
        // hallwayMakerTest();
        // hallwayScamTest();
        // hallwayLongestMakerTest();
        // newHallwayTest();
        // randomizedGeneratorTest();
        // addRandomHallwaysTest();
        // makeMapTest();
        generatorTest1();
    }
}
