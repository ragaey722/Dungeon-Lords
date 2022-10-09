package actionstest;

import de.unisaarland.cs.se.selab.actionpackage.EndTurnAction;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class EndTurnActionTest {
    @Test
    void trivialTest() {
        final EndTurnAction eta = new EndTurnAction(0);
        Assertions.assertEquals(0, eta.getCommID());
    }
}