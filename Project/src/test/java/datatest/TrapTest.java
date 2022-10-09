package datatest;

import de.unisaarland.cs.se.selab.datapackage.AttackStrategy;
import de.unisaarland.cs.se.selab.datapackage.Trap;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class TrapTest {

    @Test
    void testTrap() {
        final Trap t = new Trap(1, 3, 2, AttackStrategy.BASIC);
        Assertions.assertEquals(1, t.id());
        Assertions.assertEquals(3, t.damage());
        Assertions.assertEquals(2, t.target());
        Assertions.assertEquals(AttackStrategy.BASIC, t.attackStrategy());
    }
}