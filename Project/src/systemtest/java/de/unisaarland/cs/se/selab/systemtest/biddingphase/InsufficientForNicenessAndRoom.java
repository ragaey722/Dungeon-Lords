package de.unisaarland.cs.se.selab.systemtest.biddingphase;

import de.unisaarland.cs.se.selab.comm.BidType;
import de.unisaarland.cs.se.selab.comm.TimeoutException;
import de.unisaarland.cs.se.selab.systemtest.HelperClass;
import de.unisaarland.cs.se.selab.systemtest.RegistrationTest;
import de.unisaarland.cs.se.selab.systemtest.api.Utils;
import java.util.List;

public class InsufficientForNicenessAndRoom extends HelperClass {

    public InsufficientForNicenessAndRoom() {
        super(InsufficientForNicenessAndRoom.class, false);
    }

    @Override
    public String createConfig() {
        return Utils.loadResource(RegistrationTest.class,
                "insufficientResourcesForNicenessAndRoom.json");
    }

    @Override
    public void run() throws TimeoutException {
        final int numberOfPlayer = 3;
        registrationEventWrapper(numberOfPlayer);
        placeBidFirstSeason();
        //evaluate food
        goldChanged(-1, 2);
        foodChanged(2, 2);
        //evaluate niceness
        evilnessChanged(-1, 0);
        //evaluate imps
        foodChanged(-1, 0);
        impsChanged(1, 0);
        //evaluate room
        goldChanged(-1, 0);
        assertPlaceRoom(0);
        assertActNow(0);
        sendEndTurn(0);
        assertPlaceRoom(1);
        assertActNow(1);
        sendEndTurn(1);
        //bid retrieved
        bidRetrieved(List.of(BidType.NICENESS), 0);
        bidRetrieved(List.of(BidType.NICENESS), 1);
        bidRetrieved(List.of(BidType.NICENESS), 2);
        //adventure arrived
        assertSendEvents();
        assertSendEvents();
        assertSendEvents();
        /**
         * player 0 : food = 0, evilness = 0, coins = 0, imps = 2
         * player 1 : food = 1, evilness = 1, coins = 1, imps = 1
         * player 2 : food = 3, evilness = 1, coins = 0, imps = 1
         */
        //next round
        nextRound(2);
        //draw adventurer, monster, room
        drawAll(3);
        //bidding started
        biddingStarted();
        actNowBroadcast();
        placeBidSecondSeason();
        //evaluate food
        goldChanged(-1, 1);
        foodChanged(2, 1);
        evilnessChanged(1, 0);
        foodChanged(3, 0);
        //evaluate niceness
        evilnessChanged(-1, 1);
        //evaluate Tunnel
        assertDigTunnel(1);
        assertActNow(1);
        leave(0, 0);
        leave(2, 2);
        sendEndTurn(1);
        //bid retrieved
        bidRetrieved(List.of(BidType.IMPS, BidType.ROOM, BidType.TUNNEL), 1);
        //NOTE : no imps returned because the player 1 sends end turn immediately
        //adventure arrived
        assertSendEvents();
        //next round
        nextRound(3);
        //draw adventurer, monster, room
        drawAll(1);
        //bidding started
        biddingStarted();
        actNowBroadcast();
        leave();
    }

    private void placeBidFirstSeason() throws TimeoutException {
        //first player place bid
        sendPlaceBid(0, BidType.NICENESS, 1);
        bidPlaced(BidType.NICENESS, 0, 1);
        assertActNow(0);
        sendPlaceBid(0, BidType.IMPS, 2);
        bidPlaced(BidType.IMPS, 0, 2);
        assertActNow(0);
        sendPlaceBid(0, BidType.ROOM, 3);
        bidPlaced(BidType.ROOM, 0, 3);
        //second player place bid
        sendPlaceBid(1, BidType.NICENESS, 1);
        bidPlaced(BidType.NICENESS, 1, 1);
        assertActNow(1);
        sendPlaceBid(1, BidType.IMPS, 2);
        bidPlaced(BidType.IMPS, 1, 2);
        assertActNow(1);
        sendPlaceBid(1, BidType.ROOM, 3);
        bidPlaced(BidType.ROOM, 1, 3);
        //third player place bid
        sendPlaceBid(2, BidType.NICENESS, 1);
        bidPlaced(BidType.NICENESS, 2, 1);
        assertActNow(2);
        sendPlaceBid(2, BidType.ROOM, 2);
        bidPlaced(BidType.ROOM, 2, 2);
        assertActNow(2);
        sendPlaceBid(2, BidType.FOOD, 3);
        bidPlaced(BidType.FOOD, 2, 3);
    }

    private void placeBidSecondSeason() throws TimeoutException {
        //first player place bid
        sendPlaceBid(0, BidType.TUNNEL, 1);
        bidPlaced(BidType.TUNNEL, 0, 1);
        assertActNow(0);
        sendPlaceBid(0, BidType.FOOD, 2);
        bidPlaced(BidType.FOOD, 0, 2);
        assertActNow(0);
        sendPlaceBid(0, BidType.MONSTER, 3);
        bidPlaced(BidType.MONSTER, 0, 3);
        //second player place bid
        sendPlaceBid(1, BidType.TUNNEL, 1);
        bidPlaced(BidType.TUNNEL, 1, 1);
        assertActNow(1);
        sendPlaceBid(1, BidType.FOOD, 2);
        bidPlaced(BidType.FOOD, 1, 2);
        assertActNow(1);
        sendPlaceBid(1, BidType.NICENESS, 3);
        bidPlaced(BidType.NICENESS, 1, 3);
        //third player place bid
        sendPlaceBid(2, BidType.TUNNEL, 1);
        bidPlaced(BidType.TUNNEL, 2, 1);
        assertActNow(2);
        sendPlaceBid(2, BidType.IMPS, 2);
        bidPlaced(BidType.IMPS, 2, 2);
        assertActNow(2);
        sendPlaceBid(2, BidType.MONSTER, 3);
        bidPlaced(BidType.MONSTER, 2, 3);
    }
}
