package datatest;

import de.unisaarland.cs.se.selab.comm.BidType;
import de.unisaarland.cs.se.selab.datapackage.Adventurer;
import de.unisaarland.cs.se.selab.datapackage.AttackStrategy;
import de.unisaarland.cs.se.selab.datapackage.DungeonLord;
import de.unisaarland.cs.se.selab.datapackage.Monster;
import de.unisaarland.cs.se.selab.datapackage.Resources;
import de.unisaarland.cs.se.selab.datapackage.Trap;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class DungeonLordTest {

    @Test
    void trivialGetterTest() {
        final Resources resources = new Resources(3, 5, 3, 3);
        final DungeonLord dl = new DungeonLord(0, 3, resources);
        Assertions.assertTrue(dl.getBiddings().isEmpty());
        dl.addBidding(BidType.FOOD, 1);
        Assertions.assertFalse(dl.getBiddings().isEmpty());

        dl.addLockedBiddings(BidType.GOLD);
        final List<BidType> dummyList = new ArrayList<>();
        dummyList.add(BidType.GOLD);
        Assertions.assertEquals(dummyList, dl.getLockedBiddings());

        dl.addMonster(new Monster(1, 1, 1, 1, AttackStrategy.MULTI));
        Assertions.assertEquals(1, dl.getNumberOfMonsters());

        dl.addTrap(new Trap(1, 1, 0, AttackStrategy.MULTI));
        Assertions.assertEquals(1, dl.getTraps().size());
    }

    @Test
    void canUseMonsterTargetedAndCanUseMonsterTest() {
        final DungeonLord dl = new DungeonLord(0, 5,
                new Resources(0, 0, 0, 0));
        final Adventurer adv1 = new Adventurer(0, 0, 5, 0, 0, true);
        dl.getDungeon().queueAdventurer(adv1);
        final Monster monster1 = new Monster(0, 0, 0, 0, AttackStrategy.BASIC);
        final Monster monster2 = new Monster(1, 0, 0, 0, AttackStrategy.MULTI);
        final Monster monster3 = new Monster(2, 0, 0, 0, AttackStrategy.TARGETED);
        dl.addMonster(monster1);
        dl.addMonster(monster2);
        dl.addMonster(monster3);
        Assertions.assertTrue(dl.canUseMonster(0) && dl.canUseMonster(1));
        Assertions.assertFalse(dl.canUseMonster(2));
        Assertions.assertTrue(dl.canUseMonsterTargeted(2, 1));
        Assertions.assertFalse(dl.canUseMonsterTargeted(0, 1)
                || dl.canUseMonsterTargeted(1, 1));
        Assertions.assertFalse(dl.canUseMonsterTargeted(2, 4)
                || dl.canUseMonsterTargeted(2, 0));
        Assertions.assertTrue(dl.canUseMonsterTargeted(2, 3));
    }

}
