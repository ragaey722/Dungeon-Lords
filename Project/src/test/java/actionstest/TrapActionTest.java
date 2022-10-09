package actionstest;

import de.unisaarland.cs.se.selab.actionpackage.TrapAction;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class TrapActionTest {
    @Test
    void trivialTest() {
        final TrapAction ta = new TrapAction(0, 4);
        Assertions.assertEquals(0, ta.getCommID());
        Assertions.assertEquals(4, ta.getTrapID());
    }
}
