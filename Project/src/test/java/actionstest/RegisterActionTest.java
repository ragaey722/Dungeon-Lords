package actionstest;

import de.unisaarland.cs.se.selab.actionpackage.RegisterAction;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class RegisterActionTest {
    @Test
    void testActionCreation() {
        final RegisterAction ra = new RegisterAction(0, "John");
        Assertions.assertEquals(0, ra.getCommID());
        Assertions.assertEquals("John", ra.getPlayerName());
    }
}
