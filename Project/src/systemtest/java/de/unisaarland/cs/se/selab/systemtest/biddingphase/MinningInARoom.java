package de.unisaarland.cs.se.selab.systemtest.biddingphase;

import de.unisaarland.cs.se.selab.comm.BidType;
import de.unisaarland.cs.se.selab.comm.TimeoutException;
import de.unisaarland.cs.se.selab.systemtest.api.Utils;
import de.unisaarland.cs.se.selab.systemtest.registration.RegistrationTest;
import java.util.List;

public class MinningInARoom extends BuildRoomAndActivateRoom {

    public MinningInARoom() {
        super(MinningInARoom.class, false);
    }

    @Override
    public String createConfig() {
        return Utils.loadResource(RegistrationTest.class,
                "additionalBuildRoomAndActivateRoomConfiguration.json");
    }

    @Override
    public void run() throws TimeoutException {
        final int numberOfPlayer = 3;
        registrationEventWrapper(numberOfPlayer);
        placeBidFirstSeason();
        evaluateFirstSeason();
        //Bid retrieved
        bidRetrieved(List.of(BidType.ROOM), 0);
        bidRetrieved(List.of(BidType.ROOM), 1);
        bidRetrieved(List.of(BidType.ROOM), 2);
        //change order of evaluation of player : 1 -> 2 -> 0
        //imps returned and gold received
        //first player
        impsChanged(1, 0);
        goldChanged(1, 0);
        //second player
        impsChanged(4, 1);
        goldChanged(2, 1);
        //third player
        impsChanged(2, 2);
        //check handling all adventurer has the same difficulty
        //NOTE: ALL THE ADVENTURERS HAS THE SAME DIFFICULTY
        adventurerArrived(2, 2);
        adventurerArrived(23, 0);
        adventurerArrived(29, 1);
        //next round
        nextRound(2);
        drawAll(3);
        biddingStarted();
        actNowBroadcast();
        /**
         * player 0 : food = 3, evilness = 5, coin = 5, imps = 5
         * player 1 : food = 4, evilness = 5, coin = 5, imps = 4
         * player 2 : food = 4, evilness = 4, coin = 4, imps = 4
         */
        placeBidSecondSeason();
        evaluateSecondSeason();
        //bid retrieved
        bidRetrieved(List.of(BidType.GOLD, BidType.IMPS, BidType.ROOM), 0);
        //imps returned and gold received skipped
        //adventurer arrived
        assertSendEvents();
        //next round
        nextRound(3);
        drawAll(1);
        biddingStarted();
        actNowBroadcast();
        /**
         * player 0 : food = 6, evilness = 7, coin = 5, imps = 5
         */
        //first player place bid
        sendPlaceBid(0, BidType.GOLD, 1);
        bidPlaced(BidType.GOLD, 0, 1);
        assertActNow(0);
        sendPlaceBid(0, BidType.IMPS, 2);
        bidPlaced(BidType.IMPS, 0, 2);
        assertActNow(0);
        sendPlaceBid(0, BidType.ROOM, 3);
        bidPlaced(BidType.ROOM, 0, 3);
        //evaluate gold
        //skipped because there are no legal tunnel
        //evaluate imps
        foodChanged(-1, 0);
        impsChanged(1, 0);
        //evaluate room
        goldChanged(-1, 0);
        assertPlaceRoom(0);
        assertActNow(0);
        sendEndTurn(0);
        //bid retrieved
        bidRetrieved(List.of(BidType.TUNNEL, BidType.FOOD, BidType.GOLD), 0);
        //adventurerArrived
        assertSendEvents();
        //next round
        nextRound(4);
        //draw all
        drawAllMinusAdventurers();
        //bidding started
        biddingStarted();
        actNowBroadcast();
        leave();
    }

    @Override
    protected void evaluateSecondSeason() throws TimeoutException {
        //evaluate food
        goldChanged(-1, 1);
        foodChanged(2, 1);
        evilnessChanged(1, 2);
        foodChanged(3, 2);
        evilnessChanged(2, 0);
        foodChanged(3, 0);
        goldChanged(1, 0);
        //evaluate niceness
        evilnessChanged(-1, 1);
        //evaluate tunnel
        //only first slot
        assertDigTunnel(0);
        assertActNow(0);
        leave(1, 1);
        leave(2, 2);
        sendEndTurn(0);
        //evaluate room
        //skip first slot and second slot
        assertPlaceRoom(0);
        assertActNow(0);
        sendBuildRoom(0, 0, 0, 15);
        roomBuilt(0, 15, 0, 0);
    }
}
