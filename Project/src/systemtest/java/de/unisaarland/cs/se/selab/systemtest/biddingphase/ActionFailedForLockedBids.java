package de.unisaarland.cs.se.selab.systemtest.biddingphase;

import de.unisaarland.cs.se.selab.comm.BidType;
import de.unisaarland.cs.se.selab.comm.TimeoutException;
import de.unisaarland.cs.se.selab.systemtest.HelperClass;
import java.util.List;

public class ActionFailedForLockedBids extends HelperClass {

    public ActionFailedForLockedBids() {
        super(ActionFailedForLockedBids.class, false);
    }

    @Override
    public void run() throws TimeoutException {
        final int numberOfPlayer = 3;
        registrationEventWrapper(numberOfPlayer);
        placeBidAndEvaluate();
        //Bid retrieved
        bidRetrieved(List.of(BidType.FOOD), 0);
        bidRetrieved(List.of(BidType.FOOD), 1);
        bidRetrieved(List.of(BidType.FOOD), 2);
        //change the order of the player to be evaluated : 1 -> 2 -> 0
        //adventurer arrived
        assertSendEvents();
        assertSendEvents();
        assertSendEvents();
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
        //next round
        nextRound(2);
        broadcaster(numberOfPlayer); //AdventurerDrawn
        broadcaster(3); //MonsterDrawn
        broadcaster(2); //RoomDrawn
        biddingStarted();
        actNowBroadcast();
        sendPlaceBid(0, BidType.NICENESS, 1);
        assertActionFailed(0);
        assertActNow(0);
        sendPlaceBid(0, BidType.IMPS, 2);
        assertActionFailed(0);
        assertActNow(0);
        sendPlaceBid(1, BidType.NICENESS, 3);
        assertActionFailed(1);
        assertActNow(1);
        sendPlaceBid(1, BidType.IMPS, 2);
        assertActionFailed(1);
        assertActNow(1);
        sendPlaceBid(2, BidType.NICENESS, 1);
        assertActionFailed(2);
        assertActNow(2);
        sendPlaceBid(2, BidType.IMPS, 3);
        assertActionFailed(2);
        assertActNow(2);
        leave();
    }

    private void placeBidAndEvaluate() throws TimeoutException {
        //first player bid
        sendPlaceBid(0, BidType.FOOD, 1);
        bidPlaced(BidType.FOOD, 0, 1);
        assertActNow(0);
        sendPlaceBid(0, BidType.NICENESS, 2);
        bidPlaced(BidType.NICENESS, 0, 2);
        assertActNow(0);
        sendPlaceBid(0, BidType.IMPS, 3);
        bidPlaced(BidType.IMPS, 0, 3);

        //second player bid
        sendPlaceBid(1, BidType.FOOD, 1);
        bidPlaced(BidType.FOOD, 1, 1);
        assertActNow(1);
        sendPlaceBid(1, BidType.NICENESS, 2);
        bidPlaced(BidType.NICENESS, 1, 2);
        assertActNow(1);
        sendPlaceBid(1, BidType.IMPS, 3);
        bidPlaced(BidType.IMPS, 1, 3);

        //third player bid
        sendPlaceBid(2, BidType.FOOD, 1);
        bidPlaced(BidType.FOOD, 2, 1);
        assertActNow(2);
        sendPlaceBid(2, BidType.NICENESS, 2);
        bidPlaced(BidType.NICENESS, 2, 2);
        assertActNow(2);
        sendPlaceBid(2, BidType.IMPS, 3);
        bidPlaced(BidType.IMPS, 2, 3);
        //evaluate food option
        goldChanged(-1, 0);
        foodChanged(2, 0);
        evilnessChanged(1, 1);
        foodChanged(3, 1);
        evilnessChanged(2, 2);
        foodChanged(3, 2);
        goldChanged(1, 2);
        //evaluate niceness option
        evilnessChanged(-1, 0);
        evilnessChanged(-2, 1);
        goldChanged(-1, 2);
        evilnessChanged(-2, 2);
        //evaluate imps option
        foodChanged(-1, 0);
        impsChanged(1, 0);
        foodChanged(-2, 1);
        impsChanged(2, 1);
        foodChanged(-1, 2);
        goldChanged(-1, 2);
        impsChanged(2, 2);
    }
}
