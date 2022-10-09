package de.unisaarland.cs.se.selab.systemtest.biddingphase;

import de.unisaarland.cs.se.selab.comm.TimeoutException;

public class AllPlayerLeftInMonsterOptEval extends TooManyPlayerBidsOnMonster {

    public AllPlayerLeftInMonsterOptEval() {
        super(AllPlayerLeftInMonsterOptEval.class, false);
    }

    @Override
    public void run() throws TimeoutException {
        final int numberOfPlayer = 4;
        registrationEventWrapper(numberOfPlayer);
        firstSeasonAllPlayerBidOnOne();

        //gold
        impsChanged(-1, 0);
        impsChanged(-1, 1);
        impsChanged(-1, 2);

        //trap
        goldChanged(-1, 2);
        trapAcquired(2, 26);

        trapAcquired(3, 6);

        goldChanged(-2, 0);
        trapAcquired(0, 19);
        trapAcquired(0, 5);

        //monster

        assertSelectMonster(0);
        assertActNow(0);
        sendActivateRoom(0, 5);
        assertActionFailed(0);
        assertActNow(0);
        sendHireMonster(0, 23);
        monsterHired(23, 0);

        leave();
    }

}
