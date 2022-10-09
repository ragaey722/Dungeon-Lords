package de.unisaarland.cs.se.selab.systemtest.biddingphase;

import de.unisaarland.cs.se.selab.comm.BidType;
import de.unisaarland.cs.se.selab.comm.TimeoutException;
import de.unisaarland.cs.se.selab.systemtest.HelperClass;

public class AllPlayerHaveBidInAllSlotsOfDefaultOptions extends HelperClass {

    public AllPlayerHaveBidInAllSlotsOfDefaultOptions() {
        super(AllPlayerHaveBidInAllSlotsOfDefaultOptions.class, false);
    }

    @Override
    public void run() throws TimeoutException {
        final int numberOfPlayer = 3;
        registrationEventWrapper(numberOfPlayer);
        //first player bid
        placeBidSuccessful(BidType.FOOD, 1, 0);
        assertActNow(0);
        placeBidSuccessful(BidType.NICENESS, 2, 0);
        assertActNow(0);
        placeBidSuccessful(BidType.IMPS, 3, 0);
        //second player bid
        placeBidSuccessful(BidType.FOOD, 1, 1);
        assertActNow(1);
        placeBidSuccessful(BidType.NICENESS, 2, 1);
        assertActNow(1);
        placeBidSuccessful(BidType.IMPS, 3, 1);
        //third player bid
        placeBidSuccessful(BidType.FOOD, 1, 2);
        assertActNow(2);
        placeBidSuccessful(BidType.NICENESS, 2, 2);
        assertActNow(2);
        placeBidSuccessful(BidType.IMPS, 3, 2);

        //evaluate food option
        evaluateFood(1, 0);
        evaluateFood(2, 1);
        evaluateFood(3, 2);
        //evaluate niceness option
        evaluateEvilness(1, 0);
        evaluateEvilness(2, 1);
        evaluateEvilness(3, 2);
        //evaluate imps option
        evaluateImps(1, 0);
        evaluateImps(2, 1);
        evaluateImps(3, 2);
        /**
         player 0 : food = 4
                    evilness = 4
                    coin = 2
                    imps = 4
         player 1 : food = 4
                    evilness = 4
                    coin = 3
                    imps = 5
         player 2 : food = 5
                    evilness = 5
                    coin = 2
                    imps = 5
         */
        leave();
    }
}
