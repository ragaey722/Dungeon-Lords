package de.unisaarland.cs.se.selab.systemtest.biddingphase;

import de.unisaarland.cs.se.selab.comm.BidType;
import de.unisaarland.cs.se.selab.comm.TimeoutException;
import de.unisaarland.cs.se.selab.systemtest.HelperClass;

public class PlacingBidTest extends HelperClass {

    public PlacingBidTest() {
        super(PlacingBidTest.class, false);
    }

    @Override
    public void run() throws TimeoutException {
        final int numberOfPlayer = 2;
        registrationEventWrapper(numberOfPlayer);
        sendPlaceBid(0, BidType.FOOD, 1);
        bidPlaced(BidType.FOOD, 0, 1);
        assertActNow(0);
        sendPlaceBid(1, BidType.FOOD, 1);
        bidPlaced(BidType.FOOD, 1, 1);
        assertActNow(1);
        leave();
    }
}
