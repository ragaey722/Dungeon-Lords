package de.unisaarland.cs.se.selab.systemtest.registration;

import de.unisaarland.cs.se.selab.comm.TimeoutException;
import de.unisaarland.cs.se.selab.systemtest.HelperClass;

public class RegistrationLastPlayerIDTest extends HelperClass {

    public RegistrationLastPlayerIDTest() {
        super(RegistrationLastPlayerIDTest.class, false);
    }

    @Override
    public void run() throws TimeoutException {
        final int numberOfPlayer = 2;
        register(numberOfPlayer);
        leave(0, 0);
        register(0, 2);
        leave(0, 2);
        register(0, 3);
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
