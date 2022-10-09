package de.unisaarland.cs.se.selab.systemtest.biddingphase;

import de.unisaarland.cs.se.selab.comm.BidType;
import de.unisaarland.cs.se.selab.comm.TimeoutException;
import de.unisaarland.cs.se.selab.systemtest.HelperClass;

public class FirstPlayerHaveFinishedBidding extends HelperClass {

    public FirstPlayerHaveFinishedBidding() {
        super(FirstPlayerHaveFinishedBidding.class, false);
    }

    @Override
    public void run() throws TimeoutException {
        final int numberOfPlayer = 2;
        registrationEventWrapper(numberOfPlayer);
        sendPlaceBid(0, BidType.FOOD, 1);
        bidPlaced(BidType.FOOD, 0, 1);
        assertActNow(0);
        sendPlaceBid(0, BidType.NICENESS, 2);
        bidPlaced(BidType.NICENESS, 0, 2);
        assertActNow(0);
        sendPlaceBid(0, BidType.TUNNEL, 3);
        bidPlaced(BidType.TUNNEL, 0, 3);
        leave();
    }
}
