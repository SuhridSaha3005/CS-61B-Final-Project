package byow.Core;
import byow.TileEngine.Tileset;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.*;

public class MapMakerTest {

    @Test
    public void longestHallwayTest() {
        Engine e = new Engine();
        MapMaker m = new MapMaker(e.getSeedRandom(), e.getWorld(), Engine.WIDTH, Engine.HEIGHT);
        assertEquals(4, m.longestHallway(new XYPosn(5, 10), 180));
        assertEquals(5, m.longestHallway(new XYPosn(Engine.WIDTH - 7, 10), 0));
        assertEquals(1, m.longestHallway(new XYPosn(10, 2), 270));
        assertEquals(0, m.longestHallway(new XYPosn(10, 1), 270));
        assertEquals(3, m.longestHallway(new XYPosn(1, Engine.HEIGHT - 5), 90));
        assertEquals(0, m.longestHallway(new XYPosn(10, Engine.HEIGHT), 90));
        e.getWorld()[2][10] = Tileset.WALL;
        assertEquals(2, m.longestHallway(new XYPosn(5, 10), 180));
        e.getWorld()[2][10] = Tileset.FLOOR;
        assertEquals(2, m.longestHallway(new XYPosn(5, 10), 180));
        e.getWorld()[2][10] = Tileset.NOTHING;
        assertEquals(4, m.longestHallway(new XYPosn(5, 10), 180));
    }

    public static void hallwayMakerTest() {
        Engine e = new Engine();
        MapMaker m = new MapMaker(e.getSeedRandom(), e.getWorld(), Engine.WIDTH, Engine.HEIGHT);

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
        MapMaker m = new MapMaker(e.getSeedRandom(), e.getWorld(), Engine.WIDTH, Engine.HEIGHT);

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
        MapMaker m = new MapMaker(e.getSeedRandom(), e.getWorld(), Engine.WIDTH, Engine.HEIGHT);

        //m.longestHallwayMaker(new XYPosn(10, 15), 0);
        // m.longestHallwayMaker(new XYPosn(10, 15),90);
        // m.longestHallwayMaker(new XYPosn(10, 15), 180);
        // m.longestHallwayMaker(new XYPosn(10, 15), 270);

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
        MapMaker m = new MapMaker(e.getSeedRandom(), e.getWorld(), Engine.WIDTH, Engine.HEIGHT);

        System.out.println(m.newHallwayDirLength(new XYPosn(10, 15)));

        e.render();
    }

    @Test
    public void randomizedGeneratorTest() {
        Engine e = new Engine();
        MapMaker m = new MapMaker(new Random(), e.getWorld(), Engine.WIDTH, Engine.HEIGHT);
        for (int i  = 1; i < 10000; i *= 3) {
            System.out.println(m.newHallwayDirLength(new XYPosn(10, 15)));
        }
    }

    public static void addRandomHallwaysTest() {
        Engine e = new Engine();
        MapMaker m = new MapMaker(new Random(6), e.getWorld(), Engine.WIDTH, Engine.HEIGHT);
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
        MapMaker m = new MapMaker(new Random(11111111), e.getWorld(), Engine.WIDTH, Engine.HEIGHT);
        m.makeMap();
        e.render();
    }

    public static void generatorTest1() {
        Engine e = new Engine();
        GeneratorHelper g = new GeneratorHelper(e.getWorld());
        XYPosn entry = new XYPosn(5, 10);
        GeneratorHelper.RoomStuff roomInfo = g.randomRoom(new Random(), entry, 180);
        System.out.println("hiiiiiiiiiiiiiiii");
        Room r = new Room(e.getWorld(), roomInfo.origin, entry, roomInfo.width, roomInfo.length);
        r.addRoom();
        e.render();
    }

    public static void generatorTestCompatibility() {
        Engine e = new Engine();
        GeneratorHelper g = new GeneratorHelper(e.getWorld());
        MapMaker m = new MapMaker(e.getSeedRandom(), e.getWorld(), Engine.WIDTH, Engine.HEIGHT);

        XYPosn entry = m.hallwayMaker(new XYPosn(30, 20), 4, 0);
        entry = m.hallwayMaker(entry, 4, 90);

        GeneratorHelper.RoomStuff roomInfo  = g.randomRoom(new Random(), entry, 90);
        Room r = new Room(e.getWorld(), roomInfo.origin, entry, roomInfo.width, roomInfo.length);
        r.addRoom();
        e.render();

    }

    public static void scamTest2() {
        Engine e = new Engine();
        MapMaker m = new MapMaker(new Random(), e.getWorld(), Engine.WIDTH, Engine.HEIGHT);

        Room r = new Room(e.getWorld(), new XYPosn(30, 12), new XYPosn(30, 13), 5, 6);
        r.addRoom();
        r = new Room(e.getWorld(), new XYPosn(60, 10), new XYPosn(62, 10), 6, 5);
        r.addRoom();
        r = new Room(e.getWorld(), new XYPosn(45, 20), new XYPosn(47, 20), 4, 4);
        r.addRoom();
        r = new Room(e.getWorld(), new XYPosn(20, 20), new XYPosn(22, 20), 4, 4);
        r.addRoom();
        r = new Room(e.getWorld(), new XYPosn(30, 32), new XYPosn(30, 33), 5, 6);
        r.addRoom();
        r = new Room(e.getWorld(), new XYPosn(60, 30), new XYPosn(62, 30), 6, 5);
        r.addRoom();
        r = new Room(e.getWorld(), new XYPosn(45, 40), new XYPosn(47, 40), 4, 4);
        r.addRoom();
        r = new Room(e.getWorld(), new XYPosn(20, 40), new XYPosn(22, 40), 4, 4);
        r.addRoom();


        m.makeMap();
        e.render();
    }

    public static void smallestRoomImpossibleTest() {
        Engine e = new Engine();
        MapMaker m = new MapMaker(new Random(10), e.getWorld(), Engine.WIDTH, Engine.HEIGHT);
        XYPosn entry = new XYPosn(40, 30);
        assertTrue(m.getOrientation(entry) < 0);
        System.out.println(m.getGenHelp().smallestRoomImpossible(entry, m.getOrientation(entry))); // should be false
        entry = m.hallwayMaker(entry, 4, 0);
        entry = m.hallwayMaker(entry, 4, 90);
        System.out.println(entry.getX());
        System.out.println(entry.getY());
        System.out.println(m.getGenHelp().smallestRoomImpossible(entry, 90));  // should be false
        assertArrayEquals(m.getWorld(), m.getGenHelp().world);
        System.out.println(Arrays.equals(m.getWorld(), m.getGenHelp().world)); // should be true
        entry = m.hallwayMaker(entry, 8, 90);
        System.out.println(m.getGenHelp().smallestRoomImpossible(entry, 90)); // should be false
        entry = m.hallwayMaker(entry, 5, 90);
        System.out.println(m.getGenHelp().smallestRoomImpossible(entry, 90)); // should be TRUE
        System.out.println("*******************************************************");
        e.render();
    }

    @Test
    public void getOrientationTest() {
        Engine e = new Engine();
        MapMaker m = new MapMaker(new Random(10), e.getWorld(), Engine.WIDTH, Engine.HEIGHT);
        XYPosn entry = new XYPosn(40, 30);
        assertTrue(m.getOrientation(entry) < 0);
        entry = m.hallwayMaker(entry, 4, 0);
        assertEquals(0, m.getOrientation(entry));
        entry = m.hallwayMaker(entry, 4, 90);
        assertEquals(90, m.getOrientation(entry));
        entry = m.hallwayMaker(entry, 4, 0);
        assertEquals(0, m.getOrientation(entry));
        entry = m.hallwayMaker(entry, 4, 270);
        assertEquals(270, m.getOrientation(entry));

    }

    public static void addMultiRoomSpringTest() {
        Engine e = new Engine();
        GeneratorHelper g = new GeneratorHelper(e.getWorld());
        Random random = new Random(35);
        MapMaker m = new MapMaker(random, e.getWorld(), Engine.WIDTH, Engine.HEIGHT);

        XYPosn entry = m.hallwayMaker(new XYPosn(30, 20), 4, 90);
        System.out.println(g.addMultiSpringRoom(random, entry, 90));
    }

    @Test
    public void sanityInputStringTest() throws FileNotFoundException {
        Engine e1 = new Engine();
        e1.loadAndInteractWithKeyboard("n123s");
        assertEquals(123, e1.getSeed());
        Random r = new Random(123);
        assertEquals(r.nextInt(), e1.getSeedRandom().nextInt());
        assertEquals(r.nextInt(), e1.getSeedRandom().nextInt());
        assertEquals(r.nextInt(), e1.getSeedRandom().nextInt());
        assertEquals(r.nextInt(), e1.getSeedRandom().nextInt());
        assertEquals(r.nextInt(), e1.getSeedRandom().nextInt());
    }

    /* @Test
    public void inputStringTestFinal() {
        Engine e1 = new Engine();
        Engine e2 = new Engine(123L);
        e2.getWorld() = new TETile[e2.WIDTH][e2.HEIGHT];
        for (int x = 0; x < e2.WIDTH; x += 1) {
            for (int y = 0; y < e2.HEIGHT; y += 1) {
                e2.getWorld()[x][y] = Tileset.NOTHING;
            }
        }
        MapMaker m = new MapMaker(e2.getSeedRandom(), e2.getWorld(), Engine.WIDTH, Engine.HEIGHT);
        m.makeMap();
        e1.loadAndInteractWithKeyboard("n123s");
        assertEquals(e1.getSeed(), e2.getSeed());
        assertArrayEquals(e1.createWorld(), m.getWorld());
    } */

    public static void makeMapFinal() throws FileNotFoundException {
        Engine e = new Engine();
        Engine e2 = new Engine();
        e.loadAndInteractWithKeyboard("n123s");
        e2.loadAndInteractWithKeyboard("n5197880843569031643s");
        /* assertArrayEquals(e.createWorld(), e2.createWorld()); */
        e.initialize();
        e.render();
    }

    public static void main(String[] args) throws FileNotFoundException{
        // hallwayMakerTest();
        // hallwayScamTest();
        // hallwayLongestMakerTest();
        // newHallwayTest();
        // randomizedGeneratorTest();
        // addRandomHallwaysTest();
        // makeMapTest();
        // generatorTest1();
        // generatorTestCompatibility();
        // scamTest2();
        // smallestRoomImpossibleTest();
        // addMultiRoomSpringTest();
        makeMapFinal();
    }
}
