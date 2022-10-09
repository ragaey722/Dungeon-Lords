package de.unisaarland.cs.se.selab.systemtest.registration;


import de.unisaarland.cs.se.selab.comm.TimeoutException;
import de.unisaarland.cs.se.selab.systemtest.HelperClass;


/**
 * Register 2 Players and Leave
 */
public class RegistrationTest extends HelperClass {

    public RegistrationTest() {
        super(RegistrationTest.class, false);
    }

    @Override
    public void run() throws TimeoutException {
        register(2);
        leave();
    }
}
