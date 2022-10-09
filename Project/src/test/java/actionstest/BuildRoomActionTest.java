package actionstest;

import de.unisaarland.cs.se.selab.actionpackage.BuildRoomAction;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class BuildRoomActionTest {
    @Test
    void trivialTest() {
        final BuildRoomAction bra = new BuildRoomAction(0, 0, 0, 4);
        Assertions.assertEquals(0, bra.getCommID());
        Assertions.assertEquals(0, bra.getX());
        Assertions.assertEquals(0, bra.getY());
        Assertions.assertEquals(4, bra.getRoomID());
    }
}
