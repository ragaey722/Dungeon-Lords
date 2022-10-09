package de.unisaarland.cs.se.selab.systemtest.registration;

import de.unisaarland.cs.se.selab.comm.BidType;
import de.unisaarland.cs.se.selab.comm.TimeoutException;
import de.unisaarland.cs.se.selab.systemtest.HelperClass;

public class RegisteredPlayerSendPlaceBidInRegistration extends HelperClass {


    public RegisteredPlayerSendPlaceBidInRegistration() {
        super(RegisteredPlayerSendPlaceBidInRegistration.class, false);
    }

    @Override
    public void run() throws TimeoutException {
        register(2);
        sendPlaceBid(0, BidType.FOOD, 1);
        assertActionFailed(0);
        leave();
    }
}
