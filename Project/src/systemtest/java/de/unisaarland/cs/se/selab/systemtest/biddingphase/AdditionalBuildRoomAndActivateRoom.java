package de.unisaarland.cs.se.selab.systemtest.biddingphase;

import de.unisaarland.cs.se.selab.comm.BidType;
import de.unisaarland.cs.se.selab.comm.TimeoutException;
import de.unisaarland.cs.se.selab.systemtest.api.Utils;
import de.unisaarland.cs.se.selab.systemtest.registration.RegistrationTest;
import java.util.List;

public class AdditionalBuildRoomAndActivateRoom extends BuildRoomAndActivateRoom {

    public AdditionalBuildRoomAndActivateRoom() {
        super(AdditionalBuildRoomAndActivateRoom.class, false);
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
        broadcaster(numberOfPlayer); //adventurer drawn
        broadcaster(3); //monster drawn
        broadcaster(2); //room drawn
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
        bidRetrieved(List.of(BidType.GOLD, BidType.TUNNEL, BidType.ROOM), 1);
        bidRetrieved(List.of(BidType.NICENESS, BidType.TUNNEL, BidType.ROOM), 2);
        //imps returned and gold received
        //first player
        impsChanged(2, 0);
        //adventurer arrived
        assertSendEvents();
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
        sendBuildRoom(1, 1, 1, 8);
        roomBuilt(1, 8, 1, 1);
        //second slot
        goldChanged(-1, 2);
        assertPlaceRoom(2);
        assertActNow(2);
        //check handling adjacent room
        sendBuildRoom(2, 0, 1, 15);
        assertActionFailed(2);
        assertActNow(2);
        //check handling room on top of room
        sendBuildRoom(2, 0, 0, 15);
        assertActionFailed(2);
        assertActNow(2);
        ////////////////////////////////////
        sendBuildRoom(2, 1, 1, 15);
        roomBuilt(2, 15, 1, 1);
        //third slot is skipped because there is no more room left
    }
}


