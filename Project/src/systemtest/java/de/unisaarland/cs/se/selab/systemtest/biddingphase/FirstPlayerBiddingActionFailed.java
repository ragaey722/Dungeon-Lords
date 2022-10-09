package de.unisaarland.cs.se.selab.systemtest.biddingphase;

import de.unisaarland.cs.se.selab.comm.BidType;
import de.unisaarland.cs.se.selab.comm.TimeoutException;
import de.unisaarland.cs.se.selab.systemtest.HelperClass;


public class FirstPlayerBiddingActionFailed extends HelperClass {


    public FirstPlayerBiddingActionFailed() {
        super(FirstPlayerHaveFinishedBidding.class, false);
    }

    @Override
    public void run() throws TimeoutException {
        final int numberOfPlayer = 2;
        registrationEventWrapper(numberOfPlayer);

        sendPlaceBid(0, BidType.FOOD, 1);
        bidPlaced(BidType.FOOD, 0, 1);
        assertActNow(0);
        //bid with wrong number of slot
        sendPlaceBid(0, BidType.FOOD, 0);
        assertActionFailed(0);
        assertActNow(0);
        sendPlaceBid(0, BidType.FOOD, 4);
        assertActionFailed(0);
        assertActNow(0);
        //bid the same option
        sendPlaceBid(0, BidType.FOOD, 2);
        assertActionFailed(0);
        assertActNow(0);
        //bid the same slot
        sendPlaceBid(0, BidType.NICENESS, 1);
        assertActionFailed(0);
        assertActNow(0);
        sendPlaceBid(0, BidType.NICENESS, 2);
        bidPlaced(BidType.NICENESS, 0, 2);
        assertActNow(0);
        sendPlaceBid(0, BidType.TUNNEL, 3);
        bidPlaced(BidType.TUNNEL, 0, 3);
        //bid more than 3 times
        sendPlaceBid(0, BidType.MONSTER, 3);
        assertActionFailed(0);
        leave();
    }
}
