package de.unisaarland.cs.se.selab.systemtest.biddingphase;

import de.unisaarland.cs.se.selab.comm.BidType;
import de.unisaarland.cs.se.selab.comm.TimeoutException;
import de.unisaarland.cs.se.selab.systemtest.HelperClass;

public class SecondPlayerLeftWhenFirstPlayerHaveFinishedBidding extends HelperClass {


    public SecondPlayerLeftWhenFirstPlayerHaveFinishedBidding() {
        super(SecondPlayerLeftWhenFirstPlayerHaveFinishedBidding.class, false);
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
        sendPlaceBid(0, BidType.IMPS, 3);
        bidPlaced(BidType.IMPS, 0, 3);

        //second player bid twice before leaving
        sendPlaceBid(1, BidType.FOOD, 1);
        bidPlaced(BidType.FOOD, 1, 1);
        assertActNow(1);
        sendPlaceBid(1, BidType.NICENESS, 2);
        bidPlaced(BidType.NICENESS, 1, 2);
        assertActNow(1);
        leave(1, 1);

        goldChanged(-1, 0);
        foodChanged(2, 0);
        evilnessChanged(-1, 0);
        foodChanged(-1, 0);
        impsChanged(1, 0);
        leave();
    }
}
