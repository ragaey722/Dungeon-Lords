package de.unisaarland.cs.se.selab.systemtest.biddingphase;

import de.unisaarland.cs.se.selab.comm.TimeoutException;
import de.unisaarland.cs.se.selab.systemtest.HelperClass;

public class ActNowBroadcastAfterBiddingStarted extends HelperClass {

    public ActNowBroadcastAfterBiddingStarted() {
        super(ActNowBroadcastAfterBiddingStarted.class, false);
    }

    @Override
    public void run() throws TimeoutException {
        final int numberOfPlayer = 2;
        registrationEventWrapper(numberOfPlayer);
        leave();
    }
}
