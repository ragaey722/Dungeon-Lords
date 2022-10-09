package de.unisaarland.cs.se.selab.systemtest.registration;

import de.unisaarland.cs.se.selab.comm.TimeoutException;
import de.unisaarland.cs.se.selab.systemtest.HelperClass;

public class RegisteredPlayerSendHireMonsterActionInRegistration extends HelperClass {


    public RegisteredPlayerSendHireMonsterActionInRegistration() {
        super(RegisteredPlayerSendRegistrationActionInRegistration.class, false);
    }

    @Override
    public void run() throws TimeoutException {
        register(2);
        sendHireMonster(0, 0);
        assertActionFailed(0);
        leave();
    }
}
