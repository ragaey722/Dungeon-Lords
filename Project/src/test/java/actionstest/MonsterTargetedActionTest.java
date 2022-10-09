package actionstest;

import de.unisaarland.cs.se.selab.actionpackage.MonsterTargetedAction;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class MonsterTargetedActionTest {
    @Test
    void testActionCreation() {
        final MonsterTargetedAction mta = new MonsterTargetedAction(1, 2, 0);
        Assertions.assertEquals(2, mta.getMonster());
        Assertions.assertEquals(0, mta.getPosition());
    }
}
