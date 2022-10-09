package actionstest;

import de.unisaarland.cs.se.selab.actionpackage.StartGameAction;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class StartGameActionTest {
    @Test
    void testActionCreation() {
        final StartGameAction sga = new StartGameAction(1);
        Assertions.assertEquals(1, sga.getCommID());
    }
}
