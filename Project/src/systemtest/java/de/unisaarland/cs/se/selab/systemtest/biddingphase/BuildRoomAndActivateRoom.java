package de.unisaarland.cs.se.selab.systemtest.biddingphase;

import de.unisaarland.cs.se.selab.comm.BidType;
import de.unisaarland.cs.se.selab.comm.TimeoutException;
import de.unisaarland.cs.se.selab.systemtest.HelperClass;
import de.unisaarland.cs.se.selab.systemtest.api.Utils;
import de.unisaarland.cs.se.selab.systemtest.registration.RegistrationTest;
import java.util.List;

public class BuildRoomAndActivateRoom extends HelperClass {


    public BuildRoomAndActivateRoom() {
        super(BuildRoomAndActivateRoom.class, false);
    }

    protected BuildRoomAndActivateRoom(final Class<?> subclass, final boolean bool) {
        super(subclass, bool);
    }

    @Override
    public String createConfig() {
        return Utils.loadResource(RegistrationTest.class,
                "edgeCasesForCoinAndTunnelOptionConfiguration.json");
    }

    @Override
    public void run() throws TimeoutException {
        int numberOfPlayer = 3;
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
        broadcaster(numberOfPlayer); //adventurer drawn
        broadcaster(3); //monster drawn
        broadcaster(2); //room drawn
        biddingStarted();
        actNowBroadcast();
        /**
         * player 0 : food = 3, evilness = 5, coin = 4, imps = 5
         * player 1 : food = 4, evilness = 5, coin = 5, imps = 4
         * player 2 : food = 4, evilness = 4, coin = 4, imps = 4
         */
        placeBidSecondSeason();
        evaluateSecondSeason();
        //one player left
        numberOfPlayer--;
        //bid retrieved
        bidRetrieved(List.of(BidType.GOLD, BidType.IMPS, BidType.ROOM), 0);
        bidRetrieved(List.of(BidType.GOLD, BidType.TUNNEL, BidType.ROOM), 1);
        //imps returned and gold received
        //first player
        impsChanged(2, 0);
        //imps returned and receives rewards from room
        //roomID 4
        impsChanged(2, 1);
        evilnessChanged(-1, 1);
        //roomID 8
        impsChanged(2, 1);
        foodChanged(1, 1);
        goldChanged(1, 1);
        impsChanged(1, 1);
        //adventurer arrived
        assertSendEvents();
        assertSendEvents();
        //next round
        nextRound(3);
        broadcaster(numberOfPlayer);
        broadcaster(3);
        broadcaster(2);
        biddingStarted();
        actNowBroadcast();
        leave();
    }

    protected void placeBidFirstSeason() throws TimeoutException {
        //first player place bid
        sendPlaceBid(0, BidType.ROOM, 1);
        bidPlaced(BidType.ROOM, 0, 1);
        assertActNow(0);
        sendPlaceBid(0, BidType.GOLD, 2);
        bidPlaced(BidType.GOLD, 0, 2);
        assertActNow(0);
        sendPlaceBid(0, BidType.IMPS, 3);
        bidPlaced(BidType.IMPS, 0, 3);
        //second player place bid
        sendPlaceBid(1, BidType.ROOM, 1);
        bidPlaced(BidType.ROOM, 1, 1);
        assertActNow(1);
        sendPlaceBid(1, BidType.GOLD, 2);
        bidPlaced(BidType.GOLD, 1, 2);
        assertActNow(1);
        sendPlaceBid(1, BidType.TUNNEL, 3);
        bidPlaced(BidType.TUNNEL, 1, 3);
        //third player place bid
        sendPlaceBid(2, BidType.ROOM, 1);
        bidPlaced(BidType.ROOM, 2, 1);
        assertActNow(2);
        sendPlaceBid(2, BidType.NICENESS, 2);
        bidPlaced(BidType.NICENESS, 2, 2);
        assertActNow(2);
        sendPlaceBid(2, BidType.TUNNEL, 3);
        bidPlaced(BidType.TUNNEL, 2, 3);
    }

    protected void evaluateFirstSeason() throws TimeoutException {
        //evaluate niceness
        evilnessChanged(-1, 2);
        //evaluate tunnel
        //first slot
        assertDigTunnel(1);
        assertActNow(1);
        sendDigTunnel(1, 1, 0);
        impsChanged(-1, 1);
        tunnelDug(1, 1, 0);
        assertActNow(1);
        sendDigTunnel(1, 1, 1);
        impsChanged(-1, 1);
        tunnelDug(1, 1, 1);
        //second slot
        assertDigTunnel(2);
        assertActNow(2);
        sendDigTunnel(2, 0, 1);
        impsChanged(-1, 2);
        tunnelDug(2, 0, 1);
        assertActNow(2);
        sendDigTunnel(2, 1, 1);
        impsChanged(-1, 2);
        tunnelDug(2, 1, 1);
        assertActNow(2);
        sendEndTurn(2);
        //evaluate gold
        impsChanged(-1, 0);
        impsChanged(-2, 1);
        //evaluate imps
        foodChanged(-1, 0);
        impsChanged(1, 0);
        //evaluate room
        //slot 1
        goldChanged(-1, 0);
        assertPlaceRoom(0);
        assertActNow(0);
        sendEndTurn(0);
        //slot 2
        goldChanged(-1, 1);
        assertPlaceRoom(1);
        assertActNow(1);
        //check handling roomID not in list
        sendBuildRoom(1, 0, 0, 10);
        assertActionFailed(1);
        assertActNow(1);
        //check handling illegal coordinate
        sendBuildRoom(1, 4, 0, 4);
        assertActionFailed(1);
        assertActNow(1);
        //check handling non-current player action
        sendBuildRoom(2, 0, 0, 4);
        assertActionFailed(2);
        //check handling inner ring coordinate when room is outer ring
        sendBuildRoom(1, 1, 1, 4);
        assertActionFailed(1);
        assertActNow(1);
        //////////////////////////////////////////
        sendBuildRoom(1, 0, 0, 4);
        roomBuilt(1, 4, 0, 0);
        //slot 3
        assertPlaceRoom(2);
        assertActNow(2);
        //check handling activating room with insufficient amount of available imps
        sendActivateRoom(1, 4);
        assertActionFailed(1);
        //check handling already taken room
        sendBuildRoom(2, 0, 0, 4);
        assertActionFailed(2);
        assertActNow(2);
        //check handling lower half coordinate when room is upper half
        sendBuildRoom(2, 0, 2, 5);
        assertActionFailed(2);
        assertActNow(2);
        //check handling current player activating non-possessed room
        sendActivateRoom(2, 5);
        assertActionFailed(2);
        assertActNow(2);
        ///////////////////////////////////////////
        sendBuildRoom(2, 0, 0, 5);
        roomBuilt(2, 5, 0, 0);
    }

    protected void placeBidSecondSeason() throws TimeoutException {
        //first player place bid
        sendPlaceBid(0, BidType.ROOM, 1);
        bidPlaced(BidType.ROOM, 0, 1);
        assertActNow(0);
        sendPlaceBid(0, BidType.TUNNEL, 2);
        bidPlaced(BidType.TUNNEL, 0, 2);
        assertActNow(0);
        sendPlaceBid(0, BidType.FOOD, 3);
        bidPlaced(BidType.FOOD, 0, 3);
        //second player place bid
        sendPlaceBid(1, BidType.ROOM, 1);
        bidPlaced(BidType.ROOM, 1, 1);
        assertActNow(1);
        sendPlaceBid(1, BidType.FOOD, 2);
        bidPlaced(BidType.FOOD, 1, 2);
        assertActNow(1);
        sendPlaceBid(1, BidType.NICENESS, 3);
        bidPlaced(BidType.NICENESS, 1, 3);
        //third player place bid
        sendPlaceBid(2, BidType.ROOM, 1);
        bidPlaced(BidType.ROOM, 2, 1);
        assertActNow(2);
        sendPlaceBid(2, BidType.FOOD, 2);
        bidPlaced(BidType.FOOD, 2, 2);
        assertActNow(2);
        sendPlaceBid(2, BidType.IMPS, 3);
        bidPlaced(BidType.IMPS, 2, 3);
    }

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
        sendDigTunnel(0, 0, 1);
        impsChanged(-1, 0);
        tunnelDug(0, 0, 1);
        assertActNow(0);
        sendDigTunnel(0, 0, 2);
        impsChanged(-1, 0);
        tunnelDug(0, 0, 2);
        //evaluate imps
        foodChanged(-1, 2);
        impsChanged(1, 2);
        //evaluate room
        //first slot
        goldChanged(-1, 1);
        assertPlaceRoom(1);
        assertActNow(1);
        //check handling player after current player leaves
        leave(2, 2);
        //check handling current player activate room
        sendActivateRoom(1, 4);
        impsChanged(-2, 1);
        roomActivated(1, 4);
        assertActNow(1);
        //check handling current player activate room that is active
        sendActivateRoom(1, 4);
        assertActionFailed(1);
        assertActNow(1);
        //check handling outer ring coordinate when room is inner ring
        sendBuildRoom(1, 0, 0, 8);
        assertActionFailed(1);
        assertActNow(1);
        /////////////////////////////////////
        sendBuildRoom(1, 1, 1, 8);
        roomBuilt(1, 8, 1, 1);
        //second slot skipped because player left
        //third slot
        assertPlaceRoom(0);
        assertActNow(0);
        //check handling upper half coordinate when room is lower half
        sendBuildRoom(0, 0, 0, 15);
        assertActionFailed(0);
        assertActNow(0);
        //check handling non-current player activate room
        sendActivateRoom(1, 8);
        impsChanged(-2, 1);
        roomActivated(1, 8);
        //////////////////////////////////////
        sendBuildRoom(0, 0, 2, 15);
        roomBuilt(0, 15, 0, 2);
    }
}
