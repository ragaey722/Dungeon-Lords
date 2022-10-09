package actionstest;

import de.unisaarland.cs.se.selab.actionpackage.PlaceBidAction;
import de.unisaarland.cs.se.selab.comm.BidType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class PlaceBidActionTest {
    @Test
    void testActionCreation() {
        final PlaceBidAction pba = new PlaceBidAction(1, BidType.FOOD, 1);
        Assertions.assertEquals(1, pba.getCommID());
        Assertions.assertEquals(BidType.FOOD, pba.getBid());
        Assertions.assertEquals(1, pba.getNumber());
    }

}
