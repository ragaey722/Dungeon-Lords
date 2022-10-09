package starterparsertest;

import de.unisaarland.cs.se.selab.Parser;
import de.unisaarland.cs.se.selab.datapackage.Adventurer;
import de.unisaarland.cs.se.selab.datapackage.AttackStrategy;
import de.unisaarland.cs.se.selab.datapackage.Monster;
import de.unisaarland.cs.se.selab.datapackage.Resources;
import de.unisaarland.cs.se.selab.datapackage.Room;
import de.unisaarland.cs.se.selab.datapackage.RoomRestriction;
import de.unisaarland.cs.se.selab.datapackage.Trap;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ParserTest {

    @Test
    void testParseCheckConfig() {
        final String path = "./src/test/java/starterparsertest/configfailtest.json";
        final Parser parser = new Parser(path);
        Assertions.assertFalse(parser.parseCheckConfig());
    }

    @Test
    void testParseCheckConfigTrue() {
        final String path = "./src/test/java/starterparsertest/configtest.json";
        final Parser parser = new Parser(path);
        Assertions.assertTrue(parser.parseCheckConfig());
    }

    @Test
    void testReadConfig() {
        final String path = "./src/test/java/starterparsertest/readfiletest.json";
        final String tester = "{}";
        final Parser parser = new Parser(path);
        Assertions.assertEquals(tester, parser.readConfig());
    }

    @Test
    void testBasicResource() {
        final Resources defaultResource = new Resources(3, 5, 3, 3);
        final String path = "./src/test/java/starterparsertest/configtest.json";
        final Parser parser = new Parser(path);
        Assertions.assertTrue(parser.parseCheckConfig());
        Assertions.assertEquals(defaultResource.getCoins(), parser.getResources().getCoins());
        Assertions.assertEquals(defaultResource.getEvilness(), parser.getResources().getEvilness());
        Assertions.assertEquals(defaultResource.getFoods(), parser.getResources().getFoods());
        Assertions.assertEquals(defaultResource.getImps(), parser.getResources().getImps());
    }

    @Test
    void testBasicGetters() {
        final String path = "./src/test/java/starterparsertest/configtest.json";
        final Parser parser = new Parser(path);
        Assertions.assertTrue(parser.parseCheckConfig());
        Assertions.assertEquals(4, parser.getMaxPlayers());
        Assertions.assertEquals(2, parser.getMaxYears());
        Assertions.assertEquals(5, parser.getDungeonSideLength());
    }

    @Test
    void testMonster() {
        final List<Monster> monsterList = new ArrayList<>();
        monsterList.add(new Monster(0, 1, 1, 2, AttackStrategy.BASIC));
        monsterList.add(new Monster(1, 0, 3, 2, AttackStrategy.MULTI));
        monsterList.add(new Monster(2, 2, 0, 1, AttackStrategy.TARGETED));
        monsterList.add(new Monster(3, 2, 1, 1, AttackStrategy.BASIC));
        monsterList.add(new Monster(4, 3, 0, 2, AttackStrategy.MULTI));

        final String path = "./src/test/java/starterparsertest/configtest.json";
        final Parser parser = new Parser(path);
        Assertions.assertTrue(parser.parseCheckConfig());
        int index = 0;
        while (index < 5) {
            Assertions.assertEquals(monsterList.get(index).id(),
                    parser.getMonsters().get(index).id());
            Assertions.assertEquals(monsterList.get(index).damage(),
                    parser.getMonsters().get(index).damage());
            Assertions.assertEquals(monsterList.get(index).evilness(),
                    parser.getMonsters().get(index).evilness());
            Assertions.assertEquals(monsterList.get(index).hunger(),
                    parser.getMonsters().get(index).hunger());
            Assertions.assertEquals(monsterList.get(index).attackStrategy(),
                    parser.getMonsters().get(index).attackStrategy());
            index++;
        }

    }

    @Test
    void testRoom() {
        final List<Room> rooms = new ArrayList<>();
        rooms.add(new Room(0, RoomRestriction.LOWER_HALF, 3,
                new Resources(1, 0, 0, 0)));
        rooms.add(new Room(1, RoomRestriction.UPPER_HALF, 2,
                new Resources(0, 0, 0, 1)));
        rooms.add(new Room(2, RoomRestriction.OUTER, 4,
                new Resources(1, 1, 0, 0)));
        rooms.add(new Room(3, RoomRestriction.LOWER_HALF, 3,
                new Resources(0, 0, 1, 0)));
        rooms.add(new Room(4, RoomRestriction.INNER, 3,
                new Resources(0, 1, 0, 0)));

        final String path = "./src/test/java/starterparsertest/configtest.json";
        final Parser parser = new Parser(path);
        Assertions.assertTrue(parser.parseCheckConfig());
        int index = 0;
        while (index < 5) {
            Assertions.assertEquals(rooms.get(index).getId(),
                    parser.getRooms().get(index).getId());
            Assertions.assertEquals(rooms.get(index).getRestriction(),
                    parser.getRooms().get(index).getRestriction());
            Assertions.assertEquals(rooms.get(index).getImpsCost(),
                    parser.getRooms().get(index).getImpsCost());
            Assertions.assertEquals(rooms.get(index).getRewards().getImps(),
                    parser.getRooms().get(index).getRewards().getImps());
            Assertions.assertEquals(rooms.get(index).getRewards().getFoods(),
                    parser.getRooms().get(index).getRewards().getFoods());
            Assertions.assertEquals(rooms.get(index).getRewards().getEvilness(),
                    parser.getRooms().get(index).getRewards().getEvilness());
            Assertions.assertEquals(rooms.get(index).getRewards().getCoins(),
                    parser.getRooms().get(index).getRewards().getCoins());
            index++;
        }

    }

    @Test
    void testAdventurer() {
        final List<Adventurer> adventurerList = new ArrayList<>();
        adventurerList.add(new Adventurer(0, 1, 3, 0,
                0, true));
        adventurerList.add(new Adventurer(1, 2, 3, 1,
                0, false));
        adventurerList.add(new Adventurer(2, 3, 4, 0,
                1, false));
        adventurerList.add(new Adventurer(3, 4, 4, 2,
                0, false));
        adventurerList.add(new Adventurer(4, 5, 5, 0,
                2, true));

        final String path = "./src/test/java/starterparsertest/configtest.json";
        final Parser parser = new Parser(path);
        Assertions.assertTrue(parser.parseCheckConfig());
        int index = 0;
        while (index < 5) {
            Assertions.assertEquals(adventurerList.get(index).getId(),
                    parser.getAdventurers().get(index).getId());
            Assertions.assertEquals(adventurerList.get(index).getCharge(),
                    parser.getAdventurers().get(index).getCharge());
            Assertions.assertEquals(adventurerList.get(index).getDefuseValue(),
                    parser.getAdventurers().get(index).getDefuseValue());
            Assertions.assertEquals(adventurerList.get(index).getHP(),
                    parser.getAdventurers().get(index).getHP());
            Assertions.assertEquals(adventurerList.get(index).getDifficulty(),
                    parser.getAdventurers().get(index).getDifficulty());
            Assertions.assertEquals(adventurerList.get(index).getHealValue(),
                    parser.getAdventurers().get(index).getHealValue());
            index++;
        }
    }

    @Test
    void testTrap() {
        final List<Trap> traps = new ArrayList<>();
        traps.add(new Trap(3, 3, 0, AttackStrategy.BASIC));
        traps.add(new Trap(4, 1, 0, AttackStrategy.MULTI));
        traps.add(new Trap(5, 1, 0, AttackStrategy.MULTI));
        traps.add(new Trap(6, 1, 0, AttackStrategy.MULTI));
        traps.add(new Trap(7, 1, 0, AttackStrategy.MULTI));
        traps.add(new Trap(8, 2, 1, AttackStrategy.TARGETED));

        final String path = "./src/test/java/starterparsertest/configtest.json";
        final Parser parser = new Parser(path);
        Assertions.assertTrue(parser.parseCheckConfig());
        int index = 0;
        while (index < 5) {
            final int indexparser = index + 3;
            Assertions.assertEquals(traps.get(index).id(),
                    parser.getTraps().get(indexparser).id());
            Assertions.assertEquals(traps.get(index).attackStrategy(),
                    parser.getTraps().get(indexparser).attackStrategy());
            Assertions.assertEquals(traps.get(index).damage(),
                    parser.getTraps().get(indexparser).damage());
            index++;
        }

    }

}
