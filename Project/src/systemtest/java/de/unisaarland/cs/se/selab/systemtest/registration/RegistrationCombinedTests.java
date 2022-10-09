package de.unisaarland.cs.se.selab.systemtest.registration;

import de.unisaarland.cs.se.selab.comm.BidType;
import de.unisaarland.cs.se.selab.comm.TimeoutException;
import de.unisaarland.cs.se.selab.systemtest.HelperClass;

public class RegistrationCombinedTests extends HelperClass {

    public RegistrationCombinedTests() {
        super(RegistrationCombinedTests.class, false);
    }

    @Override
    public void run() throws TimeoutException {
        register(3);
        leave(2, 2);

        sendStartGame(2);

        sendActivateRoom(2, 5);
        sendActivateRoom(0, 0);
        assertActionFailed(0);

        sendHireMonster(2, 0);
        sendHireMonster(0, 0);
        assertActionFailed(0);


        sendRegister(1, "I have been registered!");
        assertActionFailed(1);


        sendBattleGround(2, 0, 0);
        sendBattleGround(0, 0, 0);
        assertActionFailed(0);


        sendBuildRoom(2, 0, 0, 0);
        sendBuildRoom(0, 0, 0, 0);
        assertActionFailed(0);


        sendDigTunnel(2, 0, 0);
        sendDigTunnel(0, 0, 0);
        assertActionFailed(0);


        sendEndTurn(2);
        sendEndTurn(0);
        assertActionFailed(0);


        sendMonster(2, 0);
        sendMonster(0, 0);
        assertActionFailed(0);


        sendMonsterTargeted(2, 0, 0);
        sendMonsterTargeted(0, 0, 0);
        assertActionFailed(0);


        sendPlaceBid(2, BidType.FOOD, 0);
        sendPlaceBid(0, BidType.FOOD, 0);
        assertActionFailed(0);

        sendTrap(2, 0);
        sendTrap(0, 0);
        assertActionFailed(0);


        register(2, 3);
        sendStartGame(0);
        gameStarted();
        player();
        leave();
    }
}
