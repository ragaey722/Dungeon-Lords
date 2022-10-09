package de.unisaarland.cs.se.selab.systemtest.biddingphase;

import de.unisaarland.cs.se.selab.comm.BidType;
import de.unisaarland.cs.se.selab.comm.TimeoutException;
import de.unisaarland.cs.se.selab.systemtest.HelperClass;
import de.unisaarland.cs.se.selab.systemtest.api.Utils;
import de.unisaarland.cs.se.selab.systemtest.registration.RegistrationTest;

public class InsufficientEvilnessForMonster extends HelperClass {

    public InsufficientEvilnessForMonster() {
        super(InsufficientEvilnessForMonster.class, false);
    }

    @Override
    public String createConfig() {
        return Utils.loadResource(RegistrationTest.class,
                "insufficientResourcesConfiguration.json");
    }

    @Override
    public void run() throws TimeoutException {
        final int numberOfPlayer = 2;
        registrationEventWrapper(numberOfPlayer);
        placingBid();
        evalBid();
        leave();


    }

    void placingBid() throws TimeoutException {
        sendPlaceBid(0, BidType.GOLD, 1);
        bidPlaced(BidType.GOLD, 0, 1);
        assertActNow(0);

        sendPlaceBid(1, BidType.GOLD, 1);
        bidPlaced(BidType.GOLD, 1, 1);
        assertActNow(1);

        sendPlaceBid(0, BidType.TUNNEL, 2);
        bidPlaced(BidType.TUNNEL, 0, 2);
        assertActNow(0);

        sendPlaceBid(1, BidType.TUNNEL, 2);
        bidPlaced(BidType.TUNNEL, 1, 2);
        assertActNow(1);

        sendPlaceBid(0, BidType.MONSTER, 3);
        bidPlaced(BidType.MONSTER, 0, 3);

        sendPlaceBid(1, BidType.MONSTER, 3);
        bidPlaced(BidType.MONSTER, 1, 3);
    }

    void evalBid() throws TimeoutException {
        //tunnel
        assertDigTunnel(0);
        assertActNow(0);
        sendDigTunnel(0, 0, 1);
        impsChanged(-1, 0);
        tunnelDug(0, 0, 1);

        assertActNow(0);
        sendDigTunnel(0, 0, 2);
        assertActionFailed(0);
        assertActNow(0);
        sendEndTurn(0);

        assertDigTunnel(1);
        assertActNow(1);
        sendDigTunnel(1, 0, 1);
        impsChanged(-1, 1);
        tunnelDug(1, 0, 1);

        assertActNow(1);
        sendDigTunnel(1, 0, 2);
        assertActionFailed(1);
        assertActNow(1);
        sendEndTurn(1);

        //monster
        assertSelectMonster(0);
        assertActNow(0);
        sendHireMonster(0, 10);
        assertActionFailed(0);
        assertActNow(0);
        sendEndTurn(0);

        assertSelectMonster(1);
        assertActNow(1);
        sendHireMonster(1, 10);
        assertActionFailed(1);
        assertActNow(1);
        sendEndTurn(1);
    }

}
