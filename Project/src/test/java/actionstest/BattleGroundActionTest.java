package actionstest;

import de.unisaarland.cs.se.selab.actionpackage.BattleGroundAction;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class BattleGroundActionTest {
    @Test
    void testBattleGroundAction() {
        final BattleGroundAction bga = new BattleGroundAction(1, 2, 3);
        Assertions.assertEquals(2, bga.getX());
        Assertions.assertEquals(3, bga.getY());
        Assertions.assertEquals(1, bga.getCommID());

    }
}
