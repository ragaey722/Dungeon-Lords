package de.unisaarland.cs.se.selab.systemtest.registration;

import de.unisaarland.cs.se.selab.comm.TimeoutException;
import de.unisaarland.cs.se.selab.systemtest.HelperClass;

public class RegistrationMaxPlayerTest extends HelperClass {

    public RegistrationMaxPlayerTest() {
        super(RegistrationMaxPlayerTest.class, false);
    }

    @Override
    public void run() throws TimeoutException {
        register(MAX_PLAYER);
        gameStarted();
        player();
        nextYear(1);
        nextRound(1);
        broadcaster(MAX_PLAYER); // Adventurer drawn
        broadcaster(3); // Monster drawn
        broadcaster(2); // Room drawn
        biddingStarted();
        actNowBroadcast();
        leave();
    }
}
