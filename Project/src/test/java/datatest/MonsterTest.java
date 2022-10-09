package datatest;

import de.unisaarland.cs.se.selab.datapackage.AttackStrategy;
import de.unisaarland.cs.se.selab.datapackage.Monster;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class MonsterTest {
    @Test
    void testMonster() {
        final Monster m = new Monster(1, 1, 1, 1, AttackStrategy.BASIC);
        Assertions.assertEquals(1, m.id());
        Assertions.assertEquals(1, m.hunger());
        Assertions.assertEquals(1, m.evilness());
        Assertions.assertEquals(1, m.damage());
        Assertions.assertEquals(AttackStrategy.BASIC, m.attackStrategy());
    }

}
