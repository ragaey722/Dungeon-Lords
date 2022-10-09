package datatest;

import de.unisaarland.cs.se.selab.datapackage.Adventurer;
import de.unisaarland.cs.se.selab.datapackage.AttackStrategy;
import de.unisaarland.cs.se.selab.datapackage.DungeonLord;
import de.unisaarland.cs.se.selab.datapackage.GameDataClass;
import de.unisaarland.cs.se.selab.datapackage.Monster;
import de.unisaarland.cs.se.selab.datapackage.Resources;
import de.unisaarland.cs.se.selab.datapackage.Room;
import de.unisaarland.cs.se.selab.datapackage.Trap;
import java.util.ArrayList;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class GameDataClassTest {
    @Test
    void testGameData() {
        final GameDataClass gdc = new GameDataClass(new ArrayList<Adventurer>(),
                new ArrayList<Trap>(), new ArrayList<Room>(),
                new ArrayList<Monster>(), 5, new Resources(0, 0, 0, 0));

        Assertions.assertEquals(5, gdc.getMaxSideLength());
        Assertions.assertFalse(gdc.isDungeonLordAvailable(1));
        Assertions.assertFalse(gdc.isMonsterAvailable(1));

        final Monster m = new Monster(1, 1, 1, 1, AttackStrategy.BASIC);
        gdc.addMonster(1, m);
        Assertions.assertTrue(gdc.isMonsterAvailable(1));
        Assertions.assertEquals(m, gdc.getMonster(1));

        gdc.resetMonster();
        Assertions.assertFalse(gdc.isMonsterAvailable(1));


        final DungeonLord dl = new DungeonLord(1, 5, new Resources(0, 0, 0, 0));
        gdc.addDungeonLord(1, dl);
        Assertions.assertEquals(dl, gdc.getDungeonLord(1));
        Assertions.assertTrue(gdc.isDungeonLordAvailable(1));
        gdc.removeDungeonLord(1);
        Assertions.assertFalse(gdc.isDungeonLordAvailable(1));

        Assertions.assertEquals(1, gdc.getYear());
        gdc.setYear(3);
        Assertions.assertEquals(3, gdc.getYear());


    }
}
