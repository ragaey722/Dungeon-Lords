package de.unisaarland.cs.se.selab.systemtest.registration;

import de.unisaarland.cs.se.selab.comm.TimeoutException;
import de.unisaarland.cs.se.selab.systemtest.HelperClass;

public class RegisteredPlayerSendActivateRoomActionInRegistration extends HelperClass {


    public RegisteredPlayerSendActivateRoomActionInRegistration() {
        super(RegisteredPlayerSendRegistrationActionInRegistration.class, false);
    }

    @Override
    public void run() throws TimeoutException {
        register(2);
        sendActivateRoom(0, 0);
        assertActionFailed(0);
        leave();
    }
}
