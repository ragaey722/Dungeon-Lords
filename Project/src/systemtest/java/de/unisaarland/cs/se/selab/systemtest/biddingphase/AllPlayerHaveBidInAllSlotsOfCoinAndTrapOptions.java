package de.unisaarland.cs.se.selab.systemtest.biddingphase;

import de.unisaarland.cs.se.selab.comm.BidType;
import de.unisaarland.cs.se.selab.comm.TimeoutException;
import de.unisaarland.cs.se.selab.systemtest.HelperClass;
import java.util.List;

public class AllPlayerHaveBidInAllSlotsOfCoinAndTrapOptions extends HelperClass {


    public AllPlayerHaveBidInAllSlotsOfCoinAndTrapOptions() {
        super(AllPlayerHaveBidInAllSlotsOfCoinAndTrapOptions.class, false);
    }

    @Override
    public void run() throws TimeoutException {
        final int numberOfPlayer = 3;
        registrationEventWrapper(numberOfPlayer);
        //placing all the bids
        placeBidFirstSeason();
        //evaluate all the bids
        evaluateFirstSeason();
        //Bid retrieved
        bidRetrieved(List.of(BidType.FOOD), 0);
        bidRetrieved(List.of(BidType.IMPS), 1);
        bidRetrieved(List.of(BidType.FOOD), 2);
        //change the order of the player to be evaluated : 1 -> 2 -> 0
        //adventurer arrived
        adventurerArrived(2, 0);
        adventurerArrived(29, 2);
        adventurerArrived(23, 1);
        /**
         * YEAR : 1
         * SEASON : 1
         player 0 : food = 3
                 evilness = 4
                 coin = 2
                 imps = 5
         player 1 : food = 4
                 evilness = 5
                 coin = 4
                 imps = 4
         player 2 : food = 5
                 evilness = 4
                 coin = 1
                 imps = 5
         */
        //next round
        nextRound(2);
        broadcaster(numberOfPlayer); //AdventurerDrawn
        broadcaster(3); //MonsterDrawn
        broadcaster(2); //RoomDrawn
        biddingStarted();
        actNowBroadcast();
        //placing all the bids
        placeBidSecondSeason();
        //evaluate all the bids
        evaluateSecondSeason();
        //bid retrieved
        bidRetrieved(List.of(BidType.NICENESS, BidType.IMPS, BidType.FOOD), 0);
        bidRetrieved(List.of(BidType.NICENESS, BidType.FOOD, BidType.IMPS), 1);
        bidRetrieved(List.of(BidType.NICENESS, BidType.IMPS, BidType.FOOD), 2);
        //imps return and receive gold
        impsChanged(1, 0);
        goldChanged(1, 0);
        impsChanged(1, 1);
        goldChanged(1, 1);
        impsChanged(1, 2);
        goldChanged(1, 2);
        //change the order of the player to be evaluated : 2 -> 0 -> 1
        //adventurer arrived
        /**
         * YEAR : 1
         * SEASON : 2
         player 0 : food = 6
                 evilness = 5
                 coin = 4
                 imps = 5
         player 1 : food = 3
                 evilness = 5
                 coin = 5
                 imps = 5
         player 2 : food = 7
                 evilness = 4
                 coin = 3
                 imps = 5
         */
        leave();
    }

    private void placeBidFirstSeason() throws TimeoutException {
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
        sendPlaceBid(1, BidType.FOOD, 3);
        bidPlaced(BidType.FOOD, 1, 3);
        assertActNow(1);
        sendPlaceBid(1, BidType.NICENESS, 2);
        bidPlaced(BidType.NICENESS, 1, 2);
        assertActNow(1);
        sendPlaceBid(1, BidType.IMPS, 1);
        bidPlaced(BidType.IMPS, 1, 1);
        //third player bid
        sendPlaceBid(2, BidType.FOOD, 1);
        bidPlaced(BidType.FOOD, 2, 1);
        assertActNow(2);
        sendPlaceBid(2, BidType.NICENESS, 2);
        bidPlaced(BidType.NICENESS, 2, 2);
        assertActNow(2);
        sendPlaceBid(2, BidType.IMPS, 3);
        bidPlaced(BidType.IMPS, 2, 3);
    }

    private void evaluateFirstSeason() throws TimeoutException {
        //evaluate food option
        goldChanged(-1, 0);
        foodChanged(2, 0);
        evilnessChanged(1, 2);
        foodChanged(3, 2);
        evilnessChanged(2, 1);
        foodChanged(3, 1);
        goldChanged(1, 1);
        //evaluate niceness option
        evilnessChanged(-1, 0);
        evilnessChanged(-2, 1);
        goldChanged(-1, 2);
        evilnessChanged(-2, 2);
        //evaluate imps option
        foodChanged(-1, 1);
        impsChanged(1, 1);
        foodChanged(-2, 0);
        impsChanged(2, 0);
        foodChanged(-1, 2);
        goldChanged(-1, 2);
        impsChanged(2, 2);
    }

    private void placeBidSecondSeason() throws TimeoutException {
        //first player bid
        sendPlaceBid(0, BidType.FOOD, 1);
        bidPlaced(BidType.FOOD, 0, 1);
        assertActNow(0);
        sendPlaceBid(0, BidType.GOLD, 2);
        bidPlaced(BidType.GOLD, 0, 2);
        assertActNow(0);
        sendPlaceBid(0, BidType.TRAP, 3);
        bidPlaced(BidType.TRAP, 0, 3);
        //second player bid
        sendPlaceBid(1, BidType.IMPS, 1);
        bidPlaced(BidType.IMPS, 1, 1);
        assertActNow(1);
        sendPlaceBid(1, BidType.GOLD, 2);
        bidPlaced(BidType.GOLD, 1, 2);
        assertActNow(1);
        sendPlaceBid(1, BidType.TRAP, 3);
        bidPlaced(BidType.TRAP, 1, 3);
        //third player bid
        sendPlaceBid(2, BidType.FOOD, 1);
        bidPlaced(BidType.FOOD, 2, 1);
        assertActNow(2);
        sendPlaceBid(2, BidType.GOLD, 2);
        bidPlaced(BidType.GOLD, 2, 2);
        assertActNow(2);
        sendPlaceBid(2, BidType.TRAP, 3);
        bidPlaced(BidType.TRAP, 2, 3);
    }

    private void evaluateSecondSeason() throws TimeoutException {
        //evaluate food option
        goldChanged(-1, 2);
        foodChanged(2, 2);
        evilnessChanged(1, 0);
        foodChanged(3, 0);
        //evaluate coin option
        impsChanged(-1, 1);
        impsChanged(-1, 2);
        impsChanged(-1, 0);
        //evaluate imps option
        foodChanged(-1, 1);
        impsChanged(1, 1);
        //evaluate trap option
        evaluateTraps(1, 1);
        evaluateTraps(2, 2);
        evaluateTraps(3, 0);
    }

}
