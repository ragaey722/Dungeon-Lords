package de.unisaarland.cs.se.selab.systemtest.registration;

import de.unisaarland.cs.se.selab.comm.TimeoutException;
import de.unisaarland.cs.se.selab.systemtest.HelperClass;

public class UnregisteredPlayerSendStartGameInRegistration extends HelperClass {


    public UnregisteredPlayerSendStartGameInRegistration() {
        super(UnregisteredPlayerSendStartGameInRegistration.class, false);
    }

    @Override
    public void run() throws TimeoutException {
        register(2);
        //unregistered sockets
        sendStartGame(2);
        sendStartGame(3);
        sendStartGame(2);
        //registered socket
        sendStartGame(1);
        gameStarted();
        player();
        nextYear(1);
        nextRound(1);
        drawAll(2);
        biddingStarted();
        actNowBroadcast();
        leave();
    }
}
