package actionstest;

import de.unisaarland.cs.se.selab.actionpackage.DigTunnelAction;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class DigTunnelActionTest {
    @Test
    void actionCreationTest() {
        final DigTunnelAction dta = new DigTunnelAction(0, 0, 0);
        Assertions.assertEquals(0, dta.getCommID());
        Assertions.assertEquals(0, dta.getX());
        Assertions.assertEquals(0, dta.getY());
    }
}
