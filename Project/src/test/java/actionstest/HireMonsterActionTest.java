package actionstest;

import de.unisaarland.cs.se.selab.actionpackage.HireMonsterAction;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class HireMonsterActionTest {
    @Test
    void trivialTest() {
        final HireMonsterAction hma = new HireMonsterAction(0, 1);
        Assertions.assertEquals(0, hma.getCommID());
        Assertions.assertEquals(1, hma.getMonster());
    }
}
