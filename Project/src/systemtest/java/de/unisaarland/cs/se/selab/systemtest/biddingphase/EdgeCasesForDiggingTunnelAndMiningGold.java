package de.unisaarland.cs.se.selab.systemtest.biddingphase;

import de.unisaarland.cs.se.selab.comm.BidType;
import de.unisaarland.cs.se.selab.comm.TimeoutException;
import de.unisaarland.cs.se.selab.systemtest.HelperClass;
import de.unisaarland.cs.se.selab.systemtest.api.Utils;
import de.unisaarland.cs.se.selab.systemtest.registration.RegistrationTest;
import java.util.List;

public class EdgeCasesForDiggingTunnelAndMiningGold extends HelperClass {

    public EdgeCasesForDiggingTunnelAndMiningGold() {
        super(EdgeCasesForDiggingTunnelAndMiningGold.class, false);
    }

    @Override
    public String createConfig() {
        return Utils.loadResource(RegistrationTest.class,
                "edgeCasesForCoinAndTunnelOptionConfiguration.json");
    }

    @Override
    public void run() throws TimeoutException {
        registrationEventWrapper(3);
        placeBid();
        //evaluate niceness option
        evilnessChanged(-1, 0);
        evilnessChanged(-2, 1);
        goldChanged(-1, 2);
        evilnessChanged(-2, 2);
        //evaluate tunnel option
        //first slot
        assertDigTunnel(2);
        assertActNow(2);
        //check handling already built coordinate tunnel
        sendDigTunnel(2, 0, 0);
        assertActionFailed(2);
        assertActNow(2);
        //check handling disconnected tunnel
        sendDigTunnel(2, 4, 0);
        assertActionFailed(2);
        assertActNow(2);
        //check handling non-current player action
        sendDigTunnel(1, 1, 0);
        assertActionFailed(1);
        //check handling illegal coordinate
        sendDigTunnel(2, -1, 0);
        assertActionFailed(2);
        assertActNow(2);
        sendDigTunnel(2, 5, 0);
        assertActionFailed(2);
        assertActNow(2);
        sendDigTunnel(2, 0, -1);
        assertActionFailed(2);
        assertActNow(2);
        sendDigTunnel(2, 0, 5);
        assertActionFailed(2);
        assertActNow(2);
        //player after current player leaves
        leave(0, 0);
        ////////////////////////////////////
        sendEndTurn(2);
        //second slot skipped because second player left
        //third slot
        assertDigTunnel(1);
        assertActNow(1);
        sendDigTunnel(1, 1, 0);
        impsChanged(-1, 1);
        tunnelDug(1, 1, 0);
        assertActNow(1);
        sendDigTunnel(1, 1, 1);
        impsChanged(-1, 1);
        tunnelDug(1, 1, 1);
        assertActNow(1);
        //check handling 2x2 tunnel
        sendDigTunnel(1, 0, 1);
        assertActionFailed(1);
        assertActNow(1);
        ///////////////////////////////////
        sendDigTunnel(1, 2, 1);
        impsChanged(-1, 1);
        tunnelDug(1, 2, 1);
        assertActNow(1);
        //insufficient amount of imps
        sendDigTunnel(1, 3, 1);
        assertActionFailed(1);
        assertActNow(1);
        leave(1, 1);
        //evaluate coin option
        //first and second player is skipped because they left
        impsChanged(-1, 2);
        //bid retrieved
        bidRetrieved(List.of(BidType.TUNNEL), 2);
        //imps returned and gold received
        impsChanged(1, 2);
        goldChanged(1, 2);
        //adventurer arrived
        assertSendEvents();
        //next round
        nextRound(2);
        broadcaster(1);
        broadcaster(3);
        broadcaster(2);
        biddingStarted();
        actNowBroadcast();
        leave();

    }

    private void placeBid() throws TimeoutException {
        //first player place bid
        sendPlaceBid(0, BidType.GOLD, 1);
        bidPlaced(BidType.GOLD, 0, 1);
        assertActNow(0);
        sendPlaceBid(0, BidType.TUNNEL, 2);
        bidPlaced(BidType.TUNNEL, 0, 2);
        assertActNow(0);
        sendPlaceBid(0, BidType.NICENESS, 3);
        bidPlaced(BidType.NICENESS, 0, 3);
        //second player place bid
        sendPlaceBid(1, BidType.GOLD, 1);
        bidPlaced(BidType.GOLD, 1, 1);
        assertActNow(1);
        sendPlaceBid(1, BidType.TUNNEL, 2);
        bidPlaced(BidType.TUNNEL, 1, 2);
        assertActNow(1);
        sendPlaceBid(1, BidType.NICENESS, 3);
        bidPlaced(BidType.NICENESS, 1, 3);
        //third player place bid
        sendPlaceBid(2, BidType.TUNNEL, 1);
        bidPlaced(BidType.TUNNEL, 2, 1);
        assertActNow(2);
        sendPlaceBid(2, BidType.GOLD, 2);
        bidPlaced(BidType.GOLD, 2, 2);
        assertActNow(2);
        sendPlaceBid(2, BidType.NICENESS, 3);
        bidPlaced(BidType.NICENESS, 2, 3);
    }
}
