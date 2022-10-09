package de.unisaarland.cs.se.selab.systemtest.registration;

import de.unisaarland.cs.se.selab.comm.TimeoutException;
import de.unisaarland.cs.se.selab.systemtest.HelperClass;

public class RegistrationStartGameTest extends HelperClass {

    public RegistrationStartGameTest() {
        super(RegistrationStartGameTest.class, false);
    }

    @Override
    public void run() throws TimeoutException {
        register(2);
        sendStartGame(0);
        gameStarted();
        player();
        nextYear(1);
        nextRound(1);
        broadcaster(2); // Adventurer drawn
        broadcaster(3); // Monster drawn
        broadcaster(2); // Room drawn
        biddingStarted();
        actNowBroadcast();
        leave();
    }
}
