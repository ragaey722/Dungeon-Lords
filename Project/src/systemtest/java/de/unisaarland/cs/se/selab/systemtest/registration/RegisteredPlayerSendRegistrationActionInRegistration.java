package de.unisaarland.cs.se.selab.systemtest.registration;

import de.unisaarland.cs.se.selab.comm.TimeoutException;
import de.unisaarland.cs.se.selab.systemtest.HelperClass;

public class RegisteredPlayerSendRegistrationActionInRegistration extends HelperClass {


    public RegisteredPlayerSendRegistrationActionInRegistration() {
        super(RegisteredPlayerSendRegistrationActionInRegistration.class, false);
    }

    @Override
    public void run() throws TimeoutException {
        register(2);
        sendRegister(1, "I have been registered!");
        assertActionFailed(1);
        leave();
    }
}
